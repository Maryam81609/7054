class MainClass {
    public static void main(String[] a) {
	System.out.println(new Array().createArray(10)[3] + new Array().createArray(100).length);
    }
}

class Array {
    int[] array;
    //int array;

    public int[] createArray(int n) { 
	int i;
	//boolean n;
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
	array = this.createArray(14);
	newarray = new int[this.size()];
	while (n<this.size()) {
	    newarray[n] = array[n];
	    n=n+1;
	}
	return newarray;
    }
}

//class Array{ }

    
