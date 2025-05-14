Assignment Overview
The purpose of the assignment is to enable the client to communicate with the server using Remote procedure Calls(RPC).
I used Java RMI for RPC communication. The server can handle multiple requests of the client doing concurrent PUT, GET,
and DELETE operations by implementing ThreadPool and ConcurrentHashMap.

Executive Summary
-Technical Impression
The server implements the remote interface and provides the actual behavior of the remote methods.
The server then registers these implementations with the RMI registry.
The client looks up the remote methods in the RMI registry and invokes them as if they were local methods.

Error occurred when the get method was called and the key was deleted by the prior delete operation. I handled the error
by using IF to check whether the key existed; putting the get method implementation in the try catch block to catch
RemoteException, and using a second try catch block to catch the InterruptedException or ExecutionException from
retrieving the result of the Callable task.

-How to Build Server and Client Codes
ConcurrentHashMap in server: it locks only the portion of the map being modified, rather than the entire map.
It is a safe implementation of the Map interface, multiple threads can access it simultaneously without any synchronization issues

ExecutorService in server: it provides a pool of threads, When a task is submitted to the thread pool,
a thread from the pool is selected to execute the task. Once the task is completed,
the thread can be returned to the pool and reused for other tasks.

In the client class, I create 3 separate threads for PUT, GET, and DELETE operations, and run concurrently,
performing their respective operations (5 put, 5 get, 5 delete) on the store

How to Run Server and Client
1. Compile the Java files. Navigate to the directory containing  .java files and compile them using:
PS C:\Users\HP\Desktop\project2\src> javac *.java
2.Start the RMI Registry using:
PS C:\Users\HP\Desktop\project2\src> start rmiregistry
3.Run the Server using:
PS C:\Users\HP\Desktop\project2\src> java RmiServer
4. Run the Client using:
PS C:\Users\HP\Desktop\project2\src> java RmiClient1

Sample Results
Server:PS C:\Users\HP\Desktop\project2\src> java RmiServer
       2024-06-19 12:47:45.395: Server ready
       2024-06-19 12:47:54.955: GET request for key: key1
       2024-06-19 12:47:54.956: DELETE request for key: key1
       2024-06-19 12:47:54.957: DELETE request for key: key2
       2024-06-19 12:47:54.957: DELETE request for key: key3
       2024-06-19 12:47:54.958: PUT request for key: key1, value: value1
       2024-06-19 12:47:54.958: PUT request for key: key2, value: value2
       2024-06-19 12:47:54.958: DELETE request for key: key4
       2024-06-19 12:47:54.959: PUT request for key: key3, value: value3
       2024-06-19 12:47:54.959: DELETE request for key: key5
       2024-06-19 12:47:54.959: PUT request for key: key4, value: value4
       2024-06-19 12:47:54.960: PUT request for key: key5, value: value5
       2024-06-19 12:47:54.962: GET request for key: key2
       2024-06-19 12:47:54.964: GET request for key: key3
       2024-06-19 12:47:54.965: GET request for key: key4
       2024-06-19 12:47:54.966: GET request for key: key5

Client:PS C:\Users\HP\Desktop\project2\src> java RmiClient1
       Got value: value1 for key: 1
       Got value: value2 for key: 2
       Got value: value2 for key: 2
       Got value: value3 for key: 3
       Got value: value4 for key: 4
       Got value: value5 for key: 5






