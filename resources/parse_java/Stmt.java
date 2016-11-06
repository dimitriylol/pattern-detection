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
    String initAttr;
    public Stmt () {
        initAttr = "init";
        this.initAttr = "again init";
    }
    
    public static int foo (int i) {
        int a = i + 14;
        IStmt self = new Stmt();
        return i + 88;
    }
}
