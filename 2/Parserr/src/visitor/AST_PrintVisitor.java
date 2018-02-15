package visitor;

import syntaxtree.*;

public class AST_PrintVisitor implements Visitor {
    public int depth;
    public AST_PrintVisitor() { depth = 0; }

    public void visit(Program n){
	indent();

	System.out.println("Program(");
	++depth;
	n.m.accept(this);
	for(int i=0; i<n.cl.size(); i++){
	    print_comma_end();
	    n.cl.elementAt(i).accept(this);
	}
	System.out.println();
	--depth;
	indent(); print_rparen_end();
	System.out.println();
    }

    public void visit(MainClass m){
	indent();

	System.out.println("MainClass(");
	++depth;

	m.i1.accept(this);
	print_comma_end();

	m.i2.accept(this);
	print_comma_end();

	m.s.accept(this);

	System.out.println();
	--depth;
	indent(); print_rparen_end();
    }

    public void visit(ClassDeclSimple c){
	indent();

	System.out.println("ClassDeclSimple(");
	++depth;

	c.i.accept(this);

	for(int i=0; i<c.vl.size(); i++){
	    print_comma_end();
	    c.vl.elementAt(i).accept(this);
	}

	for(int i=0; i<c.ml.size(); i++){
	    print_comma_end();
	    c.ml.elementAt(i).accept(this);
	}

	System.out.println();
	--depth;
	indent(); print_rparen_end();
    }

    public void visit(ClassDeclExtends c){
	indent();

	System.out.println("ClassDeclExtends(");
	++depth;

	c.i.accept(this);
	print_comma_end();

	c.j.accept(this);

	for(int i=0; i<c.vl.size(); i++){
	    print_comma_end();
	    c.vl.elementAt(i).accept(this);
	}

	for(int i=0; i<c.ml.size(); i++){
	    print_comma_end();
	    c.ml.elementAt(i).accept(this);
	}

	System.out.println();
	--depth;
	indent(); print_rparen_end();
    }

    public void visit(VarDecl v){
	indent();

	System.out.println("VarDecl(");
	++depth;

	v.t.accept(this);
	print_comma_end();

	v.i.accept(this);
	System.out.println();

	--depth;
	indent(); print_rparen_end();
    }

    public void visit(MethodDecl m){
	indent();

	System.out.println("MethodDecl(");
	++depth;

	m.t.accept(this);
	print_comma_end();

	m.i.accept(this);

	for(int i=0; i<m.fl.size(); i++){
	    print_comma_end();
	    m.fl.elementAt(i).accept(this);
	}

	for(int i=0; i<m.vl.size(); i++){
	    print_comma_end();
	    m.vl.elementAt(i).accept(this);
	}
	    
	for(int i=0; i<m.sl.size(); i++){
	    print_comma_end();
	    m.sl.elementAt(i).accept(this);
	}

	print_comma_end();
	m.e.accept(this);

	System.out.println();
	--depth;
	indent(); print_rparen_end();
    }

    public void visit(Formal f){
	indent();
	System.out.println("Formal(");
	++depth;

	f.t.accept(this);
	print_comma_end();

	f.i.accept(this);
	print_comma_end();

	System.out.println();
	--depth;
	indent(); print_rparen_end();
    }

    public void visit(IntArrayType i){
	indent();
	System.out.print("Type(int[])");
    }

    public void visit(BooleanType b){
	indent();
	System.out.print("Type(boolean)");
    }

    public void visit(IntegerType i){
	indent();
	System.out.print("Type(int)");
    }

    public void visit(IdentifierType i){
	indent();
	System.out.print("Type("+i.s+")");
    }

    public void visit(Block b){
	indent();
	System.out.println("Block(");
	++depth;

	int i;
	for(i=0; i<b.sl.size()-1; i++){
	    b.sl.elementAt(i).accept(this);
	    print_comma_end();
	}
	
	b.sl.elementAt(i).accept(this);
	
	--depth;
	indent(); print_rparen_end();
    }

    public void visit(If i){
	indent();
	System.out.println("If(");
	++depth;

	i.e.accept(this);
	print_comma_end();

	i.s1.accept(this);
	print_comma_end();

	i.s2.accept(this);
	System.out.println();

	--depth;
	indent(); print_rparen_end();
    }

    public void visit(While w){
	indent();
	System.out.println("While(");
	++depth;

	w.e.accept(this);
	print_comma_end();

	w.s.accept(this);

	--depth;
	indent(); print_rparen_end();
    }

