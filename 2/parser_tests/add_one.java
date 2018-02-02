class MainClass {
    public static void main(String[] a) {
	System.out.println(new Add1().getAdd1(3));
    }
}

class Add1 {
    public int getAdd1(int x) { return x++; }
}
	    
