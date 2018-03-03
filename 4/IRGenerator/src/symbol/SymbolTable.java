package symbol;

import java.util.*;

public class SymbolTable {

	private Dictionary<String, ClassTable> table; 
	
	public SymbolTable() {
		this.table = new Hashtable<String, ClassTable>();
	}
    
    public boolean addClass(String className, ClassTable classTbl) {
    	if(this.table.get(className) == null) {
    		table.put(className, classTbl);
    		return true;
    	}
    	return false;
    }
    
    public ClassTable getClass(String className) {
    	return this.table.get(className);
    }
    
    public Enumeration<String> classNames(){
    	return this.table.keys();
    }
}
