package FlowGraph;

import java.util.HashMap;

import Graph.*;
import Temp.Label;
import Temp.Temp;
import Temp.TempList;
import Assem.*;

public class CFG extends FlowGraph {
	
	@Override
	public TempList def(Node node) {
		// TODO Auto-generated method stub
		return node.instr().def();
	}

	@Override
	public TempList use(Node node) {
		// TODO Auto-generated method stub
		return node.instr().use();
	}

	@Override
	public boolean isMove(Node node) {
		// TODO Auto-generated method stub
		if(node.instr() instanceof MOVE) {
			return true;
		}
		else {
			return false;
		}
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
	
	  public Node getNode(Label l, NodeList nodes) {
		  Node ret = null;
		  if(nodes != null) {
			  Node n = nodes.head;
			  if((n.instr() instanceof LABEL) && ((LABEL)n.instr()).label.toString() == l.toString()) {
				  ret = n;
			  }
			  else {
				  ret = getNode(l, nodes.tail);
			  }
		  }
		  return ret;
	  }
	  
	  public Node getNode(Label l) {
		  return this.getNode(l, this.nodes());	  
	  }

}
