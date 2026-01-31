package bus;

import java.util.List;
import dao.LogDAO;
import dto.LogDTO;

public class LogBUS {
    
    private LogDAO logDAO;
    
    public LogBUS() {
        this.logDAO = new LogDAO();
    }
    
    public List<LogDTO> getAll() {
        return logDAO.GetAllLogs();
    }
    
    public List<LogDTO> getRecentLogs(int limit) {
        if (limit <= 0) limit = 10;
        return logDAO.GetRecentLogs(limit);
    }
    
    public void logAdd(int accountId, String entityType, String entityName) {
        String details = entityType + ": " + entityName;
        logDAO.AddLog(accountId, "ADD", details);
    }
    
    public void logEdit(int accountId, String entityType, String entityName) {
        String details = entityType + ": " + entityName;
        logDAO.AddLog(accountId, "EDIT", details);
    }
    
    public void logDelete(int accountId, String entityType, String entityName) {
        String details = entityType + ": " + entityName;
        logDAO.AddLog(accountId, "DELETE", details);
    }
    
    public void logImport(int accountId, int receiptId, double amount) {
        String details = "Nhập kho #" + receiptId + " - " + String.format("%,.0f", amount) + "đ";
        logDAO.AddLog(accountId, "IMPORT", details);
    }
    
    public void logSale(int accountId, int invoiceId, double amount) {
        String details = "Xuất kho #" + invoiceId + " - " + String.format("%,.0f", amount) + "đ";
        logDAO.AddLog(accountId, "SALE", details);
    }
    
    public void logLogin(int accountId, String username) {
        String details = username + " đăng nhập";
        logDAO.AddLog(accountId, "LOGIN", details);
    }
    
    public void logLogout(int accountId, String username) {
        String details = username + " đăng xuất";
        logDAO.AddLog(accountId, "LOGOUT", details);
    }
}
