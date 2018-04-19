package RegAlloc;

import java.util.*;
import java.util.Map.Entry;
import Assem.Instr;
import Temp.*;
import Graph.*;
import Mips.MipsFrame;
import FlowGraph.*;

public class LiveAnalysis{
	
	public static HashMap<Temp, String> frameTempMap = null;
	
	private static HashMap<String, HashMap<Temp, String>> tempMaps = null;
	
	private final int regNum = 32;
	
	private static HashMap<String, LivenessGraph> livegs;
	
	// Set of live-in Temps in Node
	private HashMap<Node, TempList> in;
	
	// Set of live-out Temps in Node
	private HashMap<Node, TempList> out;	
	
	private FlowGraph cfg;
	
	private LivenessGraph liveGraph;
	
	private HashMap<Temp, String> tempMap;
	
	// A stack maintaining the nodes removed from the graph during the simplification phase
	private NodeList simplifiedNodes = null;
	
	// Represents which node in the simplifiedNodes stack is a potential spill
	private HashMap<Node, Boolean> potSpills;
	
	private ArrayList<Temp> inUseRegs;
	
	public LiveAnalysis(FlowGraph g, String frameName) {
		cfg = g;
		liveGraph = createInterferGraph();
		if(livegs == null) {
			livegs = new HashMap<String, LivenessGraph>();
		}
		livegs.put(frameName, liveGraph);
		
		color();
		
		if(tempMaps == null) {
			tempMaps = new HashMap<String, HashMap<Temp, String>>();
		}
		tempMaps.put(frameName, tempMap);
	}
	
	public static HashMap<Temp, String> getTempMap(String frameName){
		return tempMaps.get(frameName);
	}
	
	public static void printTempMaps() {	
		Iterator<Entry<String, HashMap<Temp, String>>> i = tempMaps.entrySet().iterator();
		while(i.hasNext()) {
			Entry<String, HashMap<Temp, String>> e = i.next();
			System.out.println("frame: " + e.getKey());		
			Iterator<Entry<Temp, String>> j = e.getValue().entrySet().iterator();
			while(j.hasNext()) {
				Entry<Temp, String> e2 = j.next();
				System.out.println(e2.getKey() + " : " + e2.getValue());
			}
		}
	}
	
	public void printTempMap() {		
		Iterator<Entry<Temp, String>> i = tempMap.entrySet().iterator();
		while(i.hasNext()) {
			Entry<Temp, String> e = i.next();
			System.out.println(e.getKey() + " : " + e.getValue());
		}
	}
	
	public LivenessGraph liveGraph() {
		return this.liveGraph;
	}
	
	public static HashMap<String, LivenessGraph> getliveGs(){
		return livegs;
	}
	
	/* Begin: Create Interference Graph */
	void calcInOut() {
		in = new HashMap<Node, TempList>();
		out = new HashMap<Node, TempList>();
	
		HashMap<Node, TempList> in1, out1;
		in1 = out1 = new HashMap<Node, TempList>();
	
		do {
			NodeList nodes = reverse(cfg.nodes());
			while(nodes != null) {
				Node n = nodes.head;
				TempList out_n = out.get(n);
				TempList in_n = in.get(n);
				in1.put(n, in_n);
				out1.put(n, out_n);
				TempList use_n = cfg.use(n);
				TempList def_n = cfg.def(n);
				TempList sub_out_def_n = subtract(out_n, def_n);
				TempList newIn = union(use_n, sub_out_def_n);
				TempList newOut = null;
				NodeList succs_n = n.succ();
				while(succs_n != null) {
					Node s = succs_n.head;
					newOut = union(newOut, in.get(s));
					succs_n = succs_n.tail;
				}
				in.put(n, newIn);
				out.put(n, newOut);
				nodes = nodes.tail;
			}	
		}while(!equal(in, in1) && !equal(out, out1));	
	}
	
	LivenessGraph createInterferGraph() {
		calcInOut();
		LivenessGraph g = new LivenessGraph();
		NodeList cfgNodes = cfg.nodes();
		while(cfgNodes != null) {
			Node n = cfgNodes.head;
			TempList nDefs = cfg.def(n);
			while(nDefs != null) {
				Temp varSrc = nDefs.head;
				Node src = g.tnode(varSrc);
				if(src == null) {
					src = g.newNode(varSrc);
				}
				TempList liveOuts = out.get(n);
				while(liveOuts != null) {
					Temp varDest = liveOuts.head;
					Node dest = g.tnode(varDest);
					if(dest == null) {
						dest = g.newNode(varDest);
					}
					if(src != dest && (!cfg.isMove(n) || (cfg.isMove(n) && !inList(varDest, n.instr().use())))) { 
						if(!src.adj(dest)) {
							g.addEdge(src, dest);
						}
					}
					liveOuts = liveOuts.tail;
				}
				nDefs = nDefs.tail; 
			}
			cfgNodes = cfgNodes.tail;
		}
		return g;
	}
	/* End: Create Interference Graph */
	
