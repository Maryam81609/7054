class MyClass{
	public static void main(String[] a){
		System.out.println(new C().m());
	}
}

class C{
    public int m() {
	int x;
	x = 8;
	return x;
    }
}
