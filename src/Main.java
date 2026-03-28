import aisles.Aisles;
import customers.CustomerIdRegistry;
import customers.RegularCustomer;
import customers.VIPCustomer;
import data.StoreDataLoader;
import exceptions.CapacityExceededException;
import input.ConsoleInput;
import inventory.Inventory;
import java.util.List;
import java.util.Scanner;
import menu.StoreMenus;
import products.Products;
import shelf.Shelf;

// main contains unity of all of the classes and functions.
public class Main {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        RegularCustomer customer1 = null;
        VIPCustomer customer2 = null;

        // Inventory setup
        Inventory inventory = new Inventory();
        List<Aisles> aisles = StoreDataLoader.loadDefaultAisles();

        System.out.println("=================================");
        System.out.println("   Welcome to the Grocery Store  ");
        System.out.println("=================================");

        // Prelad shelflist

        Shelf produceShelf = new Shelf("Produce");
        Shelf dairyShelf = new Shelf("Dairy");
        Shelf snacksShelf = new Shelf("Snacks");
        Shelf suppliesShelf = new Shelf("Supplies");

        // Preload inventory and shelves with exception handling
        try {
            // inventory
            inventory.addProduct("Produce", new Products("Apples", 1.99, 20, 1001));
            inventory.addProduct("Dairy", new Products("Milk", 3.49, 8, 1002));
            inventory.addProduct("Snacks", new Products("Chips", 2.99, 5, 1003));
            inventory.addProduct("Supplies", new Products("Rice", 10.99, 50, 1004));
            inventory.addProduct("Produce", new Products("Mango", 1.99, 70, 1005));
            inventory.addProduct("Produce", new Products("Onion", 2.99, 100, 1006));
            System.out.println("Inventory preloaded successfully.");

            // Load shelves with products from inventory

            // produce
            Products apples = inventory.getProduct("Produce", 1001);
            Products mango = inventory.getProduct("Produce", 1005);
            Products onion = inventory.getProduct("Produce", 1006);

            produceShelf.addProduct(new Products(apples.getName(), apples.getPrice(), 2, apples.getID()));
            produceShelf.addProduct(new Products(mango.getName(), mango.getPrice(), 7, mango.getID()));
            produceShelf.addProduct(new Products(onion.getName(), onion.getPrice(), 10, onion.getID()));

            // dairy
            Products milk = inventory.getProduct("Dairy", 1002);
            dairyShelf.addProduct(new Products(milk.getName(), milk.getPrice(), 3, milk.getID()));

            // Snack
            Products chips = inventory.getProduct("Snacks", 1003);
            snacksShelf.addProduct(new Products(chips.getName(), chips.getPrice(),2, chips.getID()));

            // Supplies
            //            inventory.addProduct("Supplies", new Products("Rice", 10.99, 50, 1004));

            Products rice = inventory.getProduct("Supplies", 1004);
            suppliesShelf.addProduct(new Products(rice.getName(), rice.getPrice(),9, rice.getID()));


        } catch (CapacityExceededException e) {
            System.out.println("Setup error: " + e.getMessage());
        }

        int roleChoice = -1;
        while (roleChoice != 4) {
            System.out.println("\n===== Grocery Store =====");
            System.out.println("Who are you?");
            System.out.println("1. Regular customer");
            System.out.println("2. VIP customer");
            System.out.println("3. Employee");
            System.out.println("4. Exit");
            roleChoice = ConsoleInput.readInt(scanner, "Enter your choice: ");

            switch (roleChoice) {
                case 1:
                    if (customer1 == null) {
                        customer1 = registerRegularCustomer(scanner);
                    }
                    StoreMenus.runRegularCustomerSession(scanner, customer1, inventory, aisles);
                    break;
                case 2:
                    if (customer2 == null) {
                        customer2 = registerVipCustomer(scanner);
                    }
                    StoreMenus.runVipCustomerSession(scanner, customer2, inventory, aisles, customer1);
                    break;
                case 3:
                    StoreMenus.runEmployeeSession(scanner, inventory, aisles, customer1, customer2,
                            produceShelf, dairyShelf, snacksShelf, suppliesShelf);
                    break;
                case 4:
                    System.out.println("Thank you for using the Grocery Store System!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }

    private static RegularCustomer registerRegularCustomer(Scanner scanner) {
        System.out.println("\n--- New regular customer ---");
        String first = ConsoleInput.readLine(scanner, "First name: ").trim();
        String last = ConsoleInput.readLine(scanner, "Last name: ").trim();
        if (first.isEmpty()) {
            first = "Guest";
        }
        if (last.isEmpty()) {
            last = "Customer";
        }
        int id = CustomerIdRegistry.nextId();
        RegularCustomer customer = new RegularCustomer(id, first, last);
        System.out.println("Account created. Your customer ID is " + id + ". Your cart is empty.");
        return customer;
    }

    private static VIPCustomer registerVipCustomer(Scanner scanner) {
        System.out.println("\n--- New VIP customer ---");
        String first = ConsoleInput.readLine(scanner, "First name: ").trim();
        String last = ConsoleInput.readLine(scanner, "Last name: ").trim();
        if (first.isEmpty()) {
            first = "Guest";
        }
        if (last.isEmpty()) {
            last = "Customer";
        }
        int id = CustomerIdRegistry.nextId();
        VIPCustomer customer = new VIPCustomer(id, first, last, 0.10);
        System.out.println("Account created. Your customer ID is " + id + " (VIP). Your cart is empty.");
        return customer;
    }
}
