package customers;

// Represents a regular customer in the system
public class RegularCustomer extends Customer {

    // Constructor for a regular customer
    public RegularCustomer(int customerId, String firstName, String lastName) {
        super(customerId, firstName, lastName);
    }

    // Allows a regular customer to sign up for VIP membership
    public void signUpForVIP() {
        System.out.println(firstName + " " + lastName + " signed up for VIP membership.");
    }

    public void browseStore() {
        System.out.println(getFullName() + " is browsing the store.");
    }

    public void viewCartSummary() {
        System.out.println("Cart contains " + getCart().getTotalItems() + " item(s).");
    }
    @Override
    public void viewBenefits() {
        System.out.println("Regular customer benefits: standard shopping access.");
    }

    @Override
    public String getAccountType() {
        return "Regular";
    }
}
