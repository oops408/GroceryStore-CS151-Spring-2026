package customers;

// Represents a VIP customer with additional benefits like discounts and reward points
public class VIPCustomer extends Customer {

    // Discount rate applied during checkout (e.g., 0.10 = 10%)
    private double discountRate;
    
    // Reward points accumulated by the VIP customer
    private int points;

    // Constructor to create a VIP customer with a discount rate
    public VIPCustomer(int customerId, String firstName, String lastName, double discountRate) {
        super(customerId, firstName, lastName);
        this.discountRate = discountRate;
        this.points = 0;
    }

    // Returns the discount rate for this VIP customer
    @Override
    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        if (discountRate >= 0 && discountRate <= 1) {
            this.discountRate = discountRate;
        }
    }

    // Returns the current reward points of the customer
    public int getPoints() {
        return points;
    }

    // Adds reward points to the VIP customer account
    public void addPoints(int pointsToAdd) {
        if (pointsToAdd > 0) {
            points += pointsToAdd;
        }
    }

    public void redeemPoints(int pointsToRedeem) {
        if (pointsToRedeem > 0 && pointsToRedeem <= points) {
            points -= pointsToRedeem;
        }
    }

    // Displays VIP benefits including discount and reward points
    public void viewVIPBenefits() {
        System.out.println("VIP Customer: " + firstName + " " + lastName);
        System.out.println("Discount Rate: " + (discountRate * 100) + "%");
        System.out.println("Points: " + points);
    }

    @Override
    public void viewBenefits() {
        viewVIPBenefits();
    }

    @Override
    public String getAccountType() {
        return "VIP";
    }
}
