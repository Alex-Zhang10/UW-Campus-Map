import java.nio.charset.Charset;
import java.util.*;

/**
 * CampusMapViewController is the view/controller of CampusMap MVC. It accepts users' inputs
 * as commands and performs the desired operations from users.
 */
public class CampusMapViewController {
    // No abstract function because it only has static methods

    /**
     * Main method. Accepts client's input and performs the desired operations from
     * users
     * @param args arguments
     */
    public static void main(String[] args) {
        CampusMapModel model = new CampusMapModel();

        Scanner console = new Scanner(System.in, Charset.defaultCharset().name());
        showMenu();

        System.out.println();
        System.out.print("Enter an option ('m' to see the menu): ");
        String command = console.nextLine();
        while (!command.equals("q")) {
            if (command.equals("m")) {
                showMenu();
            } else if (command.equals("r")) {
                System.out.print("Abbreviated name of starting building: ");
                String start = console.nextLine();
                System.out.print("Abbreviated name of ending building: ");
                String end = console.nextLine();
                printResultPath(start, end, model);
            } else if (command.equals("b")) {
                listBuildings(model);
            } else if (command.equals("") || command.startsWith("#")) {
                System.out.println(command);
                command = console.nextLine();
                continue;
            } else {
                System.out.println("Unknown option");
            }
            System.out.println();
            System.out.print("Enter an option ('m' to see the menu): ");
            command = console.nextLine();
        }
    }

    /**
     * Prints out the list of commands to the console
     */
    public static void showMenu() {
        System.out.println("Menu:");
        System.out.println("\tr to find a route");
        System.out.println("\tb to see a list of all buildings");
        System.out.println("\tq to quit");
    }

    /**
     * Show the shortest path between two buildings by printing out the distance
     * the direction, and the destination of each sub-path to the console. Prints out
     * the total distance of the shortest path too.
     * @param start the start building
     * @param end the end building
     * @param model the CampusMapModel that stores information of campus's buildings and paths
     */
    public static void printResultPath(String start, String end, CampusMapModel model) {
        List<String> paths = model.pathToString(start, end);

        // In case if no paths were found
        if (paths == null) {
            System.out.println("no path found"); // not possible
        } else { // prints out the information of the path
            for (String subPath : paths) {
                System.out.println(subPath);
            }
        }
    }

    /**
     * Prints out every building's short name and long name based on alphabetical order
     * of its short name
     * @param model the CampusMapModel that stores information of the campus
     */
    public static void listBuildings(CampusMapModel model) {
        Set<Building> buildingOrdered = model.getBuildings();
        System.out.println("Buildings:");
        for (Building building : buildingOrdered) {
            System.out.println("\t" + building.getShortName() + ": " + building.getLongName());
        }
    }
}
