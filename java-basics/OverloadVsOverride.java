public class OverloadVsOverride {

    public static void main(String[] args) {
        Animal pet = new Dog();
        Object word = "hello";

        System.out.println(pet.sound());     // runtime type
        System.out.println(describe(word));  // compile-time type
        System.out.println(describe("hello"));
    }

    static String describe(Object value) {
        return "some object";
    }

    static String describe(String value) {
        return "a String";
    }
}

class Animal {

    String sound() {
        return "...";
    }
}

class Dog extends Animal {

    @Override
    String sound() {
        return "Woof";
    }
}
