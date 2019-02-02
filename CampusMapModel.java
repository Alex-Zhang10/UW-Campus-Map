import org.checkerframework.checker.initialization.qual.UnknownInitialization;
import org.checkerframework.checker.nullness.qual.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * CampusMapModel is the model of CampusMap MVC. It stores information about the buildings
 * on campus and all the possible paths on campus. It lets clients to find the shortest path
 * between two buildings and its direction
 */
public class CampusMapModel {

    /** the collection of all paths on campus */
    private Graph<Coordinates, Double> campusMap;

    /** the collection of all buildings in alphabetical order */
    private Set<Building> buildings;

    /** the start point client assigns through view/controller */
    private Building start = new Building("", "", new Coordinates(0, 0));

    /** the end point client assigns through view/controller */
    private Building end = new Building("", "", new Coordinates(0, 0));

    // Abstract Function:
    // Each CampusMapModel = {a Graph represents the location and length of all the possible paths,
    //                        a Set of Buildings represents the name and location of all the buildings,
    //                        a Building represents the start point and a Building represents the end
    //                        point of the shortest path the client tries to find}
    // Rep Invariant:
    // None of the fields can be null

    /**
     * Constructs a new CampusMapModel that contains the paths information in "campus_paths.tsv"
     * and the buildings information in "campus_buildings.tsv"
     * @spec.effects Constructs a new CampusMapModel that contains the paths information in
     * "campus_paths.tsv" and the buildings information in "campus_buildings.tsv"
     */
    public CampusMapModel() {
        campusMap = CampusPathsParser.parseData("src/main/java/hw8/data/campus_paths.tsv");
        Set<Building> buildingsOrdered = new TreeSet<>(new Comparator<Building>() {
            @Override
            public int compare(Building b1, Building b2) {
                return b1.getShortName().compareTo(b2.getShortName());
            }
        });
        buildingsOrdered.addAll(CampusBuildingsParser.parseData("src/main/java/hw8/data/campus_buildings.tsv"));
        buildings = buildingsOrdered;
    }

    /** Check if the rep invariant holds */
    private void checkRep() {
        assert (campusMap != null);
        assert (buildings != null);
    }

    /**
     * Return a List of Graph.Edges representing the path from start to destination
     * with the lowest weight
     * @param start the starting point
     * @param destination the goal start wants to reach
     * @spec.requires start and destination are buildings on campus
     * @return a List of Graph.Edges representing the path from start to destination
     * with the lowest weight
     */
    // "minDestination" is guaranteed to be in the graph since the it is retrieved from the children
    // of some nodes in the graph
    // and the cases where "start" and/or "destination" is not in the graph are handled beforehand
    @SuppressWarnings("keyfor")
    public @Nullable List<Graph<Coordinates, Double>.Edge> findPath(String start,
                                                                    String destination) {
        List<Graph<Coordinates, Double>.Edge> shortestPath = new ArrayList<>();

        // check if start and destination are both names of buildings
        // if not, return Edges containing negative lengths
        boolean startExists = false;
        boolean destinationExists = false;
        Coordinates startLocation = new Coordinates(0,0);
        Coordinates destinationLocation = new Coordinates(0,0);
        for (Building building : this.buildings) {
            if (building.equalsName(start)) {
                this.start = building;
                startExists = true;
                startLocation = building.getLocation();
            }
            if (building.equalsName(destination)) {
                this.end = building;
                destinationExists = true;
                destinationLocation = building.getLocation();
            }
        }
        if (!startExists || !destinationExists) {
            if (!startExists) {
                shortestPath.add(0, campusMap.new Edge(new Coordinates(0,0), new Coordinates(0,0),
                            -1.0));
            }
            if (!destinationExists) {
                if (shortestPath.size() == 0) {
                    shortestPath.add(0, campusMap.new Edge(new Coordinates(0,0), new Coordinates(0,0),
                            0.0));
                }
                shortestPath.add(1, campusMap.new Edge(new Coordinates(0,0), new Coordinates(0,0),
                                -1.0));
            }
            return shortestPath;
        }

        // base case: the start node
        // Do no more checking if start is the destination
        if (start.equals(destination)) {
            return shortestPath;
        }

        PriorityQueue<List<Graph<Coordinates, Double>.Edge>> active =
                new PriorityQueue<>(new Comparator<List<Graph<Coordinates, Double>.Edge>>() {
                    @Override
                    public int compare(List<Graph<Coordinates, Double>.Edge> path1,
                                       List<Graph<Coordinates, Double>.Edge> path2) {
                        if (path1.equals(path2)) {
                            return 0;
                        } else {
                            double totalWeight1 = 0.0;
                            for (Graph<Coordinates, Double>.Edge path : path1) {
                                totalWeight1 += path.getLabel();
                            }
                            double totalWeight2 = 0.0;
                            for (Graph<Coordinates, Double>.Edge path : path2) {
                                totalWeight2 += path.getLabel();
                            }
                            if (totalWeight1 > totalWeight2) {
                                return 1;
                            } else {
                                return -1;
                            }
                        }
                    }
                });

        Set<Coordinates> finished = new HashSet<>();

        // If start is not the destination, add the path from start to each of its children
        // to active
        for (Graph<Coordinates, Double>.Edge pathFromStart : campusMap.getEdgesFromANode(startLocation)) {
            List<Graph<Coordinates, Double>.Edge> newPath = new ArrayList<>();
            newPath.add(pathFromStart);
            active.add(newPath);
        }
        finished.add(startLocation);

        // other cases:
        while (!active.isEmpty()) {
            // minPath is the lowest-cost path in active and is the
            // minimum-cost path to some node
            List<Graph<Coordinates, Double>.Edge> minPath = active.remove();
            Coordinates minDestination = minPath.get(minPath.size() - 1).getDestination();

            if (minDestination.equals(destinationLocation)) {
                return minPath;
            }

            // If the shortest path from start to minDestination is not found,
            // find it
            if (!finished.contains(minDestination)) {
                for (Graph<Coordinates, Double>.Edge pathFromMinDestination :
                        campusMap.getEdgesFromANode(minDestination)) {
                    // If we don't know the minimum-cost path from start to child,
                    // examine the path we've just found
                    List<Graph<Coordinates, Double>.Edge> newPath = new ArrayList<>();
                    newPath.addAll(minPath);
                    newPath.add(pathFromMinDestination);
                    active.add(newPath);
                }
                finished.add(minDestination);
            }
        }
        return null;
    }

