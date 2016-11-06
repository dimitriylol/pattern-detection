package parse_java;

interface IStmt {
    default double toDouble (int a) {
        return (double)a;
    }
    static void printSadness (String sadMessage) {
        System.out.println(sadMessage + "...Cry");
    }
}

public class Stmt implements IStmt {
    public Stmt () {
        String initAttr = "init";
    }
    
    public static foo (int i) {
        int a = i + 14;
        IStmt self = new Stmt();
        return i + 88;
    }
}
