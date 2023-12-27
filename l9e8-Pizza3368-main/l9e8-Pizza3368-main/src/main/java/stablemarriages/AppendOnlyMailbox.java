package stablemarriages;

public interface AppendOnlyMailbox<T> {
    boolean post(T val);
}
