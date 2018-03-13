class MainClass {
    public static void main(String[] a) {
	//Test newobject
	System.out.println(new Double().getDouble(3));

	//Test new array lookup
	//System.out.println(new int[4][3]);

	//Test new array and length
	//System.out.println(new int[4].length);

	// Test while
	//while(false)
	//{
	//	System.out.println(222);
	//}

	// Test if and true and false
	//if(true)

	// Test if and <
	//if(4 < 5)
	//{
	//	System.out.println(11);
	//	System.out.println(12);
	//}	
	//else
	//{
	//	System.out.println(9);
	//	System.out.println(10);
	//}


	// Test if and &&
	//if(4 < 6 && 9 < 2 * 4)

	// Test if and !
	//if(!(4<3 && 6<7))
	//	System.out.println(0);
	//else
	//	System.out.println(1);
		
	// Test block
	//{
	//	System.out.println(70-2*10);
	//	System.out.println(70-2-38);
	//}

	// Test PLUS
	//System.out.println(7+2);

	// Test MINUS
	//System.out.println(7-2);

	// Test integer literal
	//System.out.println(7);
    }
}

class Double {
    int x;
    int y;
    public int getDouble(int x) { return x*2; }
}
	    
