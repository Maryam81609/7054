package Translate;

import syntaxtree.And;
import syntaxtree.ArrayAssign;
import syntaxtree.ArrayLength;
import syntaxtree.ArrayLookup;
import syntaxtree.Assign;
import syntaxtree.Block;
import syntaxtree.BooleanType;
import syntaxtree.Call;
import syntaxtree.ClassDeclExtends;
import syntaxtree.ClassDeclSimple;
import syntaxtree.False;
import syntaxtree.Formal;
import syntaxtree.Identifier;
import syntaxtree.IdentifierExp;
import syntaxtree.IdentifierType;
import syntaxtree.If;
import syntaxtree.IntArrayType;
import syntaxtree.IntegerLiteral;
import syntaxtree.IntegerType;
import syntaxtree.LessThan;
import syntaxtree.MainClass;
import syntaxtree.MethodDecl;
import syntaxtree.Minus;
import syntaxtree.NewArray;
import syntaxtree.NewObject;
import syntaxtree.Not;
import syntaxtree.Plus;
import syntaxtree.Print;
import syntaxtree.Program;
import syntaxtree.This;
import syntaxtree.Times;
import syntaxtree.True;
import syntaxtree.VarDecl;
import syntaxtree.While;
import visitor.ExpVisitor;

import java.util.HashMap;

import Frame.Access;
import Tree.BINOP;
import Tree.TEMP;

public class Translate implements ExpVisitor
{

  /* linked list of accumulated procedure fragments */
  private Frag                     frags     = null;
  private Frag                     frags_tail = null;

  /* current frame */
  private Frame.Frame              currFrame = null;

  /* pointer to address in heap at which instance variables are 
   stored, for current class (i.e., "this") */
  private Tree.Exp                 objPtr    = null;

  /* current offset from pointer to class object 
   (set to 0 in visit(ClassDecl), used in visit(VarDecl)) */
  private int                      offset;

  /* hashtable of field (instance) variables--key is variable name 
   string, value is offset from pointer to class object */
  private HashMap<String, Integer> fieldVars = null;

  /* hashtable of local and formal variables--key is variable name
   string, value is Access object to describe location of 
   variable (InFrame or InReg) */
  private HashMap<String, Access>  vars      = null;
  
  /* used in newobject */
  private symbol.SymbolTable symTbl =			null;
  
  /* constructor: set currFrame and start visitor */
  public Translate(Program p, Frame.Frame f, symbol.SymbolTable st)
  {
	symTbl = st;  
    currFrame = f;
    p.accept(this);
  }

  /* add function with frame=currFrame and body to list of 
   ProcFrag objects, frags. */
  public void procEntryExit(Tree.Stm body)
  {
    ProcFrag newfrag = new ProcFrag(body, currFrame);
    if (frags == null)
      frags = newfrag;
    else
      frags_tail.next = newfrag;
    frags_tail = newfrag;
    
    /* original code
    newfrag.next = frags;
    frags = newfrag; */
  }

  /* public method for retrieving linked list of accumulated 
   procedure fragments */
  public Frag getResults()
  {
    return frags;
  }

  /* public method for printing body (IR tree) of each procedure */
  public void printResults()
  {
    Tree.Print p = new Tree.Print(System.out);
    Frag f = frags;
    while (f != null)
      {
        System.out.println();
        System.out.println("Function: " + ((ProcFrag) f).frame.name.toString());
        p.prStm(((ProcFrag) f).body);
        f = f.next;
      }
  }

  /* simply visit all class declarations */
  public Exp visit(Program n)
  {
    n.m.accept(this);
    for (int i = 0; i < n.cl.size(); i++)
      n.cl.elementAt(i).accept(this);
    return null;
  }

