class MainClass{
    public static void main(String[] a){
	System.out.println( new C().f() );
    }
}

class C {
    int c;
    public boolean print(boolean b1, int i1, boolean b2, int[] a1, 
			 boolean b3, B c1){
	if(b1)
	    System.out.println(i1);
	else
	    System.out.println(0);
	
	if(b2)
	    System.out.println(a1[0]);
	else
	    System.out.println(0);
	
	if(b3)
	    b1 = c1.print(a1[0]*a1[0]);
	else
	    System.out.println(0);

	return true;
    }
    public int f() {
	int i;
	boolean b;
	B b_;
	int[] a;
	b_ = new B();
	i = 2*2; 
	a = new int[1];
	a[0] = i*i;
	b = this.print(true, i, a, a, true, b_); 
	return 1;
    }
}

class B{
    public boolean print(int i) { System.out.println(i); return true; }
}
