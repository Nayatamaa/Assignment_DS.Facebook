/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package y1s2_assignment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class ConnectionGraph {

    private LinkedList<Vertex> vertices;
    DatabaseSQL database = new DatabaseSQL();
    private int index;

    public ConnectionGraph() {
        vertices = new LinkedList<>();
        index = 0;
    }

    public void addVertex(User users) {
        if (!vertexExists(users)) {
            vertices.add(new Vertex(users, index++));

        }
    }

    public void addEdge(User user, User friend) {
        if (user.equals(friend)) {
            return;
        }

        Vertex vertex1 = getVertex(user);
        Vertex vertex2 = getVertex(friend);

        if (vertex1 != null && vertex2 != null) {
            vertex1.addNeighbour(vertex2);
            vertex2.addNeighbour(vertex1);
        }
    }

    public void removeVertex(User user) {
        Vertex vertex = getVertex(user);
        if (vertex != null) {
            vertices.remove(vertex);
            for (Vertex neighbor : vertex.getNeighbours()) {
                neighbor.removeNeighbour(vertex);
            }
        }
    }

    public void removeEdge(User user1, User user2) {
        if (user1.equals(user2)) {
            return;
        }

        Vertex vertex1 = getVertex(user1);
        Vertex vertex2 = getVertex(user2);

        if (vertex1 != null && vertex2 != null) {
            vertex1.removeNeighbour(vertex2);
            vertex2.removeNeighbour(vertex1);
        }
    }

    public List<User> showFirstDegreeConnections(User user) {
        List<User> firstDegree = new ArrayList<>();
        Vertex vertex = findVertex(user);

        if (vertex != null) {
            for (Vertex neighbour : vertex.getNeighbours()) {
                firstDegree.add(neighbour.getUser());
            }
        } 
        return firstDegree;
    }

    public List<User> showSecondDegreeConnections(User loggedInUser) {
        List<User> secondDegree = new ArrayList<>();
        List<String> usernames = bfs(loggedInUser, 2);

        for (String username : usernames) {
            User user = database.getUser("UserName", username);
            if (user != null) {
                secondDegree.add(user);
            }
        }

        return secondDegree;
    }

    public List<User> showThirdDegreeConnections(User loggedInUser) {
        List<User> thirdDegree = new ArrayList<>();
        List<String> usernames = bfs(loggedInUser, 3);

        for (String username : usernames) {
            User user = database.getUser("UserName", username);
            if (user != null) {
                thirdDegree.add(user);
            }
        }

        return thirdDegree;
    }

    public List<User> getRecommendedConnections(User loggedInUser) {
        List<User> recommendedConnections = new ArrayList<>();
        Vertex loggedInUserVertex = findVertex(loggedInUser);

        if (loggedInUserVertex != null) {
            Set<Vertex> visited = new HashSet<>();
            Queue<Vertex> queue = new LinkedList<>();
            visited.add(loggedInUserVertex);
            int level = 1;

            queue.offer(loggedInUserVertex);

            while (!queue.isEmpty() && level <= 3) {
                int size = queue.size();

                for (int i = 0; i < size; i++) {
                    Vertex current = queue.poll();

                    for (Vertex neighbour : current.getNeighbours()) {
                        if (!visited.contains(neighbour)) {
                            visited.add(neighbour);
                            queue.offer(neighbour);
                            if (level == 2 || level == 3) {
                                recommendedConnections.add(neighbour.getUser());
                            }
                        }
                    }
                }
                level++;
            }

            for (User user : recommendedConnections) {
                Vertex userVertex = findVertex(user);

            }
        }

        return recommendedConnections;
    }

    private List<String> bfs(User loggedInUser, int degree) {
        List<String> connections = new ArrayList<>();
        Vertex loggedInUserVertex = findVertex(loggedInUser);

        if (loggedInUserVertex != null) {
            Set<Vertex> visited = new HashSet<>();
            Queue<Vertex> queue = new LinkedList<>();
            visited.add(loggedInUserVertex);
            int level = 1;

            queue.offer(loggedInUserVertex);

            while (!queue.isEmpty() && level <= degree) {
                int size = queue.size();

                for (int i = 0; i < size; i++) {
                    Vertex current = queue.poll();

                    for (Vertex neighbour : current.getNeighbours()) {
                        if (!visited.contains(neighbour)) {
                            visited.add(neighbour);
                            queue.offer(neighbour);
                            if (level == degree) {
                                connections.add(neighbour.getUser().getUsername());
                            }
                        }
                    }
                }
                level++;
            }
        }

        return connections;
    }

    Vertex findVertex(User user) {
        for (Vertex vertex : vertices) {
            if (vertex.getUser().equals(user)) {
                return vertex;
            }
        }
        return null;
    }

    public int countMutualConnections(Vertex vertex1, Vertex vertex2) {
        int count = 0;
        LinkedList<Vertex> neighbours1 = vertex1.getNeighbours();
        LinkedList<Vertex> neighbours2 = vertex2.getNeighbours();

        for (Vertex neighbour1 : neighbours1) {
            if (neighbours2.contains(neighbour1)) {
                count++;
            }
        }

        return count;
    }

    public int getShortestDistance(User user1, User user2) {
        Vertex vertex1 = getVertex(user1);
        Vertex vertex2 = getVertex(user2);

        if (vertex1 != null && vertex2 != null) {
            LinkedList<Vertex> queue = new LinkedList<>();
            boolean[] visited = new boolean[vertices.size()];
            int[] distance = new int[vertices.size()];

            queue.add(vertex1);
            visited[vertex1.getIndex()] = true;

            while (!queue.isEmpty()) {
                Vertex current = queue.poll();

                if (current.equals(vertex2)) {
                    return distance[current.getIndex()];
                }

                LinkedList<Vertex> neighbours = current.getNeighbours();
                int currentDistance = distance[current.getIndex()];
                int nextDistance = currentDistance + 1;

                for (Vertex neighbour : neighbours) {
                    int neighbourIndex = neighbour.getIndex();
                    if (!visited[neighbourIndex]) {
                        queue.add(neighbour);
                        visited[neighbourIndex] = true;
                        distance[neighbourIndex] = nextDistance;
                    }
                }
            }
        }

        return -1; // Return -1 if the shortest distance is not found
    }

    // Check whether the graph works
    public String showAdjacency() {
        StringBuilder sb = new StringBuilder();

        for (Vertex vertex : vertices) {
            User user = vertex.getUser();
            sb.append(user.getUsername()).append(":");
            List<Vertex> neighbours = vertex.getNeighbours();
            for (Vertex neighbour : neighbours) {
                User neighbourUser = neighbour.getUser();
                sb.append(neighbourUser.getUsername()).append(",");
            }

            if (!neighbours.isEmpty()) {
                sb.deleteCharAt(sb.length() - 1);
            }
            sb.append("\n");
        }

        sb.append("Adjacency list generated successfully.");
        return sb.toString();
    }

    private boolean vertexExists(User user) {
        for (Vertex vertex : vertices) {
            if (vertex.getUser().equals(user)) {
                return true;
            }
        }
        return false;
    }

    private Vertex getVertex(User user) {
        for (Vertex vertex : vertices) {
            if (vertex.getUser().equals(user)) {
                return vertex;
            }
        }
        return null;

    }

    public class Vertex {

        private User user;
        private int index;
        private LinkedList<Vertex> neighbours;
        private int degree;

        public Vertex(User user, int index) {
            this.user = user;
            this.index = index;
            this.neighbours = new LinkedList<>();
        }

        public User getUser() {
            return user;
        }

        public int getIndex() {
            return index;
        }

        public LinkedList<Vertex> getNeighbours() {
            return neighbours;
        }

        public void addNeighbour(Vertex neighbour) {
            if (!neighbours.contains(neighbour)) {
                neighbours.add(neighbour);
            }
        }

        public void removeNeighbour(Vertex neighbour) {
            neighbours.remove(neighbour);
        }

        public int getDegree() {
            return degree;
        }

        public void setDegree(int degree) {
            this.degree = degree;
        }

    }
}
