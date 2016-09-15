package com.akuacom.pss2.drw;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class StringKeyAndEventsListValueHolderListWrapper {
    private ArrayList<StringKeyAndEventsListValueHolder> map =
        new ArrayList<StringKeyAndEventsListValueHolder>();

    public StringKeyAndEventsListValueHolderListWrapper() {
    }

    public ArrayList<StringKeyAndEventsListValueHolder> getStringKeyAndEventsListValueHolders() {
        return map;
    }

    public void setStringKeyAndEventsListValueHolders(
            ArrayList<StringKeyAndEventsListValueHolder> dateKeyAndDreamsListValueHolders) {
        this.map = dateKeyAndDreamsListValueHolders;
    }
}