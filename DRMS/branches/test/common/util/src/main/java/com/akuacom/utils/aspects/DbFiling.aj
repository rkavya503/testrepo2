package com.akuacom.utils.aspects;

import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.lang.reflect.AnnotatedElement;

import com.akuacom.annotations.LoadDbBefore;
import com.akuacom.annotations.LoadDbAfter;
import com.akuacom.annotations.SaveDbBefore;
import com.akuacom.annotations.SaveDbAfter;
import com.akuacom.utils.lang.ClassPathUtil;
import com.akuacom.utils.Exec;
import com.akuacom.utils.lang.Dbg;

/**
 * Annotation-driven file <-> db io
 *
 */
public aspect DbFiling extends Common {
	pointcut loadDbBeforeTag() :
		akuacomScope()
		&& execution( @LoadDbBefore * *.*(..) );

	pointcut loadDbAfterTag() :
		akuacomScope()
		&& execution( @LoadDbAfter * *.*(..) );

    pointcut saveDbBeforeTag() :
        akuacomScope()
        && execution( @SaveDbBefore * *.*(..) );

    pointcut saveDbAfterTag() :
        akuacomScope()
        && execution( @SaveDbAfter * *.*(..) );

	declare warning : loadDbBeforeTag() : "found a @LoadDbBefore tag";
	declare warning : loadDbAfterTag() : "found a @LoadDbAfter tag";

    declare warning : saveDbBeforeTag() : "found a @SaveDbBefore tag";
    declare warning : saveDbAfterTag() : "found a @SaveDbAfter tag";
    

    
    private void loadDb(String targetDb, String[] files) throws Exception {
        Dbg.info("targetDb " + targetDb);
        Dbg.info("files " + Dbg.oSA(files));
        for(String file : files) {
            if(file.startsWith(ClassPathUtil.LOOK_IN_CLASSPATH_PREFIX)){
                file = ClassPathUtil.resolve(file.substring(1));
            }
            Exec exec = new Exec();
            exec.setDebug(true);
            String cmd[] = {exec.getBash().getAbsolutePath(), "-c", "mysql -u root " + targetDb    + " < '" + file + "'"};
            int status = exec.command( cmd );
            if (status == 0) {
                System.out.println(exec.getOutputGobbler().output());
            } else {
                System.out.println(exec.getErrorGobbler().output());
            }
        }
       
    }
    
	before() : loadDbBeforeTag() {
		Signature sig = thisJoinPointStaticPart.getSignature();
		AnnotatedElement declaringTypeAnnotationInfo = sig.getDeclaringType();
		if (sig instanceof MethodSignature) {
			Method method = ((MethodSignature) sig).getMethod();
			try {
				LoadDbBefore anno = method.getAnnotation(LoadDbBefore.class);
				if(anno != null) {
					String targetDb = anno.db();
					String[] files = anno.files();
					loadDb(targetDb, files);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	after() : loadDbAfterTag() {
		Signature sig = thisJoinPointStaticPart.getSignature();
		AnnotatedElement declaringTypeAnnotationInfo = sig.getDeclaringType();
		if (sig instanceof MethodSignature) {
			Method method = ((MethodSignature) sig).getMethod();
			try {
				LoadDbAfter anno = method.getAnnotation(LoadDbAfter.class);
				if(anno != null) {
					String targetDb = anno.db();
					String[] files = anno.files();
                    loadDb(targetDb, files);
				} else {
				    Dbg.info("anno is null!");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
    private void saveDb(String targetDb, String[] tables, String filePath) throws Exception {
        Dbg.info("targetDb " + targetDb);
        Dbg.info("tables " + Dbg.oSA(tables));
        Dbg.info("path " + filePath);
        StringBuffer tableExpr = new StringBuffer();
        if(tables.length > 0) {
            for(String table : tables) {
                tableExpr.append(table);
                tableExpr.append(" ");
            }
            tableExpr.setLength(tableExpr.length() -1);
        }
        
        Exec exec = new Exec();
        exec.setDebug(true);
        String cmd[] = {exec.getBash().getAbsolutePath(), "-c", "mysqldump -u root " + targetDb + " " + tableExpr  + " > " + filePath };
        int status = exec.command( cmd );
        if (status == 0) {
            System.out.println(exec.getOutputGobbler().output());
        } else {
            System.out.println(exec.getErrorGobbler().output());
        }
    }
	
   before() : saveDbBeforeTag() {
        Signature sig = thisJoinPointStaticPart.getSignature();
        AnnotatedElement declaringTypeAnnotationInfo = sig.getDeclaringType();
        if (sig instanceof MethodSignature) {
            Method method = ((MethodSignature) sig).getMethod();
            try {
                SaveDbBefore anno = method.getAnnotation(SaveDbBefore.class);
                if(anno != null) {
                    String targetDb = anno.db();
                    String[] tables = anno.tables();
                    String filePath = anno.filePath();
                    saveDb(targetDb, tables, filePath);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

   after() : saveDbAfterTag() {
       Signature sig = thisJoinPointStaticPart.getSignature();
       AnnotatedElement declaringTypeAnnotationInfo = sig.getDeclaringType();
       if (sig instanceof MethodSignature) {
           Method method = ((MethodSignature) sig).getMethod();
           try {
               SaveDbAfter anno = method.getAnnotation(SaveDbAfter.class);
               if(anno != null) {
                   String targetDb = anno.db();
                   String[] tables = anno.tables();
                   String filePath = anno.filePath();
                   saveDb(targetDb, tables, filePath);
               }
           } catch (Exception e) {
               e.printStackTrace();
           }
       }
   }
   
}