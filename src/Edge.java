public class Edge implements Comparable<Edge>{
    int from;
    int to;
    double cost;
    Time arrivalTime;
    Integer tripID = -1;
    Edge(int from, int to, double cost)
    {
        this.from = from;
        this.to = to;
        this.cost = cost;
    }
    Edge(int from, int to, double cost, Time arrivalTime, int tripID)
    {
        this.from = from;
        this.to = to;
        this.cost = cost;
        this.arrivalTime = arrivalTime;
        this.tripID = tripID;
    }

    @Override
    public int compareTo(Edge other) {
        return this.tripID.compareTo(other.tripID);
    }
}
