import java.awt.*;
import java.awt.event.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

public class WorldClockApp extends Frame {
    
    // GUI Components
    private Label titleLabel;
    private Label dateLabel;
    private Panel clockPanel;
    private Label[] timeZoneLabels;
    private Label[] timeLabels;
    private Label[] dateLabels;
    private Button refreshButton, exitButton, addButton, removeButton;
    private Choice timeZoneChoice;
    
    // Time zones list (can be modified)
    private java.util.List<String> activeTimeZones;
    private java.util.List<String> displayNames;
    
    private Timer updateTimer;
    
    // Available time zones
    private String[] allTimeZones = {
        "America/New_York",
        "America/Chicago",
        "America/Denver",
        "America/Los_Angeles",
        "Europe/London",
        "Europe/Paris",
        "Europe/Berlin",
        "Europe/Moscow",
        "Asia/Dubai",
        "Asia/Kolkata",
        "Asia/Bangkok",
        "Asia/Hong_Kong",
        "Asia/Tokyo",
        "Asia/Seoul",
        "Australia/Sydney",
        "Australia/Melbourne",
        "Pacific/Auckland",
        "Pacific/Fiji"
    };
    
    private String[] allDisplayNames = {
        "New York (EST/EDT)",
        "Chicago (CST/CDT)",
        "Denver (MST/MDT)",
        "Los Angeles (PST/PDT)",
        "London (GMT/BST)",
        "Paris (CET/CEST)",
        "Berlin (CET/CEST)",
        "Moscow (MSK)",
        "Dubai (GST)",
        "India (IST)",
        "Bangkok (ICT)",
        "Hong Kong (HKT)",
        "Tokyo (JST)",
        "Seoul (KST)",
        "Sydney (AEDT/AEST)",
        "Melbourne (AEDT/AEST)",
        "Auckland (NZDT/NZST)",
        "Fiji (FJT)"
    };
    
