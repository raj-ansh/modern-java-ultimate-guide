public class RaggedArray {

    public static void main(String[] args) {
        String[] books = {"Short Stories", "Java for Everyone",
                "Poems"};
        int[][] chapterPages = {
                {12, 9, 15},
                {30, 42, 38, 51, 47},
                {4}
        };

        for (int b = 0; b < books.length; b++) {
            int pages = 0;
            for (int chapter : chapterPages[b]) {
                pages += chapter;
            }
            System.out.println(books[b] + ": "
                    + chapterPages[b].length + " chapters, "
                    + pages + " pages");
        }
    }
}
