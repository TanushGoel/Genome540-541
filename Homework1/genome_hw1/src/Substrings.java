import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Substrings {

    private List<Integer> hist;
    private int[] histogram;
    private String lcs;
    private int max_match_len;
    private int max_idx = -1;

    private List<Integer> idxs_seq1;
    private List<Integer> idxs_seq2;
    private List<Integer> idxs_seq3;

    private final String[] files;

    public Substrings(String[] file_names){
        this.hist = new ArrayList<>();
        this.files = file_names;
    }

    private int get_match_length(String suffixA, String suffixB) {
        int minLength = Math.min(suffixA.length(), suffixB.length());
        for(int i = 0; i < minLength; i++) {
            if(suffixA.charAt(i) != suffixB.charAt(i)) {
                return i;
            }
        }
        return minLength;
    }

    public void get_longest_common_substring(Suffix[] suffixArray, String sequence, String nt) {
        int n = suffixArray.length;

        Map<Integer, int[]> nearest = new HashMap<>();
        int pos = -1;
        for(int i=0; i<suffixArray.length; i++) {
            if (suffixArray[i].genome == 1) {
                nearest.put(i, new int[2]);
                nearest.get(i)[0] = pos;
            }
            else {
                pos = i;
            }
        }
        pos = -1;
        for(int i=n-1; i>=0; i--) {
            if (suffixArray[i].genome == 1) {
                nearest.get(i)[1] = pos;
            }
            else {
                pos = i;
            }
        }

        double count = 0.;
        double progress;
        int total = nearest.size();
        double thres = 0.01;
        System.out.println();
        for(int idx: nearest.keySet()){

            count++;
            progress = count / total;
            if(progress > thres){
                System.out.println((int) (progress*100.) + "% done");
                thres += 0.01;
            }

            int[] idxs = nearest.get(idx);
            int idx_up = idxs[0];
            int idx_down = idxs[1];

            String seq = sequence.substring(suffixArray[idx].index);
            int ind = seq.indexOf(nt);
            if(ind != -1){
                seq = seq.substring(0, ind);
            }

            int max1 = 0;
            if(idx_up > -1){
                String up_seq = sequence.substring(suffixArray[idx_up].index);
                ind = up_seq.indexOf(nt);
                if(ind != -1) {
                    up_seq = up_seq.substring(0, ind);
                }
                max1 = get_match_length(up_seq, seq);
            }

            int max2 = 0;
            if(idx_down > -1){
                String down_seq = sequence.substring(suffixArray[idx_down].index);
                ind = down_seq.indexOf(nt);
                if(ind != -1){
                    down_seq = down_seq.substring(0, ind);
                }
                max2 = get_match_length(down_seq, seq);
            }

            int maxx = Math.max(max1, max2);
            hist.add(maxx);
            if(maxx > this.max_match_len){
                this.max_match_len = maxx;
                this.max_idx = idx;
            }
        }

        this.lcs = sequence.substring(suffixArray[this.max_idx].index, suffixArray[this.max_idx].index + this.max_match_len);
        this.histogram = countOccurrences(hist);
    }

    public int[] countOccurrences(List<Integer> list) {
        int max = Collections.max(list);
        int[] countArray = new int[max + 1];
        for (Integer num : list) {
            countArray[num]++;
        }
        return countArray;
    }

    private List<Integer> get_all_substring(String sequence, String substring) {
        List<Integer> indexes = new ArrayList<>();
        Matcher matcher = Pattern.compile(substring).matcher(sequence);
        while(matcher.find()) {
            indexes.add(matcher.start());
        }
        return indexes;
    }

    public void get_all_substrings(String s1, String s2, String s3){
        this.idxs_seq1 = get_all_substring(s1, this.lcs);
        this.idxs_seq2 = get_all_substring(s2, this.lcs);
        this.idxs_seq3 = get_all_substring(s3, this.lcs);
    }

    public void print(){
        System.out.println("\nMatch Length Histogram:");
        for(int i=0; i<this.histogram.length; i++){
            if(this.histogram[i] > 0){
                System.out.println(i + " " + this.histogram[i]);
            }
        }

        System.out.println("\nThe longest match length: " + this.max_match_len);
        System.out.println("Number of match strings: " + 1);

        System.out.println("\nMatch string: " + this.lcs);
        System.out.println("Description: This sequence comes from [look up entry in .gbff annotation file using the position information below]");

        for(int idx: idxs_seq1){
            String f = this.files[0].split("/")[5];
            System.out.println("\nFasta: " + f.substring(0, f.length() - 4));
            System.out.println("Position: " + (idx+1));
            System.out.println("Strand: " + "forward");
        }

        for(int idx: idxs_seq2){
            String f = this.files[1].split("/")[5];
            System.out.println("\nFasta: " + f.substring(0, f.length() - 4));
            System.out.println("Position: " + (idx+1));
            System.out.println("Strand: " + "forward");
        }

        for(int idx: idxs_seq3){
            String f = this.files[1].split("/")[5];
            System.out.println("\nFasta: " + f.substring(0, f.length() - 4));
            System.out.println("Position: " + (idx+1));
            System.out.println("Strand: " + "forward");
        }
    }
}