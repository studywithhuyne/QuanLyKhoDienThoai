package ui.product;

import javax.swing.*;
import javax.swing.border.*;

import dao.ProductDAO;
import dto.ProductDTO;

import java.awt.*;
import java.awt.event.*;
import static utils.ColorUtil.*;

public class ProductDeleteDialog extends JDialog {
    
    // Product data
    private int productId;
    private String productName;
    private boolean confirmed = false;
    
    // Buttons
    private JButton btnDelete;
    private JButton btnCancel;
    
    private ProductPanel productPanel;
    
    public ProductDeleteDialog(Frame parent, int id, String name, ProductPanel productPanel) {
        super(parent, "Xác nhận xóa", true);
        this.productId = id;
        this.productName = name;
        this.productPanel = productPanel;
        
        initializeDialog();
        createComponents();
        setVisible(true);
    }
    
    private void initializeDialog() {
        setSize(460, 380);
        setLocationRelativeTo(getParent());
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(DIALOG_BG);
    }
    
    private void createComponents() {
        JPanel contentPanel = createContent();
        add(contentPanel, BorderLayout.CENTER);
        
        JPanel footerPanel = createFooter();
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createContent() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(DIALOG_BG);
        wrapper.setBorder(new EmptyBorder(15, 30, 10, 30));
        
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(30, 30, 30, 30)
        ));
        
        card.add(Box.createVerticalGlue());
        
        // Warning icon với background tròn
        JPanel iconPanel = new JPanel();
        iconPanel.setBackground(DANGER_BG);
        iconPanel.setPreferredSize(new Dimension(70, 70));
        iconPanel.setMaximumSize(new Dimension(70, 70));
        iconPanel.setMinimumSize(new Dimension(70, 70));
        iconPanel.setLayout(new GridBagLayout());
        iconPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel iconLabel = new JLabel("⚠");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconLabel.setForeground(DANGER_RED);
        iconPanel.add(iconLabel);
        
        // Title
        JLabel titleLabel = new JLabel("Xóa sản phẩm?");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Message
        JLabel messageLabel = new JLabel("<html><center>Bạn có chắc chắn muốn xóa sản phẩm<br><b>\"" + productName + "\"</b>?</center></html>");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageLabel.setForeground(TEXT_SECONDARY_DARK);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(iconPanel);
        card.add(Box.createVerticalStrut(20));
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(12));
        card.add(messageLabel);
        
        card.add(Box.createVerticalGlue());
        
        wrapper.add(card, BorderLayout.CENTER);
        
        return wrapper;
    }
    
    private JPanel createFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        footer.setBackground(DIALOG_BG);
        footer.setBorder(new EmptyBorder(0, 30, 30, 30));
        
        btnCancel = createButton("Hủy bỏ", TEXT_SECONDARY_DARK, CARD_BG, true);
        btnCancel.addActionListener(e -> {
            confirmed = false;
            dispose();
        });
        
        btnDelete = createButton("Xóa sản phẩm", Color.WHITE, DANGER_RED, false);
        btnDelete.addActionListener(e -> deleteProduct());
        
        footer.add(btnCancel);
        footer.add(btnDelete);
        
        return footer;
    }
    
    private JButton createButton(String text, Color textColor, Color bgColor, boolean isOutline) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(textColor);
        button.setBackground(bgColor);
        button.setPreferredSize(new Dimension(isOutline ? 110 : 150, 44));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        
        if (isOutline) {
            button.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        } else {
            button.setBorder(new EmptyBorder(8, 16, 8, 16));
            button.setBorderPainted(false);
        }
        
        // Hover effect
        Color hoverColor = isOutline ? CONTENT_BG : DANGER_HOVER;
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private void deleteProduct() {
        confirmed = true;
        
        ProductDTO deleteProduct = new ProductDTO();
        deleteProduct.setId(this.productId); 
        ProductDAO dao = new ProductDAO();
        boolean isSuccess = dao.DeleteProduct(deleteProduct);
        if (isSuccess) {
        	JOptionPane.showMessageDialog(this, "Xóa sản phẩm thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        	if (productPanel != null) {
        	    productPanel.loadData();
        	}
        	dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Xóa sản phẩm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
    
    public int getProductId() {
        return productId;
    }
}
