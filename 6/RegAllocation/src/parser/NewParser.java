/* Generated By:JavaCC: Do not edit this line. NewParser.java */
package parser;
import syntaxtree.*;
import visitor.*;
import symbol.*;
import Translate.Frag;
import Translate.ProcFrag;
import Translate.Translate;
import Mips.MipsFrame;
import RegAlloc.LiveAnalysis;
import RegAlloc.RegisterMap;
import Temp.RegMap;
//import Mips.SpimCodegen;
import Mips.Codegen;
import Canon.TraceSchedule;
import Canon.BasicBlocks;
import Canon.Canon;
import Tree.StmList;
import IR_visitor.GenCVisitor;
import IR_visitor.TempVisitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.SortedMap;
import java.util.Vector;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import Assem.Instr;
import Assem.InstrList;
import Assem.LABEL;
import FlowGraph.*;


public class NewParser implements NewParserConstants {
  public static void main(String[] args) throws FileNotFoundException {
        try{
        		//String inputFile = "/home/maryam/7054/6/generator_tests/func.java";
                File f = new File(args[0]);//inputFile); //
                InputStream in = new FileInputStream(f);
                Program root = new NewParser(in).Goal();
                SymbolTableVisitor symTblVisitor = new SymbolTableVisitor();
                root.accept(symTblVisitor);
                SymbolTable symTable = symTblVisitor.getTable();
                root.accept(new TypeCheckVisitor(symTable));
                
                // Satge4: IR Generation
                MipsFrame fr = new MipsFrame();
                Translate ir = new Translate(root, fr, symTable);
                
                Frag frag = ir.getResults();
                
                // CodeGenerator
                TempVisitor gen;
                MipsFrame frame;
                LinkedHashMap<String, ArrayList<Instr>> assemInstrs = new LinkedHashMap<String, ArrayList<Instr>>();;
                
                while(frag != null)
                {
                	ProcFrag pf = (ProcFrag) frag;
            		Tree.Stm fragBody = pf.body;
            	
            		// Canonicalize
            		TraceSchedule t = new TraceSchedule(new BasicBlocks(new Canon().linearize(fragBody)));
            	
            		// Generate Assembly Code
            		frame = (MipsFrame)pf.frame;
                	gen = new Codegen(frame);
            		
                    // Create CFG for the frag
                    CFGGenerator fragCfgGen = new CFGGenerator(frame.name.toString());
                    	
                    ArrayList<Instr> fragInstrList = new ArrayList<Instr>();
                    
            		InstrList proInstrs = gen.prologue();
            		fragInstrList.addAll(proInstrs.toList());
            		
            		// Create CFG Nodes for prologue
            		fragCfgGen.createNodes(proInstrs);
            		
            		Tree.StmList stms = t.stms;
            		while(stms != null) {
            			InstrList instrLst = gen.codegen(stms.head);
            			if(instrLst != null) {
            				fragInstrList.addAll(instrLst.toList());
            			}
            			
            			// Create CFG Nodes for function body
            			fragCfgGen.createNodes(instrLst);
            			
               			stms = stms.tail;
            		}
            		InstrList epiInstrs = gen.epilogue();
            		fragInstrList.addAll(epiInstrs.toList());
            		
            		assemInstrs.put(frame.name.toString(), fragInstrList);
            		
            		// Create CFG Nodes for epilogue
            		fragCfgGen.createNodes(epiInstrs);
            		
            		// Add CFG Edges for the created nodes
            		fragCfgGen.addEdges();
            		
            		// Create Interference graph here
            		LiveAnalysis la = new LiveAnalysis(fragCfgGen.getCFG(), frame.name.toString());
            		
            		frag = frag.next;
                }
                //LiveAnalysis.printTempMaps();
                //CFGGenerator.show(new Temp.RegMap(new MipsFrame()));
                //LiveAnalysis.show(new Temp.RegMap(new MipsFrame()));
                //LiveAnalysis.print(assemInstrs, new RegMap(new MipsFrame()));
                LiveAnalysis.print(assemInstrs, new RegisterMap());
                
    }
    catch(ParseException e){
      System.err.println("SyntaxError: " + e.getMessage());
    }
    catch(TokenMgrError e) {
      System.err.println("TokenMgrError: " + e.getMessage());
    }
  }

