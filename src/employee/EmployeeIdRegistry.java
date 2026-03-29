package employee;

/**
 * Issues unique employee IDs for the current run of the application.
 */
public final class EmployeeIdRegistry {

    private static int nextId = 2001;

    private EmployeeIdRegistry() {
    }

    public static synchronized int nextId() {
        return nextId++;
    }
}