    public void visit(Print p){
	indent();
	System.out.println("Print(");
	++depth;

	p.e.accept(this);
	System.out.println();
	
	--depth;
	indent(); print_rparen_end();
    }

    public void visit(Assign a){
	indent();
	System.out.println("Assign(");
	++depth;

	a.i.accept(this);
	print_comma_end();

	a.e.accept(this);
	System.out.println();

	--depth;
	indent(); print_rparen_end();
    }

    public void visit(ArrayAssign a){
	indent();
	System.out.println("ArrayAssign(");
	++depth;

	a.i.accept(this);
	print_comma_end();

	a.e1.accept(this);
	print_comma_end();

	a.e2.accept(this);
	System.out.println();

	--depth;
	indent(); print_rparen_end();
    }

    public void visit(And a){
	indent();
	System.out.println("And(");
	++depth;

	a.e1.accept(this);
	print_comma_end();

	a.e2.accept(this);
	System.out.println();

	--depth;
	indent(); print_rparen_end();
    }

    public void visit(LessThan l){
	indent();
	System.out.println("LessThan(");
	++depth;

	l.e1.accept(this);
	print_comma_end();

	l.e2.accept(this);
	System.out.println();

	--depth;
	indent(); print_rparen_end();
    }
	
    public void visit(Plus p){
	indent();
	System.out.println("Plus(");
	++depth;

	p.e1.accept(this);
	print_comma_end();

	p.e2.accept(this);
	System.out.println();

	--depth;
	indent(); print_rparen_end();
    }

    public void visit(Minus m){
	indent();
	System.out.println("Minus(");
	++depth;

	m.e1.accept(this);
	print_comma_end();

	m.e2.accept(this);
	System.out.println();

	--depth;
	indent(); print_rparen_end();
    }

    public void visit(Times t){
	indent();
	System.out.println("Times(");
	++depth;

	t.e1.accept(this);
	print_comma_end();

	t.e2.accept(this);
	System.out.println();

	--depth;
	indent(); print_rparen_end();
    }

    public void visit(ArrayLookup a){
	indent();
	System.out.println("ArrayLookup(");
	++depth;

	a.e1.accept(this);
	print_comma_end();

	a.e2.accept(this);
	System.out.println();

	--depth;
	indent(); print_rparen_end();
    }

    public void visit(ArrayLength a){
	indent();
	System.out.println("ArrayLength(");
	++depth;

	a.e.accept(this);
	System.out.println();

	--depth;
	indent(); print_rparen_end();
    }

    public void visit(Call c){
	indent();
	System.out.println("Call(");
	++depth;

	c.e.accept(this);

	print_comma_end();
	c.i.accept(this);

	for(int i=0; i<c.el.size(); i++){
	    print_comma_end();
	    c.el.elementAt(i).accept(this);
	}

	System.out.println();

	--depth;
	indent(); print_rparen_end();
    }

    public void visit(IntegerLiteral i){
	indent();
	System.out.print("IntegerLiteral("+i.i+")");
    }

    public void visit(True t){
	indent();
	System.out.print("True()");
    }

    public void visit(False f){
	indent();
	System.out.print("False()");
    }

    public void visit(IdentifierExp i){
	indent();
	System.out.print("IdentifierExp("+i.s+")");
    }

    public void visit(This t){
	indent();
	System.out.print("This()");
    }

    public void visit(NewArray n){
	indent();
	System.out.println("NewArray(");
	++depth;
	
	n.e.accept(this);
	System.out.println();

	--depth;
	indent(); print_rparen_end();
    }

    public void visit(NewObject n){
	indent();
	System.out.println("NewObject(");
	++depth;

	n.i.accept(this);
	System.out.println();

	--depth;
	indent(); print_rparen_end();
    }

    public void visit(Not n){
	indent();
	System.out.println("Not(");
	++depth;

	n.e.accept(this);
	System.out.println();
	
	--depth;
	indent(); print_rparen_end();
    }

    public void visit(Identifier i){
	indent();
	System.out.print("Identifer("+i.s+")");
    }

    private void indent(){
	for(int i=0; i<depth; i++){
	    System.out.print(" ");
	}
    }

    private void print_comma_end(){
	System.out.println(",");
    }

    private void print_rparen_end(){
	System.out.print(")");
    }
}