	/* Begin: Coloring */
	void color() {
		this.potSpills = new HashMap<Node, Boolean>();
		LivenessGraph gsimp = liveGraph;
		Node n;
		do {
			n = getInsignificantNotPrecolored(gsimp);
			if(n != null) {
				gsimp = simplify(gsimp);
			}
			n = getNotPrecolored(gsimp);
			if(n != null) {
				gsimp = spill(gsimp);
			} 
		} while(n != null);
		
		NodeList gsimp_nodes1 = gsimp.nodes();
		NodeList gsimp_nodes = gsimp_nodes1;
		inUseRegs = new ArrayList<Temp>();
		tempMap = new HashMap<Temp, String>();
		MipsFrame f = new MipsFrame();
		while(gsimp_nodes != null) {
			Node m = gsimp_nodes.head;
			Temp m_temp = gsimp.gtemp(m); 
			tempMap.put(m_temp, f.tempMap(m_temp));
			inUseRegs.add(m_temp); 
			gsimp_nodes = gsimp_nodes.tail;
		}
		select(gsimp);
	}
	
	/* Begin: Select */
	void select(LivenessGraph gsimp) {
		NodeList stack = simplifiedNodes;
		while(stack != null) {
			Node selectedNode = stack.head;
			if(!colorable(selectedNode, gsimp)) {
				throw new Error("**********SPILL**********");
			}
			Temp selectedReg = getaFreeReg();
			tempMap.put(gsimp.gtemp(selectedNode), (new MipsFrame()).tempMap(selectedReg));
			inUseRegs.add(selectedReg);			
			stack = stack.tail;
		}		
	}
	
	Temp getaFreeReg() {
		ArrayList<Temp> regs = MipsFrame.availableRegs();
		regs.removeAll(inUseRegs);
		return regs.get(0);
	}
	
	boolean colorable(Node node, LivenessGraph ig){
		NodeList node_adjs = node.adj(); 
		if(node.len(node_adjs) < regNum) {
			return true;
		}
		Set<String> usedRegs = new HashSet<String>();
		while(node_adjs != null) {
			Node adj = node_adjs.head;
			usedRegs.add(tempMap.get(ig.gtemp(adj)));
			node_adjs = node_adjs.tail;
		}
		return (usedRegs.size() < regNum);
	}
	
	/* End: Select */
	
	/* Begin: Spill */
	LivenessGraph spill(LivenessGraph g) {
		LivenessGraph gsimp = g;
		Node n = getNotPrecolored(gsimp);
		potSpills.put(n, true);
		simplifiedNodes = new NodeList(n, simplifiedNodes);
		NodeList n_adjs = n.adj();
		while(n_adjs != null) {
			Node adj = n_adjs.head;
			if(n.comesFrom(adj)) {
				gsimp.rmEdge(adj, n);
			}
			else if(n.goesTo(adj)) {
				gsimp.rmEdge(n, adj);
			}
			n_adjs = n_adjs.tail;
		}
		return gsimp;
	}
	
	Node getNotPrecolored(LivenessGraph g) {
		Node ret = null;
		NodeList nodes = g.nodes();
		while(nodes != null) {
			Node n = nodes.head;
			if(!g.isPrecolored(n) && n.degree() > 0) {
				ret = n;
				break;
			}
			nodes = nodes.tail;
		}
		return ret;
	}
	
	/* End: Spill */
	
	/* Begin: Simplify Interference Graph */
	
	LivenessGraph simplify(LivenessGraph g) {
		LivenessGraph ret = null;
		LivenessGraph gsimp = g;
		Node n = getInsignificantNotPrecolored(gsimp);
		while(n != null) {
			simplifiedNodes = new NodeList(n, simplifiedNodes);
			NodeList n_adjs = n.adj();
			while(n_adjs != null) {
				Node adj = n_adjs.head;
				if(n.comesFrom(adj)) {
					gsimp.rmEdge(adj, n);
				}
				else if(n.goesTo(adj)) {
					gsimp.rmEdge(n, adj);
				}
				n_adjs = n_adjs.tail;
			}
			n = getInsignificantNotPrecolored(gsimp);
		}
		ret = gsimp;
		return ret;
	}
	
