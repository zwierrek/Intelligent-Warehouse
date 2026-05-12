import java.sql.*;
import java.util.Scanner;

public class MageProducts {

    private static final String DB_URL = "jdbc:sqlite:Test-DB";

    public static void main(int decide) throws SQLException {
        Scanner input = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PrintHelper.printInfo("Database connected.");

            PrintHelper.printManageMenu();
            int action = input.nextInt();

            if (action == 1) {
                PrintHelper.prompt("Product name");
                String newName = input.next();

                PrintHelper.prompt("Product price");
                double newPrice = input.nextDouble();

                PrintHelper.prompt("Quantity");
                int newQuantity = input.nextInt();

                try (Statement stmt = conn.createStatement()) {
                    ResultSet rs = stmt.executeQuery("SELECT MAX(Id) FROM AGD");
                    int newId = rs.next() ? rs.getInt(1) + 1 : 1;
                    rs.close();

                    try (PreparedStatement insertStmt = conn.prepareStatement(
                            "INSERT INTO AGD (Id, NAME, QUANTITY, Price) VALUES (?, ?, ?, ?)")) {

                        insertStmt.setInt(1, newId);
                        insertStmt.setString(2, newName);
                        insertStmt.setInt(3, newQuantity);
                        insertStmt.setDouble(4, newPrice);

                        if (insertStmt.executeUpdate() > 0) {
                            PrintHelper.printSuccess("Product \"" + newName + "\" added successfully (Id: " + newId + ").");
                        } else {
                            PrintHelper.printError("Failed to add product.");
                        }
                    }
                }
            } else if (action == 2) {
                PrintHelper.prompt("Product ID to delete");
                int targetId = input.nextInt();

                if (targetId < 1) {
                    PrintHelper.printError("Invalid product ID.");
                    return;
                }

                String productName = null;
                try (PreparedStatement selectStmt = conn.prepareStatement(
                        "SELECT Name FROM AGD WHERE Id = ?")) {
                    selectStmt.setInt(1, targetId);
                    try (ResultSet rs = selectStmt.executeQuery()) {
                        if (rs.next()) {
                            productName = rs.getString("Name");
                        }
                    }
                }

                if (productName == null) {
                    PrintHelper.printError("Product with ID " + targetId + " not found.");
                    return;
                }

                try (PreparedStatement deleteStmt = conn.prepareStatement(
                        "DELETE FROM AGD WHERE Id = ?")) {
                    deleteStmt.setInt(1, targetId);

                    if (deleteStmt.executeUpdate() > 0) {
                        PrintHelper.printSuccess("Product \"" + productName + "\" deleted successfully.");
                    } else {
                        PrintHelper.printError("Failed to delete product.");
                    }
                }
            } else {
                PrintHelper.printError("Invalid option.");
            }
        }
    }
}
