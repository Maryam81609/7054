package symbol;

import syntaxtree.*;
import java.util.*;

public class MethodTable {

	private String name;
	private Type type;
	private Dictionary<String, Type> locals; 
	private LinkedHashMap<String, Type> params;
	
	public MethodTable(String methodName, Type methodType) {
		this.name = methodName;
		this.type = methodType;
		this.locals = new Hashtable<String, Type>();
		this.params = new LinkedHashMap<String, Type>();
	}

	public String getName() {
		return this.name;
	}
	
	public Type getType() {
		return this.type;
	}
	
	public boolean addParam(String paramName, Type paramType) {
		if(this.params.get(paramName) == null) {
			this.params.put(paramName, paramType);
			return true;
		}
		return false;	
	}
	
	public Type getParam(int idx) {
		Type paramType = new ArrayList<Type>(this.params.values()).get(idx);
		return paramType;
	}
	
	public Type getParam(String paramName) {
		return this.params.get(paramName);
	}
	
	public int paramsCount(){
		return this.params.size();
	}
	
	public boolean addLocal(String localName, Type localType) {
		if(this.params.get(localName) == null)
			if(this.locals.get(localName) == null) {
				this.locals.put(localName, localType);
				return true;
			}
		return false;
	}
	
	public Type getVar(String localName) {
		return this.locals.get(localName);
	}

}
