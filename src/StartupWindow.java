import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class StartupWindow extends JFrame implements ActionListener {
    private JTextField fileNameField, populationField, mutationField, crossoverField, timeLimitField;
    private JTextField maxIterationField, sField, bField, cField, mediansField;
    private JButton startButton, browseButton, exitButton;
    private JCheckBox geneticAlgorithmCheckBox;

    private JPanel supraParameterPanel;

    /**
     * Konštruktor triedy pre vytvorenie grafického rozhrania na spustenie algoritmov.
     */
    public StartupWindow() {
        setTitle("Genetic Algorithm - Supra method");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 480);
        getRootPane().setBorder(BorderFactory.createMatteBorder(10,10,10,10, Color.DARK_GRAY));
        setLocationRelativeTo(null); // Okno bude vycentrované

        // Panel pre výber súboru
        JPanel filePanel = new JPanel(new FlowLayout());
        JLabel fileLabel = new JLabel("Data Input:");
        fileNameField = new JTextField("distances/S_CZA_0315_0001_D.txt");
        fileNameField.setPreferredSize(new Dimension(200, 30));
        filePanel.add(fileLabel);
        filePanel.add(fileNameField);
        browseButton = new JButton("Browse");
        browseButton.addActionListener(e -> browseFile(fileNameField));
        filePanel.add(browseButton);

        // Panel pre vstup mediánov
        JPanel medianPanel = new JPanel(new FlowLayout());
        JLabel mediansLabel = new JLabel("Medians:");
        mediansField = new JTextField("31");
        mediansField.setPreferredSize(new Dimension(50, 30));
        medianPanel.add(mediansLabel);
        medianPanel.add(mediansField);

        // Pridanie checkboxu
        geneticAlgorithmCheckBox = new JCheckBox("Run Genetic Algorithm only", false);
        geneticAlgorithmCheckBox.addActionListener(this);
        medianPanel.add(geneticAlgorithmCheckBox);

        // Panel pre nastavenie parametrov pre genetický algoritmus
        JPanel gaParameterPanel = new JPanel(new GridLayout(5, 2));
        Border gaBorder = BorderFactory.createLineBorder(Color.lightGray);
        gaParameterPanel.setBorder(BorderFactory.createTitledBorder(gaBorder, "Genetic Algorithm Parameters"));
        gaParameterPanel.setBackground(Color.lightGray);
        gaParameterPanel.add(new JLabel("Population:"));
        populationField = new JTextField("50");
        gaParameterPanel.add(populationField);
        gaParameterPanel.add(new JLabel("Mutation probability:"));
        mutationField = new JTextField("0.2");
        gaParameterPanel.add(mutationField);
        gaParameterPanel.add(new JLabel("Crossover probability:"));
        crossoverField = new JTextField("0.2");
        gaParameterPanel.add(crossoverField);
        gaParameterPanel.add(new JLabel("Time Limit (seconds):"));
        timeLimitField = new JTextField("20");
        gaParameterPanel.add(timeLimitField);

        // Panel pre nastavenie parametrov pre metódu Supra
        supraParameterPanel = new JPanel(new GridLayout(5, 2));
        Border supraBorder = BorderFactory.createLineBorder(Color.lightGray);
        supraParameterPanel.setBorder(BorderFactory.createTitledBorder(supraBorder, "Supra Method Parameters"));
        supraParameterPanel.setBackground(Color.lightGray);
        supraParameterPanel.add(new JLabel("Max Iteration:"));
        maxIterationField = new JTextField("10");
        supraParameterPanel.add(maxIterationField);
        supraParameterPanel.add(new JLabel("S (Points generated in 1. phase):"));
        sField = new JTextField("5");
        supraParameterPanel.add(sField);
        supraParameterPanel.add(new JLabel("B (Forgetting parameter):"));
        bField = new JTextField("0.15");
        supraParameterPanel.add(bField);
        supraParameterPanel.add(new JLabel("C (Learning parameter):"));
        cField = new JTextField("0.3");
        supraParameterPanel.add(cField);

        // Tlačidlá run na spustenie
        startButton = new JButton("Run");
        startButton.addActionListener(this);
        startButton.setPreferredSize(new Dimension(100, 50));

        // Tlačidlo stop na zastavenie
        exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> exitApplication());
        exitButton.setPreferredSize(new Dimension(100, 50));

        // Nastavenie veľkosti písma pre nadpisy
        Font titleFont = new Font("Arial", Font.BOLD, 16);
        ((javax.swing.border.TitledBorder) gaParameterPanel.getBorder()).setTitleFont(titleFont);
        ((javax.swing.border.TitledBorder) supraParameterPanel.getBorder()).setTitleFont(titleFont);

        // Rozmiestnenie komponentov
        Container container = getContentPane();
        container.setLayout(new BorderLayout(5, 5));

        // Panel pre nastavenia genetického algoritmu a metódy Supra
        JPanel supraPanel = new JPanel(new GridLayout(1, 2, 30, 0));
        container.add(supraPanel, BorderLayout.CENTER);

        supraPanel.add(gaParameterPanel);
        supraPanel.add(supraParameterPanel);

        JPanel fileAndMedianPanel = new JPanel(new GridLayout(2, 1, 30, 30));
        fileAndMedianPanel.add(filePanel);
        fileAndMedianPanel.add(medianPanel);
        container.add(fileAndMedianPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(); // Panel pre tlačidlá
        buttonPanel.add(startButton);
        buttonPanel.add(exitButton);
        container.add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Metóda na kontrolu vykonanej akcie. Úprava parametrov do rozsahu a kontrola stlačenia tlačidiel.
     */
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == geneticAlgorithmCheckBox) {
            boolean isSelected = geneticAlgorithmCheckBox.isSelected();
            // Ak je checkbox označený, deaktivuj panel pre metódu Supra
            supraParameterPanel.setEnabled(!isSelected);
            for (Component component : supraParameterPanel.getComponents()) {
                component.setEnabled(!isSelected);
            }

        } else {
            String fileName = fileNameField.getText();
            int medians = Integer.parseInt(mediansField.getText());
            // Uprava vstupov do rozsahu
            int population = Integer.parseInt(populationField.getText());
            population = Math.max(2, population);
            population = Math.min(1000, population);

            double mutation = Double.parseDouble(mutationField.getText());
            mutation = Math.max(0, mutation);
            mutation = Math.min(1, mutation);
            if (mutation < 0.02) mutation = 0.02;
            if (mutation > 1) mutation = 1;

            double crossover = Double.parseDouble(crossoverField.getText());
            crossover = Math.max(0.02, crossover);
            crossover = Math.min(1, crossover);
            if (crossover < 0.02) crossover = 0.02;
            if (crossover > 1) crossover = 1;

            int timeLimit = Integer.parseInt(timeLimitField.getText());
            timeLimit = Math.max(20, timeLimit);
            timeLimit = Math.min(300, timeLimit);

            int maxIteration = Integer.parseInt(maxIterationField.getText());
            maxIteration = Math.max(1, maxIteration);
            maxIteration = Math.min(20, maxIteration);

            int s = Integer.parseInt(sField.getText());
            s = Math.max(1, s);
            s = Math.min(20, s);

            double B = Double.parseDouble(bField.getText());
            B = Math.max(0, B);
            B = Math.min(2, B);

            double C = Double.parseDouble(cField.getText());
            C = Math.max(0, C);
            C = Math.min(1, C);

            // Kontrola, či bol vybratý vstupný súbor
            if (fileName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select input file.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Spustenie genetického algoritmu alebo metódy Supra podľa toho, či je checkbox označený
            if (geneticAlgorithmCheckBox.isSelected()) {
                runGeneticAlgorithm(fileName, medians, population, mutation, crossover, timeLimit);
            } else {
                runSupraMethod(fileName, medians, maxIteration, s, B, C);
            }
        }
    }
    /**
     * Metóda na spustenie genetického algoritmu.
     */
    private void runGeneticAlgorithm(String fileName, int medians, int population, double mutation, double crossover, int timeLimit) {
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(fileName, medians, population, mutation, crossover, timeLimit);
        geneticAlgorithm.run();

        // Otvorenie okna s výsledkami
        double[] parameterVector = geneticAlgorithm.getParameters();   // posielame vektor parametrov
        int[] resultVector = geneticAlgorithm.getDeepCopySolution();    // posielame vektor indexov umiestnenia
        double resultCost = geneticAlgorithm.getSolutionCost();         // posielame najlepšie cenu
        ResultsWindow resultsWindow = new ResultsWindow(parameterVector, resultVector, resultCost);
        resultsWindow.setVisible(true);
    }

    /**
     * Metóda na spustenie metódy Supra.
     */
    private void runSupraMethod(String fileName, int medians, int maxIteration, int s, double B, double C) {
        SupraMethod supra = new SupraMethod(fileName, medians, maxIteration, s, B, C);
        supra.runSupraMethod();

        // Otvorenie okna s výsledkami
        double[] parameterVector = supra.getFinalPoint();   // posielame vektor parametrov
        int[] resultVector = supra.getDeepCopySolution();   // posielame vektor indexov umiestnenia
        double resultCost = supra.getP_max();               // posielame najlepšie cenu
        ResultsWindow resultsWindow = new ResultsWindow(parameterVector, resultVector, resultCost);
        resultsWindow.setVisible(true);
    }

    /**
     * Metóda na ukončenie aplikácie.
     */
    private void exitApplication() {
        System.exit(0);
    }

    /**
     * Metóda na vyhľadávanie v súbore.
     */
    private void browseFile(JTextField fileNameField) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
        fileChooser.setFileFilter(filter);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            fileNameField.setText(selectedFile.getAbsolutePath());
        }
    }

    /**
     * Main na spustenie aplikácie.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StartupWindow startupWindow = new StartupWindow();
            startupWindow.setVisible(true);
        });
    }
}
