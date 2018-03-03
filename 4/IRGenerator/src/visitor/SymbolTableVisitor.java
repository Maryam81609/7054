package visitor;

import syntaxtree.*;
import symbol.*;
import error.*;

public class SymbolTableVisitor implements Visitor {
	
	private SymbolTable table;
	private ClassTable currClass;
	private MethodTable currMethod;
	
	public SymbolTable getTable() {
		return this.table;
	}
	
	public SymbolTableVisitor() {
		table = new SymbolTable();
		currClass = null;
		currMethod = null;
	}
	
	@Override
	public void visit(Program n) {
		n.m.accept(this);
		for(int i = 0; i < n.cl.size(); i++)
			n.cl.elementAt(i).accept(this);
	}

	@Override
	public void visit(MainClass n) {
		currClass = new ClassTable(n.i1.toString());
		currMethod = new MethodTable("main", new IdentifierType("void"));
		currMethod.addLocal(n.i2.toString(), new IntArrayType());
		currClass.addMethod("main", currMethod);
		table.addClass(n.i1.toString(), currClass);
		currMethod = null;
		currClass = null;
	}

	@Override
	public void visit(ClassDeclSimple n) {
		currClass = new ClassTable(n.i.toString());
		for(int i = 0; i < n.vl.size(); i++)
			n.vl.elementAt(i).accept(this);
		for(int i = 0; i < n.ml.size(); i++)
			n.ml.elementAt(i).accept(this);
		if(!table.addClass(n.i.toString(), currClass))
				ErrorMsg.duplicateClass(n.i.toString());
		currClass = null;
	}

	@Override
	public void visit(ClassDeclExtends n) {  // do you need to do this?
		currClass = new ClassTable(n.i.toString());
		for(int i = 0; i < n.vl.size(); i++)
			n.vl.elementAt(i).accept(this);
		for(int i = 0; i < n.ml.size(); i++)
			n.ml.elementAt(i).accept(this);
		if(!table.addClass(n.i.toString(), currClass))
				ErrorMsg.duplicateClass(n.i.toString());
		currClass = null;	
	}

	@Override
	public void visit(VarDecl n) {
		if(currMethod != null) {
			if(!currMethod.addLocal(n.i.toString(), n.t))
				ErrorMsg.duplicateVar(n.i.toString(), currClass.getName(), currMethod.getName());
		}
		else {
			if(!currClass.addVar(n.i.toString(), n.t))
				ErrorMsg.duplicateFieldVar(n.i.toString(), currClass.getName());
		}
	}

	@Override
	public void visit(MethodDecl n) {
		if(currClass != null) {
			currMethod = new MethodTable(n.i.toString(), n.t);
			for(int i = 0; i < n.fl.size(); i++)
				n.fl.elementAt(i).accept(this);
			for(int i = 0; i < n.vl.size(); i++)
				n.vl.elementAt(i).accept(this);
			if(!currClass.addMethod(n.i.toString(), currMethod)) 
				ErrorMsg.duplicateMethod(n.i.toString(), currClass.getName());
			currMethod = null;
		}
	}

	@Override
	public void visit(Formal n) {
		if(currMethod != null) {
			if(!currMethod.addParam(n.i.toString(), n.t))
				ErrorMsg.duplicateVar(n.i.toString(), currClass.getName(), currMethod.getName());
		}
	}

	@Override
	public void visit(IntArrayType n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(BooleanType n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(IntegerType n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(IdentifierType n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Block n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(If n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(While n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Print n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Assign n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ArrayAssign n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(And n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(LessThan n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Plus n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Minus n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Times n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ArrayLookup n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ArrayLength n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Call n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(IntegerLiteral n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(True n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(False n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(IdentifierExp n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(This n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(NewArray n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(NewObject n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Not n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Identifier n) {
		// TODO Auto-generated method stub
		
	}

}