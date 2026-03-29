package employee;

import aisles.Aisles;
import customers.Customer;
import customers.RegularCustomer;
import customers.VIPCustomer;
import exceptions.InvalidQuantityException;
import exceptions.NotFoundException;
import input.ConsoleInput;
import inventory.Inventory;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import products.Products;
import shelf.Shelf;

public final class EmployeeMenu {

    private EmployeeMenu() {
    }

    public static void run(
            Scanner scanner,
            Inventory inventory,
            Map<Integer, RegularCustomer> regularCustomers,
            Map<Integer, VIPCustomer> vipCustomers,
            Shelf produceShelf,
            Shelf dairyShelf,
            Shelf snacksShelf,
            Shelf suppliesShelf,
            List<Aisles> aisles,
            Employee signedInEmployee) {

        System.out.println();
        System.out.println("Signed in as employee: " + signedInEmployee.getFullName()
                + " (ID " + signedInEmployee.getEmployeeID()
                + ", " + signedInEmployee.getDepartment() + ")");

        if (signedInEmployee instanceof Stocker stocker) {
            runStockerMenu(scanner, inventory, produceShelf, dairyShelf,
                    snacksShelf, suppliesShelf, aisles, stocker);
        } else if (signedInEmployee instanceof Manager manager) {
            runManagerMenu(scanner, inventory, regularCustomers, vipCustomers,
                    manager);
        } else {
            System.out.println("Unknown employee type.");
        }
    }

