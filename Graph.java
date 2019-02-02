import java.util.*;

import org.checkerframework.checker.nullness.qual.*;
import org.checkerframework.checker.initialization.qual.*;
import org.checkerframework.checker.units.qual.K;

/**
 * Graph is a mutable directed labeled multi-graph containing a collection of nodes
 * and Edges between nodes. All the nodes and Edges cannot be null.
 */
public class Graph<N, E> {

    /** a collection of all the nodes and edges in the graph */
    private Map<@NonNull N, Set<Graph<@NonNull N, @NonNull E>.Edge>> graph;

    /** if true, run checkRep() */
    private final boolean runCheckRep = false;

    // Abstract Function:
    // Each Graph, g, maps every node, n, with its possible edges (n as the parent node)
    //
    // Representation Invariant for every Graph g:
    // graph != null
    // forall n in g, n != null

    /**
     * @spec.effects Constructs an empty Graph
     */
    public Graph() {
        this.graph = new HashMap<>();
        checkRep();
    }

    /** Checks that the representation invariant holds (if any). */
    private void checkRep(@UnknownInitialization(Graph.class) Graph<N, E> this) {
        if (runCheckRep) {
            assert (this.graph != null) : "graph should never be null";
        }
    }

    /**
     * Add the given node to this Graph
     * No two identical nodes are allowed in the same graph
     *
     * @param newNode the new node that will be added
     * @spec.requires {@code newNode != null && !this.contains(newNode)}
     * @spec.modifies this
     * @spec.effects Add "newNode" to this Graph
     */
    public void addNode(N newNode) {
        checkRep();
        if (newNode == null) {
            throw new IllegalArgumentException();
        }
        if (!this.graph.containsKey(newNode)) {
            this.graph.put(newNode, new HashSet<>());
        }
        checkRep();
    }

    /**
     * Add an edge of a given label between the given two nodes to this Graph.
     * No identical Edges are allowed in the same Graph
     *
     * @param start the parent node of the Edge
     * @param destination the children node of the Edge
     * @param label the label of the Edge
     * @spec.requires {@code this.contains(start) && this.contains(destination) &&
     *                label != null && !this.contains(Edge)}
     * @spec.modifies this
     * @spec.effects Add a new Edge from start to destination of label "label"
     *               to this Graph
     */
    public void addEdge(@NonNull @KeyFor("graph") N start, @NonNull @KeyFor("graph") N destination,
                        @NonNull E label) {
        checkRep();
        /*if (!this.contains(start) || !this.contains(destination)) {
            throw new IllegalArgumentException();
        }*/
        Graph<N, E>.Edge e = new Graph<N, E>.Edge(start, destination, label);
        if (!this.graph.get(start).contains(e)) {
            this.graph.get(start).add(e);
        }
        checkRep();
    }

    /**
     * Remove the given node from this Graph
     *
     * @param name the node that will be removed
     * @spec.requires this.contains(name)
     * @spec.modifies this
     * @spec.effects Remove the node "name" in this Graph
     */
    public void removeNode(N name) {
        checkRep();
        if (!this.contains(name)) {
            throw new IllegalArgumentException();
        }
        this.graph.remove(name);
        checkRep();
    }

    /**
     * Remove the edge of the given label between the given two nodes from this Graph
     *
     * @param start the parent node of the Edge
     * @param destination the children node of the Edge
     * @param label the label of the Edge
     * @spec.requires {@code this.contains(start) && this.contains(destination) &&
     *                this.contains(edge)}
     * @spec.modifies this
     * @spec.effects Remove the Edge edge of label "label" between the given two nodes
     *               from this Graph
     */
    public void removeEdge(@KeyFor("graph") N start, @KeyFor("graph") N destination, E label) {
        checkRep();
        if (!this.contains(start) || !this.contains(destination)) {
            throw new IllegalArgumentException();
        }
        for (Edge e : this.graph.get(start)) {
            if (e.getLabel().equals(label)) {
                this.graph.get(start).remove(e);
                checkRep();
                return;
            }
        }
        checkRep();
    }

    /**
     * Set the name of the given node to the given newName
     * @param desiredNode the node we want to change the name of
     * @param newName the new name we want to change to
     * @spec.requires {@code this.contains(desiredNode) && newName != null}
     * @spec.modifies this
     * @spec.effects Set the name of "desiredNode" to "newName"
     */
    public void renameNode(@KeyFor("graph") N desiredNode, N newName) {
        checkRep();
        if (!this.contains(desiredNode) || newName == null || newName.equals("")) {
            throw new IllegalArgumentException();
        }
        this.graph.put(newName, this.graph.get(desiredNode));
        this.graph.remove(desiredNode);
        checkRep();
    }

    /**
     * Checks if the graph contains the given node
     * @param name the node the method checks
     * @spec.requires {@code name != null}
     * @return true if this contains the given node "name"
     *         false otherwise
     */
    public boolean contains(N name) {
        checkRep();
        return this.graph.containsKey(name);
    }

