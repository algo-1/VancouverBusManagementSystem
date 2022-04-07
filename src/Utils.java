import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class Utils {
    static Set<String> stopWords = new HashSet<String>(Arrays.asList("FLAGSTOP", "WB", "NB", "SB", "EB"));

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
                    String stopName = format(input[2]);
                    String stopCode = input[1];
                    String zoneID = input[6];
                    stops.add(new Stop(stopID, stopName, stopCode, zoneID));
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
                    String[] time = input[1].split(":");
                    String hour = time[0];
                    String min = time[1];
                    String sec = time[2];
                    Time arrivalTime = new Time(hour, min, sec);
                    if (Time.isInvalid(arrivalTime))
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

    private static String format(String stopName)
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
