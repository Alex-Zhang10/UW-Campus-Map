package hw8;

import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import org.checkerframework.checker.nullness.qual.*;

/** Parser utility to load the Campus Buildings dataset. */
public class CampusBuildingsParser {

    // No abstract function since it only has static method

    /**
     * Reads the CampusBuildings dataset. Each line of the input file contains a short name,
     * a long name, and x-y coordinates of a building
     *
     * @spec.requires filename is a valid file path
     * @param filename the file that will be read
     * @return a Set of Building objects, each represents a building and its location
     * return null if File is badly formatted
     */
    @SuppressWarnings({"DefaultCharset", "StringSplitter"})
    public static Set<Building> parseData(String filename) {
        Set<Building> buildings = new HashSet<>();
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
                if (tokens.length != 4) {
                    throw new IllegalArgumentException("Line should contain exactly three tab: " + inputLine);
                }

                Coordinates location = (Coordinates) converter.convert(tokens[2] + "," + tokens[3]);
                buildings.add(new Building(tokens[0], tokens[1], location));
            }
            return buildings;
        } catch (IOException e) {
            System.err.println(e.toString());
            e.printStackTrace(System.err);
        } catch (CsvDataTypeMismatchException cdtme) {
            throw new IllegalArgumentException("CSV Data Type Mismatch");
        } catch (CsvConstraintViolationException ccve) {
            throw new IllegalArgumentException("CSV Constraint Violation Exception");
        }
        return buildings;
    }
}
