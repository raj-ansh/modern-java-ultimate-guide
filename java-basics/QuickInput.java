void main() {
    String name = IO.readln("Your name: ");
    String genre = IO.readln("Favourite genre: ");
    IO.println("Hi " + name + "! We'll show you more "
            + genre.toLowerCase() + " books.");
}
