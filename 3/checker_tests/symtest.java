class MainClass {
    public static void main(String[] a) {
	System.out.println(new Array().createArray(10)[3] + new Array().createArray(100).length);
	//System.out.println(new Array().createArray(10));
    }
}

class Array {
    int[] array;
    //int array; //multiple vars in a class

    public int[] createArray(int n){//, int[] n) { //multiple param
	int i;
	//boolean i; //multiple loca vars
	i = 0;
	array = new int[n]; 
	while (i<array.length) {
	    array[i] = i;
	    i = i+1;
	}
	return array;
    }

    public int size() { return array.length; }

    //public int size() {return 1;}
    
    public int[] copyArray() {
	int n;
	int[] array;
	int[] newarray;
	//int c;
	//foo f;
	//f = new foo();
	//c = f.color(array);
	//n = array.color();
	array = this.createArray(14);
	newarray = new int[this.size()];
	while (n<this.size()) {
	 //   n[4] = array[n];
	    newarray[n] = array[n];
	    n=n+1;
	}
	//if(true){
	//	n=n-3;
	//}
	//else{
	//	n=n+1;
	//}
	return newarray;
    }
}

//class Array{ } //multiple class

class foo extends Arrayy {

	public int color(int n){
		return n;
	}
}    
