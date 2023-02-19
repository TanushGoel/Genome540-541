import java.util.concurrent.ThreadLocalRandom;

public class Simulation {

    public static String equal_frequency(int len){
        String[] bases = new String[]{"A", "C", "G", "T"};
        StringBuilder seq = new StringBuilder();
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        for(int i=0; i<len-1; i++){
            seq.append(bases[rand.nextInt(0, 4)]);
        }
        return seq.toString();
    }

    public static String markov0(double[] occurrences, int len){
        String[] nucleotides = {"A", "C", "G", "T"};
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        StringBuilder simulatedSequence = new StringBuilder();
        for (int i=0; i<len; i++) {
            double randomNum = rand.nextDouble();
            double cumulativeProb = 0;
            int currentNucleotide = 0;
            for (int j=0; j<4; j++) {
                cumulativeProb += occurrences[j];
                if (randomNum < cumulativeProb) {
                    currentNucleotide = j;
                    break;
                }
            }
            simulatedSequence.append(nucleotides[currentNucleotide]);
        }
        return simulatedSequence.toString();
    }

    public static String markov1(double[][] occurrences, int len){
        String[] nucleotides = {"A", "C", "G", "T"};
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        int previousNucleotide = rand.nextInt(4);
        StringBuilder simulatedSequence = new StringBuilder();

        for (int i=0; i<len; i++) {
            double randomNum = rand.nextDouble();
            double cumulativeProb = 0;
            int currentNucleotide = 0;
            for (int j=0; j<4; j++) {
                cumulativeProb += occurrences[previousNucleotide][j];
                if (randomNum <= cumulativeProb) {
                    currentNucleotide = j;
                    break;
                }
            }
            simulatedSequence.append(nucleotides[currentNucleotide]);
            previousNucleotide = currentNucleotide;
        }
        return simulatedSequence.toString();
    }
}
