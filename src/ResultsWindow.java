import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ResultsWindow extends JFrame {
    private JTextArea resultTextArea;

    /**
     * Konštruktor triedy pre vytvorenie grafického rozhriania pre výpis.
     */
    public ResultsWindow(double[] parameterVector, int[] resultVector, double resultCost) {
        setTitle("Results");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        getRootPane().setBorder(BorderFactory.createMatteBorder(10,10,10,10, Color.DARK_GRAY));
        setLocationRelativeTo(null); // Okno bude vycentrované

        // Vytvorenie panelu pre zobrazenie výsledkov
        JPanel panel = new JPanel(new BorderLayout());

        // Panel pre zobrazenie parametrov a ceny najlepšieho bodu
        JPanel infoPanel = new JPanel(new GridLayout(parameterVector.length + 1, 1));
        Border border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        infoPanel.setBorder(BorderFactory.createTitledBorder(border, "Parameters"));

        // Vytvoriť názvy parametrov a hodnoty parametrov
        String[] parameterNames = {"Population:", "Mutation Probability:", "Crossover Probability:", "Time Limit (seconds):"};

        // Názvy parametrov a hodnoty parametrov v panely
        for (int i = 0; i < parameterVector.length; i++) {
            JLabel parameterNameLabel = new JLabel(parameterNames[i]);
            JLabel parameterValueLabel = new JLabel(String.valueOf(parameterVector[i]));

            // Populáciu zaokrúhľujeme na celé čislo smerom nadol
            if (i == 0) {
                parameterValueLabel = new JLabel(String.valueOf((int)Math.floor(parameterVector[i])));
            } else {
                parameterValueLabel = new JLabel(String.valueOf(parameterVector[i]));
            }

            JPanel parameterPairPanel = new JPanel(new BorderLayout());
            parameterPairPanel.add(Box.createHorizontalGlue(), BorderLayout.WEST); // Zarovnanie na pravú stranu
            parameterPairPanel.add(parameterNameLabel, BorderLayout.CENTER);
            parameterPairPanel.add(parameterValueLabel, BorderLayout.EAST);

            JPanel wrapperPanel = new JPanel(new BorderLayout());
            wrapperPanel.add(parameterPairPanel, BorderLayout.CENTER);

            infoPanel.add(wrapperPanel);
        }

        // Pridanie Best Solution k parametrom
        JLabel bestPointCostLabel = new JLabel("Best Solution: ");
        JLabel bestPointCostValueLabel = new JLabel(String.valueOf(resultCost));

        JPanel bestPointCostPanel = new JPanel(new BorderLayout());
        bestPointCostPanel.add(bestPointCostLabel, BorderLayout.WEST);
        bestPointCostPanel.add(bestPointCostValueLabel, BorderLayout.EAST);

        JPanel bestPointCostWrapperPanel = new JPanel(new BorderLayout());
        bestPointCostWrapperPanel.add(bestPointCostPanel, BorderLayout.CENTER);

        infoPanel.add(bestPointCostWrapperPanel);

        // Nastavenie veľkosti písma pre nadpisy
        Font titleFont = new Font("Arial", Font.BOLD, 16);
        ((javax.swing.border.TitledBorder) infoPanel.getBorder()).setTitleFont(titleFont);

        // Panel pre zobrazenie výsledkov
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBorder(BorderFactory.createTitledBorder("Result Vector Indexes"));

        // Textové pole pre zobrazenie výsledkov
        resultTextArea = new JTextArea();
        resultTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultTextArea);

        // Pridanie textového poľa do panelu pre zobrazenie výsledkov
        resultPanel.add(scrollPane, BorderLayout.CENTER);

        // Tlačidlo Exit
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Tlačidlo Close
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        // Panel pre tlačidlá
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(exitButton);
        buttonPanel.add(closeButton);

        // Pridanie panelov do hlavného panelu
        panel.add(infoPanel, BorderLayout.NORTH);
        panel.add(resultPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Pridanie hlavného panelu do okna
        add(panel);

        // Výpis výsledkov
        printResultVector(resultVector);
    }

    /**
     * Metóda na výpis výsledkov.
     */
    public void printResultVector(int[] resultVector) {
        // Vytvorenie reťazca z výsledkov
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < resultVector.length; i++) {
            if (resultVector[i] == 1) {
                sb.append(i).append(" ");
            }
        }
        // Nastavenie textu v textovom poli
        resultTextArea.setText(sb.toString());
    }
}