	private Node getInsignificantNotPrecolored(LivenessGraph g) {
		Node ret = null;
		NodeList nodes = g.nodes();
		while(nodes != null) {
			Node n = nodes.head;
			if(!g.isPrecolored(n) && n.degree() > 0 && n.degree() < regNum) {
				ret = n;
				break;
			}
			nodes = nodes.tail;
		}
		return ret;
	}
	
	/* End: Simplify Interference Graph */
	/* End: Coloring */
	
	// Auxiliary Methods
	public NodeList reverse(NodeList nodes) {
		  NodeList reverseNodes = null;
		  while (nodes != null) {
			  reverseNodes = new NodeList(nodes.head, reverseNodes);
			  nodes = nodes.tail;
		  }
		  return reverseNodes;
	  }

	private boolean equal(TempList l1, TempList l2) {
		if(l1 == null && l2 == null) {
			return true;
		}
		else if(l1 == null || l2 == null) {
			return false;
		}
		TempList l1copy = l1;
		while(l1copy != null) {
			if(!inList(l1copy.head, l2)) {
				return false;
			}
			l1copy = l1copy.tail;
		}
		TempList l2copy = l2;
		while(l2copy != null) {
			if(!inList(l2copy.head, l1)) {
				return false;
			}
			l2copy = l2copy.tail;
		}
		return true;
	}
	
	private boolean inList(Temp a, TempList l) {
	       for(TempList p=l; p!=null; p=p.tail)
	             if (p.head==a) return true;
	       return false;
	  }
	
	// returns appends l2 to the end of l1
	private TempList union(TempList l1, TempList l2) {
		if (l1 == null) {
			return l2;
		}
		if(l2 == null) {
			return l1;
		}
		TempList ret = l1;
		TempList l2copy = l2;
		while(l2copy != null) {
			if(!inList(l2copy.head, ret)) {
				ret = new TempList(l2copy.head, ret);
			}
			l2copy = l2copy.tail;
		}
		return ret;
	}
	
	// returns l1-l2
	private TempList subtract(TempList l1, TempList l2) {	
		if(l2 == null) {
			return l1;
		}
		else if(l1 == null) {
			return null;
		}
		TempList res = null;
		boolean exists;
		TempList l1copy = l1;
		while(l1copy != null) {
			exists = false;
			Temp tl1 = l1copy.head;
			TempList l2copy = l2;
			while(l2copy != null) {
				if(tl1 == l2copy.head) {
					exists = true;
					break;
				}
				l2copy = l2copy.tail;
			}
			if(!exists) {
				res = new TempList(tl1, res);
			}
			l1copy = l1copy.tail;
		}
		return res;
	}
	
	private boolean equal(HashMap<Node, TempList> map1, HashMap<Node, TempList> map2) {
		Set<Node> map1keys = map1.keySet();
		Set<Node> map2keys = map2.keySet();
		if(map1keys.equals(map2keys)) {
			Iterator<Node> it = map1keys.iterator();
			while(it.hasNext()) {
				Node key = (Node)it.next();
				if(!equal(map1.get(key), map2.get(key))){
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	public static void show(TempMap map) {
		Iterator<Map.Entry<String, LivenessGraph>> it = livegs.entrySet().iterator();
		while(it.hasNext()) {
			LivenessGraph lg = ((Map.Entry<String, LivenessGraph>)it.next()).getValue();
			lg.show(System.out);
		}
	}
	
	public static void print (LinkedHashMap<String, ArrayList<Instr>> assemInstrs) {
		System.out.print("\t.text\n" + "\t.globl main\n");
		TempMap registerMap = new RegisterMap(); 
		Iterator<Entry<String, ArrayList<Instr>>> i = assemInstrs.entrySet().iterator();
		while(i.hasNext()) {
			Entry<String, ArrayList<Instr>> e = i.next();
			String frameName = e.getKey();
			ArrayList<Instr> frameInstrs = e.getValue();
			frameTempMap = tempMaps.get(frameName);
			for (Instr instr : frameInstrs) {
				System.out.println(instr.format(registerMap));
			}
		}
		frameTempMap = null;
		System.out.println("\n # MiniJava Library\n" + (new MipsFrame()).mjLibrary());
	}
	
}
