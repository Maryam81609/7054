package Mips;

import Assem.Instr;
import Assem.InstrList;
import Assem.LABEL;
import Assem.MOVE;
import Assem.OPER;
import IR_visitor.TempVisitor;

public class Codegen implements TempVisitor
{

  private InstrList codeList, listTail;
  private MipsFrame frame;

  public Codegen(MipsFrame f)
  {
    frame = f;
    codeList = listTail = null;
  }

  public InstrList codegen(Tree.Stm s)
  {
    s.accept(this);
    InstrList result = codeList;
    codeList = listTail = null;
    return result;
  }

  /* Assumes instructions are generated in reverse order. */
  private void emit(Instr instr)
  {
    if (listTail == null)
      listTail = codeList = new InstrList(instr, null);
    else
      listTail = listTail.tail = new InstrList(instr, null);
  }

  public void visit(Tree.SEQ n)
  {
    throw new Error("There should be no SEQ nodes in a canonical IR tree.");
  }

  public void visit(Tree.LABEL n)
  {
    emit(new LABEL(n.label.toString() + ":\n", n.label));
  }

  public void visit(Tree.JUMP n)
  {
    // TO DO: fill in
  }

  public void visit(Tree.CJUMP n)
  {
	 int i = 0;
	 System.out.println(i);
    // TO DO: fill in
  }

  public void visit(Tree.MOVE n)
  {
	  // TEST DONE: fill in
	  Temp.Temp r1 = n.dst.accept(this);
	  if(n.dst instanceof Tree.TEMP) {
		  if(n.src instanceof Tree.CONST) {
			  emit(new OPER("li r1, " + ((Tree.CONST)n.src).value + "\n", new Temp.TempList(r1, null), null));
		  }
		  else {
			  Temp.Temp r2 = n.src.accept(this);
			  emit(new MOVE("move r1, r2\n", r1, r2));
		  }
	  }
	  else if(n.dst instanceof Tree.MEM) {
		  if(n.src instanceof Tree.CONST) {
			  emit(new OPER("sw " + ((Tree.CONST)n.src).value + ", r1\n", new Temp.TempList(r1, null), null));
		  }
		  else {
			  Temp.Temp r2 = n.src.accept(this);
			  emit(new OPER("sw r2, r1\n", new Temp.TempList(r1, null), 
					  new Temp.TempList(r2, null)));  
		  }
	  }
	  
  }

  public void visit(Tree.EXP1 n)
  {
	  n.exp.accept(this);
  }

