package symbol;

import java.util.Dictionary;

public class Symbol {

    private String name;
    private Symbol(String n) {name = n;}
    private static Dictionary dict = new java.util.Hashtable();

    public String toString() {return name;}

    public static Symbol symbol(String n) {
	String u = n.intern();
 	Symbol s = (Symbol)dict.get(u);
	if(s == null) {
	    s = new Symbol(u);
	    dict.put(u, s);
	}
	return s;
    }
}
