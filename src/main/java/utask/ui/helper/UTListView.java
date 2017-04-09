package utask.ui.helper;

import java.util.Collection;
import java.util.Iterator;

import com.jfoenix.controls.JFXListView;

//@@author A0139996A
/**
 *  As ListHelper works with generic of Collection type, it is required to implement Collection.
 *
 *  Although, ListView has a underlying collection, we are using the helper for UI manipulation.
 *  Thus, the need to abstract different methods to prevent the need to cast generic types to get item size.
 *
 *  E.g.    ((FilteredList) T).size()
 *         ((ListView) T).getItems().size()
 *
 * This demonstrates the use of adapter pattern, where incompatible types are ‘adapted’ so they can work together.
 **/
public class UTListView<T> extends JFXListView<T> implements Collection<T> {

    @Override
    public int size() {
        return getItems().size();
    }

    @Override
    public boolean isEmpty() {
        return getItems().isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return getItems().contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return getItems().iterator();
    }

    @Override
    public Object[] toArray() {
        return getItems().toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return getItems().toArray(a);
    }

    @Override
    public boolean add(T e) {
        return getItems().add(e);
    }

    @Override
    public boolean remove(Object o) {
        return getItems().remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return getItems().containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return getItems().addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return getItems().removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return getItems().retainAll(c);
    }

    @Override
    public void clear() {
        getItems().clear();
    }
}
