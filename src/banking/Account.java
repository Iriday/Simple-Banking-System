package banking;

public class Account {
    public final long id;
    public final String number;
    public final String pin;
    public final long balance;

    public Account(long id, String number, String pin, long balance) {
        this.id = id;
        this.number = number;
        this.pin = pin;
        this.balance = balance;
    }
}
