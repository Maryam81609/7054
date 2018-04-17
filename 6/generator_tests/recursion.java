class t06{
    public static void main(String[] a){
                {
                        System.out.println( new A6().fun1(5) );
                        System.out.println( 14 );       // expected outcome
                }
    }
}

class A6 {
        int myInt;
        public int fun1(int x){
                int retVal;

                if (x < 1){
                        retVal = 1;
                } else {
                        retVal = 2 * this.fun2(x - 1);
                }

                return retVal ;
        }

        public int fun2(int x){
                int retVal;

                if (x < 1){
                        retVal = 1;
                } else {
                        retVal = 1 + this.fun1(x - 1);
                }

                return retVal;
        }
}