    public WorldClockApp() {
        setTitle("World Digital Clock");
        setSize(700, 700);
        setBackground(new Color(20, 20, 30));
        setLayout(new BorderLayout(10, 10));
        
        // Initialize time zones list
        activeTimeZones = new java.util.ArrayList<>();
        displayNames = new java.util.ArrayList<>();
        
        // Add default time zones
        addTimeZoneToList(0);  // New York
        addTimeZoneToList(4);  // London
        addTimeZoneToList(5);  // Paris
        addTimeZoneToList(12); // Tokyo
        addTimeZoneToList(9);  // India
        addTimeZoneToList(14); // Sydney
        
        // Create title panel
        Panel titlePanel = createTitlePanel();
        
        // Create clock display panel
        Panel displayPanel = createDisplayPanel();
        
        // Create control panel
        Panel controlPanel = createControlPanel();
        
        // Create button panel
        Panel buttonPanel = createButtonPanel();
        
        // Add components to frame
        add(titlePanel, BorderLayout.NORTH);
        add(displayPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.WEST);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Window listener
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                stopClock();
                System.exit(0);
            }
        });
        
        // Start timer to update time
        startClock();
    }
    
    private Panel createTitlePanel() {
        Panel panel = new Panel();
        panel.setBackground(new Color(40, 40, 60));
        panel.setLayout(new BorderLayout(10, 5));
        
        titleLabel = new Label("🌍 WORLD DIGITAL CLOCK 🌍");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(new Color(0, 255, 150));
        titleLabel.setAlignment(Label.CENTER);
        
        dateLabel = new Label("");
        dateLabel.setFont(new Font("Arial", Font.BOLD, 14));
        dateLabel.setForeground(new Color(100, 200, 255));
        dateLabel.setAlignment(Label.CENTER);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(dateLabel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private Panel createDisplayPanel() {
        Panel panel = new Panel();
        panel.setBackground(new Color(20, 20, 30));
        
        int size = activeTimeZones.size();
        panel.setLayout(new GridLayout(size, 3, 15, 12));
        
        timeZoneLabels = new Label[size];
        timeLabels = new Label[size];
        dateLabels = new Label[size];
        
        for (int i = 0; i < size; i++) {
            // Time zone name
            timeZoneLabels[i] = new Label(displayNames.get(i));
            timeZoneLabels[i].setFont(new Font("Arial", Font.BOLD, 13));
            timeZoneLabels[i].setForeground(new Color(100, 200, 255));
            timeZoneLabels[i].setAlignment(Label.LEFT);
            
            // Time display
            timeLabels[i] = new Label("--:--:--");
            timeLabels[i].setFont(new Font("Courier New", Font.BOLD, 22));
            timeLabels[i].setForeground(new Color(0, 255, 100));
            timeLabels[i].setAlignment(Label.CENTER);
            
            // Date display
            dateLabels[i] = new Label("");
            dateLabels[i].setFont(new Font("Arial", Font.PLAIN, 11));
            dateLabels[i].setForeground(new Color(150, 150, 200));
            dateLabels[i].setAlignment(Label.RIGHT);
            
            panel.add(timeZoneLabels[i]);
            panel.add(timeLabels[i]);
            panel.add(dateLabels[i]);
        }
        
        return panel;
    }
    
    private Panel createControlPanel() {
        Panel panel = new Panel();
        panel.setBackground(new Color(40, 40, 60));
        panel.setLayout(new BorderLayout(5, 5));
        
        Panel topPanel = new Panel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        topPanel.setBackground(new Color(40, 40, 60));
        
        Label addLabel = new Label("Add Timezone:");
        addLabel.setFont(new Font("Arial", Font.BOLD, 11));
        addLabel.setForeground(Color.WHITE);
        
        timeZoneChoice = new Choice();
        for (int i = 0; i < allTimeZones.length; i++) {
            timeZoneChoice.addItem(allDisplayNames[i]);
        }
        
        addButton = new Button("+ Add");
        addButton.setBackground(new Color(0, 150, 100));
        addButton.setForeground(Color.WHITE);
        addButton.setFont(new Font("Arial", Font.BOLD, 10));
        addButton.addActionListener(e -> addTimeZone());
        
        topPanel.add(addLabel);
        topPanel.add(timeZoneChoice);
        topPanel.add(addButton);
        
        Panel bottomPanel = new Panel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        bottomPanel.setBackground(new Color(40, 40, 60));
        
        removeButton = new Button("✕ Remove Last");
        removeButton.setBackground(new Color(200, 50, 50));
        removeButton.setForeground(Color.WHITE);
        removeButton.setFont(new Font("Arial", Font.BOLD, 10));
        removeButton.addActionListener(e -> removeTimeZone());
        
        bottomPanel.add(removeButton);
        
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private Panel createButtonPanel() {
        Panel panel = new Panel();
        panel.setBackground(new Color(40, 40, 60));
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        refreshButton = new Button("🔄 Refresh");
        refreshButton.setFont(new Font("Arial", Font.BOLD, 12));
        refreshButton.setBackground(new Color(0, 150, 200));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.addActionListener(e -> updateAllTimes());
        
        exitButton = new Button("❌ Exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 12));
        exitButton.setBackground(new Color(200, 50, 50));
        exitButton.setForeground(Color.WHITE);
        exitButton.addActionListener(e -> {
            stopClock();
            System.exit(0);
        });
        
        panel.add(refreshButton);
        panel.add(exitButton);
        
        return panel;
    }
    
    private void addTimeZoneToList(int index) {
        if (index >= 0 && index < allTimeZones.length) {
            activeTimeZones.add(allTimeZones[index]);
            displayNames.add(allDisplayNames[index]);
        }
    }
    
    private void addTimeZone() {
        int selectedIndex = timeZoneChoice.getSelectedIndex();
        if (selectedIndex >= 0) {
            String tz = allTimeZones[selectedIndex];
            String name = allDisplayNames[selectedIndex];
            
            // Check if not already added
            if (!activeTimeZones.contains(tz)) {
                activeTimeZones.add(tz);
                displayNames.add(name);
                rebuildClockPanel();
            }
        }
    }
    
    private void removeTimeZone() {
        if (activeTimeZones.size() > 1) {
            activeTimeZones.remove(activeTimeZones.size() - 1);
            displayNames.remove(displayNames.size() - 1);
            rebuildClockPanel();
        }
    }
    
    private void rebuildClockPanel() {
        remove(clockPanel);
        clockPanel = createDisplayPanel();
        add(clockPanel, BorderLayout.CENTER);
        clockPanel.revalidate();
        clockPanel.repaint();
        updateAllTimes();
    }
    
    private void startClock() {
        updateAllTimes();
        
        updateTimer = new Timer();
        updateTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateAllTimes();
            }
        }, 0, 1000);
    }
    
    private void stopClock() {
        if (updateTimer != null) {
            updateTimer.cancel();
        }
    }
    
    private void updateAllTimes() {
        ZonedDateTime now = ZonedDateTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEE, MMM dd");
        
        for (int i = 0; i < activeTimeZones.size(); i++) {
            try {
                ZoneId zoneId = ZoneId.of(activeTimeZones.get(i));
                ZonedDateTime zonedTime = now.withZoneSameInstant(zoneId);
                
                String formattedTime = zonedTime.format(timeFormatter);
                String formattedDate = zonedTime.format(dateFormatter);
                
                timeLabels[i].setText(formattedTime);
                dateLabels[i].setText(formattedDate);
            } catch (Exception ex) {
                timeLabels[i].setText("ERROR");
                dateLabels[i].setText("");
            }
        }
        
        // Update main date
        ZonedDateTime mainTime = now.withZoneSameInstant(ZoneId.systemDefault());
        dateLabel.setText(mainTime.format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy")));
    }
    
    public static void main(String[] args) {
        WorldClockApp app = new WorldClockApp();
        app.setVisible(true);
    }
}
