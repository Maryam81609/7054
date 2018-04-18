package RegAlloc;

import java.util.HashMap;

import Graph.Node;
import Graph.NodeList;
import Temp.Temp;
import Temp.TempList;
import Temp.TempMap;

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
		// TODO Auto-generated method stub
		return this.tNode.get(temp);
	}

	@Override
	public Temp gtemp(Node node) {
		// TODO Auto-generated method stub
		return this.nTemp.get(node);
	}

	@Override
	public MoveList moves() {
		// TODO Auto-generated method stub
		return null;
	}

	public void show(java.io.PrintStream out) {//, TempMap map) {
		for (NodeList p=nodes(); p!=null; p=p.tail) {
		  Node n = p.head;
		  out.print(n.toString());
		  out.print("[" + gtemp(n).toString() + "]: ");
		  
		  /*for(TempList q=def(n); q!=null; q=q.tail) {
		     out.print(q.head.toString());
		     out.print(" ");
		  }
		  out.print(isMove(n) ? "<= " : "<- ");
		  for(TempList q=use(n); q!=null; q=q.tail) {
		     out.print(q.head.toString());
		     out.print(" ");
		  }*/
		  out.print("; goto ");
		  for(NodeList q=n.succ(); q!=null; q=q.tail) {
		     out.print(q.head.toString());
		     out.print(" ");
		  }
		  out.println();
		}
   }
}
