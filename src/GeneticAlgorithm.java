import java.io.*;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class GeneticAlgorithm {
    // Veľkosť populácie
    private final int populationSize;

    // Pravdepodobnosť mutácie
    private final double mutationProbability;

    // Matica vzdialeností
    private int[][] distanceMatrix;

    // Počet lokácii na umiestnenie
    private int numLocations ; // na začiatok dáme 10% z celkových umiestnení

    // Časový limit v sekundách
    private final long timeLimit; // na ukončenie behu algoritmu

    // Počet p-mediánov -> bude zadané
    private static final int pMedians = 36;

    // Pravdepodobnosť kríženia
    private static final double crossoverProbability = 0.5;

    private static final Random random = new Random();

    // Reprezentácia populácie
    private static int[][] population;

    // Cena každého jedinca v populácii
    private static int[] fitness;

    // TODO: pozrieť sa na výpis riešenia -> zdá sa, že vypisuje rovnaké riešenie pre rôzne ceny
    // TODO: overiť, či si správne riešenie aj s cenou ukladám ako hardcopy

    /**
     * Konštruktor genetického algoritmu.
     * @param populationSize, mutationProbability, timeLimit
     */
    public GeneticAlgorithm(int populationSize, double mutationProbability, long timeLimit) {
        this.populationSize = populationSize;
        this.mutationProbability = mutationProbability;
        this.timeLimit = timeLimit * 1000; // prevod zo sekúnd na milisekundy
    }

    /**
     * Beh algoritmu.
     */
    public void run() {
        // Vytvorenie miest vhodných na umiestňovanie
        this.calculateNumLocations();

        // Inicializácia populácie
        this.initializePopulation();

        // Inicializácia počiatočného času
        long startTime = System.currentTimeMillis();

        // Algoritmus beží do stanoveného limitu
        while (System.currentTimeMillis() - startTime <= this.timeLimit) {
            calculateFitness();
            int[][] newPopulation = new int[this.populationSize][this.numLocations];

            // Inicializácia najlepšieho indexu v rámci generácie
            int bestIndex = getBestSolutionIndex();

            for (int i = 0; i < this.populationSize; i += 2) { // spracúvajú sa 2 jedinci(rodičia)
                int parent1 = tournamentSelection();
                int parent2 = tournamentSelection();

                // Kríženie
                if (random.nextDouble() < crossoverProbability) {
                    uniformCrossover(parent1, parent2, newPopulation, i);
                } else {
                    // Ak sa neskrížil, prekopírujeme ho do ďalšej generácie taký aký je
                    copyToNextGeneration(parent1, parent2, newPopulation, i);
                }

                // Mutácia
                if (random.nextDouble() < mutationProbability) {
                    mutate(newPopulation[i]);
                }
                if (random.nextDouble() < mutationProbability) {
                    mutate(newPopulation[i + 1]);
                }
            }
            // Náhrada populácie novovytvorenou(zmutovanou) populáciou
            population = newPopulation;

            // Aktualizácia najlepšieho riešenia(indexu)
            bestIndex = getBestSolutionIndex();

            // Prepis poľa do stringu
            System.out.println("Best Solution: " + Arrays.toString(population[bestIndex]));
            System.out.println("Best Cost = " + fitness[bestIndex]);
//            System.out.println("Best index: " + bestIndex);
            System.out.println("----------------------------");
        }
        System.out.println("Time limit: " + this.timeLimit / 1000 + " seconds");
        System.out.println("Time elapsed: " + (System.currentTimeMillis() - startTime)  + " miliseconds");
    }

    /**
     * Metóda na pridelenie množstva umiestnení. Počet umiestnení je rovný veľkosti matice.
     */
    private void calculateNumLocations() {
        this.numLocations = distanceMatrix.length;
    }

    /**
     * Metóda na inicializáciu populácie. Náhodne rozmiestni p-mediány
     */
    private void initializePopulation() {
        population = new int[this.populationSize][this.numLocations];
        fitness = new int[this.populationSize];

        for (int i = 0; i < populationSize; i++) {
            for (int j = 0; j < pMedians; j++) {
                int location = random.nextInt(this.numLocations);
                population[i][location] = 1;
            }
        }
    }

    /**
     * Metóda na výpočet vhodnosti jedinca v celej populácii.
     */
    private void calculateFitness() {
        for (int i = 0; i < this.populationSize; i++) {
            fitness[i] = calculateCost(population[i]);
        }
    }

    /**
     * Metóda na výpočet vzdialeností(ceny) medzi p-mediánmi.
     * @return totalDistance
     */
    private int calculateCost(int[] individual) {
        int totalDistance = 0;

        for (int i = 0; i < distanceMatrix.length; i++) {
            int minimalDistance = Integer.MAX_VALUE;

            for (int j = 0; j < distanceMatrix[i].length; j++) {
                if (individual[j] == 1) {  // Ak je na danej pozícii umiestnenie
                    minimalDistance = Math.min(minimalDistance, distanceMatrix[i][j]);
                }
            }
            totalDistance += minimalDistance;
        }
        return totalDistance;
    }

    /**
     * Metóda na zavedenie rozmanitosti pri výbere rodičov.
     * Umožňuje, aby boli za rodičov vybraní lepší aj slabší jedinci.
     */
    private int tournamentSelection() {
        int tournamentSize = 5;
        int[] candidates = new int[tournamentSize];

        for (int i = 0; i < tournamentSize; i++) {
            candidates[i] = random.nextInt(this.populationSize);
        }

        int bestCandidate = candidates[0];
        for (int i = 1; i < tournamentSize; i++) {
            if (fitness[candidates[i]] < fitness[bestCandidate]) {
                bestCandidate = candidates[i];
            }
        }
        return bestCandidate;
    }

    /**
     * Operácia uniformného kríženia.
     * Náhodný výber bitu od jedného z rodičov -> každý bit potomka je zvolený od rodiča s pravdepodobnosťou 0,5.
     * */
    private void uniformCrossover(int parent1, int parent2, int[][] newPopulation, int index) {
        int[] child1 = new int[this.numLocations];
        int[] child2 = new int[this.numLocations];

        int pMediansChild1 = 0;
        int pMediansChild2 = 0;

        for (int i = 0; i < this.numLocations; i++) {
            if (random.nextDouble() < 0.5) {
                child1[i] = population[parent1][i];
                child2[i] = population[parent2][i];
            } else {
                child1[i] = population[parent2][i];
                child2[i] = population[parent1][i];
            }
            pMediansChild1 += child1[i];
            pMediansChild2 += child2[i];
        }

        // Kontrola, či majú potomkovia požadovaný počet p-mediánov
        validateSolution(child1, pMediansChild1);
        validateSolution(child2, pMediansChild2);

        System.arraycopy(child1, 0, newPopulation[index], 0, this.numLocations);
        System.arraycopy(child2, 0, newPopulation[index + 1], 0, this.numLocations);
    }

    /**
     * Operácia na kontrolu správnosti riešenie.
     * Pri krížení môže vzniknúť nevyhovujúce riešenie, ktoré môže obsahovať menej, resp. viac umiestnení ako je požadovaný počet.
     * */
    private void validateSolution(int[] child, int currentCountPMedians) {
        // Ak je počet p-mediánov menší, pridávaj pokiaľ ich nebudem potrebné množstvo
        while (currentCountPMedians < pMedians) {
            int randomIndex = random.nextInt(this.numLocations);
            if (child[randomIndex] == 0) {
                child[randomIndex] = 1;
                currentCountPMedians++;
            }
        }

        // Ak je počet p-mediánov väčší, odoberaj pokiaľ ich nebude potrebné množstvo
        while (currentCountPMedians > pMedians) {
            int randomIndex = random.nextInt(this.numLocations);
            if (child[randomIndex] == 1) {
                child[randomIndex] = 0;
                currentCountPMedians--;
            }
        }
    }

    /**
     * Operácia mutácie výmenou priradených mediánov.
     * Výmena bitov na dvoch pozíciach(SWAP).
     * */
    private void mutate(int[] mutationPlace) {
        int position1 = random.nextInt(this.numLocations);
        int position2 = random.nextInt(this.numLocations);

        // Kontrola, aby sa nevymieňali rovnaké pozície
        while (position1 == position2) {
            position2 = random.nextInt(this.numLocations);
        }

        // Výmena hodnôt na vybraných pozíciach
        int temp = mutationPlace[position1];
        mutationPlace[position1] = mutationPlace[position2];
        mutationPlace[position2] = temp;
    }

    /**
     * Nájdenie jedinca s minimálnou cenou riešenia(jeho index).
     * @return bestIndex
     */
    private int getBestSolutionIndex() {
        int bestIndex = 0;
        for (int i = 1; i < this.populationSize; i++) {
            if (fitness[i] < fitness[bestIndex]) {
                bestIndex = i;
            }
        }
        return bestIndex;
    }

    /**
     * Metóda na kopírovanie rodičovských jedincov do novej generácie.
     */
    private void copyToNextGeneration(int parent1, int parent2, int[][] newPopulation, int index) {
        System.arraycopy(population[parent1], 0, newPopulation[index], 0, this.numLocations);
        System.arraycopy(population[parent2], 0, newPopulation[index + 1], 0, this.numLocations);
    }

    /**
     * Metóda na načítanie matice vzdialeností zo súboru.
     */
    public void loadDistanceMatrixFromFile(String fileName) {
        try {
            Scanner scanner = new Scanner(new File(fileName));
            // Prvé dve hodnoty sú rozmer matice
            int rows = scanner.nextInt();
            int columns = scanner.nextInt();

            this.distanceMatrix = new int[rows][columns];

            // Čítanie a napĺňanie
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    this.distanceMatrix[i][j] = scanner.nextInt();
                }
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        /*         Výpis matice    */
//        for (int i = 0; i < distanceMatrix.length; i++) {
//            for (int j = 0; j < distanceMatrix[0].length; j++) {
//                System.out.print(distanceMatrix[i][j] + " ");
//            }
//            System.out.println();
//        }
    }
}
