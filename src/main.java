import java.util.ArrayList;

public class main {
    public static void main(String[] args) {
//        ArrayList<GeneticAlgorithm> tests = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(50, 1, 0.2,  60);
//            geneticAlgorithm.run();
//            tests.add(geneticAlgorithm);
//        }
//
//        double average = 0;
//        for (int i = 0; i < tests.size(); i++) {
//            average += tests.get(i).getSolutionCost();
//        }
//        System.out.println("Average cost: " + average / 10);

        SupraMethod supra = new SupraMethod(5, 0.15, 0.3);
        supra.runSupraMethod();
    }
}
