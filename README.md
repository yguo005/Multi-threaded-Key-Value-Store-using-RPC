# CS 6650 Project #2: Multi-threaded Key-Value Store using RPC

## Project Overview

This project extends Project #1 by introducing two significant enhancements to the Key-Value store:
1.  **Remote Procedure Calls (RPC):** Client-server communication will be transitioned from raw sockets (TCP/UDP) to an RPC framework. For Java implementations, Java RMI is a suggested option. Alternatively, other RPC frameworks like Apache Thrift can be used, which support cross-language communication via an Interface Definition Language (IDL).
2.  **Multi-threaded Server:** The server must be upgraded to handle multiple outstanding client requests concurrently. This requires implementing a threading model (e.g., thread pools) and ensuring proper mutual exclusion (synchronization) to protect shared data structures (the Key-Value store itself) during concurrent PUT, GET, and DELETE operations.

The core functionality of the Key-Value store (PUT, GET, DELETE operations) and the client's role in pre-populating the store and performing a set number of operations remain the same as in Project #1.

## Features to Implement

*   **RPC-based Communication:**
    *   Define a service interface for the Key-Value store operations (PUT, GET, DELETE).
    *   Implement the client to use RPC stubs to invoke remote methods on the server.
    *   Implement the server to expose the Key-Value store operations via RPC skeletons/dispatchers.
*   **Multi-threaded Server:**
    *   The server must be capable of handling requests from multiple client instances concurrently.
    *   Implement a threading strategy (e.g., one thread per client, thread pool).
    *   Ensure thread safety for the underlying data store (e.g., `HashMap`) using appropriate synchronization mechanisms (e.g., `synchronized` blocks/methods, `ConcurrentHashMap`, or other locks).
*   **Key-Value Store Operations:**
    *   `PUT(key, value)`: Stores a value associated with a key.
    *   `GET(key)`: Retrieves the value associated with a key.
    *   `DELETE(key)`: Removes a key and its associated value.
*   **Client Operations:**
    *   Pre-populate the Key-Value store with an initial set of data.
    *   Perform at least 5 PUTs, 5 GETs, and 5 DELETEs after pre-population.

## Suggested Technologies (for Java)

*   **RPC:** Java Remote Method Invocation (RMI) or Apache Thrift.
    *   **Java RMI:** Leverages Java's built-in capabilities for creating distributed applications. Involves defining remote interfaces, implementing remote objects, and using the RMI registry.
    *   **Apache Thrift:** A language-agnostic framework that requires defining services in a Thrift IDL file, then generating client and server stub code in Java (or other languages).
*   **Multi-threading:** Java's `java.util.concurrent` package (e.g., `ExecutorService`, `ThreadPoolExecutor`) and synchronization primitives (`synchronized`, `ReentrantLock`, etc.).
*   **Data Store:** `java.util.HashMap` (requiring external synchronization) or `java.util.concurrent.ConcurrentHashMap` (provides built-in thread safety for many operations).

## Project Structure (Suggested if using Java RMI)

*   `KeyValueStoreInterface.java`: The remote interface defining PUT, GET, DELETE methods (extends `java.rmi.Remote`).
*   `KeyValueServer.java`: The server-side implementation of `KeyValueStoreInterface`. This class will also handle RMI registry setup and binding the remote object. It will manage the multi-threading logic.
*   `KeyValueClient.java`: The client application that looks up the remote object from the RMI registry and invokes its methods.
*   (Optional) `DataStore.java`: A class to encapsulate the actual key-value data and its synchronized access.

## Compilation and Running

Instructions will vary significantly based on the chosen RPC framework (Java RMI, Apache Thrift, etc.).

### General Steps (Conceptual)

1.  **Define Service Interface (IDL for Thrift, Remote interface for RMI):**
    *   Specify the methods (PUT, GET, DELETE) and their parameters/return types.
2.  **Generate Stubs/Skeletons (if applicable, e.g., for Thrift):**
    *   Use the RPC framework's compiler to generate necessary client and server-side communication code from the interface definition.
3.  **Implement Server Logic:**
    *   Implement the service interface methods.
    *   Initialize and manage the multi-threading mechanism (e.g., thread pool).
    *   Ensure thread-safe access to the key-value data.
    *   For RMI: Start the RMI registry and bind the server object.
    *   For Thrift: Start the Thrift server.
4.  **Implement Client Logic:**
    *   For RMI: Look up the remote server object from the RMI registry.
    *   For Thrift: Create a client instance connected to the server's transport.
    *   Call the remote PUT, GET, DELETE methods.
5.  **Compile all Java source files:**
    *   Include any libraries required by the RPC framework.
    *   Example (very general): `javac -cp <path_to_rpc_libs_if_any> *.java`
6.  **Run the Server:**
    *   Example (RMI, conceptual): `java KeyValueServer <port_for_rmi_registry_or_server>`
    *   The server should start and indicate it's ready to accept client connections/requests.
7.  **Run the Client(s):**
    *   Example (RMI, conceptual): `java KeyValueClient <server_host> <server_port_or_registry_port>`
    *   Multiple instances of the client should be runnable concurrently against the server.

**Refer to the specific documentation of your chosen RPC framework (Java RMI or Apache Thrift) for detailed build and run instructions.**

## Key Considerations

*   **Mutual Exclusion:** Critical sections of code that access or modify the shared Key-Value store must be protected to prevent race conditions and ensure data integrity.
*   **Error Handling:** Implement robust error handling for RPC communication issues, server-side exceptions, etc.
*   **Server Shutdown:** While the project description for Project #1 mentioned running forever, consider how your multi-threaded server might be gracefully shut down if needed (though not explicitly required to be implemented for this project beyond manual termination).

## Executive Summary

As part of the submission, an executive summary is required, containing:
1.  **Assignment Overview:** (1 paragraph, up to ~250 words) Your understanding of the project's purpose and scope, particularly the enhancements for RPC and multi-threading.
2.  **Technical Impression:** (1-2 paragraphs, ~200-500 words) Your experiences, challenges (e.g., choosing an RPC framework, implementing thread safety), and learnings during the assignment.

## Project Deliverables

The following items should be archived (e.g., in a `.zip` or `.tar.gz` file) and submitted via Blackboard:
1.  All novel Java (or other language, if permitted and using a framework like Thrift) source code files implementing the RPC client and multi-threaded server programs, plus any additional support code (e.g., IDL files for Thrift).
2.  A simple `README` file that includes:
    *   i. How to build your server and client codes (including any external libraries necessary and how to obtain/link them).
    *   ii. How to run your server and client programs (command line syntax, parameters).
    *   iii. Your executive summary (can be part of the README or a separate plain text file).
