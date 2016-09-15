
package com.akuacom.pss2.richsite;


import java.util.ArrayList;
import javax.faces.model.SelectItem;


/**
 * @author Ilya Shaikovsky
 *
 */
public class FilteringBean {

    private String filterZone = "5";
    private String filterValue="";
    private ArrayList<SelectItem> filterZones = new ArrayList<SelectItem>();



    public FilteringBean() {
        for (int i = 5; i < 11; i++) {
            SelectItem select = new SelectItem();
            select.setLabel("-" + i);
            select.setValue(i);
            filterZones.add(select);
        }
    }

    public String getFilterValue() {
        return filterValue;
    }

    public void setFilterValue(String filterValue) {
        this.filterValue = filterValue;
    }

    public String getFilterZone() {
        return filterZone;
    }

    public void setFilterZone(String filterZone) {
        this.filterZone = filterZone;
    }

    public ArrayList<SelectItem> getFilterZones() {
        return filterZones;
    }
}