package cpen221.mp2.graph;

import java.util.*;

public class ALGraph<V extends Vertex, E extends Edge<V>> implements MGraph<V, E> {
    public List<V> listOfVertex ;
    public List<E> listOfEdge;
    public Map<Integer, List<V>> adjacencyList;
    public int numOfVertex;
    public ALGraph(){
        listOfVertex = new ArrayList<>();
        listOfEdge = new ArrayList<>();
        adjacencyList = new HashMap<>();
        numOfVertex = listOfVertex.size();
    }
    /**
     * Add a vertex to the graph
     *
     * @param v vertex to add
     * @return true if the vertex was added successfully and false otherwise
     */
    @Override
    public boolean addVertex(V v) {
        if (listOfVertex.contains(v)){
            return false;
        }else{
            listOfVertex.add(v);
            List<V> emptyList = new ArrayList<>();
            adjacencyList.put(v.id(), emptyList);
            return true;
        }
    }

    /**
     * Check if a vertex is part of the graph
     *
     * @param v vertex to check in the graph
     * @return true of v is part of the graph and false otherwise
     */
    @Override
    public boolean vertex(V v) {
        if (listOfVertex.contains(v)){
            return true;
        }else{
            return false;
        }
    }
    /**
     * Add an edge of the graph
     *
     * @param e the edge to add to the graph
     * @return true if the edge was successfully added and false otherwise
     */
    @Override
    public boolean addEdge(E e) {
        if (listOfEdge.contains(e)){
            return false;
        }else {
            if (listOfVertex.contains(e.v1()) && listOfVertex.contains(e.v2())) {
                listOfEdge.add(e);
                if (adjacencyList.get(e.v1().id()) == null) {
                    List<V> listOfV1 = new ArrayList<>();
                    listOfV1.add(e.v2());
                    adjacencyList.put(e.v1().id(), listOfV1);
                } else {
                    List<V> listOfV1 = new ArrayList<>(adjacencyList.get(e.v1().id()));
                    listOfV1.add(e.v2());
                    adjacencyList.put(e.v1().id(), listOfV1);
                }
                if (adjacencyList.get(e.v2().id()) == null) {
                    List<V> listOfV2 = new ArrayList<>();
                    listOfV2.add(e.v1());
                    adjacencyList.put(e.v2().id(), listOfV2);
                } else {
                    List<V> listOfV2 = new ArrayList<>(adjacencyList.get(e.v2().id()));
                    listOfV2.add(e.v1());
                    adjacencyList.put(e.v2().id(), listOfV2);
                }
                return true;
            }else{
                return false;
            }
        }
        }
    /**
     * Check if an edge is part of the graph
     *
     * @param e the edge to check in the graph, e is a valid edge
     * @return true if e is an edge in the graoh and false otherwise
     */
    @Override
    public boolean edge(E e) {
        if (listOfEdge.contains(e)){
            return true;
        }else{
            return false;
        }
    }
    /**
     * Check if v1-v2 is an edge in the graph
     *
     * @param v1 the first vertex of the edge
     * @param v2 the second vertex of the edge
     * @return true of the v1-v2 edge is part of the graph and false otherwise
     */
    @Override
    public boolean edge(V v1, V v2) {
        for (Edge x: listOfEdge){
            if ((x.v1().equals(v1) && x.v2().equals(v2)) || (x.v1().equals(v2) && x.v2().equals(v1))){
                return true;
            }
        }
        return false;
    }
    /**
     * Determine the length on an edge in the graph
     *
     * @param v1 the first vertex of the edge
     * @param v2 the second vertex of the edge
     * @return the length of the v1-v2 edge if this edge is part of the graph
     * return -1 when the edge is not in the graph
     */
    @Override
    public int edgeLength(V v1, V v2) {
        for (Edge x: listOfEdge){
            if ((x.v1().equals(v1) && x.v2().equals(v2)) || (x.v1().equals(v2) && x.v2().equals(v1))){
                return x.length();
            }
        }
        return -1;
    }
    /**
     * Obtain the sum of the lengths of all edges in the graph
     *
     * @return the sum of the lengths of all edges in the graph
     */
    @Override
    public int edgeLengthSum() {
        int sum = 0;
        for (E x: listOfEdge){
            sum += x.length();
        }
        return sum;
    }
    /**
     * Remove an edge from the graph
     *
     * @param e the edge to remove
     * @return true if e was successfully removed and false otherwise
     */
    @Override
    public boolean remove(E e) {
        if (listOfEdge.contains(e)) {
            for (Edge x : listOfEdge) {
                if (e.v1().equals(x.v1()) && e.v2().equals(x.v2())) {
                    listOfEdge.remove(e);
                    List<V> listOfV1 = new ArrayList<>(adjacencyList.get(e.v1().id()));
                    listOfV1.remove(e.v2());
                    adjacencyList.put(e.v1().id(), listOfV1);
                    List<V> listOfV2 = new ArrayList<>(adjacencyList.get(e.v2().id()));
                    listOfV2.remove(e.v1());
                    adjacencyList.put(e.v2().id(), listOfV2);
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * Remove a vertex from the graph
     *
     * @param v the vertex to remove
     * @return true if v was successfully removed and false otherwise
     */
    @Override
    public boolean remove(V v) {
        if (listOfVertex.contains(v)){
            listOfVertex.remove(v);
            adjacencyList.remove(v.id());
            for (List<V> x : adjacencyList.values()){
                if (x.contains(v)){
                    x.remove(v);
                }
            }
            List<E> listOfEdgeTemp = new ArrayList<>(listOfEdge);
            for (E x : listOfEdge){
                if (x.v1().equals(v) || x.v2().equals(v)){
                    listOfEdgeTemp.remove(x);
                }
            }
            listOfEdge = listOfEdgeTemp;
            return true;
        }else{
            return false;
        }
    }

    /**
     * Obtain a set of all vertices in the graph.
     * Access to this set **should not** permit graph mutations.
     *
     * @return a set of all vertices in the graph
     */
    @Override
    public Set<V> allVertices() {
        Set<V> allVertices = new HashSet<>(listOfVertex);
        return allVertices;
    }

    /**
     * Obtain a set of all vertices incident on v.
     * Access to this set **should not** permit graph mutations.
     *
     * @param v the vertex of interest
     * @return all edges incident on v
     */
    @Override
    public Set<E> allEdges(V v) {
        Set<E> allEdges = new HashSet<>();
        for (E x : listOfEdge){
            if (x.v1() == v || x.v2() == v){
                allEdges.add(x);
            }
        }
        return allEdges;
    }
    /**
     * Obtain a set of all edges in the graph.
     * Access to this set **should not** permit graph mutations.
     *
     * @return all edges in the graph
     */
    @Override
    public Set<E> allEdges() {
        Set<E> allEdges = new HashSet<>(listOfEdge);
        return allEdges;
    }
    /**
     * Obtain all the neighbours of vertex v.
     * Access to this map **should not** permit graph mutations.
     *
     * @param v is the vertex whose neighbourhood we want.
     * @return a map containing each vertex w that neighbors v and the edge between v and w.
     */
    @Override
    public Map<V, E> getNeighbours(V v) {
        List<V> neighboursV = new ArrayList<>();
        Map<V, E> getNeigbours = new HashMap<>();
        if (v != null) {
            neighboursV = adjacencyList.get(v.id());
            if (neighboursV != null) {
                for (V x : neighboursV) {
                    for (E y : listOfEdge) {
                        if ((y.v1().equals(v) && y.v2().equals(x)) || (y.v1().equals(x) && y.v2().equals(v))) {
                            getNeigbours.put(x, y);
                        }
                    }
                }
            }
        }
        return getNeigbours;
    }
}
