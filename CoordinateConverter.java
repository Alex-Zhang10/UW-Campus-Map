package hw8;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * CoordinateConverter creates a new Coordinates object based on a given String
 */
public class CoordinateConverter extends AbstractBeanField<String> {

    // Abstract function:
    // Not an ADT because it does not represents any data. It only converts
    // a String to a Coordinates

    /**
     * Creates a new Coordinate object based on the given String
     * @param value the String that is going to be converted to a Coordinate
     * @spec.requires "value" is in the form "double, double"
     * @return a Coordinate object that contains the information in the given String
     * @throws CsvDataTypeMismatchException if csv file isn't the correct type
     * @throws CsvConstraintViolationException if csv constraint is violated
     */
    @Override
    // e.getMessage() can possibly return null
    // but since the staff provides it I can't fix it
    // value.split has a warning
    // but since the staff provides it I can't fix it and it's working fine
    // other suggestions didn't work
    @SuppressWarnings({"argument.type.incompatible", "StringSplitter"})
    protected Object convert(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        try {
            String[] parts = value.split(",");
            return new Coordinates(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]));
        } catch (RuntimeException e) {
            throw new CsvDataTypeMismatchException(e.getMessage());
        }
    }
}