# Modern Java: The Ultimate Guide — Companion Code

This is the complete code from both volumes of *Modern Java: The Ultimate Guide* by Raj Kumar Panda. Every file here is the code printed in the book, and every one of them was compiled and run on Java 25 while the book was written.

Type the code yourself first, because that's how you learn it. Use these files to check your work, or to catch up if you start in the middle of the book.

## What You Need

- **JDK 25** (any vendor). Check it with `java --version`.
- **Maven 3.9 or later** for BookNest, which is a Maven project from Chapter 2 on. Check it with `mvn --version`.

## What's Inside

| Folder | Contents |
|---|---|
| `java-basics/` | The small example programs, at the same paths the book prints above each listing. Chapters 1–17 keep most of theirs directly in this folder; later chapters have their own folders, such as `java-basics/ch18-streams/` |
| `access-demo/` | The access-modifier example from Chapter 9 |
| `bisect-demo/` | The `git bisect` example from Chapter 27 |
| `booknest/` | The finished BookNest application: Chapter 29, version 1.1.0 |
| `booknest-by-chapter/chNN/` | BookNest exactly as it stands at the end of chapter NN, for every chapter that changes it |

Listings that the book prints to show a compiler error aren't included, because they're meant not to compile.

## Running the Examples

Most examples are single source files. Run them from their folder:

```bash
cd java-basics/ch18-streams
java FirstPipeline.java
```

One Chapter 21 example uses a preview feature:

```bash
java --enable-preview PrimitivePatterns.java
```

The JDBC examples in Chapter 25 need the H2 database driver: put `h2-2.4.240.jar` in a `lib` folder next to them and run, for example, `java -cp "lib/*" SqlTour.java`, as the chapter explains.

## Running BookNest

```bash
cd booknest
mvn package
java -jar target/booknest.jar
```

`mvn package` also runs all 46 tests. The shop keeps its catalog, orders, database, and logs in the `data` folder. Chapters 25–29 describe the settings, for example `-Dbooknest.db=mem` for a throwaway in-memory database.

To see BookNest as it was at the end of an earlier chapter, use `booknest-by-chapter/chNN/` instead, and build and run it the way that chapter shows.

## Questions and Corrections

If something doesn't work as the book describes, first compare your file with the one here. A missing import or a typo is the usual cause. If the problem is in the book itself, please report it where you bought the book, so it can be fixed in the next printing.

The latest version of this code is always at https://github.com/raj-ansh/modern-java-ultimate-guide.

Copyright © 2026 Raj Kumar Panda. This code is provided for readers of the book to learn from and to use in their own projects.
