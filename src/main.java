import java.util.ArrayList;

public class main {

    private static void runGeneticAlgorithm(int population, double mutation, double crossover, int timeLimit) {
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(population, mutation, crossover,  timeLimit);
        geneticAlgorithm.run();
    }

    private static void runSupraMethod(int s, double B, double C) {
        SupraMethod supra = new SupraMethod(s, B, C);
        supra.runSupraMethod();
    }
    public static void main(String[] args) {
        ArrayList<SupraMethod> tests = new ArrayList<>();
        SupraMethod supra1 = new SupraMethod(5, 0.15, 0.3);
        supra1.runSupraMethod();
        tests.add(supra1);

        SupraMethod supra2 = new SupraMethod(5, 0.15, 0.15);
        supra2.runSupraMethod();
        tests.add(supra2);

        SupraMethod supra3 = new SupraMethod(5, 0.15, 0.5);
        supra3.runSupraMethod();
        tests.add(supra3);


        SupraMethod supra4 = new SupraMethod(5, 0.5, 0.15);
        supra4.runSupraMethod();
        tests.add(supra4);

        SupraMethod supra5 = new SupraMethod(5, 0.5, 0.15);
        supra5.runSupraMethod();
        tests.add(supra5);

        SupraMethod supra6 = new SupraMethod(5, 0.5, 0.15);
        supra6.runSupraMethod();
        tests.add(supra6);

        SupraMethod supra7 = new SupraMethod(5, 0.15, 0.5);
        supra7.runSupraMethod();
        tests.add(supra7);

        SupraMethod supra8 = new SupraMethod(5, 0.15, 0.5);
        supra8.runSupraMethod();
        tests.add(supra8);

        SupraMethod supra9 = new SupraMethod(5, 0.15, 0.5);
        supra9.runSupraMethod();
        tests.add(supra9);

        for (int i = 0; i < tests.size(); i++) {
            System.out.println(i + " Cost: " + tests.get(i).getP_max());
        }

//        ArrayList<GeneticAlgorithm> tests = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(110, 0.2551789705678296, 0.23458472218287452, 68);
        //  runGeneticAlgorithm(75, 0.2148145669439971, 0.28060897946281504, 51);
//            geneticAlgorithm.run();
//            tests.add(geneticAlgorithm);
//        }
//
//        double average = 0;
//        for (int i = 0; i < tests.size(); i++) {
//            average += tests.get(i).getSolutionCost();
//        }
//        System.out.println("Average cost: " + average / 10);
    }
}
