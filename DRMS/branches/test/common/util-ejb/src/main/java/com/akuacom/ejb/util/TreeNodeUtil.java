package com.akuacom.ejb.util;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.akuacom.ejb.TreeNode;

/**
 * Utility class to support regular java access to the behavior woven into TreeNode by NodeAncestry
 * all of these methods are intercepted and calculations involving the 'hidden' ancestry field are
 * performed by the aspect
 *
 */
public class TreeNodeUtil {
    public static <T> boolean descendantOfQ(TreeNode<? extends T> src, TreeNode<? extends T> trg) {
        return false;
    }

    public static <T> boolean ancestorOfQ(TreeNode<? extends T> src, TreeNode<? extends T> trg) {
        return false;
    }

    public static <T> int generation(TreeNode<? extends T> trg) {
        return -1;
    }

    public static <T> List<String> ancestorIds(TreeNode<? extends T> trg) {
        return Collections.<String>emptyList();
    }
    
    public static <T> Set<String> commonAncestors(TreeNode<? extends T>... nodes) {
        return Collections.<String>emptySet();
    }    
    
    public static <T> String toAncestry(TreeNode<? extends T>... nodes) {
        return null;
    }
    
}
