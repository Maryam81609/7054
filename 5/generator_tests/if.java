class MyClass{
	public static void main(String[] a){
		System.out.println(new C().m(78));
	}
}

class C{
    public int m(int x) {
	int a;
	if(100<x)
	  a = x;
	else
	  a = 1;
	return a;
    }
}
