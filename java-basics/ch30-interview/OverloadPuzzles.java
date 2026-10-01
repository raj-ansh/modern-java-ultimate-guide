public class OverloadPuzzles {

    static void print(Object o) { say("print(Object)"); }
    static void print(String s) { say("print(String)"); }

    static void size(long n)    { say("size(long)"); }
    static void size(Integer n) { say("size(Integer)"); }
    static void size(int... n)  { say("size(int...)"); }

    static class Parent {
        String name = "Parent field";
        static String kind() { return "Parent.kind (static)"; }
        String hello() { return "Parent.hello"; }
    }

    static class Child extends Parent {
        String name = "Child field";
        static String kind() { return "Child.kind (static)"; }
        @Override
        String hello() { return "Child.hello"; }
    }

    static void say(String s) { System.out.println(s); }

    public static void main(String[] args) {
        print(null);
        size(5);

        Parent p = new Child();
        System.out.println(p.hello());
        System.out.println(p.name);
        System.out.println(((Child) p).name);
        System.out.println(p.kind());       // static: no override
    }
}
