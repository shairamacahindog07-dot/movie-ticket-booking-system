import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

public class MovieTicketBookingSystem extends Frame {
    
    // GUI Components
    private Label customerNameLabel, movieLabel, formatLabel, ticketsLabel, addOnsLabel;
    private TextField customerNameField, ticketsField;
    private Choice movieChoice, formatChoice;
    private Checkbox popcornCheckbox, softDrinkCheckbox;
    private Button calculateButton, clearButton, exitButton;
    private TextArea receiptArea;
    
    // Constants
    private static final double TICKET_PRICE = 12.50;
    private static final double POPCORN_PRICE = 5.00;
    private static final double SOFT_DRINK_PRICE = 3.50;
    
    public MovieTicketBookingSystem() {
        setTitle("Movie Ticket Booking System");
        setSize(600, 700);
        setBackground(new Color(240, 240, 240));
        setLayout(new BorderLayout(10, 10));
        
        // Create panels
        Panel formPanel = createFormPanel();
        Panel buttonPanel = createButtonPanel();
        receiptArea = new TextArea(10, 40);
        receiptArea.setEditable(false);
        receiptArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        
        // Add components to frame
        add(formPanel, BorderLayout.NORTH);
        add(receiptArea, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Window listener for close button
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
    
    private Panel createFormPanel() {
        Panel panel = new Panel();
        panel.setLayout(new GridLayout(6, 2, 10, 10));
        panel.setBackground(new Color(220, 220, 220));
        
        // Customer Name
        customerNameLabel = new Label("Customer Name:");
        customerNameField = new TextField(20);
        panel.add(customerNameLabel);
        panel.add(customerNameField);
        
        // Movie Selection
        movieLabel = new Label("Select Movie:");
        movieChoice = new Choice();
        movieChoice.addItem("Avengers");
        movieChoice.addItem("Doomsday");
        movieChoice.addItem("Moana 2");
        movieChoice.addItem("Mr. Fan Boy");
        panel.add(movieLabel);
        panel.add(movieChoice);
        
        // Movie Format
        formatLabel = new Label("Movie Format:");
        formatChoice = new Choice();
        formatChoice.addItem("Regular");
        formatChoice.addItem("Director's Club");
        formatChoice.addItem("IMAX");
        panel.add(formatLabel);
        panel.add(formatChoice);
        
        // Number of Tickets
        ticketsLabel = new Label("Number of Tickets:");
        ticketsField = new TextField(5);
        ticketsField.setText("1");
        panel.add(ticketsLabel);
        panel.add(ticketsField);
        
        // Add-ons
        addOnsLabel = new Label("Add-ons:");
        Panel addOnsPanel = new Panel();
        addOnsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        popcornCheckbox = new Checkbox("Popcorn ($5.00)");
        softDrinkCheckbox = new Checkbox("Soft Drink ($3.50)");
        addOnsPanel.add(popcornCheckbox);
        addOnsPanel.add(softDrinkCheckbox);
        panel.add(addOnsLabel);
        panel.add(addOnsPanel);
        
        return panel;
    }
    
    private Panel createButtonPanel() {
        Panel panel = new Panel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(new Color(200, 200, 200));
        
        calculateButton = new Button("Calculate");
        clearButton = new Button("Clear");
        exitButton = new Button("Exit");
        
        calculateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calculateAndDisplayReceipt();
            }
        });
        
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });
        
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        panel.add(calculateButton);
        panel.add(clearButton);
        panel.add(exitButton);
        
        return panel;
    }
    
    private void calculateAndDisplayReceipt() {
        try {
            // Validate inputs
            String customerName = customerNameField.getText().trim();
            if (customerName.isEmpty()) {
                receiptArea.setText("Error: Please enter customer name.");
                return;
            }
            
            int numberOfTickets = Integer.parseInt(ticketsField.getText().trim());
            if (numberOfTickets <= 0) {
                receiptArea.setText("Error: Number of tickets must be greater than 0.");
                return;
            }
            
            // Get selections
            String movie = movieChoice.getSelectedItem();
            String format = formatChoice.getSelectedItem();
            boolean hasPopcorn = popcornCheckbox.getState();
            boolean hasSoftDrink = softDrinkCheckbox.getState();
            
            // Calculate costs
            double ticketCost = TICKET_PRICE * numberOfTickets;
            double addonsCost = 0;
            if (hasPopcorn) addonsCost += POPCORN_PRICE;
            if (hasSoftDrink) addonsCost += SOFT_DRINK_PRICE;
            
            double totalCost = ticketCost + addonsCost;
            
            // Format receipt
            DecimalFormat df = new DecimalFormat("#.##");
            StringBuilder receipt = new StringBuilder();
            receipt.append("====================================\n");
            receipt.append("     MOVIE TICKET BOOKING RECEIPT\n");
            receipt.append("====================================\n\n");
            receipt.append("Customer Name: ").append(customerName).append("\n");
            receipt.append("Movie: ").append(movie).append("\n");
            receipt.append("Format: ").append(format).append("\n");
            receipt.append("Number of Tickets: ").append(numberOfTickets).append("\n\n");
            receipt.append("------------------------------------\n");
            receipt.append("Ticket Cost (").append(numberOfTickets).append(" x $").append(df.format(TICKET_PRICE)).append("): $").append(df.format(ticketCost)).append("\n");
            
            if (hasPopcorn) {
                receipt.append("Popcorn: $").append(df.format(POPCORN_PRICE)).append("\n");
            }
            if (hasSoftDrink) {
                receipt.append("Soft Drink: $").append(df.format(SOFT_DRINK_PRICE)).append("\n");
            }
            
            receipt.append("------------------------------------\n");
            receipt.append("TOTAL AMOUNT: $").append(df.format(totalCost)).append("\n");
            receipt.append("====================================\n");
            receipt.append("Thank you for your booking!\n");
            receipt.append("====================================\n");
            
            receiptArea.setText(receipt.toString());
            
        } catch (NumberFormatException ex) {
            receiptArea.setText("Error: Please enter a valid number for tickets.");
        }
    }
    
    private void clearForm() {
        customerNameField.setText("");
        ticketsField.setText("1");
        movieChoice.select(0);
        formatChoice.select(0);
        popcornCheckbox.setState(false);
        softDrinkCheckbox.setState(false);
        receiptArea.setText("");
    }
    
    public static void main(String[] args) {
        MovieTicketBookingSystem frame = new MovieTicketBookingSystem();
        frame.setVisible(true);
    }
}