  private static void printCode(NewParser pars) {
    System.out.println("todo");
  }

  private static void printTokenInfo(NewParser pars) {
    Token t;
    for(t = pars.getNextToken(); t.kind != 0; t = pars.getNextToken()) {
      System.out.print(t.kind+" ");
    }
    System.out.print(t.kind);
    System.out.println();
  }

  final public Program Goal() throws ParseException {
  MainClass mc;
  ClassDeclList cdlist;
    mc = MainClass();
    cdlist = ClassDeclList();
    jj_consume_token(0);
                                                    {if (true) return new Program(mc, cdlist);}
    throw new Error("Missing return statement in function");
  }

  final public MainClass MainClass() throws ParseException {
  Token t1, t2;
  Statement s1;
    jj_consume_token(CLASS);
    t1 = jj_consume_token(ID);
    jj_consume_token(LBRACE);
    jj_consume_token(PUBLIC);
    jj_consume_token(STAT);
    jj_consume_token(VOID);
    jj_consume_token(MAIN);
    jj_consume_token(LPAREN);
    jj_consume_token(STRING);
    jj_consume_token(LBRACK);
    jj_consume_token(RBRACK);
    t2 = jj_consume_token(ID);
    jj_consume_token(RPAREN);
    jj_consume_token(LBRACE);
    s1 = Statement();
    jj_consume_token(RBRACE);
    jj_consume_token(RBRACE);
                                                {if (true) return new MainClass(new Identifier(t1.image), new Identifier(t2.image), s1);}
    throw new Error("Missing return statement in function");
  }

