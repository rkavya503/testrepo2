package com.akuacom.pss2.system;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.akuacom.ejb.EntityUtil;
import com.akuacom.ejb.jboss.test.JBossFixture;
import com.akuacom.ejb.util.TreeNodeUtil;
import com.akuacom.utils.lang.Dbg;

public class TaskTest extends JBossFixture {

    protected static TaskGenEAO taskEao;
    static Logger log = Logger.getLogger(TaskTest.class.getSimpleName());

    @BeforeClass
    public static void setUp() {
        if (taskEao == null) {
            
            try {
                taskEao = lookupSessionRemote(TaskGenEAO.class);
            }catch(Exception e) {
                log.severe(e.toString());
            }
        }
    }
    @Ignore
    @Test
    public void testNoEjb() {
        Task t1 = EntityUtil.randomlyPopulate(EntityUtil.neux(Task.class));
        // won't compile unless commented out
        // see NodeAncestry.declare error : setAncestry()
        // t1.setAncestry(null);
        
        t1.setName("t1");
        Task t1_2 = EntityUtil.randomlyPopulate(EntityUtil.neux(Task.class));
        t1_2.setName("t1_2");
        Task t1_2a = EntityUtil.randomlyPopulate(EntityUtil.neux(Task.class));
        t1_2a.setName("t1_2a");

        Task t0 = EntityUtil.randomlyPopulate(EntityUtil.neux(Task.class));
        t0.setName("t0");
        Task t00 = EntityUtil.randomlyPopulate(EntityUtil.neux(Task.class));
        t00.setName("t00");
        
//          TODO  implement this protection in NodeAncestry
//        try {
//            Set<Task> kids = t0.getChildren();
//            kids.add(t00);
//            fail("shouldn't have allowed");
//        } catch(RuntimeException expected) {
//            
//        }
         
        t0.addChild(t00);
        
        assertNotNull(t00.getAncestry());
        t0.removeChild(t00);
        assertNull(t00.getAncestry());
        
        
        boolean haveTasks = true;

        t1.addChild(t1_2);
        t1.addChild(t1_2a);
        assertTrue(t1_2.siblingQ(t1_2a));

        Task t3 = EntityUtil.randomlyPopulate(EntityUtil.neux(Task.class));
        t3.setName("t3");
        Task t4a = EntityUtil.randomlyPopulate(EntityUtil.neux(Task.class));
        t4a.setName("t4a");
        Task t4b = EntityUtil.randomlyPopulate(EntityUtil.neux(Task.class));
        t4b.setName("t4b");
        Task t5 = EntityUtil.randomlyPopulate(EntityUtil.neux(Task.class));
        t5.setName("t5");
        Task t6 = EntityUtil.randomlyPopulate(EntityUtil.neux(Task.class));
        t6.setName("t6");

        t1_2.addChild(t3);
        t3.addChild(t4a);
        t3.addChild(t4b);
        t4a.addChild(t5);

        try {
            t5.addChild(t6);
        } catch (Exception expected) {
            log.info("expected " + expected);
        }

        try {
            t1_2.addChild(t1_2);
            fail("allowed cyclic graph");
        } catch (Exception expected) {
            log.info("expected " + expected);
        }

        assertTrue(TreeNodeUtil.descendantOfQ(t5, t1));
        assertTrue(TreeNodeUtil.ancestorOfQ(t1,t5));
        log.info("t5.descendantOfQ(t5) " + TreeNodeUtil.descendantOfQ(t5, t5));
        log.info("t5.ancestorOfQ(t5) " + TreeNodeUtil.ancestorOfQ(t1,t5));

        log.info(t1.deepToString());

        log.info(t3.deepToString());
        
        // generations
        
        assertEquals(4, TreeNodeUtil.generation(t5));
        log.info("TreeNodeUtil.ancestorIds(t3) " + Dbg.oS(TreeNodeUtil.ancestorIds(t3)));
        assertEquals(2, TreeNodeUtil.ancestorIds(t3).size());
        
        log.info("t5.commonAncestors(t4) " + Dbg.oS(TreeNodeUtil.commonAncestors(t5,t4a)));
        log.info("t5.commonAncestors(t1_2, t1_2a) " + Dbg.oS(TreeNodeUtil.commonAncestors(t5,t1_2, t1_2a)));
        
        log.info("t5.getAncestry() " + t5.getAncestry());
        log.info("TreeNodeUtil.toAncestry(t5) " + TreeNodeUtil.toAncestry(t5,t1_2));
    	
    }

    @Test @Ignore
    public void testTasks() {
        Task t1 = EntityUtil.randomlyPopulate(EntityUtil.neux(Task.class));
        
        // won't compile unless commented out
        // see NodeAncestry.declare error : setAncestry()
        // t1.setAncestry(null);
        
        t1.setName("t1");
        Task t1_2 = EntityUtil.randomlyPopulate(EntityUtil.neux(Task.class));
        t1_2.setName("t1_2");
        Task t1_2a = EntityUtil.randomlyPopulate(EntityUtil.neux(Task.class));
        t1_2a.setName("t1_2a");

        Task t0 = EntityUtil.randomlyPopulate(EntityUtil.neux(Task.class));
        t0.setName("t0");
        Task t00 = EntityUtil.randomlyPopulate(EntityUtil.neux(Task.class));
        t00.setName("t00");
        t0.addChild(t00);
        
        boolean haveTasks = true;
        if (taskEao != null) {
            try {
                taskEao.createMany(t1, t1_2, t1_2a);
            }catch(IllegalArgumentException e) {
                haveTasks= false;
                log.info(e.getMessage());
            }
        }

        t1.addChild(t1_2);
        t1.addChild(t1_2a);

        if (haveTasks && taskEao != null) {
            taskEao.updateMany(t1, t1_2, t1_2a);
        }

        Task t3 = EntityUtil.randomlyPopulate(EntityUtil.neux(Task.class));
        t3.setName("t3");
        Task t4a = EntityUtil.randomlyPopulate(EntityUtil.neux(Task.class));
        t4a.setName("t4a");
        Task t4b = EntityUtil.randomlyPopulate(EntityUtil.neux(Task.class));
        t4b.setName("t4b");
        Task t5 = EntityUtil.randomlyPopulate(EntityUtil.neux(Task.class));
        t5.setName("t5");
        Task t6 = EntityUtil.randomlyPopulate(EntityUtil.neux(Task.class));
        t6.setName("t6");

        if (haveTasks && taskEao != null) {
            taskEao.createMany(t3, t4a, t4b, t5, t6);
        }
        
        t1_2.addChild(t3);
        t3.addChild(t4a);
        t3.addChild(t4b);
        t4a.addChild(t5);

        if (haveTasks && taskEao != null) {
            taskEao.updateMany(t1_2, t3, t4a, t5);
        }
        
      
        if (haveTasks && taskEao != null) {

            List<Task> desc = taskEao.findDecendants(t1.getUUID());
            assertTrue(desc.contains(t1_2a));
            assertTrue(desc.contains(t1_2));
            assertTrue(!desc.contains(t1));
            assertTrue(!desc.contains(t6));
            desc = null;

            try {
                taskEao.deleteMany(t1, t1_2, t1_2a, t3, t4a, t4b, t5, t6);
            } catch (Exception e) {
                fail("deleteMany " + e.toString());
            }
        }

    }

}
