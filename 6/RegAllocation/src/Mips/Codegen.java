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

public class Codegen implements TempVisitor
{

  private final int frameSize = 800;
  private int offset = -136;
  private HashMap<String, Integer> tempOffset;
  private InstrList codeList, listTail;
  private MipsFrame frame;

  public Codegen(MipsFrame f)
  {
    frame = f;
    codeList = listTail = null;
    tempOffset = new HashMap<String, Integer>();
    offset = -8;
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
	  emit(new LABEL(n.label.toString() + ":", n.label));
  }

  public void visit(Tree.JUMP n)
  {
    // DONE: fill in
	  if(n.exp instanceof Tree.NAME) {
		  Temp.Label l = ((Tree.NAME)n.exp).label;
		  emit(new OPER("\tj `j0", null, null, new Temp.LabelList(l, null)));
	  }
	  else {
		  throw new Error("Jump to no name or label.");
	  }
  }

  public void visit(Tree.CJUMP n)
  {
    // DONE: fill in
	  Temp.Temp r2 = n.left.accept(this);
	  Temp.Temp r3 = n.right.accept(this);
	  switch(n.relop) {
	  case Tree.CJUMP.NE:
		  emit(new OPER("\tbne `s0, `s1, `j0", null, new Temp.TempList(r2, new Temp.TempList(r3, null)),
				  new Temp.LabelList(n.iftrue, new Temp.LabelList(n.iffalse, null))));
		  break;
	  case Tree.CJUMP.GE:
		  emit(new OPER("\tbge `s0, `s1, `j0" , null, new Temp.TempList(r2, new Temp.TempList(r3, null)), 
				  new Temp.LabelList(n.iftrue, new Temp.LabelList(n.iffalse, null))));
		  break;
	  case Tree.CJUMP.LT:
		  emit(new OPER("\tblt `s0, `s1, `j0" , null, new Temp.TempList(r2, new Temp.TempList(r3, null)), 
				  new Temp.LabelList(n.iftrue, new Temp.LabelList(n.iffalse, null))));
		  break;
	  default:
		  break;
	  } 
  }

