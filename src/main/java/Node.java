import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Node implements Runnable {
    private int id;
    private boolean isCoordinator;
    private boolean isActive;
    private boolean receivedOk;
    private List<Node> allNodes;
    private static volatile boolean coordinatorElected = false; // Shared flag not allowed
    private ExecutorService executorService;

    public Node(int id, List<Node> allNodes) {
        this.id = id;
        this.isCoordinator = false;
        this.isActive = true;
        this.receivedOk = false;
        this.allNodes = allNodes;
        this.executorService = Executors.newCachedThreadPool();
    }

    public int getId() {
        return id;
    }

    public boolean isCoordinator() {
        return isCoordinator;
    }

    public void setCoordinator(boolean coordinator) {
        isCoordinator = coordinator;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void initiateElection() {
        if (isActive && !coordinatorElected) {
            executorService.execute(() -> {
                System.out.println("Node " + id + " initiates election.");
                receivedOk = false;

                // Send election message to higher nodes asynchronously
                for (Node node : allNodes) {
                    if (node.getId() > id && node.isActive()) {
                        executorService.execute(() -> node.receiveElection(id));
                    }
                }

                // If no OK message received within a certain time, declare self as coordinator
                try {
                    TimeUnit.SECONDS.sleep(1); // Wait for 1 second
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (!receivedOk && !coordinatorElected) {
                    System.out.println("Node " + id + " becomes coordinator.");
                    isCoordinator = true;
                    coordinatorElected = true; // Set the flag
                    announceCoordinator();
                }
            });
        }
    }

    public void receiveElection(int initiatingNodeId) {
        if (isActive && !coordinatorElected) {
            System.out.println("Node " + id + " receives election from Node " + initiatingNodeId);

            // Send OK message to initiating node asynchronously
            executorService.execute(() -> sendOkMessage(initiatingNodeId));

            // Start a new election if this node's ID is higher
            if (id > initiatingNodeId) {
                initiateElection();
            }
        }
    }

    public void sendOkMessage(int initiatingNodeId) {
        if (!coordinatorElected) {
            System.out.println("Node " + id + " sends OK message to Node " + initiatingNodeId);
            for (Node node : allNodes) {
                if (node.getId() == initiatingNodeId) {
                    node.receiveOk();
                    break;
                }
            }
        }
    }

    public void receiveOk() {
        System.out.println("Node " + id + " receives OK message.");
        receivedOk = true;
    }

    public void announceCoordinator() {
        if (isCoordinator) {
            System.out.println("Node " + id + " announces itself as coordinator.");
            for (Node node : allNodes) {
                if (node.getId() != id) {
                    node.setCoordinator(false);
                }
            }
            coordinatorElected = true; // Set the flag to prevent further announcements
            System.exit(0);
        }
    }

    @Override
    public void run() {
        // Nodes run in their own threads, waiting to start the election
        initiateElection();
    }
}
