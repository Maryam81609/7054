package Graph;

import Assem.Instr;
public class Node {

    Graph mygraph;
    private Node(){}
    int mykey;
    private Instr instr;
    
    public Node(Graph g, Instr i) {
		mygraph=g; 
		instr = i;
		mykey= g.nodecount++;
		NodeList p = new NodeList(this, null);
		if (g.mylast==null)
			g.mynodes=g.mylast=p;
		else 
			g.mylast = g.mylast.tail = p;
    }

    public Instr instr() {
    	return this.instr;
    }
    
    NodeList succs;
    NodeList preds;
    public NodeList succ() {return succs;}
    public NodeList pred() {return preds;}
      NodeList cat(NodeList a, NodeList b) {
          if (a==null) return b;
	  else return new NodeList(a.head, cat(a.tail,b));
    }
    public NodeList adj() {return cat(succ(), pred());}

    int len(NodeList l) {
	int i=0;
	for(NodeList p=l; p!=null; p=p.tail) i++;
	return i;
    }

    public int inDegree() {return len(pred());}
    public int outDegree() {return len(succ());}
    public int degree() {return inDegree()+outDegree();} 

    public boolean goesTo(Node n) {
	return Graph.inList(n, succ());
    }

    public boolean comesFrom(Node n) {
	return Graph.inList(n, pred());
    }

    public boolean adj(Node n) {
	return goesTo(n) || comesFrom(n);
    }

    public String toString() {return String.valueOf(mykey);}
    
    public boolean equals(Node n) {
    	// They must have the same graph for this project, because it is being called from liveness that has only one cfg
    	// so no need to check this.mygraph == n.mygraph && 
    	if(this.mykey == n.mykey) {
    		return true;
    	}
    	return false;
    }

}
