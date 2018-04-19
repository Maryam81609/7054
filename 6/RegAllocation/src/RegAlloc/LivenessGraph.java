package RegAlloc;

import java.util.HashMap;

import Graph.Node;
import Graph.NodeList;
import Temp.Temp;

public class LivenessGraph extends InterferenceGraph{

	HashMap<Node, Temp> nTemp;
	
	HashMap<Temp, Node> tNode;
	
	public LivenessGraph() {
		if(nTemp == null) {
			nTemp = new HashMap<Node, Temp>();
		}
		if(tNode == null) {
			tNode = new HashMap<Temp, Node>();
		}
	}
	
	public Node newNode(Temp t) {
		Node n = super.newNode(null);
		nTemp.put(n, t);
		tNode.put(t, n);
		return n;
	}
	
	@Override
	public Node tnode(Temp temp) {
		return this.tNode.get(temp);
	}

	@Override
	public Temp gtemp(Node node) {
		return this.nTemp.get(node);
	}

	@Override
	public MoveList moves() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean isPrecolored() {
		boolean ret = true;
		NodeList nodes = nodes();
		while(nodes != null) {
			if(!isPrecolored(nodes.head)) {
				ret = false;
				break;
			}
			nodes = nodes.tail;
		}
		return ret;
	}
	
	public boolean isPrecolored(Node node) {
		if(nTemp.get(node).getNum() < 32) {
			return true;
		}
		return false;
	}

	public void show(java.io.PrintStream out) {
		for (NodeList p=nodes(); p!=null; p=p.tail) {
		  Node n = p.head;
		  out.print(n.toString());
		  out.print("[" + gtemp(n).toString() + "]: ");

		  out.print("; goto ");
		  for(NodeList q=n.succ(); q!=null; q=q.tail) {
		     out.print(q.head.toString());
		     out.print(" ");
		  }
		  out.println();
		}
   }
}
