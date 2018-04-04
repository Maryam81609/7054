package Mips;

import Assem.Instr;
import Assem.InstrList;
import Assem.LABEL;
import Assem.MOVE;
import Assem.OPER;
import IR_visitor.TempVisitor;
import Temp.Label;
import Tree.Exp;

public class SpimCodegen implements TempVisitor
{

  private InstrList codeList, listTail;
  private MipsFrame frame;

  public SpimCodegen(MipsFrame f)
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
	  if  (n.label.toString() != "main") {
		  emit(new LABEL(n.label.toString() + ":\n", n.label));
	  }
    
  }

  public void visit(Tree.JUMP n)
  {
    // TEST DONE: fill in
	  if(n.exp instanceof Tree.NAME) {
		  Temp.Label l = ((Tree.NAME)n.exp).label;
		  emit(new OPER("\tj `j0", null, null, new Temp.LabelList(l, null)));
	  }
	  else {
		  Temp.Temp r1 = n.exp.accept(this);
		  emit(new OPER("\tjr `d0", new Temp.TempList(r1, null), null));
	  }
  }

  public void visit(Tree.CJUMP n)
  {
    // TEST DONE: fill in
	  switch(n.relop) {
	  case Tree.CJUMP.LT:
		  Temp.Temp r2 = n.left.accept(this);
		  Temp.Temp r3 = n.right.accept(this);
		  emit(new OPER("\tblt `d0, `s0, `j0" , null, new Temp.TempList(r2, new Temp.TempList(r3, null)), 
				  new Temp.LabelList(n.iftrue, new Temp.LabelList(n.iffalse, null))));
		  break;
	  default:
		  break;
	  } 
  }

  public void visit(Tree.MOVE n)
  {
	  // TEST DONE: fill in
	  Temp.Temp r1 = n.dst.accept(this);
	  if(n.dst instanceof Tree.TEMP) {
		  if(n.src instanceof Tree.CONST) { 
			  //emit(new OPER("\tsw " + ((Tree.CONST)n.src).value + ", ?(`d0)", new Temp.TempList(r1, null), null));
			  emit(new OPER("\tli `d0, " + ((Tree.CONST)n.src).value, new Temp.TempList(r1, null), null));
		  }
		  else {
			  // if function call, then r2 is the return value of the function
			  Temp.Temp r2 = n.src.accept(this);
			  emit(new MOVE("\tmove `d0, `s0", r1, r2));
		  }
	  }
	  else if(n.dst instanceof Tree.MEM) {
		  if(n.src instanceof Tree.CONST) {
			  emit(new OPER("\tsw " + ((Tree.CONST)n.src).value + ", `d0", new Temp.TempList(r1, null), null));
		  }
		  else {
			  Temp.Temp r2 = n.src.accept(this);
			  emit(new OPER("\tsw `s0, `d0", new Temp.TempList(r1, null), new Temp.TempList(r2, null)));  
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
			  emit(new OPER("\taddi `d0, " + ((Tree.CONST)n.left).value + ", " + ((Tree.CONST)n.right).value, 
					  dstList, null));
		  }
		  else if(n.left instanceof Tree.CONST) {
			  Temp.Temp r2 = n.right.accept(this);
			  emit(new OPER("\taddi `d0, " + ((Tree.CONST)n.left).value + ", `s0", 
					  dstList, new Temp.TempList(r2, null)));
		  }
		  else if(n.right instanceof Tree.CONST) {
			  Temp.Temp r2 = n.left.accept(this);
			  emit(new OPER("\taddi `d0, `s0, " + ((Tree.CONST)n.right).value , 
					  dstList, new Temp.TempList(r2, null)));
		  }
		  else {
			  Temp.Temp r2 = n.left.accept(this);
			  Temp.Temp r3 = n.right.accept(this);
			  emit(new OPER("\tadd `d0, `s0, `s1", 
					  dstList, new Temp.TempList(r2, new Temp.TempList(r3, null))));
		  }
		  break;
	  case Tree.BINOP.MINUS:
		  if(n.left instanceof Tree.CONST && n.right instanceof Tree.CONST) {
			  emit(new OPER("\taddi `d0, " + ((Tree.CONST)n.left).value + ", " + (-1 * ((Tree.CONST)n.right).value) , 
					  dstList, null));
		  }
		  else if(n.right instanceof Tree.CONST) {
			  Temp.Temp r2 = n.left.accept(this);
			  emit(new OPER("\taddi `d0, `s0, " + (-1 * ((Tree.CONST)n.right).value) , 
					  dstList, new Temp.TempList(r2, null)));
		  }
		  else {
			  Temp.Temp r2 = n.left.accept(this);
			  Temp.Temp r3 = n.right.accept(this);
			  emit(new OPER("\tadd `d0, `s0, `s1", 
					  dstList, new Temp.TempList(r2, new Temp.TempList(r3, null))));
		  }
		  break;
	  case Tree.BINOP.MUL:
		  Temp.Temp r4 = n.left.accept(this);
		  Temp.Temp r5 = n.right.accept(this);
		  emit(new OPER("\tmul `d0, `s0, `s1", dstList, new Temp.TempList(r4, new Temp.TempList(r5, null))));
		  break;
	  case Tree.BINOP.AND:
		  if(n.left instanceof Tree.CONST && n.right instanceof Tree.CONST) {
			  emit(new OPER("\tandi `d0, " + ((Tree.CONST)n.left).value + ", " + ((Tree.CONST)n.right).value , 
					  dstList, null));
		  }
		  else if(n.left instanceof Tree.CONST) {
			  Temp.Temp r2 = n.right.accept(this);
			  emit(new OPER("\tandi `d0, " + ((Tree.CONST)n.left).value + ", `s0", 
					  dstList, new Temp.TempList(r2, null)));
		  }
		  else if(n.right instanceof Tree.CONST) {
			  Temp.Temp r2 = n.left.accept(this);
			  emit(new OPER("\tandi `d0, `s0, " + ((Tree.CONST)n.right).value , 
					  dstList, new Temp.TempList(r2, null)));
		  }
		  else {
			  Temp.Temp r2 = n.left.accept(this);
			  Temp.Temp r3 = n.right.accept(this);
			  emit(new OPER("\tand `d0, `s0, `s1", dstList, 
					  new Temp.TempList(r2, new Temp.TempList(r3, null))));
		  }
		  break;		  
	  }
	  return r1;
  }

  public Temp.Temp visit(Tree.MEM n)
  {
    // TEST DONE: fill in
	  Temp.Temp r1 = new Temp.Temp();
	  if(n.exp instanceof Tree.CONST) {
		  emit(new OPER("\tlw `d0, 0(" + ((Tree.CONST)n.exp).value + ")" , new Temp.TempList(r1, null), null));
	  }
	  else {
		  Temp.Temp addr = n.exp.accept(this);
		  emit(new OPER("\tlw `d0, (`s0)", new Temp.TempList(r1, null), new Temp.TempList(addr, null)));
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
	  emit(new Assem.OPER("\taddi `d0, `s0, " + n.value , new Temp.TempList(t, null), new Temp.TempList(MipsFrame.ZERO, null)));
	  return t;
  }

  public Temp.Temp visit(Tree.CALL n)
  {
    // TEST DONE: fill in
	  
	  Tree.ExpList args = reverse(n.args);	  
	  int i = 0;
	  while(args != null && i < 4) {
		  switch(i) {
		  case 0:
			  Temp.Temp arg0 = args.head.accept(this);
			  emit(new MOVE("\tmove `d0, `s0", MipsFrame.A0, arg0));
			  args = args.tail;
			  i = i + 1;
			  break;
		  case 1:
			  Temp.Temp arg1 = args.head.accept(this);
			  emit(new MOVE("\tmove `d0, `s0", MipsFrame.A1, arg1));
			  args = args.tail;
			  i = i + 1;
			  break;
		  case 2:
			  Temp.Temp arg2 = args.head.accept(this);
			  emit(new MOVE("\tmove `d0, `s0", MipsFrame.A2, arg2));
			  args = args.tail;
			  i = i + 1;
			  break;
		  case 3:
			  Temp.Temp arg3 = args.head.accept(this);
			  emit(new MOVE("\tmove `d0, `s0", MipsFrame.A3, arg3));
			  args = args.tail;
			  i = i + 1;
			  break;
		  }
	  }
	  
	  Temp.Label fl = ((Tree.NAME)n.func).label;
	  emit(new OPER("\tjal `j0", null, null, new Temp.LabelList(fl, null)));
	  
	  /*Temp.Temp ra = new Temp.Temp();
	  Instr tempInstr= new OPER("\tlw `d0, 0(`s0)", new Temp.TempList(ra, null), 
			  new Temp.TempList(frame.FP(), null)); 
	  emit(tempInstr);
	  //System.out.println(tempInstr.format(new Temp.RegMap(frame)));

	  tempInstr = new MOVE("\tmove `d0, `s0", MipsFrame.RA, ra);
	  emit(tempInstr);
	  //System.out.println(tempInstr.format(new Temp.RegMap(frame)));
	  tempInstr = new OPER("\tjr `s0", null, new Temp.TempList(MipsFrame.RA, null));
	  emit(tempInstr);
	  //System.out.println(tempInstr.format(new Temp.RegMap(frame)));*/
	  
	  return frame.RV();
  }

  public void prologue() {
	  Instr tempInstr = new MOVE("\tmove `d0, `s0",frame.FP(), MipsFrame.SP);
	  System.out.println(tempInstr.format(new Temp.RegMap(frame)));
	  
	  Temp.Temp r2 = new Temp.Temp();
	  tempInstr = new OPER("\taddi `d0, `s0, " + 800 , 
			  new Temp.TempList(r2, null), new Temp.TempList(MipsFrame.ZERO, null));
	  System.out.println(tempInstr.format(new Temp.RegMap(frame))); 
	  
	  tempInstr = new OPER("\tsub `d0, `s0, `s1", new Temp.TempList(MipsFrame.SP, null), 
			  new Temp.TempList(MipsFrame.SP, new Temp.TempList(r2, null)));
	  System.out.println(tempInstr.format(new Temp.RegMap(frame))); 
	  
	  tempInstr = new OPER("\tsw `s0, 0(`d0)", new Temp.TempList(frame.FP(), null), 
			  new Temp.TempList(MipsFrame.RA, null));
	  System.out.println(tempInstr.format(new Temp.RegMap(frame)));
  }
  
  public void epilogue() {
	  Temp.Temp ra = new Temp.Temp();
	  Instr tempInstr; /*= new OPER("lw `d0, 0(`s0)", new Temp.TempList(ra, null), 
			  new Temp.TempList(frame.FP(), null)); 
	  System.out.println(tempInstr.format(new Temp.RegMap(frame)));
	  
	  tempInstr = new MOVE("move `d0, `s0", MipsFrame.RA, ra);
	  System.out.println(tempInstr.format(new Temp.RegMap(frame)));
	  tempInstr = new OPER("jr `s0", null, new Temp.TempList(MipsFrame.RA, null));
	  System.out.println(tempInstr.format(new Temp.RegMap(frame)));*/
	  
	  tempInstr = new MOVE("\tmove `d0, `s0", MipsFrame.SP, frame.FP());
	  System.out.println(tempInstr.format(new Temp.RegMap(frame)));
	  
	  tempInstr = new OPER("\taddi `d0, `s0, " + 800, new Temp.TempList(frame.FP(), null), 
			  new Temp.TempList(frame.FP(), null));
	  System.out.println(tempInstr.format(new Temp.RegMap(frame)));
  }
  
  public Tree.ExpList reverse(Tree.ExpList args_in) {
	  Tree.ExpList args_out = null;
	  while (args_in != null) {
		  args_out = new Tree.ExpList(args_in.head, args_out);
		  args_in = args_in.tail;
	  }
	  return args_out;
  }
}
