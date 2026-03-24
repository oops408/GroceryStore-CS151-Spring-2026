import customers.RegularCustomer;
import customers.VIPCustomer;
import data.StoreDataLoader;
import aisles.Aisles;
import exceptions.CapacityExceededException;
import exceptions.InvalidQuantityException;
import exceptions.NotFoundException;
import inventory.Inventory;
import java.util.List;
import java.util.Scanner;
import products.Products;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Customer setup
        RegularCustomer customer1 = new RegularCustomer(101, "Vishal", "Raichur");
        VIPCustomer customer2 = new VIPCustomer(102, "John", "Doe", 0.10);

        customer1.getCart().addItem("Milk");
        customer1.getCart().addItem("Bread");
        customer2.getCart().addItem("Eggs");
        customer2.addPoints(50);

        // Inventory setup
        Inventory inventory = new Inventory();
        List<Aisles> aisles = StoreDataLoader.loadDefaultAisles();

        System.out.println("=================================");
        System.out.println("   Welcome to the Grocery Store  ");
        System.out.println("=================================");

        // Preload inventory with exception handling
        try {
            inventory.addProduct("Produce", new Products("Apples", 1.99, 20, 1001));
            inventory.addProduct("Dairy", new Products("Milk", 3.49, 8, 1002));
            inventory.addProduct("Snacks", new Products("Chips", 2.99, 5, 1003));
            inventory.addProduct("Supplies", new Products("Rice", 10.99, 50, 1004));
            System.out.println("Inventory preloaded successfully.");
        } catch (CapacityExceededException e) {
            System.out.println("Setup error: " + e.getMessage());
        }

        // Automatic inventory tests
        System.out.println("\n========== INVENTORY TESTS ==========");

        System.out.println("\n--- Print Inventory ---");
        inventory.printInventory();

        System.out.println("\n--- Find Product by ID (valid) ---");
        try {
            Products found = inventory.findProduct(1002);
            System.out.println("Found: " + found);
        } catch (NotFoundException e) {
            System.out.println("Find error: " + e.getMessage());
        }

        System.out.println("\n--- Search By Name: 'i' ---");
        List<Products> matches = inventory.searchByName("i");
        if (matches.isEmpty()) {
            System.out.println("No matching products found.");
        } else {
            for (Products product : matches) {
                System.out.println(product);
            }
        }

        System.out.println("\n--- Low Stock Products (< 10) ---");
        List<Products> lowStock = inventory.listLowStock(10);
        if (lowStock.isEmpty()) {
            System.out.println("No low-stock products.");
        } else {
            for (Products product : lowStock) {
                System.out.println(product);
            }
        }

        System.out.println("\n--- Restock Milk by 10 ---");
        try {
            inventory.restockProduct("Dairy", 1002, 10);
            System.out.println("Restock successful.");
        } catch (NotFoundException | InvalidQuantityException e) {
            System.out.println("Restock error: " + e.getMessage());
        }

        System.out.println("\n--- Decrease Apples stock by 5 ---");
        try {
            inventory.decreaseStock("Produce", 1001, 5);
            System.out.println("Decrease stock successful.");
        } catch (NotFoundException | InvalidQuantityException e) {
            System.out.println("Decrease stock error: " + e.getMessage());
        }

        System.out.println("\n--- Remove Chips from Snacks ---");
        try {
            inventory.removeProduct("Snacks", 1003);
            System.out.println("Remove product successful.");
        } catch (NotFoundException e) {
            System.out.println("Remove error: " + e.getMessage());
        }

        System.out.println("\n--- Updated Inventory ---");
        inventory.printInventory();

        System.out.println("\n========== INVENTORY EDGE CASES ==========");

        System.out.println("\n--- Edge Case: duplicate product ID ---");
        try {
            inventory.addProduct("Produce", new Products("Green Apples", 2.49, 15, 1001));
        } catch (CapacityExceededException e) {
            System.out.println("Capacity error: " + e.getMessage());
        }

        System.out.println("\n--- Edge Case: invalid product ID ---");
        try {
            inventory.findProduct(9999);
        } catch (NotFoundException e) {
            System.out.println("Expected error: " + e.getMessage());
        }

        System.out.println("\n--- Edge Case: invalid restock quantity ---");
        try {
            inventory.restockProduct("Dairy", 1002, 0);
        } catch (NotFoundException | InvalidQuantityException e) {
            System.out.println("Expected error: " + e.getMessage());
        }

        System.out.println("\n--- Edge Case: insufficient stock ---");
        try {
            inventory.decreaseStock("Produce", 1001, 999);
        } catch (NotFoundException | InvalidQuantityException e) {
            System.out.println("Expected error: " + e.getMessage());
        }

        System.out.println("\n--- Edge Case: remove missing product ---");
        try {
            inventory.removeProduct("Snacks", 9999);
        } catch (NotFoundException e) {
            System.out.println("Expected error: " + e.getMessage());
        }

        // Menu
        int choice = -1;

        while (choice != 13) {
            System.out.println("\n===== Grocery Store Menu =====");
            System.out.println("1. View Regular Customer Info");
            System.out.println("2. View Regular Customer Cart");
            System.out.println("3. Add Item to Regular Customer Cart");
            System.out.println("4. Remove Item from Regular Customer Cart");
            System.out.println("5. Clear Regular Customer Cart");
            System.out.println("6. View VIP Customer Benefits");
            System.out.println("7. View Inventory");
            System.out.println("8. Find Product by ID");
            System.out.println("9. Restock Product");
            System.out.println("10. Decrease Product Stock");
            System.out.println("11. View Low Stock Products");
            System.out.println("12. View Aisles");
            System.out.println("13. Exit");
            choice = readIntInput(scanner, "Enter your choice: ");

            switch (choice) {
                case 1:
                    customer1.displayCustomerInfo();
                    break;

                case 2:
                    customer1.getCart().viewCart();
                    System.out.println("Total items: " + customer1.getCart().getTotalItems());
                    break;

                case 3:
                    String addItem = readLineInput(scanner, "Enter item to add: ");
                    customer1.getCart().addItem(addItem);
                    break;

                case 4:
                    String removeItem = readLineInput(scanner, "Enter item to remove: ");
                    customer1.getCart().removeItem(removeItem);
                    break;

                case 5:
                    customer1.getCart().clearCart();
                    break;

                case 6:
                    customer2.displayCustomerInfo();
                    customer2.viewVIPBenefits();
                    customer2.getCart().viewCart();
                    break;

                case 7:
                    inventory.printInventory();
                    break;

                case 8:
                    try {
                        int productId = readIntInput(scanner, "Enter product ID to find: ");

                        Products found = inventory.findProduct(productId);
                        System.out.println("Found: " + found);
                    } catch (NotFoundException e) {
                        System.out.println("Search error: " + e.getMessage());
                    }
                    break;

                case 9:
                    try {
                        String section = readLineInput(scanner, "Enter section: ");
                        int productId = readIntInput(scanner, "Enter product ID: ");
                        int quantity = readIntInput(scanner, "Enter quantity to restock: ");

                        inventory.restockProduct(section, productId, quantity);
                        System.out.println("Product restocked successfully.");
                    } catch (NotFoundException | InvalidQuantityException e) {
                        System.out.println("Restock error: " + e.getMessage());
                    }
                    break;

                case 10:
                    try {
                        String section = readLineInput(scanner, "Enter section: ");
                        int productId = readIntInput(scanner, "Enter product ID: ");
                        int quantity = readIntInput(scanner, "Enter quantity to decrease: ");

                        inventory.decreaseStock(section, productId, quantity);
                        System.out.println("Product stock decreased successfully.");
                    } catch (NotFoundException | InvalidQuantityException e) {
                        System.out.println("Decrease stock error: " + e.getMessage());
                    }
                    break;

                case 11:
                    int threshold = readIntInput(scanner, "Enter low-stock threshold: ");
                    List<Products> lowStockProducts = inventory.listLowStock(threshold);

                    if (lowStockProducts.isEmpty()) {
                        System.out.println("No low-stock products found.");
                    } else {
                        System.out.println("Low-stock products:");
                        for (Products product : lowStockProducts) {
                            System.out.println(product);
                        }
                    }
                    break;

                case 12:
                    boolean viewingAisles = true;
                    while (viewingAisles) {
                        System.out.println("\nAvailable aisles:");
                        for (Aisles aisle : aisles) {
                            System.out.println("- Aisle " + aisle.getAisleNumber() + " (" + aisle.getAisleType() + ")");
                        }
                        System.out.println("0. Back to main menu");

                        try {
                            int selectedAisleNumber = readIntInput(scanner, "Enter aisle number to view: ");

                            if (selectedAisleNumber == 0) {
                                viewingAisles = false;
                                continue;
                            }

                            Aisles selectedAisle = null;
                            for (Aisles aisle : aisles) {
                                if (aisle.getAisleNumber() == selectedAisleNumber) {
                                    selectedAisle = aisle;
                                    break;
                                }
                            }

                            if (selectedAisle == null) {
                                System.out.println("Aisle not found.");
                                continue;
                            }

                            boolean viewingSingleAisle = true;
                            while (viewingSingleAisle) {
                                selectedAisle.printAisle();
                                System.out.println("\n1. Buy from this aisle");
                                System.out.println("2. Back to aisle list");
                                int aisleAction = readIntInput(scanner, "Choose an option: ");

                                if (aisleAction == 2) {
                                    viewingSingleAisle = false;
                                    continue;
                                }

                                if (aisleAction != 1) {
                                    System.out.println("Invalid choice.");
                                    continue;
                                }

                                int productIdToBuy = readIntInput(scanner, "Enter product ID to buy: ");
                                int quantityToBuy = readIntInput(scanner, "Enter quantity to buy: ");

                                if (quantityToBuy <= 0) {
                                    System.out.println("Quantity must be greater than 0.");
                                    continue;
                                }

                                Products selectedProduct = null;
                                for (Products product : selectedAisle.getAllProducts()) {
                                    if (product.getID() == productIdToBuy) {
                                        selectedProduct = product;
                                        break;
                                    }
                                }

                                if (selectedProduct == null) {
                                    System.out.println("Product ID not found in this aisle.");
                                    continue;
                                }

                                if (selectedProduct.getQuantity() < quantityToBuy) {
                                    System.out.println("Not enough stock. Available: " + selectedProduct.getQuantity());
                                    continue;
                                }

                                selectedProduct.setQuantity(selectedProduct.getQuantity() - quantityToBuy);
                                for (int i = 0; i < quantityToBuy; i++) {
                                    customer1.getCart().addItem(selectedProduct.getName());
                                }

                                System.out.println("Added " + quantityToBuy + " x " + selectedProduct.getName() + " to cart.");
                            }
                        } catch (Exception e) {
                            System.out.println("Invalid input.");
                        }
                    }
                    break;

                case 13:
                    System.out.println("Thank you for using the Grocery Store System!");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }

    private static String readLineInput(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();

        if (input.equalsIgnoreCase("EXIT")) {
            gracefulExit(scanner);
        }

        return input;
    }

    private static int readIntInput(Scanner scanner, String prompt) {
        while (true) {
            String input = readLineInput(scanner, prompt);
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static void gracefulExit(Scanner scanner) {
        System.out.println("EXIT keyword detected. Closing the Grocery Store System. Goodbye!");
        scanner.close();
        System.exit(0);
    }
}