  final public ClassDeclList ClassDeclList() throws ParseException {
  ClassDeclList cdlist;
  ClassDecl cd;
   cdlist = new ClassDeclList();
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case CLASS:
        ;
        break;
      default:
        jj_la1[0] = jj_gen;
        break label_1;
      }
      cd = ClassDecl();
                                                        cdlist.addElement(cd);
    }
                                                                                      {if (true) return cdlist;}
    throw new Error("Missing return statement in function");
  }

  final public ClassDecl ClassDecl() throws ParseException {
  Token t1, t2, t3;
  VarDeclList vdlist1, vdlist2;
  MethodDeclList mdlist1, mdlist2;
    if (jj_2_1(3)) {
      jj_consume_token(CLASS);
      t1 = jj_consume_token(ID);
      jj_consume_token(LBRACE);
      vdlist1 = VarDeclList();
      mdlist1 = MethodDeclList();
      jj_consume_token(RBRACE);
       {if (true) return new ClassDeclSimple(new Identifier(t1.image), vdlist1, mdlist1);}
    } else if (jj_2_2(3)) {
      jj_consume_token(CLASS);
      t2 = jj_consume_token(ID);
      jj_consume_token(EXTENDS);
      t3 = jj_consume_token(ID);
      jj_consume_token(LBRACE);
      vdlist2 = VarDeclList();
      mdlist2 = MethodDeclList();
      jj_consume_token(RBRACE);
       {if (true) return new ClassDeclExtends(new Identifier(t2.image), new Identifier(t3.image), vdlist2, mdlist2);}
    } else {
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public MethodDeclList MethodDeclList() throws ParseException {
  MethodDeclList mdlist;
  MethodDecl md;
    mdlist = new MethodDeclList();
    label_2:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PUBLIC:
        ;
        break;
      default:
        jj_la1[1] = jj_gen;
        break label_2;
      }
      md = MethodDecl();
                                                            mdlist.addElement(md);
    }
                                                                                          {if (true) return mdlist;}
    throw new Error("Missing return statement in function");
  }

  final public MethodDecl MethodDecl() throws ParseException {
  Type ty1, ty2;
  Token t1, t2, t3;
  Exp e1;
  FormalList frmlist;
  VarDeclList vdlist;
  StatementList stmtlist;
    jj_consume_token(PUBLIC);
    ty1 = Type();
    t1 = jj_consume_token(ID);
    jj_consume_token(LPAREN);
    frmlist = FrmlList();
    jj_consume_token(RPAREN);
    jj_consume_token(LBRACE);
    vdlist = VarDeclList();
    stmtlist = StmtList();
    jj_consume_token(RETURN);
    e1 = Exp();
    jj_consume_token(SEMI);
    jj_consume_token(RBRACE);
    {if (true) return new MethodDecl(ty1, new Identifier(t1.image), frmlist, vdlist, stmtlist, e1);}
    throw new Error("Missing return statement in function");
  }

  final public Formal Formal() throws ParseException {
  Type ty1;
  Token t1;
    ty1 = Type();
    t1 = jj_consume_token(ID);
                           {if (true) return new Formal(ty1, new Identifier(t1.image));}
    throw new Error("Missing return statement in function");
  }

  final public FormalList FrmlList() throws ParseException {
  FormalList frmllist;
  Formal frm1, frm2;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case INT:
    case BOOLEAN:
    case ID:
    frmllist = new FormalList();
      frm1 = Formal();
                                                     frmllist.addElement(frm1);
      label_3:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case COMMA:
          ;
          break;
        default:
          jj_la1[2] = jj_gen;
          break label_3;
        }
        jj_consume_token(COMMA);
        frm2 = Formal();
                                                                                                              frmllist.addElement(frm2);
      }
                                                                                                                                                {if (true) return frmllist;}
      break;
    default:
      jj_la1[3] = jj_gen;
    {if (true) return new FormalList();}
    }
    throw new Error("Missing return statement in function");
  }

  final public VarDeclList VarDeclList() throws ParseException {
  VarDeclList vdlist;
  VarDecl vd;
    vdlist = new VarDeclList();
    label_4:
    while (true) {
      if (jj_2_3(2)) {
        ;
      } else {
        break label_4;
      }
      vd = VarDecl();
                                                                  vdlist.addElement(vd);
    }
                                                                                                {if (true) return vdlist;}
    throw new Error("Missing return statement in function");
  }

  final public VarDecl VarDecl() throws ParseException {
  Type ty;
  Token t;
    ty = Type();
    t = jj_consume_token(ID);
    jj_consume_token(SEMI);
                                {if (true) return new VarDecl(ty, new Identifier(t.image));}
    throw new Error("Missing return statement in function");
  }

  final public Type Type() throws ParseException {
  Token t1;
    if (jj_2_4(2)) {
      jj_consume_token(INT);
      jj_consume_token(LBRACK);
      jj_consume_token(RBRACK);
                                         {if (true) return new IntArrayType();}
    } else {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case INT:
        jj_consume_token(INT);
          {if (true) return new IntegerType();}
        break;
      case BOOLEAN:
        jj_consume_token(BOOLEAN);
              {if (true) return new BooleanType();}
        break;
      case ID:
        t1 = jj_consume_token(ID);
              {if (true) return new IdentifierType(t1.image);}
        break;
      default:
        jj_la1[4] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
    throw new Error("Missing return statement in function");
  }

  final public Statement Statement() throws ParseException {
  StatementList list;
  Exp e1, e2, e3, e4, e5, e6;
  Statement s1, s2, s3;
  Token t1, t2;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LBRACE:
      jj_consume_token(LBRACE);
      list = StmtList();
      jj_consume_token(RBRACE);
                                        {if (true) return new Block(list);}
      break;
    case IF:
      jj_consume_token(IF);
      jj_consume_token(LPAREN);
      e1 = Exp();
      jj_consume_token(RPAREN);
      s1 = Statement();
      jj_consume_token(ELSE);
      s2 = Statement();
       {if (true) return new If(e1, s1, s2);}
      break;
    case WHILE:
      jj_consume_token(WHILE);
      jj_consume_token(LPAREN);
      e2 = Exp();
      jj_consume_token(RPAREN);
      s3 = Statement();
                                                          {if (true) return new While(e2, s3);}
      break;
    case SYSOPRNTL:
      jj_consume_token(SYSOPRNTL);
      jj_consume_token(LPAREN);
      e3 = Exp();
      jj_consume_token(RPAREN);
      jj_consume_token(SEMI);
                                                    {if (true) return new Print(e3);}
      break;
    default:
      jj_la1[5] = jj_gen;
      if (jj_2_5(2)) {
        t1 = jj_consume_token(ID);
        jj_consume_token(ASSIGN);
        e4 = Exp();
        jj_consume_token(SEMI);
       {if (true) return new Assign(new Identifier(t1.image), e4);}
      } else if (jj_2_6(2)) {
        t2 = jj_consume_token(ID);
        jj_consume_token(LBRACK);
        e5 = Exp();
        jj_consume_token(RBRACK);
        jj_consume_token(ASSIGN);
        e6 = Exp();
        jj_consume_token(SEMI);
       {if (true) return new ArrayAssign(new Identifier(t2.image), e5, e6);}
      } else {
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
    throw new Error("Missing return statement in function");
  }

  final public StatementList StmtList() throws ParseException {
  StatementList stmtlist;
  Statement s1;
    stmtlist = new StatementList();
    label_5:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LBRACE:
      case IF:
      case WHILE:
      case SYSOPRNTL:
      case ID:
        ;
        break;
      default:
        jj_la1[6] = jj_gen;
        break label_5;
      }
      s1 = Statement();
      stmtlist.addElement(s1);
    }
                                      {if (true) return stmtlist;}
    throw new Error("Missing return statement in function");
  }

  final public Exp Exp() throws ParseException {
  Exp e1, e2;
    e1 = LtLevel();
    e2 = Expp(e1);
                                 {if (true) return e2;}
    throw new Error("Missing return statement in function");
  }

  final public Exp Expp(Exp ae) throws ParseException {
  Exp e1, e2, e3;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case AND:
      jj_consume_token(AND);
      e1 = LtLevel();
                         e2 = new And(ae, e1);
      e3 = Expp(e2);
                                                                 {if (true) return e3;}
      break;
    default:
      jj_la1[7] = jj_gen;
    {if (true) return ae;}
    }
    throw new Error("Missing return statement in function");
  }

  final public Exp LtLevel() throws ParseException {
  Exp e1, e2;
    e1 = PlusLevel();
    e2 = LtLevelp(e1);
                                       {if (true) return e2;}
    throw new Error("Missing return statement in function");
  }

  final public Exp LtLevelp(Exp ae) throws ParseException {
  Exp e1;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LT:
      jj_consume_token(LT);
      e1 = PlusLevel();
                          {if (true) return new LessThan(ae, e1);}
      break;
    default:
      jj_la1[8] = jj_gen;
    {if (true) return ae;}
    }
    throw new Error("Missing return statement in function");
  }

  final public Exp PlusLevel() throws ParseException {
  Exp e1, e2;
    e1 = MultLevel();
    e2 = PlusLevelp(e1);
                                         {if (true) return e2;}
    throw new Error("Missing return statement in function");
  }

  final public Exp PlusLevelp(Exp ae) throws ParseException {
  Exp e1, e2, e3, e4, e5, e6;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case PLUS:
      jj_consume_token(PLUS);
      e1 = MultLevel();
                              e2 = new Plus(ae, e1);
      e3 = PlusLevelp(e2);
                                                                             {if (true) return e3;}
      break;
    case MINUS:
      jj_consume_token(MINUS);
      e4 = MultLevel();
                               e5 = new Minus(ae, e4);
      e6 = PlusLevelp(e5);
                                                                               {if (true) return e6;}
      break;
    default:
      jj_la1[9] = jj_gen;
      {if (true) return ae;}
    }
    throw new Error("Missing return statement in function");
  }

  final public Exp MultLevel() throws ParseException {
  Exp e1, e2;
    e1 = DotLevel();
    e2 = MultLevelp(e1);
                                        {if (true) return e2;}
    throw new Error("Missing return statement in function");
  }

  final public Exp MultLevelp(Exp ae) throws ParseException {
  Exp e1, e2, e3;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case TIMES:
      jj_consume_token(TIMES);
      e1 = DotLevel();
                            e2 = new Times(ae, e1);
      e3 = MultLevelp(e2);
                                                                            {if (true) return e3;}
      break;
    default:
      jj_la1[10] = jj_gen;
    {if (true) return ae;}
    }
    throw new Error("Missing return statement in function");
  }

  final public Exp DotLevel() throws ParseException {
 Exp e1, e2, e3, e4;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case EXCLAM:
      jj_consume_token(EXCLAM);
      e1 = LastLevelPrimiry();
      e2 = DotLevelp(e1);
                                                        {if (true) return new Not(e2);}
      break;
    case LPAREN:
    case NEW:
    case THIS:
    case TRUE:
    case FALSE:
    case ID:
    case NUM:
      e3 = LastLevelPrimiry();
      e4 = DotLevelp(e3);
                                               {if (true) return e4;}
      break;
    default:
      jj_la1[11] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public Exp DotLevelp(Exp ae) throws ParseException {
  Exp e1, e2;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LBRACK:
    case DOT:
      e1 = LastLevel(ae);
      e2 = DotLevelp(e1);
                                           {if (true) return e2;}
      break;
    default:
      jj_la1[12] = jj_gen;
                                                           {if (true) return ae;}
    }
    throw new Error("Missing return statement in function");
  }

  final public Exp LastLevel(Exp ae) throws ParseException {
  Exp e, i;
  ExpList el;
  Token t5;
    if (jj_2_7(2)) {
      jj_consume_token(DOT);
      jj_consume_token(LENGTH);
                                {if (true) return new ArrayLength(ae);}
    } else {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case DOT:
        jj_consume_token(DOT);
        t5 = jj_consume_token(ID);
        jj_consume_token(LPAREN);
        el = ExpList();
        jj_consume_token(RPAREN);
          {if (true) return new Call(ae, new Identifier(t5.image), el);}
        break;
      case LBRACK:
        jj_consume_token(LBRACK);
        i = Exp();
        jj_consume_token(RBRACK);
                                {if (true) return new ArrayLookup(ae, i);}
        break;
      default:
        jj_la1[13] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
    throw new Error("Missing return statement in function");
  }

  final public Exp LastLevelPrimiry() throws ParseException {
  Exp e, i;
  Token t1, t2, t3, t4, t6, t7;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case TRUE:
      jj_consume_token(TRUE);
           {if (true) return new True();}
      break;
    case FALSE:
      jj_consume_token(FALSE);
            {if (true) return new False();}
      break;
    case THIS:
      jj_consume_token(THIS);
           {if (true) return new This();}
      break;
    default:
      jj_la1[14] = jj_gen;
      if (jj_2_8(2)) {
        jj_consume_token(NEW);
        t4 = jj_consume_token(ID);
        jj_consume_token(LPAREN);
        jj_consume_token(RPAREN);
         {if (true) return new NewObject(new Identifier(t4.image));}
      } else {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case LPAREN:
          jj_consume_token(LPAREN);
          e = Exp();
          jj_consume_token(RPAREN);
                                {if (true) return e;}
          break;
        default:
          jj_la1[15] = jj_gen;
          if (jj_2_9(2)) {
            jj_consume_token(NEW);
            jj_consume_token(INT);
            jj_consume_token(LBRACK);
            i = Exp();
            jj_consume_token(RBRACK);
          {if (true) return new NewArray(i);}
          } else if (jj_2_10(2)) {
            t6 = jj_consume_token(ID);
                           {if (true) return new IdentifierExp(t6.image);}
          } else {
            switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
            case NUM:
              t7 = jj_consume_token(NUM);
               {if (true) return new IntegerLiteral(Integer.parseInt(t7.image));}
              break;
            default:
              jj_la1[16] = jj_gen;
              jj_consume_token(-1);
              throw new ParseException();
            }
          }
        }
      }
    }
    throw new Error("Missing return statement in function");
  }

  final public ExpList ExpList() throws ParseException {
  ExpList list;
  Exp e1, e2;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LPAREN:
    case EXCLAM:
    case NEW:
    case THIS:
    case TRUE:
    case FALSE:
    case ID:
    case NUM:
   list = new ExpList();
      e1 = Exp();
     list.addElement(e1);
      label_6:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case COMMA:
          ;
          break;
        default:
          jj_la1[17] = jj_gen;
          break label_6;
        }
        jj_consume_token(COMMA);
        e2 = Exp();
                          list.addElement(e2);
      }
      {if (true) return list;}
      break;
    default:
      jj_la1[18] = jj_gen;
      {if (true) return new ExpList();}
    }
    throw new Error("Missing return statement in function");
  }

  private boolean jj_2_1(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_1(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(0, xla); }
  }

  private boolean jj_2_2(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_2(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(1, xla); }
  }

  private boolean jj_2_3(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_3(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(2, xla); }
  }

  private boolean jj_2_4(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_4(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(3, xla); }
  }

  private boolean jj_2_5(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_5(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(4, xla); }
  }

  private boolean jj_2_6(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_6(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(5, xla); }
  }

  private boolean jj_2_7(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_7(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(6, xla); }
  }

  private boolean jj_2_8(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_8(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(7, xla); }
  }

  private boolean jj_2_9(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_9(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(8, xla); }
  }

  private boolean jj_2_10(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_10(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(9, xla); }
  }

  private boolean jj_3R_11() {
    if (jj_scan_token(ID)) return true;
    return false;
  }

  private boolean jj_3_10() {
    if (jj_scan_token(ID)) return true;
    return false;
  }

  private boolean jj_3_2() {
    if (jj_scan_token(CLASS)) return true;
    if (jj_scan_token(ID)) return true;
    if (jj_scan_token(EXTENDS)) return true;
    return false;
  }

  private boolean jj_3R_10() {
    if (jj_scan_token(BOOLEAN)) return true;
    return false;
  }

  private boolean jj_3_3() {
    if (jj_3R_7()) return true;
    return false;
  }

  private boolean jj_3R_9() {
    if (jj_scan_token(INT)) return true;
    return false;
  }

  private boolean jj_3_9() {
    if (jj_scan_token(NEW)) return true;
    if (jj_scan_token(INT)) return true;
    return false;
  }

  private boolean jj_3_1() {
    if (jj_scan_token(CLASS)) return true;
    if (jj_scan_token(ID)) return true;
    if (jj_scan_token(LBRACE)) return true;
    return false;
  }

  private boolean jj_3R_8() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_3_4()) {
    jj_scanpos = xsp;
    if (jj_3R_9()) {
    jj_scanpos = xsp;
    if (jj_3R_10()) {
    jj_scanpos = xsp;
    if (jj_3R_11()) return true;
    }
    }
    }
    return false;
  }

  private boolean jj_3_4() {
    if (jj_scan_token(INT)) return true;
    if (jj_scan_token(LBRACK)) return true;
    return false;
  }

  private boolean jj_3_8() {
    if (jj_scan_token(NEW)) return true;
    if (jj_scan_token(ID)) return true;
    return false;
  }

  private boolean jj_3R_7() {
    if (jj_3R_8()) return true;
    if (jj_scan_token(ID)) return true;
    return false;
  }

  private boolean jj_3_6() {
    if (jj_scan_token(ID)) return true;
    if (jj_scan_token(LBRACK)) return true;
    return false;
  }

  private boolean jj_3_5() {
    if (jj_scan_token(ID)) return true;
    if (jj_scan_token(ASSIGN)) return true;
    return false;
  }

  private boolean jj_3_7() {
    if (jj_scan_token(DOT)) return true;
    if (jj_scan_token(LENGTH)) return true;
    return false;
  }

  /** Generated Token Manager. */
  public NewParserTokenManager token_source;
  SimpleCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private Token jj_scanpos, jj_lastpos;
  private int jj_la;
  private int jj_gen;
  final private int[] jj_la1 = new int[19];
  static private int[] jj_la1_0;
  static private int[] jj_la1_1;
  static {
      jj_la1_init_0();
      jj_la1_init_1();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x2000000,0x20000000,0x80,0x40000,0x40000,0x8020002,0x8020002,0x1000,0x10000,0x6000,0x8000,0x4c80420,0x208,0x208,0x4c00000,0x20,0x0,0x80,0x4c80420,};
   }
   private static void jj_la1_init_1() {
      jj_la1_1 = new int[] {0x0,0x0,0x0,0x12,0x12,0x8,0x18,0x0,0x0,0x0,0x0,0x30,0x0,0x0,0x0,0x0,0x20,0x0,0x30,};
   }
  final private JJCalls[] jj_2_rtns = new JJCalls[10];
  private boolean jj_rescan = false;
  private int jj_gc = 0;

  /** Constructor with InputStream. */
  public NewParser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public NewParser(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new NewParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 19; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 19; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor. */
  public NewParser(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new NewParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 19; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 19; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor with generated Token Manager. */
  public NewParser(NewParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 19; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(NewParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 19; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      if (++jj_gc > 100) {
        jj_gc = 0;
        for (int i = 0; i < jj_2_rtns.length; i++) {
          JJCalls c = jj_2_rtns[i];
          while (c != null) {
            if (c.gen < jj_gen) c.first = null;
            c = c.next;
          }
        }
      }
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  static private final class LookaheadSuccess extends java.lang.Error { }
  final private LookaheadSuccess jj_ls = new LookaheadSuccess();
  private boolean jj_scan_token(int kind) {
    if (jj_scanpos == jj_lastpos) {
      jj_la--;
      if (jj_scanpos.next == null) {
        jj_lastpos = jj_scanpos = jj_scanpos.next = token_source.getNextToken();
      } else {
        jj_lastpos = jj_scanpos = jj_scanpos.next;
      }
    } else {
      jj_scanpos = jj_scanpos.next;
    }
    if (jj_rescan) {
      int i = 0; Token tok = token;
      while (tok != null && tok != jj_scanpos) { i++; tok = tok.next; }
      if (tok != null) jj_add_error_token(kind, i);
    }
    if (jj_scanpos.kind != kind) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) throw jj_ls;
    return false;
  }


/** Get the next Token. */
  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  private int[] jj_expentry;
  private int jj_kind = -1;
  private int[] jj_lasttokens = new int[100];
  private int jj_endpos;

  private void jj_add_error_token(int kind, int pos) {
    if (pos >= 100) return;
    if (pos == jj_endpos + 1) {
      jj_lasttokens[jj_endpos++] = kind;
    } else if (jj_endpos != 0) {
      jj_expentry = new int[jj_endpos];
      for (int i = 0; i < jj_endpos; i++) {
        jj_expentry[i] = jj_lasttokens[i];
      }
      boolean exists = false;
      for (java.util.Iterator<?> it = jj_expentries.iterator(); it.hasNext();) {
        exists = true;
        int[] oldentry = (int[])(it.next());
        if (oldentry.length == jj_expentry.length) {
          for (int i = 0; i < jj_expentry.length; i++) {
            if (oldentry[i] != jj_expentry[i]) {
              exists = false;
              break;
            }
          }
          if (exists) break;
        }
      }
      if (!exists) jj_expentries.add(jj_expentry);
      if (pos != 0) jj_lasttokens[(jj_endpos = pos) - 1] = kind;
    }
  }

  /** Generate ParseException. */
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[44];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 19; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 44; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    jj_endpos = 0;
    jj_rescan_token();
    jj_add_error_token(0, 0);
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  final public void enable_tracing() {
  }

  /** Disable tracing. */
  final public void disable_tracing() {
  }

  private void jj_rescan_token() {
    jj_rescan = true;
    for (int i = 0; i < 10; i++) {
    try {
      JJCalls p = jj_2_rtns[i];
      do {
        if (p.gen > jj_gen) {
          jj_la = p.arg; jj_lastpos = jj_scanpos = p.first;
          switch (i) {
            case 0: jj_3_1(); break;
            case 1: jj_3_2(); break;
            case 2: jj_3_3(); break;
            case 3: jj_3_4(); break;
            case 4: jj_3_5(); break;
            case 5: jj_3_6(); break;
            case 6: jj_3_7(); break;
            case 7: jj_3_8(); break;
            case 8: jj_3_9(); break;
            case 9: jj_3_10(); break;
          }
        }
        p = p.next;
      } while (p != null);
      } catch(LookaheadSuccess ls) { }
    }
    jj_rescan = false;
  }

  private void jj_save(int index, int xla) {
    JJCalls p = jj_2_rtns[index];
    while (p.gen > jj_gen) {
      if (p.next == null) { p = p.next = new JJCalls(); break; }
      p = p.next;
    }
    p.gen = jj_gen + xla - jj_la; p.first = token; p.arg = xla;
  }

  static final class JJCalls {
    int gen;
    Token first;
    int arg;
    JJCalls next;
  }

}