import java.rmi.Naming;
import java.rmi.RemoteException;

/**
 * The RmiClient1 class provides a client-side application for interacting
 * with a remote key-value store. It uses Java RMI to communicate with the
 * server
 */

public class RmiClient1 {
    /**
     * The main method for the RmiClient1 class. Looks up the remote object,
     * creates separate threads for PUT, GET, and DELETE operations, and
     * starts the threads.
     *
     * @param args The command-line arguments.
     */
    public static void main(String[] args){
        try{
            // Look up the remote object and casting it to KeyValueInt interface
            // so that client can call get, put, delete, and getAll methods on it
            KeyValueInt store = (KeyValueInt) Naming.lookup("//localhost/KeyValue");

            // Create separate threads for PUT, GET, and DELETE operations,
            // and will run concurrently, performing their respective operations (put, get, delete) on the store
            Thread putThread = new Thread(() -> {
                for (int i = 1; i <= 5; i++) {
                    try {
                        store.put("key" + i, "value" + i);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            });

            Thread getThread = new Thread(() -> {
                for (int i = 1; i <= 5; i++) {
                    try {
                        String value = store.get("key" + i);
                        if (value != null) {
                            System.out.println("Got value: " + value + " for key: " + i);
                        } else {
                            System.out.println("Key: " + i + " does not exist");
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            });

            Thread deleteThread = new Thread(() -> {
                for (int i = 1; i <= 5; i++) {
                    try {
                        store.delete("key" + i);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            });

            // Start the threads
            putThread.start();
            getThread.start();
            deleteThread.start();

            // join(): allows one thread to wait until another thread completes its execution
            putThread.join();
            getThread.join();
            deleteThread.join();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
