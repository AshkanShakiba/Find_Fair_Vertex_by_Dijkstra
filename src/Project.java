import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Project {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int verticesCount = scanner.nextInt();
        int edgesCount = scanner.nextInt();
        ArrayList<Vertex> vertices = new ArrayList<>();
        for (int i = 0; i < verticesCount; i++) {
            Vertex vertex=new Vertex(scanner.nextInt());
            vertices.add(vertex);
        }
        Graph graph = new Graph(edgesCount, verticesCount, vertices);
        for (int i = 0; i < edgesCount; i++) {
            int srcNumber = scanner.nextInt();
            int dstNumber = scanner.nextInt();
            int weight = scanner.nextInt();
            graph.addEdge(graph.findVertex(srcNumber), graph.findVertex(dstNumber), weight);
        }
        ArrayList<Vertex> path = graph.DFS(new ArrayList<>(), vertices.get(0));
        for (Vertex vertex : path) {
            System.out.print(vertex.number + " ");
        }
    }
}

class Vertex {
    int number;
    boolean isVisited;
    ArrayList<Vertex> neighbors;

    Vertex(int number) {
        this.number = number;
        isVisited = false;
        neighbors = new ArrayList<>();
    }

    void visit() {
        isVisited = true;
    }

    void addNeighbor(Vertex neighbor) {
        neighbors.add(neighbor);
    }
}

class Edge {
    Vertex vertex;
    double weight;

    Edge(Vertex vertex, double weight) {
        this.vertex = vertex;
        this.weight = weight;
    }
}

class Graph {
    int edgesCount;
    int verticesCount;
    ArrayList<Vertex> vertices;
    HashMap<Vertex, ArrayList<Edge>> edgesMap;

    Graph(int edgesCount, int verticesCount, ArrayList<Vertex> vertices) {
        this.edgesCount = edgesCount;
        this.verticesCount = verticesCount;
        this.vertices = vertices;
        edgesMap = new HashMap<>();
        for (Vertex vertex : vertices) {
            edgesMap.put(vertex, new ArrayList<>());
        }
    }

    void addEdge(Vertex src, Vertex dst, int weight) {
        edgesMap.get(src).add(new Edge(dst, weight));
        edgesMap.get(dst).add(new Edge(src, weight));
        src.addNeighbor(dst);
        dst.addNeighbor(src);
    }

    Vertex findVertex(int number){
        for(Vertex vertex:vertices)
            if(vertex.number==number)
                return vertex;
        return null;
    }

    ArrayList<Vertex> DFS(ArrayList<Vertex> path, Vertex vertex) {
        vertex.visit();
        path.add(vertex);
        if (vertex.neighbors.size() == 0)
            return path;
        for (Vertex neighbor : vertex.neighbors) {
            if (!neighbor.isVisited)
                DFS(path, neighbor);
        }
        return path;
    }
}
