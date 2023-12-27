package cpen221.mp2;

import cpen221.mp2.graph.Edge;
import cpen221.mp2.graph.Graph;
import cpen221.mp2.graph.Vertex;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class GraphTask3Test {
    public static Graph<Vertex, Edge<Vertex>> graph1;
    public static Graph<Vertex, Edge<Vertex>> graph2;

    public static Vertex v1 = new Vertex(1, "A");
    public static Vertex v2 = new Vertex(2, "B");
    public static Vertex v3 = new Vertex(3, "C");
    public static Vertex v4 = new Vertex(4, "D");

    public static Edge<Vertex> e1 = new Edge<>(v1, v2, 5);
    public static Edge<Vertex> e2 = new Edge<>(v1, v3, 2);
    public static Edge<Vertex> e3 = new Edge<>(v1, v4, 12);
    public static Edge<Vertex> e4 = new Edge<>(v2, v3, 7);
    public static Edge<Vertex> e5 = new Edge<>(v2, v4, 11);
    public static Edge<Vertex> e6 = new Edge<>(v3, v4, 2);

    @BeforeAll
    public static void graphGenerate(){
        graph1 = new Graph<>();
        graph2 = new Graph<>();

        graph1.addVertex(v1);
        graph1.addVertex(v2);
        graph1.addVertex(v3);
        graph1.addVertex(v4);

        graph1.addEdge(e1);
        graph1.addEdge(e2);
        graph1.addEdge(e3);
        graph1.addEdge(e4);
        graph1.addEdge(e5);
        graph1.addEdge(e6);

        graph2.addVertex(v1);
        graph2.addVertex(v2);
        graph2.addVertex(v3);
        graph2.addVertex(v4);

        graph2.addEdge(e1);
        graph2.addEdge(e6);

        graph2.edge(e1);
        graph2.edge(v1,v2);

        graph2.edgeLengthSum();
        graph2.edgeLength(v1,v2);
        graph2.remove(v1);
        graph2.allEdges(v1);
        graph2.allEdges();
        graph2.getNeighbours(v1);
        graph2.remove(e1);
        graph2.vertex(v1);

    }

    @Test
    public void testGetEdge(){
        assertEquals(e1,graph1.getEdge(v1,v2));
        assertEquals(e3,graph1.getEdge(v1,v4));
    }

    @Test
    public void testShortestPath13(){
        List<Vertex> shortestOfv1v3=new ArrayList<>();
        List<Vertex> outPut;
        outPut = graph1.shortestPath(v1, v3);

        shortestOfv1v3.add(v1);
        shortestOfv1v3.add(v3);

        assertEquals(shortestOfv1v3.size(), outPut.size());
        assertEquals(graph1.pathLength(outPut), graph1.pathLength(shortestOfv1v3));
        for(int i=0; i<shortestOfv1v3.size(); i++){
            assertEquals(shortestOfv1v3.get(i), outPut.get(i));
        }


    }


    @Test
    public void testShortestPath14(){
        List<Vertex> shortestOfv1v4=new ArrayList<>();
        List<Vertex> outPut;

        shortestOfv1v4.add(v1);
        shortestOfv1v4.add(v3);
        shortestOfv1v4.add(v4);

        outPut =  graph1.shortestPath(v1, v4);

        assertEquals(shortestOfv1v4.size(),outPut.size());
        assertEquals(graph1.pathLength(outPut), graph1.pathLength(shortestOfv1v4));
        for(int i=0; i<outPut.size(); i++){
            assertEquals(shortestOfv1v4.get(i), outPut.get(i));
        }
    }

    @Test
    public void testShortestPathWithSameVertex(){
        List<Vertex> shortestOfv1v1=new ArrayList<>();
        List<Vertex> outPut;

        shortestOfv1v1.add(v1);

        outPut =  graph1.shortestPath(v1, v1);

        assertEquals(shortestOfv1v1.size(), outPut.size());
        for(int i=0; i<outPut.size(); i++){
            assertEquals(shortestOfv1v1.get(i), outPut.get(i));
        }
    }

    @Test
    public void seperategraphsmoke(){
        List<Vertex> shortestOfv1v4=new ArrayList<>();
        List<Vertex> outPut;

        graph2.seperateGraph();

    }

    @Test
    public void testGetNeighbours(){
        Map<Vertex,Edge<Vertex>> outPut1;
        Map<Vertex,Edge<Vertex>> outPut2;
        Map<Vertex,Edge<Vertex>> outPut3;
        Map<Vertex,Edge<Vertex>>NeighboursV1Graph1R5 = new HashMap<>();
        Map<Vertex,Edge<Vertex>>NeighboursV1Graph1R1 = new HashMap<>();
        Map<Vertex,Edge<Vertex>>NeighboursV1Graph2R5 = new HashMap<>();

        NeighboursV1Graph1R5.put(v2,e1);
        NeighboursV1Graph1R5.put(v3,e2);
        NeighboursV1Graph1R5.put(v4,e6);

        NeighboursV1Graph2R5.put(v2,e1);

        outPut1 = graph1.getNeighbours(v1, 5);
        outPut2 = graph1.getNeighbours(v1, 1);
        outPut3 = graph2.getNeighbours(v1, 5);

        assertEquals(NeighboursV1Graph1R5.size(),outPut1.size());
        assertEquals(0,outPut2.size());
        assertEquals(0,outPut3.size());


    }
}