    private static void runStockerMenu(
            Scanner scanner,
            Inventory inventory,
            Shelf produceShelf,
            Shelf dairyShelf,
            Shelf snacksShelf,
            Shelf suppliesShelf,
            List<Aisles> aisles,
            Stocker stocker) {

        System.out.println("Welcome, Stocker! You can view low stock products and restock shelves.");

        int stockerInput = -1;
        int lowStockThreshold = 10;

        while (stockerInput != 7) {
            System.out.println();
            System.out.println("--- Stocker Menu ---");
            System.out.println("1. View Low Stock Products");
            System.out.println("2. Restock Product");
            System.out.println("3. View Shelf");
            System.out.println("4. Decrease product stock (inventory)");
            System.out.println("5. View low stock in aisles");
            System.out.println("6. View inventory");
            System.out.println("7. Sign out (type exit to quit the program)");
            stockerInput = ConsoleInput.readInt(scanner, "Enter your choice: ");

            switch (stockerInput) {
                case 1:
                    Shelf selectedShelf = chooseShelf(scanner, produceShelf, dairyShelf,
                            snacksShelf, suppliesShelf);
                    if (selectedShelf != null) {
                        lowStockThreshold = ConsoleInput.readInt(
                                scanner, "Enter low-stock threshold: ");

                        System.out.println();
                        stocker.viewLowShelfStock(selectedShelf, lowStockThreshold);

                        for (Products product : selectedShelf.getProducts()) {
                            if (product.getQuantity() < lowStockThreshold) {
                                System.out.println(product.getName() + " needs "
                                        + (lowStockThreshold - product.getQuantity())
                                        + " more items.");
                            }
                        }
                    }
                    break;

                case 2:
                    Shelf selectedRestockShelf = chooseShelf(scanner, produceShelf, dairyShelf,
                            snacksShelf, suppliesShelf);
                    if (selectedRestockShelf == null) {
                        break;
                    }

                    if (selectedRestockShelf.getProducts().isEmpty()) {
                        System.out.println("This shelf is empty.");
                        break;
                    }

                    System.out.println();
                    System.out.println("Reminder " + lowStockThreshold + " is the max.");
                    System.out.println("Products on " + selectedRestockShelf.getSection() + " shelf:");

                    int itemNumber = 1;
                    for (Products product : selectedRestockShelf.getProducts()) {
                        System.out.println(itemNumber + ". " + product.getName()
                                + " (ID: " + product.getID()
                                + ", Current Stock: " + product.getQuantity() + ")");
                        itemNumber++;
                    }

                    int productChoice = ConsoleInput.readInt(
                            scanner, "Choose product to restock: ");

                    Products chosenProduct = null;
                    int currentIndex = 1;
                    for (Products product : selectedRestockShelf.getProducts()) {
                        if (currentIndex == productChoice) {
                            chosenProduct = product;
                            break;
                        }
                        currentIndex++;
                    }

                    if (chosenProduct == null) {
                        System.out.println("Invalid product choice.");
                        break;
                    }

                    int restockAmount = ConsoleInput.readInt(
                            scanner, "Enter quantity to restock: ");

                    if (restockAmount <= 0) {
                        System.out.println("Quantity must be greater than 0.");
                        break;
                    }

                    int newQuantity = chosenProduct.getQuantity() + restockAmount;
                    if (newQuantity > lowStockThreshold) {
                        System.out.println("Error: restock amount exceeds threshold of "
                                + lowStockThreshold + ".");
                        System.out.println(chosenProduct.getName()
                                + " would become " + newQuantity + " in stock.");
                        break;
                    }

                    try {
                        chosenProduct.stockToShelf(restockAmount);
                        System.out.println("Restocked successfully.");
                        System.out.println(chosenProduct.getName() + " now has "
                                + chosenProduct.getQuantity() + " on the shelf.");
                    } catch (InvalidQuantityException e) {
                        System.out.println("Restock error: " + e.getMessage());
                    }
                    break;

                case 3:
                    int viewChoice = chooseShelfOrAll(scanner);
                    switch (viewChoice) {
                        case 1:
                            produceShelf.printShelf();
                            break;
                        case 2:
                            dairyShelf.printShelf();
                            break;
                        case 3:
                            snacksShelf.printShelf();
                            break;
                        case 4:
                            suppliesShelf.printShelf();
                            break;
                        case 5:
                            System.out.println("\n--- Produce Shelf ---");
                            produceShelf.printShelf();

                            System.out.println("\n--- Dairy Shelf ---");
                            dairyShelf.printShelf();

                            System.out.println("\n--- Snacks Shelf ---");
                            snacksShelf.printShelf();

                            System.out.println("\n--- Supplies Shelf ---");
                            suppliesShelf.printShelf();
                            break;
                        default:
                            System.out.println("Invalid shelf choice.");
                            break;
                    }
                    break;

                case 4:
                    try {
                        String invSection = ConsoleInput.readLine(scanner, "Enter section: ");
                        int decProductId = ConsoleInput.readInt(scanner, "Enter product ID: ");
                        int decQty = ConsoleInput.readInt(scanner, "Enter quantity to decrease: ");
                        inventory.decreaseStock(invSection, decProductId, decQty);
                        System.out.println("Product stock decreased successfully.");
                    } catch (NotFoundException | InvalidQuantityException e) {
                        System.out.println("Decrease stock error: " + e.getMessage());
                    }
                    break;

                case 5:
                    int aisleThreshold = ConsoleInput.readInt(
                            scanner, "Enter low-stock threshold: ");
                    printLowStockAisles(aisles, aisleThreshold);
                    break;

                case 6:
                    inventory.printInventory();
                    break;

                case 7:
                    System.out.println("Signing out of Stocker Menu.");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void runManagerMenu(
            Scanner scanner,
            Inventory inventory,
            Map<Integer, RegularCustomer> regularCustomers,
            Map<Integer, VIPCustomer> vipCustomers,
            Manager manager) {

        System.out.println("Welcome, Manager! You can manage inventory and view customer history.");

        int managerInput = -1;

        while (managerInput != 8) {
            System.out.println();
            System.out.println("--- Manager Menu ---");
            System.out.println("1. Add Product to Inventory");
            System.out.println("2. Remove Product from Inventory");
            System.out.println("3. Change Product Price");
            System.out.println("4. Restock Inventory Products");
            System.out.println("5. View Inventory");
            System.out.println("6. View Customer Info");
            System.out.println("7. Find product by ID");
            System.out.println("8. Sign out (type exit to quit the program)");
            managerInput = ConsoleInput.readInt(scanner, "Enter your choice: ");

            switch (managerInput) {
                case 1:
                    handleAddProduct(scanner, inventory, manager);
                    break;

                case 2:
                    handleRemoveProduct(scanner, inventory);
                    break;

                case 3:
                    handleChangePrice(scanner, inventory, manager);
                    break;

                case 4:
                    handleRestockInventory(scanner, inventory);
                    break;

                case 5:
                    System.out.println();
                    manager.viewInventory(inventory);
                    break;

                case 6:
                    viewCustomerInfo(scanner, manager, regularCustomers, vipCustomers);
                    break;

                case 7:
                    try {
                        int lookupId = ConsoleInput.readInt(scanner, "Enter product ID to find: ");
                        Products foundProduct = inventory.findProduct(lookupId);
                        System.out.println("Found: " + foundProduct);
                    } catch (NotFoundException e) {
                        System.out.println("Search error: " + e.getMessage());
                    }
                    break;

                case 8:
                    System.out.println("Signing out of Manager Menu.");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void handleAddProduct(
            Scanner scanner,
            Inventory inventory,
            Manager manager) {

        System.out.println();
        String section = chooseInventorySection(scanner);
        if (section.isEmpty()) {
            return;
        }

        boolean done = false;
        while (!done) {
            try {
                System.out.println();
                String name = ConsoleInput.readLine(scanner, "Enter product name: ");
                double price = ConsoleInput.readDouble(scanner, "Enter price: ");
                int quantity = ConsoleInput.readInt(scanner, "Enter quantity: ");
                int id = inventory.getNextProductId();

                Products newProduct = new Products(name, price, quantity, id);
                boolean added = manager.addProduct(inventory, section, newProduct);

                if (added) {
                    System.out.println();
                    System.out.println("Product added:");
                    System.out.println("Name: " + name);
                    System.out.println("Section: " + section);
                    System.out.println("ID: " + id);
                    System.out.println("Price: $" + price);
                    System.out.println("Quantity: " + quantity);
                    done = true;
                } else {
                    System.out.println("Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input or stock exceeds 100. Please try again.");
            }
        }
    }

    private static void handleRemoveProduct(
            Scanner scanner,
            Inventory inventory) {

        System.out.println();
        String section = chooseInventorySection(scanner);
        if (section.isEmpty()) {
            return;
        }

        System.out.println();
        boolean hasProducts = inventory.printSectionProducts(section);
        if (!hasProducts) {
            return;
        }

        boolean done = false;
        while (!done) {
            try {
                System.out.println();
                int productId = ConsoleInput.readInt(
                        scanner, "Enter product ID (0 to cancel): ");

                if (productId == 0) {
                    System.out.println("Cancelled.");
                    break;
                }

                Products product = inventory.getProduct(section, productId);
                if (product == null) {
                    System.out.println("Error: product ID not found in " + section + ".");
                    continue;
                }

                int quantityToRemove = ConsoleInput.readInt(
                        scanner, "Enter quantity to remove (0 to cancel): ");

                if (quantityToRemove == 0) {
                    System.out.println("Cancelled.");
                    break;
                }

                String productName = product.getName();
                inventory.decreaseStock(section, productId, quantityToRemove);

                System.out.println();
                System.out.println("Inventory updated successfully.");
                System.out.println("Removed from: " + section);
                System.out.println("Product: " + productName);
                System.out.println("ID: " + productId);
                System.out.println("Amount Removed: " + quantityToRemove);
                System.out.println("Remaining Stock: " + product.getQuantity());

                done = true;
            } catch (NotFoundException | InvalidQuantityException e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Please try again.");
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter numbers only.");
            }
        }
    }

    private static void handleChangePrice(
            Scanner scanner,
            Inventory inventory,
            Manager manager) {

        System.out.println();
        String section = chooseInventorySection(scanner);
        if (section.isEmpty()) {
            return;
        }

        System.out.println();
        boolean hasProducts = inventory.printSectionProducts(section);
        if (!hasProducts) {
            return;
        }

        boolean done = false;
        while (!done) {
            try {
                System.out.println();
                int productId = ConsoleInput.readInt(
                        scanner, "Enter product ID (0 to cancel): ");

                if (productId == 0) {
                    System.out.println("Cancelled.");
                    break;
                }

                double newPrice = ConsoleInput.readDouble(scanner, "Enter new price: ");

                Products product = inventory.getProduct(section, productId);
                if (product == null) {
                    System.out.println("Error: product ID not found in " + section + ".");
                    continue;
                }

                String productName = product.getName();
                manager.changePrice(inventory, section, productId, newPrice);

                System.out.println();
                System.out.println("Price updated successfully.");
                System.out.println("Section: " + section);
                System.out.println("Product: " + productName);
                System.out.println("ID: " + productId);
                System.out.println("New Price: $" + product.getPrice());

                done = true;
            } catch (Exception e) {
                System.out.println("Invalid input. Please try again.");
            }
        }
    }

    private static void handleRestockInventory(
            Scanner scanner,
            Inventory inventory) {

        System.out.println();
        String section = chooseInventorySection(scanner);
        if (section.isEmpty()) {
            return;
        }

        System.out.println();
        boolean hasProducts = inventory.printSectionProducts(section);
        if (!hasProducts) {
            return;
        }

        boolean done = false;
        while (!done) {
            try {
                int productId = ConsoleInput.readInt(
                        scanner, "Enter product ID (0 to cancel): ");

                if (productId == 0) {
                    System.out.println("Cancelled.");
                    break;
                }

                Products product = inventory.getProduct(section, productId);
                if (product == null) {
                    System.out.println("Error: product ID not found in " + section + ".");
                    continue;
                }

                int quantityToRestock = ConsoleInput.readInt(
                        scanner, "Enter quantity to restock (0 to cancel): ");

                if (quantityToRestock == 0) {
                    System.out.println("Cancelled.");
                    break;
                }

                inventory.restockProduct(section, productId, quantityToRestock);

                System.out.println();
                System.out.println("Inventory restocked successfully.");
                System.out.println("Section: " + section);
                System.out.println("Product: " + product.getName());
                System.out.println("ID: " + productId);
                System.out.println("Amount Added: " + quantityToRestock);
                System.out.println("New Stock: " + product.getQuantity());

                done = true;
            } catch (NotFoundException | InvalidQuantityException e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Please try again.");
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter numbers only.");
            }
        }
    }

    private static void viewCustomerInfo(
            Scanner scanner,
            Manager manager,
            Map<Integer, RegularCustomer> regularCustomers,
            Map<Integer, VIPCustomer> vipCustomers) {

        System.out.println();
        System.out.println("Choose a customer type:");
        System.out.println("1. Regular Customer");
        System.out.println("2. VIP Customer");
        int customerChoice = ConsoleInput.readInt(scanner, "Enter choice: ");

        int customerId = ConsoleInput.readInt(scanner, "Enter customer ID: ");

        Customer selectedCustomer = null;

        switch (customerChoice) {
            case 1:
                selectedCustomer = regularCustomers.get(customerId);
                break;
            case 2:
                selectedCustomer = vipCustomers.get(customerId);
                break;
            default:
                System.out.println("Invalid customer type.");
                return;
        }

        if (selectedCustomer == null) {
            System.out.println("No customer found with ID " + customerId + ".");
            return;
        }

        System.out.println();
        selectedCustomer.displayCustomerInfo();
        selectedCustomer.getCart().viewCart();
        manager.viewCustomerHistory(selectedCustomer);
    }

    private static Shelf chooseShelf(
            Scanner scanner,
            Shelf produceShelf,
            Shelf dairyShelf,
            Shelf snacksShelf,
            Shelf suppliesShelf) {

        System.out.println("1. Produce");
        System.out.println("2. Dairy");
        System.out.println("3. Snacks");
        System.out.println("4. Supplies");
        int sectionChoice = ConsoleInput.readInt(scanner, "Enter section to check: ");

        switch (sectionChoice) {
            case 1:
                return produceShelf;
            case 2:
                return dairyShelf;
            case 3:
                return snacksShelf;
            case 4:
                return suppliesShelf;
            default:
                System.out.println("Invalid shelf.");
                return null;
        }
    }

    private static int chooseShelfOrAll(Scanner scanner) {
        System.out.println();
        System.out.println("Choose a shelf to view:");
        System.out.println("1. Produce");
        System.out.println("2. Dairy");
        System.out.println("3. Snacks");
        System.out.println("4. Supplies");
        System.out.println("5. Print All Shelves");
        return ConsoleInput.readInt(scanner, "Enter choice: ");
    }

    private static String chooseInventorySection(Scanner scanner) {
        System.out.println("Choose a section:");
        System.out.println("1. Produce");
        System.out.println("2. Dairy");
        System.out.println("3. Snacks");
        System.out.println("4. Supplies");
        int choice = ConsoleInput.readInt(scanner, "Enter choice: ");

        switch (choice) {
            case 1:
                return "Produce";
            case 2:
                return "Dairy";
            case 3:
                return "Snacks";
            case 4:
                return "Supplies";
            default:
                System.out.println("Invalid section.");
                return "";
        }
    }

    private static void printLowStockAisles(List<Aisles> aisles, int threshold) {
        boolean any = false;

        for (Aisles aisle : aisles) {
            boolean aisleHeaderPrinted = false;

            for (Map.Entry<Integer, List<Products>> entry : aisle.getShelves().entrySet()) {
                int shelfNum = entry.getKey();

                for (Products product : entry.getValue()) {
                    if (product.getQuantity() < threshold) {
                        if (!aisleHeaderPrinted) {
                            System.out.println();
                            System.out.println("Aisle " + aisle.getAisleNumber()
                                    + " (" + aisle.getAisleType() + ")");
                            aisleHeaderPrinted = true;
                            any = true;
                        }

                        System.out.println("  Shelf " + shelfNum + ": "
                                + product.getName()
                                + " (ID " + product.getID()
                                + ", stock " + product.getQuantity() + ")");
                    }
                }
            }
        }

        if (!any) {
            System.out.println("No products below threshold "
                    + threshold + " in any aisle.");
        }
    }
}