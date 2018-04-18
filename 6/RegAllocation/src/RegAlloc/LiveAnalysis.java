package RegAlloc;

import java.util.*;
import Temp.*;
import Graph.*;
import FlowGraph.*;

public class LiveAnalysis{
	
	private static HashMap<String, LivenessGraph> livegs;
	
	// Set of live-in Temps in Node
	private HashMap<Node, TempList> in;
	
	// Set of live-out Temps in Node
	private HashMap<Node, TempList> out;	
	
	private FlowGraph cfg;
	
	private LivenessGraph liveGraph;
	
	public LiveAnalysis(FlowGraph g, String frameName) {
		cfg = g;
		liveGraph = createInterferGraph();
		if(livegs == null) {
			livegs = new HashMap<String, LivenessGraph>();
		}
		livegs.put(frameName, liveGraph);
	}

	public LivenessGraph liveGraph() {
		return this.liveGraph;
	}
	
	public static HashMap<String, LivenessGraph> getliveGs(){
		return livegs;
	}
	
	LivenessGraph createInterferGraph() {
		calcInOut();
		LivenessGraph g = new LivenessGraph();
		NodeList cfgNodes = cfg.nodes();
		while(cfgNodes != null) {
			Node n = cfgNodes.head;
			//if(!cfg.isMove(n)) {
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
				//}
			/*if(!cfg.isMove(n)) {
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
						if(src != dest) { 
							g.addEdge(src, dest);
						}
						liveOuts = liveOuts.tail;
					}
					nDefs = nDefs.tail; 
				}*/
			}
			cfgNodes = cfgNodes.tail;
		}
		return g;
	}
	
	public NodeList reverse(NodeList nodes) {
		  NodeList reverseNodes = null;
		  while (nodes != null) {
			  reverseNodes = new NodeList(nodes.head, reverseNodes);
			  nodes = nodes.tail;
		  }
		  return reverseNodes;
	  }
	
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
			lg.show(System.out);//, map);
		}
		
	}
	
}
