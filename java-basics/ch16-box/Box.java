/**
 * A box that holds one value of type T.
 *
 * @param <T> the type of the value
 */
public class Box<T> {

    private T content;

    public void put(T content) {
        this.content = content;
    }

    public T get() {
        return content;
    }

    public boolean isEmpty() {
        return content == null;
    }
}
