package hw8;

import hw3.Graph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * the API of campus map spring framework
 */
@RestController
@CrossOrigin("http://localhost:3000")
public class CampusPathAPI {

    /**
     * the service of campus map spring framework
     */
    @Autowired
    private CampusMapService service;

    /**
     Return the shortest path between given two building names
     * @param start the start building
     * @param end the end building
     * @return a List of all the sub-paths of the shortest path between given two building names
     */
    @GetMapping("/buildingLocation")
    public List<Graph<Coordinates, Double>.Edge> shortestPath(@RequestParam(value="start") String start,
                                                              @RequestParam(value="end") String end) {
        this.service = new CampusMapService();
        return service.findPath(start, end);
    }

    /**
     * Return all the buildings on campus
     * @return a Set of all the Buildings on campus
     */
    @GetMapping("/getBuildings")
    public Set<Building> getBuildings() {
        this.service = new CampusMapService();
        return service.getBuildings();
    }
}
