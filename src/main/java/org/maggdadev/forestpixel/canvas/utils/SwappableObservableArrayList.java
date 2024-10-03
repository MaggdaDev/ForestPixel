package org.maggdadev.forestpixel.canvas.utils;

import com.sun.javafx.collections.ObservableListWrapper;
import javafx.collections.ListChangeListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SwappableObservableArrayList<T> extends ObservableListWrapper<T> {
    private final List<T> source;

    public SwappableObservableArrayList() {
        this(new ArrayList<>());
    }

    private SwappableObservableArrayList(List<T> source) {
        super(source);
        this.source = source;
    }

    public void swap(int index1, int index2) {
        beginChange();
        Collections.swap(source, index1, index2);
        int[] perm = new int[size()];
        for (int i = 0; i < size(); i++) {
            if (i == index1) {
                perm[i] = index2;
            } else if (i == index2) {
                perm[i] = index1;
            } else {
                perm[i] = i;
            }
        }
        nextPermutation(0, size(), perm);
        endChange();
    }

    public void applyPermutations(ListChangeListener.Change<? extends T> change) {
        beginChange();
        applyPermutationsToList(source, change);
        nextPermutation(0, 0, new int[0]);
        endChange();
    }

    public static <T> void applyPermutationsToList(List<T> list, ListChangeListener.Change change) {
        List<T> temp = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            temp.add(list.get(change.getPermutation(i)));
        }
        list.clear();
        list.addAll(temp);
    }
}
