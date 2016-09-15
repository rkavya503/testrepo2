package com.akuacom.utils.lang;

public class MathUtil {

    public static double linterp(double a0,double a1,double b0,double b1,double b) {
        return (a1-a0)/(b1-b0)*(b-b0)+a0;
    }

}
