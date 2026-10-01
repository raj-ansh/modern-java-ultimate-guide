import java.util.Arrays;

public class EnumBasics {

    public static void main(String[] args) {
        Genre favourite = Genre.MYSTERY;

        System.out.println("Favourite: " + favourite);
        System.out.println("All genres: "
                + Arrays.toString(Genre.values()));
        System.out.println("name():    " + favourite.name());
        System.out.println("ordinal(): " + favourite.ordinal());
        System.out.println("Is mystery? "
                + (favourite == Genre.MYSTERY));

        Genre parsed = Genre.valueOf("POETRY");
        System.out.println("Parsed:    " + parsed);

        for (Genre genre : Genre.values()) {
            System.out.println(genre + " shelf: " + shelfFor(genre));
        }

        System.out.println(Genre.valueOf("ROMANCE"));
    }

    static String shelfFor(Genre genre) {
        return switch (genre) {
            case FICTION, MYSTERY -> "Ground floor";
            case SCIENCE -> "First floor";
            case POETRY -> "Reading corner";
        };
    }
}

enum Genre {
    FICTION, MYSTERY, SCIENCE, POETRY
}
