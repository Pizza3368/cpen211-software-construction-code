package cpen221.mp2.graph;

import java.util.*;

/**
 * Represents a graph with vertices of type V.
 *
 * @param <V> represents a vertex type
 */
public class Graph<V extends Vertex, E extends Edge<V>> implements ImGraph<V, E>, MGraph<V, E> {


    // You can re-implement this graph, or use composition and
    // rely on your implementation of ALGraph or AMGraph


    private ALGraph<V, E> fieldOne;
    public Graph(){
        fieldOne = new ALGraph<>();

    }

    //// add all new code above this line ////

    /**
     * This method removes some edges at random while preserving connectivity
     * <p>
     * DO NOT CHANGE THIS METHOD
     * </p>
     * <p>
     * You will need to implement allVertices() and allEdges(V v) for this
     * method to run correctly
     *</p>
     * <p><strong>requires:</strong> this graph is connected</p>
     *
     * @param rng random number generator to select edges at random
     */
    public void pruneRandomEdges(Random rng) {
        class VEPair {
            V v;
            E e;

            public VEPair(V v, E e) {
                this.v = v;
                this.e = e;
            }
        }
        /* Visited Nodes */
        Set<V> visited = new HashSet<>();
        /* Nodes to visit and the cpen221.mp2.graph.Edge used to reach them */
        Deque<VEPair> stack = new LinkedList<VEPair>();
        /* Edges that could be removed */
        ArrayList<E> candidates = new ArrayList<>();
        /* Edges that must be kept to maintain connectivity */
        Set<E> keep = new HashSet<>();

        V start = null;
        for (V v : this.allVertices()) {
            start = v;
            break;
        }
        if (start == null) {
            // nothing to do
            return;
        }
        stack.push(new VEPair(start, null));
        while (!stack.isEmpty()) {
            VEPair pair = stack.pop();
            if (visited.add(pair.v)) {
                keep.add(pair.e);
                for (E e : this.allEdges(pair.v)) {
                    stack.push(new VEPair(e.distinctVertex(pair.v), e));
                }
            } else if (!keep.contains(pair.e)) {
                candidates.add(pair.e);
            }
        }
        // randomly trim some candidate edges
        int iterations = rng.nextInt(candidates.size());
        for (int count = 0; count < iterations; ++count) {
            int end = candidates.size() - 1;
            int index = rng.nextInt(candidates.size());
            E trim = candidates.get(index);
            candidates.set(index, candidates.get(end));
            candidates.remove(end);
            remove(trim);
        }
    }


    /**
     * Find the edge that connects two vertices if such an edge exists.
     * This method should not permit graph mutations.
     *
     * @param v1 one end of the edge
     * @param v2 the other end of the edge
     * @param v1, v2 have a edge between them and v1,v2 in the graph
     * @return the edge connecting v1 and v2
     */
    @Override
    public E getEdge(V v1, V v2) {

        int length =0;

        if(this.fieldOne.edge(v1, v2) == false){
            return null;
        }

        length = this.fieldOne.edgeLength(v1, v2);


        Edge<Vertex> temp = new Edge<>(v1, v2, length);

        return (E) temp;
    }