  /* setup new frame and visit statement in body */
  public Exp visit(MainClass n)
  {
    /* setup new frame for main method */
    Frame.Frame newFrame = currFrame.newFrame("main", 1);
    Frame.Frame oldFrame = currFrame;
    currFrame = newFrame;

    /* visit single statement in method body */
    Tree.Stm s = (n.s.accept(this)).unNx();

    /* there is no return expression, so return 0
     then create Tree.MOVE to store return value */
    Tree.Exp retExp = new Tree.CONST(0);
    Tree.Stm body = new Tree.MOVE(new Tree.TEMP(currFrame.RV()), new Tree.ESEQ(s, retExp));

    /* create new procedure fragment for method and add to list */
    procEntryExit(body);
    currFrame = oldFrame;

    return null;
  }

  /* prepare HashMap fieldVars so we can add to it in visit(VarDecl),
   set back to null at end */
  public Exp visit(ClassDeclSimple n)
  {
	  
    fieldVars = new HashMap<String, Integer>();
    offset = -1 * currFrame.wordSize(); 
    for (int i = 0; i < n.vl.size(); i++)
      n.vl.elementAt(i).accept(this);
    for (int i = 0; i < n.ml.size(); i++)
      n.ml.elementAt(i).accept(this);
    fieldVars = null;
    return null;
  }

  /* do not bother with this node, since we are not handling
   inheritance */
  public Exp visit(ClassDeclExtends n)
  {
    return null;
  }

  /* if VarDecl in ClassDecl, add offset from class object pointer to 
   HashMap fieldVars
   else if VarDecl in MethodDecl, allocate local and add to HashMap vars */
  public Exp visit(VarDecl n)
  {
    if (vars == null)
      fieldVars.put(n.i.toString(), new Integer(offset += currFrame.wordSize()));
    else
      vars.put(n.i.toString(), currFrame.allocLocal(false));

    return null;
  }

  public Exp visit(MethodDecl n)
  {
    /* setup new frame for method */
    Frame.Frame newFrame = currFrame.newFrame(n.i.toString(), n.fl.size() + 1);
    Frame.Frame oldFrame = currFrame;
    currFrame = newFrame;

    vars = new HashMap<String, Access>();
    /* add locals to HashMap vars */
    for (int i = 0; i < n.vl.size(); i++)
      n.vl.elementAt(i).accept(this);

    Tree.Stm argMoves = null;
    
    /* DONE CODE: set value of Tree.Exp objPtr
    Recall that objPtr is a pointer to the address in memory at which 
    instance variables are stored for the current class 
    (i.e., it is "this").
    In the MiniJava compiler, it is passed as an argument during all
    calls to MiniJava methods. */ 
    Frame.Access fLoc = currFrame.allocLocal(false);
	Tree.Stm objMove = new Tree.MOVE(fLoc.exp(new Tree.TEMP(currFrame.FP())), 
									currFrame.formals.head.exp(new Tree.TEMP(currFrame.FP())));
	objPtr = fLoc.exp(new Tree.TEMP(currFrame.FP()));
	argMoves = objMove; //new Tree.SEQ(argMoves, objMove);
	
	
	/* DONE CODE: move formals to fresh temps and add them to the HashMap vars */
	Frame.AccessList frmls = currFrame.formals.tail; 
    for (int i=0; i < n.fl.size(); i++) {
    	syntaxtree.Formal f = n.fl.elementAt(i);
    	fLoc = currFrame.allocLocal(false);
    	vars.put(f.i.toString(), fLoc);
   	 	Tree.Stm argMove = new Tree.MOVE(fLoc.exp(new Tree.TEMP(currFrame.FP())), 
   	 								frmls.head.exp(new Tree.TEMP(currFrame.FP())));
   	 	argMoves = new Tree.SEQ(argMoves, argMove);
   	 	frmls = frmls.tail;
    }
    
    /* DONE CODE: visit each statement in method body, 
     creating new Tree.SEQ nodes as needed */
    Tree.Stm body = null; // FILL IN
    for (int i=0; i<n.sl.size(); i++) {
    	syntaxtree.Statement s = n.sl.elementAt(i);
    	if(body == null)
    		body = s.accept(this).unNx();
    	else
    		body = new Tree.SEQ(body, s.accept(this).unNx()); 
    }

    
    /* DONE CODE: get return expression and group with statements of body,
     then create Tree.MOVE to store return value */
    if(body == null)
    	body = argMoves;
    else
    	body = new Tree.SEQ(argMoves, body);
    
    Tree.Exp ret = n.e.accept(this).unEx();
    Tree.ESEQ retESEQ = new Tree.ESEQ(body, ret);
 
    body = new Tree.MOVE(new Tree.TEMP(currFrame.RV()), retESEQ);
    
    
    /* create new procedure fragment for method and add to list */
    procEntryExit(body);
    currFrame = oldFrame;
    vars = null;
    objPtr = null;

    return null;
  }

