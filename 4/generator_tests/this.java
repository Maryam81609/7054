class MainClass{
	public static void main(String[] a){
		System.out.println(new MyClass().printInt());
	}
}

class MyClass{
    int a;

    public boolean init() {
	a = 9;
	return true;
    }

    public int printInt() {
	boolean b;
	b = this.init();
	System.out.println(a);
	return 0;
    }
}
