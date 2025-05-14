import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * The RmiServer class implements the KeyValueInt interface and provides
 * the server-side implementation for the remote methods. It extends the
 * UnicastRemoteObject class from the java.rmi package
 */

public class RmiServer extends UnicastRemoteObject implements KeyValueInt {

    //private: it can only be accessed within the same class
    //final: store is a ConcurrentHashMap and executor is an ExecutorService,
    //once they are initialized, they cannot be altered
    private final Map<String, String> store;
    //ExecutorService automatically provides a pool of threads and an API for assigning tasks to it
    private final ExecutorService executor;

    /**
     * Constructs a new RmiServer object. Initializes the key-value store
     * and the executor service
     *
     * @throws RemoteException If a remote communication error occurs
     */
    public RmiServer() throws RemoteException{
        // calls the constructor of the superclass UnicastRemoteObject
        super();
        //ConcurrentHashMap is a thread-safe implementation of the Map interface
        this.store = new ConcurrentHashMap<>();
        //creates a thread pool with 10 threads, which is a collection of threads that can be used to execute tasks concurrently
        this.executor = Executors.newFixedThreadPool(3);

        // Initialize the HashMap with 5 keys and values
        for (int i = 1; i <= 5; i++){
            store.put("key" + i, "value" + i);
        }
    }

    @Override
    public String get(String key) throws RemoteException {
        //submits a Callable task to the ExecutorService (the executor),which returns a Future object
        //Callable: Callable is an interface that represents a task that returns a result and may throw an exception
        //Future: is an interface that stores the result of an asynchronous computation
        Future<String> future = executor.submit(() -> {
            log("GET request for key: " + key);
            try {//This block is inside the Callable task that is submitted to the ExecutorService. It handles RemoteException that occurs when the key does not exist in the store
                if (!store.containsKey(key)) {
                    log("ERROR: Invalid key");
                    throw new RemoteException("ERROR: Invalid key");
                }
                return store.get(key);
            } catch (RemoteException e) {
                log("ERROR: Exception while processing GET request");
                //e.printStackTrace():prints the exception error message, followed by the stack trace, which includes the line number and method where the exception occurred
                e.printStackTrace();
                return null;
            }
        });

        try {//This block is outside the Callable task and handles exceptions that occurs when retrieve the result of the Callable task. it handles InterruptedException and ExecutionException

            //retrieve the result of the Callable task.
            // If the task is not yet complete, future.get() will block and wait until the task completes
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RemoteException("ERROR: Exception while processing GET request", e);
        }
    }

    @Override
    public void put (String key, String value) throws RemoteException{
        executor.submit(() -> {
            log("PUT request for key: " + key + ", value: " + value);
            store.put(key, value);
        });
    }

    @Override
    public void delete(String key) throws RemoteException {
        executor.submit(() -> {
            log("DELETE request for key: " + key);
            try {
                if (!store.containsKey(key)) {
                    log("ERROR: Invalid key");
                    throw new RemoteException("ERROR: Invalid key");
                }
                store.remove(key);
            } catch (RemoteException e) {
                log("ERROR: " + e.getMessage());
            }
        });
    }

    @Override
    public Map<String, String> getAll() throws RemoteException{
        Future<Map<String, String>> future = executor.submit(() -> {
            log("GETALL request");
            return new HashMap<>(store);
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RemoteException("ERROR: Exception while processing GETALL request", e);
        }
    }


    /**
     * The main method for the RmiServer class. Creates a new RmiServer object
     * and registers it with the Java RMI registry
     *
     * @param args The command-line arguments
     */
    public static void main(String[] args) {
        try{
            // Create and export the remote object
            RmiServer server = new RmiServer();
            // Register the remote object server with a Java RMI registry so that it can be accessed remotely
            // This makes the remote object available to clients, who can look up the object by this name. The URL "//localhost/KeyValue" is the location at which the remote object can be accessed
            Naming.rebind("//localhost/KeyValue",server);

            log("Server ready");
        } catch (Exception e){
            log("Server exception: " + e);
            e.printStackTrace();
        }
    }

    /**
     * Logs a message to the console with a timestamp
     *
     * @param message The message to log
     */
    private static void log(String message){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        String timestamp = LocalDateTime.now().format(formatter);
        System.out.println(timestamp + ": " + message);
    }
}
