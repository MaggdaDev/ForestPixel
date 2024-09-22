package org.maggdadev.forestpixel.canvas.utils;

import com.sun.javafx.collections.ObservableListWrapper;

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
}
