public class PrintHelper {

    private static final String LINE  = "═".repeat(50);
    private static final String LINE2 = "─".repeat(50);

    // ── Headers / Separators ──────────────────────────────

    public static void printHeader(String title) {
        System.out.println("\n" + LINE);
        System.out.println("  " + title);
        System.out.println(LINE);
    }

    public static void printSeparator() {
        System.out.println(LINE2);
    }

    // ── Main Menu ─────────────────────────────────────────

    public static void printMainMenu() {
        printHeader("WAREHOUSE MANAGER — MAIN MENU");
        System.out.println(" [1] Change product name");
        System.out.println(" [2] Change product price");
        System.out.println(" [3] View warehouse status");
        System.out.println(" [4] Manage products");
        System.out.println(" [5] Create a new table with co-workers");
        System.out.println(" [6] Add new product");
        System.out.println(" [7] Delete product");
        System.out.println(" [8] Search product");
        System.out.println(" [9] Sort products by price");
        System.out.println(" [10] Show products with chosen quantity in stock");
        System.out.println(" [11] Increase quantity");
        System.out.println(" [12] Decrease quantity");
        System.out.println(" [13] Generate random products");
        System.out.println(" [14] Export products to TXT");
        System.out.println(" [15] Show most expensive product");
        System.out.println(" [16] Show cheapest product");
        System.out.println(" [17] Count total warehouse value");
        System.out.println(" [18] Login system");
        System.out.println(" [19] Admin panel");
        System.out.println(" [20] Clear database");
        System.out.println(" [0] Exit");
        printSeparator();
        System.out.print("  Your choice: ");
    }

    // ── Manage Products Menu ──────────────────────────────

    public static void printManageMenu() {
        printHeader("MANAGE PRODUCTS");
        System.out.println("  [1]  Add a new product");
        System.out.println("  [2]  Delete a product");
        printSeparator();
        System.out.print("  Your choice: ");
    }

    // ── Product Table ─────────────────────────────────────

    public static void printTableHeader() {
        printSeparator();
        System.out.printf("  %-6s %-25s %-10s %-10s%n", "ID", "Name", "Quantity", "Price");
        printSeparator();
    }

    public static void printTableRow(int id, String name, int quantity, double price) {
        System.out.printf("  %-6d %-25s %-10d %-10.2f%n", id, name, quantity, price);
    }

    public static void printTableRow(int id, String name, int quantity) {
        System.out.printf("  %-6d %-25s %-10d%n", id, name, quantity);
    }

    // ── Status Messages ───────────────────────────────────

    public static void printSuccess(String msg) {
        System.out.println("\n  ✔  " + msg);
    }

    public static void printError(String msg) {
        System.out.println("\n  ✘  " + msg);
    }

    public static void printInfo(String msg) {
        System.out.println("  ℹ  " + msg);
    }

    // ── Input Prompts ─────────────────────────────────────

    public static void prompt(String msg) {
        System.out.print("  » " + msg + ": ");
    }

    public static void pause(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