    /**
     * Return an unmodifiable Set of all the nodes in the graph
     *
     * @return an unmodifiable Set of all the nodes in the graph
     */
    public Set<N> getNodes() {
        checkRep();
        return Collections.unmodifiableSet(this.graph.keySet());
    }

    /**
     * Return a String representation of all the nodes in the graph in alphabetical
     * order
     *
     * @return a String representation of all the nodes in the graph in alphabetical
     *         order
     */
    public String listNodes() {
        checkRep();
        String result = "";
        Set<N> nodes = new TreeSet<>();
        nodes.addAll(this.graph.keySet());
        for (N node: nodes) {
            result += " " + node;
        }
        return result;
    }

    /**
     * Return an unmodifiable Set of all the edges origins from the given node
     *
     * @param target the node client wants its edges
     * @spec.requires this graph contains "target"
     * @return an unmodifiable Set of all the edges origins from "target"
     */
    public Set<Graph<N, E>.Edge> getEdgesFromANode(@KeyFor("graph") N target) {
        checkRep();
        if (!this.contains(target)) {
            throw new IllegalArgumentException();
        } else {
            return Collections.unmodifiableSet(this.graph.get(target));
        }
    }

    /**
     * Return a String representation of all the children of the given parent node in
     * alphabetical order in the format children1(path1) children1(path2) children2(path1) ...
     *
     * @param parent the given parent node
     * @spec.requires this graph contains "parent"
     * @return a String representation of all the children of "parent"
     */
    public String getChildrenFromParent(@KeyFor("graph") N parent) {
        checkRep();
        if (!this.contains(parent)) {
            throw new IllegalArgumentException();
        }
        String childrenName = "";
        Set<String> children = new TreeSet<>();
        for (Edge e : this.graph.get(parent)) {
            children.add(e.getDestination() + "(" + e.getLabel() + ")");
        }
        for (String s : children) {
            childrenName += " " + s;
        }
        return childrenName;
    }

    /**
     * Returns a string representation of this Graph
     *
     * @return a String representation of this Graph by listing the String representation
     *         of every node in the Graph and separating them with a " "
     */
    @Override
    public String toString() {
        checkRep();
        String s = "";
        for (N node : this.graph.keySet()) {
            s += node + " ";
        }
        checkRep();
        return s.trim();
    }

    /**
     * Edge represents an immutable one-way path to a node of a graph.
     */
    public final class Edge {

        /** the destination of the Edge */
        @NonNull private final N start;

        /** the destination of the Edge */
        @NonNull private final N destination;

        /** the label of the Edge */
        @NonNull private final E label;

        /** if true, run checkRep() */
        private final boolean runCheckRep = false;

        // Abstraction Function:
        // Edge, e, stores a start node, a destination node and its label.
        //
        // Representation Invariant for every Edge e:
        // start != null && destination != null && && label != null

        /**
         * @param start the start of the Edge
         * @param destination the destination of the Edge
         * @param label the label of the Edge
         * @spec.requires {@code start != null && destination != null && label != null}
         * @spec.effects Constructs a Edge whose start is "start", destination is "destination"
         *      and the label of the Edge is "label"
         */
        public Edge(@NonNull N start, @NonNull N destination, @NonNull E label) {
            this.start = start;
            this.destination = destination;
            this.label = label;
            checkRep();
        }

        /** Checks that the representation invariant holds (if any). */
        private void checkRep(@UnknownInitialization(Edge.class)Edge this) {
            if (runCheckRep) {
                assert (destination != null) : "destination should never be null";
                assert (start != null) : "destination should never be null";
                assert (label != null) : "label can't be null";
            }
        }

        /**
         * Returns the start of this Edge
         *
         * @return a generic representation of the start of this Edge
         */
        public @NonNull N getStart() {
            checkRep();
            return this.start;
        }

        /**
         * Returns the destination of this Edge
         *
         * @return a generic representation of the destination of this Edge
         */
        public @NonNull N getDestination() {
            checkRep();
            return this.destination;
        }

        /**
         * Returns the label of this Edge
         *
         * @return a generic representation of the label of this Edge
         */
        public @NonNull E getLabel() {
            checkRep();
            return this.label;
        }

        /**
         * Standard hashCode function
         *
         * @return an int that all objects equal to this will also return
         */
        @Override
        public int hashCode() {
            checkRep();
            return this.start.hashCode() + this.destination.hashCode() + 11 * label.hashCode();
        }

        /**
         * Standard equality operation.
         *
         * @param obj The object to be compared for equality.
         * @return true if and only if 'obj' is an instance of an Edge and 'this' and 'obj'
         *         have the same values
         */
        @Override
        public boolean equals(@Nullable Object obj) {
            checkRep();
            if (obj instanceof Graph.Edge) {
                Graph.Edge other = (Graph.Edge) obj;
                return this.start.equals(other.getStart()) &&
                        this.getDestination().equals(other.getDestination()) &&
                        this.getLabel().equals(other.getLabel());
            } else {
                return false;
            }
        }
    }
}
