public class main {

        private static void runGeneticAlgorithm(String fileName, int medians, int population, double mutation, double crossover, int timeLimit) {
            GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(fileName, medians, population, mutation, crossover,  timeLimit);
            geneticAlgorithm.run();
        }

        private static void runSupraMethod(String fileName, int medians, int maxIteration, int s, double B, double C) {
            SupraMethod supra = new SupraMethod(fileName, medians, maxIteration, s, B, C);
            supra.runSupraMethod();
        }
    public static void main(String[] args) {
        runSupraMethod("distances/S_CZA_0315_0001_D.txt", 31, 10, 5, 0.15, 0.3);
    }
}
