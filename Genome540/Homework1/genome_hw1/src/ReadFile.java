import java.io.*;
import java.util.*;

public class ReadFile {

    private static String[] read_fasta_file(String file_name) throws IOException {
        StringBuilder seq = new StringBuilder();
        String title = "";
        Integer invalid_chars = 0;

        Map<Character, Integer> base_counts = new HashMap<>();
        base_counts.put('A', 0);
        base_counts.put('C', 0);
        base_counts.put('T', 0);
        base_counts.put('G', 0);
        Set<Character> keys = base_counts.keySet();

        String line;
        BufferedReader buffer = new BufferedReader(new FileReader(file_name));
        while ((line=buffer.readLine()) != null) {

            if(line.charAt(0) == '>'){
                title = line;
                continue;
            }

            for(char c: line.toCharArray()){
                if(c == '\n' || c == ' ') {
                    continue;
                }
                if(keys.contains(Character.toUpperCase(c))) {
                    base_counts.put(Character.toUpperCase(c), base_counts.get(Character.toUpperCase(c))+1);
                    seq.append(Character.toUpperCase(c));
                }
                else {
                    invalid_chars++;
                }
            }
        }

        return new String[]{seq.toString(), base_counts.toString(), String.valueOf(invalid_chars), title};
    }

    public static String[] read_fasta_files(String[] files) throws IOException {
        String[] file1 = read_fasta_file(files[0]);
        String[] file2 = read_fasta_file(files[1]);

        String f1 = files[0].split("/")[5];
        String[] bases1 = file1[1].substring(1, file1[1].length() - 1).split(", ");
        System.out.println("\nFasta 1: " + f1.substring(0, f1.length() - 4));
        System.out.println("Non-alphabetic characters: " + file1[2]);
        System.out.println(file1[3].strip());
        System.out.println("*=" + file1[0].length());
        for (String base : bases1) {
            System.out.println(base);
        }
        System.out.println("N=" + 0);

        String f2 = files[1].split("/")[5];
        String[] bases2 = file2[1].substring(1, file2[1].length() - 1).split(", ");
        System.out.println("\nFasta 2: " + f2.substring(0, f2.length() - 4));
        System.out.println("Non-alphabetic characters: " + file2[2]);
        System.out.println(file2[3].strip());
        System.out.println("*=" + file2[0].length());
        for (String base : bases2) {
            System.out.println(base);
        }
        System.out.println("N=" + 0);

        return new String[]{file1[0], file2[0], get_reverse_complement(file2[0])};
    }

    private static String get_reverse_complement(String sequence){
        StringBuilder result = new StringBuilder();
        Map<Character, Character> reverse_map = new HashMap<>();
        reverse_map.put('A', 'T');
        reverse_map.put('C', 'G');
        reverse_map.put('T', 'A');
        reverse_map.put('G', 'C');
        for(char c: sequence.toCharArray()){
            result.append(reverse_map.get(c));
        }
        return result.toString();
    }
}