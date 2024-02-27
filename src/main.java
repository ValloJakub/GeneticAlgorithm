public class main {

    public static void main(String[] args) {
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(100, 0.15,  60);
        geneticAlgorithm.loadDistanceMatrixFromFile("C:\\Users\\jakub\\OneDrive\\Počítač\\Bakalárska Práca\\Súbory\\Stredne CestnaSiet_SR 1\\VUC ZA\\S_CZA_0315_0001_D.txt");
        geneticAlgorithm.run();
    }
}
