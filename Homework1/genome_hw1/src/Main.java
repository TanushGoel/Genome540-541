import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {

        long startTime = System.nanoTime();

        String root_dir = "/Users/tgoel/Downloads/GENOME540/";
        String human_dir = root_dir + "CP001872.fna.txt";
        String mouse_dir = root_dir + "CP003913.fna.txt";
        String bacteria1_dir = root_dir + "bacteria1.fna.txt";
        String bacteria2_dir = root_dir + "bacteria2.fna.txt";
        String test1_dir = root_dir + "test1.fna.txt";
        String test2_dir = root_dir + "test2.fna.txt";

        String[] files = new String[]{human_dir, mouse_dir};

        String[] seqs = ReadFile.read_fasta_files(files);
        String null_token = "_";
        String s = seqs[0] + null_token + seqs[1] + null_token + seqs[2] + null_token;

        Suffix[] suffixArr = SuffixArray.suffixArray(s, null_token);

        Substrings sub = new Substrings(files);
        sub.get_longest_common_substring(suffixArr, s, null_token);
        sub.get_all_substrings(seqs[0], seqs[1], seqs[2]);
        sub.print();

        System.out.println("\nRuntime: " + ((System.nanoTime() - startTime)/1000000./1000./60.) + " minutes");

    }

}