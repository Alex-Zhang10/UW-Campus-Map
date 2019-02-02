import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import hw3.Graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.checkerframework.checker.nullness.qual.*;

/** Parser utility to load the Campus Paths dataset. */
public class CampusPathsParser {
    // No abstract function because it only has static methods

    /**
     * Reads the CampusPaths dataset. Each line of the input file contains two x-y coordinates
     * and the distance between them
     *
     * @spec.requires filename is a valid file path
     * @param filename the file that will be read
     * @return Return a Graph with information of every path on campus
     * return null if tsv file is not formatted correctly
     */
    // "KeyFor": location1 and location2 are guaranteed to be in the Graph
    // because I call addNode on each of them right before
    @SuppressWarnings({"DefaultCharset", "StringSplitter", "KeyFor"})
    public static Graph<Coordinates, Double> parseData(String filename) {
        Graph<Coordinates, Double> campusMap = new Graph<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filename));

            // Construct the data <shortname, longname, x, y>
            String inputLine;
            CoordinateConverter converter = new CoordinateConverter();
            reader.readLine(); // omit the first line
            while ((inputLine = reader.readLine()) != null) {

                // Ignore comment lines.
                if (inputLine.startsWith("#")) {
                    continue;
                }

                // Parse the data, stripping out quotation marks and throwing
                // an exception for malformed lines.
                inputLine = inputLine.replace("\"", "");
                String[] tokens = inputLine.split("\t");
                if (tokens.length != 3) {
                    throw new IllegalArgumentException("Line should contain exactly two tab: " + inputLine);
                }

                Coordinates location1 = (Coordinates) converter.convert(tokens[0]);
                Coordinates location2 = (Coordinates) converter.convert(tokens[1]);
                Double distance = Double.valueOf(tokens[2]);

                // Add the parsed data to the Graph.
                campusMap.addNode(location1);
                campusMap.addNode(location2);
                campusMap.addEdge(location1, location2, distance);
            }
            return campusMap;
        } catch (IOException e) {
            System.err.println(e.toString());
            e.printStackTrace(System.err);
        } catch (CsvDataTypeMismatchException cdtme) {
            throw new IllegalArgumentException("CSV Data Type Mismatch");
        } catch (CsvConstraintViolationException ccve) {
            throw new IllegalArgumentException("CSV Constraint Violation Exception");
        }
        return campusMap;
    }
}