  public void visit(Tree.MOVE n)
  {
	  // DONE: fill in
	  
	  if(n.dst instanceof Tree.TEMP) {
		  Temp.Temp r1 = n.dst.accept(this); 
		  if(((Tree.TEMP)n.dst).temp == frame.RV()) {
			  Temp.Temp r2 = n.src.accept(this);
			  emit(new OPER("\tmove `d0, `s0", new Temp.TempList(r1, null), new Temp.TempList(r2, null)));
		  }
		  else if(n.src instanceof Tree.CONST) {
			  emit(new OPER("\tli `d0, " + ((Tree.CONST)n.src).value, new Temp.TempList(r1, null), null));
		  }

		  else if(n.src instanceof Tree.MEM) {
			  Temp.Temp src = n.src.accept(this);
			  emit(new MOVE("\tmove `d0, `s0", r1, src));
		  }
		  else {
			  // if function call, then r2 is the return value of the function
			  Temp.Temp r2 = n.src.accept(this);
			  emit(new MOVE("\tmove `d0, `s0", r1, r2));
		 }
	  }
	  else if(n.dst instanceof Tree.MEM) {
		  Temp.Temp dstAddr = ((Tree.MEM)n.dst).exp.accept(this);
		  if(n.src instanceof Tree.CONST) {
			  Temp.Temp srcTemp = new Temp.Temp();
			  emit(new OPER("\tli `d0, " + ((Tree.CONST)n.src).value, new Temp.TempList(srcTemp, null), null));
			  emit(new OPER("\tsw `s0, 0(`s1)", null, new Temp.TempList(srcTemp, new Temp.TempList(dstAddr, null))));
		  }
		  else {
			  Temp.Temp r2 = n.src.accept(this);	   
			  emit(new OPER("\tsw `s0, 0(`s1)", null, new Temp.TempList(r2, new Temp.TempList(dstAddr, null))));
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
	  // DONE: fill in
	  Temp.Temp r1 = new Temp.Temp();
	  Temp.TempList dstList = new Temp.TempList(r1, null);
	  switch(n.binop) {
	  case Tree.BINOP.PLUS:	
		  if(n.left instanceof Tree.CONST) {
			  Temp.Temp r2 = n.right.accept(this);
			  emit(new OPER("\taddi `d0, `s0, " + ((Tree.CONST)n.left).value, 
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
		  if(n.right instanceof Tree.CONST) {
			  Temp.Temp r2 = n.left.accept(this);
			  emit(new OPER("\taddi `d0, `s0, " + (-1 * ((Tree.CONST)n.right).value) , 
					  dstList, new Temp.TempList(r2, null)));
		  }
		  else {
			  Temp.Temp r2 = n.left.accept(this);
			  Temp.Temp r3 = n.right.accept(this);
			  emit(new OPER("\tsub `d0, `s0, `s1", 
					  dstList, new Temp.TempList(r2, new Temp.TempList(r3, null))));
		  }
		  break;
	  case Tree.BINOP.MUL:
		  Temp.Temp r4 = n.left.accept(this);
		  Temp.Temp r5 = n.right.accept(this);
		  emit(new OPER("\tmul `d0, `s0, `s1", dstList, new Temp.TempList(r4, new Temp.TempList(r5, null))));
		  break;
	  case Tree.BINOP.AND:
		  if(n.left instanceof Tree.CONST) {
			  Temp.Temp r2 = n.right.accept(this);
			  emit(new OPER("\tandi `d0, `s0, " + ((Tree.CONST)n.left).value, dstList, new Temp.TempList(r2, null)));
		  }
		  else if(n.right instanceof Tree.CONST) {
			  Temp.Temp r2 = n.left.accept(this);
			  emit(new OPER("\tandi `d0, `s0, " + ((Tree.CONST)n.right).value, 
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
    // DONE: fill in
	  Temp.Temp r1 = new Temp.Temp();  
	  Temp.Temp addr = n.exp.accept(this);
	  emit(new OPER("\tlw `d0, 0(`s0)", new Temp.TempList(r1, null), new Temp.TempList(addr, null)));
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
	  // DONE: fill in 
	  Temp.Temp t = new Temp.Temp();
	  emit(new Assem.OPER("\taddi `d0, `s0, " + n.value , 
			  new Temp.TempList(t, null), new Temp.TempList(MipsFrame.ZERO, null)));
	  return t;
  }

  public Temp.Temp visit(Tree.CALL n)
  {
    // DONE: fill in
	 
	  Temp.Label fl = ((Tree.NAME)n.func).label;
	  String funcName = fl.toString();
	  if(!funcName.equals("_printInt") && !funcName.equals("_newl") 
			  && !funcName.equals("_alloc") && !funcName.equals("_finish")) {
		  // store caller-saved registers
		  int T0Offset = getOffset(MipsFrame.T0.toString());
		  emit(new OPER("\tsw `s0, " + T0Offset + "(`s1)", 
				  null, new Temp.TempList(MipsFrame.T0, new Temp.TempList(frame.FP(), null))));
		  
		  int T1Offset = getOffset(MipsFrame.T1.toString());
		  emit(new OPER("\tsw `s0, " + T1Offset + "(`s1)", 
				  null, new Temp.TempList(MipsFrame.T1, new Temp.TempList(frame.FP(), null))));
		  
		  int T2Offset = getOffset(MipsFrame.T2.toString());
		  emit(new OPER("\tsw `s0, " + T2Offset + "(`s1)", 
				  null, new Temp.TempList(MipsFrame.T2, new Temp.TempList(frame.FP(), null))));
		  
		  int T3Offset = getOffset(MipsFrame.T3.toString());
		  emit(new OPER("\tsw `s0, " + T3Offset + "(`s1)", 
				  null, new Temp.TempList(MipsFrame.T3, new Temp.TempList(frame.FP(), null))));
		  
		  int T4Offset = getOffset(MipsFrame.T4.toString());
		  emit(new OPER("\tsw `s0, " + T4Offset + "(`s1)", 
				  null, new Temp.TempList(MipsFrame.T4, new Temp.TempList(frame.FP(), null))));
		  
		  int T5Offset = getOffset(MipsFrame.T5.toString());
		  emit(new OPER("\tsw `s0, " + T5Offset + "(`s1)", 
				  null, new Temp.TempList(MipsFrame.T5, new Temp.TempList(frame.FP(), null))));
		  
		  int T6Offset = getOffset(MipsFrame.T6.toString());
		  emit(new OPER("\tsw `s0, " + T6Offset + "(`s1)", 
				  null, new Temp.TempList(MipsFrame.T6, new Temp.TempList(frame.FP(), null))));
		  
		  int T7Offset = getOffset(MipsFrame.T7.toString());
		  emit(new OPER("\tsw `s0, " + T7Offset + "(`s1)", 
				  null, new Temp.TempList(MipsFrame.T7, new Temp.TempList(frame.FP(), null))));
		  
		  int T8Offset = getOffset(MipsFrame.T8.toString());
		  emit(new OPER("\tsw `s0, " + T8Offset + "(`s1)", 
				  null, new Temp.TempList(MipsFrame.T8, new Temp.TempList(frame.FP(), null))));
		  
		  int T9Offset = getOffset(MipsFrame.T9.toString());
		  emit(new OPER("\tsw `s0, " + T9Offset + "(`s1)", 
				  null, new Temp.TempList(MipsFrame.T9, new Temp.TempList(frame.FP(), null))));
	  }
	  
	  Tree.ExpList args = n.args;	  
	  int i = 0;
	  while(args != null) {
		  if(i < 4) {
			  switch(i) {
			  case 0:
				  Temp.Temp arg0 = args.head.accept(this);
				  emit(new MOVE("\tmove `d0, `s0" , MipsFrame.A0, arg0));		
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
			  default: 
				  break;
			  }
		  }
		  else {
			  Temp.Temp argi = args.head.accept(this);
			  int spOffset = (i - 3) * 4;
			  emit(new OPER("\tsw `s0, " + spOffset + "(`s1)", 
					  null, new Temp.TempList(argi, new Temp.TempList(MipsFrame.SP, null))));
			  args = args.tail;
			  i = i + 1;
		  }
	  }
	  
	  
	  emit(new OPER("\tjal `j0", null, null, new Temp.LabelList(fl, null)));
	  
	  
	  // restore caller-saved registers
	  if(!funcName.equals("_printInt") && !funcName.equals("_newl") 
			  && !funcName.equals("_alloc") && !funcName.equals("_finish")) {
		  int T0Offset = getOffset(MipsFrame.T0.toString());
		  emit(new OPER("\tlw `d0, " + T0Offset + "(`s0)", 
				  new Temp.TempList(MipsFrame.T0, null), new Temp.TempList(frame.FP(), null)));
		  
		  int T1Offset = getOffset(MipsFrame.T1.toString());
		  emit(new OPER("\tlw `d0, " + T1Offset + "(`s0)", 
				  new Temp.TempList(MipsFrame.T1, null), new Temp.TempList(frame.FP(), null)));
		  
		  int T2Offset = getOffset(MipsFrame.T2.toString());
		  emit(new OPER("\tlw `d0, " + T2Offset + "(`s0)", 
				  new Temp.TempList(MipsFrame.T2, null), new Temp.TempList(frame.FP(), null)));
		  
		  int T3Offset = getOffset(MipsFrame.T3.toString());
		  emit(new OPER("\tlw `d0, " + T3Offset + "(`s0)", 
				  new Temp.TempList(MipsFrame.T3, null), new Temp.TempList(frame.FP(), null)));
		  
		  int T4Offset = getOffset(MipsFrame.T4.toString());
		  emit(new OPER("\tlw `d0, " + T4Offset + "(`s0)", 
				  new Temp.TempList(MipsFrame.T4, null), new Temp.TempList(frame.FP(), null)));
		  
		  int T5Offset = getOffset(MipsFrame.T5.toString());
		  emit(new OPER("\tlw `d0, " + T5Offset + "(`s0)", 
				  new Temp.TempList(MipsFrame.T5, null), new Temp.TempList(frame.FP(), null)));
		  
		  int T6Offset = getOffset(MipsFrame.T6.toString());
		  emit(new OPER("\tlw `d0, " + T6Offset + "(`s0)", 
				  new Temp.TempList(MipsFrame.T6, null), new Temp.TempList(frame.FP(), null)));
		  
		  int T7Offset = getOffset(MipsFrame.T7.toString());
		  emit(new OPER("\tlw `d0, " + T7Offset + "(`s0)", 
				  new Temp.TempList(MipsFrame.T7, null), new Temp.TempList(frame.FP(), null)));
		  
		  int T8Offset = getOffset(MipsFrame.T8.toString());
		  emit(new OPER("\tlw `d0, " + T8Offset + "(`s0)", 
				  new Temp.TempList(MipsFrame.T8, null), new Temp.TempList(frame.FP(), null)));
		  
		  int T9Offset = getOffset(MipsFrame.T9.toString());
		  emit(new OPER("\tlw `d0, " + T9Offset + "(`s0)", 
				  new Temp.TempList(MipsFrame.T9, null), new Temp.TempList(frame.FP(), null)));
	  }
	  return frame.RV();
  }

  public InstrList prologue() {	
	  
	  emit(new LABEL(frame.name.toString() + ":", frame.name));
	  Instr tempInstr = new MOVE("\tmove `d0, `s0",frame.FP(), MipsFrame.SP);
	  emit(tempInstr);
	  
	  Temp.Temp r2 = new Temp.Temp();
	  tempInstr = new OPER("\taddi `d0, `s0, " + frameSize , 
			  new Temp.TempList(r2, null), new Temp.TempList(MipsFrame.ZERO, null));
	  emit(tempInstr);
	  tempInstr = new OPER("\tsub `d0, `s0, `s1", new Temp.TempList(MipsFrame.SP, null), 
			  new Temp.TempList(MipsFrame.SP, new Temp.TempList(r2, null)));
	  emit(tempInstr);
	  
	  tempInstr = new OPER("\tsw `s0, 0(`s1)", null, 
			  new Temp.TempList(MipsFrame.RA, new Temp.TempList(frame.FP(), null)));
	  emit(tempInstr);
	  
	  if(frame.name.toString() != "main") {	  
		// store callee-save registers
		  int S0Offset = getOffset(MipsFrame.S0.toString());
		  emit(new OPER("\tsw `s0, " + S0Offset + "(`s1)", 
				  null, new Temp.TempList(MipsFrame.S0, new Temp.TempList(frame.FP(), null))));
		  
		  int S1Offset = getOffset(MipsFrame.S1.toString());
		  emit(new OPER("\tsw `s0, " + S1Offset + "(`s1)", 
				  null, new Temp.TempList(MipsFrame.S1, new Temp.TempList(frame.FP(), null))));
		  
		  int S2Offset = getOffset(MipsFrame.S2.toString());
		  emit(new OPER("\tsw `s0, " + S2Offset + "(`s1)", 
				  null, new Temp.TempList(MipsFrame.S2, new Temp.TempList(frame.FP(), null))));
		  
		  int S3Offset = getOffset(MipsFrame.S3.toString());
		  emit(new OPER("\tsw `s0, " + S3Offset + "(`s1)", 
				  null, new Temp.TempList(MipsFrame.S3, new Temp.TempList(frame.FP(), null))));
		  
		  int S4Offset = getOffset(MipsFrame.S4.toString());
		  emit(new OPER("\tsw `s0, " + S4Offset + "(`s1)", 
				  null, new Temp.TempList(MipsFrame.S4, new Temp.TempList(frame.FP(), null))));
		  
		  int S5Offset = getOffset(MipsFrame.S5.toString());
		  emit(new OPER("\tsw `s0, " + S5Offset + "(`s1)", 
				  null, new Temp.TempList(MipsFrame.S5, new Temp.TempList(frame.FP(), null))));
		  
		  int S6Offset = getOffset(MipsFrame.S6.toString());
		  emit(new OPER("\tsw `s0, " + S6Offset + "(`s1)", 
				  null, new Temp.TempList(MipsFrame.S6, new Temp.TempList(frame.FP(), null))));
		  
		  int S7Offset = getOffset(MipsFrame.S7.toString());
		  emit(new OPER("\tsw `s0, " + S7Offset + "(`s1)", 
				  null, new Temp.TempList(MipsFrame.S7, new Temp.TempList(frame.FP(), null))));
		
		  
		  int formalsCount = getCounts(frame.formals); 
		  int i = formalsCount - 1;
		  
		  while(i >= 0) {
			  if(i >= 4) {
				  Temp.Temp argTemp = new Temp.Temp();
				  int aiPreOffset = (i - 3) * 4;
				  //Load arg From Caller Frame
				  tempInstr = new OPER("\tlw `d0, " + aiPreOffset + "(`s0)", 
						  new Temp.TempList(argTemp, null), new Temp.TempList(frame.FP(), null));
				  emit(tempInstr);
				  int aiOffset = getOffset(new Temp.Temp().toString());
				  tempInstr = new OPER("\tsw `s0, " + aiOffset + "(`s1)", 
						  null, new Temp.TempList(argTemp, new Temp.TempList(frame.FP(), null)));
				  emit(tempInstr);
			  }
			  else {
				  switch(i) {
				  case 3:
					  int a3Offset = getOffset(MipsFrame.A3.toString());
					  tempInstr = new OPER("\tsw `s0, " + a3Offset + "(`s1)", 
							  null, new Temp.TempList(MipsFrame.A3, new Temp.TempList(frame.FP(), null)));
					  emit(tempInstr);
					  break;
				  case 2:
					  int a2Offset = getOffset(MipsFrame.A2.toString());
					  tempInstr = new OPER("\tsw `s0, " + a2Offset + "(`s1)", 
							  null, new Temp.TempList(MipsFrame.A2, new Temp.TempList(frame.FP(), null)));
					  emit(tempInstr);
					  break;
				  case 1:
					  int a1Offset = getOffset(MipsFrame.A1.toString());
					  tempInstr = new OPER("\tsw `s0, " + a1Offset + "(`s1)", 
							  null, new Temp.TempList(MipsFrame.A1, new Temp.TempList(frame.FP(), null)));
					  emit(tempInstr);
					  break;
				  case 0:
					  int a0Offset = getOffset(MipsFrame.A0.toString());
					  tempInstr = new OPER("\tsw `s0, " + a0Offset + "(`s1)", 
							  null, new Temp.TempList(MipsFrame.A0, new Temp.TempList(frame.FP(), null)));
					  emit(tempInstr);
					  break;
				  }  
			  }
			  i = i - 1;
		  }
	  }
	  
	  InstrList result = codeList;
	  codeList = listTail = null;
	  return result;
  }
  
  public InstrList epilogue() {
	  
	  if(frame.name.toString() != "main") {	  
		  // restore caller-saved registers
		  int S0Offset = getOffset(MipsFrame.S0.toString());
		  emit(new OPER("\tlw `d0, " + S0Offset + "(`s0)", 
				  new Temp.TempList(MipsFrame.S0, null), new Temp.TempList(frame.FP(), null)));
		  
		  int S1Offset = getOffset(MipsFrame.S1.toString());
		  emit(new OPER("\tlw `d0, " + S1Offset + "(`s0)", 
				  new Temp.TempList(MipsFrame.S1, null), new Temp.TempList(frame.FP(), null)));
		  
		  int S2Offset = getOffset(MipsFrame.S2.toString());
		  emit(new OPER("\tlw `d0, " + S2Offset + "(`s0)", 
				  new Temp.TempList(MipsFrame.S2, null), new Temp.TempList(frame.FP(), null)));
		  
		  int S3Offset = getOffset(MipsFrame.S3.toString());
		  emit(new OPER("\tlw `d0, " + S3Offset + "(`s0)", 
				  new Temp.TempList(MipsFrame.S3, null), new Temp.TempList(frame.FP(), null)));
		  
		  int S4Offset = getOffset(MipsFrame.S4.toString());
		  emit(new OPER("\tlw `d0, " + S4Offset + "(`s0)", 
				  new Temp.TempList(MipsFrame.S4, null), new Temp.TempList(frame.FP(), null)));
		  
		  int S5Offset = getOffset(MipsFrame.S5.toString());
		  emit(new OPER("\tlw `d0, " + S5Offset + "(`s0)", 
				  new Temp.TempList(MipsFrame.S5, null), new Temp.TempList(frame.FP(), null)));
		  
		  int S6Offset = getOffset(MipsFrame.S6.toString());
		  emit(new OPER("\tlw `d0, " + S6Offset + "(`s0)", 
				  new Temp.TempList(MipsFrame.S6, null), new Temp.TempList(frame.FP(), null)));
		  
		  int S7Offset = getOffset(MipsFrame.S7.toString());
		  emit(new OPER("\tlw `d0, " + S7Offset + "(`s0)", 
					  new Temp.TempList(MipsFrame.S7, null), new Temp.TempList(frame.FP(), null)));
	  }
	  Instr tempInstr = new OPER("\tlw `d0, 0(`s0)", 
			  new Temp.TempList(MipsFrame.RA, null), new Temp.TempList(frame.FP(), null));
	  emit(tempInstr);
	  
	  tempInstr = new MOVE("\tmove `d0, `s0", MipsFrame.SP, frame.FP());
	  emit(tempInstr);
	  
	  tempInstr = new OPER("\taddi `d0, `s0, " + frameSize, new Temp.TempList(frame.FP(), null), 
			  new Temp.TempList(frame.FP(), null));
	  emit(tempInstr);
	  
	  if(frame.name.toString() == "main") {
		  tempInstr = new OPER("\tj _finish", null, null);
		  emit(tempInstr);
	  }
	  else {
		  tempInstr = new OPER("\tjr `d0", new Temp.TempList(MipsFrame.RA, null), null);
		  emit(tempInstr);
	  }
	   
	  InstrList result = codeList;
	  codeList = listTail = null;
	  return result;
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
