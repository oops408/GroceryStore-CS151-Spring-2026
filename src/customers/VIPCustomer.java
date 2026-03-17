package customers;

public class VIPCustomer extends Customer {
    public static final double DISCOUNT_RATE = 0.15;

    public VIPCustomer(int customerId, String firstName, String lastName) {
        super(customerId, firstName, lastName);
    }

    @Override
    public double getDiscountRate() {
        return DISCOUNT_RATE;
    }

    public double applyDiscount(double subtotal) {
        return subtotal * (1.0 - getDiscountRate());
    }
}

