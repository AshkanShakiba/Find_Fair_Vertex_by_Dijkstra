import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class Project {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int verticesCount = scanner.nextInt();
        int edgesCount = scanner.nextInt();
        ArrayList<Vertex> vertices = new ArrayList<>();
        for (int i = 0; i < verticesCount; i++) {
            Vertex vertex = new Vertex(scanner.nextInt());
            vertices.add(vertex);
        }
        Graph graph = new Graph(edgesCount, verticesCount, vertices);
        for (int i = 0; i < edgesCount; i++) {
            int srcNumber = scanner.nextInt();
            int dstNumber = scanner.nextInt();
            int weight = scanner.nextInt();
            graph.addEdge(graph.findVertex(srcNumber), graph.findVertex(dstNumber), weight);
        }
        graph.initialize();
        int number;
        String command;
        while (true) {
            command = scanner.next();
            if (command.equals("test")) {
                ArrayList<Vertex> path = graph.DFS(new ArrayList<>(), vertices.get(0));
                for (Vertex vertex : path) {
                    System.out.print(vertex.number + " ");
                }
                System.out.println();
                graph.resetVisitFields();
            } else if (command.equals("join")) {
                number = scanner.nextInt();
                graph.join(number);
                for (Vertex vertex : graph.getPlace()) System.out.print(vertex.number + " ");
                System.out.println();
            } else if (command.equals("left")) {
                number = scanner.nextInt();
                graph.left(number);
                for (Vertex vertex : graph.getPlace()) System.out.print(vertex.number + " ");
                System.out.println();
            } else if (command.equals("exit")) break;
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

    void reset() {
        isVisited = false;
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
    HashMap<Vertex, HashMap<Vertex, Double>> dijkstra;
    ArrayList<Vertex> newVertices;
    ArrayList<Vertex> notJoined;

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

    Vertex findVertex(int number) {
        for (Vertex vertex : vertices)
            if (vertex.number == number)
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

    void resetVisitFields() {
        for (Vertex vertex : vertices) {
            vertex.reset();
        }
    }

    public HashMap<Vertex, Double> dijkstra(Vertex vertex) {
        HashMap<Vertex, Double> distances = new HashMap<>();
        for (Vertex vertex2 : vertices) {
            distances.put(vertex2, Double.MAX_VALUE);
        }
        distances.put(vertex, 0.0);
        ArrayList<Vertex> notVisited = new ArrayList<>(vertices);
        while (!notVisited.isEmpty()) {
            Vertex vertex1 = null;
            double finalDistance = Integer.MAX_VALUE;
            for (Vertex vertex2 : notVisited) {
                if (distances.get(vertex2) < finalDistance) {
                    vertex1 = vertex2;
                    finalDistance = distances.get(vertex2);
                }
            }
            if (vertex1 == null) break;
            notVisited.remove(vertex1);
            for (Edge edge : edgesMap.get(vertex1)) {
                double currentDistance = distances.get(vertex1) + edge.weight;
                if (currentDistance < distances.get(edge.vertex)) {
                    distances.put(edge.vertex, currentDistance);
                }
            }
        }
        return distances;
    }

    void initialize() {
        dijkstra(vertices.get(0));
        dijkstra = new HashMap<>();
        newVertices = new ArrayList<>();
        notJoined = new ArrayList<>(vertices);
    }

    void join(int number) {
        dijkstra.put(findVertex(number), dijkstra(findVertex(number)));
        newVertices.add(findVertex(number));
        notJoined.remove(findVertex(number));
    }

    void left(int number) {
        dijkstra.remove(findVertex(number));
        newVertices.remove(findVertex(number));
        notJoined.add(findVertex(number));
    }

    HashSet<Vertex> getPlace() {
        float minimum = Float.MAX_VALUE;
        HashSet<Vertex> result = new HashSet<>();
        float score = 0;
        if (newVertices.size() == 1)
            result.add(newVertices.get(0));
        for (Vertex vertex1 : newVertices) {
            for (Vertex vertex2 : newVertices) {
                for (Vertex vertex3 : vertices) {
                    if (vertex1.equals(vertex2)) continue;
                    score = (float) Math.abs(dijkstra.get(vertex1).get(vertex3) - dijkstra.get(vertex2).get(vertex3));
                    // score = score / (float) newVertices.size();
                    if (score < minimum) {
                        result.clear();
                        result.add(vertex3);
                        minimum = score;
                    } else if (score == minimum) {
                        result.add(vertex3);
                    }
                }
            }
        }
        return result;
    }
}