    /**
     * Compute the shortest path from source to sink
     *
     * @param source the start vertex
     * @param sink   the end vertex
     * @return the vertices, in order, on the shortest path from source to sink (both end points are part of the list)
     */
    @Override
    public List<V> shortestPath(V source, V sink) {
        Set<V> unsettledNodes = new HashSet<>(this.fieldOne.listOfVertex); //record all nodes that havent been current vertex yet
        Map<V, Integer> lengthMap = new HashMap<>(); // distance from source to each nodes
        Map<V, V>pastMap = new HashMap<>(); //the map recording where each nodes is shortest from other node
        V currentVertex = source; //the current node we are observing from
        Map<V, E> mapNB = new HashMap<>(); //map of all neighbour of the current vertex

        //check if source = sink
        if(source.equals(sink)){
            List<V> shortestPath = new ArrayList<>();
            shortestPath.add(source);
            return shortestPath;
        }

        //check if source and sink are in graph
        if(!fieldOne.vertex(source)||!fieldOne.vertex(sink)){
            List<V> shortestPath = new ArrayList<>();
            return shortestPath;
        }

        for(V v:unsettledNodes){
            lengthMap.put(v, Integer.MAX_VALUE);
        }
        lengthMap.put( currentVertex, 0);


        unsettledNodes.remove(currentVertex);
        while(unsettledNodes.size()!= 0){


            mapNB = this.fieldOne.getNeighbours(currentVertex);
            for(V v: mapNB.keySet()){

                if (lengthMap.get(currentVertex)+mapNB.get(v).length() < lengthMap.get(v)) {
                    lengthMap.put(v, lengthMap.get(currentVertex)+mapNB.get(v).length());

                    pastMap.put(v,currentVertex);
                }

            }
            //test for Connectivity, ie, false if all unsettled nodes have distance of infinity
            boolean connectivity = false;
            for(V v: unsettledNodes){
                if(lengthMap.get(v)<Integer.MAX_VALUE){
                    connectivity = true;
                }
            }

            if(connectivity == false){
                break;
            }

            int min =Integer.MAX_VALUE;

            for(V v: unsettledNodes){

                if (lengthMap.get(v) <= min){
                    min = lengthMap.get(v);
                    currentVertex = v;
                }

            }

            unsettledNodes.remove(currentVertex);

        }

        List<V> shortestPath = new ArrayList<>();
        currentVertex = sink;

        while(pastMap.containsKey(currentVertex)&&currentVertex!= source){
            shortestPath.add(currentVertex);
            currentVertex = pastMap.get(currentVertex);

        }

        if(!sink.equals(source) && shortestPath.size()==0){
            return shortestPath;
        }

        shortestPath.add(source);
        Collections.reverse(shortestPath);

        return shortestPath;
    }
    /**
     * generate a distance map between source and other vertex
     *
     * @param  source vertex and graph, vertex in the graph, graph is connected
     *
     * @retrun Distance map
     * */
    //graph diameter helper, generate the same map as find shortest length map of a single all connected graph
    public Map<V, Integer> generateLengthMap (V source, Graph<V, E> theGraph) {
        Set<V> unsettledNodes = new HashSet<>(theGraph.allVertices()); //record all nodes that havent been current vertex yet
        Map<V, Integer> lengthMap = new HashMap<>(); // distance from source to each nodes
        Map<V, V>pastMap = new HashMap<>(); //the map recording where each nodes is shortest from other node
        V currentVertex = source; //the current node we are observing from
        Map<V, E> mapNB = new HashMap<>(); //map of all neighbour of the current vertex


        for(V v:unsettledNodes){
            lengthMap.put(v, Integer.MAX_VALUE);
        }
        lengthMap.put( currentVertex, 0);

        for(V v:unsettledNodes){
            pastMap.put(v, v);
        }

        unsettledNodes.remove(currentVertex);
        while(unsettledNodes.size()!= 0){


            mapNB = this.fieldOne.getNeighbours(currentVertex);
            for(V v: mapNB.keySet()){

                if (lengthMap.get(currentVertex)+mapNB.get(v).length() < lengthMap.get(v)) {
                    lengthMap.put(v, lengthMap.get(currentVertex)+mapNB.get(v).length());

                    pastMap.put(v,currentVertex);
                }

            }


            int min =Integer.MAX_VALUE;

            for(V v: unsettledNodes){

                if (lengthMap.get(v) <= min){
                    min = lengthMap.get(v);
                    currentVertex = v;
                }

            }

            unsettledNodes.remove(currentVertex);

        }

        return lengthMap;

    }



