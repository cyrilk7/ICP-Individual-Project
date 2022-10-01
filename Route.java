
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.*;

/**
 * This class represents a route instance
 * @author Cyril K
 */

public class Route {
    String airlineIATA, airlineID, sourceIATA, sourceID, destIATA, destID, stops;

    static HashMap<String, ArrayList<Route>> routes = new HashMap<>();
    static HashMap<String, ArrayList<String>> flights = new HashMap<>();
    static HashMap<String, String> parents = new HashMap<>();


    /***
     * This constructor instantiates a route object with the following parameters
     * @param airlineIATA Unique IATA code for an airline
     * @param airlineID    Unique ID for an airline
     * @param sourceIATA   IATA code for source airport
     * @param sourceID      ID for source airport
     * @param destIATA      IATA code for destination airport
     * @param destID        ID for destination airport
     * @param stops         Number of stops flight makes on this route
     */
    public Route(String airlineIATA, String airlineID, String sourceIATA, String sourceID, String destIATA, String destID, String stops) {
        this.airlineIATA = airlineIATA;
        this.airlineID = airlineID;
        this.sourceIATA = sourceIATA;
        this.sourceID = sourceID;
        this.destIATA = destIATA;
        this.destID = destID;
        this.stops = stops;
    }



    /**
     * This method converts a route object to a string
     * @return
     */
    public String toString(){
        String myStr = this.airlineIATA + " from " + this.sourceIATA + " to " + this.destIATA + " "  + this.stops + " stops";
        return myStr;
    }

    /**
     * This method reads the routes file and adds the list of route objects to a HashMap
     * @param csvFile The routes file being read
     */
    public static void getFlights(String csvFile) {
        Scanner routeInputStream = null;

        try {

            routeInputStream = new Scanner(new FileInputStream(csvFile));

            while (routeInputStream.hasNextLine()) {
                String line = routeInputStream.nextLine();
                String[] cur = line.split(",");
                if (routes.containsKey(cur[2])){
                    ArrayList<Route> routeList = routes.get(cur[2]);
                    Route flightRoute = new Route(cur[0],cur[1],cur[2],cur[3],cur[4],cur[5],cur[7]);
                    routeList.add(flightRoute);
                    routes.put(cur[2],routeList);


                }
                else{

                    ArrayList<Route> routeList = new ArrayList<>();
                    Route flightRoute = new Route(cur[0],cur[1],cur[2],cur[3],cur[4],cur[5],cur[7]);
                    routeList.add(flightRoute);
                    routes.put(cur[2],routeList);
                        }

                    }

        } catch (FileNotFoundException e) {
            System.out.println("Problem opening file");
            System.exit(0);
        }
        routeInputStream.close();


    }


    /**
     * This method reads the routes csv file and used the values stored to create a hashmap for the given airline codes and their routes
     * @param csvFile The routes csv file being read
     */
    public static void getFlightCodes(String csvFile) {
        Scanner routeInputStream = null;

        try {

            routeInputStream = new Scanner(new FileInputStream(csvFile));

            while (routeInputStream.hasNextLine()) {
                String line = routeInputStream.nextLine();
                String[] cur = line.split(",");
                String key = cur[2] + "," + cur[4];
                if (flights.containsKey(key)){
                    ArrayList<String> flightList = flights.get(key);
                    flightList.add(cur[0]);
                    flights.put(key,flightList);


                }
                else{
                    ArrayList<String> flightList = new ArrayList<>();
                    flightList.add(cur[0]);
                    flights.put(key,flightList);
                }

            }

        } catch (FileNotFoundException e) {
            System.out.println("Problem opening file");
            System.exit(0);
        }
        routeInputStream.close();


    }

        /**
     * This method reads an input file from the user and uses it to find a route given a start and end destination
     * @param csvFile The input file from the user being read
     * @return
     */

