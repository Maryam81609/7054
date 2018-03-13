package visitor;

import syntaxtree.*;
import symbol.*;
import error.ErrorMsg;

public class TypeCheckVisitor implements TypeVisitor {
	
	private SymbolTable table;
	private ClassTable currClass;
	private MethodTable currMethod;
	
	public TypeCheckVisitor(SymbolTable _table) {
		table = _table;
		currClass = null;
		currMethod = null;
	}

	@Override
	public Type visit(Program n) {
		n.m.accept(this);
		for(int i = 0; i < n.cl.size(); i++)
			n.cl.elementAt(i).accept(this);
		return null;
	}

	@Override
	public Type visit(MainClass n) {
		n.s.accept(this);
		return null;
	}

	@Override
	public Type visit(ClassDeclSimple n) {
		currClass = table.getClass(n.i.toString());
		for(int i = 0; i < n.ml.size(); i++)
			n.ml.elementAt(i).accept(this);
		currClass = null;
		return null;
	}

	@Override
	public Type visit(ClassDeclExtends n) {
		if(table.getClass(n.j.toString()) == null){
			ErrorMsg.unknownExtendedClass(n.i.toString(), n.j.toString());
		}
		currClass = table.getClass(n.i.toString());
		for(int i = 0; i < n.ml.size(); i++)
			n.ml.elementAt(i).accept(this);
		currClass = null;
		return null;
	}

	@Override
	public Type visit(VarDecl n) {
		// TODO Auto-generated method stub
		return null;   // DO NOTHING
	}

	@Override
	public Type visit(MethodDecl n) {
		currMethod = currClass.getMethod(n.i.toString());
		if(!(n.t.getClass().isAssignableFrom(n.e.accept(this).getClass())))
			ErrorMsg.typeMismatchReturn(n.i.toString(), currClass.getName(), n.t);
		for(int i=0; i<n.sl.size(); i++) {
			n.sl.elementAt(i).accept(this);
		}
		n.i = new Identifier(currClass.getName() + "_" + n.i.s);
		currMethod = null;
		return null;
	}

