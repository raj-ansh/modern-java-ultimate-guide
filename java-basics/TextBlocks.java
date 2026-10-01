public class TextBlocks {

    public static void main(String[] args) {
        String customer = "Priya";
        String orderId = "BN-1042";

        String email = """
                Hi %s,

                Thank you for shopping at BookNest!
                Your order %s is on its way.

                Happy reading,
                The BookNest Team
                """.formatted(customer, orderId);

        System.out.print(email);

        String json = """
                {
                  "orderId": "BN-1042",
                  "items": 3
                }""";
        System.out.println(json);
    }
}
