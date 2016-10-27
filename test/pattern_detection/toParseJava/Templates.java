package com.patterns;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Templates {
    public abstract class CovariantTemplate<T extends InputStream> {

    }


    public <T extends Number> List<T> getList(Comparator<? super T> item) {
        ArrayList<T> ts = new ArrayList<>();
        return ts;
    }
}