	@Override
	public Type visit(Formal n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(IntArrayType n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(BooleanType n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(IntegerType n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(IdentifierType n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(Block n) {
		for(int i = 0; i < n.sl.size(); i++)
			n.sl.elementAt(i).accept(this);
		return null;
	}

	@Override
	public Type visit(If n) {
		if(!(n.e.accept(this) instanceof BooleanType))
				ErrorMsg.typeMismatchIf();
		n.s1.accept(this);
		n.s2.accept(this);
		return null;
	}

	@Override
	public Type visit(While n) {
		if(!(n.e.accept(this) instanceof BooleanType))
			ErrorMsg.typeMismatchWhile();
		n.s.accept(this);
		return null;
	}

	@Override
	public Type visit(Print n) {
		if(!(n.e.accept(this) instanceof IntegerType))
			ErrorMsg.typeMismatchPrint();
		return null;
	}

	@Override
	public Type visit(Assign n) {
		String varName = n.i.toString();
		Type varType = null;
		if((varType = currMethod.getVar(varName)) == null) {
			if((varType = currMethod.getParam(varName)) == null) {
				if((varType = currClass.getVar(varName)) == null) {
					ErrorMsg.unknownVar(varName, currClass.getName(), currMethod.getName());
				}
			}
		}
			
		Type expType = n.e.accept(this);

		if(!(varType.getClass().getName().equals(expType.getClass().getName()))) {
			if(!(varType.getClass().isAssignableFrom(expType.getClass()))) {
				ErrorMsg.typeMismatchAssign(varName, varType);
			}
		}
		return null;
	}

	@Override
	public Type visit(ArrayAssign n) {
		String varName = n.i.toString();
		Type varType = null;
		if((varType = currMethod.getVar(varName)) == null) {
			if((varType = currMethod.getParam(varName)) == null) {
				if((varType = currClass.getVar(varName)) == null) {
					ErrorMsg.unknownVar(varName, currClass.getName(), currMethod.getName());
				}
			}
		}
		
		if(!(varType instanceof IntArrayType))
			ErrorMsg.typeMismatchArray(varName);
		
		if(!(n.e1.accept(this) instanceof IntegerType))
			ErrorMsg.typeMismatchArrayIndex();
		
		if(!(n.e2.accept(this) instanceof IntegerType))
			ErrorMsg.typeMismatchArrayAssign(varName);		
		return null;
	}

	@Override
	public Type visit(And n) {
		if(!(n.e1.accept(this) instanceof BooleanType))
			ErrorMsg.typeMismatchAndL();
		if(!(n.e2.accept(this) instanceof BooleanType))
			ErrorMsg.typeMismatchAndR();	
		return new BooleanType();
	}

	@Override
	public Type visit(LessThan n) {
		if(!(n.e1.accept(this) instanceof IntegerType))
			ErrorMsg.typeMismatchLessThanL();
		if(!(n.e2.accept(this) instanceof IntegerType))
			ErrorMsg.typeMismatchLessThanR();
		return new BooleanType();
	}

	@Override
	public Type visit(Plus n) {
		if(!(n.e1.accept(this) instanceof IntegerType))
			ErrorMsg.typeMismatchPlusL();
		if(!(n.e2.accept(this) instanceof IntegerType))
			ErrorMsg.typeMismatchPlusR();
		return new IntegerType();
	}

	@Override
	public Type visit(Minus n) {
		if(!(n.e1.accept(this) instanceof IntegerType))
			ErrorMsg.typeMismatchMinusL();
		if(!(n.e2.accept(this) instanceof IntegerType))
			ErrorMsg.typeMismatchMinusR();
		return new IntegerType();
	}

	@Override
	public Type visit(Times n) {
		if(!(n.e1.accept(this) instanceof IntegerType))
			ErrorMsg.typeMismatchTimesL();
		if(!(n.e2.accept(this) instanceof IntegerType))
			ErrorMsg.typeMismatchTimesR();
		return new IntegerType();
	}

	@Override
	public Type visit(ArrayLookup n) {
		if(!(n.e1.accept(this) instanceof IntArrayType))
			ErrorMsg.typeMismatchArray();
		if(!(n.e2.accept(this) instanceof IntegerType))
			ErrorMsg.typeMismatchArrayIndex();
		return new IntegerType();
	}

	@Override
	public Type visit(ArrayLength n) {
		if(!(n.e.accept(this) instanceof IntArrayType))
			ErrorMsg.typeMismatchLength();
		return new IntegerType();
	}

	@Override
	public Type visit(Call n) {
		Type expClassType = n.e.accept(this);
		if(!(expClassType instanceof IdentifierType)) {
			ErrorMsg.typeMismatchObject(n.i.toString());
		}
		String expClassName = ((IdentifierType)expClassType).s;
		ClassTable expClassTbl = table.getClass(expClassName);
		if(expClassTbl == null) {
			ErrorMsg.unknownClass(expClassName);
		}
		String methodName = n.i.toString();
		MethodTable methodTbl = expClassTbl.getMethod(methodName);
		if(methodTbl == null) {
			ErrorMsg.unknownMethod(methodName, expClassName);
		}
		
		ExpList args = n.el;
		int paramsCount = methodTbl.paramsCount();
		if(args.size() != paramsCount) {
			ErrorMsg.typeMismatchArgNum(methodName, expClassName, paramsCount);
		}
				
		for(int i = 0; i < args.size(); i++) {
			Type argType = args.elementAt(i).accept(this);
			Type paramType = methodTbl.getParam(i);
			if(!paramType.getClass().isAssignableFrom(argType.getClass())) {
					ErrorMsg.typeMismatchCall(methodTbl.getName(), expClassTbl.getName(), i+1, paramType);
			}
		}
		n.i = new Identifier(expClassName + "_" + n.i.s);
		return methodTbl.getType();
	}

	@Override
	public Type visit(IntegerLiteral n) {
		return new IntegerType();
	}

	@Override
	public Type visit(True n) {
		return new BooleanType();
	}

	@Override
	public Type visit(False n) {
		return new BooleanType();
	}

	@Override
	public Type visit(IdentifierExp n) {
		String name = n.s;
		Type ret = null;
		
		if(currMethod == null) {
			if(currClass.getVar(name) == null) {
				ErrorMsg.unknownVar(name, currClass.getName(), null);
			}
			ret = currClass.getVar(name);
		}
		else {
			if(currMethod.getVar(name) == null) {
				if(currMethod.getParam(name) == null) {
					if(currClass.getVar(name) == null) {
						ErrorMsg.unknownVar(name, currClass.getName(), currMethod.getName());
					}
					ret = currClass.getVar(name);
				}
				else {
					ret = currMethod.getParam(name);
				}
			}
			else {
				ret = currMethod.getVar(name);
			}
		}
		return ret;
	}

	@Override
	public Type visit(This n) {
		// TODO Auto-generated method stub 
		return new IdentifierType(currClass.getName());
	}

	@Override
	public Type visit(NewArray n) {
		Type nType = n.e.accept(this);
		if(!(nType instanceof IntegerType))
			ErrorMsg.typeMismatchNewArray();
		return new IntArrayType();
	}

	@Override
	public Type visit(NewObject n) {
		String className = n.i.toString();
		if(table.getClass(className) == null)
			ErrorMsg.unknownClass(className);
		return new IdentifierType(className);
	}

	@Override
	public Type visit(Not n) {
		if(!(n.e.accept(this) instanceof BooleanType))
			ErrorMsg.typeMismatchNot();
		return new BooleanType();
	}

	@Override
	public Type visit(Identifier n) {
		return null;
	}

}