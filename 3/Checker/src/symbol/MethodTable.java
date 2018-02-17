package symbol;

import syntaxtree.*;
import java.util.*;

public class MethodTable {

	private String name;
	private Type type;
	private Dictionary<String, Type> locals; //includes params and local vars
	
	public MethodTable(String methodName, Type methodType) {
		this.name = methodName;
		this.type = methodType;
		this.locals = new Hashtable<String, Type>();
	}

	public String getName() {
		return this.name;
	}
	
	public boolean addLocal(String localName, Type localType) {
		if(this.locals.get(localName) == null) {
			this.locals.put(localName, localType);
			return true;
		}
		else
			return false;
	}
	
	public Type getLocal(String localName) {
		return this.locals.get(localName);
	}

}
