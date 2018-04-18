package FlowGraph;

import java.util.*;
import Graph.*;
import Temp.TempList;
import Temp.TempMap;
import Assem.*;
import Temp.LabelList;
import visitor.DotVisitor;

public class CFGGenerator {
	
	static HashMap<String, FlowGraph> cfgs;
	
	String currFrameName;
	CFG currGraph;
	
	public CFGGenerator(String frameName) {
		//currGraph = new CFG();
		currFrameName = frameName;
		currGraph = new CFG();
		
		if (cfgs == null) {
			cfgs = new HashMap<String, FlowGraph>();
		}
		cfgs.put(frameName, currGraph);
	} 
	
	public CFG getCFG() {
		return currGraph;
	}
	
	public void createNodes(InstrList instrList) {
		
		InstrList il = instrList;		
		while(il != null) {
			Instr i = il.head;
			//Node n = 
			currGraph.newNode(i);			
			il = il.tail;
		}
	}
	
	public void addEdges() {
		NodeList nodes = currGraph.nodes();
		NodeList nodes1 = nodes;
		while(nodes1 != null) {
			Node node = nodes1.head;
			Targets jumps = node.instr().jumps();
			if(!node.instr().assem.startsWith("\tjal") && jumps != null) {
				LabelList labels = jumps.labels;
				while(labels != null) {
					Node dstNode = currGraph.getNode(labels.head);
					if(dstNode != null) {
						currGraph.addEdge(node, dstNode);
					}
					labels = labels.tail;
				}
			}
			else if(nodes1.tail != null) {
				currGraph.addEdge(node, nodes1.tail.head);
			}
			nodes1 = nodes1.tail;
		}
	}
	
	public FlowGraph getCFG(String frameName) {
		return cfgs.get(frameName);
	}
	
	public static void show(TempMap map) {
		Iterator<Map.Entry<String, FlowGraph>> it = cfgs.entrySet().iterator();
		while(it.hasNext()) {
			FlowGraph cfg = ((Map.Entry<String, FlowGraph>)it.next()).getValue();
			cfg.show(System.out, map);
		}
		
	}

}
