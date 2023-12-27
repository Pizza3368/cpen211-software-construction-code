package cpen221.mp2;

import cpen221.mp2.graph.AMGraph;
import cpen221.mp2.graph.Edge;
import cpen221.mp2.graph.Vertex;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class AMGraphTest {
    public static AMGraph<Vertex, Edge<Vertex>> amGraph1;
    public static AMGraph<Vertex, Edge<Vertex>> amGraph2;

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
    public static Edge<Vertex> e6 = new Edge<>(v7, v8, 5);
    public static Edge<Vertex> e7 = new Edge<>(v4, v8, 7);


    @BeforeAll
    public static void graphGenerate(){

        int maxId=4;

        amGraph1 = new AMGraph<>(maxId);
        amGraph2 = new AMGraph<>(maxId);

        assertTrue(amGraph1.addVertex(v1));
        amGraph1.addVertex(v2);
        amGraph1.addVertex(v3);
        amGraph1.addVertex(v4);
        amGraph1.addEdge(e1);
        amGraph1.addEdge(e2);
        amGraph1.addEdge(e3);
        assertFalse(amGraph1.addEdge(e3));
        assertFalse(amGraph1.addEdge(e7));

        amGraph2.addVertex(v1);
        amGraph2.addVertex(v2);
        amGraph2.addVertex(v3);
        amGraph2.addVertex(v4);

        amGraph2.addEdge(e1);
        amGraph2.addEdge(e6);
    }

    @Test
    public void testEdgeOfAMGraph1(){

        AMGraph<Vertex, Edge<Vertex>> copy = amGraph1;

        assertFalse(amGraph1.addEdge(e1));
        assertFalse(copy.addEdge(e6));

        assertTrue(amGraph1.edge(v1,v2));
        assertFalse(amGraph1.addEdge(e7));
        assertTrue(amGraph1.edge(e2));
        assertFalse(amGraph1.edge(v7,v8));
    }

    @Test
    public void testVertexOfAMGraph1(){

        AMGraph<Vertex, Edge<Vertex>> copy = new AMGraph<>(4);
        copy.addVertex(v1);
        copy.addVertex(v2);
        copy.addVertex(v3);
        copy.addVertex(v4);
        copy.addEdge(e1);
        copy.addEdge(e2);
        copy.addEdge(e3);


        assertTrue(amGraph1.vertex(v2));
        assertFalse(amGraph1.vertex(v8));
        assertFalse(copy.addVertex(v1));
        assertFalse(copy.addVertex(v8));
        assertFalse(copy.vertex(v8));
    }

    @Test
    public void testLengthOfAMGraph1(){

        assertEquals(0, amGraph1.edgeLength(v3, v4));
        assertEquals(0, amGraph1.edgeLength(v3, v3));
        assertEquals(9, amGraph1.edgeLength(v1, v4));
    }

    @Test
    public void testEdgeLengthSum(){
        assertEquals(21, amGraph1.edgeLengthSum());
    }
    @Test
    public void testAllVertexAMGraph1(){
        Set<Vertex> allVertex1=new HashSet<>();

        allVertex1.add(v1);
        allVertex1.add(v2);
        allVertex1.add(v3);
        allVertex1.add(v4);

        for(Vertex i: allVertex1){
            assertTrue(amGraph1.allVertices().contains(i));
        }
        allVertex1.add(v8);
        assertFalse(amGraph1.vertex(v8));
    }

    @Test
    public void testRemove(){
        AMGraph<Vertex, Edge<Vertex>> copy= amGraph1;

        assertTrue(copy.remove(e3));
        assertTrue(copy.remove(v1));
        assertFalse(copy.remove(v8));
        assertFalse(copy.remove(e7));
        assertFalse(copy.vertex(v1));
        assertFalse(copy.edge(e3));

    }
    @Test
    public void testAllEdgeAMGraph1(){
        Set<Edge<Vertex>> allEdge1=new HashSet<>();
        Set<Edge<Vertex>> allEdge1Ofv1=new HashSet<>();

        allEdge1.add(e1);
        allEdge1.add(e2);
        allEdge1.add(e3);

        allEdge1Ofv1.add(e1);
        allEdge1Ofv1.add(e3);

        for(Edge i: allEdge1){
            assertTrue(amGraph1.allEdges().contains(i));
        }

        for(Edge i: allEdge1Ofv1){
            assertTrue(amGraph1.allEdges(v1).contains(i));
        }

        assertFalse(amGraph1.allEdges(v1).contains(e2));
        allEdge1Ofv1.add(e7);
        allEdge1.add(e4);
        assertFalse(amGraph1.edge(e7));
        assertFalse(amGraph1.edge(e4));
    }

    @Test
    public void testGetNeighboursAMGraph1(){
        HashMap<Vertex,Edge<Vertex>> neighbourOfv1= new HashMap<>();


        neighbourOfv1.put(v2, e1);
        neighbourOfv1.put(v4, e3);

        for(Vertex i: neighbourOfv1.keySet()){
            assertTrue(amGraph1.getNeighbours(v1).containsKey(i));
            assertTrue(amGraph1.getNeighbours(v1).containsValue(neighbourOfv1.get(i)));
        }
        neighbourOfv1.put(v4,e7);
        assertFalse(amGraph1.edge(e7));
        assertFalse(amGraph1.vertex(v8));
    }

    @Test
    public void testGetNeighboursAMGraph2(){
        HashMap<Vertex,Edge<Vertex>> neighbourOfv2= new HashMap<>();


        neighbourOfv2.put(v1, e1);

        for(Vertex i: neighbourOfv2.keySet()){
            assertTrue(amGraph2.getNeighbours(v2).containsKey(i));
            assertTrue(amGraph2.getNeighbours(v2).containsValue(neighbourOfv2.get(i)));
        }
        neighbourOfv2.put(v4,e7);
        assertFalse(amGraph2.edge(e7));
        assertFalse(amGraph2.vertex(v8));
    }
}
