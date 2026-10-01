public class GreetingDemo {

    public static void main(String[] args) {
        Greeting member = new MemberGreeting("Priya");
        Greeting guest = Greeting.forGuest();

        System.out.println(member.welcome());
        System.out.println(guest.welcome());
    }
}
