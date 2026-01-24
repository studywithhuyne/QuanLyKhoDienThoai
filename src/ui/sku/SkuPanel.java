package ui.sku;

import javax.swing.*;
import java.util.List;
import java.text.NumberFormat;
import java.util.Locale;

import dao.SkuDAO;
import dto.SkuDTO;
import ui.BaseCrudPanel;

/**
 * Panel quản lý SKU (Stock Keeping Unit)
 */
public class SkuPanel extends BaseCrudPanel {
    
    private static final String[] COLUMNS = {"ID", "Mã SKU", "Sản phẩm", "Thuộc tính", "Giá", "Tồn kho"};
    
    public SkuPanel(JFrame parentFrame) {
        super(parentFrame, "SKU", COLUMNS);
    }
    
    @Override
    public void loadData() {
        SkuDAO skuDAO = new SkuDAO();
        List<SkuDTO> skus = skuDAO.GetAllSku();
        tableModel.setRowCount(0);
        NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        for (SkuDTO sku : skus) {
            tableModel.addRow(new Object[]{
                sku.getID(), 
                sku.getCode(),
                sku.getProductName(),
                sku.getAttributes(),
                currencyFormat.format(sku.getPrice()) + "₫",
                sku.getStock()
            });
        }
    }
    
    @Override
    protected void setupColumnWidths() {
        setFixedColumnWidth(0, 60);   // ID
        setFlexibleColumnWidth(1, 120, 180); // Mã SKU
        setFlexibleColumnWidth(2, 150, 250); // Sản phẩm
        setFlexibleColumnWidth(3, 150, 300); // Thuộc tính
        setFlexibleColumnWidth(4, 100, 150); // Giá
        setFixedColumnWidth(5, 80);   // Tồn kho
    }
    
    @Override
    protected void onAddAction() {
        new SkuAddDialog(parentFrame, this);
    }
    
    @Override
    protected void onEditAction(int modelRow) {
        int id = (int) getValueAt(modelRow, 0);
        String code = (String) getValueAt(modelRow, 1);
        new SkuEditDialog(parentFrame, id, code, this);
    }
    
    @Override
    protected void onDeleteAction(int modelRow) {
        int id = (int) getValueAt(modelRow, 0);
        String code = (String) getValueAt(modelRow, 1);
        new SkuDeleteDialog(parentFrame, id, code, this);
    }
}
