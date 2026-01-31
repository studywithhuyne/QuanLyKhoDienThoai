package bus;

import java.util.List;
import dao.InvoiceDAO;
import dto.InvoiceDTO;
import dto.InvoiceDetailDTO;

public class InvoiceBUS {
    
    private InvoiceDAO invoiceDAO;
    
    public InvoiceBUS() {
        this.invoiceDAO = new InvoiceDAO();
    }
    
    public List<InvoiceDTO> getAll() {
        return invoiceDAO.GetAllInvoice();
    }
    
    public InvoiceDTO getById(int id) {
        return invoiceDAO.GetInvoiceById(id);
    }
    
    public List<InvoiceDetailDTO> getDetails(int invoiceId) {
        return invoiceDAO.GetInvoiceDetails(invoiceId);
    }
    
    public int add(InvoiceDTO invoice) {
        if (!validateInvoice(invoice)) return -1;
        return invoiceDAO.AddInvoice(invoice);
    }
    
    public boolean addDetail(int invoiceId, int skuId, int quantity, Integer imeiId) {
        if (invoiceId <= 0 || skuId <= 0 || quantity <= 0) return false;
        return invoiceDAO.AddInvoiceDetail(invoiceId, skuId, quantity, imeiId);
    }

    public boolean decreaseSkuStock(int skuId, int quantityToDecrease) {
        if (skuId <= 0 || quantityToDecrease <= 0) return false;
        return invoiceDAO.DecreaseSkuStock(skuId, quantityToDecrease);
    }

    public boolean updateImeiStatus(int imeiId, String status) {
        if (imeiId <= 0 || status == null || status.trim().isEmpty()) return false;
        return invoiceDAO.UpdateImeiStatus(imeiId, status);
    }
    
    public boolean update(InvoiceDTO invoice) {
        if (invoice.getID() <= 0) return false;
        if (!validateInvoice(invoice)) return false;
        return invoiceDAO.EditInvoice(invoice);
    }
    
    public boolean delete(int id) {
        if (id <= 0) return false;
        return invoiceDAO.DeleteInvoice(id);
    }
    

    
    private boolean validateInvoice(InvoiceDTO invoice) {
        if (invoice == null) return false;
        if (invoice.getStaffId() <= 0) return false;
        if (invoice.getTotalAmount() < 0) return false;
        return true;
    }
    
    private boolean validateDetail(InvoiceDetailDTO detail) {
        if (detail == null) return false;
        if (detail.getInvoiceId() <= 0) return false;
        if (detail.getSkuId() <= 0) return false;
        if (detail.getQuantity() <= 0) return false;
        if (detail.getPrice() < 0) return false;
        return true;
    }
    
    public double calculateTotal(List<InvoiceDetailDTO> details) {
        if (details == null || details.isEmpty()) return 0;
        return details.stream().mapToDouble(d -> d.getPrice() * d.getQuantity()).sum();
    }
    
    public boolean updateTotalAmount(int invoiceId, double totalAmount) {
        if (invoiceId <= 0 || totalAmount < 0) return false;
        return invoiceDAO.UpdateTotalAmount(invoiceId, totalAmount);
    }
}
