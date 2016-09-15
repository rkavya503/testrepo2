package com.akuacom.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ColUtil {

    public static <T> List<T> getList(Set<T> set) {
        return new ArrayList<T>(set);
    }

    public static <T> Set<T> getSet(List<T> list) {
        return new HashSet<T>(list);    
    }
}
