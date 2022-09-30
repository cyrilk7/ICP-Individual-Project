import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.*;

/**
 * This class represents a route instance
 */
public class Route {
    String airlineIATA, airlineID, sourceIATA, sourceID, destIATA, destID, stops;
    Route parent;
    //static HashMap<String,ArrayList<Airline>> flights = new HashMap<>();
    static HashMap<String, ArrayList<Route>> routes = new HashMap<>();


    /***
     * This constructor instantiates a route object with the following parameters
     * @param airlineIATA
     * @param airlineID
     * @param sourceIATA
     * @param sourceID
     * @param destIATA
     * @param destID
     * @param stops
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

    public Route(String airlineIATA, String airlineID, String sourceIATA, String sourceID, String destIATA, String destID, String stops, Route parent) {
        this.airlineIATA = airlineIATA;
        this.airlineID = airlineID;
        this.sourceIATA = sourceIATA;
        this.sourceID = sourceID;
        this.destIATA = destIATA;
        this.destID = destID;
        this.stops = stops;
        this.parent = parent;
    }


    public void setParent(Route parent){
        this.parent = parent;
    }
//    public Route(String airlineIATA, String airlineID, String sourceIATA, String sourceID, String destIATA, String destID, String codeShare, String stops) {
//        this.airlineIATA = airlineIATA;
//        this.airlineID = airlineID;
//        this.sourceIATA = sourceIATA;
//        this.sourceID = sourceID;
//        this.destIATA = destIATA;
//        this.destID = destID;
//        //this.codeShare = codeShare;
//        this.stops = stops;
//        //this.equipment = equipment;
//    }

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
     * @param csvFile
     */
    public static void getFlights(String csvFile) {
        Scanner routeInputStream = null;

        try {

            routeInputStream = new Scanner(new FileInputStream(csvFile));
            //Airline.readFile("C:/Users/Cyril K/Downloads/airlines.csv");
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


    public static void search (String startIATA, String desIATA){
        Stack<String> frontier = new Stack<>();

        System.out.println("Starting search on sourceIATA: " + startIATA);
        Set<String> explored = new HashSet<> ();

        if (routes.containsKey(startIATA)){
            if(startIATA.equals(desIATA)){
                System.out.println("Found a route!");
                System.exit(0);
            }

            else{
                frontier.add(startIATA);
            }

            while (frontier.size() > 0){
                String poppedValue = frontier.pop();
                explored.add(poppedValue);


                if (routes.containsKey(poppedValue)){
                    for (int i =0; i < routes.get(poppedValue).size(); i++ ){
                        String child = routes.get(poppedValue).get(i).destIATA;
                        if (!explored.contains(child) && !frontier.contains(child)){
                            if(child.equals(desIATA)){
                                System.out.println("Found a route: "+ child);
                                System.exit(0);
                            }
                            frontier.add(child);
                        }
                    }
                }else{
                    System.out.println("Moving to next node");
                    continue;
                }
            }

        }

    }

//    public static ArrayList<Route> getAirline (String IATA){
//        return routes.get(IATA);
//    }

    /**
     * This method reads an input file from the user and uses it to find a route given a start and end destination
     * @param csvFile
     * @return
     */

    public static Route findRoute(String csvFile) {
        Airport.readFile("/C:/Users/Cyril K/Downloads/airports.csv");
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
        String source = list.get(0);
        String destination = list.get(1);
        ArrayList<Airport> sourceAirports = Airport.getAirport(source);

        ArrayList<Airport> destinationAirport = Airport.getAirport(destination);

        for (int k = 0; k < destinationAirport.size(); k++) {
            for (int i = 0; i < sourceAirports.size(); i++) {
                searchTest(sourceAirports.get(i),destinationAirport.get(k));

            }
        }
    return null;

}





        //test(source,destination);


    /**
     * This method finds a direct route to a destination given a start and end destination
     * @param source
     * @param destination
     * @return
     */


    /**
     * This method writes the output route generated to a file
     *
     * @param file
     */
    public static void writeToFile(String file){
        try {
            PrintWriter outPutstream = new PrintWriter(new FileOutputStream(file));
            outPutstream.write(file);



            outPutstream.close();
        } catch (FileNotFoundException f) {
            System.out.println("Problem opening file");
            System.exit(0);
        }

    }


    public static void searchTest (Airport start, Airport destination){
        Stack<String> frontier = new Stack<>();
        Stack<Route> portFrontier = new Stack<>();

        System.out.println("Starting search on sourceIATA: " + start.IATAcode);
        Set<String> explored = new HashSet<> ();

        if (routes.containsKey(start.IATAcode)){
            if(start.IATAcode.equals(destination.IATAcode)){
                System.out.println("Found a route!");
                System.exit(0);
            }

            else{
                frontier.add(start.IATAcode);
                //portFrontier.add();
            }

            while (frontier.size() > 0){
                String poppedValue = frontier.pop();
                explored.add(poppedValue);


                if (routes.containsKey(poppedValue)){
                    for (int i =0; i < routes.get(poppedValue).size(); i++ ){
                        Route child = routes.get(poppedValue).get(i);

                        if (!explored.contains(child.destIATA) && !frontier.contains(child.destIATA)){
                            if(child.destIATA.equals(destination.IATAcode)){
                                System.out.println("Found a route: "+ child);
                                String file = start.name + destination.name;
                                writeToFile(child.toString());
                                System.exit(0);
                            }
                            frontier.add(child.destIATA);
                        }
                    }
                }else{
                    System.out.println("Moving to next node");
                    continue;
                }
            }

        }

    }





    public static void main(String[]args){
        Route.findRoute("C:/Users/Cyril K/Downloads/test.csv/");




    }

}