    /**
     * look at existing graph, group the connecting vertex together
     *
     * @return a list of sets, where each sets contain vertex that are connected together, different sets do not connect to eachother
     */
    public List<Graph<V,E>> seperateGraph(){

        List<Set<V>> listOfConnectingVertex = new ArrayList<>();
        List<Set<E>> listOfConnectingEdge = new ArrayList<>();
        List<E> alledge = new ArrayList<>(this.fieldOne.allEdges());
        List<V> allVertices = new ArrayList<>(this.fieldOne.allVertices());
        int targetone = 0; //the index of set that contain the first vertex of edge
        int targettwo = 0;// the index of set that contain the second vertex of edge
        List<Graph<V,E>> listOfSeperateGraph = new ArrayList<>();

        for(E e: alledge){

            targetone = Integer.MAX_VALUE;
            targettwo = Integer.MAX_VALUE;

            //find targetone
            for(Set<V> s: listOfConnectingVertex){
                if(s.contains(e.v1())){
                    targetone = listOfConnectingVertex.indexOf(s);
                }
            }

            //find targettwo
            for(Set<V> s: listOfConnectingVertex){
                if(s.contains(e.v2())){
                    targettwo = listOfConnectingVertex.indexOf(s);
                }
            }

            //both vertex never observed
            if(targetone == Integer.MAX_VALUE&&targettwo == Integer.MAX_VALUE){
                Set<V> newSet = new HashSet<>();
                newSet.add(e.v1());
                newSet.add(e.v2());
                listOfConnectingVertex.add(newSet);
                allVertices.remove(e.v1());
                allVertices.remove(e.v2());
            }

            //both vertex found in same set
            else if(targetone == targettwo){
                listOfConnectingVertex.get(targetone).add(e.v1());
                listOfConnectingVertex.get(targetone).add(e.v2());
                allVertices.remove(e.v1());
                allVertices.remove(e.v2());
            }

            //both vertex found in different set, join two set and delete the extra
            else if(targetone != Integer.MAX_VALUE && targettwo != Integer.MAX_VALUE){
                listOfConnectingVertex.get(targetone).addAll(listOfConnectingVertex.get(targettwo));
                listOfConnectingVertex.get(targetone).add(e.v1());
                listOfConnectingVertex.get(targetone).add(e.v2());
                listOfConnectingVertex.remove(targettwo);
                allVertices.remove(e.v1());
                allVertices.remove(e.v2());
            }

            //only vertex one is found
            else if(targetone != Integer.MAX_VALUE){
                listOfConnectingVertex.get(targetone).add(e.v1());
                listOfConnectingVertex.get(targetone).add(e.v2());
                allVertices.remove(e.v1());
                allVertices.remove(e.v2());
            }

            //only vertex two is found
            else{
                listOfConnectingVertex.get(targettwo).add(e.v1());
                listOfConnectingVertex.get(targettwo).add(e.v2());
                allVertices.remove(e.v1());
                allVertices.remove(e.v2());
            }
        }
        for (V x : allVertices){
            Set<V> newSet = new HashSet<>();
            newSet.add(x);
            listOfConnectingVertex.add(newSet);
        }

        for (Set<V> x: listOfConnectingVertex){
            Graph<V, E> temp = new Graph<>();
            for (V y: x){
                temp.addVertex(y);
            }
            listOfSeperateGraph.add(temp);
        }
        for (Graph y : listOfSeperateGraph)
            for (E x : alledge){
                if (y.fieldOne.listOfVertex.contains(x.v1())){
                    y.addEdge(x);
                }
            }
        for (Graph x : listOfSeperateGraph){

        }


        Collections.sort(listOfSeperateGraph, new Comparator<Graph>() {
            @Override
            public int compare(Graph o1, Graph o2) {
                return o1.fieldOne.listOfVertex.size() < o2.fieldOne.listOfVertex.size() ? -1 : (o1.fieldOne.listOfVertex.size() > o2.fieldOne.listOfVertex.size()) ? 1 : 0;
            }
        });

        Collections.reverse(listOfSeperateGraph);
        return listOfSeperateGraph;

    }



    /**
     * Compute the length of a given path
     *
     * @param path indicates the vertices on the given path
     * @return the length of path
     */
    @Override
    public int pathLength(List<V> path) {
        int totalDistance = 0;

        if(path.size() == 0){
            return Integer.MAX_VALUE;
        }

        for(int i=0; i<path.size() -1 ; i++){
            totalDistance = totalDistance + fieldOne.edgeLength(path.get(i), path.get(i + 1));

        }

        return totalDistance;
    }


    /**
     * Obtain all vertices w that are no more than a <em>path distance</em> of range from v.
     *
     * @param v     the vertex to start the search from.
     * @param range the radius of the search.
     * @return a map where the keys are the vertices in the neighbourhood of v,
     *          and the value for key w is the last edge on the shortest path
     *          from v to w.
     */
    @Override
    public Map<V, E> getNeighbours(V v, int range) {

        List<Graph<V,E>> temp = seperateGraph();
        Map<V, E> neighbours = new HashMap<>();
        Graph<V,E> part = null;
        List<V> path = new ArrayList<>();

        V last = null;
        V secondLast = null;
        if(!fieldOne.vertex(v)){
            return neighbours;
        }

        if(temp.size() != 1){
            for(Graph i: temp){
                if(i.vertex(v)){
                    part = i;
                }
            }
        }else{part = temp.get(0);}

        Map<V, Integer> lengthMap = generateLengthMap(v, part);

        for(V i : lengthMap.keySet()){
            if(lengthMap.get(i) <= range&& i != v){
                path = shortestPath(v,i);
                if(path.size() > 1){
                    last = path.get(path.size()-1);
                    secondLast = path.get(path.size()-2);
                }

                neighbours.put(i,getEdge(secondLast,last));
            }
        }

        return neighbours;
    }
    /**
     * Return a set with k connected components of the graph.
     *
     * <ul>
     * <li>When k = 1, the method returns one graph in the set, and that graph
     * represents the minimum spanning tree of the graph.
     * See: https://en.wikipedia.org/wiki/Minimum_spanning_tree</li>
     *
     * <li>When k = n, where n is the number of vertices in the graph, then
     * the method returns a set of n graphs, and each graph contains a
     * unique vertex and no edge.</li>
     *
     * <li>When k is in [2, n-1], the method partitions the graph into k connected sub-graphs
     * such that for any two vertices V_i and V_j, if vertex V_i is in subgraph
     * G_a and vertex V_j is in subgraph G_b (a != b), and there is an edge
     * between V_i and V_j, and |G_a| > 1, then there must exist some vertex V_k in G_a such
     * that the length of the edge between V_i and V_k is at most the length
     * of the edge between V_i and V_j.</li>
     * </ul>
     *
     * @return a set of graph partitions such that a vertex in one partition
     * is no closer to a vertex in a different partition than it is to a vertex
     * in its own partition.
     */