  /* these nodes are never reached by visitor */
  public Exp visit(Formal n)
  {
    return null;
  }

  public Exp visit(IntArrayType n)
  {
    return null;
  }

  public Exp visit(BooleanType n)
  {
    return null;
  }

  public Exp visit(IntegerType n)
  {
    return null;
  }

  public Exp visit(IdentifierType n)
  {
    return null;
  }

  public Exp visit(Block n)
  {
    /* DONE CODE -- don't return null */

	int size = n.sl.size();
		
	if(size == 0)
		return null;
	
	Tree.Stm lastStm = n.sl.elementAt(size-1).accept(this).unNx();
	Tree.SEQ retSEQ = null;
	for(int i = size-1; i > 0; i--) {
		Tree.Stm stmt = n.sl.elementAt(i-1).accept(this).unNx();
		if(retSEQ == null)
			retSEQ = new Tree.SEQ(stmt, lastStm);
		else
			retSEQ = new Tree.SEQ(stmt, retSEQ);
	}
	if(retSEQ == null)
		return new Nx(lastStm);
	else
		return new Nx(retSEQ);
  }

  public Exp visit(If n)
  {
    /* DONE CODE -- don't return null */
	Temp.Label t = new Temp.Label();
	Temp.Label f = new Temp.Label();
	Temp.Label j = new Temp.Label();
	
	Tree.LABEL tL = new Tree.LABEL(t);
	Tree.LABEL fL = new Tree.LABEL(f);
	
	Tree.Stm condStm = n.e.accept(this).unCx(t, f);
	Tree.Stm tBody = new Tree.SEQ(new Tree.SEQ(tL, n.s1.accept(this).unNx()), new Tree.JUMP(j));
	Tree.Stm fBody = new Tree.SEQ(fL, n.s2.accept(this).unNx()); //new Tree.SEQ(new Tree.SEQ(fL, n.s2.accept(this).unNx()), new Tree.JUMP(j));
	
	Tree.Stm retStm = new Tree.SEQ(new Tree.SEQ(condStm, new Tree.SEQ(tBody, fBody)), new Tree.LABEL(j));
	
    return new Nx(retStm);
  }

  public Exp visit(While n)
  {
    /* DONE CODE -- don't return null */
	Temp.Label test = new Temp.Label();
	Temp.Label body = new Temp.Label();
	Temp.Label done = new Temp.Label();
	
	Tree.Stm testInsts = n.e.accept(this).unCx(body, done);
	Tree.Stm testStm = new Tree.SEQ(new Tree.LABEL(test), testInsts);

	Tree.Stm bodyInsts = n.s.accept(this).unNx();
	Tree.Stm bodyStm = new Tree.SEQ(new Tree.SEQ(new Tree.LABEL(body), bodyInsts), new Tree.JUMP(test));
	
	Tree.Stm retStm = new Tree.SEQ(new Tree.SEQ(testStm, bodyStm), new Tree.LABEL(done));
	
    return new Nx(retStm);
  }

