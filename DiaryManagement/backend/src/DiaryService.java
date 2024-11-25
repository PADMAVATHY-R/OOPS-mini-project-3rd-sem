import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiaryService {
    public List<DiaryEntry> getAllEntries() throws Exception {
        List<DiaryEntry> entries = new ArrayList<>();
        Connection connection = DatabaseConnection.getConnection();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM diary_entries");

        while (rs.next()) {
            entries.add(new DiaryEntry(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("content"),
                rs.getString("created_at")
            ));
        }
        return entries;
    }

    public void addEntry(String title, String content) throws Exception {
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement pstmt = connection.prepareStatement(
            "INSERT INTO diary_entries (title, content) VALUES (?, ?)"
        );
        pstmt.setString(1, title);
        pstmt.setString(2, content);
        pstmt.executeUpdate();
    }
}

public DiaryEntry getEntryById(int id) throws Exception {
    Connection connection = DatabaseConnection.getConnection();
    PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM diary_entries WHERE id = ?");
    pstmt.setInt(1, id);
    ResultSet rs = pstmt.executeQuery();

    if (rs.next()) {
        return new DiaryEntry(
            rs.getInt("id"),
            rs.getString("title"),
            rs.getString("content"),
            rs.getString("created_at")
        );
    }
    return null; // No entry found with the given ID
}
