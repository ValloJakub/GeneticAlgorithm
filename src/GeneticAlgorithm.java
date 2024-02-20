import java.util.Arrays;
import java.util.Random;

public class GeneticAlgorithm {
    // veľkosť populácie
    private int populationSize;

    // pravdepodobnosť mutácie
    private double mutationProbability;

    // maximálny počet generácii
    //private int maxGenerations;

    // počet lokácii
    private int numLocations ; // na začiatok dáme 10% z celkových umiestnení

    // časový limit v sekundách
    private long timeLimit; // na ukončenie behu algoritmu

    // počet mutácii
    private int maxMutations;

    // počet mediánov // malo by byť zadané
    private static final int p = 5;

    // pravdepodobnosť kríženia
    private static final double crossoverProbability = 0.8;

    private static final Random random = new Random();

    // reprezentácia populácie
    private static int[][] population;

    // cena každého jedinca v populácii
    private static int[] fitness;

    /**
     * Konštruktor algoritmu.
     * @param populationSize, mutationProbability, maxGenerations, timeLimit
     */
    public GeneticAlgorithm(int populationSize, double mutationProbability, long timeLimit) {
        this.populationSize = populationSize;
        this.mutationProbability = mutationProbability;
     //   this.maxGenerations = maxGenerations;
        this.timeLimit = timeLimit * 1000; // prevod zo sekúnd na milisekundy
    }

    /**
     * Priebeh algoritmu.
     */
    public void run() {
        // pred inicializáciou vypočíta množstvo lokácii podľa maximálnej populácie
        this.calculateNumLocations();

        // inicializujem populaciu
        this.initializePopulation();

        // inicializácia počiatočného času
        long startTime = System.currentTimeMillis();

        // algoritmus beží do stanoveného limitu
        while (System.currentTimeMillis() - startTime <= this.timeLimit) {
            calculateFitness();
            int[][] newPopulation = new int[this.populationSize][this.numLocations];

            for (int i = 0; i < this.populationSize; i += 2) {
                int parent1 = tournamentSelection();
                int parent2 = tournamentSelection();

                // Kríženie
                if (random.nextDouble() < this.crossoverProbability) {
                    uniformCrossover(parent1, parent2, newPopulation, i);
                } else {
                    // Ak sa neskrížil, prekopírujeme ho do ďalšej generácie taký aký je
                    copyToNextGeneration(parent1, parent2, newPopulation, i);
                }

                // Mutácia
                if (random.nextDouble() < this.mutationProbability) {
                    mutate(newPopulation[i]);
                }
                if (random.nextDouble() < this.mutationProbability) {
                    mutate(newPopulation[i + 1]);
                }
            }

            // nahraď populáciu zmutovanou populáciou
            this.population = newPopulation;

            // Vypíš najlepšie riešenie pre každu generáciu
            int bestIndex = getBestSolutionIndex();

            System.out.println("Best Cost = " + fitness[bestIndex]);
            System.out.println("Best Solution: " + Arrays.toString(population[bestIndex]));
        }

        System.out.println("Time limit: " + this.timeLimit / 1000 + " seconds");
        System.out.println("Time elapsed: " + (System.currentTimeMillis() - startTime)  + " miliseconds");
    }

    /**
     * Metóda na výpočet množstva umiestnení.
     * 10% z celkovej populácie.
     */
    private void calculateNumLocations() {
        this.numLocations = (this.populationSize / 100) * 10;
    }

    /**
     * Metóda na inicializáciu populácie.
     */
    private void initializePopulation() {
        population = new int[this.populationSize][this.numLocations];
        fitness = new int[this.populationSize];

        for (int i = 0; i < populationSize; i++) {
            for (int j = 0; j < p; j++) {
                int location = random.nextInt(this.numLocations);
                this.population[i][location] = 1;
            }
        }
    }

    /**
     * Metóda na výpočet vhodnosti jedinca v populácii.
     */
    private void calculateFitness() {
        for (int i = 0; i < this.populationSize; i++) {
            this.fitness[i] = calculateCost(this.population[i]);
        }
    }

    /**
     * Metóda na výpočet vzdialenosti(ceny) medzi mediánmi.
     * @param solution
     * @return totalDistance
     */
    private int calculateCost(int[] solution) {
        int totalDistance = 0;

        // Pre každý bod zistíme pridelený p-median a pridáme jeho vzdialenosť k celkovej vzdialenosti
        for (int i = 0; i < this.numLocations; i++) {
            int assignedPMedian = findAssignedPMedian(solution);
            totalDistance += Math.abs(i - assignedPMedian); // Pridáme vzdialenosť medzi bodom a prideleným p-medianom
        }

        return totalDistance;
    }

    /**
     * Metóda na nájdenie prideleného p-medianu pre daný bod.
     */
    private int findAssignedPMedian(int[] solution) {
        for (int i = 0; i < this.numLocations; i++) {
            if (solution[i] == 1) {
                return i; // Index p-medianu, ktorý je pridelený danému bodu
            }
        }
        return -1;
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
            if (this.fitness[candidates[i]] < this.fitness[bestCandidate]) {
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
        for (int i = 0; i < this.numLocations; i++) {
            if (random.nextDouble() < 0.5) {
                newPopulation[index][i] = this.population[parent1][i];
                newPopulation[index + 1][i] = this.population[parent2][i];
            } else {
                newPopulation[index][i] = this.population[parent2][i];
                newPopulation[index + 1][i] = this.population[parent1][i];
            }
        }
    }

    /**
     * Operácia mutácie náhodnou zmenou priradených mediánov.
     * Generuje číslo od 0 po numLocations-1, čo sa použije ako index kde, kde prebehne mutácia.
     * */
    private void mutate(int[] solution) {
        int locationToMutate = random.nextInt(this.numLocations);
        solution[locationToMutate] = 1 - solution[locationToMutate];
    }

    /**
     * Nájdenie indexu riešenia s minimálnou cenou.
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
     * Kopírovanie rodičovských jedincov do novej generácie.
     */
    private void copyToNextGeneration(int parent1, int parent2, int[][] newPopulation, int index) {
        System.arraycopy(this.population[parent1], 0, newPopulation[index], 0, this.numLocations);
        System.arraycopy(this.population[parent2], 0, newPopulation[index + 1], 0, this.numLocations);
    }
}
