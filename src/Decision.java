import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

public class Decision {

    static final String DB_URL = "jdbc:sqlite:D:\\Pulpit\\Intelligent Warehouse\\Intelligent Warehouse.db";

    public static void main(int decide) throws SQLException, IOException {
        Scanner input = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            GetID.idArray = GetID.pobierzId(conn).stream().mapToInt(Integer::intValue).toArray();
            int[] idList = GetID.idArray;
            PrintHelper.printInfo("IDs in database: " + Arrays.toString(idList));

            switch (decide) {
                case 1:
                    changeProductName(input, conn, idList);
                    break;
                case 2:
                    changeProductPrice(input, conn, idList);
                    break;
                case 3:
                    printWarehouseStatus(conn);
                    break;
                case 4:

                    break;
                case 5:

                    break;
                case 6:

                    break;
                case 7:

                    break;
                case 8:

                    break;

                case 9:
                    sortProductsByPrice(input, conn);
                    break;
                case 10:
                    showProductsByQuantity(input, conn);
                    break;
                case 11:
                    increaseQuantity(input, conn);
                    break;
                case 12:
                    decreaseQuantity(input, conn);
                    break;
                case 14:
                    exportProducts(input, conn);
                    break;
                case 15:
                    printMostExpensive(conn);
                    break;
                case 16:
                    printCheapest(conn);
                    break;
                case 17:
                    printWarehouseValue(conn);
                    break;
                case 20:
                    clearDatabase(input, conn);
                    break;
                default:
                    break;
            }
        }
    }

    private static void changeProductName(Scanner input, Connection conn, int[] idList) throws SQLException {
        PrintHelper.prompt("Enter the product ID");
        int targetId = input.nextInt();
        boolean foundId = false;

        for (int id : idList) {
            if (targetId != id) {
                continue;
            }
            foundId = true;
            PrintHelper.prompt("Enter the new product name");
            String newName = input.next();

            try (PreparedStatement selectStmt = conn.prepareStatement("SELECT 1 FROM AGD WHERE Id = ?")) {
                selectStmt.setInt(1, id);
                try (ResultSet rs = selectStmt.executeQuery()) {
                    if (rs.next()) {
                        try (PreparedStatement updateStmt = conn.prepareStatement("UPDATE AGD SET Name = ? WHERE Id = ?")) {
                            updateStmt.setString(1, newName);
                            updateStmt.setInt(2, id);
                            if (updateStmt.executeUpdate() > 0) {
                                PrintHelper.printSuccess("Product name updated successfully.");
                                PrintHelper.pause(2000);
                            } else {
                                PrintHelper.printError("Update failed.");
                            }
                        }
                    } else {
                        PrintHelper.printError("Product with ID " + id + " not found.");
                    }
                }
            }
            break;
        }

        if (!foundId) {
            PrintHelper.printError("Product with ID " + targetId + " not found.");
        }
    }

    private static void changeProductPrice(Scanner input, Connection conn, int[] idList) throws SQLException {
        PrintHelper.prompt("Enter the product ID");
        int targetId = input.nextInt();
        boolean foundId = false;

        for (int id : idList) {
            if (targetId != id) {
                continue;
            }
            foundId = true;
            PrintHelper.prompt("Enter the new product price");
            double newPrice = input.nextDouble();

            try (PreparedStatement selectStmt = conn.prepareStatement("SELECT 1 FROM AGD WHERE Id = ?")) {
                selectStmt.setInt(1, id);
                try (ResultSet rs = selectStmt.executeQuery()) {
                    if (rs.next()) {
                        try (PreparedStatement updateStmt = conn.prepareStatement("UPDATE AGD SET Price = ? WHERE Id = ?")) {
                            updateStmt.setDouble(1, newPrice);
                            updateStmt.setInt(2, id);
                            if (updateStmt.executeUpdate() > 0) {
                                PrintHelper.printSuccess("Product price updated successfully.");
                                PrintHelper.pause(2000);
                            } else {
                                PrintHelper.printError("Update failed.");
                            }
                        }
                    } else {
                        PrintHelper.printError("Product with ID " + id + " not found.");
                    }
                }
            }
            break;
        }

        if (!foundId) {
            PrintHelper.printError("Product with ID " + targetId + " not found.");
        }
    }

    private static void printWarehouseStatus(Connection conn) throws SQLException {
        PrintHelper.printHeader("WAREHOUSE STATUS");
        PrintHelper.printTableHeader();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM AGD")) {
            while (rs.next()) {
                PrintHelper.printTableRow(
                        rs.getInt("Id"),
                        rs.getString("Name"),
                        rs.getInt("quantity")
                );
            }
        }
        PrintHelper.printSeparator();
        PrintHelper.pause(2000);
    }

    private static void sortProductsByPrice(Scanner input, Connection conn) throws SQLException {
        System.out.println("Enter the name of the table: ");
        String tableName = readRequiredLine(input).toUpperCase(Locale.ROOT);
        if (!tableName.equals("AGD") && !tableName.equals("PRODUCTS")) {
            PrintHelper.printError("Invalid table name.");
            return;
        }

        System.out.println(" [1]   Ascending");
        System.out.println(" [2]   Descending");
        int option = input.nextInt();
        String query;
        if (option == 1) {
            query = "SELECT * FROM " + tableName + " ORDER BY PRICE ASC";
        } else if (option == 2) {
            query = "SELECT * FROM " + tableName + " ORDER BY PRICE DESC";
        } else {
            PrintHelper.printError("Invalid option.");
            return;
        }

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            boolean found = false;
            while (rs.next()) {
                found = true;
                int id = rs.getInt("Id");
                String name = rs.getString("Name");
                int quantity = rs.getInt("quantity");
                double price = rs.getDouble("PRICE");
                System.out.println(id + " | " + name + " | " + quantity + " | " + price);
            }

            if (found) {
                PrintHelper.printSuccess("Sorting completed.");
                PrintHelper.pause(2000);
            } else {
                System.out.println("No products found.");
            }
        }
    }

    private static void showProductsByQuantity(Scanner input, Connection conn) throws SQLException {
        System.out.println(" [1] Min Quantity");
        System.out.println(" [2] Max Quantity");
        int option = input.nextInt();

        System.out.println("Enter quantity value: ");
        int quantityValue = input.nextInt();

        String sql;
        if (option == 1) {
            sql = "SELECT * FROM AGD WHERE QUANTITY > ?";
        } else if (option == 2) {
            sql = "SELECT * FROM AGD WHERE QUANTITY < ?";
        } else {
            PrintHelper.printError("Invalid option.");
            return;
        }

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, quantityValue);
            try (ResultSet rs = pstmt.executeQuery()) {
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    System.out.println("ID: " + rs.getInt("Id"));
                    System.out.println("Name: " + rs.getString("NAME"));
                    System.out.println("Quantity: " + rs.getInt("QUANTITY"));
                    System.out.println("Price: " + rs.getDouble("PRICE"));
                    System.out.println("-------------------");
                    PrintHelper.pause(2000);
                }
                System.out.println(found ? "Query finished." : "No products found.");
            }
        }
    }

    private static void increaseQuantity(Scanner input, Connection conn) throws SQLException {
        PrintHelper.printInfo("Increasing Quantity");
        PrintHelper.prompt("Enter the product ID");
        int targetId = input.nextInt();
        PrintHelper.prompt("Enter the product quantity");
        int increaseBy = input.nextInt();

        if (targetId < 1) {
            PrintHelper.printError("Invalid product ID.");
            return;
        }
        if (increaseBy < 1) {
            PrintHelper.printError("Quantity must be greater than 0.");
            return;
        }
        if (!productExists(conn, targetId)) {
            PrintHelper.printError("Product with ID " + targetId + " not found.");
            return;
        }

        String updateSql = "UPDATE AGD SET QUANTITY = QUANTITY + ? WHERE Id = ?";
        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
            updateStmt.setInt(1, increaseBy);
            updateStmt.setInt(2, targetId);
            if (updateStmt.executeUpdate() > 0) {
                printCurrentQuantity(conn, targetId);
            } else {
                PrintHelper.printError("Update failed.");
            }
        }
    }

    private static void decreaseQuantity(Scanner input, Connection conn) throws SQLException {
        PrintHelper.printInfo("Decreasing Quantity");
        PrintHelper.prompt("Enter the product ID");
        int targetId = input.nextInt();
        PrintHelper.prompt("Enter the product quantity");
        int decreaseBy = input.nextInt();

        if (targetId < 1) {
            PrintHelper.printError("Invalid product ID.");
            return;
        }
        if (decreaseBy < 1) {
            PrintHelper.printError("Quantity must be greater than 0.");
            return;
        }
        if (!productExists(conn, targetId)) {
            PrintHelper.printError("Product with ID " + targetId + " not found.");
            return;
        }

        String updateSql = "UPDATE AGD SET QUANTITY = QUANTITY - ? WHERE Id = ? AND QUANTITY >= ?";
        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
            updateStmt.setInt(1, decreaseBy);
            updateStmt.setInt(2, targetId);
            updateStmt.setInt(3, decreaseBy);
            if (updateStmt.executeUpdate() > 0) {
                printCurrentQuantity(conn, targetId);
            } else {
                PrintHelper.printError("Update failed. Not enough stock to decrease.");
            }
        }
    }

    private static void exportProducts(Scanner input, Connection conn) throws SQLException {
        System.out.println("Enter the export path: ");
        PrintHelper.printSeparator();
        System.out.println("EXAMPLE PATH: C:/Users/YourFiles/Desktop/NewFile");
        PrintHelper.printSeparator();
        String exportPath = readRequiredLine(input);

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM AGD");
             FileWriter fw = new FileWriter(exportPath);
             BufferedWriter bw = new BufferedWriter(fw)) {

            bw.write("ID |  NAME |  QUANTITY |  PRICE");
            bw.newLine();
            bw.write("-".repeat(59));
            bw.newLine();

            while (rs.next()) {
                String line = rs.getInt("Id") + " | " +
                        rs.getString("Name") + " | " +
                        rs.getInt("Quantity") + " | " +
                        rs.getDouble("Price");
                bw.write(line);
                bw.newLine();
            }

            PrintHelper.printSuccess("Export successful to " + exportPath);
            PrintHelper.pause(2000);
        } catch (IOException e) {
            PrintHelper.printError("Export failed: " + e.getMessage());
        }
    }

    private static void printMostExpensive(Connection conn) throws SQLException {
        PrintHelper.printInfo("Printing the most expensive product");
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM AGD WHERE PRICE = (SELECT MAX(PRICE) FROM AGD)")) {
            if (rs.next()) {
                PrintHelper.printHeader("MOST EXPENSIVE PRODUCT");
                PrintHelper.printTableHeader();
                PrintHelper.printTableRow(
                        rs.getInt("Id"),
                        rs.getString("Name"),
                        rs.getInt("Quantity"),
                        rs.getDouble("Price")
                );
                PrintHelper.printSeparator();
                PrintHelper.pause(2000);
            }
        }
    }

    private static void printCheapest(Connection conn) throws SQLException {
        PrintHelper.printInfo("Printing the cheapest product");
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM AGD WHERE PRICE = (SELECT MIN(PRICE) FROM AGD)")) {
            if (rs.next()) {
                PrintHelper.printHeader("CHEAPEST PRODUCT");
                PrintHelper.printTableHeader();
                PrintHelper.printTableRow(
                        rs.getInt("Id"),
                        rs.getString("Name"),
                        rs.getInt("Quantity"),
                        rs.getDouble("Price")
                );
                PrintHelper.printSeparator();
                PrintHelper.pause(2000);
            }
        }
    }

    private static void printWarehouseValue(Connection conn) throws SQLException {
        PrintHelper.printSeparator();
        PrintHelper.printInfo("Warehouse Value:");
        String sql = "SELECT SUM(PRICE * QUANTITY) AS total_value FROM AGD";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                double totalValue = rs.getDouble("total_value");
                PrintHelper.printSuccess("Warehouse Total Value: " + totalValue);
                PrintHelper.pause(2000);
            }
        }
    }

    private static void clearDatabase(Scanner input, Connection conn) throws SQLException {
        System.out.println("Are you sure you want to clear Database?");
        System.out.println("YES/NO");
        String option = readRequiredLine(input).toUpperCase(Locale.ROOT);

        if (!option.equals("YES")) {
            PrintHelper.printInfo("Clear database canceled.");
            return;
        }

        try (Statement stmt = conn.createStatement()) {
            int rowsDeleted = stmt.executeUpdate("DELETE FROM AGD");
            PrintHelper.printSuccess("Deleting completed. Rows removed: " + rowsDeleted);
            PrintHelper.pause(2000);
        }
    }

    private static boolean productExists(Connection conn, int targetId) throws SQLException {
        String existSql = "SELECT 1 FROM AGD WHERE Id = ?";
        try (PreparedStatement existsStmt = conn.prepareStatement(existSql)) {
            existsStmt.setInt(1, targetId);
            try (ResultSet rs = existsStmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    private static void printCurrentQuantity(Connection conn, int targetId) throws SQLException {
        String getQtySql = "SELECT QUANTITY FROM AGD WHERE Id = ?";
        try (PreparedStatement getQtyStmt = conn.prepareStatement(getQtySql)) {
            getQtyStmt.setInt(1, targetId);
            try (ResultSet rs = getQtyStmt.executeQuery()) {
                if (rs.next()) {
                    int qty = rs.getInt("QUANTITY");
                    PrintHelper.printSuccess("Quantity updated. New quantity: " + qty);
                }
            }
        }
    }

    private static String readRequiredLine(Scanner input) {
        String value = input.nextLine().trim();
        while (value.isEmpty()) {
            value = input.nextLine().trim();
        }
        return value;
    }
}
