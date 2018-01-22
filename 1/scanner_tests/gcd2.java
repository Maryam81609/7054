class MainClass {
    public static void main(String[] a) {
	System.out.println(new GCD().getGCD(35,28));
    }
}

class GCD {
    public int getGCD(int x,int y) {
	int ret;

	if(!(!(x-y<0) || !(0<x-y)))
	    ret=x;
	else if(x<y)
	    ret=this.getGCD(x,y-x);
	else ret=this.getGCD(y,x-y);   return ret;
    }
}
	    
