package irrigation.ui;

import irrigation.adapter.LegacyPumpAdapter;
import irrigation.adapter.LegacyPumpSystem;
import irrigation.bridge.EmergencyIrrigation;
import irrigation.bridge.IrrigationMode;
import irrigation.bridge.ScheduledIrrigation;
import irrigation.source.CityWaterSource;
import irrigation.source.RainTankWaterSource;
import irrigation.source.WaterSource;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SmartIrrigationDemo extends JFrame {

    // ===== UI FIELDS =====
    private JTextField areaField;
    private JComboBox<String> modeCombo;
    private JComboBox<String> sourceCombo;
    private JTextArea outputArea;
    private DefaultListModel<String> historyModel;

    // domain objects
    private WaterSource cityWater;
    private RainTankWaterSource rainTank;
    private WaterSource legacyPump;

    public SmartIrrigationDemo() {
        setTitle("Riego inteligente - Bridge & Adapter");
        setSize(650, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.add(new JLabel("Área (m²):"));
        areaField = new JTextField();
        inputPanel.add(areaField);

        inputPanel.add(new JLabel("Modo de riego:"));
        modeCombo = new JComboBox<>(new String[]{"Programado", "Emergencia"});
        inputPanel.add(modeCombo);

        inputPanel.add(new JLabel("Fuente de agua:"));
        sourceCombo = new JComboBox<>(new String[]{"Agua de la ciudad", "Tanque de lluvia", "Bomba antigua"});
        inputPanel.add(sourceCombo);

        JButton irrigateButton = new JButton("Regar");
        irrigateButton.addActionListener(this::onIrrigate);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(outputArea);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(irrigateButton, BorderLayout.NORTH);
        centerPanel.add(scroll, BorderLayout.CENTER);

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // historial
        historyModel = new DefaultListModel<>();
        JList<String> historyList = new JList<>(historyModel);
        JScrollPane historyScroll = new JScrollPane(historyList);

        JPanel historyPanel = new JPanel(new BorderLayout(10, 10));
        historyPanel.add(new JLabel("Historial de riegos:"), BorderLayout.NORTH);
        historyPanel.add(historyScroll, BorderLayout.CENTER);

        // pestañas
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Riego", mainPanel);
        tabbedPane.addTab("Historial", historyPanel);

        add(tabbedPane, BorderLayout.CENTER);

        // initialize domain objects
        cityWater = new CityWaterSource();
        rainTank = new RainTankWaterSource(500); // 500 liters available
        legacyPump = new LegacyPumpAdapter(new LegacyPumpSystem());
    }

    private void onIrrigate(ActionEvent e) {
        try {
            String areaText = areaField.getText().trim();
            if (areaText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor ingrese un área en m².");
                return;
            }
            double area = Double.parseDouble(areaText);

            String mode = (String) modeCombo.getSelectedItem();
            String source = (String) sourceCombo.getSelectedItem();

            WaterSource selectedSource;
            switch (source) {
                case "Agua de la ciudad":
                    selectedSource = cityWater;
                    break;
                case "Tanque de lluvia":
                    selectedSource = rainTank;
                    break;
                case "Bomba antigua":
                    selectedSource = legacyPump;
                    break;
                default:
                    throw new IllegalArgumentException("Fuente de agua desconocida: " + source);
            }

            IrrigationMode irrigationMode;
            if ("Programado".equals(mode)) {
                irrigationMode = new ScheduledIrrigation(selectedSource, 5); // 5 L/m²
            } else {
                irrigationMode = new EmergencyIrrigation(selectedSource);
            }

            // redirect "console" messages to the text area
            java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream();
            java.io.PrintStream ps = new java.io.PrintStream(buffer);
            java.io.PrintStream oldOut = System.out;
            System.setOut(ps);
            try {
                irrigationMode.irrigate(area);
            } finally {
                System.setOut(oldOut);
            }
            String consoleText = buffer.toString();
            outputArea.setText(consoleText);

            // si hubo error no se agrega al historial
            if (consoleText.startsWith("Error")) {
                JOptionPane.showMessageDialog(this, consoleText, "Error de riego", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String resumen = "Modo: " + mode + " | Fuente: " + source + " | Área: " + area + " m²";
            historyModel.addElement(resumen);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El área debe ser un valor numérico.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error inesperado: " + ex.getMessage());
        }
    }

    // ===== MAIN =====
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SmartIrrigationDemo().setVisible(true));
    }
}
