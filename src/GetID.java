import java.sql.*;
import java.util.*;

public class GetID {

    static final String DB_URL = "jdbc:sqlite:Test-DB";

    public static int[] idArray;

    // ── Initializes the ID array on program start ─────────

    public static void GetIds(String[] args) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            idArray = pobierzId(conn)
                    .stream()
                    .mapToInt(Integer::intValue)
                    .toArray();
        } catch (SQLException e) {
            System.err.println("Connection error: " + e.getMessage());
        }
    }

    // ── Fetches all IDs from the Products table ───────────

    static List<Integer> pobierzId(Connection conn) throws SQLException {
        List<Integer> lista = new ArrayList<>();
        String sql = "SELECT Id FROM AGD";

        try (Statement stmt = conn.createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(rs.getInt("Id"));
            }
        }
        return lista;
    }
}
