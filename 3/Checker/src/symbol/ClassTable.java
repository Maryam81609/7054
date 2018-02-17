package symbol;

import syntaxtree.*;
import java.util.*;

public class ClassTable {
	
	private String name;
	private Dictionary<String, Type> vars;
	private Dictionary<String, MethodTable> methods;
	
	public ClassTable(String className) {
		this.name = className;
		this.vars = new Hashtable<String, Type>();
		this.methods = new Hashtable<String, MethodTable>();
	}
	
	public String getName() {
		return this.name;
	}
	
	public boolean addVar(String varName, Type varType) {
		if(this.vars.get(varName) == null) {
			this.vars.put(varName, varType);
			return true;
		}
		return false;
	}
	
	public boolean addMethod(String methodName, MethodTable methodTbl) {
		if(this.methods.get(methodName) == null) {
			this.methods.put(methodName, methodTbl);
			return true;
		}
		return false;
	}
	
	public Type getVar(String varName) {
		return this.vars.get(varName);
	}
	
	public MethodTable getMethod(String methodName) {
		return this.methods.get(methodName);
	}

	public Enumeration<String> varNames(){
		return this.vars.keys();
	}
	
	public Enumeration<String> methodNames(){
		return this.methods.keys();
	}
}
