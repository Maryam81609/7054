class MainClass {
    public static void main(String[] a){
	System.out.println(new OrderOfOps().init(1300));
    }
}

class OrderOfOps {
    public int init(int num_a)
    {
	int num_x; int num_y; int num_z; int ret;

	num_x=2; num_y=18; num_z=2005;

	if(!(num_a*6<num_z-40*num_y)&&(num_x*14)-num_a*8<0&&439078-num_a<num_z*num_a)
	   ret = 1;
	else
	   ret = 0;

	return ret;
    }
}

