# Leader Election for Asynchronous Systems

Leader Election for Asynchronous Systems is a Java application that simulates the leader election process in an asynchronous distributed system using the **Bully Algorithm**. The solution consists of two main components: the **Main** class and the **RealisticNode** class, which collectively manage the election process among a group of nodes.

## Features

- **Initiate Election**: Nodes can initiate the election process by sending election messages to higher-ID nodes.
- **Receive Election**: Nodes handle incoming election messages and respond with "OK" messages.
- **Send OK Message**: Nodes send "OK" messages to the initiating node with a probability of message loss and random delays.
- **Announce Coordinator**: Nodes announce themselves as the coordinator if elected.
- **Asynchronous Execution**: Nodes operate asynchronously, simulating real-world conditions with message delays and potential losses.
- **Node Management**: Nodes are managed with unique IDs and can be set as inactive to simulate potential failures.

### RealisticNode

#### Attributes
- `id`: Unique identifier for the node.
- `isCoordinator`: Flag indicating whether the node is the coordinator.
- `isActive`: Flag indicating if the node is active in the system.
- `receivedOk`: Flag indicating whether the node has received an "OK" message.
- `allNodes`: List of all nodes in the system.
- `coordinatorElected`: Shared flag to track if a coordinator has been elected.
- `executorService`: Executor service for asynchronous task execution.
- `random`: Random number generator for simulating message delays and losses.

#### Methods
- `initiateElection()`: Initiates the election process.
- `receiveElection(int initiatingNodeId)`: Handles incoming election messages.
- `sendOkMessage(int initiatingNodeId)`: Sends "OK" messages with delays and loss probability.
- `receiveOk()`: Sets the `receivedOk` flag upon receiving an "OK" message.
- `announceCoordinator()`: Announces itself as the coordinator if elected.
- `run()`: Entry point for the node's execution, triggering the election process.

### Main

#### Setup
- Creates a list of nodes and initializes them with unique IDs.
- Sets up an executor service for concurrent execution of node tasks.
- Manages a shared flag indicating if a coordinator has been elected.

#### Node Management
- Sets one node as inactive to simulate potential node failures.
- Executes each active node in its own thread using the executor service.

#### Election Process
- Uses a completion service to manage the completion of tasks.
- Identifies the elected coordinator and displays the result.

### Node

Behaves like the **RealisticNode** class but without the delays or message loss, reflecting an ideal scenario.

## Tested Scenarios

### Difference Between Normal Node and Realistic Node

1. **Communication Model**
   - **Normal Node**: Synchronous communication without message delays or losses.
   - **Realistic Node**: Asynchronous communication with simulated delays and message losses.
   
2. **Message Handling**
   - **Normal Node**: Immediate message delivery.
   - **Realistic Node**: Simulates network latency and message losses.
   
3. **Simulation Accuracy**
   - **Normal Node**: Suitable for testing scenarios with reliable communication.
   - **Realistic Node**: Provides a realistic simulation of real-world conditions.
   
4. **Impact on Election Process**
   - **Normal Node**: Smooth election process.
   - **Realistic Node**: May experience delays but ultimately resilient.
   
5. **Usage Scenario**
   - **Normal Node**: Suitable for initial testing.
   - **Realistic Node**: Recommended for testing under realistic network conditions.

## Results and Observations

### Normal Node Testing

- **Election Process**: Smooth without delays or disruptions.
- **Performance**: Short total runtime.
- **Reliability**: Reliable communication.
- **Scalability**: Successfully completed with the desired number of nodes.

### Realistic Node Testing

- **Election Process**: Faced challenges due to delays and message losses, but ultimately elected a coordinator.
- **Performance**: Longer total runtime.
- **Robustness**: Exhibited robustness in handling communication challenges.
- **Fault Tolerance**: Successfully completed the election process despite disruptions.

### Comparison

- **Communication Model**:
  - **Normal Node**: Synchronous, no delays or losses.
  - **Realistic Node**: Asynchronous, with delays and potential losses.
  
- **Performance Impact**:
  - **Normal Node**: Efficient and fast.
  - **Realistic Node**: Longer runtime due to delays and losses.
  
- **Realism**:
  - **Normal Node**: Suitable for basic testing.
  - **Realistic Node**: Accurate simulation of real-world conditions.

## Conclusion

- **Normal Node**: Suitable for basic testing and validation.
- **Realistic Node**: Recommended for comprehensive testing under realistic network conditions.
