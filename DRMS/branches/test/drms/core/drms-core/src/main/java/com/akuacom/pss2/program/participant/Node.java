package com.akuacom.pss2.program.participant;

import com.akuacom.ejb.VersionedEntity;
import com.akuacom.utils.lang.Dbg;
import org.apache.log4j.Logger;

import javax.persistence.*;
import java.util.*;

@MappedSuperclass
public class Node<T extends Node<T>> extends VersionedEntity implements Comparable<T>{
    static Logger log = Logger.getLogger(Node.class);
    private static final long serialVersionUID = 1L;
    public final static int MAX_LEVELS = 10;
    protected String name = "";
    protected String value = "";

    @Column(columnDefinition = "text(" + (UUID_SIZE * MAX_LEVELS) + ")")
    protected String ancestry;

    @ManyToOne
    protected T parent;

    @OneToMany(mappedBy = "parent")
    protected Set<T> children = new HashSet<T>();

    public Node() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public T getParent() {
        return parent;
    }

    public void setParent(T parent) {
        this.parent = parent;
    }

    public Set<T> getChildren() {
        return children;
    }

    public void setChildren(Set<T> children) {
        this.children = children;
    }

    public boolean addChild(T child) {
        return children.add(child);
    }

    public boolean removeChild(T child) {
        if( children.remove(child)) {
        	return true; 
        }
        throw new NoSuchElementException(child.toString());
    }

    public void addChildren(Collection<T> children) {
        for (T child : children) {
            addChild(child);
        }
    }

    public String getAncestry() {
        return ancestry;
    }

    public void setAncestry(String ancestry) {
        this.ancestry = ancestry;
    }

    public boolean descendantOfQ(T trg) {
        return ancestry == null ? false : ancestry.indexOf(trg.getUUID()) != -1;
    }

    public boolean ancestorOfQ(T trg) {
        return trg.getAncestry() == null ? false : trg.getAncestry().indexOf(
                getUUID()) != -1;
    }

    public boolean siblingQ(T trg) {
        return parent.equals(trg.getParent());
    }

    public String toString(int n, Node<T> node) {
        log.info(n + " called with " + node.getName() + " kids " + Dbg.oS(node.getChildren()));
        String tabStr = "";
        StringBuilder sb = new StringBuilder();
        if (n > 0) {
            char[] tabs = new char[n];
            Arrays.fill(tabs, '\t');
            tabStr = new String(tabs);
        }
        sb.append(tabStr);
        sb.append(node.getName());
        sb.append("\n");
        for (T child : node.getChildren()) {
            sb.append(toString(n + 1, child));
        }

        return sb.toString();
    }

    public String deepToString() {
        return toString(0, this);
    }
    
    @Override
    public String toString() {
        return name + " :: " +getUUID();
    }
  
    public int generation() {
    	return ancestry == null ? 0 : ancestry.length() / UUID_SIZE;
    }

    public List<String> ancestorIds() {
        List<String> ids = new ArrayList<String>();
        int generation = generation();
        for(int i = 0; i < generation; i++ ){
            ids.add( ancestry.substring(i * UUID_SIZE, (i + 1) * UUID_SIZE) );
        }
        return ids;
    }
    
    public Set<String> commonAncestors(Node<T>... nodes) {
        Set<String> ids = new HashSet<String>(ancestorIds());
        for(Node<T> node : nodes) {
            ids.retainAll(node.ancestorIds());
        }
        return ids;
    }
    
    @Override
    public int compareTo(T o) {
        if(siblingQ(o)) {
            
        }
        return 0;
    }
    
}
