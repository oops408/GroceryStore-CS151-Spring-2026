public class Product {
    private String name;
    private double price;
    private int quantity;
    private int id;

    public Product(String name, double price, int quantity, int id) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getId() {
        return id;
    }

    public void addStock(int amount) {
        if (amount > 0) {
            quantity += amount;
        }
    }

    public void removeStock(int amount) {
        if (amount > 0 && amount <= quantity) {
            quantity -= amount;
        }
    }

    public void setPrice(double newPrice) {
        if (newPrice >= 0) {
            price = newPrice;
        }
    }

    @Override
    public String toString() {
        return "Product ID: " + id + ", Name: " + name + ", Price: $" + price + ", Quantity: " + quantity;
    }
}