  public Temp.Temp visit(Tree.BINOP n)
  {
	  // TEST DONE: fill in
	  Temp.Temp r1 = new Temp.Temp();
	  Temp.TempList dstList = new Temp.TempList(r1, null);
	  switch(n.binop) {
	  case Tree.BINOP.PLUS:		  
		  if(n.left instanceof Tree.CONST && n.right instanceof Tree.CONST) {
			  emit(new OPER("addi r1, " + ((Tree.CONST)n.left).value + ", " + ((Tree.CONST)n.right).value + "\n", dstList, null));
		  }
		  else if(n.left instanceof Tree.CONST) {
			  Temp.Temp r2 = n.right.accept(this);
			  emit(new OPER("addi r1, " + ((Tree.CONST)n.left).value + ", r2\n", dstList, new Temp.TempList(r2, null)));
		  }
		  else if(n.right instanceof Tree.CONST) {
			  Temp.Temp r2 = n.left.accept(this);
			  emit(new OPER("addi r1, r2, " + ((Tree.CONST)n.right).value + "\n", dstList, new Temp.TempList(r2, null)));
		  }
		  else {
			  Temp.Temp r2 = n.left.accept(this);
			  Temp.Temp r3 = n.right.accept(this);
			  emit(new OPER("add r1, r2, r3\n", dstList, new Temp.TempList(r2, new Temp.TempList(r3, null))));
		  }
		  break;
	  case Tree.BINOP.MINUS:
		  if(n.left instanceof Tree.CONST && n.right instanceof Tree.CONST) {
			  emit(new OPER("addi r1, " + ((Tree.CONST)n.left).value + ", " + (-1 * ((Tree.CONST)n.right).value) + "\n", dstList, null));
		  }
		  else if(n.right instanceof Tree.CONST) {
			  Temp.Temp r2 = n.left.accept(this);
			  emit(new OPER("addi r1, r2, " + (-1 * ((Tree.CONST)n.right).value) + "\n", dstList, new Temp.TempList(r2, null)));
		  }
		  else {
			  Temp.Temp r2 = n.left.accept(this);
			  Temp.Temp r3 = n.right.accept(this);
			  emit(new OPER("add r1, r2, r3\n", dstList, new Temp.TempList(r2, new Temp.TempList(r3, null))));
		  }
		  break;
	  case Tree.BINOP.MUL:
		  Temp.Temp r4 = n.left.accept(this);
		  Temp.Temp r5 = n.right.accept(this);
		  emit(new OPER("mulo r1, r4, r5\n", dstList, new Temp.TempList(r4, new Temp.TempList(r5, null))));
		  break;
	  case Tree.BINOP.AND:
		  if(n.left instanceof Tree.CONST && n.right instanceof Tree.CONST) {
			  emit(new OPER("andi r1, " + ((Tree.CONST)n.left).value + ", " + ((Tree.CONST)n.right).value + "\n", dstList, null));
		  }
		  else if(n.left instanceof Tree.CONST) {
			  Temp.Temp r2 = n.right.accept(this);
			  emit(new OPER("andi r1, " + ((Tree.CONST)n.left).value + ", r2\n", dstList, new Temp.TempList(r2, null)));
		  }
		  else if(n.right instanceof Tree.CONST) {
			  Temp.Temp r2 = n.left.accept(this);
			  emit(new OPER("andi r1, r2, " + ((Tree.CONST)n.right).value + "\n", dstList, new Temp.TempList(r2, null)));
		  }
		  else {
			  Temp.Temp r2 = n.left.accept(this);
			  Temp.Temp r3 = n.right.accept(this);
			  emit(new OPER("and r1, r2, r3\n", dstList, new Temp.TempList(r2, new Temp.TempList(r3, null))));
		  }
		  break;		  
	  }
	  return r1;
  }

  public Temp.Temp visit(Tree.MEM n)
  {
    // TEST DONE: fill in
	  Temp.Temp r1 = new Temp.Temp();
	  /*if(n.exp instanceof Tree.BINOP && ((Tree.BINOP)n.exp).binop == Tree.BINOP.PLUS) {
		  if(((Tree.BINOP)n.exp).left instanceof Tree.CONST) {
			  emit(new OPER("lw r1, r2 + " + ((Tree.CONST)((Tree.BINOP)n.exp).left).value + "\n", )
		  }
	  }*/
	  if(n.exp instanceof Tree.CONST) {
		  emit(new OPER("lw r1, " + ((Tree.CONST)n.exp).value + "\n", new Temp.TempList(r1, null), null));
	  }
	  else {
		  Temp.Temp add = n.exp.accept(this);
		  emit(new OPER("lw r1, add\n", new Temp.TempList(r1, null), new Temp.TempList(add, null)));
	  }
	  
	  return r1;
  }

  public Temp.Temp visit(Tree.TEMP n)
  {
    return n.temp;
  }

  public Temp.Temp visit(Tree.ESEQ n)
  {
    throw new Error("There should be no ESEQ nodes in a canonical IR tree.");
  }

  /* Assumes NAME node is handled in visit methods for JUMP and CALL. */
  public Temp.Temp visit(Tree.NAME n)
  {
    throw new Error("In well-formed MiniJava program, NAME node is never visited outside of JUMP and CALL.");
  }

  public Temp.Temp visit(Tree.CONST n)
  {
	  // TEST DONE: fill in
	  Temp.Temp t = new Temp.Temp();
	  emit(new Assem.OPER("addi t1, zero, " + n.value + "\n", new Temp.TempList(t, null), new Temp.TempList(MipsFrame.ZERO, null)));
	  return t;
  }

  public Temp.Temp visit(Tree.CALL n)
  {
    // TO DO: fill in
    return null;
  }

}
