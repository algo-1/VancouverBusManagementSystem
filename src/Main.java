import java.util.Deque;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);

        List<Stop> stops = Utils.getStops("stops.txt");
        List<Integer> stopIDs = stops.stream().map(s -> s.stopID).collect(Collectors.toList());

        List<Edge> edges = Utils.getEdges("stop_times.txt");
        edges.addAll(Utils.getEdges("transfers.txt"));
        Graph graph = new Graph(edges);

        // Q1
        do {
            System.out.print("Enter a start stop_id: ");
            if (!in.hasNextInt()) break;
            int sourceStopID = in.nextInt();
            System.out.print("Enter a stop stop_id: ");
            if (!in.hasNextInt()) break;
            int endStopID = in.nextInt();
            System.out.printf("start stop_id = %d stop stop_id = %d\n", sourceStopID, endStopID);
            Deque<Pair<Integer, Double>> shortestPath = Dijkstra.dijkstra(sourceStopID, endStopID, stopIDs, graph);
            for (Pair<Integer, Double> pair : shortestPath) {
                System.out.printf("stop_id = %d cost = %f\n", pair.first, pair.second);
            }
        } while (true);


        // Q2


        // Q3
        in = new Scanner(System.in);
        do {
            System.out.print("Enter an arrival time in format hh:mm:ss: ");
            String arrivalTime = in.next();
            if (arrivalTime.equals("quit")) break;

            List<Edge> result = edges.stream()
                    .filter(edge -> Utils.isEqual(arrivalTime, edge.arrivalTime))
                    .sorted()
                    .collect(Collectors.toList());

            for (Edge edge : result) {
                System.out.printf("trip_id = %d, stop_id = %d, arrival time = %s\n", edge.tripID, edge.to, edge.arrivalTime);
            }
        } while (true);

    }
}
