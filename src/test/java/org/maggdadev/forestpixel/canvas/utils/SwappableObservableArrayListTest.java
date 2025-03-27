package org.maggdadev.forestpixel.canvas.utils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SwappableObservableArrayListTest {

    @org.junit.jupiter.api.Test
    void applyPermutations() {
        SwappableObservableArrayList<Integer> list = new SwappableObservableArrayList<>();
        List<Integer> original = List.of(0, 1, 2, 3, 4, 5);
        list.addAll(original);
        int[] permutation = {5, 3, 4, 0, 2, 1};
        list.applyPermutations(permutation);
        for (int i = 0; i < list.size(); i++) {
            assertEquals(list.get(permutation[i]), original.get(i));
        }
    }
}