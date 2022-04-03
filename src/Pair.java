public class Pair<E, T extends Comparable<? super T>> implements Comparable<Pair<E, T>> {
    E first;
    T second;

    Pair(E first, T second)
    {
        this.first = first;
        this.second = second;
    }

    @Override
    public int compareTo(Pair<E, T> other)
    {
        return this.second.compareTo(other.second);
    }
}
