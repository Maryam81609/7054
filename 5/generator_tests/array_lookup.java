class MainClass{
	public static void main(String[] a){
		System.out.println(new A().m());
	}
}

class A{
    public int m() {
	int[] a;
	a = new int[1];
	System.out.println(a[0]);
	return 1;
    }
}
