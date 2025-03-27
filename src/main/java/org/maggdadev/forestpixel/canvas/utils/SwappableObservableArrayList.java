package org.maggdadev.forestpixel.canvas.utils;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class SwappableObservableArrayList<T> {
    private final ObservableList<T> list, unmodifiableList;

    private final List<Consumer<T>> onElementAddedList = new ArrayList<>(),
            onElementRemovedList = new ArrayList<>();
    private final List<Consumer<Swap>> onSwapList = new ArrayList<>();

    public SwappableObservableArrayList() {
        list = FXCollections.observableArrayList();
        unmodifiableList = FXCollections.unmodifiableObservableList(list);
    }

    public void applyPermutations(ListChangeListener.Change<? extends T> change) {
        int[] permutation = new int[size()];
        for (int i = 0; i < size(); i++) {
            permutation[i] = change.getPermutation(i);
        }
        applyPermutations(permutation);
    }

    /**
     * Applies the given permutation to the list. The permutation should be an array of integers where the value at index
     * oldPosition is the new position of the element at that index.
     *
     * @param permutation
     */
    public void applyPermutations(int[] permutation) {
        ArrayList<Swap> swaps = new ArrayList<>();
        boolean[] visited = new boolean[size()];
        for (int start = 0; start < size(); start++) {
            if (visited[start] || permutation[start] == start) {
                continue;
            }
            ArrayList<Integer> cycle = new ArrayList<>();
            int i = start;
            while (!visited[i]) {
                cycle.add(i);
                visited[i] = true;
                i = permutation[i];
            }

            for (int j = cycle.size() - 1; j > 0; j--) {
                swaps.add(new Swap(cycle.get(j), cycle.get(j - 1)));
            }
        }
        swaps.forEach(s -> swap(s.index1, s.index2));
    }

    public void removeIf(Predicate<T> predicate) {
        for (T t : (T[]) list.toArray()) {
            try {
                if (predicate.test(t)) {
                    remove(t);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static record Swap(int index1, int index2) {
    }

    ;


    public void swap(int index1, int index2) {
        onSwapList.forEach(c -> c.accept(new Swap(index1, index2)));
        Collections.swap(list, index1, index2);

    }

    public ObservableList<T> getUnmodifiable() {
        return unmodifiableList;
    }

    public void remove(T toRemove) {
        list.remove(toRemove);
        onElementRemovedList.forEach(c -> c.accept(toRemove));
    }

    public void clear() {
        for (T t : (T[]) list.toArray()) {
            remove(t);
        }
    }

    public void addAll(Iterable<? extends T> toAdd) {
        toAdd.forEach(this::add);
    }

    public void setAll(Iterable<? extends T> toSet) {
        clear();
        addAll(toSet);
    }

    public void add(T toAdd) {
        list.add(toAdd);
        onElementAddedList.forEach(c -> c.accept(toAdd));
    }

    public void addOnElementAdded(Consumer<T> consumer) {
        onElementAddedList.add(consumer);
    }

    public void addOnElementRemoved(Consumer<T> consumer) {
        onElementRemovedList.add(consumer);
    }

    /**
     * Adds a consumer that will be called when a swap occurs. The pair should contain the indices of the elements that were swapped.
     *
     * @param consumer
     */
    public void addOnSwap(Consumer<Swap> consumer) {
        onSwapList.add(consumer);
    }

    public void removeOnElementAdded(Consumer<T> consumer) {
        onElementAddedList.remove(consumer);
    }

    public void removeOnElementRemoved(Consumer<T> consumer) {
        onElementRemovedList.remove(consumer);
    }

    public void removeOnSwap(Consumer<Swap> consumer) {
        onSwapList.remove(consumer);
    }

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public T get(int index) {
        return list.get(index);
    }

    public T getFirst() {
        return list.getFirst();
    }

    public int indexOf(T t) {
        return list.indexOf(t);
    }

    @Override
    public String toString() {
        return list.toString();
    }
}
