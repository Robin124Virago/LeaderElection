// Main.java
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {

        // Create a shared list of nodes
        List<Node> allNodes = new ArrayList<>();
        List<RealisticNode> allRealisticNodes = new ArrayList<>();

        // ExecutorService to run nodes in parallel
        ExecutorService executorService = Executors.newCachedThreadPool();

        int numberOfNodes = 10; // Change this to the number of nodes you want to create

        boolean RealisticNodes = false; // Set this to false to use Node instead of RealisticNode

        if (RealisticNodes) {
            for (int i = 1; i <= numberOfNodes; i++) {
                RealisticNode node = new RealisticNode(i, allRealisticNodes);
                allRealisticNodes.add(node);
            }

            // Set node4 as inactive
            RealisticNode node4 = allRealisticNodes.stream()
                    .filter(node -> node.getId() == 4)
                    .findFirst()
                    .orElse(null);

            if (node4 != null) {
                node4.setActive(false);
            }

            for (RealisticNode node : allRealisticNodes) {
                if (node.isActive()) {
                    executorService.execute(node);
                }
            }
        } else {
            for (int i = 1; i <= numberOfNodes; i++) {
                Node node = new Node(i, allNodes);
                allNodes.add(node);
            }

            // Set node4 as inactive
            Node node4 = allNodes.stream()
                    .filter(node -> node.getId() == 4)
                    .findFirst()
                    .orElse(null);

            if (node4 != null) {
                node4.setActive(false);
            }

            for (Node node : allNodes) {
                if (node.isActive()) {
                    executorService.execute(node);
                }
            }
        }

        // Wait for the election process to complete
        try {
            executorService.shutdown();
            executorService.awaitTermination(5, TimeUnit.SECONDS); // Wait for 5 seconds to ensure all elections complete
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check which node has become the coordinator
        Node coordinator = allNodes.stream()
                .filter(Node::isCoordinator)
                .findFirst()
                .orElse(null);

        if (coordinator != null) {
            System.out.println("The elected leader is: Node " + coordinator.getId());
        } else {
            System.out.println("No coordinator elected.");
        }
    }
}