package com.akuacom.ejb.util;

import javax.faces.model.SelectItem;

public class SelectItemUtil {
    public static <T extends Enum<T>> SelectItem[] asSelectItems(Class<T> enumClass) {
        T[] enums = enumClass.getEnumConstants();
        SelectItem[] items = new SelectItem[enums.length];
        for (int i = 0; i < items.length; i++) {
            items[i] = new SelectItem( enums[i].ordinal(), enums[i].name());
        }
        return items;
    }
    
    public static SelectItem[] asSelectItems(int start, int end) {
        int count = end-start+1;
        SelectItem[] items = new SelectItem[count];
        for (int i = 0; i < count; i++) {
            items[i] = new SelectItem( start + i, ""+ (start + i));
        }
        return items;
    }
    
    public static  <T extends Enum<T>> SelectItem toSelectItem(Enum<T> e) {
        return new SelectItem(e.ordinal(), e.name());
    }
}
