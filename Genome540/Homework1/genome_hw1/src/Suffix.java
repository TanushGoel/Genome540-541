public class Suffix implements Comparable<Suffix> {

    int index;
    int rank;
    int next;
    int genome;

    public Suffix(int ind, int r, int nr)
    {
        index = ind;
        rank = r;
        next = nr;
        genome = -1;
    }

    public int compareTo(Suffix s)
    {
        if (rank != s.rank) return Integer.compare(rank, s.rank);
        return Integer.compare(next, s.next);
    }

}