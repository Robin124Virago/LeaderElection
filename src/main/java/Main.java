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

        // Create nodes and add them to the shared list
        Node node1 = new Node(1, allNodes);
        Node node2 = new Node(2, allNodes);
        Node node3 = new Node(3, allNodes);
        Node node4 = new Node(4, allNodes);

        allNodes.add(node1);
        allNodes.add(node2);
        allNodes.add(node3);
        allNodes.add(node4);

//        RealisticNode node1 = new RealisticNode(1, allRealisticNodes);
//        RealisticNode node2 = new RealisticNode(2, allRealisticNodes);
//        RealisticNode node3 = new RealisticNode(3, allRealisticNodes);
//        RealisticNode node4 = new RealisticNode(4, allRealisticNodes);
//
//        allRealisticNodes.add(node1);
//        allRealisticNodes.add(node2);
//        allRealisticNodes.add(node3);
//        allRealisticNodes.add(node4);

        // Simulate this higher node as inactive
        node4.setActive(false);

        // Use an ExecutorService to run nodes in parallel
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(node1);
        executorService.execute(node2);
        executorService.execute(node3);

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
