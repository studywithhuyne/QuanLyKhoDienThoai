package bus;

import java.util.List;
import dao.ImportReceiptDAO;
import dto.ImportReceiptDTO;
import dto.ImportDetailDTO;

public class ImportReceiptBUS {
    
    private ImportReceiptDAO importReceiptDAO;
    
    public ImportReceiptBUS() {
        this.importReceiptDAO = new ImportReceiptDAO();
    }
    
    public List<ImportReceiptDTO> getAll() {
        return importReceiptDAO.GetAllImportReceipt();
    }
    
    public ImportReceiptDTO getById(int id) {
        return importReceiptDAO.GetImportReceiptById(id);
    }
    
    public List<ImportDetailDTO> getDetails(int receiptId) {
        return importReceiptDAO.GetImportDetails(receiptId);
    }
    
    public int add(ImportReceiptDTO receipt) {
        if (!validateReceipt(receipt)) return -1;
        return importReceiptDAO.AddImportReceipt(receipt);
    }
    
    public boolean addDetail(int receiptId, int productId, int skuId, int quantity) {
        if (receiptId <= 0 || productId <= 0 || skuId <= 0 || quantity <= 0) return false;
        return importReceiptDAO.AddImportDetail(receiptId, productId, skuId, quantity);
    }
    
    public boolean update(ImportReceiptDTO receipt) {
        if (receipt.getID() <= 0) return false;
        if (!validateReceipt(receipt)) return false;
        return importReceiptDAO.EditImportReceipt(receipt);
    }
    
    public boolean delete(int id) {
        if (id <= 0) return false;
        return importReceiptDAO.DeleteImportReceipt(id);
    }
    

    
    private boolean validateReceipt(ImportReceiptDTO receipt) {
        if (receipt == null) return false;
        if (receipt.getSupplierId() <= 0) return false;
        if (receipt.getStaffId() <= 0) return false;
        if (receipt.getTotalAmount() < 0) return false;
        return true;
    }
    
    private boolean validateDetail(ImportDetailDTO detail) {
        if (detail == null) return false;
        if (detail.getImportReceiptId() <= 0) return false;
        if (detail.getSkuId() <= 0) return false;
        if (detail.getQuantity() <= 0) return false;
        if (detail.getPrice() < 0) return false;
        return true;
    }
    
    public double calculateTotal(List<ImportDetailDTO> details) {
        if (details == null || details.isEmpty()) return 0;
        return details.stream().mapToDouble(d -> d.getPrice() * d.getQuantity()).sum();
    }
    
    public boolean updateTotalAmount(int receiptId, double totalAmount) {
        if (receiptId <= 0 || totalAmount < 0) return false;
        return importReceiptDAO.UpdateTotalAmount(receiptId, totalAmount);
    }

    public boolean updateSkuStock(int skuId, int quantityToAdd) {
        if (skuId <= 0 || quantityToAdd == 0) return false;
        return importReceiptDAO.UpdateSkuStock(skuId, quantityToAdd);
    }
}
