import java.awt.*;
import java.awt.event.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class DigitalClockApp extends Frame {
    
    // GUI Components
    private Label titleLabel;
    private Panel clockPanel;
    private Label[] timeZoneLabels;
    private Label[] timeLabels;
    private Button refreshButton, exitButton;
    
    // Time zones
    private String[] timeZones = {
        "America/New_York",      // EST/EDT
        "Europe/London",         // GMT/BST
        "Europe/Paris",          // CET/CEST
        "Asia/Tokyo",            // JST
        "Asia/Dubai",            // GST
        "Asia/Kolkata",          // IST
        "Australia/Sydney",      // AEDT/AEST
        "Pacific/Auckland"       // NZDT/NZST
    };
    
    private String[] displayNames = {
        "New York (EST/EDT)",
        "London (GMT/BST)",
        "Paris (CET/CEST)",
        "Tokyo (JST)",
        "Dubai (GST)",
        "India (IST)",
        "Sydney (AEDT/AEST)",
        "Auckland (NZDT/NZST)"
    };
    
    private Timer updateTimer;
    
    public DigitalClockApp() {
        setTitle("Digital Clock - Multiple Time Zones");
        setSize(600, 550);
        setBackground(new Color(30, 30, 30));
        setLayout(new BorderLayout(10, 10));
        
        // Create title panel
        Panel titlePanel = createTitlePanel();
        
        // Create clock panel
        clockPanel = createClockPanel();
        
        // Create button panel
        Panel buttonPanel = createButtonPanel();
        
        // Add components to frame
        add(titlePanel, BorderLayout.NORTH);
        add(clockPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Window listener
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (updateTimer != null) {
                    updateTimer.cancel();
                }
                System.exit(0);
            }
        });
        
        // Start timer to update time every second
        startClockUpdate();
    }
    
    private Panel createTitlePanel() {
        Panel panel = new Panel();
        panel.setBackground(new Color(50, 50, 50));
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        titleLabel = new Label("WORLD TIME ZONES");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 200, 100));
        
        panel.add(titleLabel);
        return panel;
    }
    
    private Panel createClockPanel() {
        Panel panel = new Panel();
        panel.setBackground(new Color(30, 30, 30));
        panel.setLayout(new GridLayout(timeZones.length, 2, 15, 10));
        
        timeZoneLabels = new Label[timeZones.length];
        timeLabels = new Label[timeZones.length];
        
        for (int i = 0; i < timeZones.length; i++) {
            // Time zone name label
            timeZoneLabels[i] = new Label(displayNames[i]);
            timeZoneLabels[i].setFont(new Font("Arial", Font.BOLD, 14));
            timeZoneLabels[i].setForeground(new Color(100, 200, 255));
            timeZoneLabels[i].setAlignment(Label.LEFT);
            
            // Time label
            timeLabels[i] = new Label("--:--:--");
            timeLabels[i].setFont(new Font("Courier New", Font.BOLD, 18));
            timeLabels[i].setForeground(new Color(0, 255, 100));
            timeLabels[i].setAlignment(Label.CENTER);
            
            panel.add(timeZoneLabels[i]);
            panel.add(timeLabels[i]);
        }
        
        return panel;
    }
    
    private Panel createButtonPanel() {
        Panel panel = new Panel();
        panel.setBackground(new Color(50, 50, 50));
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        refreshButton = new Button("Refresh");
        refreshButton.setFont(new Font("Arial", Font.BOLD, 12));
        refreshButton.setBackground(new Color(0, 150, 200));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateAllTimes();
            }
        });
        
        exitButton = new Button("Exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 12));
        exitButton.setBackground(new Color(200, 50, 50));
        exitButton.setForeground(Color.WHITE);
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (updateTimer != null) {
                    updateTimer.cancel();
                }
                System.exit(0);
            }
        });
        
        panel.add(refreshButton);
        panel.add(exitButton);
        
        return panel;
    }
    
    private void startClockUpdate() {
        updateAllTimes();
        
        // Create and schedule timer to update every 1000 milliseconds (1 second)
        updateTimer = new Timer();
        updateTimer.schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                updateAllTimes();
            }
        }, 1000, 1000);
    }
    
    private void updateAllTimes() {
        ZonedDateTime now = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        
        for (int i = 0; i < timeZones.length; i++) {
            try {
                ZoneId zoneId = ZoneId.of(timeZones[i]);
                ZonedDateTime zonedTime = now.withZoneSameInstant(zoneId);
                String formattedTime = zonedTime.format(formatter);
                timeLabels[i].setText(formattedTime);
            } catch (Exception ex) {
                timeLabels[i].setText("ERROR");
            }
        }
    }
    
    public static void main(String[] args) {
        DigitalClockApp app = new DigitalClockApp();
        app.setVisible(true);
    }
}
