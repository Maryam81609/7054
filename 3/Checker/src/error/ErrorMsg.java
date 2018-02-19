package error;

import syntaxtree.*;

public class ErrorMsg {

    /** Use if a class named id has already been declared.  */
    public static void duplicateClass(String id) {
      System.err.println("SEMANTIC ERROR: Class " + id + " is already declared.");
      System.exit(0);    
    }

    /** Use if a method named id has already been declared in class cid.  */
    public static void duplicateMethod(String id, String cid) {
      System.err.println("SEMANTIC ERROR: Method " + id + " is already declared in class " + cid + ".");
      System.exit(0);
    }

    /** Use if a field variable named id has already been declared in class cid.  */
    public static void duplicateFieldVar(String id, String cid) {
      System.err.println("SEMANTIC ERROR: Variable " + id + " is already declared in class " + cid + ".");
      System.exit(0);
    }

    /** Use if a variable (local or formal parameter) named id has already been declared in method mid of class cid. */
    public static void duplicateVar(String id, String cid, String mid) {
      System.err.println("SEMANTIC ERROR: Variable " + id + " is already declared in method " + cid + "." + mid + ".");
      System.exit(0);    
    }

    /** Use if a class named id is extending another class named exid which is not declared. */
    public static void unknownExtendedClass(String id, String exid) {
    	System.err.println("SEMANTIC ERROR: Class " + id + " is extending class " + exid + " which is not declared.");
        System.exit(0);
    }
    
    /** Use if a class named id is used but not declared. */
    public static void unknownClass(String id) {
      System.err.println("SEMANTIC ERROR: Class " + id + " is not declared.");
      System.exit(0);
    }

    /** Use if a variable is used but not declared. */
    public static void unknownVar(String id, String cid, String mid) {
      System.err.println("SEMANTIC ERROR: Variable " + id + " is not declared in class " + cid + " or in method " + cid + "." + mid + ".");
      System.exit(0);
    }

    /** Use if a method named id is used but not declared. */
    public static void unknownMethod(String id, String cid) {
      System.err.println("SEMANTIC ERROR: Method " + id + " is not defined in class " + cid + ".");
      System.exit(0);
    }

    /** Use if the expression returned by a method named id in class cid does not match its declared return type. */
    public static void typeMismatchReturn(String id, String cid, Type t) {
      System.err.println("SEMANTIC ERROR: For method " + cid + "." + id + ", " + "the expression returned must be of type " + getString(t) + ".");
      System.exit(0);
    }

    /** Use if the test expression of an if-statement is not a boolean type. */
    public static void typeMismatchIf() {
      System.err.println("SEMANTIC ERROR: The test expression of an if-statement must be of type boolean.");
      System.exit(0);
    }

    /** Use if the test expression of a while-statement is not a boolean type. */
    public static void typeMismatchWhile() {
      System.err.println("SEMANTIC ERROR: The test expression of a while-statement must be of type boolean.");
      System.exit(0);
    }

    /** Use if the expression of a print-statement is not an int type. */
    public static void typeMismatchPrint() {
      System.err.println("SEMANTIC ERROR: The expression of a print-statement must be of type int.");
      System.exit(0);
    }

    /** Use if the expression assigned to variable id does not match its declared type. */
    public static void typeMismatchAssign(String id, Type t) {
      System.err.println("SEMANTIC ERROR: The expression assigned to " + "variable " + id + " must be of type " + getString(t) + ".");
      System.exit(0);
    }

    /** Use if the expression assigned to an element of array variable id is not an int type. */
    public static void typeMismatchArrayAssign(String id) {
      System.err.println("SEMANTIC ERROR: The expression assigned to " + id + "[...] must be of type int.");
      System.exit(0);
    }

    /** Use if the expression used as an array index is not an int type. */
    public static void typeMismatchArrayIndex() {
      System.err.println("SEMANTIC ERROR: The expression used to index an array must be of type int.");
      System.exit(0);
    }

    /** Use if variable id is not an int[] type. */
    public static void typeMismatchArray(String id) {
      System.err.println("SEMANTIC ERROR: Variable " + id + " must be of type int[].");
      System.exit(0);
    }

    /** Use if the left side of an AND expression is not a boolean type. */
    public static void typeMismatchAndL() {
      System.err.println("SEMANTIC ERROR: The left side of an AND expression must be of type boolean.");
      System.exit(0);
    }

