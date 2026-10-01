import java.math.BigDecimal;
import java.util.Objects;

public class BuilderDemo {

    /** Many fields, most of them optional. */
    record Listing(String title, BigDecimal price, String author,
                   String series, int edition, boolean signed) {}

    /** Builds a Listing step by step, with sensible defaults. */
    static final class ListingBuilder {
        private final String title;
        private final BigDecimal price;
        private String author = "Unknown";
        private String series = "";
        private int edition = 1;
        private boolean signed = false;

        ListingBuilder(String title, String price) {
            this.title = Objects.requireNonNull(title);
            this.price = new BigDecimal(price);
        }

        ListingBuilder author(String author) {
            this.author = author;
            return this;                    // return this: chainable
        }

        ListingBuilder series(String series) {
            this.series = series;
            return this;
        }

        ListingBuilder edition(int edition) {
            if (edition < 1) {
                throw new IllegalArgumentException(
                        "edition " + edition);
            }
            this.edition = edition;
            return this;
        }

        ListingBuilder signed() {
            this.signed = true;
            return this;
        }

        Listing build() {
            return new Listing(title, price, author, series, edition,
                    signed);
        }
    }

    public static void main(String[] args) {
        // Compare: new Listing("Dune", price, "Frank Herbert", "",
        //                      3, true) - which argument is which?
        Listing signedCopy = new ListingBuilder("Dune", "45.00")
                .author("Frank Herbert")
                .edition(3)
                .signed()
                .build();
        Listing plain =
                new ListingBuilder("Night Trains", "11.25").build();

        describe(signedCopy);
        describe(plain);
    }

    static void describe(Listing listing) {
        System.out.println(listing.title() + " by " + listing.author()
                + ", edition " + listing.edition()
                + (listing.signed() ? ", signed" : "")
                + ": $" + listing.price());
    }
}
