// A compact source file (Java 25): no class declaration, and the
// whole java.base module (java.util, java.time, ...) is imported.

record Session(String title, LocalDate day, int pages) {}

final List<Session> LOG = List.of(
        new Session("Dune", LocalDate.of(2026, 3, 2), 40),
        new Session("Dune", LocalDate.of(2026, 3, 3), 65),
        new Session("Emma", LocalDate.of(2026, 3, 3), 30),
        new Session("Dune", LocalDate.of(2026, 3, 7), 55),
        new Session("Emma", LocalDate.of(2026, 3, 8), 45));

void main() {
    var pagesPerBook = LOG.stream()
            .collect(Collectors.groupingBy(Session::title,
                    TreeMap::new,
                    Collectors.summingInt(Session::pages)));
    IO.println("Pages per book: " + pagesPerBook);

    var weekend = LOG.stream()
            .filter(s -> s.day().getDayOfWeek().getValue() >= 6)
            .mapToInt(Session::pages)
            .sum();
    IO.println("Weekend pages: " + weekend);

    var report = """
            Sessions: %d
            Busiest day: %s
            """.formatted(LOG.size(), busiestDay());
    IO.print(report);
}

LocalDate busiestDay() {
    return LOG.stream()
            .collect(Collectors.groupingBy(Session::day,
                    Collectors.summingInt(Session::pages)))
            .entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElseThrow();
}