    /** Use if the right side of an AND expression is not a boolean type. */
    public static void typeMismatchAndR() {
      System.err.println("SEMANTIC ERROR: The right side of an AND expression must be of type boolean.");
      System.exit(0);
    }

    /** Use if the left side of a LESSTHAN expression is not an int type. */
    public static void typeMismatchLessThanL() {
      System.err.println("SEMANTIC ERROR: The left side of a LESSTHAN expression must be of type int.");
      System.exit(0);
    }

    /** Use if the right side of a LESSTHAN expression is not an int type. */
    public static void typeMismatchLessThanR() {
      System.err.println("SEMANTIC ERROR: The right side of a LESSTHAN expression must be of type int.");
      System.exit(0);
    }

    /** Use if the left side of a PLUS expression is not an int type. */
    public static void typeMismatchPlusL() {
      System.err.println("SEMANTIC ERROR: The left side of a PLUS expression must be of type int.");
      System.exit(0);
    }

    /** Use if the right side of a PLUS expression is not an int type. */
    public static void typeMismatchPlusR() {
      System.err.println("SEMANTIC ERROR: The right side of a PLUS expression must be of type int.");
      System.exit(0);
    }

    /** Use if the left side of a MINUS expression is not an int type. */
    public static void typeMismatchMinusL() {
      System.err.println("SEMANTIC ERROR: The left side of a MINUS expression must be of type int.");
      System.exit(0);
    }

    /** Use if the right side of a MINUS expression is not an int type. */
    public static void typeMismatchMinusR() {
      System.err.println("SEMANTIC ERROR: The right side of a MINUS expression must be of type int.");
      System.exit(0);
    }

    /** Use if the left side of a TIMES expression is not an int type. */
    public static void typeMismatchTimesL() {
      System.err.println("SEMANTIC ERROR: The left side of a TIMES expression must be of type int.");
      System.exit(0);
    }

    /** Use if the right side of a TIMES expression is not an int type. */
    public static void typeMismatchTimesR() {
      System.err.println("SEMANTIC ERROR: The right side of a TIMES expression must be of type int.");
      System.exit(0);
    }

    /** Use if the expression indexed as an array is not an int[] type. */
    public static void typeMismatchArray() {
      System.err.println("SEMANTIC ERROR: The expression indexed as an array must be of type int[].");
      System.exit(0);
    }

    /** Use if "length" is applied to an expression that is not an int[] type. */
    public static void typeMismatchLength() {
      System.err.println("SEMANTIC ERROR: The expression 'length' is applied to must be of type int[].");
      System.exit(0);
    }

    /** Use if the expression on which a method is called is not a class type. */
    public static void typeMismatchObject(String id) {
      System.err.println("SEMANTIC ERROR: Attempt to call method " + id + " on an expression that is not a class instance.");
      System.exit(0);
    }

    /** Use if the ith argument passed to a method named id in class cid does not match its declared type. */
    public static void typeMismatchCall(String id, String cid, int i, Type t) {
      System.err.println("SEMANTIC ERROR: For method " + cid + "." + id + ", argument " + i + " must be of type " + getString(t) + ".");
      System.exit(0);
    }
    
    /** Use if the number of arguments passed to a method named id in class cid does not match its declaration. */
    public static void typeMismatchArgNum(String id, String cid, int n) {
      System.err.println("SEMANTIC ERROR: For method " + cid + "." + id + ", " + n + " arguments are expected.");
      System.exit(0);
    }

    /** Use if the size expression for a new array is not an int type. */
    public static void typeMismatchNewArray() {
      System.err.println("SEMANTIC ERROR: The size expression for a new array must be of type int.");
      System.exit(0);
    }

    /** Use if NOT is applied to an expression that is not a boolean type. */
    public static void typeMismatchNot() {
      System.err.println("SEMANTIC ERROR: The expression NOT is applied to must be of type boolean.");
      System.exit(0);
    }    

    
    public static String getString(Type t) {
      if(t instanceof IntegerType)
        return "int";
      if(t instanceof BooleanType)
        return "boolean";
      if(t instanceof IntArrayType)
        return "int[]"; 
      return ((IdentifierType)t).s;
    }
}
