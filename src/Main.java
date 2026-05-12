import java.io.IOException;
import java.sql.*;
import java.util.*;

public class Main {

    private static final String DB_URL = "jdbc:sqlite:D:\\Pulpit\\Intelligent Warehouse\\Intelligent Warehouse.db";

    public static void main(String[] args) throws SQLException, IOException {
        Scanner input = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection(DB_URL)) {

            if (conn == null) return;

            // ── Startup info ──────────────────────────────────────

            PrintHelper.printHeader("WAREHOUSE MANAGER — STARTUP");
            PrintHelper.printInfo("Connected to: " + DB_URL);

            Statement stmt = conn.createStatement();
            ResultSet rs   = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table'");
            while (rs.next()) {
                PrintHelper.printInfo("Table found: " + rs.getString("name"));
            }
            rs.close();

            // ── Main menu loop ────────────────────────────────────

            PrintHelper.printMainMenu();
            int decide = input.nextInt();

            while ( decide != 0) {

                if (decide == 4) {
                    MageProducts.main(decide);
                } else if (decide == 1 || decide == 2 || decide == 3 || decide == 5 || decide == 6 || decide == 7 || decide == 8 || decide == 9 || decide == 10 || decide == 11 || decide == 12 || decide == 13 || decide == 14 || decide == 15
                        || decide == 16 || decide == 17 || decide == 18 || decide == 19 || decide == 20) {
                    Decision.main(decide);
                }

                PrintHelper.printMainMenu();
                decide = input.nextInt();
            }

            // ── Final warehouse summary on exit ───────────────────

            PrintHelper.printHeader("WAREHOUSE STATUS — FINAL SUMMARY");
            PrintHelper.printTableHeader();

            stmt = conn.createStatement();
            rs   = stmt.executeQuery("SELECT * FROM AGD");
            while (rs.next()) {
                PrintHelper.printTableRow(
                        rs.getInt("ID"),
                        rs.getString("Name"),
                        rs.getInt("quantity"),
                        rs.getDouble("Price")
                );
            }
            PrintHelper.printSeparator();

            input.close();
            PrintHelper.printSuccess("Disconnected from database. Goodbye!");
            System.exit(0);

        } catch (SQLException e) {
            PrintHelper.printError("SQL error: " + e.getMessage());
        }
    }
}
