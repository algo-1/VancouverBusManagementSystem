import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
    Map<Integer, List<Pair<Integer, Double>>> map;
    Graph(List<Edge> edges)
    {
        this.map = buildMap(edges);
    }

    public List<Pair<Integer, Double>> getAdjacentStops(int stopID)
    {
        return map.get(stopID);
    }

    public boolean containsStopID(int stopID)
    {
        return this.map.containsKey(stopID);
    }

    public int getStopID(Pair<Integer, Double> value)
    {
        return value.first;
    }

    public double getCost(Pair<Integer, Double> value)
    {
        return value.second;
    }

    private Map<Integer, List<Pair<Integer, Double>>> buildMap(List<Edge> edges)
    {
        Map<Integer, List<Pair<Integer, Double>>> map = new HashMap<>();
        for (Edge edge: edges)
        {
            if (edge != null && edge.from != -1) {
                if (!map.containsKey(edge.from))
                {
                    List<Pair<Integer, Double>> neighbours = new ArrayList<>();
                    neighbours.add(new Pair<Integer, Double>(edge.to, edge.cost));
                    map.put(edge.from, neighbours);
                } else
                {
                    map.get(edge.from).add(new Pair<Integer, Double>(edge.to, edge.cost));
                }
            }
        }

        return map;
    }
}
