package ui.import_;

import static utils.ColorUtil.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dao.ImportReceiptDAO;
import dao.SupplierDAO;
import dao.SkuDAO;
import dto.ImportReceiptDTO;
import dto.SupplierDTO;
import dto.SkuDTO;
import dto.AccountDTO;
import utils.SessionManager;
import utils.LogHelper;

public class ImportAddDialog extends JDialog {
    
    // Header fields
    private JComboBox<SupplierDTO> cmbSupplier;
    private JTextField txtEmployee;
    private JTextField txtDate;
    
    // Detail table
    private JTable detailTable;
    private DefaultTableModel detailTableModel;
    private List<ImportDetailRow> detailRows = new ArrayList<>();
    
    // Searchable SKU combo
    private JComboBox<SkuDTO> cmbSku;
    private JTextField txtSkuSearch;
    private List<SkuDTO> allSkus = new ArrayList<>();
    private JSpinner spnQuantity;
    
    // Total
    private JLabel lblTotal;
    
    private JButton btnSave;
    private JButton btnCancel;
    
    private ImportPanel importPanel;
    private NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));

    // Inner class for detail row
    private static class ImportDetailRow {
        int skuId;
        int productId;
        String productName;
        String skuCode;
        double price;
        int quantity;
        
        double getTotal() {
            return price * quantity;
        }
    }

    public ImportAddDialog(Frame parent, ImportPanel importPanel) {
        super(parent, "Thêm phiếu nhập kho", true);
        this.importPanel = importPanel;
        initializeDialog();
        createComponents();
        loadComboBoxData();
        setVisible(true);
    }
    
    private void initializeDialog() {
        setSize(950, 750);
        setLocationRelativeTo(getParent());
        setResizable(true);
        setLayout(new BorderLayout());
        getContentPane().setBackground(DIALOG_BG);
    }

    private void createComponents() {
        add(createHeader(), BorderLayout.NORTH);
        add(createContent(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(CARD_BG);
        header.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, BORDER_COLOR),
            new EmptyBorder(20, 25, 20, 25)
        ));
        
        JLabel titleLabel = new JLabel("Thêm phiếu nhập kho");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_PRIMARY);
        
        header.add(titleLabel, BorderLayout.WEST);
        return header;
    }
    
    private JPanel createContent() {
        JPanel contentWrapper = new JPanel(new BorderLayout(0, 15));
        contentWrapper.setBackground(DIALOG_BG);
        contentWrapper.setBorder(new EmptyBorder(20, 25, 10, 25));
        
        // Top: Info panel
        contentWrapper.add(createInfoPanel(), BorderLayout.NORTH);
        
        // Center: Detail panel
        contentWrapper.add(createDetailPanel(), BorderLayout.CENTER);
        
        return contentWrapper;
    }
    
    private JPanel createInfoPanel() {
        JPanel infoCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.dispose();
            }
        };
        infoCard.setLayout(new GridBagLayout());
        infoCard.setOpaque(false);
        infoCard.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Nhà cung cấp
        gbc.gridx = 0; gbc.gridy = 0;
        infoCard.add(createLabel("Nhà cung cấp:"), gbc);
        gbc.gridx = 1;
        cmbSupplier = new JComboBox<>();
        cmbSupplier.setPreferredSize(new Dimension(250, 35));
        cmbSupplier.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbSupplier.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof SupplierDTO) {
                    setText(((SupplierDTO) value).getName());
                }
                return this;
            }
        });
        infoCard.add(cmbSupplier, gbc);
        
        // Nhân viên
        gbc.gridx = 2;
        infoCard.add(createLabel("Nhân viên:"), gbc);
        gbc.gridx = 3;
        txtEmployee = new JTextField();
        txtEmployee.setPreferredSize(new Dimension(200, 35));
        txtEmployee.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtEmployee.setEditable(false);
        txtEmployee.setBackground(CONTENT_BG);
        infoCard.add(txtEmployee, gbc);
        
        // Ngày tạo
        gbc.gridx = 0; gbc.gridy = 1;
        infoCard.add(createLabel("Ngày tạo:"), gbc);
        gbc.gridx = 1;
        txtDate = new JTextField(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
        txtDate.setPreferredSize(new Dimension(250, 35));
        txtDate.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDate.setEditable(false);
        txtDate.setBackground(CONTENT_BG);
        infoCard.add(txtDate, gbc);
        
        return infoCard;
    }
    
    private JPanel createDetailPanel() {
        JPanel detailPanel = new JPanel(new BorderLayout(0, 10));
        detailPanel.setOpaque(false);
        
        // Add detail row
        JPanel addRowPanel = createAddRowPanel();
        detailPanel.add(addRowPanel, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"STT", "Sản phẩm", "Mã SKU", "Đơn giá", "Số lượng", "Thành tiền", ""};
        detailTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Only delete column is "editable"
            }
        };
        detailTable = new JTable(detailTableModel);
        setupTable();
        
        JScrollPane scrollPane = new JScrollPane(detailTable);
        scrollPane.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        scrollPane.getViewport().setBackground(CARD_BG);
        detailPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Total panel
        JPanel totalPanel = createTotalPanel();
        detailPanel.add(totalPanel, BorderLayout.SOUTH);
        
        return detailPanel;
    }
    
    private JPanel createAddRowPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.dispose();
            }
        };
        panel.setLayout(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Row 1: Search SKU
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(createLabel("Tìm SKU:"), gbc);
        
        gbc.gridx = 1;
        txtSkuSearch = new JTextField();
        txtSkuSearch.setPreferredSize(new Dimension(300, 35));
        txtSkuSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSkuSearch.putClientProperty("JTextField.placeholderText", "Nhập tên sản phẩm hoặc mã SKU...");
        txtSkuSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { filterSkuList(); }
            @Override
            public void removeUpdate(DocumentEvent e) { filterSkuList(); }
            @Override
            public void changedUpdate(DocumentEvent e) { filterSkuList(); }
        });
        gbc.gridwidth = 2;
        panel.add(txtSkuSearch, gbc);
        
        // Row 2: SKU selection
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(createLabel("Chọn SKU:"), gbc);
        
        gbc.gridx = 1;
        cmbSku = new JComboBox<>();
        cmbSku.setPreferredSize(new Dimension(350, 35));
        cmbSku.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbSku.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof SkuDTO) {
                    SkuDTO sku = (SkuDTO) value;
                    String displayText = sku.getProductName() + " - " + sku.getCode();
                    if (sku.getAttributes() != null && !sku.getAttributes().isEmpty()) {
                        displayText += " (" + sku.getAttributes() + ")";
                    }
                    setText(displayText);
                }
                return this;
            }
        });
        gbc.gridwidth = 2;
        panel.add(cmbSku, gbc);
        
        // Row 3: Quantity and Add button
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridwidth = 1;
        panel.add(createLabel("Số lượng:"), gbc);
        
        gbc.gridx = 1;
        JPanel quantityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        quantityPanel.setOpaque(false);
        
        spnQuantity = new JSpinner(new SpinnerNumberModel(1, 1, 9999, 1));
        spnQuantity.setPreferredSize(new Dimension(100, 35));
        spnQuantity.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        quantityPanel.add(spnQuantity);
        
        JButton btnAdd = new JButton("+ Thêm vào phiếu");
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setBackground(GREEN);
        btnAdd.setPreferredSize(new Dimension(140, 35));
        btnAdd.setFocusPainted(false);
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.addActionListener(e -> addDetailRow());
        quantityPanel.add(btnAdd);
        
        panel.add(quantityPanel, gbc);
        
        return panel;
    }
    
    private void filterSkuList() {
        String searchText = txtSkuSearch.getText().toLowerCase().trim();
        cmbSku.removeAllItems();
        
        for (SkuDTO sku : allSkus) {
            String productName = sku.getProductName() != null ? sku.getProductName().toLowerCase() : "";
            String skuCode = sku.getCode() != null ? sku.getCode().toLowerCase() : "";
            String attributes = sku.getAttributes() != null ? sku.getAttributes().toLowerCase() : "";
            
            if (searchText.isEmpty() || 
                productName.contains(searchText) || 
                skuCode.contains(searchText) ||
                attributes.contains(searchText)) {
                cmbSku.addItem(sku);
            }
        }
    }
    
    private JPanel createTotalPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        panel.setOpaque(false);
        
        JLabel label = new JLabel("TỔNG CỘNG:");
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(TEXT_PRIMARY);
        panel.add(label);
        
        lblTotal = new JLabel("0₫");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTotal.setForeground(DARK_BLUE);
        panel.add(lblTotal);
        
        return panel;
    }
    
    private void setupTable() {
        detailTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        detailTable.setRowHeight(40);
        detailTable.setShowGrid(false);
        detailTable.setIntercellSpacing(new Dimension(0, 0));
        detailTable.setBackground(CARD_BG);
        detailTable.setSelectionBackground(SELECTION_BG);
        
        detailTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        detailTable.getTableHeader().setBackground(CARD_BG);
        detailTable.getTableHeader().setForeground(TEXT_PRIMARY);
        detailTable.getTableHeader().setPreferredSize(new Dimension(0, 40));
        
        // Column widths
        detailTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // STT
        detailTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Sản phẩm
        detailTable.getColumnModel().getColumn(2).setPreferredWidth(130); // Mã SKU
        detailTable.getColumnModel().getColumn(3).setPreferredWidth(120); // Đơn giá
        detailTable.getColumnModel().getColumn(4).setPreferredWidth(80);  // Số lượng
        detailTable.getColumnModel().getColumn(5).setPreferredWidth(130); // Thành tiền
        detailTable.getColumnModel().getColumn(6).setPreferredWidth(70);  // Delete
        
        // Delete button column
        detailTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        detailTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor());
    }
    
    private void addDetailRow() {
        SkuDTO selectedSku = (SkuDTO) cmbSku.getSelectedItem();
        if (selectedSku == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn SKU!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int quantity = (int) spnQuantity.getValue();
        
        // Check if SKU already exists in table
        for (ImportDetailRow row : detailRows) {
            if (row.skuId == selectedSku.getID()) {
                row.quantity += quantity;
                refreshTable();
                return;
            }
        }
        
        // Add new row
        ImportDetailRow row = new ImportDetailRow();
        row.skuId = selectedSku.getID();
        row.productId = selectedSku.getProductId();
        row.productName = selectedSku.getProductName();
        row.skuCode = selectedSku.getCode();
        row.price = selectedSku.getPrice();
        row.quantity = quantity;
        
        detailRows.add(row);
        refreshTable();
        
        // Reset quantity
        spnQuantity.setValue(1);
    }
    
    private void refreshTable() {
        detailTableModel.setRowCount(0);
        int stt = 1;
        double total = 0;
        
        for (ImportDetailRow row : detailRows) {
            detailTableModel.addRow(new Object[]{
                stt++,
                row.productName,
                row.skuCode,
                currencyFormat.format(row.price) + "₫",
                row.quantity,
                currencyFormat.format(row.getTotal()) + "₫",
                "Xóa"
            });
            total += row.getTotal();
        }
        
        lblTotal.setText(currencyFormat.format(total) + "₫");
    }
    
    private void removeDetailRow(int index) {
        if (index >= 0 && index < detailRows.size()) {
            detailRows.remove(index);
            refreshTable();
        }
    }
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(TEXT_PRIMARY);
        return label;
    }
    
    private void loadComboBoxData() {
        // Load suppliers
        SupplierDAO supplierDAO = new SupplierDAO();
        List<SupplierDTO> suppliers = supplierDAO.GetAllSupplier();
        for (SupplierDTO supplier : suppliers) {
            cmbSupplier.addItem(supplier);
        }
        
        // Load SKUs
        SkuDAO skuDAO = new SkuDAO();
        allSkus = skuDAO.GetAllSku();
        filterSkuList();
        
        // Set current employee
        AccountDTO currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null) {
            txtEmployee.setText(currentUser.getFullname());
        }
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        footer.setBackground(DIALOG_BG);
        footer.setBorder(new EmptyBorder(15, 25, 20, 25));
        
        btnCancel = createButton("Hủy bỏ", TEXT_SECONDARY_DARK, CARD_BG, true);
        btnCancel.addActionListener(e -> dispose());
        
        btnSave = createButton("Lưu phiếu nhập", Color.WHITE, DARK_BLUE, false);
        btnSave.addActionListener(e -> saveImport());
        
        footer.add(btnCancel);
        footer.add(btnSave);
        
        return footer;
    }
    
    private JButton createButton(String text, Color textColor, Color bgColor, boolean isOutline) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (isOutline) {
                    g2.setColor(getModel().isRollover() ? CONTENT_BG : bgColor);
                    g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                    g2.setColor(BORDER_COLOR);
                    g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 10, 10));
                } else {
                    g2.setColor(getModel().isRollover() ? PRIMARY_HOVER : bgColor);
                    g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                }
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(textColor);
        button.setPreferredSize(new Dimension(isOutline ? 100 : 160, 42));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }

    private void saveImport() {
        SupplierDTO selectedSupplier = (SupplierDTO) cmbSupplier.getSelectedItem();
        AccountDTO currentUser = SessionManager.getInstance().getCurrentUser();
        
        if (selectedSupplier == null) {
            showError("Vui lòng chọn nhà cung cấp!");
            return;
        }
        
        if (currentUser == null) {
            showError("Không tìm thấy thông tin nhân viên!");
            return;
        }
        
        if (detailRows.isEmpty()) {
            showError("Vui lòng thêm ít nhất một sản phẩm!");
            return;
        }
        
        // Calculate total
        double totalAmount = 0;
        for (ImportDetailRow row : detailRows) {
            totalAmount += row.getTotal();
        }
        
        // Create receipt
        ImportReceiptDTO receipt = new ImportReceiptDTO();
        receipt.setSupplierId(selectedSupplier.getID());
        receipt.setStaffId(currentUser.getID());
        receipt.setTotalAmount(totalAmount);
        
        ImportReceiptDAO importDAO = new ImportReceiptDAO();
        int newId = importDAO.AddImportReceipt(receipt);
        
        if (newId > 0) {
            // Add details and update stock
            boolean allSuccess = true;
            for (ImportDetailRow row : detailRows) {
                boolean detailAdded = importDAO.AddImportDetail(newId, row.productId, row.skuId, row.quantity);
                boolean stockUpdated = importDAO.UpdateSkuStock(row.skuId, row.quantity);
                if (!detailAdded || !stockUpdated) {
                    allSuccess = false;
                }
            }
            
            if (allSuccess) {
                LogHelper.logImport(newId, selectedSupplier.getName());
                JOptionPane.showMessageDialog(this, 
                    "Thêm phiếu nhập thành công!\nMã phiếu: #" + newId, 
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
                if (importPanel != null) {
                    importPanel.loadData();
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Phiếu nhập đã tạo nhưng có lỗi khi thêm chi tiết!", 
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                dispose();
            }
        } else {
            showError("Thêm phiếu nhập thất bại!");
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
    
    // Button renderer for delete column
    private class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setFont(new Font("Segoe UI", Font.PLAIN, 12));
            setForeground(DANGER_COLOR);
            setBackground(CARD_BG);
            setBorderPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText("Xóa");
            return this;
        }
    }
    
    // Button editor for delete column
    private class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private int currentRow;
        
        public ButtonEditor() {
            super(new JCheckBox());
            button = new JButton("Xóa");
            button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            button.setForeground(DANGER_COLOR);
            button.setBackground(CARD_BG);
            button.setBorderPainted(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.addActionListener(e -> {
                fireEditingStopped();
                removeDetailRow(currentRow);
            });
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            currentRow = row;
            return button;
        }
        
        @Override
        public Object getCellEditorValue() {
            return "Xóa";
        }
    }
}
