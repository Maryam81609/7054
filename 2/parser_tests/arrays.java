class MainClass {
    public static void main(String[] a) {
	System.out.println(new Array().createArray(10)[3] + new Array().createArray(100).length);
    }
}

class Array {
    int[] array;

    public int[] createArray(int n) { 
	int i;
	i = 0;
	array = new int[n]; 
	while (i<array.length) {
	    array[i] = i;
	    i = i+1;
	}
	return array;
    }
}

    
