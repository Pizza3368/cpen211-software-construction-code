package cpen221.mp2.graph;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;



public class AMGraph<V extends Vertex, E extends Edge<V>> implements MGraph<V, E> {

    int theMtx[][];
    int distanceMtx[][];

    Set<E> listofEdge;
    Set<V> listofVertex;
    int max;




    /**
     * Create an empty graph with an upper-bound on the number of vertices
     * @param maxVertices is greater than 1
     */
    public AMGraph(int maxVertices) {
        this.theMtx = new int[maxVertices+1][maxVertices+1];
        this.distanceMtx = new int[maxVertices+1][maxVertices+1];
        this.listofEdge = new HashSet<E>();
        this.listofVertex = new HashSet<V>();
        this.max=maxVertices;

    }

    /**
     * Add a vertex to the graph
     *
     * @param v vertex to add
     * @return true if the vertex was added successfully and false otherwise
     */
    @Override
    public boolean addVertex(V v) {
        if(this.listofVertex.contains(v)){
            return false;
        }

        else if(listofVertex.size()>=max){
            return false;
        }

        else{

            listofVertex.add(v);
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
        if(this.listofVertex.contains(v)){
            return true;
        }

        return false;
    }

    /**
     * Add an edge of the graph
     *
     * @param e the edge to add to the graph
     * @return true if the edge was successfully added and false otherwise
     */
    @Override
    public boolean addEdge(E e) {
        if(this.listofEdge.contains(e)){
            return false;
        }

        if(!listofVertex.contains(e.v1()) ||!listofVertex.contains(e.v2()) ){
            return false;
        }

        else{
            listofEdge.add(e);
            this.theMtx[e.v1().id()][e.v2().id()] = 1;
            this.theMtx[e.v2().id()][e.v1().id()] = 1;

            this.distanceMtx[e.v1().id()][e.v2().id()] = e.length();
            this.distanceMtx[e.v2().id()][e.v1().id()] = e.length();

            return true;
        }


    }

    /**
     * Check if an edge is part of the graph
     *
     * @param e the edge to check in the graph
     * @return true if e is an edge in the graoh and false otherwise
     */
    @Override
    public boolean edge(E e) {
        if(this.listofEdge.contains(e)){
            return true;
        }


        return false;
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

        if(vertex(v1)&&vertex(v2)) {

            if (this.theMtx[v1.id()][v2.id()] == 1) {
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
     */
    @Override
    public int edgeLength(V v1, V v2) {
        int temp =0;

        /*
        Optional<E> edge = this.listofEdge.stream().filter(e -> e.v1().equals(v1) && e.v2().equals(v2)).findFirst();
        if (edge.isPresent())
        {
            temp = edge.get().length();
        }
        */

        temp = distanceMtx[v1.id()][v2.id()];

        return temp;
    }

    /**
     * Obtain the sum of the lengths of all edges in the graph
     *
     * @return the sum of the lengths of all edges in the graph
     */
    @Override
    public int edgeLengthSum() {
        int temp =0;

        for(E e : listofEdge){
            temp = temp+e.length();
        }

        return temp;
    }

    /**
     * Remove an edge from the graph
     *
     * @param e the edge to remove
     * @return true if e was successfully removed and false otherwise
     */
    @Override
    public boolean remove(E e) {

        if(listofEdge.contains(e)){

            this.theMtx[e.v1().id()][e.v2().id()] =0;
            this.distanceMtx[e.v1().id()][e.v2().id()] =0;
            this.listofEdge.remove(e);

            return true;
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
        if(listofVertex.contains(v)){
            //remove vertex
            this.listofVertex.remove(v);

            //this.listofEdge.removeIf(e ->  e.v1().equals(v) || e.v2().equals(v));

            List<E> edge = this.listofEdge.stream().filter(e -> e.v1().equals(v) || e.v2().equals(v)).toList();
            if (!edge.isEmpty())
            {
                for(E e: edge){

                    this.listofEdge.remove(e);
                    this.theMtx[e.v1().id()][e.v2().id()] =0;
                    this.theMtx[e.v2().id()][e.v1().id()] =0;
                    this.distanceMtx[e.v1().id()][e.v2().id()] =0;
                    this.distanceMtx[e.v2().id()][e.v1().id()] =0;

                }
            }

            return true;
        }



        return false;
    }

    /**
     * Obtain a set of all vertices in the graph.
     * Access to this set **should not** permit graph mutations.
     *
     * @return a set of all vertices in the graph
     */
    @Override
    public Set<V> allVertices() {

        Set<V> temp = new HashSet<>(this.listofVertex);
        return temp;
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
        Set<E> edge = this.listofEdge.stream().filter(e -> e.v1().equals(v) || e.v2().equals(v)).collect(Collectors.toSet());
        return edge;
    }

    /**
     * Obtain a set of all edges in the graph.
     * Access to this set **should not** permit graph mutations.
     *
     * @return all edges in the graph
     */
    @Override
    public Set<E> allEdges() {
        Set<E> temp = new HashSet<>(this.listofEdge);
        return temp;
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
        Map<V, E> temp = new HashMap<>();
        if (v != null) {
            Set<E> ed = allEdges(v);


            for (E e : allEdges()) {
                if (e.v1().equals(v)) {

                    temp.put(e.v2(), e);
                } else if (e.v2().equals(v)) {

                    temp.put(e.v1(), e);
                }
            }
        }
        return temp;
    }
}
