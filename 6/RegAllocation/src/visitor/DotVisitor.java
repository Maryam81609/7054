package visitor;

/**
 * Created by Brycen Ainge 1/30/2007
 * 
 * This will traverse your AST and make a .dot file out of it.
 * To use, add this code to your main method where p is the Program
 * object that is the root of you tree and filename is the name of
 * the file you want to output to (should end in .dot):
 * 
 * new visitor.DotVisitor().writeDotFile("filename.dot", p);
 * 
 * After, use a tool to convert the dot file to a gif.  One program
 * is installed in the CADE lab that will do this.  Use this command:
 * 
 * dot -Tgif -ofile.gif filename.dot
 * 
 * Where file.gif is the output file and filename.dot is the source
 * dot file.
 * 
 * UPDATED 1/31/2007 - Added List nodes, fixed Class ordering (vars before methods),
 *   made it so calling any visit method directly will cause a NullPointerException
 *   to be thrown, should now read null lists and empty lists the same 
 * 
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import syntaxtree.*;

public class DotVisitor implements Visitor {
	
	BufferedWriter out;
	static int i = 0;
		
	public void writeDotFile(String file, Program p) {
		try {
			out = new BufferedWriter(new FileWriter(file));
			out.write("graph AST {\n");
			visit(p);
			out.write("}");
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		out = null;
	}

	public void visit(Program n) {
		int j = i;
		i++;
		try {
			out.write("Node" + j + " [label=\"Program\"];\n");
			out.write("Node" + j + " -- Node" + i + ";\n");
			n.m.accept(this);
			out.write("Node" + j + " -- Node" + i + ";\n");
			visit(n.cl);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void visit(MainClass n) {
		int j = i;
		i++;
		try {
			out.write("Node" + j + " [label=\"MainClass\"];\n");
			out.write("Node" + j + " -- Node" + i + ";\n");
			n.i1.accept(this);
			out.write("Node" + j + " -- Node" + i + ";\n");
			n.i2.accept(this);
			out.write("Node" + j + " -- Node" + i + ";\n");
			n.s.accept(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void visit(ClassDeclSimple n) {
		int j = i;
		i++;
		try {
			out.write("Node" + j + " [label=\"ClassDeclSimple\"];\n");
			out.write("Node" + j + " -- Node" + i + ";\n");
			n.i.accept(this);
			out.write("Node" + j + " -- Node" + i + ";\n");
			visit(n.vl);
			out.write("Node" + j + " -- Node" + i + ";\n");
			visit(n.ml);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

	public void visit(ClassDeclExtends n) {
		int j = i;
		i++;
		try {
			out.write("Node" + j + "[label=\"ClassDeclExtends\"];\n");
			out.write("Node" + j + " -- Node" + i + ";\n");
			n.i.accept(this);
			out.write("Node" + j + " -- Node" + i + ";\n");
			n.j.accept(this);
			out.write("Node" + j + " -- Node" + i + ";\n");
			visit(n.vl);
			out.write("Node" + j + " -- Node" + i + ";\n");
			visit(n.ml);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

	public void visit(VarDecl n) {
		int j = i;
		i++;
		try {
			out.write("Node" + j + "[label=\"VarDecl\"];\n");
			out.write("Node" + j + " -- Node" + i + ";\n");
			n.t.accept(this);
			out.write("Node" + j + " -- Node" + i + ";\n");
			n.i.accept(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

	public void visit(MethodDecl n) {
		int j = i;
		i++;
		try {
			out.write("Node" + j + "[label=\"MethodDecl\"];\n");
			out.write("Node" + j + " -- Node" + i + ";\n");
			n.t.accept(this);
			out.write("Node" + j + " -- Node" + i + ";\n");
			n.i.accept(this);
			out.write("Node" + j + " -- Node" + i + ";\n");
			visit(n.fl);
			out.write("Node" + j + " -- Node" + i + ";\n");
			visit(n.vl);
			out.write("Node" + j + " -- Node" + i + ";\n");
			visit(n.sl);
			out.write("Node" + j + " -- Node" + i + ";\n");
			n.e.accept(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

	public void visit(Formal n) {
		int j = i;
		i++;
		try {
			out.write("Node" + j + "[label=\"Formal\"];\n");
			out.write("Node" + j + " -- Node" + i + ";\n");
			n.t.accept(this);
			out.write("Node" + j + " -- Node" + i + ";\n");
			n.i.accept(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

	public void visit(IntArrayType n) {
		int j = i;
		i++;
		try {
			out.write("Node" + j + "[label=\"IntArrayType\"];\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

	public void visit(BooleanType n) {
		int j = i;
		i++;
		try {
			out.write("Node" + j + "[label=\"BooleanType\"];\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

	public void visit(IntegerType n) {
		int j = i;
		i++;
		try {
			out.write("Node" + j + "[label=\"IntegerType\"];\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

	public void visit(IdentifierType n) {
		int j = i;
		i++;
		try {
			out.write("Node" + j + "[label=\"IdentifierType (" + n.s + ")\"];\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

	public void visit(Block n) {
		int j = i;
		i++;
		try {
			out.write("Node" + j + "[label=\"Block\"];\n");
			out.write("Node" + j + " -- Node" + i + ";\n");
			visit(n.sl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

	public void visit(If n) {
		int j = i;
		i++;
		try {
			out.write("Node" + j + "[label=\"If\"];\n");
			out.write("Node" + j + " -- Node" + i + ";\n");
			n.e.accept(this);
			out.write("Node" + j + " -- Node" + i + ";\n");
			n.s1.accept(this);
			out.write("Node" + j + " -- Node" + i + ";\n");
			n.s2.accept(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

	public void visit(While n) {
		int j = i;
		i++;
		try {
			out.write("Node" + j + "[label=\"While\"];\n");
			out.write("Node" + j + " -- Node" + i + ";\n");
			n.e.accept(this);
			out.write("Node" + j + " -- Node" + i + ";\n");
			n.s.accept(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

	public void visit(Print n) {
		int j = i;
		i++;
		try {
			out.write("Node" + j + "[label=\"Print\"];\n");
			out.write("Node" + j + " -- Node" + i + ";\n");
			n.e.accept(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

	public void visit(Assign n) {
		int j = i;
		i++;
		try {
			out.write("Node" + j + "[label=\"Assign\"];\n");
			out.write("Node" + j + " -- Node" + i + ";\n");
			n.i.accept(this);
			out.write("Node" + j + " -- Node" + i + ";\n");
			n.e.accept(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

	public void visit(ArrayAssign n) {
		int j = i;
		i++;
		try {
			out.write("Node" + j + "[label=\"ArrayAssign\"];\n");
			out.write("Node" + j + " -- Node" + i + ";\n");
			n.i.accept(this);
			out.write("Node" + j + " -- Node" + i + ";\n");
			n.e1.accept(this);
			out.write("Node" + j + " -- Node" + i + ";\n");
			n.e2.accept(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

	public void visit(And n) {
		int j = i;
		i++;
		try {
			out.write("Node" + j + "[label=\"And\"];\n");
			out.write("Node" + j + " -- Node" + i + ";\n");
			n.e1.accept(this);
			out.write("Node" + j + " -- Node" + i + ";\n");
			n.e2.accept(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

	public void visit(LessThan n) {
		int j = i;
		i++;
		try {
			out.write("Node" + j + "[label=\"LessThan\"];\n");
			out.write("Node" + j + " -- Node" + i + ";\n");
			n.e1.accept(this);
			out.write("Node" + j + " -- Node" + i + ";\n");
			n.e2.accept(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

	public void visit(Plus n) {
		int j = i;
		i++;
		try {
			out.write("Node" + j + "[label=\"Plus\"];\n");
			out.write("Node" + j + " -- Node" + i + ";\n");
			n.e1.accept(this);
			out.write("Node" + j + " -- Node" + i + ";\n");
			n.e2.accept(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

	public void visit(Minus n) {
		int j = i;
		i++;
		try {
			out.write("Node" + j + "[label=\"Minus\"];\n");
			out.write("Node" + j + " -- Node" + i + ";\n");
			n.e1.accept(this);
			out.write("Node" + j + " -- Node" + i + ";\n");
			n.e2.accept(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

	public void visit(Times n) {
		int j = i;
		i++;
		try {
			out.write("Node" + j + "[label=\"Times\"];\n");
			out.write("Node" + j + " -- Node" + i + ";\n");
			n.e1.accept(this);
			out.write("Node" + j + " -- Node" + i + ";\n");
			n.e2.accept(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

	public void visit(ArrayLookup n) {
		int j = i;
		i++;
		try {
			out.write("Node" + j + "[label=\"ArrayLookup\"];\n");
			out.write("Node" + j + " -- Node" + i + ";\n");
			n.e1.accept(this);
			out.write("Node" + j + " -- Node" + i + ";\n");
			n.e2.accept(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

	public void visit(ArrayLength n) {
		int j = i;
		i++;
		try {
			out.write("Node" + j + "[label=\"ArrayLength\"];\n");
			out.write("Node" + j + " -- Node" + i + ";\n");
			n.e.accept(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

	public void visit(Call n) {
		int j = i;
		i++;
		try {
			out.write("Node" + j + "[label=\"Call\"];\n");
			out.write("Node" + j + " -- Node" + i + ";\n");
			n.e.accept(this);
			out.write("Node" + j + " -- Node" + i + ";\n");
			n.i.accept(this);
			out.write("Node" + j + " -- Node" + i + ";\n");
			visit(n.el);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

	public void visit(IntegerLiteral n) {
		int j = i;
		i++;
		try {
			out.write("Node" + j + "[label=\"IntegerLiteral (" + n.i + ")\"];\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

	public void visit(True n) {
		int j = i;
		i++;
		try {
			out.write("Node" + j + "[label=\"True\"];\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

	public void visit(False n) {
		int j = i;
		i++;
		try {
			out.write("Node" + j + "[label=\"False\"];\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

	public void visit(IdentifierExp n) {
		int j = i;
		i++;
		try {
			out.write("Node" + j + "[label=\"IdentifierExpression (" + n.s + ")\"];\n");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

	public void visit(This n) {
		int j = i;
		i++;
		try {
			out.write("Node" + j + "[label=\"This\"];\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

	public void visit(NewArray n) {
		int j = i;
		i++;
		try {
			out.write("Node" + j + "[label=\"NewArray\"];\n");
			out.write("Node" + j + " -- Node" + i + ";\n");
			n.e.accept(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

	public void visit(NewObject n) {
		int j = i;
		i++;
		try {
			out.write("Node" + j + "[label=\"NewObject\"];\n");
			out.write("Node" + j + " -- Node" + i + ";\n");
			n.i.accept(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

	public void visit(Not n) {
		int j = i;
		i++;
		try {
			out.write("Node" + j + "[label=\"Not\"];\n");
			out.write("Node" + j + " -- Node" + i + ";\n");
			n.e.accept(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}

	public void visit(Identifier n) {
		int j = i;
		i++;
		try {
			out.write("Node" + j + "[label=\"Identifier (" + n.s + ")\"];\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}
	
	private void visit(ClassDeclList n) {
		int j = i;
		i++;
		try {
			out.write("Node" + j + " [label=\"ClassDeclList\"];\n");
			if (n == null) return;
			for (int a = 0; a < n.size(); a++) {
				out.write("Node" + j + " -- Node" + i + ";\n");
				n.elementAt(a).accept(this);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void visit(ExpList n) {
		int j = i;
		i++;
		try {
			out.write("Node" + j + " [label=\"ExpList\"];\n");
			if (n == null) return;
			for (int a = 0; a < n.size(); a++) {
				out.write("Node" + j + " -- Node" + i + ";\n");
				n.elementAt(a).accept(this);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void visit(FormalList n) {
		int j = i;
		i++;
		try {
			out.write("Node" + j + " [label=\"FormalList\"];\n");
			if (n == null) return;
			for (int a = 0; a < n.size(); a++) {
				out.write("Node" + j + " -- Node" + i + ";\n");
				n.elementAt(a).accept(this);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void visit(MethodDeclList n) {
		int j = i;
		i++;
		try {
			out.write("Node" + j + " [label=\"MethodDeclList\"];\n");
			if (n == null) return;
			for (int a = 0; a < n.size(); a++) {
				out.write("Node" + j + " -- Node" + i + ";\n");
				n.elementAt(a).accept(this);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void visit(StatementList n) {
		int j = i;
		i++;
		try {
			out.write("Node" + j + " [label=\"StatementList\"];\n");
			if (n == null) return;
			for (int a = 0; a < n.size(); a++) {
				out.write("Node" + j + " -- Node" + i + ";\n");
				n.elementAt(a).accept(this);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void visit(VarDeclList n) {
		int j = i;
		i++;
		try {
			out.write("Node" + j + " [label=\"VarDeclList\"];\n");
			if (n == null) return;
			for (int a = 0; a < n.size(); a++) {
				out.write("Node" + j + " -- Node" + i + ";\n");
				n.elementAt(a).accept(this);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}