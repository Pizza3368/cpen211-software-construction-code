package cpen221.mp2;

import cpen221.mp2.graph.ALGraph;
import cpen221.mp2.graph.Edge;
import cpen221.mp2.graph.Vertex;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ALGraphTest {
    public static ALGraph<Vertex, Edge<Vertex>> alGraph1;

    public static Vertex v1 = new Vertex(1, "A");
    public static Vertex v2 = new Vertex(2, "B");
    public static Vertex v3 = new Vertex(3, "C");
    public static Vertex v4 = new Vertex(4, "D");
    public static Vertex v5 = new Vertex(5, "E");
    public static Vertex v6 = new Vertex(6, "F");
    public static Vertex v7 = new Vertex(7, "G");
    public static Vertex v8 = new Vertex(8, "H");


    public static Edge<Vertex> e1 = new Edge<>(v1, v2, 5);
    public static Edge<Vertex> e2 = new Edge<>(v2, v3, 7);
    public static Edge<Vertex> e3 = new Edge<>(v1, v4, 9);
    public static Edge<Vertex> e4 = new Edge<>(v5, v6, 2);
    public static Edge<Vertex> e5 = new Edge<>(v6, v7, 3);
    public static Edge<Vertex> e6 = new Edge<>(v7, v8, 5);
    public static Edge<Vertex> e7 = new Edge<>(v4, v8, 7);


    @BeforeAll
    public static void graphGenerate(){

        alGraph1 = new ALGraph<>();

        alGraph1.addVertex(v1);
        alGraph1.addVertex(v2);
        alGraph1.addVertex(v3);
        alGraph1.addVertex(v4);
        alGraph1.addEdge(e1);
        alGraph1.addEdge(e2);
        alGraph1.addEdge(e3);

    }

    @Test
    public void testEdgeOfALGraph1(){

        ALGraph<Vertex, Edge<Vertex>> copy = new ALGraph<>();

        copy.addVertex(v1);
        copy.addVertex(v2);
        copy.addVertex(v3);
        copy.addVertex(v4);
        copy.addEdge(e1);
        copy.addEdge(e2);
        copy.addEdge(e3);

        assertFalse(alGraph1.addEdge(e1));
        assertFalse(alGraph1.addEdge(e7));
        assertFalse(alGraph1.vertex(v8));
        assertFalse(copy.addEdge(e6));

        assertTrue(alGraph1.edge(v1,v2));
        assertFalse(alGraph1.edge(v1,v1));
        assertFalse(alGraph1.edge(v7,v8));
        assertTrue(alGraph1.edge(e2));
    }

    @Test
    public void testVertexOfALGraph1(){

        ALGraph<Vertex, Edge<Vertex>> copy = new ALGraph<>();

        copy.addVertex(v1);
        copy.addVertex(v2);
        copy.addVertex(v3);
        copy.addVertex(v4);
        copy.addEdge(e1);
        copy.addEdge(e2);
        copy.addEdge(e3);

        assertTrue(alGraph1.vertex(v2));
        assertFalse(alGraph1.vertex(v8));
        assertFalse(alGraph1.addVertex(v1));
        assertTrue(copy.addVertex(v8));
    }

    @Test
    public void testLengthOfALGraph1(){

        assertEquals(-1, alGraph1.edgeLength(v3, v4));
        assertEquals(5, alGraph1.edgeLength(v1, v2));
        assertEquals(-1, alGraph1.edgeLength(v3, v3));
    }

    @Test
    public void testEdgeLengthSum(){
        assertEquals(21, alGraph1.edgeLengthSum());

    }

    @Test
    public void testRemove(){

        ALGraph<Vertex, Edge<Vertex>> copy= new ALGraph<>();
        copy.addVertex(v1);
        copy.addVertex(v2);
        copy.addVertex(v3);
        copy.addVertex(v4);
        copy.addEdge(e1);
        copy.addEdge(e2);
        copy.addEdge(e3);

        assertTrue(copy.remove(e3));
        assertFalse(copy.remove(e3));
        assertTrue(copy.remove(v1));
        assertFalse(copy.remove(v8));
        assertFalse(copy.remove(e7));
        assertFalse(copy.vertex(v1));
        assertFalse(copy.edge(e3));


    }

    @Test
    public void testAllVertexALGraph1(){
        Set<Vertex> allVertex1=new HashSet<>();

        allVertex1.add(v1);
        allVertex1.add(v2);
        allVertex1.add(v3);
        allVertex1.add(v4);


        for(Vertex i: allVertex1){
            assertTrue(alGraph1.allVertices().contains(i));
        }
        allVertex1.add(v8);
        assertFalse(alGraph1.vertex(v8));
    }

    @Test
    public void testAllEdgeALGraph1(){
        Set<Edge<Vertex>> allEdge1=new HashSet<>();
        Set<Edge<Vertex>> allEdge1Ofv1=new HashSet<>();

        allEdge1.add(e1);
        allEdge1.add(e2);
        allEdge1.add(e3);

        allEdge1Ofv1.add(e1);
        allEdge1Ofv1.add(e3);

        for(Edge i: allEdge1){
            assertTrue(alGraph1.allEdges().contains(i));
        }
        assertFalse(alGraph1.allEdges().contains(e4));

        for(Edge i: allEdge1Ofv1){
            assertTrue(alGraph1.allEdges(v1).contains(i));
        }

        assertFalse(alGraph1.allEdges(v1).contains(e4));
        assertFalse(alGraph1.allEdges(v1).contains(e2));
        allEdge1Ofv1.add(e7);
        allEdge1.add(e4);
        assertFalse(alGraph1.edge(e7));
        assertFalse(alGraph1.edge(e4));


    }

    @Test
    public void testGetNeighboursALGraph1(){
        HashMap<Vertex,Edge<Vertex>> neighbourOfv1= new HashMap<>();

        neighbourOfv1.put(v2, e1);
        neighbourOfv1.put(v4, e3);

        for(Vertex i: neighbourOfv1.keySet()){
            assertTrue(alGraph1.getNeighbours(v1).containsKey(i));
            assertTrue(alGraph1.getNeighbours(v1).containsValue(neighbourOfv1.get(i)));
        }
        neighbourOfv1.put(v4,e7);
        assertFalse(alGraph1.edge(e7));
        assertFalse(alGraph1.vertex(v8));


    }
}
