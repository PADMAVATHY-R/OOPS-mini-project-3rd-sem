import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.util.*;

public class DiaryServlet extends HttpServlet {
    private DiaryService diaryService = new DiaryService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try {
            List<DiaryEntry> entries = diaryService.getAllEntries();
            PrintWriter out = response.getWriter();
            out.println("[");
            for (int i = 0; i < entries.size(); i++) {
                DiaryEntry entry = entries.get(i);
                out.printf("{\"id\":%d,\"title\":\"%s\",\"content\":\"%s\",\"createdAt\":\"%s\"}",
                    entry.getId(), entry.getTitle(), entry.getContent(), entry.getCreatedAt());
                if (i < entries.size() - 1) out.println(",");
            }
            out.println("]");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String title = request.getParameter("title");
            String content = request.getParameter("content");
            diaryService.addEntry(title, content);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }
}

@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");

    try {
        String idParam = request.getParameter("id");
        PrintWriter out = response.getWriter();

        if (idParam != null) {
            // Fetch a single entry by ID
            int id = Integer.parseInt(idParam);
            DiaryEntry entry = diaryService.getEntryById(id);

            if (entry != null) {
                out.printf(
                    "{\"id\":%d,\"title\":\"%s\",\"content\":\"%s\",\"createdAt\":\"%s\"}",
                    entry.getId(), entry.getTitle(), entry.getContent(), entry.getCreatedAt()
                );
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\":\"Entry not found\"}");
            }
        } else {
            // Fetch all entries (existing functionality)
            List<DiaryEntry> entries = diaryService.getAllEntries();
            out.println("[");
            for (int i = 0; i < entries.size(); i++) {
                DiaryEntry entry = entries.get(i);
                out.printf("{\"id\":%d,\"title\":\"%s\",\"content\":\"%s\",\"createdAt\":\"%s\"}",
                    entry.getId(), entry.getTitle(), entry.getContent(), entry.getCreatedAt());
                if (i < entries.size() - 1) out.println(",");
            }
            out.println("]");
        }
    } catch (Exception e) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        e.printStackTrace();
    }
}