  /* call external print function, passing integer expression as argument */
  public Exp visit(Print n)
  {
    Tree.Exp e = (n.e.accept(this)).unEx();
    return new Ex(currFrame.externalCall("printInt", new Tree.ExpList(e, null)));
  }

  /* get appropriate Tree.Exp node for identifier (set as Tree.MOVE dst),
   set expression as Tree.MOVE src */
  public Exp visit(Assign n)
  {
    Tree.Exp e = (n.e.accept(this)).unEx();
    return new Nx(new Tree.MOVE(getIdTree(n.i.toString()), e));
  }

  public Exp visit(ArrayAssign n)
  {
    /* DONE CODE -- don't return null */
	  
	  Tree.Exp baseAdd = getIdTree(n.i.toString());
	  
	  
	  Tree.Exp offsetExp = new Tree.BINOP(Tree.BINOP.PLUS, n.e1.accept(this).unEx(), new Tree.CONST(1));
	  //int offset = ((Tree.CONST)n.e1.accept(this).unEx()).value + 1;
	  Tree.Exp target = new Tree.MEM(new Tree.BINOP(Tree.BINOP.PLUS, 
			  										baseAdd, 
			  										new Tree.BINOP(Tree.BINOP.MUL, 
			  												offsetExp, 
			  												new Tree.CONST(currFrame.wordSize()))));
			  										//new Tree.CONST(offset * currFrame.wordSize())));
	  Tree.Exp e = (n.e2.accept(this)).unEx();
	  Tree.Stm setTarget = new Tree.MOVE(target, e);
	  
	  return new Nx(setTarget);
  }

  public Exp visit(And n)
  {
    /* DONE CODE --don't return null */
	Exp lExp = n.e1.accept(this);
	Exp rExp = n.e2.accept(this);
	
	Exp retExp = new AndExp(lExp, rExp);
			
    return retExp;
  }

  public Exp visit(LessThan n)
  {
    /* DONE CODE -- don't return null */	
	Tree.Exp lExp = n.e1.accept(this).unEx();
	Tree.Exp rExp = n.e2.accept(this).unEx();
	
	//Tree.Exp retStm = new RelCx(Tree.CJUMP.LT, lExp, rExp).unEx();
			
	return new RelCx(Tree.CJUMP.LT, lExp, rExp); //new Ex(retStm);
  }

  public Exp visit(Plus n)
  {
    /* Done CODE -- don't return null */
	  Tree.Exp lExp = (n.e1.accept(this)).unEx();
	  Tree.Exp rExp = (n.e2.accept(this)).unEx();
	  Tree.BINOP retExp = new Tree.BINOP(BINOP.PLUS, lExp, rExp);
      return new Ex(retExp);
  }

  public Exp visit(Minus n)
  {
    /* Done CODE -- don't return null */
	  Tree.Exp lExp = (n.e1.accept(this)).unEx();
	  Tree.Exp rExp = (n.e2.accept(this)).unEx();
	  Tree.BINOP retExp = new Tree.BINOP(BINOP.MINUS, lExp, rExp);
      return new Ex(retExp);
  }

  public Exp visit(Times n)
  {
    /* Done CODE -- don't return null */
	  Tree.Exp lExp = (n.e1.accept(this)).unEx();
	  Tree.Exp rExp = (n.e2.accept(this)).unEx();
	  Tree.BINOP retExp = new Tree.BINOP(BINOP.MUL, lExp, rExp);
      return new Ex(retExp);
  }

