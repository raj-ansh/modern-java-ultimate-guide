import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class HashPuzzles {

    static class Point {
        final int x, y;
        Point(int x, int y) { this.x = x; this.y = y; }

        @Override
        public boolean equals(Object o) {
            return o instanceof Point p && p.x == x && p.y == y;
        }
        // hashCode() is missing
    }

    record GoodPoint(int x, int y) { }

    static class Tag {
        String name;
        Tag(String name) { this.name = name; }

        @Override
        public boolean equals(Object o) {
            return o instanceof Tag t && t.name.equals(name);
        }

        @Override
        public int hashCode() { return Objects.hash(name); }
    }

    public static void main(String[] args) {
        Set<Point> points = new HashSet<>();
        points.add(new Point(1, 2));
        points.add(new Point(1, 2));
        System.out.println("equal points, no hashCode: size "
                + points.size());

        Set<GoodPoint> good = new HashSet<>();
        good.add(new GoodPoint(1, 2));
        good.add(new GoodPoint(1, 2));
        System.out.println("equal records:             size "
                + good.size());

        Set<Tag> tags = new HashSet<>();
        Tag java = new Tag("java");
        tags.add(java);
        java.name = "kotlin";            // mutate a key in the set
        System.out.println("contains mutated key:      "
                + tags.contains(java));
        System.out.println("but the set still has:     size "
                + tags.size());
    }
}
