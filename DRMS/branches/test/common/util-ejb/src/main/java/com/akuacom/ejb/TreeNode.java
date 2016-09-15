package com.akuacom.ejb;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

import org.apache.log4j.Logger;

import com.akuacom.utils.lang.Dbg;

/**
 *  Note:  the ancestry field is woven in and managed exclusively by NodeAncestry; 
 *      nothing should refer to this field directly but NodeAncestry; it is OK to 
 *      refer to this field in NamedQueries
 * @param <T>
 */
@MappedSuperclass
public class TreeNode<T extends TreeNode<T>> extends VersionedEntity implements Comparable<T>{
    static Logger log = Logger.getLogger(TreeNode.class);
    private static final long serialVersionUID = 1L;
    public final static int MAX_GENERATIONS = 10;
    protected String name;
    protected String value;

    @ManyToOne
    protected T parent;

    @OneToMany(mappedBy = "parent")
    protected Set<T> children = new HashSet<T>();
    
    public TreeNode() {
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

    public boolean siblingQ(T trg) {
        return parent.equals(trg.getParent());
    }

    public String toString(int n, TreeNode<T> node) {
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
  
    
    @Override
    public int compareTo(T o) {
        if(siblingQ(o)) {
            
        }
        return 0;
    }
    
}
