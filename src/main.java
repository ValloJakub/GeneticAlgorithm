import java.util.Random;

public class main {
    public static void main(String[] args) {
//        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(555, 0.15, 0.5,  15);
//        geneticAlgorithm.run();
        SupraMethod supra = new SupraMethod(2, 0.2, 0.3);
        supra.runSupraMethod();
    }
}
