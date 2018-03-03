class MyClass{
	public static void main(String[] a){
		System.out.println(new C().m(9, 12));
	}
}

class C{
    public int m(int x, int y) {
	while(x<y) {
	    System.out.println(x);
	    x = x + 1;
	}

	return x;
    }
}
