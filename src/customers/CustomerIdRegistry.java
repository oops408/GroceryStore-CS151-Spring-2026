package customers;

/**
 * Issues unique customer IDs for the current run of the application.
 */
public final class CustomerIdRegistry {

    private static int nextId = 1;

    private CustomerIdRegistry() {
    }

    public static synchronized int nextId() {
        return nextId++;
    }
}
