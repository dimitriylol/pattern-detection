package com.patterns;


public class Annotations {
    public @interface AnAnnotation{
    }

    public @interface ParameterAnnotation{
        String val();
    }

    @AnAnnotation
    public int val;
    @ParameterAnnotation(val = "meeeeeh")
    public double val2;
}