  public Exp visit(ArrayLookup n)
  {
    /* DONE CODE -- don't return null */
	 Tree.Exp arrayExp = n.e1.accept(this).unEx();
	 Tree.Exp baseAdd = null;
	 if(arrayExp instanceof Tree.ESEQ)
	 {
		 Tree.ESEQ arrayEseq = (Tree.ESEQ)arrayExp;
		 baseAdd = arrayEseq.exp;
	 }
	 else
	 {
		 baseAdd = arrayExp;
	 }

	 Tree.Exp idxExp = n.e2.accept(this).unEx();
	 /*
	 if(idxExp instanceof Tree.ESEQ)
	 {
		 Tree.ESEQ idxEseq = (Tree.ESEQ)idxExp;
		 idxExp = idxEseq.exp;
	 }*/
	 
	 Tree.Exp offset = new Tree.BINOP(Tree.BINOP.MUL, 
									new Tree.BINOP(Tree.BINOP.PLUS, 
													idxExp, 
													new Tree.CONST(1)), 
									new Tree.CONST(currFrame.wordSize()));
	 Tree.Exp elementAdd = new Tree.BINOP(Tree.BINOP.PLUS, 
											baseAdd,
											offset);
	 
	 /*Frame.Access tempLoc = currFrame.allocLocal(false);
	 Tree.Exp tempExp = tempLoc.exp(new Tree.TEMP(currFrame.FP()));
	 Tree.Stm lenMove = new Tree.MOVE(tempExp, lengthExp);*/
	  
	 Tree.Exp idxCnt = new Tree.MEM(elementAdd);
	 Tree.Stm retSEQ = new Ex(arrayExp).unNx();
	 Tree.Exp retESEQ = new Tree.ESEQ(retSEQ, idxCnt);
	  
     return new Ex(retESEQ);
  }

  public Exp visit(ArrayLength n)
  {
    /* DONE CODE -- don't return null */

	Tree.Exp arrayExp = n.e.accept(this).unEx();
	Tree.Exp arrayPtr = null;
	
	if(arrayExp instanceof Tree.ESEQ)
	{
		Tree.ESEQ arrayEseq = (Tree.ESEQ)arrayExp; 
		arrayPtr = arrayEseq.exp;
	}
	else
	{
		arrayPtr = arrayExp;
	}
	
	Tree.Exp baseAdd = new Tree.MEM(arrayPtr);
	
	Tree.Stm retSeq = new Ex(arrayExp).unNx();
	Tree.Exp retEseq = new Tree.ESEQ(retSeq, baseAdd);
	
    return new Ex(retEseq);
  }

  public Exp visit(Call n)
  {
    /* DONE CODE -- don't return null */
	  Tree.Exp callObjExp = n.e.accept(this).unEx();
	  Tree.Exp objectPtr = null;
	 
	  if(callObjExp instanceof Tree.ESEQ)
	  {
		  Tree.ESEQ callObjEseq = (Tree.ESEQ)callObjExp;
		  objectPtr = callObjEseq.exp; 
	  }
	  else
		  objectPtr = callObjExp;
	  

	  Tree.Stm retSeq = new Ex(callObjExp).unNx();//callObjEseq.stm; 
	  
	 String mn = n.i.s;
	 Temp.Label methUniqName = new Temp.Label(mn);
	 
	 Tree.ExpList args = null;
	 Tree.ExpList last = null;
	 for (int i=n.el.size()-1; i >= 0 ; i--) {
		 Tree.Exp arg = n.el.elementAt(i).accept(this).unEx();
		 if (args == null)
		 {
			 args = new Tree.ExpList(arg, null);
			 last = args;
		 }
		 else
		 {
			 last.tail = new Tree.ExpList(arg, null);
			 last = last.tail;
		 }
	 }
	 
	 if (args == null)
	 {
		 args = new Tree.ExpList(objectPtr, null);
		 last = args;
	 }
	 else
	 {
		 last.tail = new Tree.ExpList(objectPtr, null);
		 last = last.tail;
	 }
	 
	 Tree.Exp methodCall = new Tree.CALL(new Tree.NAME(methUniqName), args);
	 
	 retSeq = new Tree.SEQ(retSeq, new Ex(methodCall).unNx());
	 Tree.ESEQ retESeq = new Tree.ESEQ(retSeq, new Tree.TEMP(currFrame.RV()));
	  
    return new Ex(retESeq);
  }

