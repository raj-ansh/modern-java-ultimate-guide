public class ForgetfulMember {

    private final String email;

    public ForgetfulMember(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof ForgetfulMember member
                && email.equals(member.email);
    }

    // hashCode() NOT overridden: this breaks the contract
}
