package com.akuacom.ejb.util;

import static javax.persistence.FetchType.LAZY;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import org.apache.log4j.Logger;

import com.akuacom.utils.lang.Dbg;
import com.akuacom.utils.lang.FieldUtil;

public class EjbUtil {
    static Logger log = Logger.getLogger(EjbUtil.class);
    static Map<Class<?>, String> entityQuery = Collections
            .synchronizedMap(new HashMap<Class<?>, String>());

    public static boolean inDb(EntityManager em, Class entityClass, Object id) {
        String query = entityQuery.get(entityClass);
        Field idField = FieldUtil.idFieldClass(entityClass);
        idField.setAccessible(true);
        String paramExpr = null;
        if (idField.getType() == String.class) {
            paramExpr = "'" + id + "'";
        } else {
            paramExpr = "" + id;
        }
        if (query == null) {
            query = "select count(*) from " + entityClass.getSimpleName()
                    + " o where o." + idField.getName() + " = ";
            entityQuery.put(entityClass, query);
        }

//        Long count = ((Long) em.createQuery(query + paramExpr)
//                .getSingleResult());
//        log.debug("query " + query + paramExpr + " -> " + count);
//
//        return count > 0;
        return true;
    }

    public static boolean inDb(EntityManager em, Object entity) {
        Class c = entity.getClass();
        String query = entityQuery.get(c);
        Field idField = FieldUtil.idField(entity);
        idField.setAccessible(true);
        String paramExpr = null;
        try {
            return inDb(em, c, idField.get(entity));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    static Map<Class<?>, String> fetchJoinQuery = Collections
            .synchronizedMap(new HashMap<Class<?>, String>());

    static Annotation getLazyRelation(Field f) {
        Annotation anno = f.getAnnotation(OneToMany.class);
        if (anno != null) {
            OneToMany o2m = (OneToMany) anno;
            if (LAZY == o2m.fetch()) {
                return anno;
            }
        }
        anno = f.getAnnotation(ManyToMany.class);
        if (anno != null) {
            ManyToMany m2m = (ManyToMany) anno;
            if (LAZY == m2m.fetch()) {
                return anno;
            }
        }
        return null;
    }

    public static <T> T getInitialized(EntityManager em,
            Class<? extends T> entityClass, Object id)
            throws IllegalArgumentException, IllegalAccessException {
        String query = fetchJoinQuery.get(entityClass);
        Field idField = FieldUtil.idFieldClass(entityClass);
        String paramExpr = null;
        if (idField.getType() == String.class) {
            paramExpr = "'" + id + "'";
        } else {
            paramExpr = "" + id;
        }
        String where = "where o." + idField.getName() + " = ";
        if (query == null) {
            query = "select distinct(o) from " + entityClass.getSimpleName()
                    + " o ";

            int mods;
            Annotation anno;
            String jf = "";
            for (Field f : FieldUtil.getLinealFields(entityClass)) {
                mods = f.getModifiers();
                anno = getLazyRelation(f);
                if (!Modifier.isFinal(mods) && !Modifier.isStatic(mods)
                        & anno != null) {
                    jf += " left join fetch o." + f.getName();
                }
            }
            query += jf + " " ;
            log.info(entityClass + " query " + query);
            fetchJoinQuery.put(entityClass, query);
        }
        log.info(entityClass + " query + where + paramExpr " + query + where + paramExpr);
        return (T) em.createQuery(query + where + paramExpr).getSingleResult();
    }

    public static <T> List<T> getInitialized(EntityManager em,
            Class<? extends T> entityClass, Collection ids)
            throws IllegalArgumentException, IllegalAccessException {
        String query = fetchJoinQuery.get(entityClass);
        Field idField = FieldUtil.idFieldClass(entityClass);
        String paramExpr = null;
        String where = "where o." + idField.getName() + " in (:ids) ";
        if (query == null) {
            query = "select distinct(o) from " + entityClass.getSimpleName()
                    + " o ";

            int mods;
            Annotation anno;
            String jf = "";
            for (Field f : FieldUtil.getLinealFields(entityClass)) {
                mods = f.getModifiers();
                anno = getLazyRelation(f);
                if (!Modifier.isFinal(mods) && !Modifier.isStatic(mods)
                        & anno != null) {
                    jf += " left join fetch o." + f.getName();
                }
            }
            query += jf + " " ;
            log.info(entityClass + " query " + query);
            fetchJoinQuery.put(entityClass, query);
        }
        log.info(entityClass + " query + where + paramExpr " + query + where + paramExpr);
        return em.createQuery(query + where + paramExpr)
                .setParameter("ids", ids)
                .getResultList();
    }
    public static String countQuery(NamedQuery nq) {
        String ejbql = nq.query();
        return "select count(*) " + ejbql.substring(ejbql.indexOf("from"));
    }
    
    public static long countFromQuery(EntityManager em, NamedQuery nq) {
        return (Long)em.createQuery(countQuery(nq)).getSingleResult();
    }
    
    static Map<Class<?>, Set<Field>> relations = Collections.synchronizedMap(new HashMap<Class<?>, Set<Field>>());
    static Set<Field> rels(Class<?> c) {
        Set<Field> fields = relations.get(c);
        if(fields == null) {
            fields = new HashSet<Field>();
            for(Field f : FieldUtil.getLinealFields(c)) {
                if(Collection.class.isAssignableFrom(f.getType()) 
                        && (f.getAnnotation(OneToMany.class) != null || f.getAnnotation(ManyToMany.class) != null)) {
                    f.setAccessible(true);
                    fields.add(f);
                }
            }
        }
        System.out.println("fields " + Dbg.oS(fields));
        return fields;
    }
    
    public static void clearRelations(Object trg) {
        for(Field f : rels(trg.getClass())) {
            try {
                System.out.println("clearing " + f);
                ((Collection)f.get(trg)).clear();
            } catch (Exception e) {
                e.printStackTrace();
            } 
        }
    }
}
