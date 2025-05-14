import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

/**
 * The KeyValueInt interface defines the methods that a client can call remotely.
 * It extends the Remote interface from the java.rmi package
 */
public interface KeyValueInt extends Remote {

    /**
     * Retrieves the value associated with the given key from the key-value store
     *
     * @param key The key to look up in the key-value store
     * @return The value associated with the key
     * @throws RemoteException If a remote communication error occurs
     */
    String get(String key) throws RemoteException;

    /**
     * Inserts a key-value pair into the key-value store.
     *
     * @param key The key to be inserted into the key-value store
     * @param value The value to be associated with the key
     * @throws RemoteException If a remote communication error occurs
     */
    void put(String key, String value) throws  RemoteException;

    /**
     * Deletes a key-value pair from the key-value store
     *
     * @param key The key to be deleted from the key-value store
     * @throws RemoteException If a remote communication error occurs
     */
    void delete(String key) throws RemoteException;

    /**
     * Retrieves all key-value pairs from the key-value store
     *
     * @return A map containing all key-value pairs in the key-value store
     * @throws RemoteException If a remote communication error occurs
     */
    Map<String, String> getAll() throws RemoteException;

}
