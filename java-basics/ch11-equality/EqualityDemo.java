import java.util.HashSet;
import java.util.Set;

public class EqualityDemo {

    public static void main(String[] args) {
        Member priya = new Member("Priya@Example.com", "Priya");
        Member again = new Member("priya@example.com", "Priya S.");
        priya.addPoints(120);

        System.out.println(priya);
        System.out.println("Same member?  " + priya.equals(again));
        System.out.println("Same hash?    "
                + (priya.hashCode() == again.hashCode()));

        Set<Member> members = new HashSet<>();
        members.add(priya);
        members.add(again);
        System.out.println("Members in set: " + members.size());

        Set<ForgetfulMember> broken = new HashSet<>();
        broken.add(new ForgetfulMember("arjun@example.com"));
        boolean found = broken.contains(
                new ForgetfulMember("arjun@example.com"));
        System.out.println("Broken set finds Arjun? " + found);
    }
}
