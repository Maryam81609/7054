class MyClass{
	public static void main(String[] a){
		System.out.println(new C().m(true, 78));
	}
}

class C{
    public int m(boolean i, int x) {
	int a;
	if(i && 100<x)
	  a = 14;
	else
	  a = 56;
	return a;
    }
}
