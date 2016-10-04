/* test several class/interface declaration
   test several methods with formal parameters and empty body
   test inheritance and realizaton  
 */

public class Simple {
    void method1 () {
    }
    static int method2 () { }
    static public String method3 () { }
    public CustomType method4 (anotherCustom custom) { }
    private float method5 () {}
    protected static final long method6 (char sym, type1 var) { }
}

interface MyInterface { }

private static interface AnotherInterface { }

private class EmptyPrivateClass extends PrivateStaticClass implements MyInterface, AnotherInterface {}
