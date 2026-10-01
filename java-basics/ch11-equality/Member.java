import java.util.Objects;

public class Member {

    private final String email;
    private final String name;
    private int points;

    public Member(String email, String name) {
        this.email = Objects.requireNonNull(email).toLowerCase();
        this.name = Objects.requireNonNull(name);
    }

    public void addPoints(int earned) {
        points += earned;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Member member)) {
            return false;
        }
        return email.equals(member.email);
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }

    @Override
    public String toString() {
        return "Member[" + name + ", " + email + ", "
                + points + " points]";
    }
}
