What This Swing App Does:
Collects order details using input fields.

Accepts comma-separated product IDs.

On clicking Place Order, it:

Creates an Order object.
Creates Product entries.
Shows confirmation.


code- 
// Merge all the code into your MarketplaceApp.java

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MarketplaceApp extends JFrame {
    CardLayout cardLayout;
    JPanel mainPanel;
    ArrayList<Product> cart = new ArrayList<>();
    HashMap<String, ArrayList<Product>> categoryProducts = new HashMap<>();
    OrderFormPanel orderFormPanel;

    public MarketplaceApp() {
        setTitle("Marketplace");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        populateProducts();

        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(homePage(), "Home");
        mainPanel.add(cartPage(), "Cart");

        orderFormPanel = new OrderFormPanel();
        mainPanel.add(orderFormPanel, "OrderForm");

        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    // -- Your populateProducts, createTopPanel, homePage, showProductsForCategory code remains SAME --

    private JPanel cartPanel;
    private JTable cartTable;
    private DefaultTableModel cartModel;

    private JPanel cartPage() {
        cartPanel = new JPanel(new BorderLayout());
        cartPanel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Your Cart", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        cartPanel.add(title, BorderLayout.NORTH);

        String[] columns = {"Product", "Price"};
        cartModel = new DefaultTableModel(columns, 0);
        cartTable = new JTable(cartModel);
        cartTable.setRowHeight(40);
        cartTable.setFont(new Font("Arial", Font.PLAIN, 18));
        cartTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));

        JScrollPane scrollPane = new JScrollPane(cartTable);
        cartPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.setBackground(Color.WHITE);

        JButton back = new JButton("Back to Shopping");
        back.setFont(new Font("Arial", Font.BOLD, 18));
        back.setBackground(new Color(52, 152, 219));
        back.setForeground(Color.white);
        back.setFocusPainted(false);
        back.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        back.addActionListener(e -> cardLayout.show(mainPanel, "Home"));

        JButton checkout = new JButton("Checkout");
        checkout.setFont(new Font("Arial", Font.BOLD, 18));
        checkout.setBackground(new Color(46, 204, 113));
        checkout.setForeground(Color.white);
        checkout.setFocusPainted(false);
        checkout.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        checkout.addActionListener(e -> {
            if (cart.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Your cart is empty!");
            } else {
                orderFormPanel.prefillProducts(cart);
                cardLayout.show(mainPanel, "OrderForm");
            }
        });

        bottom.add(back);
        bottom.add(checkout);

        cartPanel.add(bottom, BorderLayout.SOUTH);

        return cartPanel;
    }

    private void refreshCartPage() {
        cartModel.setRowCount(0);
        for (Product p : cart) {
            cartModel.addRow(new Object[]{p.name, p.price});
        }
    }

    class Product {
        String name, price;

        Product(String name, String price) {
            this.name = name;
            this.price = price;
        }
    }

    // -- ButtonRenderer and ButtonEditor Classes same --

    // 🆕🆕 -- Here's your Order Form integrated as an inner class
    class OrderFormPanel extends JPanel {
        private JTextField nameField, addressField, zipField, cityField, countryField, stripeField;
        private JTextField totalField;
        private JTextArea productArea;

        public OrderFormPanel() {
            setLayout(new GridLayout(10, 1));
            setBackground(Color.WHITE);

            stripeField = new JTextField();
            nameField = new JTextField();
            addressField = new JTextField();
            zipField = new JTextField();
            cityField = new JTextField();
            countryField = new JTextField();
            totalField = new JTextField();
            productArea = new JTextArea(3, 20);

            add(new JLabel("Stripe ID:"));
            add(stripeField);
            add(new JLabel("Name:"));
            add(nameField);
            add(new JLabel("Address:"));
            add(addressField);
            add(new JLabel("Zipcode:"));
            add(zipField);
            add(new JLabel("City:"));
            add(cityField);
            add(new JLabel("Country:"));
            add(countryField);
            add(new JLabel("Total (₹):"));
            add(totalField);
            add(new JLabel("Product Names (comma separated):"));
            add(new JScrollPane(productArea));

            JButton placeOrderBtn = new JButton("Place Order");
            add(placeOrderBtn);

            placeOrderBtn.addActionListener(e -> placeOrder());
        }

        public void prefillProducts(ArrayList<Product> cart) {
            double total = 0;
            StringBuilder sb = new StringBuilder();
            for (Product p : cart) {
                sb.append(p.name).append(",");
                total += parsePrice(p.price);
            }
            productArea.setText(sb.toString());
            totalField.setText(String.format("%.2f", total));
        }

        private double parsePrice(String price) {
            return Double.parseDouble(price.replace("₹", "").trim());
        }

        private void placeOrder() {
            String userId = "user123";
            String stripeId = stripeField.getText();
            String name = nameField.getText();
            String addr = addressField.getText();
            String zip = zipField.getText();
            String city = this.cityField.getText();
            String country = this.countryField.getText();
            double total = Double.parseDouble(totalField.getText());

            String[] productNames = productArea.getText().split(",");
            List<Product> products = new ArrayList<>();
            for (String pname : productNames) {
                products.add(new Product(pname.trim(), "₹0")); // dummy price, can be improved
            }

            // Normally save order to database here

            JOptionPane.showMessageDialog(this, "✅ Order placed for " + name + " with " + products.size() + " items!");

            cart.clear();
            refreshCartPage();
            cardLayout.show(mainPanel, "Home");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MarketplaceApp::new);
    }
}
