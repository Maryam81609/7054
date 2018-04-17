class t08{
    public static void main(String[] a){
                {
                        System.out.println( new A8().fun1(11) );
                        System.out.println( 763610 );   // expected outcome
                }
    }
}

class A8 {
        int myInt;
        public int fun1(int x){
                int retVal;
                int a;
                int b;
                int c;
                int d;
                int e;
                int f;
                int g;

                // with constants
                a = x - 1;   
                b = x * 3;
                c = x + 4;
                // with variables
                a = b - a;
                b = a + c;
                d = b * a;
                // special mathematical identities
                e = c + 0;
                f = d - 0;
                g = f * 1;

                retVal = a + b * c - d + e + f * g;

                return retVal ;
        }

}