  public Exp visit(IntegerLiteral n)
  {
    /* DONE CODE -- don't return null */
	Tree.Exp consExp = new Tree.CONST(n.i);
	return new Ex(consExp);
  }

  public Exp visit(True n)
  {
    /* DONE CODE -- don't return null */
	Tree.Exp tExp = new Tree.CONST(1);
    return new Ex(tExp);
  }

  public Exp visit(False n)
  {
    /* DONE CODE -- don't return null */
	Tree.Exp fExp = new Tree.CONST(0);
	return new Ex(fExp);
  }

  /* get appropriate Tree.Exp node for identifier */
  public Exp visit(IdentifierExp n)
  {
    return new Ex(getIdTree(n.s));
  }

  /* return pointer to address in heap at which instance variables are 
   stored, for current class */
  public Exp visit(This n)
  {
    return new Ex(objPtr);
  }

  /* call external "alloc" function, pass the number of bytes
   needed ((array length + 1) * wordsize) as the argument,
   initialize the value of each array element to 0,
   store the array length with the array */ 
  public Exp visit(NewArray n)
  {
    /* DONE CODE
     (Note: use currFrame.externalCall("alloc", new Tree.ExpList(...))) 
     -- don't return null */
	  
	  Tree.Exp lengthExp = n.e.accept(this).unEx(); 
	  int wordSize = currFrame.wordSize();
	  Tree.Exp allocSizeExp = new Tree.BINOP(Tree.BINOP.MUL, 
			  									new Tree.BINOP(Tree.BINOP.PLUS, lengthExp, new Tree.CONST(1)), 
			  									new Tree.CONST(wordSize));
	  //allocate memory and get the address
	  Tree.ExpList exFunArgs = new Tree.ExpList(allocSizeExp, null);
	  Tree.Exp arrayAllocExp  = currFrame.externalCall("alloc", exFunArgs);	  
	  Tree.Exp baseAdd = new Tree.TEMP(currFrame.RV());

	  //arrayPtr should have the base address of the array
	  //Tree.MEM or Tree.TEMP
	  Tree.Exp arrayPtr = (currFrame.allocLocal(false)).exp(new Tree.TEMP(currFrame.FP()));
	  Tree.Stm saveArrPtr = new Tree.MOVE(arrayPtr, baseAdd);
	  
	  Tree.Stm retSEQ = new Tree.SEQ(new Ex(arrayAllocExp).unNx(), saveArrPtr);
	  
	  // Save the array length at offset 0
	  Tree.Stm lenSave = new Tree.MOVE(new Tree.MEM(baseAdd), lengthExp);
	  retSEQ = new Tree.SEQ(retSEQ, lenSave);
	  
	  Temp.Label test = new Temp.Label();
	  Temp.Label body = new Temp.Label();
	  Temp.Label done = new Temp.Label();
	  		  
	  Frame.Access tempLoc = currFrame.allocLocal(false);
	  Tree.Exp tempExp = tempLoc.exp(new Tree.TEMP(currFrame.FP()));
	  Tree.Stm lenMove = new Tree.MOVE(tempExp, lengthExp);
	  retSEQ = new Tree.SEQ(retSEQ, lenMove);
	  
	  Tree.Stm testStm = new RelCx(Tree.CJUMP.LT, new Tree.CONST(0), tempExp).unCx(body, done);

	  Tree.Exp target = new Tree.BINOP(Tree.BINOP.PLUS,
										baseAdd, 
										new Tree.BINOP(Tree.BINOP.MUL, 
														tempExp, 
														new Tree.CONST(currFrame.wordSize())));
	  Tree.Stm t3 = new Tree.SEQ(new Tree.MOVE(
			  						new Tree.MEM(target), 
			  						new Tree.CONST(0)) , 
			  					 new Tree.MOVE(tempExp, new Tree.BINOP(Tree.BINOP.MINUS, tempExp, new Tree.CONST(1))));
			  						
	  
	  Tree.Stm headerStm = new Tree.SEQ(new Tree.LABEL(test), testStm);
	  Tree.Stm bodyStm = new Tree.SEQ(new Tree.SEQ(new Tree.LABEL(body), t3),
			  						  new Tree.JUMP(test));
	  
	  Tree.Stm whileStm = new Tree.SEQ(new Tree.SEQ(headerStm, bodyStm),
			  							new Tree.LABEL(done));
	  
	  retSEQ = new Tree.SEQ(retSEQ, whileStm);  
	  Tree.Exp retExp = new Tree.ESEQ(retSEQ, arrayPtr);
	  return new Ex(retExp);
  }

