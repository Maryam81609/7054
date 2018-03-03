class MainClass {
    public static void main(String[] a){
	System.out.println(new Compute().init());
    }
}

class Compute {
    int a; int b; int c; int d; int sum;

    public int init()
    {
	int limit;  limit=100;
	a=0; {b=0; {c=0; {d=0; {sum=0;}}}}

	while(a<limit) {
	    while(b<a) {
		while(c<b) {
		    while(d<c) {
			sum = sum + d; d = d+1;}
		c = c+1; }
	    b = b+1; }
	a = a+1; }
	return sum;
    }
}

