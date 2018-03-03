class MyClass{
	public static void main(String[] a){
		System.out.println(new C().m(9, 12));
	}
}

class C{
    public int m(int x, int y) {
	if(x<y) {
	    System.out.println(x);
	    x = x + 1;
	    if(10<x)
		x = x + 1;
	    else
		x = x - 1;
	    System.out.println(x);
	}
	else
	    x = 0;

	return x;
    }
}
