import java.util.*;

public class Dijkstra {
    public static Deque<Pair<Integer, Double>> dijkstra(
            int sourceStopID,
            int endStopID,
            Set<Integer> stopIDs,
            Graph graph
    ) throws Exception
    {
        Map<Integer, Double> lowestCosts = new HashMap<Integer, Double>();

        Map<Integer, Integer> prevStopIDs = new HashMap<Integer, Integer>();

        // set the cost from the source to all other intersections to the max value
        for (int stopID: stopIDs)
        {
            lowestCosts.put(stopID, Double.MAX_VALUE);
        }

        lowestCosts.put(sourceStopID, 0.0);

        PriorityQueue<Pair<Integer, Double>> queue = new PriorityQueue<>();

        queue.add(new Pair<Integer, Double>(sourceStopID, 0.0));
        while (!queue.isEmpty()) {
            Pair<Integer, Double> minCostStop = queue.poll();
            int minStopID = graph.getStopID(minCostStop);
            if (minStopID == endStopID)
            {
                break;
            }
            if (graph.containsStopID(minStopID)) {
                List<Pair<Integer, Double>> neighbours = graph.getAdjacentStops(minStopID);
                for (Pair<Integer, Double> neigh : neighbours) {
                    int neighStopID = graph.getStopID(neigh);
                    double neighCost = graph.getCost(neigh);
                    double cost = lowestCosts.get(minStopID) + neighCost;
                    if (cost < lowestCosts.get(neighStopID)) {
                        lowestCosts.put(neighStopID, cost);
                        queue.add(new Pair<Integer, Double>(neighStopID, cost));
                        prevStopIDs.put(neighStopID, minStopID);
                    }
                }
            }
        }

        Deque<Pair<Integer, Double>> result = new LinkedList<>();

        // if path does not exist return empty list
        if (!prevStopIDs.containsKey(endStopID) && endStopID != sourceStopID)
        {
            return result;
        }

        // construct path
        int currStopID = endStopID;
        while (currStopID != sourceStopID)
        {
            int prev = prevStopIDs.get(currStopID);
            double cost = (lowestCosts.get(currStopID) - lowestCosts.get(prev));
            result.addFirst(new Pair<Integer, Double>(currStopID, cost));
            currStopID = prev;
        }
        result.addFirst(new Pair<Integer, Double>(sourceStopID, 0.0));

        return result;
    }
}
