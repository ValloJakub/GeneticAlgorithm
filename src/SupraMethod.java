import java.util.Random;

public class SupraMethod {
    private static final Random random = new Random();
    private final double B; // Parameter zabúdania
    private final double C; // Parameter učenia
    private final int max_s; // Maximálny počet generovaných bodov
    private double[] w; // Parameter učenia/pamäť

    private static final int VECTOR_SIZE = 4;   // Veľkosť vektorov určená podľa počtu potrebných parametrov
    private int p_max; // Hodnota doteraz najlepšieho nájdeného riešenia
    private double[] pk; // Vektor parametrov počiatočného bodu
    private int initialPointCost; // Na uloženie hodnoty účelovej funkcie počiatočného bodu
    private double[] statisticalGradient; // Štatistický gradient r

    public SupraMethod(int s) {
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(100, 0.15,  0.5, 2);
        geneticAlgorithm.run();
        this.p_max = geneticAlgorithm.getSolutionCost();
        this.initialPointCost = p_max;

        this.B = 0.25;
        this.C = 0.3;
        this.max_s = s; // Počet bodov na vytvorenie

        this.w = new double[]{0.0, 0.0, 0.0, 0.0};                       // Na začiatku pamäť prázdna/neinicializovaná
        this.statisticalGradient = new double[]{0.0, 0.0, 0.0, 0.0};     // Na začiatku štatistický gradient prázdny/neinicializovaný

        this.pk = new double[]{geneticAlgorithm.getPopulationSize(), geneticAlgorithm.getMutationProbability(), geneticAlgorithm.getCrossoverProbability(), geneticAlgorithm.getTimeLimit()};
    }

    /**
     * Beh metódy.
     */
    public void runSupraMethod() {
        // Prvá fáza
        int j = 0;
        while (j < max_s) {
            // Uloženie vektora posunu
            double[] r = generateRandomVectorR();

            // Vytvorenie nového bodu na základe vzťahu p^kj = R(p^k + r^j)
            double[] newPoint = this.createNewPoint(r);

            // Spustenie genetického algoritmu pre každý vytvorený bod p^kj
            GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm((int) newPoint[0], newPoint[1], newPoint[2], (int) newPoint[3]);
            geneticAlgorithm.run();

            // Aktualizácia hodnôt štatistického gradientu(r), pamäte(w) a ceny bodu
            updateValues(newPoint, geneticAlgorithm.getSolutionCost());

            j++;
        }

        System.out.println("Statistical gradient parameters:");
        System.out.print("Population size: " + statisticalGradient[0] + ", Mutation probability: " + statisticalGradient[1] + ", Crossover probability: "
                + statisticalGradient[2] + ", Time limit: " + statisticalGradient[3] + " sec.\n");


        // Druhá fáza
    }

    /**
     * Metóda na vytvorenie nového bodu p^kj na základe vzťahu p^kj = p^k +r^j
     */
    private double[] createNewPoint(double[] r) {
        double[] pkj = new double[VECTOR_SIZE];

        for (int i = 0; i < VECTOR_SIZE; i++) {
            pkj[i] = pk[i] + r[i];

            // Upravenie do rozsahu (pri prekročení hraničných hodnôt) podľa vzťahu p^kj= R(p^k + r^j)
            // Populácia
            if (pkj[0] % 2 != 0) {  // Treba zabezpečiť, aby populácia kvôli kríženiu bola párnym číslom
                pkj[0] += 1;
            }
            if (pkj[0] > 1000) {
                pkj[0] = 1000;
            }
            if (pkj[0] < 2) {
                pkj[0] = 2;
            }


            // Mutácia
            if (pkj[1] > 1) {
                pkj[1] = 1;
            }
            if (pkj[1] < 0) {
                pkj[1] = 0;
            }

            // Kríženie
            if (pkj[2] > 1) {
                pkj[2] = 1;
            }
            if (pkj[2] < 0) {
                pkj[2] = 0;
            }

            // Časový limit
            if (pkj[3] > 1200) {
                pkj[3] = 1200;
            }
            if (pkj[3] < 60) {
                pkj[3] = 60;
            }
        }
        return pkj;
    }

    /**
     * Generovanie vektora posunu r^j podľa vzťahu r^j = w + x
     */
    private double[] generateRandomVectorR() {
        double[] x = new double[]{this.generatePopulationSize(), this.generateMutationProbability(), generateCrossoverProbability(), this.generateTimeLimit()};

        // Súčet jednotlivých zložiek vektorov
        for (int i = 0; i < VECTOR_SIZE; i++) {
            x[i] += w[i];
        }
        return x;
    }

    private void updateValues(double[] newPoint, int cost) {
        // Aktualizácia hodnoty p_max, ak bolo nájdené lepšie riešenie
        if (cost < p_max) {
            p_max = cost;
        }

        // Aktualizácia hodnoty štatistického gradientu r
        for (int i = 0; i < VECTOR_SIZE; i++) {
            statisticalGradient[i] += (this.initialPointCost - cost) * (newPoint[i] - pk[i]);
        }

        // Aktualizácia hodnoty vektoru w
        for (int i = 0; i < VECTOR_SIZE; i++) {
            w[i] = B * w[i] + C * ((this.initialPointCost - cost) * (newPoint[i] - pk[i]));
        }
    }

    /**
     * Generovanie náhodnej veľkosti populácie.
     */
    private int generatePopulationSize() {
        return random.nextInt(999) + 2; // Generovanie v rozmedzí od 2 po 1000
    }

    /**
     * Generovanie náhodnej pravdepodobnosti mutácie.
     */
    private double generateMutationProbability() {
        return random.nextDouble();     // Generovanie v rozmedzí od 0 po 1
    }

    /**
     * Generovanie náhodnej pravdepodobnosti kríženia.
     */
    private double generateCrossoverProbability() {
        return random.nextDouble();     // Generovanie v rozmedzí od 0 po 1
    }

    /**
     * Generovanie náhodného časového limitu.
     */
    private long generateTimeLimit() {
//        return (random.nextInt(1140) + 60); // Generovanie od 60 po 1200 sekúnd
        return (random.nextInt(3) + 2); // 2 až 5 sekúnd
    }
}
