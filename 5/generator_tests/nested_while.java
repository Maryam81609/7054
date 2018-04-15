class MainClass {
    public static void main(String[] a){
	System.out.println(new Compute().init());
    }
}

class Compute {
     
    int sum;

    public int init()
    {  
	int a;
	a=0;
	sum=0;

	while(a<100) {
		sum = sum + 1;
		a = a+1; 
	}
	return sum;
    }
}
