package FlowGraph;

import Graph.*;
import Temp.TempList;

public class CFG extends FlowGraph {

	@Override
	public TempList def(Node node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TempList use(Node node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isMove(Node node) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	public void print(java.io.PrintStream out) {
		NodeList nodes = this.nodes();
		while(nodes != null) {
			Node n = nodes.head;
			NodeList succs = n.succ();
			while(succs != null) {
				
			}
		}
	}

}