    @Override
    public Set<ImGraph<V, E>> minimumSpanningComponents(int k) {
        List<Graph<V,E>> separateGraph;
        separateGraph = seperateGraph();
        int numberOfCut = 0;
        int numberOfCutNeeded = k - seperateGraph().size();
        Set<ImGraph<V, E>> result = new HashSet<>();
        for (Graph g : separateGraph) {
            int numberOfVertices = g.fieldOne.listOfVertex.size();
            int numberOfEdges = g.fieldOne.listOfEdge.size();
            List<E> listOfAllEdges;
            listOfAllEdges = g.fieldOne.listOfEdge;
            List<V> listOfAllVertices = g.fieldOne.listOfVertex;
            Graph<V, E> resultGraph = new Graph<>();
            E resultEdges[] = (E[]) new Edge[numberOfVertices - 1];
            E sortedEdges[] = (E[]) new Edge[numberOfEdges];
            int q = 0;
            for (E x : listOfAllEdges) {
                sortedEdges[q] = x;
                q++;
            }

            Arrays.sort(sortedEdges, Comparator.comparing(Edge::length));

            // Allocate memory for creating numberOfVertices subsets
            subset subsets[] = new subset[fieldOne.listOfVertex.size()];
            for (int i = 0; i < fieldOne.listOfVertex.size(); ++i)
                subsets[i] = new subset();

            // Create V subsets with single elements
            for (int v = 0; v < fieldOne.listOfVertex.size(); ++v) {
                subsets[v].parent = v;
                subsets[v].rank = 0;
            }
            int e = 0;
            int i = 0;
            while (e < numberOfVertices - 1) {
                // Pick the smallest edge. And increment
                // the index for next iteration
                E next_edge = sortedEdges[i++];

                int x = find(subsets, next_edge.v1().id() - 1);
                int y = find(subsets, next_edge.v2().id() - 1);

                // If including this edge doesn't cause cycle,
                // include it in result and increment the index
                // of result for next edge
                if (x != y) {
                    resultEdges[e++] = next_edge;
                    Union(subsets, x, y);
                }
                // Else discard the next_edge
            }
            for (V x : listOfAllVertices) {
                resultGraph.addVertex(x);
            }
            for (int o = 0; o < resultEdges.length; o++) {
                resultGraph.addEdge(resultEdges[o]);
            }
            //numberOfGraphs++;
            int numOfDividedGraph = 0;

            //divide the current graph
            while (numberOfCut < numberOfCutNeeded) {
                if (numOfDividedGraph >= resultGraph.fieldOne.listOfVertex.size()){
                    break;
                }
                Graph<V, E> newImGraph = new Graph<>();
                List<E> storedEdge = new ArrayList<>();
                int longestDistance = 0;
                for (E x : resultGraph.fieldOne.allEdges()) {
                    if (x.length() > longestDistance) {
                        storedEdge.add(x);
                        longestDistance = x.length();
                    }
                }
                resultGraph.fieldOne.remove(storedEdge.get(storedEdge.size() - 1));
                newImGraph.addEdge(storedEdge.get(storedEdge.size() - 1));
                for (int z = 0; z < resultGraph.fieldOne.adjacencyList.size(); z++){
                    int idOfVertice = resultGraph.fieldOne.listOfVertex.get(z).id();
                    if (resultGraph.fieldOne.adjacencyList.get(idOfVertice).size() == 0){
                        V temp;
                        temp = resultGraph.fieldOne.listOfVertex.get(z);
                        resultGraph.remove(temp);
                        newImGraph.addVertex(temp);
                    }
                }
                result.add(newImGraph);
                numberOfCut++;
                numOfDividedGraph++;
            }
            result.add(resultGraph);
        }
        return result;
    }

