package Mips;

import Assem.Instr;
import Assem.InstrList;
import Assem.LABEL;
import Assem.MOVE;
import Assem.OPER;
import IR_visitor.TempVisitor;
import Temp.Label;
import Tree.Exp;
import java.util.HashMap;

public class SpimCodegen implements TempVisitor
{

  private int offset = -136;
  private HashMap<String, Integer> tempOffset;
  private InstrList codeList, listTail;
  private MipsFrame frame;

  public SpimCodegen(MipsFrame f)
  {
    frame = f;
    codeList = listTail = null;
    tempOffset = new HashMap<String, Integer>();
    offset = -8;
    //tempOffset.put("t0", -8);
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
	  //if  (n.label.toString() != "main") {
		  emit(new LABEL(n.label.toString() + ":\n", n.label));
	  //}
    
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
		  int jrOffset = getOffset(r1.toString());
		  emit(new OPER("\tlw $t2, " + jrOffset + "($fp)", null, null));
		  emit(new OPER("\tjr $t2", new Temp.TempList(r1, null), null));
	  }
  }

  public void visit(Tree.CJUMP n)
  {
    // TEST DONE: fill in
	  Temp.Temp r2 = n.left.accept(this);
	  Temp.Temp r3 = n.right.accept(this);
	  int srcOffset1 = getOffset(r2.toString());
	  int srcOffset2 = getOffset(r3.toString());
	  emit(new OPER("\tlw $t0, " + srcOffset1 + "($fp)", null, null));
	  emit(new OPER("\tlw $t1, " + srcOffset2 + "($fp)", null, null));
	  switch(n.relop) {
	  case Tree.CJUMP.NE:
		  emit(new OPER("\tbne $t0, $t1, `j0", null, new Temp.TempList(r2, new Temp.TempList(r3, null)),
				  new Temp.LabelList(n.iftrue, new Temp.LabelList(n.iffalse, null))));
		  break;
	  case Tree.CJUMP.GE:
		  emit(new OPER("\tbge $t0, $t1, `j0" , null, new Temp.TempList(r2, new Temp.TempList(r3, null)), 
				  new Temp.LabelList(n.iftrue, new Temp.LabelList(n.iffalse, null))));
		  break;
	  case Tree.CJUMP.LT:	  
		  emit(new OPER("\tblt $t0, $t1, `j0" , null, new Temp.TempList(r2, new Temp.TempList(r3, null)), 
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
	  int dstOffset = getOffset(r1.toString());
	  
	  if(n.dst instanceof Tree.TEMP) {
		  if(n.src instanceof Tree.CONST) { 
			  emit(new OPER("\tli $t2, " + ((Tree.CONST)n.src).value, new Temp.TempList(r1, null), null));
			  emit(new OPER("\tsw $t2, " + dstOffset + "($fp)", new Temp.TempList(r1, null), null));
		  }
		  else {
			  // if function call, then r2 is the return value of the function
			  Temp.Temp r2 = n.src.accept(this);
			  int srcOffset = getOffset(r2.toString());
			  
			  emit(new OPER("\tlw $t0, " + srcOffset + "($fp)", null, null));
			  emit(new MOVE("\tmove $t2, $t0", r1, r2));
			  emit(new OPER("\tsw $t2, " + dstOffset + "($fp)", null, null));//, r1, r2));
		  }
	  }
	  else if(n.dst instanceof Tree.MEM) {
		  if(n.src instanceof Tree.CONST) {
			  emit(new OPER("\tli $t2, " + ((Tree.CONST)n.src).value, null, null));
			  emit(new OPER("\tsw $t2, " + dstOffset + "($fp)", new Temp.TempList(r1, null), null));
			  //emit(new OPER("\tsw " + ((Tree.CONST)n.src).value + ", `d0", new Temp.TempList(r1, null), null));
		  }
		  else {
			  Temp.Temp r2 = n.src.accept(this);
			  int srcOffset = getOffset(r2.toString());
			  emit(new OPER("\tlw $t0, " + srcOffset + "($fp)", null, null));
			  emit(new MOVE("\tmove $t2, $t0", r1, r2));  
			  emit(new OPER("\tsw $t2, " + dstOffset + "($fp)", null, null));
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
	  int dstOffset = getOffset(r1.toString());
	  Temp.TempList dstList = new Temp.TempList(r1, null);
	  switch(n.binop) {
	  case Tree.BINOP.PLUS:	
		  /*if(n.left instanceof Tree.CONST && n.right instanceof Tree.CONST) {
			  emit(new OPER("\taddi $t2, " + ((Tree.CONST)n.left).value + ", " + ((Tree.CONST)n.right).value, 
					  dstList, null));
			  emit(new OPER("\tsw $t2, " + dstOffset + "($fp)", null, null));
		  }*/
		  if(n.left instanceof Tree.CONST) {
			  Temp.Temp r2 = n.right.accept(this);
			  int srcOffset = getOffset(r2.toString());
			  emit(new OPER("\tlw $t0, " + srcOffset + "($fp)", null, null));
			  emit(new OPER("\taddi $t2, $t0, " + ((Tree.CONST)n.left).value, 
					  dstList, new Temp.TempList(r2, null)));
			  emit(new OPER("\tsw $t2, " + dstOffset + "($fp)", null, null));
		  }
		  else if(n.right instanceof Tree.CONST) {
			  Temp.Temp r2 = n.left.accept(this);
			  int srcOffset = getOffset(r2.toString());
			  emit(new OPER("\tlw $t0, " + srcOffset + "($fp)", null, null));
			  emit(new OPER("\taddi $t2, $t0, " + ((Tree.CONST)n.right).value , 
					  dstList, new Temp.TempList(r2, null)));
			  emit(new OPER("\tsw $t2, " + dstOffset + "($fp)", null, null));
		  }
		  else {
			  Temp.Temp r2 = n.left.accept(this);
			  Temp.Temp r3 = n.right.accept(this);
			  int srcOffset1 = getOffset(r2.toString());
			  int srcOffset2 = getOffset(r3.toString());
			  emit(new OPER("\tlw $t0, " + srcOffset1 + "($fp)", null, null));
			  emit(new OPER("\tlw $t1, " + srcOffset2 + "($fp)", null, null));
			  emit(new OPER("\tadd $t2, $t0, $t1", 
					  dstList, new Temp.TempList(r2, new Temp.TempList(r3, null))));
			  emit(new OPER("\tsw $t2, " + dstOffset + "($fp)", null, null));
		  }
		  break;
	  case Tree.BINOP.MINUS:
		 /* if(n.left instanceof Tree.CONST && n.right instanceof Tree.CONST) {
			  emit(new OPER("\taddi $t2, " + ((Tree.CONST)n.left).value + ", " + (-1 * ((Tree.CONST)n.right).value) , 
					  dstList, null));
			  emit(new OPER("\tsw $t2, " + dstOffset + "($fp)", null, null));
		  }*/
		  if(n.right instanceof Tree.CONST) {
			  Temp.Temp r2 = n.left.accept(this);
			  int srcOffset = getOffset(r2.toString());
			  emit(new OPER("\tlw $t0, " + srcOffset + "($fp)", null, null));
			  emit(new OPER("\taddi $t2, $t0, " + (-1 * ((Tree.CONST)n.right).value) , 
					  dstList, new Temp.TempList(r2, null)));
			  emit(new OPER("\tsw $t2, " + dstOffset + "($fp)", null, null));
		  }
		  else {
			  Temp.Temp r2 = n.left.accept(this);
			  Temp.Temp r3 = n.right.accept(this);
			  int srcOffset1 = getOffset(r2.toString());
			  int srcOffset2 = getOffset(r3.toString());
			  emit(new OPER("\tlw $t0, " + srcOffset1 + "($fp)", null, null));
			  emit(new OPER("\tlw $t1, " + srcOffset2 + "($fp)", null, null));
			  emit(new OPER("\tsub $t2, $t0, $t1", 
					  dstList, new Temp.TempList(r2, new Temp.TempList(r3, null))));
			  emit(new OPER("\tsw $t2, " + dstOffset + "($fp)", null, null));
		  }
		  break;
	  case Tree.BINOP.MUL:
		  Temp.Temp r4 = n.left.accept(this);
		  Temp.Temp r5 = n.right.accept(this);
		  int srcOffset4 = getOffset(r4.toString());
		  int srcOffset5 = getOffset(r5.toString());
		  emit(new OPER("\tlw $t0, " + srcOffset4 + "($fp)", null, null));
		  emit(new OPER("\tlw $t1, " + srcOffset5 + "($fp)", null, null));
		  emit(new OPER("\tmul $t2, $t0, $t1", dstList, new Temp.TempList(r4, new Temp.TempList(r5, null))));
		  emit(new OPER("\tsw $t2, " + dstOffset + "($fp)", null, null));
		  break;
	  case Tree.BINOP.AND:
		  /*if(n.left instanceof Tree.CONST && n.right instanceof Tree.CONST) {
			  emit(new OPER("\tandi $t2, " + ((Tree.CONST)n.left).value + ", " + ((Tree.CONST)n.right).value , 
					  dstList, null));
			  emit(new OPER("\tsw $t2, " + dstOffset + "($fp)", null, null));
		  }*/
		  if(n.left instanceof Tree.CONST) {
			  Temp.Temp r2 = n.right.accept(this);
			  int srcOffset2 = getOffset(r2.toString());
			  emit(new OPER("\tlw $t0, " + srcOffset2 + "($fp)", null, null));
			  emit(new OPER("\tandi $t2, $t0, " + ((Tree.CONST)n.left).value, 
					  dstList, new Temp.TempList(r2, null)));
			  emit(new OPER("\tsw $t2, " + dstOffset + "($fp)", null, null));
		  }
		  else if(n.right instanceof Tree.CONST) {
			  Temp.Temp r2 = n.left.accept(this);
			  int srcOffset2 = getOffset(r2.toString());
			  emit(new OPER("\tlw $t0, " + srcOffset2 + "($fp)", null, null));
			  emit(new OPER("\tandi $t2, $t0, " + ((Tree.CONST)n.right).value , 
					  dstList, new Temp.TempList(r2, null)));
			  emit(new OPER("\tsw $t2, " + dstOffset + "($fp)", null, null));
		  }
		  else {
			  Temp.Temp r2 = n.left.accept(this);
			  Temp.Temp r3 = n.right.accept(this);
			  int srcOffset2 = getOffset(r2.toString());
			  int srcOffset3 = getOffset(r3.toString());
			  emit(new OPER("\tlw $t0, " + srcOffset2 + "($fp)", null, null));
			  emit(new OPER("\tlw $t1, " + srcOffset3 + "($fp)", null, null));
			  emit(new OPER("\tand $t2, $t0, $t1", dstList, 
					  new Temp.TempList(r2, new Temp.TempList(r3, null))));
			  emit(new OPER("\tsw $t2, " + dstOffset + "($fp)", null, null));
		  }
		  break;		  
	  }
	  return r1;
  }

  public Temp.Temp visit(Tree.MEM n)
  {
    // TEST DONE: fill in
	  Temp.Temp r1 = new Temp.Temp();
	  int dstOffset = getOffset(r1.toString());
	  if(n.exp instanceof Tree.CONST) {
		  emit(new OPER("\tli $t0, " + ((Tree.CONST)n.exp).value, null, null));
		  emit(new OPER("\tlw $t2, 0($t0)" , new Temp.TempList(r1, null), null));
		  emit(new OPER("\tsw $t2, " + dstOffset + "($fp)", null, null));
		  //emit(new OPER("\tlw `d0, 0(" + ((Tree.CONST)n.exp).value + ")" , new Temp.TempList(r1, null), null));
	  }
	  else {
		  Temp.Temp addr = n.exp.accept(this);
		  int addrOffset = getOffset(addr.toString());
		  emit(new OPER("\tlw $t0, " + addrOffset + "($fp)", null, null));
		  emit(new OPER("\tlw $t2, 0($t0)", new Temp.TempList(r1, null), new Temp.TempList(addr, null)));
		  emit(new OPER("\tsw $t2, " + dstOffset + "($fp)", null, null));
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
	  int dstOffset = getOffset(t.toString());  
	  emit(new Assem.OPER("\taddi $t2, $0, " + n.value , new Temp.TempList(t, null), new Temp.TempList(MipsFrame.ZERO, null)));
	  emit(new OPER("\tsw $t2, " + dstOffset + "($fp)", null, null));
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
			  int arg0Offset = getOffset(arg0.toString());
			  emit(new OPER("\tlw $t0, " + arg0Offset +"($fp)", null, null));
			  emit(new MOVE("\tmove $a0, $t0" , MipsFrame.A0, arg0));
			  args = args.tail;
			  i = i + 1;
			  break;
		  case 1:
			  Temp.Temp arg1 = args.head.accept(this);
			  int arg1Offset = getOffset(arg1.toString());
			  emit(new OPER("\tlw $t0, " + arg1Offset +"($fp)", null, null));
			  emit(new MOVE("\tmove $a1, $t0", MipsFrame.A1, arg1));
			  args = args.tail;
			  i = i + 1;
			  break;
		  case 2:
			  Temp.Temp arg2 = args.head.accept(this);
			  int arg2Offset = getOffset(arg2.toString());
			  emit(new OPER("\tlw $t0, " + arg2Offset +"($fp)", null, null));
			  emit(new MOVE("\tmove $a2, $t0", MipsFrame.A2, arg2));
			  args = args.tail;
			  i = i + 1;
			  break;
		  case 3:
			  Temp.Temp arg3 = args.head.accept(this);
			  int arg3Offset = getOffset(arg3.toString());
			  emit(new OPER("\tlw $t0, " + arg3Offset +"($fp)", null, null));
			  emit(new MOVE("\tmove $a3, $t0", MipsFrame.A3, arg3));
			  args = args.tail;
			  i = i + 1;
			  break;
		  }
	  }
	  
	  Temp.Label fl = ((Tree.NAME)n.func).label;
	  emit(new OPER("\tjal `j0", null, null, new Temp.LabelList(fl, null)));
	  
	  int v0Offset = getOffset(MipsFrame.V0.toString());
	  emit(new OPER("\tsw $v0, " + v0Offset + "($fp)", null, null));
	  
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
	  System.out.println(frame.name.toString() + ":");
	  //Instr tempInstr = new MOVE("\tmove `d0, `s0",frame.FP(), MipsFrame.SP);
	  Instr tempInstr = new MOVE("\tmove $fp, $sp",frame.FP(), MipsFrame.SP);
	  System.out.println(tempInstr.format(new Temp.RegMap(frame)));
	  
	  Temp.Temp r2 = new Temp.Temp();
	  //int r2Offset = getOffset(r2.toString());
	  tempInstr = new OPER("\taddi $t2, $0, " + 800 , 
			  new Temp.TempList(r2, null), new Temp.TempList(MipsFrame.ZERO, null));
	  System.out.println(tempInstr.format(new Temp.RegMap(frame))); 
	  
	  tempInstr = new OPER("\tsub $sp, $sp, $t2", new Temp.TempList(MipsFrame.SP, null), 
			  new Temp.TempList(MipsFrame.SP, new Temp.TempList(r2, null)));
	  System.out.println(tempInstr.format(new Temp.RegMap(frame))); 
	  
	  tempInstr = new OPER("\tsw $ra, 0($fp)", new Temp.TempList(frame.FP(), null), 
			  new Temp.TempList(MipsFrame.RA, null));
	  System.out.println(tempInstr.format(new Temp.RegMap(frame)));
  }
  
  public void epilogue() {
	  //Temp.Temp r = new Temp.Temp();
	  Temp.TempMap map = new Temp.RegMap(frame);
	  
	  /*= new OPER("lw `d0, 0(`s0)", new Temp.TempList(ra, null), 
			  new Temp.TempList(frame.FP(), null)); 
	  System.out.println(tempInstr.format(new Temp.RegMap(frame)));
	  
	  tempInstr = new MOVE("move `d0, `s0", MipsFrame.RA, ra);
	  System.out.println(tempInstr.format(new Temp.RegMap(frame)));
	  tempInstr = new OPER("jr `s0", null, new Temp.TempList(MipsFrame.RA, null));
	  System.out.println(tempInstr.format(new Temp.RegMap(frame)));*/
	  
	  Instr tempInstr = new OPER("\tlw $t0, 0($fp)", null, null);
	  System.out.println(tempInstr.format(map));
	  tempInstr = new MOVE("\tmove $ra, $t0", null, null);
	  System.out.println(tempInstr.format(map));
	  
	  tempInstr = new MOVE("\tmove `d0, `s0", MipsFrame.SP, frame.FP());
	  System.out.println(tempInstr.format(map));
	  
	  tempInstr = new OPER("\taddi `d0, `s0, " + 800, new Temp.TempList(frame.FP(), null), 
			  new Temp.TempList(frame.FP(), null));
	  System.out.println(tempInstr.format(map));
	  
	  if(frame.name.toString() == "main") {
		  System.out.println("\tj _finish");
	  }
	  else {
		  System.out.println("\tjr $ra");
	  }
  }
  
  public Tree.ExpList reverse(Tree.ExpList args_in) {
	  Tree.ExpList args_out = null;
	  while (args_in != null) {
		  args_out = new Tree.ExpList(args_in.head, args_out);
		  args_in = args_in.tail;
	  }
	  return args_out;
  }
  
  public int getOffset(String temp) {
	  int res; 
	  if(tempOffset.get(temp) != null) {
		  res = (int)tempOffset.get(temp);
	  }
	  else {
		  tempOffset.put(temp, offset);
		  res = offset;
		  offset -= 4;
	  }  
	  return res;
  }
}
