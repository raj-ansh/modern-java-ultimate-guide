public class SeveralCatches {

    public static void main(String[] args) {
        String[] titles = {"Poems", "Atlas"};
        String[] requests = {"1", "7", "x", null};

        for (String request : requests) {
            System.out.println(pick(titles, request));
        }
    }

    static String pick(String[] titles, String request) {
        try {
            int position = Integer.parseInt(request.strip());
            return "Picked " + titles[position];
        } catch (NumberFormatException | NullPointerException e) {
            return "Invalid request: " + request;
        } catch (ArrayIndexOutOfBoundsException e) {
            return "No book at position " + request;
        } finally {
            System.out.print("(checked " + request + ") ");
        }
    }
}
