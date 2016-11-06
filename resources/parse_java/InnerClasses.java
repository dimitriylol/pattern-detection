package com.patterns;

public class InnerClasses {
    private int innerVariable = 20;


    public static class C1 {
        private final int innerInnerVariable;

        //contains explicit dependency to its parent class
        public C1(InnerClasses c) {
            this.innerInnerVariable = c.innerVariable;
        }

        public int returnInnerVariable() {
            //this does not work :(
            //return innerVariable;
            return this.innerInnerVariable;
        }
    }


    public class C2 {
        public int returnInnerVariable() {
            //C2 contains private link to outer class, so it is implicitly dependent on its parent
            //unlike c1
            return innerVariable;
        }
    }

    //this class does not depend on its parent
    public static class C3 {
        private final int innerInnerVariable;

        public C3(int c) {
            this.innerInnerVariable = c;
        }

        public int returnInnerVariable() {
            return this.innerInnerVariable;
        }
    }

}
