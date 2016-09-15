package com.akuacom.ejb.utils.aspects;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;

import org.apache.log4j.Logger;

import com.akuacom.utils.aspects.Common;
import com.akuacom.utils.lang.Util;
import com.akuacom.ejb.BaseEntity;
import com.akuacom.ejb.TreeNode;
import com.akuacom.ejb.NodeException;
import com.akuacom.ejb.util.TreeNodeUtil;

public privileged aspect NodeAncestry extends Common {

    static Logger log = Logger.getLogger(NodeAncestry.class);

    // TreeNode itds
    @Column(columnDefinition = "varchar("
            + (BaseEntity.UUID_SIZE * TreeNode.MAX_GENERATIONS) + ")")
    public String TreeNode.ancestry;

    public String TreeNode.getAncestry() {
        return ancestry;
    }

    public void TreeNode.setAncestry(String ancestry) {
        this.ancestry = ancestry;
    }

    static Method getAncestry;

    static {
        try {
            getAncestry = TreeNode.class.getDeclaredMethod("getAncestry",
                    Util.VOID_SIG);
            getAncestry.setAccessible(true);
        } catch (Exception e) {
            log.error("getAncestry", e);
        }
    }

    pointcut setParent() :
        akuacomScope()
        && execution( * TreeNode+.setParent(TreeNode+));

    declare warning : setParent() : "\n\n\nFYI calling TreeNode+.setParent()";

    pointcut setParentNodeCtx(TreeNode child, TreeNode parent) :
        setParent()
        && this(child)
        && args(parent);

    private String noNulls(String s) {
        return s == null ? "" : s;
    }

    private String getAncestry(TreeNode node) {
        try {
            return (String) getAncestry.invoke(node, Util.VOID_ARG);
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    void around(TreeNode child, TreeNode parent) : setParentNodeCtx( child, parent) {
        log.info("around setParent with " + child + ", parent " + parent);
        if (parent == null ) {
            if(child.getParent() != null) {
                child.getParent().getChildren().remove(child);
            }
            child.setAncestry(null);
            proceed(child, parent);
            return;
        }
        String parentAncestry = getAncestry(parent);
        if (parentAncestry == null
                || parentAncestry.length() < BaseEntity.UUID_SIZE
                        * TreeNode.MAX_GENERATIONS) {
            if (parentAncestry != null
                    && parentAncestry.indexOf(child.getUUID()) != -1) {
                throw new NodeException(child + " is already an ancestor of "
                        + parent);
            }
            child.setAncestry(noNulls(parentAncestry) + parent.getUUID());
            proceed(child, parent);
            parent.getChildren().add(child);
        } else {
            throw new NodeException(
                    "TreeNode.MAX_LEVELS ("
                            + TreeNode.MAX_GENERATIONS
                            + ") exceeded!  Only trees of this depth or less are supported");
        }

    }

    pointcut getChildren() :
        akuacomScope()
        && call(* TreeNode+.getChildren())
        && !within(NodeAncestry) && !within(TreeNode);

    declare warning : getChildren() : "\n\n\nBeware calling TreeNode+.getChildren() -- please do not add children to this set directly; use addChild or addChildren\n\n\n";


    /*
    pointcut getChildrenCtx(TreeNode mbr) :
        getChildren()
        && target(mbr);
    
    Set<Set<TreeNode>> chillins = new HashSet<Set<TreeNode>>();
    
    after (TreeNode mbr) returning :  getChildrenCtx(mbr) {
        chillins.add((Set<TreeNode>)mbr.children);
    }
    
    pointcut setAdd(Set parent, TreeNode<?> child) :
        akuacomScope()
        && call(* java.util.Set.add(..))
        && target(parent)
        && args(child);
    
    before (Set parent, TreeNode<?> child) : setAdd(parent, child) {
        throw new RuntimeException(); 
    }
    */
    
    pointcut addChild() :
        akuacomScope()
        && execution( public boolean TreeNode.addChild(TreeNode));

    declare warning : addChild() : "addChild";

    pointcut addChildCtx(TreeNode child, TreeNode parent) :
        addChild() 
        && this(parent)
        && args(child);

    static Field parentF;
    static {
        try {
            parentF = TreeNode.class.getDeclaredField("parent");
            parentF.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
        }
    }

    boolean around(TreeNode child, TreeNode parent) : addChildCtx( child, parent) {
        log.info("around addChild with " + child + ", parent " + parent);
        if (parent.equals(child)) {
            throw new NodeException("no cycles; !addChild(this)");
        }
        if (parent.getAncestry() == null
                || parent.getAncestry().length() < BaseEntity.UUID_SIZE
                        * TreeNode.MAX_GENERATIONS) {
            if (parent.getAncestry() != null
                    && parent.getAncestry().indexOf(child.getUUID()) != -1) {
                throw new NodeException(child + " is already an ancestor of "
                        + parent);
            }
            child.setAncestry(noNulls(parent.getAncestry()) + parent.getUUID());
            boolean ret = proceed(child, parent);
            try {
                parentF.set(child, parent);
            } catch (Exception e) {
                log.error(e);
            }
            return ret;
        } else {
            throw new NodeException(
                    "TreeNode.MAX_LEVELS ("
                            + TreeNode.MAX_GENERATIONS
                            + ") exceeded!  Only trees of this depth or less are supported");
        }
    }

    pointcut setAncestry() :
        akuacomScope()
        && call( * TreeNode+.setAncestry(..))
        && !within(NodeAncestry);

    declare error : setAncestry() : "you shouldn't mutate this field; only NodeAncestry and Hibernate may do so";

    pointcut removeChild() :
        akuacomScope()
        && execution( public boolean TreeNode.removeChild(TreeNode));

    declare warning : removeChild() : "removeChild";

    pointcut removeChildCtx(TreeNode child, TreeNode parent) :
    	removeChild() 
        && this(parent)
        && args(child);

    boolean around(TreeNode child, TreeNode parent) : removeChildCtx( child, parent) {
        log.info("around removeChild with " + child + ", parent " + parent);
        if (!parent.getChildren().contains(child)) {
            throw new NodeException(child + " is not a child of " + parent);
        }
        try {
            parentF.set(child, null);
        } catch (Exception e) {
            log.error(e);
        }
        child.setAncestry(null);
        return proceed(child, parent);
    }

    // TreeNodeUtil methods
    pointcut descendantOfQ(TreeNode src, TreeNode trg) :
        akuacomScope()
        && execution(public boolean TreeNodeUtil.descendantOfQ(TreeNode,TreeNode ))
        && args(src,trg);

    boolean around(TreeNode src, TreeNode trg) : descendantOfQ(src,trg) {
        return src.getAncestry() == null ? false : src.getAncestry()
                .indexOf(trg
                .getUUID()) != -1;
    }

    pointcut ancestorOfQ(TreeNode src, TreeNode trg) :
        akuacomScope()
        && execution(public boolean TreeNodeUtil.ancestorOfQ(TreeNode,TreeNode ))
        && args(src,trg);

    boolean around(TreeNode src, TreeNode trg) : ancestorOfQ(src,trg) {
        return trg.getAncestry() == null ? false : trg.getAncestry().indexOf(
                src.UUID) != -1;
    }

    int around(TreeNode trg) : 
        akuacomScope()
        && execution(public int TreeNodeUtil.generation(TreeNode))
        && args(trg) {
        return trg.getAncestry() == null ? 0 : trg.getAncestry().length()
                / BaseEntity.UUID_SIZE;
    }

    List<String> around(TreeNode trg) : 
        akuacomScope()
        && execution(public List<String> TreeNodeUtil.ancestorIds(TreeNode))
        && args(trg) {
        List<String> ids = new ArrayList<String>();
        int generation = trg.getAncestry() == null ? 0 : trg.getAncestry().length()
                / BaseEntity.UUID_SIZE;
        for (int i = 0; i < generation; i++) {
            ids.add(trg.getAncestry().substring(i * BaseEntity.UUID_SIZE, (i + 1)
                    * BaseEntity.UUID_SIZE));
        }
        return ids;
    }

    Set<String> around(TreeNode[] tnArray) : 
        akuacomScope()
        && execution(public Set<String> TreeNodeUtil.commonAncestors(TreeNode...))
        && args(tnArray) {
        Set<String> ids = null;
        for (TreeNode node : tnArray) {
            if (ids == null) {
                ids = new HashSet<String>(TreeNodeUtil.ancestorIds(node));
            } else {
                ids.retainAll(TreeNodeUtil.ancestorIds(node));
            }
        }
        return ids;
    }

    String around(TreeNode[] tnArray) : 
        akuacomScope()
        && execution(public String TreeNodeUtil.toAncestry(TreeNode...))
        && args(tnArray) {
        StringBuilder sb = new StringBuilder();
        for (TreeNode n : tnArray) {
            sb.append(n.getUUID());
        }
        return sb.toString();
    }

}