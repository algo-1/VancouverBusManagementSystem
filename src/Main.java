import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);

        List<Stop> stops = Utils.getStops("stops.txt");
        Set<Integer> stopIDs = stops.stream().map(s -> s.stopID).collect(Collectors.toSet());

        List<Edge> edges = Utils.getEdges("stop_times.txt");
        edges.addAll(Utils.getEdges("transfers.txt"));
        Graph graph = new Graph(edges);

        TernarySearchTree tst = new TernarySearchTree();

        for (Stop stop : stops)
        {
            tst.insert(stop.stopName, stop);
        }

        // CLI 
        int select = 0;
        do {
            System.out.print("Hello!\nEnter 1, 2, or 3 to select between shortest paths, " +
                    "bus stop search or arrival time search respectively.\nEnter a number: ");
            if (in.hasNextInt())
            {
                select = in.nextInt();
            } else in.next();
        } while (select < 1 || select > 3);

        if (select == 1)
        {
            // Q1
            in = new Scanner(System.in);
            outerloop:
            while(true)
            {
                do
                {
                    System.out.print("Enter a start bus stop id: ");
                    if (in.hasNextInt()) break;
                    if (in.next().equalsIgnoreCase("quit")) break outerloop;
                } while (true);
                int sourceStopID = in.nextInt();

                do
                {
                    System.out.print("Enter an end bus stop id: ");
                    if (in.hasNextInt()) break;
                    if (in.next().equalsIgnoreCase("quit")) break outerloop;
                } while (true);
                int endStopID = in.nextInt();

                System.out.printf("start stop_id = %d stop stop_id = %d\n", sourceStopID, endStopID);
                boolean inputValid = true;
                if (!stopIDs.contains(sourceStopID))
                {
                    System.out.printf("your start stop id, %d is an invalid bus stop id\n", sourceStopID);
                    inputValid = false;
                }
                if (!stopIDs.contains(endStopID))
                {
                    System.out.printf("your end stop id, %d is an invalid bus stop id\n", endStopID);
                    inputValid = false;
                }
                if (!inputValid) continue;

                Deque<Pair<Integer, Double>> shortestPath = Dijkstra.dijkstra(sourceStopID, endStopID, stopIDs, graph);
                if (shortestPath.size() == 0) System.out.printf("There exists no path between stop %d and stop %d\n", sourceStopID, endStopID);
                else
                {
                    for (Pair<Integer, Double> pair : shortestPath)
                    {
                        System.out.printf("stop_id = %d cost = %f\n", pair.first, pair.second);
                    }
                }
            }
        }
        else if (select == 2)
        {
            // Q2
            in = new Scanner(System.in);
            boolean done = false;
            do {
                String busStop = "";
                do
                {
                    System.out.print("Enter part of a bus stop name: ");
                    busStop = in.nextLine().trim().toUpperCase();
                    if (busStop.equalsIgnoreCase("quit")) done = true;

                } while (busStop.length() == 0);

                if (!busStop.equalsIgnoreCase("quit"))
                {
                    List<Stop> matches = tst.find(busStop);
                    if (matches == null) System.out.println("no matches found.");
                    else
                    {
                        for (Stop stop : matches)
                        {
                            System.out.printf("stop name = %s, stop id = %d, stop code = %s zone id = %s\n",
                                    stop.stopName, stop.stopID, stop.stopCode, stop.zoneID);
                        }
                    }

                }

            } while (!done);
        } else {
            // Q3
            in = new Scanner(System.in);
            do {
                System.out.print("Enter an arrival time in format hh:mm:ss: ");
                String input = in.next();
                if (input.equalsIgnoreCase("quit")) break;
                if (Time.isInvalidFormat(input)){
                    System.out.println("Invalid time format.");
                    continue;
                }
                String[] time = input.split(":");
                Time arrivalTime = new Time(time[0], time[1], time[2]);
                if(Time.isInvalid(arrivalTime))
                {
                    System.out.println("Invalid time.");
                    continue;
                }
                List<Edge> result = edges.stream()
                        .filter(edge -> Time.isEqual(arrivalTime, edge.arrivalTime))
                        .sorted()
                        .collect(Collectors.toList());

                for (Edge edge : result) {
                    System.out.printf("trip_id = %d, stop_id = %d, arrival time = %s\n", edge.tripID, edge.to, edge.arrivalTime);
                }
            } while (true);
        }

    }
}
