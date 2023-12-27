package cpen221.mp2;

import cpen221.mp2.graph.Edge;
import cpen221.mp2.graph.Graph;
import cpen221.mp2.graph.ImGraph;
import cpen221.mp2.graph.Vertex;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GraphTask4Test {

    @Test
    //Test 1
    public void testConnectedGraph() {
        Vertex v1 = new Vertex(1, "A");
        Vertex v2 = new Vertex(2, "B");
        Vertex v3 = new Vertex(3, "C");
        Vertex v4 = new Vertex(4, "D");
        Vertex v5 = new Vertex(5, "E");
        Vertex v6 = new Vertex(6, "F");
        Vertex v7 = new Vertex(7, "G");

        Edge<Vertex> e1 = new Edge<>(v1, v2, 9);
        Edge<Vertex> e2 = new Edge<>(v2, v3, 7);
        Edge<Vertex> e3 = new Edge<>(v1, v4, 10);
        Edge<Vertex> e4 = new Edge<>(v1, v3, 9);
        Edge<Vertex> e5 = new Edge<>(v2, v4, 15);
        Edge<Vertex> e6 = new Edge<>(v3, v7, 6);
        Edge<Vertex> e7 = new Edge<>(v3, v5, 7);
        Edge<Vertex> e8 = new Edge<>(v2, v5, 8);
        Edge<Vertex> e9 = new Edge<>(v2, v6, 4);
        Edge<Vertex> e10 = new Edge<>(v6, v7, 5);

        Graph<Vertex, Edge<Vertex>> g = new Graph<>();
        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);
        g.addVertex(v4);
        g.addVertex(v5);
        g.addVertex(v6);
        g.addVertex(v7);
        g.addEdge(e2);
        g.addEdge(e3);
        g.addEdge(e1);
        g.addEdge(e4);
        g.addEdge(e5);
        g.addEdge(e6);
        g.addEdge(e7);
        g.addEdge(e8);
        g.addEdge(e9);
        g.addEdge(e10);

        Graph<Vertex, Edge<Vertex>>[] graphOne2 = new Graph[2];
        Graph<Vertex, Edge<Vertex>> firstGraph = new Graph<>();
        Graph<Vertex, Edge<Vertex>> secondGraph = new Graph<>();
        Set<ImGraph<Vertex, Edge<Vertex>>> minimumSpanningComponents = g.minimumSpanningComponents(2);
        graphOne2[0] = secondGraph;
        graphOne2[1] = firstGraph;
        firstGraph.addVertex(v1);
        firstGraph.addVertex(v2);
        firstGraph.addVertex(v3);
        firstGraph.addVertex(v5);
        firstGraph.addVertex(v6);
        firstGraph.addVertex(v7);
        firstGraph.addEdge(e1);
        firstGraph.addEdge(e6);
        firstGraph.addEdge(e7);
        firstGraph.addEdge(e9);
        firstGraph.addEdge(e10);

        secondGraph.addVertex(v4);

        Graph<Vertex, Edge<Vertex>>[] result = minimumSpanningComponents.toArray(new Graph[0]);
        for (int i = 0; i < result.length; i++){
            assertTrue(result[i].allVertices().containsAll(graphOne2[i].allVertices()));
        }


    }
    @Test
    //Test 2
    public void testDisconnectedGraph() {
        Vertex v1 = new Vertex(1, "A");
        Vertex v2 = new Vertex(2, "B");
        Vertex v3 = new Vertex(3, "C");
        Vertex v4 = new Vertex(4, "D");
        Vertex v5 = new Vertex(5, "E");
        Vertex v6 = new Vertex(6, "F");
        Vertex v7 = new Vertex(7, "G");

        Edge<Vertex> e1 = new Edge<>(v1, v2, 9);
        Edge<Vertex> e2 = new Edge<>(v2, v3, 7);
        Edge<Vertex> e3 = new Edge<>(v1, v4, 10);
        Edge<Vertex> e4 = new Edge<>(v1, v3, 9);
        Edge<Vertex> e5 = new Edge<>(v2, v4, 15);
        Edge<Vertex> e6 = new Edge<>(v3, v7, 6);
        Edge<Vertex> e7 = new Edge<>(v3, v5, 7);
        Edge<Vertex> e8 = new Edge<>(v2, v5, 8);


        Graph<Vertex, Edge<Vertex>> g = new Graph<>();
        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);
        g.addVertex(v4);
        g.addVertex(v5);
        g.addVertex(v6);
        g.addVertex(v7);
        g.addEdge(e2);
        g.addEdge(e3);
        g.addEdge(e1);
        g.addEdge(e4);
        g.addEdge(e5);
        g.addEdge(e6);
        g.addEdge(e7);
        g.addEdge(e8);
        g.minimumSpanningComponents(4);

    }
    @Test
    //Test 3  fail 
    public void testDisconnectedGraph2() {
        Vertex v1 = new Vertex(1, "A");
        Vertex v2 = new Vertex(2, "B");
        Vertex v3 = new Vertex(3, "C");
        Vertex v4 = new Vertex(4, "D");
        Vertex v5 = new Vertex(5, "E");
        Vertex v6 = new Vertex(6, "F");
        Vertex v7 = new Vertex(7, "G");

        Edge<Vertex> e1 = new Edge<>(v1, v2, 9);
        Edge<Vertex> e2 = new Edge<>(v2, v3, 7);
        Edge<Vertex> e3 = new Edge<>(v4, v6, 5);
        Edge<Vertex> e4 = new Edge<>(v1, v3, 9);

        Edge<Vertex> e6 = new Edge<>(v3, v7, 6);
        Edge<Vertex> e7 = new Edge<>(v3, v5, 7);
        Edge<Vertex> e8 = new Edge<>(v2, v5, 8);


        Graph<Vertex, Edge<Vertex>> g = new Graph<>();
        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);
        g.addVertex(v4);
        g.addVertex(v5);
        g.addVertex(v6);
        g.addVertex(v7);
        g.addEdge(e2);
        g.addEdge(e3);
        g.addEdge(e1);
        g.addEdge(e4);

        g.addEdge(e6);
        g.addEdge(e7);
        g.addEdge(e8);
        Set<ImGraph<Vertex, Edge<Vertex>>> result = g.minimumSpanningComponents(4);
        


    }

    @Test
    //Test 4 for diameter
    public void testdiameter() {
        Vertex v1 = new Vertex(1, "A");
        Vertex v2 = new Vertex(2, "B");
        Vertex v3 = new Vertex(3, "C");
        Vertex v4 = new Vertex(4, "D");

        Edge<Vertex> e1 = new Edge<>(v1, v2, 9);
        Edge<Vertex> e2 = new Edge<>(v2, v3, 7);
        Edge<Vertex> e3 = new Edge<>(v1, v4, 10);
        Edge<Vertex> e4 = new Edge<>(v1, v3, 9);
        Edge<Vertex> e5 = new Edge<>(v2, v4, 15);
        Edge<Vertex> e6 = new Edge<>(v3, v4, 10);

        Graph<Vertex, Edge<Vertex>> g = new Graph<>();
        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);
        g.addVertex(v4);

        g.addEdge(e2);
        g.addEdge(e3);
        g.addEdge(e1);
        g.addEdge(e4);
        g.addEdge(e5);
        g.addEdge(e6);

        g.diameter();


    }


    @Test
    public void testGetCenter1(){
        Graph<Vertex, Edge<Vertex>> g = new Graph<>();

        Vertex v1 = new Vertex(1, "A");
        Vertex v2 = new Vertex(2, "B");
        Vertex v3 = new Vertex(3, "C");
        Vertex v4 = new Vertex(4, "D");

        Edge<Vertex> e1 = new Edge<>(v1, v2, 9);
        Edge<Vertex> e2 = new Edge<>(v1, v3, 7);
        Edge<Vertex> e3 = new Edge<>(v1, v4, 10);

        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);
        g.addVertex(v4);
        g.addEdge(e1);
        g.addEdge(e2);
        g.addEdge(e3);
        assertEquals(v1,g.getCenter());
    }

    @Test
    public void testGetCenter2(){

        Graph<Vertex, Edge<Vertex>> g2 = new Graph<>();
        Vertex v1 = new Vertex(1, "A");
        Vertex v2 = new Vertex(2, "B");
        Vertex v3 = new Vertex(3, "C");
        Vertex v4 = new Vertex(4, "D");


        Edge<Vertex> e4 = new Edge<>(v1, v2, 1);
        Edge<Vertex> e5 = new Edge<>(v2, v3, 1);
        Edge<Vertex> e6 = new Edge<>(v3, v4, 1);
        Edge<Vertex> e7 = new Edge<>(v1, v4, 1);


        g2.addVertex(v1);
        g2.addVertex(v2);
        g2.addVertex(v3);
        g2.addVertex(v4);
        g2.addEdge(e4);
        g2.addEdge(e5);
        g2.addEdge(e6);
        g2.addEdge(e7);

        Set<Vertex> c1 = new HashSet<>();
        c1.add(v1);
        c1.add(v2);
        c1.add(v3);
        c1.add(v4);

        Vertex v = g2.getCenter();


        assertTrue(c1.contains(g2.getCenter()));
    }


    @Test
    public void testGetCenter() {
        Vertex v1 = new Vertex(1, "A");
        Vertex v2 = new Vertex(2, "B");
        Vertex v3 = new Vertex(3, "C");
        Vertex v4 = new Vertex(4, "D");
        Vertex v5 = new Vertex(5, "E");
        Vertex v6 = new Vertex(6, "F");
        Vertex v7 = new Vertex(7, "G");

        Edge<Vertex> e1 = new Edge<>(v1, v2, 9);
        Edge<Vertex> e2 = new Edge<>(v2, v3, 7);
        Edge<Vertex> e3 = new Edge<>(v1, v4, 10);
        Edge<Vertex> e4 = new Edge<>(v1, v3, 9);
        Edge<Vertex> e5 = new Edge<>(v2, v4, 15);
        Edge<Vertex> e6 = new Edge<>(v3, v7, 6);
        Edge<Vertex> e7 = new Edge<>(v3, v5, 7);
        Edge<Vertex> e8 = new Edge<>(v2, v5, 8);
        Edge<Vertex> e9 = new Edge<>(v2, v6, 4);
        Edge<Vertex> e10 = new Edge<>(v6, v7, 5);

        Graph<Vertex, Edge<Vertex>> g = new Graph<>();
        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);
        g.addVertex(v4);
        g.addVertex(v5);
        g.addVertex(v6);
        g.addVertex(v7);
        g.addEdge(e2);
        g.addEdge(e3);
        g.addEdge(e1);
        g.addEdge(e4);
        g.addEdge(e5);
        g.addEdge(e6);
        g.addEdge(e7);
        g.addEdge(e8);
        g.addEdge(e9);
        g.addEdge(e10);
        Vertex center = g.getCenter();


    }

    @Test
    public void testGetCenter3(){
        Vertex v1 = new Vertex(1, "A");
        Vertex v2 = new Vertex(2, "B");
        Vertex v3 = new Vertex(3, "C");
        Vertex v4 = new Vertex(4, "D");
        Vertex v5 = new Vertex(5, "E");
        Vertex v6 = new Vertex(6, "F");
        Vertex v7 = new Vertex(7, "E");
        Vertex v8 = new Vertex(8, "F");


        Edge<Vertex> e1 = new Edge<>(v1, v2, 1);
        Edge<Vertex> e2 = new Edge<>(v1, v3, 1);
        Edge<Vertex> e3 = new Edge<>(v1, v4, 1);
        Edge<Vertex> e4 = new Edge<>(v1, v5, 1);
        Edge<Vertex> e5 = new Edge<>(v5, v6, 1);
        Edge<Vertex> e6 = new Edge<>(v5, v7, 1);
        Edge<Vertex> e7 = new Edge<>(v5, v8, 1);

        Graph<Vertex, Edge<Vertex>> g = new Graph<>();
        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);
        g.addVertex(v4);
        g.addVertex(v5);
        g.addVertex(v6);
        g.addVertex(v7);
        g.addVertex(v8);
        g.addEdge(e2);
        g.addEdge(e3);
        g.addEdge(e1);
        g.addEdge(e4);
        g.addEdge(e5);
        g.addEdge(e6);
        g.addEdge(e7);

        Set<Vertex> center = new HashSet<>();
        center.add(v1);
        center.add(v5);

        assertTrue(center.contains(g.getCenter()));
    }

    @Test
    public void testGetCenter4(){
        Vertex v1 = new Vertex(1, "A");
        Vertex v2 = new Vertex(2, "B");
        Vertex v3 = new Vertex(3, "C");
        Vertex v4 = new Vertex(4, "D");
        Vertex v5 = new Vertex(5, "E");
        Vertex v6 = new Vertex(6, "F");
        Vertex v7 = new Vertex(7, "E");
        Vertex v8 = new Vertex(8, "F");


        Edge<Vertex> e1 = new Edge<>(v1, v2, 1);
        Edge<Vertex> e2 = new Edge<>(v1, v3, 1);
        Edge<Vertex> e3 = new Edge<>(v1, v4, 1);
        Edge<Vertex> e4 = new Edge<>(v1, v5, 1);
        Edge<Vertex> e5 = new Edge<>(v6, v7, 1);
        Edge<Vertex> e6 = new Edge<>(v7, v8, 1);

        Graph<Vertex, Edge<Vertex>> g = new Graph<>();
        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);
        g.addVertex(v4);
        g.addVertex(v5);
        g.addVertex(v6);
        g.addVertex(v7);
        g.addVertex(v8);
        g.addEdge(e2);
        g.addEdge(e3);
        g.addEdge(e1);
        g.addEdge(e4);
        g.addEdge(e5);
        g.addEdge(e6);

        assertEquals(v1,g.getCenter());
    }

    @Test
    public void testGetCenter5(){
        Vertex v1 = new Vertex(1, "A");
        Vertex v2 = new Vertex(2, "B");
        Vertex v3 = new Vertex(3, "C");
        Vertex v4 = new Vertex(4, "D");
        Vertex v5 = new Vertex(5, "E");
        Vertex v6 = new Vertex(6, "F");
        Vertex v7 = new Vertex(7, "E");
        Vertex v8 = new Vertex(8, "F");


        Edge<Vertex> e1 = new Edge<>(v1, v2, 1);
        Edge<Vertex> e2 = new Edge<>(v2, v3, 1);
        Edge<Vertex> e3 = new Edge<>(v3, v4, 1);
        Edge<Vertex> e4 = new Edge<>(v5, v6, 1);
        Edge<Vertex> e5 = new Edge<>(v6, v7, 1);
        Edge<Vertex> e6 = new Edge<>(v7, v8, 1);

        Graph<Vertex, Edge<Vertex>> g = new Graph<>();
        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);
        g.addVertex(v4);
        g.addVertex(v5);
        g.addVertex(v6);
        g.addVertex(v7);
        g.addVertex(v8);
        g.addEdge(e2);
        g.addEdge(e3);
        g.addEdge(e1);
        g.addEdge(e4);
        g.addEdge(e5);
        g.addEdge(e6);

        Set<Vertex> center = new HashSet<>();
        center.add(v2);
        center.add(v3);
        center.add(v6);
        center.add(v7);

        assertTrue(center.contains(g.getCenter()));
    }

    @Test
    public void testGetCenter6() {
        Vertex v1 = new Vertex(1, "A");
        Vertex v2 = new Vertex(2, "B");
        Vertex v3 = new Vertex(3, "C");
        Vertex v4 = new Vertex(4, "D");
        Vertex v5 = new Vertex(5, "E");
        Vertex v6 = new Vertex(6, "F");
        Vertex v7 = new Vertex(7, "E");
        Vertex v8 = new Vertex(8, "F");


        Edge<Vertex> e1 = new Edge<>(v1, v2, 1);
        Edge<Vertex> e2 = new Edge<>(v1, v3, 1);
        Edge<Vertex> e3 = new Edge<>(v1, v4, 1);
        Edge<Vertex> e4 = new Edge<>(v5, v6, 1);
        Edge<Vertex> e5 = new Edge<>(v7, v8, 1);

        Graph<Vertex, Edge<Vertex>> g = new Graph<>();
        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);
        g.addVertex(v4);
        g.addVertex(v5);
        g.addVertex(v6);
        g.addVertex(v7);
        g.addVertex(v8);
        g.addEdge(e2);
        g.addEdge(e3);
        g.addEdge(e1);
        g.addEdge(e4);
        g.addEdge(e5);

        assertEquals(v1,g.getCenter());
    }

    @Test
    public void testGetCenter7(){
        Vertex v1 = new Vertex(1, "A");
        Vertex v2 = new Vertex(2, "B");
        Vertex v3 = new Vertex(3, "C");
        Vertex v4 = new Vertex(4, "D");
        Vertex v5 = new Vertex(5, "E");
        Vertex v6 = new Vertex(6, "F");
        Vertex v7 = new Vertex(7, "G");
        Vertex v8 = new Vertex(8, "H");
        Vertex v9 = new Vertex(9, "I");
        Vertex v10 = new Vertex(10, "J");
        Vertex v11 = new Vertex(11, "K");
        Vertex v12 = new Vertex(12, "L");
        Vertex v13 = new Vertex(13, "M");


        Edge<Vertex> e1 = new Edge<>(v1, v2, 6);
        Edge<Vertex> e2 = new Edge<>(v2, v3, 7);
        Edge<Vertex> e3 = new Edge<>(v3, v4, 11);
        Edge<Vertex> e4 = new Edge<>(v1, v4, 9);
        Edge<Vertex> e5 = new Edge<>(v6, v7, 3);
        Edge<Vertex> e6 = new Edge<>(v7, v8, 5);
        Edge<Vertex> e7 = new Edge<>(v7, v9, 7);
        Edge<Vertex> e8 = new Edge<>(v10, v11, 11);
        Edge<Vertex> e9 = new Edge<>(v8, v10, 13);
        Edge<Vertex> e10 = new Edge<>(v12, v13, 13);
        Edge<Vertex> e11 = new Edge<>(v12, v5, 13);


        Graph<Vertex, Edge<Vertex>> g = new Graph<>();
        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);
        g.addVertex(v4);
        g.addVertex(v5);
        g.addVertex(v6);
        g.addVertex(v7);
        g.addVertex(v8);
        g.addVertex(v9);
        g.addVertex(v10);
        g.addVertex(v11);
        g.addVertex(v12);
        g.addVertex(v13);
        g.addEdge(e2);
        g.addEdge(e3);
        g.addEdge(e1);
        g.addEdge(e4);
        g.addEdge(e5);
        g.addEdge(e6);
        g.addEdge(e7);
        g.addEdge(e8);
        g.addEdge(e9);
        g.addEdge(e10);
        g.addEdge(e11);

        assertEquals(v8,g.getCenter());
    }
}
