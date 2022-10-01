import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


/**
 * This class represents an airport Instance
 * @author Cyril K
 */
public class Airport {
    String airportID, name, city, country, IATAcode, ICAOcode;
    static Map<String, ArrayList<Airport>> airpots = new HashMap<>();


    /**
     * This constructor instantiates an airport object with the following parameters
     *
     * @param AirportID The ID of the airport
     * @param Name      The name of the airport
     * @param City      The city the airport is located
     * @param Country   The country the airport is located
     * @param IATAcode  The IATAcode of the airport
     * @param ICAOcode  The ICAO code of the airport
     */

    public Airport(String AirportID, String Name, String City, String Country, String IATAcode, String ICAOcode) {
        this.airportID = AirportID;
        this.name = Name;
        this.city = City;
        this.country = Country;
        this.IATAcode = IATAcode;
        this.ICAOcode = ICAOcode;
    }


    /**
     * This method reads a csv file. For each line in the file it creates an airport object using these values and stores them in a Hashmap
     *
     * @param csvFile The csv file containing the airport information
     */
    public static void readFile(String csvFile) {
        Scanner inputStream = null;
        try {
            inputStream = new Scanner(new FileInputStream(csvFile));
            while (inputStream.hasNextLine()) {
                String line = inputStream.nextLine();
                String[] list = line.split(",");
                String tempKey = list[2] + ", " + list[3];          //Hashmap Key (Cityname, CountryName)
                if (airpots.containsKey(tempKey)) {
                    ArrayList<Airport> routeList = airpots.get(tempKey);
                    Airport myAirport = new Airport(list[0], list[1], list[2], list[3], list[4], list[5]);
                    routeList.add(myAirport);
                    airpots.put(tempKey, routeList);

                } else {
                    ArrayList<Airport> portList = new ArrayList<>();
                    Airport myAirport = new Airport(list[0], list[1], list[2], list[3], list[4], list[5]);
                    portList.add(myAirport);
                    airpots.put(tempKey, portList);
                    //temp.put(list[0],tempKey);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Problem opening file");
            System.exit(0);
        }
        inputStream.close();

    }


    /**
     * This method returns a list of airpots available from a given city,country
     *
     * @param city The airport city and country name
     * @return
     */
    public static ArrayList<Airport> getAirport(String city) {
        return airpots.get(city);
    }

}



//    public static void main (String[]args){
//        Airport.readFile("/C:/Users/Cyril K/Downloads/airports.csv");
//        System.out.println(Airport.airpots);
//
//
//    }
//}


