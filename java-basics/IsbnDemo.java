public class IsbnDemo {

    public static void main(String[] args) {
        Isbn clean = new Isbn("978-1-00-000001-1");
        Isbn same = new Isbn(" 9781000000011 ");

        System.out.println(clean);
        System.out.println("Equal after normalizing? "
                + clean.equals(same));
        System.out.println("Formatted: " + clean.formatted());

        Isbn broken = new Isbn("12345");
        System.out.println("Never printed: " + broken);
    }
}

record Isbn(String value) {

    Isbn {
        if (value == null) {
            throw new IllegalArgumentException(
                    "ISBN must not be null");
        }
        value = value.strip().replace("-", "");
        if (!value.matches("\\d{13}")) {
            throw new IllegalArgumentException(
                    "ISBN must have 13 digits: " + value);
        }
    }

    String formatted() {
        return value.substring(0, 3) + "-" + value.substring(3);
    }
}