    public static Route findRoute(String csvFile) {
        Airport.readFile("/C:/Users/Cyril K/Downloads/airports.csv");           //The file for this stream is the airports csv file
        Scanner inputStream = null;
        ArrayList<String> list = new ArrayList<>();
        try {

            inputStream = new Scanner(new FileInputStream(csvFile));
            while (inputStream.hasNextLine()) {
                String line = inputStream.nextLine();
                list.add(line);
            }

        } catch (FileNotFoundException e) {
            System.out.println("Problem opening file");
            System.exit(0);
        }
        inputStream.close();
        getFlights("C:/Users/Cyril K/Downloads/routes.csv");
        getFlightCodes("C:/Users/Cyril K/Downloads/routes.csv");
        String source = list.get(0);
        String destination = list.get(1);

        String[] tempsource = source.split(", ");
        String[] tempdestination = destination.split(", ");


        ArrayList<Airport> sourceAirports = Airport.getAirport(source);

        ArrayList<Airport> destinationAirport = Airport.getAirport(destination);

        if (Airport.getAirport(source) != null && Airport.getAirport(destination) != null) {
            ArrayList path = null;
            ArrayList<String> flightPath = new ArrayList<>();
            for (int k = 0; k < destinationAirport.size(); k++) {
                for (int i = 0; i < sourceAirports.size(); i++) {
                    path = search(sourceAirports.get(i), destinationAirport.get(k));
                }
            }
            for (int j = 0; j < path.size()-1; j++) {
                String key = path.get(j) + "," + path.get(j+1);
                if (flights.containsKey(key)) {
                    String current = flights.get(path.get(j) + "," + path.get(j+1)).get(0);
                    flightPath.add(current);
                }
            }

            if (path != null )
                writeToFile(tempsource[0], tempdestination[0], path, flightPath);
            else
                System.out.println("Couldn't find a route");
      }

        else{
            System.out.println("This city has no airports stored here");
        }
    return null;

}












    /**
     * This method writes the output route to a file
     * @param start The city of the source airport
     * @param dest The city of the destination airport
     */
    public static void writeToFile(String start, String dest, ArrayList path, ArrayList flightPath){
        try {
            PrintWriter outPutstream = new PrintWriter(new FileOutputStream(start +"-"+ dest +"_output.txt"));

            int count = 0;

            while (count < path.size()-1){
                outPutstream.println(flightPath.get(count) + " From " + path.get(count) + " to " + path.get(count+1) + ", stops = 0");
                count++;
            }

            int size = path.size()-1;
            outPutstream.println("Total flights = " + size);
            outPutstream.println("Total Additional stops = 0");



            outPutstream.close();
        } catch (FileNotFoundException f) {
            System.out.println("Problem opening file");
            System.exit(0);
        }

    }

    /**
     * This method, given a start and end destination, searches for a valid route
     * @param start The start airport object
     * @param destination The destination airport object
     * @return
     */
    public static ArrayList<String> search(Airport start, Airport destination) {


        Queue<String> frontier = new LinkedList<>();
        HashSet<String> explored = new HashSet<>();
        frontier.add(start.IATAcode);
        parents.put(start.IATAcode, null);

        while (!frontier.isEmpty()) {
            String poppedValue = frontier.remove();
            explored.add(poppedValue);
            if (routes.containsKey(poppedValue)) {
                for (int i=0; i < routes.get(poppedValue).size(); i++) {
                    Route child = routes.get(poppedValue).get(i);

                    if (!frontier.contains(child.destIATA) && !explored.contains(child.destIATA)) {
                        if (!parents.containsKey(child.destIATA)) {
                            parents.put(child.destIATA, poppedValue);
                        }
                        if (child.destIATA.equals(destination.IATAcode)) {
                            return solutionPath(child.destIATA);
                        }
                        frontier.add(child.destIATA);
                    }
                }
            }
        }
        return null;
    }



    /**
     * This method returns the route, i.e paths taken to get to the specified destination
     * @param destinationIATA The IATAcode of the destination airport
     * @return
     */
    public static ArrayList<String> solutionPath(String destinationIATA) {

        ArrayList<String> path = new ArrayList<>();
        path.add(destinationIATA);
        String current = destinationIATA;

        while (parents.containsKey(current)) {
            current = parents.get(current);
            if (current == null) {
                break;
            }
            else
                path.add(current);
        }

        Collections.reverse(path);
        return path;
    }



    public static void main(String[]args){
        Route.findRoute("C:/Users/Cyril K/Downloads/test.csv/");



    }

}
