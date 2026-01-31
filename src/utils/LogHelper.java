package utils;

import dao.LogDAO;
import dto.AccountDTO;

/**
 * Utility class để ghi log các hoạt động trong hệ thống
 */
public class LogHelper {
    
    private static final LogDAO logDAO = new LogDAO();
    
    // Các action
    public static final String ACTION_LOGIN = "Đăng nhập";
    public static final String ACTION_LOGOUT = "Đăng xuất";
    public static final String ACTION_ADD = "Thêm";
    public static final String ACTION_EDIT = "Sửa";
    public static final String ACTION_DELETE = "Xóa";
    public static final String ACTION_IMPORT = "Tạo phiếu nhập";
    public static final String ACTION_SALE = "Xuất kho";
    
    /**
     * Ghi log với user hiện tại từ SessionManager
     */
    public static void log(String action, String details) {
        AccountDTO currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null) {
            logDAO.AddLog(currentUser.getID(), action, details);
        }
    }
    
    /**
     * Ghi log với userId cụ thể
     */
    public static void log(int userId, String action, String details) {
        logDAO.AddLog(userId, action, details);
    }
    
    /**
     * Ghi log đăng nhập
     */
    public static void logLogin(int userId) {
        logDAO.AddLog(userId, ACTION_LOGIN, "Đăng nhập thành công");
    }
    
    /**
     * Ghi log đăng xuất
     */
    public static void logLogout() {
        log(ACTION_LOGOUT, "Đăng xuất thành công");
    }
    
    /**
     * Ghi log thêm mới
     */
    public static void logAdd(String entityType, String entityName) {
        log(ACTION_ADD + " " + entityType, "Thêm " + entityType + ": " + entityName);
    }
    
    /**
     * Ghi log chỉnh sửa
     */
    public static void logEdit(String entityType, String entityName) {
        log(ACTION_EDIT + " " + entityType, "Cập nhật " + entityType + ": " + entityName);
    }
    
    /**
     * Ghi log xóa
     */
    public static void logDelete(String entityType, String entityName) {
        log(ACTION_DELETE + " " + entityType, "Xóa " + entityType + ": " + entityName);
    }
    
    /**
     * Ghi log tạo phiếu nhập
     */
    public static void logImport(int receiptId, String supplierName) {
        log(ACTION_IMPORT, "Phiếu nhập #" + receiptId + " - " + supplierName);
    }
    
    /**
     * Ghi log xuất kho
     */
    public static void logSale(int invoiceId, String details) {
        log(ACTION_SALE, "Phiếu xuất #" + invoiceId + " - " + details);
    }
}
