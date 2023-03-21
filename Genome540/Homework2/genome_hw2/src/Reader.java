import java.io.*;
import java.util.*;

public class Reader {

    private Map<Character, Integer> bases;
    private String sequence;
    private int invalid_chars;
    private String title;
    private String file_name;
    private int[][] dinucleotides;
    private double[] frequencies;
    private double[][] frequency_table;
    private int simulation_type;

    public Reader(String file_name) throws IOException {
        this.bases = new TreeMap<>();
        this.bases.put('A', 0);
        this.bases.put('C', 0);
        this.bases.put('G', 0);
        this.bases.put('T', 0);
        this.dinucleotides = new int[4][4];
        this.frequencies = new double[4];
        this.frequency_table = new double[4][4];
        this.file_name = file_name;
        read_file();
        read_fasta_file();
        read_sequence(true);
    }

    public Reader(Reader reader, String title, int simulation_type) throws IOException {
        this.bases = new TreeMap<>();
        this.bases.put('A', 0);
        this.bases.put('C', 0);
        this.bases.put('G', 0);
        this.bases.put('T', 0);
        this.sequence = reader.get_seq();
        this.frequencies = reader.get_freqs();
        this.frequency_table = reader.get_freq_table();
        this.title = title;
        this.file_name = "/Users/tgoel/Downloads/GENOME540/" + title + ".fna.txt";
        this.simulation_type = simulation_type;
        create_simulated();
        read_sequence(false);
        write_sequence();
    }

    public String get_seq(){
        return this.sequence;
    }
    public double[] get_freqs(){
        return this.frequencies;
    }
    public double[][] get_freq_table() {
        return this.frequency_table;
    }

    private void read_file() throws IOException {

        Set<Character> keys = this.bases.keySet();

        String line;
        StringBuilder seq = new StringBuilder();
        BufferedReader buffer = new BufferedReader(new FileReader(this.file_name));
        while ((line=buffer.readLine()) != null) {

            if(line.charAt(0) == '>'){
                this.title = line;
                continue;
            }

            for(char c: line.toCharArray()){
                if(c == '\n' || c == ' ') {
                    continue;
                }
                if(keys.contains(Character.toUpperCase(c))) {
                    this.bases.put(Character.toUpperCase(c), this.bases.get(Character.toUpperCase(c))+1);
                    seq.append(Character.toUpperCase(c));
                }
                else {
                    this.invalid_chars++;
                }
            }
        }
        this.sequence = seq.toString();
    }

    private void read_fasta_file() {
        System.out.println("\nFasta 1: " + this.file_name.split("/")[5].substring(0, this.file_name.split("/")[5].length() - 4));
        System.out.println("Non-alphabetic characters: " + this.invalid_chars);
        System.out.println(this.title);
        System.out.println("*=" + this.sequence.length());
        for (Character base : this.bases.keySet()) {
            System.out.println(base + "=" + this.bases.get(base));
        }
        System.out.println("N=" + 0);
    }

    private void read_sequence(boolean from_file) {

        char[] characters = this.sequence.toCharArray();

        if(!from_file) {
            Set<Character> keys = this.bases.keySet();
            for (char c : characters) {
                if (keys.contains(Character.toUpperCase(c))) {
                    this.bases.put(Character.toUpperCase(c), this.bases.get(Character.toUpperCase(c)) + 1);
                } else {
                    this.invalid_chars++;
                }
            }

            System.out.println("\nFasta " + (this.simulation_type + 3) + ": " + this.file_name.split("/")[5].substring(0, this.file_name.split("/")[5].length() - 4));
            System.out.println("Non-alphabetic characters: " + this.invalid_chars);
            System.out.println(this.title);
            System.out.println("*=" + this.sequence.length());
            for (Character base : this.bases.keySet()) {
                System.out.println(base + "=" + this.bases.get(base));
            }
            System.out.println("N=" + 0);
        }

        System.out.println("\nNucleotide Frequencies:");
        int base_num = 0;
        for (Character base : this.bases.keySet()) {
            double freq = this.bases.get(base) / (double) this.sequence.length();
            freq = ((int) (freq * 100000));
            freq /= 100000;
            this.frequencies[base_num] = freq;
            System.out.println(base + ": " + freq);
            base_num++;
        }

        this.dinucleotides = new int[4][4];
        Map<Character, Integer> nucleotide_to_int = new HashMap<>();
        nucleotide_to_int.put('A', 0);
        nucleotide_to_int.put('C', 1);
        nucleotide_to_int.put('G', 2);
        nucleotide_to_int.put('T', 3);
        for(int idx=0; idx<characters.length - 1; idx++){
            int i = nucleotide_to_int.get(characters[idx]);
            int j = nucleotide_to_int.get(characters[idx+1]);
            this.dinucleotides[i][j]++;
        }

        System.out.println();
        System.out.println("Dinucleotide Count Matrix:");
        String[] b = new String[]{"A", "C", "G", "T"};
        for(int i=0; i<this.dinucleotides.length; i++){
            System.out.print(b[i] + "=");
            for(int j: this.dinucleotides[i]){
                System.out.print(j + " ");
            }
            System.out.println();
        }

        System.out.println();
        System.out.println("Dinucleotide Frequency Matrix:");
        for(int i=0; i<this.dinucleotides.length; i++){
            System.out.print(b[i] + "=");
            for(int j=0; j<this.dinucleotides[i].length; j++){
                double frequency = (this.dinucleotides[i][j] / (double) (characters.length - 1));
                frequency = ((int) (frequency * 100000));
                frequency /= 100000;
                System.out.print(frequency + " ");
            }
            System.out.println();
        }

        System.out.println();
        System.out.println("Conditional Frequency Matrix:");
        for(int i=0; i<this.dinucleotides.length; i++){
            System.out.print(b[i] + "=");
            for(int j=0; j<this.dinucleotides[i].length; j++){
                double frequency = (this.dinucleotides[i][j] / (double) Arrays.stream(this.dinucleotides[i]).sum());
                frequency = ((int) (frequency * 100000));
                frequency /= 100000;
                this.frequency_table[i][j] = frequency;
                System.out.print(frequency + " ");
            }
            System.out.println();
        }
    }

    public String get_reverse_complement(){
        StringBuilder result = new StringBuilder();
        Map<Character, Character> reverse_map = new HashMap<>();
        reverse_map.put('A', 'T');
        reverse_map.put('C', 'G');
        reverse_map.put('G', 'C');
        reverse_map.put('T', 'A');
        for(char c: this.sequence.toCharArray()){
            result.append(reverse_map.get(c));
        }
        return result.toString();
    }

    private void write_sequence() throws IOException {
        Writer fileWriter = new FileWriter(this.file_name, false);
        fileWriter.write(this.sequence);
        fileWriter.close();
    }

    private void create_simulated(){
        if(this.simulation_type == -1){
            this.sequence = Simulation.equal_frequency(this.sequence.length());
        }
        else if(this.simulation_type == 0){
            this.sequence = Simulation.markov0(this.frequencies, this.sequence.length());
        }
        else if(this.simulation_type == 1){
            this.sequence = Simulation.markov1(this.frequency_table, this.sequence.length());
        }
        else{
            throw new IllegalArgumentException("simulation type impossible");
        }
    }
}