    /**
     * Convert each sub-path of the shortest path to a String containing the distance,
     * the direction, and the destination of each sub-path.
     * @param start the start building
     * @param end the end building
     * @return a List of Strings, each represents a sub-path of the shortest path between
     * start and end. The first element in the List is the path title and the last element in
     * the List is the total distance of the path
     */
    public @Nullable List<String> pathToString(String start, String end) {
        List<Graph<Coordinates, Double>.Edge> paths = this.findPath(start, end);

        List<String> stringPath = new ArrayList<>();

        // In case if no paths were found
        if (paths == null) {
            return null;
        } else { // check if there are unknown buildings, represented by -1.0 length
            boolean known = true;
            if (paths.size() >= 1) {
                if (paths.get(0).getLabel() == -1.0) {
                    stringPath.add("Unknown building: " + start);
                    known = false;
                }
                if (paths.size() >= 2) {
                    if (paths.get(1).getLabel() == -1.0) {
                        if (!start.equals(end)) {
                            stringPath.add("Unknown building: " + end);
                        }
                        known = false;
                    }
                }
            }
            if (known) {
                Building startBuilding = this.getStart();
                Building endBuilding = this.getEnd();

                stringPath.add("Path from " + startBuilding.getLongName() + " to "
                        + endBuilding.getLongName() + ":");
                double totalCost = 0.0;
                for (Graph<Coordinates, Double>.Edge subPath : paths) {
                    totalCost += subPath.getLabel();
                    String direction = this.findDirection(subPath);
                    stringPath.add("\tWalk " + String.format("%.0f", subPath.getLabel()) +
                            " feet " + direction + " to " + String.format("(%.0f, ",
                            subPath.getDestination().getX()) + String.format("%.0f)",
                            subPath.getDestination().getY()));
                }
                stringPath.add("Total distance: " + String.format("%.0f", totalCost)
                        + " feet");
            }
        }
        return stringPath;
    }

    /**
     * Find the direction of the given path based on the eight sectors for
     * classifying directions, with origin on the top-left corner
     * @param path the path whose direction is to be found
     * @return return a String representing the direction of the given path based on the eight sectors for
     *      * classifying directions, with origin on the top-left corner
     */
    public String findDirection(Graph<Coordinates, Double>.Edge path) {
        double changeInX = path.getDestination().getX() - path.getStart().getX();
        double changeInY = path.getDestination().getY() - path.getStart().getY();
        double angle = Math.atan2(-changeInY, changeInX);
        if (-Math.PI / 8 <= angle && angle <= Math.PI / 8) {
            return "E";
        } else if (Math.PI / 8 <= angle && angle <= 3 * Math.PI / 8) {
            return "NE";
        } else if (3 * Math.PI / 8 <= angle && angle <= 5 * Math.PI / 8) {
            return "N";
        } else if (5 * Math.PI / 8 <= angle && angle <= 7 * Math.PI / 8) {
            return "NW";
        } else if (-7 * Math.PI / 8 >= angle || angle >= 7 * Math.PI / 8) {
            return "W";
        } else if (-7 * Math.PI / 8 <= angle && angle <= -5 * Math.PI / 8) {
            return "SW";
        } else if (-5 * Math.PI / 8 <= angle && angle <= -3 * Math.PI / 8) {
            return "S";
        } else {
            return "SE";
        }
    }

    /**
     * Getter method, return the start building client assigns through view/controller
     * @return return the start building client assigns through view/controller
     */
    public Building getStart() {
        return this.start;
    }

    /**
     * Getter method, return the destination building client assigns through view/controller
     * @return return the destination building client assigns through view/controller
     */
    public Building getEnd() {
        return this.end;
    }

    /**
     * Getter method. Return an unmodifiable set of Buildings (in alphebetical order)
     * this contains
     * @return return an unmodifiable set of Buildings (in alphebetical order)
     *      * this contains
     */
    public Set<Building> getBuildings() {
        return Collections.unmodifiableSet(this.buildings);
    }
}
