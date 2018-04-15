class MainClass {
    public static void main(String[] a){
	System.out.println(new Compute().init(true, 4, true, 16, true, 32));
    }
}

class Compute {

    public int init(boolean b1, int i1, boolean b2, int a1, 
			 boolean b3, int i3)
    {  
	if(b1)
	    System.out.println(i1);
	else
	    System.out.println(0);
	
	if(b2)
	    System.out.println(a1);
	else
	    System.out.println(0);
	
	if(b3)
	    System.out.println(i3);
	else
	    System.out.println(0);

	return 888;
    }
}