  /* call external "alloc" function, pass the number of bytes
   needed (number of instance variables * wordsize) as
   the argument, initialize each instance variable to 0 */  
  public Exp visit(NewObject n)
  { 
    /* DONE CODE 
     (Note: you will need to get the number of field variables from your
     symbol table)  -- don't return null */
	  
	  symbol.ClassTable objClass = symTbl.getClass(n.i.s);
	  int fldCnt = objClass.varCount();
	  int wordSize = currFrame.wordSize();
	  int allocSize = fldCnt * wordSize;
	 
	  //allocate memory and get the address
	  Tree.ExpList exFunArgs = new Tree.ExpList(new Tree.CONST(allocSize), null);
	  Tree.Exp objAllocExp  = currFrame.externalCall("alloc", exFunArgs);
	  Tree.Exp objBaseAdd = new Tree.TEMP(currFrame.RV());
	  
	  Tree.Exp objectPtr = (currFrame.allocLocal(false)).exp(new Tree.TEMP(currFrame.FP()));
	  Tree.Stm saveObjectPtr = new Tree.MOVE(objectPtr, objBaseAdd);
	  
	  Tree.Stm retSEQ = new Tree.SEQ(new Ex(objAllocExp).unNx(), saveObjectPtr);
	  
	  for(int i = 0; i < fldCnt; i++) {
		  Tree.Exp offset = new Tree.CONST(i * wordSize);
		  Tree.Exp dest = new Tree.BINOP(Tree.BINOP.PLUS, objBaseAdd, offset);
		  Tree.Stm init_i = new Tree.MOVE(new Tree.MEM(dest), new Tree.CONST(0));
		  retSEQ = new Tree.SEQ(retSEQ, init_i);
	  }
	  
	  Tree.Exp retExp = new Tree.ESEQ(retSEQ, objectPtr);
	  return new Ex(retExp);
  }

  
  public Exp visit(Not n)
  {
    /* DONE CODE -- don't return null */
	Tree.Exp argExp = n.e.accept(this).unEx();
	Tree.Exp retExp = new Tree.BINOP(Tree.BINOP.MINUS, new Tree.CONST(1), argExp);
	
    return new Ex(retExp);
  }

  /* node never reached by visitor */
  public Exp visit(Identifier n)
  {
    return null;
  }

  /* if id is a local or formal variable (i.e., vars.get(id) returns an Access
   object), return result of calling exp()--Tree.MEM if InFrame, 
   Tree.TEMP if InReg
   else if id is a field (instance) variable, get its offset from class
   object pointer and return Tree.MEM 
   **For demonstration of this method, see visit(Assign) and visit(Identifier). */
  private Tree.Exp getIdTree(String id)
  {
    Frame.Access a = vars.get(id);
    if (a == null)
      {
        int offset = fieldVars.get(id).intValue();
        return new Tree.MEM(new Tree.BINOP(Tree.BINOP.PLUS, objPtr, new Tree.CONST(offset)));
      }

    return a.exp(new Tree.TEMP(currFrame.FP()));
  }
}
