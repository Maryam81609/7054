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

  private final int frameSize = 1600;
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
	  emit(new LABEL(n.label.toString() + ":\n", n.label));
  }

  public void visit(Tree.JUMP n)
  {
    // TEST DONE: fill in
	  if(n.exp instanceof Tree.NAME) {
		  Temp.Label l = ((Tree.NAME)n.exp).label;
		  emit(new OPER("\tj `j0", null, null, new Temp.LabelList(l, null)));
	  }
	  else {
		  throw new Error("Jump to no name or label.");
		  /*Temp.Temp r1 = n.exp.accept(this);
		  int jrOffset = getOffset(r1.toString());
		  emit(new OPER("\tlw $t2, " + jrOffset + "($fp)", null, null));
		  emit(new OPER("\tjr $t2", new Temp.TempList(r1, null), null));*/
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
	  
	  if(n.dst instanceof Tree.TEMP) {
		  Temp.Temp r1 = n.dst.accept(this); 
		  int dstOffset = getOffset(r1.toString());
		  if(((Tree.TEMP)n.dst).temp == frame.RV()) {
			  Temp.Temp r2 = n.src.accept(this);
			  int srcOffset = getOffset(r2.toString());
			  
			  emit(new OPER("\tlw $v0, " + srcOffset + "($fp)", null, null));
			  /*emit(new MOVE("\tmove $t2, $t0", r1, r2));
			  emit(new OPER("\tsw $t2, " + dstOffset + "($fp)", null, null));//, r1, r2));*/
		  }
		  else if(n.src instanceof Tree.CONST) { 
			  emit(new OPER("\tli $t2, " + ((Tree.CONST)n.src).value, new Temp.TempList(r1, null), null));
			  emit(new OPER("\tsw $t2, " + dstOffset + "($fp)", new Temp.TempList(r1, null), null));
		  }

		  else if(n.src instanceof Tree.MEM) {
			  Temp.Temp src = n.src.accept(this);
			  int srcOffset = getOffset(src.toString());
			  emit(new OPER("\tlw $t0, " + srcOffset + "($fp)", null, null));
			  emit(new MOVE("\tmove $t2, $t0", r1, src));
			  emit(new OPER("\tsw $t2, " + dstOffset + "($fp)", null, null));
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
		  Temp.Temp dstAddr = ((Tree.MEM)n.dst).exp.accept(this);
		  int dstAddrOffset = getOffset(dstAddr.toString());
		  if(n.src instanceof Tree.CONST) {
			  emit(new OPER("\tlw $t2, " + dstAddrOffset + "($fp)", null, null));
			  emit(new OPER("\tli $t0, " + ((Tree.CONST)n.src).value, null, null));
			  emit(new OPER("\tsw $t0, 0($t2)", null, null));//new Temp.TempList(r1, null), null));
			  //emit(new OPER("\tsw " + ((Tree.CONST)n.src).value + ", `d0", new Temp.TempList(r1, null), null));
		  }
		  else {
			  Temp.Temp r2 = n.src.accept(this);
			  int srcOffset = getOffset(r2.toString());
			  emit(new OPER("\tlw $t0, " + srcOffset + "($fp)", null, null));
			  emit(new OPER("\tlw $t2, " + dstAddrOffset + "($fp)", null, null)); 
			  emit(new OPER("\tsw $t0, 0($t2)", null, null));
		  }
	  }
	  else {
		 throw new Error("destination is other than MEM or TEMP: " + n.dst.toString());
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
			  if(r2 == MipsFrame.S8) {
				  emit(new OPER("\tadd $t0, $fp, $0", null, null));
			  }
			  else {
				  int srcOffset = getOffset(r2.toString());
				  emit(new OPER("\tlw $t0, " + srcOffset + "($fp)", null, null));
			  }
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
	  
	  Tree.ExpList args = n.args;//reverse(n.args);	  
	  int i = 0;
	  while(args != null) {
		  if(i < 4) {
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
			  default: 
				  break;
			  }
		  }
		  else {
			  Temp.Temp argi = args.head.accept(this);
			  int argiOffset = getOffset(argi.toString());
			  int spOffset = (i - 3) * 4;
			  emit(new OPER("\tlw $t0, " + argiOffset +"($fp)", null, null));
			  emit(new OPER("\tsw $t0, " + spOffset + "($sp)", null, null));
			  args = args.tail;
			  i = i + 1;
		  }
	  }
	  
	  //Store rest of the args at $sp + 4, + 8, ...
	  /*if(args != null) {
		  Temp.Temp arg = args.head.accept(this);
	  }*/
	  
	  Temp.Label fl = ((Tree.NAME)n.func).label;
	  emit(new OPER("\tjal `j0", null, null, new Temp.LabelList(fl, null)));
	  
	  Temp.Temp retValTemp = frame.RV();
	  int retValTempOffset = getOffset(retValTemp.toString());
	  //emit(new MOVE("\tmove $v0, `s0", null, frame.RV()));
	  emit(new OPER("\tsw $v0, " + retValTempOffset + "($fp)", null, null));
	  
	  return retValTemp;//frame.RV();
  }

  public void prologue() {
	  /*
	   * lw $t0, 12($fp)
	   * sw $t0, -16($fp)
	   * addi $t3, $fp, 12
	   * lw $t3, 0($t3)
	   * sw $t3, -20($fp)
	   * 
	   */
	  
	  System.out.println(frame.name.toString() + ":");
	  Instr tempInstr = new MOVE("\tmove $fp, $sp",frame.FP(), MipsFrame.SP);
	  System.out.println(tempInstr.format(new Temp.RegMap(frame)));
	  
	  Temp.Temp r2 = new Temp.Temp();
	  //int r2Offset = getOffset(r2.toString());
	  tempInstr = new OPER("\taddi $t2, $0, " + frameSize , 
			  new Temp.TempList(r2, null), new Temp.TempList(MipsFrame.ZERO, null));
	  System.out.println(tempInstr.format(new Temp.RegMap(frame))); 
	  
	  tempInstr = new OPER("\tsub $sp, $sp, $t2", new Temp.TempList(MipsFrame.SP, null), 
			  new Temp.TempList(MipsFrame.SP, new Temp.TempList(r2, null)));
	  System.out.println(tempInstr.format(new Temp.RegMap(frame))); 
	  
	  tempInstr = new OPER("\tsw $ra, 0($fp)", new Temp.TempList(frame.FP(), null), 
			  new Temp.TempList(MipsFrame.RA, null));
	  System.out.println(tempInstr.format(new Temp.RegMap(frame)));
	  
	  if(frame.name.toString() != "main") {
		  int formalsCount = getCounts(frame.formals); 
		  int i = formalsCount - 1;
		  
		  while(i >= 0) {
			  if(i >= 4) {
				  int aiOffset = getOffset(new Temp.Temp().toString());
				  int aiPreOffset = (i - 3) * 4;
				  //Load Arg From Caller Frame
				  tempInstr = new OPER("\tlw $t0, " + aiPreOffset + "($fp)", null, null);
				  System.out.println(tempInstr.format(new Temp.RegMap(frame)));
				  tempInstr = new OPER("\tsw $t0, " + aiOffset + "($fp)", null, null);
				  System.out.println(tempInstr.format(new Temp.RegMap(frame)));
			  }
			  else {
				  switch(i) {
				  case 3:
					  int a3Offset = getOffset(MipsFrame.A3.toString());
					  tempInstr = new OPER("\tsw $a3, " + a3Offset + "($fp)", null, null);
					  System.out.println(tempInstr.format(new Temp.RegMap(frame)));
					  break;
				  case 2:
					  int a2Offset = getOffset(MipsFrame.A2.toString());
					  tempInstr = new OPER("\tsw $a2, " + a2Offset + "($fp)", null, null);
					  System.out.println(tempInstr.format(new Temp.RegMap(frame)));
					  break;
				  case 1:
					  int a1Offset = getOffset(MipsFrame.A1.toString());
					  tempInstr = new OPER("\tsw $a1, " + a1Offset + "($fp)", null, null);
					  System.out.println(tempInstr.format(new Temp.RegMap(frame)));
					  break;
				  case 0:
					  int a0Offset = getOffset(MipsFrame.A0.toString());
					  tempInstr = new OPER("\tsw $a0, " + a0Offset + "($fp)", null, null);
					  System.out.println(tempInstr.format(new Temp.RegMap(frame)));
					  break;
				  }  
			  }
			  i = i - 1;
		  }
	  }
  }
  
  public void epilogue() {
	  //Temp.Temp r = new Temp.Temp();
	  Temp.TempMap map = new Temp.RegMap(frame);
	  
	  Instr tempInstr = new OPER("\tlw $t0, 0($fp)", null, null);
	  System.out.println(tempInstr.format(map));
	  tempInstr = new MOVE("\tmove $ra, $t0", null, null);
	  System.out.println(tempInstr.format(map));
	  
	  tempInstr = new MOVE("\tmove `d0, `s0", MipsFrame.SP, frame.FP());
	  System.out.println(tempInstr.format(map));
	  
	  tempInstr = new OPER("\taddi `d0, `s0, " + frameSize, new Temp.TempList(frame.FP(), null), 
			  new Temp.TempList(frame.FP(), null));
	  System.out.println(tempInstr.format(map));
	  
	  if(frame.name.toString() == "main") {
		  System.out.println("\tj _finish");
	  }
	  else {
		  System.out.println("\tjr $ra");
	  }
  }
  
  public int getCounts(Frame.AccessList formals) {
	  int count = 0;
	  Frame.AccessList args = formals;
	  while(args != null) {
		  count += 1;
		  args = args.tail;
	  }
	  
	  return count;
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
