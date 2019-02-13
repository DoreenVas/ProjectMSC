package Resources;

/***
 * An interface for container
 * @param <T> the type of the container
 */
public interface Container<T> {

    /***
     * the function returns the value in the container
     * @return the value of the container
     */
    T getValue();
}