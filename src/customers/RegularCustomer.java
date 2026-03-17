package customers;

public class RegularCustomer extends Customer {

    public RegularCustomer(int customerId, String firstName, String lastName) {
        super(customerId, firstName, lastName);
    }

    @Override
    public double getDiscountRate() {
        return 0.0;
    }
}

