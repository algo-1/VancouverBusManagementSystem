import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static List<Stop> getStops(String filename)
    {
        List<Stop> stops = new ArrayList<>();
        try {
            File file = new File(filename).getAbsoluteFile();
            BufferedReader br = new BufferedReader(new FileReader(file));
            int count = 0;
            String line;
            while ((line = br.readLine()) != null) {
                if (count > 1)
                {
                    String[] input = line.trim().split(",");
                    int stopID = Integer.parseInt(input[0]);
                    stops.add(new Stop(stopID));
                }
                count++;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return stops;
    }

    public static List<Edge> getEdges(String filename) throws Exception {
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
                if (count > 2)
                {
                    line = line.replace(" ", "");
                    String[] input = line.trim().split(",");
                    int tripID = Integer.parseInt(input[0]);
                    int stopID = Integer.parseInt(input[3]);
                    if (prevTripID != tripID)
                    {
                        edges.add(new Edge(prevStopID, stopID, cost));
                    }
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
                if (count > 1){
                    String[] input = line.trim().split("\\s+");
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
}
