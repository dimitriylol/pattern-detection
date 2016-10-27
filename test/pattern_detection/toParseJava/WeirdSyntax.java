package com.patterns;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class WeirdSyntax {
    final static public Integer arr[] = {1, 2, 3};
    public ArrayList<Integer> arrayList = new ArrayList<Integer>() {{
        add(1);
        add(2);
        add(3);
        addAll(Arrays.asList(arr));
    }};


    public int someFunction() [] {
        return new int[] {4,5,6};
    }
}
