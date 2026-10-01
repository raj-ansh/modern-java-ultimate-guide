public class InitOrder {

    static class Parent {
        static { System.out.println("Parent static block"); }
        { System.out.println("Parent instance block"); }
        Parent() { System.out.println("Parent constructor"); }
    }

    static class Child extends Parent {
        static { System.out.println("Child static block"); }
        { System.out.println("Child instance block"); }
        Child() { System.out.println("Child constructor"); }
    }

    public static void main(String[] args) {
        new Child();
        System.out.println("--- second object ---");
        new Child();
    }
}
