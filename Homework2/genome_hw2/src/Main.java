import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {

        long startTime = System.nanoTime();

        String root_dir = "/Users/tgoel/Downloads/GENOME540/";
        String human_dir = root_dir + "CP001872.fna.txt";
        String mouse_dir = root_dir + "CP003913.fna.txt";
        String bacteria1_dir = root_dir + "bacteria1.fna.txt";
        String bacteria2_dir = root_dir + "bacteria2.fna.txt";
        String simulated_equal_dir = root_dir + "simulated_equal_freq.fna.txt";
        String simulated_markov0_dir = root_dir + "simulated_markov_0.fna.txt";
        String simulated_markov1_dir = root_dir + "simulated_markov_1.fna.txt";

        Reader genome0 = new Reader(human_dir);
        Reader genome1 = new Reader(mouse_dir);
        String seq = genome0.get_seq();

        Reader simulated = new Reader(genome1, "simulated_equal_freq", -1);
        Substrings sub = new Substrings(new String[]{human_dir, simulated_equal_dir, simulated_equal_dir});
        //Reader simulated = new Reader(genome1, "simulated_markov_0", 0);
        //Substrings sub = new Substrings(new String[]{human_dir, simulated_markov0_dir, simulated_markov0_dir});
        //Reader simulated = new Reader(genome1, "simulated_markov_1", 1);
        //Substrings sub = new Substrings(new String[]{human_dir, simulated_markov1_dir, simulated_markov1_dir});

        String simulated_seq = simulated.get_seq();
        String reverse_compliment_simulated = simulated.get_reverse_complement();

        String null_token = "_";
        String s = seq + null_token + simulated_seq + null_token + reverse_compliment_simulated + null_token;

        Suffix[] suffixArr = SuffixArray.suffixArray(s, null_token);

        sub.get_longest_common_substring(suffixArr, s, null_token);
        sub.get_all_substrings(seq, simulated_seq, reverse_compliment_simulated);
        sub.print();

        System.out.println("\nRuntime: " + ((System.nanoTime() - startTime)/1000000./1000./60.) + " minutes");

    }

}