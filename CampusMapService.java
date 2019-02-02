import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * The Service part of my Spring framework of CampusMap
 */
@Service
public class CampusMapService {

    /**
     * the CampusMapModel that stores information
     */
    private CampusMapModel model;

    /**
     * Constructor
     * @spec.effects constructs a service
     */
    public CampusMapService() {
        model = new CampusMapModel();
    }

    /**
     * Return the shortest path between given two building names
     * @param start the start building
     * @param end the end building
     * @return a List of all the sub-paths of the shortest path between given two building names
     */
    public List<Graph<Coordinates, Double>.Edge> findPath(String start, String end) {
        return model.findPath(start, end);
    }

    /**
     * Return all the buildings on campus
     * @return a Set of all the Buildings on campus
     */
    public Set<Building> getBuildings() {
        return model.getBuildings();
    }
}

