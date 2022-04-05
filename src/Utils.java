import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class Utils {
    public static List<Stop> getStops(String filename)
    {
        List<Stop> stops = new ArrayList<>();
        try
        {
            File file = new File(filename).getAbsoluteFile();
            BufferedReader br = new BufferedReader(new FileReader(file));
            int count = 0;
            String line;
            while ((line = br.readLine()) != null)
            {
                if (count > 0)
                {
                    String[] input = line.trim().split(",");
                    int stopID = Integer.parseInt(input[0]);
                    String stopName = input[2];
                    stops.add(new Stop(stopID, stopName));
                }
                count++;
            }
        } catch(Exception e)
        {
            e.printStackTrace();
        }

        return stops;
    }

    public static List<Edge> getEdges(String filename) throws Exception
    {
        if (filename.equals("stop_times.txt"))
        {
            return parseStopTimes(filename);
        }
        else if (filename.equals("transfers.txt"))
        {
            return parseTransfers(filename);
        }
        else throw new Exception("Invalid File name.");
    }

    private static List<Edge> parseStopTimes(String filename)
    {
        List<Edge> edges = new ArrayList<>();
        double cost = 1.0;
        int prevStopID = -1;
        int prevTripID = -1;
        try {
            File file = new File(filename).getAbsoluteFile();
            BufferedReader br = new BufferedReader(new FileReader(file));
            int count = 0;
            String line;
            while ((line = br.readLine()) != null)
            {
                if (count > 0)
                {
                    line = line.replace(" ", "");
                    String[] input = line.trim().split(",");
                    int tripID = Integer.parseInt(input[0]);
                    int stopID = Integer.parseInt(input[3]);
                    String arrivalTime = input[1];
                    if (isInvalid(arrivalTime))
                    {
                        continue;
                    }
                    if (prevTripID == tripID)
                    {
                        edges.add(new Edge(prevStopID, stopID, cost, arrivalTime, tripID));
                    }
                    else edges.add(new Edge(-1, stopID, 0.0, arrivalTime, tripID));
                    prevStopID = stopID;
                    prevTripID = tripID;
                }
                count++;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return edges;
    }

    private static List<Edge> parseTransfers(String filename)
    {
        List<Edge> edges = new ArrayList<>();
        double cost = 2.0;
        try
        {
            File file = new File(filename).getAbsoluteFile();
            BufferedReader br = new BufferedReader(new FileReader(file));
            int count = 0;
            String line;
            while ((line = br.readLine()) != null) {
                if (count > 0)
                {
                    line = line.replace(" ", "");
                    String[] input = line.trim().split(",");
                    int from = Integer.parseInt(input[0]);
                    int to = Integer.parseInt(input[1]);
                    int transferType = Integer.parseInt(input[2]);
                    if (transferType == 2)
                    {
                        int minTransferTime = Integer.parseInt(input[3]);
                        cost = minTransferTime / 100.00;
                    }
                    edges.add(new Edge(from, to, cost));
                }
                count++;
            }
        } catch(Exception e)
        {
            e.printStackTrace();
        }
        return edges;
    }

    private static boolean isInvalid(String arrivalTime)
    {
        String[] time = arrivalTime.split(":");
        return Integer.parseInt(time[0]) >= 24 || Integer.parseInt(time[1]) >= 60 || Integer.parseInt(time[2]) >= 60;
    }

    public static boolean isEqual(String arrivalTime, String stopArrivalTime)
    {
        if (stopArrivalTime.equals(""))
        {
            return false;
        }
        String[] time1 = arrivalTime.split(":");
        String[] time2 = stopArrivalTime.split(":");

        return Integer.parseInt(time1[0]) == Integer.parseInt(time2[0]) &&
                Integer.parseInt(time1[1]) == Integer.parseInt(time2[1]) &&
                Integer.parseInt(time1[2]) == Integer.parseInt(time2[2]);
    }

    public static String format(String stopName, Set<String> stopWords)
    {
        String[] word = stopName.split(" ", 3);
        if (stopWords.contains(word[0]))
        {
            if (stopWords.contains(word[1]))
            {
                return word[2] + " " + word[0] + " " + word[1];
            }
            if (word.length == 3) return word[1] + " " + word[2] + " " + word[0];
            return word[1] + " " + word[0];
        }
        return stopName;
    }
}