    /**
     * minimumSpanningComponents helper
     *
     * @param subset[]    the vertex to start the search from.
     * @param int element  the radius of the search.
     * @return the parent of a element
     */
    int find(subset subsets[], int element){
        if (subsets[element].parent != element){
            subsets[element].parent = find(subsets, subsets[element].parent);
        }
        return subsets[element].parent;
    }

    /**
     * merge two subset together
     *
     * @param int x and y idicating the coordinate of the smaller subset
     * @param subsets[] the larger subst to attach to
     */
    void Union(subset subsets[], int x, int y)
    {
        int xroot = find(subsets, x);
        int yroot = find(subsets, y);

        // Attach smaller rank tree under root
        // of high rank tree (Union by Rank)
        if (subsets[xroot].rank < subsets[yroot].rank)
            subsets[xroot].parent = yroot;
        else if (subsets[xroot].rank > subsets[yroot].rank)
            subsets[yroot].parent = xroot;

            // If ranks are same, then make one as
            // root and increment its rank by one
        else {
            subsets[yroot].parent = xroot;
            subsets[xroot].rank++;
        }
    }

    /**
     * Compute the diameter of the graph.
     * <ul>
     * <li>The diameter of a graph is the length of the longest shortest path in the graph.</li>
     * <li>If a graph has multiple components then we will define the diameter
     * as the diameter of the largest component.</li>
     * </ul>
     *
     * @return the diameter of the graph.
     */
    @Override
    public int diameter() {
        List<Graph<V,E>> listofGraph;
        listofGraph = seperateGraph();
        int max =0;
        for(Graph<V, E> g : listofGraph){
            Set<V> allV = g.allVertices();
            for(V v : allV){
                Map<V, Integer> vToAll= generateLengthMap(v, g);
                for(V vt: allV){
                    if(vToAll.get(vt) >= max){
                        max = vToAll.get(vt);
                    }
                }
            }

        }

        return max;
    }
    /**
     * Compute the center of the graph.
     *
     * <ul>
     * <li>For a vertex s, the eccentricity of s is defined as the maximum distance
     * between s and any other vertex t in the graph.</li>
     *
     * <li>The center of a graph is the vertex with minimum eccentricity.</li>
     *
     * <li>If a graph is not connected, we will define the graph's center to be the
     * center of the largest connected component.</li>
     * </ul>
     *
     * @return the center of the graph.
     */
    @Override
    public V getCenter() {

        V center = null;
        List<Graph<V,E>> separated = seperateGraph();
        Graph<V,E> target;
        Map<V, Integer> furthest = new HashMap<>();
        List<V> allvertex;

        //find graph with largest conneceted component
        target = separated.get(0);

        allvertex = target.fieldOne.listOfVertex;

        for(V v:allvertex){
            int max =0;
            Map<V, Integer> distanceTo = generateLengthMap(v, target);
            for(V vt: allvertex){
                if(distanceTo.get(vt)>=max){
                    max = distanceTo.get(vt);
                    furthest.put(v, max);
                }
            }
        }

        int min = Integer.MAX_VALUE;
        for(V v: allvertex){
            if(furthest.get(v) <= min){
                min = furthest.get(v);
                center = v;
            }
        }

        return center;
    }



    @Override
    public boolean addVertex(V v) {
        return fieldOne.addVertex(v);
    }

    @Override
    public boolean vertex(V v) {
        return fieldOne.vertex(v);
    }

    @Override
    public boolean addEdge(E e) {
        return fieldOne.addEdge(e);
    }

    @Override
    public boolean edge(E e) {
        return fieldOne.edge(e);
    }

    @Override
    public boolean edge(V v1, V v2) {
        return fieldOne.edge(v1, v2);
    }

    @Override
    public int edgeLength(V v1, V v2) {
        return fieldOne.edgeLength(v1, v2);
    }

    @Override
    public int edgeLengthSum() {
        return fieldOne.edgeLengthSum();
    }

    @Override
    public boolean remove(E e) {
        return fieldOne.remove(e);
    }

    @Override
    public boolean remove(V v) {
        return fieldOne.remove(v);
    }

    @Override
    public Set<V> allVertices() {
        return fieldOne.allVertices();
    }

    @Override
    public Set<E> allEdges(V v) {
        return fieldOne.allEdges(v);
    }

    @Override
    public Set<E> allEdges() {
        return fieldOne.allEdges();
    }

    @Override
    public Map<V, E> getNeighbours(V v) {
        return fieldOne.getNeighbours(v);
    }
}