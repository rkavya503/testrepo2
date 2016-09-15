package com.akuacom.ejb.processor;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.NullType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.tools.Diagnostic;

import scala.Tuple2;
import scala.Tuple3;

import com.akuacom.ejb.BaseEntity;
import com.akuacom.ejb.VersionedEntity;
import com.akuacom.utils.lang.Dbg;
import com.akuacom.utils.lang.FieldUtil;
import com.akuacom.utils.lang.MethodUtil;
import com.akuacom.utils.lang.StringUtil;

/**
 * Annotation processor that generates parameterized EAO classes (interface,
 * implementation and, if entity declares NamedQueries, test) that extend
 * BaseEAO and BaseEAOBean. You can use them straight or extend them with your
 * own EAOs or other session beans EaoFromEntityProcessor imposes this contract
 * on the NamedQuery: if you want the query to be wrapped in a 'finder method':
 * it must be named "X.findYYY" (not case sensitive); where X is the name of the
 * entity queries that do not begin with 'find' will be ignored the processor
 * will attempt to figure out the types of the query parameters; if it can't, it
 * declares them as type 'Object' the processor ASSUMES that the result of the
 * query is always Collection<X> (parameterized by the entity type)
 * 
 */
@SupportedAnnotationTypes(value = { "javax.persistence.Entity" })
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class EaoFromEntityProcessor extends AbstractProcessor {

    enum Extends {
        BaseEntity, VersionedEntity, Neither
    }

    enum ReturnType {
        Single, Set, List
    }

    boolean isTestContext = true;

    Logger log = Logger.getLogger(EaoFromEntityProcessor.class.getSimpleName());
    Types types = null;
    Elements elements = null;
    public final static String BASE_EAO_SIMPLE = "BaseEAO";
    public final static String BASE_EAO = "com.akuacom.ejb." + BASE_EAO_SIMPLE;
    public final static String BASE_EAO_BEAN_SIMPLE = "BaseEAOBean";
    public final static String BASE_EAO_BEAN = "com.akuacom.ejb."
            + BASE_EAO_BEAN_SIMPLE;

    
    final static Pattern locateEx = Pattern.compile("locate\\((.*),(.*)\\)");
    /**
     * pattern for finding query parameters
     */
    final static Pattern argsEx = Pattern.compile("(:[a-zA-Z][a-zA-Z0-9]*)");
    /**
     * pattern for finding variable declarations parameters
     */
    final static Pattern varDecsEx = Pattern
            .compile("(from|FROM)(.*)(where|WHERE)");
    /**
     * pattern for finding word pairs
     */
    final static Pattern pairEx = Pattern.compile("(\\w+)(\\s*)(\\w+)");
    /**
     * pattern for sql functions eg UPPER(m.to)
     */
    final static Pattern funcEx = Pattern.compile("(\\w+)\\((.*)\\)");
    /**
     * pattern for query name and type
     */
    final static Pattern queryNameAndTypeEx = Pattern
            .compile("(\\w+)\\.(\\w+)\\.(\\w+)");
    /**
     * pattern for query name
     */
    final static Pattern queryNameEx = Pattern.compile("(\\w+)\\.(\\w+)");

    public EaoFromEntityProcessor() {
        String value = System
                .getProperty("com.akuacom.ejb.processor.EaoFromEntityProcessor.flags");
        isTestContext = "generate-tests".equals(value);
        log.info("\n\n\n\nrunning: generating EAO "
                + (isTestContext ? "tests" : "sources") + "\n\n\n");
    }

    public void init(ProcessingEnvironment procEnv) {
        super.init(procEnv);
        types = procEnv.getTypeUtils();
        elements = procEnv.getElementUtils();
    }

    /*
     * Main entry point; javac calls this with the list of classes that have the
     * annos specified in the @SupportedAnnotationTypes anno (non-Javadoc)
     * 
     * @see javax.annotation.processing.AbstractProcessor#process(java.util.Set,
     * javax.annotation.processing.RoundEnvironment)
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations,
            RoundEnvironment roundEnv) {
        if (annotations.isEmpty()) {
            return false;
        }

        Set<? extends Element> elements = roundEnv.getRootElements();
        Collection<? extends TypeElement> typeElements = ElementFilter
                .typesIn(elements);
        Dbg.info("typeElements " + typeElements);
        for (TypeElement type : typeElements) {
            writeEao(type);
        }

        return true;
    }

    public String eaoName(TypeElement type) {
        String path = type.asType().toString();
        String packageString = path.substring(0, path.lastIndexOf(".") + 1);
        return packageString + type.getSimpleName() + "GenEAO";
    }

    /**
     * Checks that entity class in question subclasses BaseEntity or
     * VersionedEntity
     * 
     * @param superclasses
     * @return
     */
    public Extends baseEntityInAncestry(TypeElement type,
            List<? extends TypeMirror> superclasses) {
        for (TypeMirror tm : superclasses) {
            String name = tm.toString();

            if (name.endsWith("VersionedEntity")) {
                return Extends.VersionedEntity;
            } else if (name.endsWith("BaseEntity")) {
                return Extends.BaseEntity;
            }
        }
        // for some reason, above method does not always correctly describe class hierarchy
        try {
            Class<?> c = Class.forName(type.asType().toString());
            if (VersionedEntity.class.isAssignableFrom(c)) {
                return Extends.VersionedEntity;
            } else if (BaseEntity.class.isAssignableFrom(c)) {
                return Extends.BaseEntity;
            }
        } catch (ClassNotFoundException e) {
            log.info("unable to load " + type.asType().toString());
        }
        return Extends.Neither;
    }

    /**
     * Called once per @Entity class, generates the source files of the EAO
     * class/if
     * 
     * @param type
     *            TypeElement representing the Entity
     */
    public void writeEao(TypeElement type) {
        log.info("processing " + type);
        String eaoName = eaoName(type);
        try {
            if (type.getAnnotation(Entity.class) != null) {
                Extends ex = baseEntityInAncestry(type,
                        types.directSupertypes(type.asType()));
                if (ex == Extends.BaseEntity || ex == Extends.VersionedEntity) {
                    writeEaoSourceFilesForType(type, eaoName, ex);
                }
            }
        } catch (IOException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                    "Could not create source file for " + eaoName + ": " + e);
        }
    }

    /**
     * given the index of a parameter in a query string, returns the preceding
     * two expressions (as a Tuple) to complete the predicate e.g. given a
     * phrase "select distinct a from X a where a.field = :value" would return
     * {"a.field", "="} given the index of :value
     * 
     * @param src
     * @param index
     * @return
     */
    public Tuple2<String, String> previousTwoExpressions(String src, int index) {
        int idx = index - 1;
        StringBuffer word1 = new StringBuffer(), word2 = new StringBuffer(), word = word1;
        while (idx > 0) {
            if (idx == index - 1 && src.charAt(idx) == '(') {
                // ignore the '(' as in 'xxx IN (:var)'
            } else {
                if (src.charAt(idx) != ' ' || idx == index - 1
                        || (idx == index - 2 && word.length() == 0)) {
                    word.append(src.charAt(idx));
                } else {
                    if (word == word2) {
                        break;
                    } else {
                        word = word2;
                    }
                }
            }
            idx--;
        }
        Tuple2<String, String> fieldOp = new Tuple2<String, String>(word2
                .reverse().toString().trim(), word1.reverse().toString().trim());
        return fieldOp;
    }

    /**
     * Returns a list of parameter expressions in the supplied query string
     * format is Tuple3 eg o.startTime >= :startTime -> {"o.startTime", ">=",
     * ":startTime"}
     * 
     * @param query
     * @return
     */
    public List<Tuple3<String, String, String>> args(String query) {
        List<Tuple3<String, String, String>> args = new ArrayList<Tuple3<String, String, String>>();
        Matcher m = argsEx.matcher(query);
        Set<String> argNames = new HashSet<String>();
        while (m.find()) {
            String arg = m.group().substring(1);
            if (!argNames.contains(arg)) {
                argNames.add(arg);
                Tuple2<String, String> prev2 = previousTwoExpressions(query,
                        m.start());
                Tuple3<String, String, String> fieldOpParam = new Tuple3<String, String, String>(
                        prev2._1, prev2._2, arg);
                args.add(fieldOpParam);
            }
        }
        return args;
    }

    public String upperFirstChar(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    public String removeParens(String s) {
        s = s.replace("(", "");
        s = s.replace(")", "");
        return s;
    }

    /**
     * Attempt to determine the type of a query parameter, given the predicate
     * expression in a Tuple3 {entity property path, comparison operator,
     * parameter} Looks for member named name in list members; returns type if
     * found otherwise 'Object'
     * 
     * @param members
     *            The fields of the entity being queried
     * @param fieldNameParamName
     *            the predicate as a Tuple {entity property path, comparison
     *            operator, parameter}
     * @return the type of the entity field, as a String
     */
    public String inferType(TypeElement baseType, List<? extends Element> members,
            Tuple3<String, String, String> fieldNameParamName,
            Map<String, String> declaredVars) {
        List<Element> elems = new ArrayList<Element>(members);

        String fieldName = fieldNameParamName._1();
        String paramName = StringUtil.dePackage(fieldNameParamName._3());

        Matcher m = funcEx.matcher(fieldNameParamName._1());
        if (m.find()) {
            fieldName = m.group(2);
        } else {
            fieldName = removeParens(fieldNameParamName._1());
        }

        String[] path = fieldName.split("\\."); // turn path expression a.b.c
                                                // into array {a,b,c}

        log.info("fieldName " + fieldName);
        if (path.length == 2) {
            fieldName = path[1];
        } else if (path.length > 2) {
            // have a var.sub1.sub2.sub3.sub4...subN path
            String type = declaredVars.get(path[0]);
            if (type != null && typeFields.containsKey(type)) {
                // recognized type of var, look for type of sub1
                for (Element em : typeFields.get(type)) {
                    if (em.getSimpleName().toString().equals(path[1])) {
                        int i = 2;
                        Class<?> c = null;
                        try {
                            c = Class.forName(em.asType().toString());
                            while (i < path.length) {
                                for (Field f : FieldUtil.getLinealFields(c)) {
                                    if (f.getName().equals(path[i])) {
                                        c = f.getType();
                                        log.info(fieldName + " -> " + c);
                                        if (i == path.length - 1) {
                                            return c.getSimpleName();
                                        } else {
                                            break;
                                        }
                                    }
                                }
                                i++;
                            }
                            log.info("found nothing for " + fieldNameParamName
                                    + " with reflection...");
                        } catch (ClassNotFoundException e) {
                            log.info("class not found: "
                                    + em.asType().toString());
                        }
                    }
                }
            } else {
                log.info("type is null or no fields for " + fieldNameParamName);
            }
            fieldName = path[2];
        }
        for (Element em : elems) {
            if (em.getKind().equals(ElementKind.FIELD)) {
                String eName = em.getSimpleName().toString();
                if (eName.equals(fieldName) || eName.equals(paramName)) {
                    TypeMirror erased = types.erasure(em.asType());
                    if(erased.getKind() != TypeKind.DECLARED) {
                        log.info(fieldName + " paramd--> " + erased.toString());
                        return erased.toString();
                    }
                    
                    try {
                        Class baseClass = Class.forName(baseType.toString());
                        Class erasedClass = Class.forName(erased.toString());
                        if(erasedClass.isAssignableFrom(baseClass) && erasedClass.getAnnotation(MappedSuperclass.class) != null) {
                            return baseType.toString();
                        } else {
                            return erased.toString();
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        erased.toString();
                    }
                }
            }
        }

        return "Object"; // couldn't infer type
    }

    /**
     * @param nqs
     * @return true if a NamedQuery is of the form 'findXXX' or 'FindXXX' or 'deleteXXX' or 'DeleteXXX'; this
     *         is how you mark a query 'for export' to the generated EAO
     */
    public boolean hasFinderOrDeleter(NamedQueries nqs) {
        if (nqs != null) {
            for (NamedQuery q : nqs.value()) {
                // convention is if namedq starts with 'find' it's expected
                // that it will return a collection parameterized by the entity
                // being processed
                // use 'get' for queries returning scalars/non-entities
                if (q.name().indexOf(".find") != -1
                        || q.name().indexOf(".Find") != -1
                        || q.name().indexOf(".delete") != -1
                        || q.name().indexOf(".Delete") != -1 
                ) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param ejbQl
     *            query to parse
     * @return map of declared variables as member -> class
     */
    public Map<String, String> declaredVariables(String ejbQl) {
        Matcher m = varDecsEx.matcher(ejbQl);
        Map<String, String> map = new HashMap<String, String>();
        if (m.find()) {
            String decExpr = m.group(2);
            String[] desc = decExpr.split(",");
            for (String d : desc) {
                d = d.replace(" as ", " "); // remove optional 'as' keyword
                d = d.replace(" AS ", " ");
                Matcher md = pairEx.matcher(d);
                if (md.find()) {
                    map.put(md.group(3), md.group(1));
                }
            }
        }
        return map;
    }

    /**
     * cache of member elements (in this case, fields) associated with a type
     * name
     */
    private Map<String, List<Element>> typeFields = new HashMap<String, List<Element>>();

    /**
     * Returns (and caches) the field elements of the type 'typeElement'
     * 
     * @param typeElement
     * @return
     */
    public List<Element> getFields(TypeElement typeElement) {
        List<Element> elems = typeFields.get(typeElement);
        if (elems == null) {
            elems = new ArrayList<Element>();
            for (Element em : typeElement.getEnclosedElements()) {
                if (em.getKind() == ElementKind.FIELD) {
                    elems.add(em);
                }
            }
            TypeMirror parent = typeElement.getSuperclass();
            if (parent.getKind() != TypeKind.NONE) {
                DeclaredType dtype = (DeclaredType) parent;
                elems.addAll(getFields((TypeElement) dtype.asElement()));
            }
            typeFields.put(typeElement.getSimpleName().toString(), elems);
        }
        return elems;
    }

    /**
     * Writes the headers (package, imports, copyright boilerplate) for a given
     * entity bean's eao files
     * 
     * @param pwEao
     *            Interface Printwriter
     * @param pwEaoBean
     *            Interface Printwriter
     * @param pwEaoBeanTest
     *            Interface Printwriter
     * @param type
     *            the entity class
     * @param eaoName
     *            eao name derived from entity name
     * @param hasFinder
     *            true if entity declares NamedQueries
     * @throws ClassNotFoundException
     */
    private void writeHeaders(PrintWriter pwEao, PrintWriter pwEaoBean,
            PrintWriter pwEaoBeanTest, TypeElement type, String eaoName,
            boolean hasFinder, Extends ex) throws ClassNotFoundException {
        int lastDot = eaoName.lastIndexOf('.');
        String packageStr = "";
        String eaoBaseName = eaoName.substring(lastDot + 1);
        String entitySimple = type.getSimpleName().toString();

        if (lastDot > 0) {
            packageStr = eaoName.substring(0, lastDot);
            if (!isTestContext) {
                pwEao.println("package " + packageStr + ";");
                pwEao.println();
                pwEaoBean.println("package " + packageStr + ";");
                pwEaoBean.println();
            } else if (hasFinder) {
                pwEaoBeanTest.println("package " + packageStr + ";");
                pwEaoBeanTest.println();
            }

        }

        if (!isTestContext) {
            pwEaoBean.println("import java.util.Collection;");
            pwEaoBean.println("import java.util.List;");
            pwEaoBean.println("import javax.ejb.Stateless;");
            pwEaoBean.println("import javax.persistence.Query;");
            pwEaoBean.println("import javax.persistence.NonUniqueResultException;");
            pwEaoBean.println("import javax.annotation.Generated;");
            pwEaoBean.println("import com.akuacom.ejb.BaseEAOBean;");
        } else if (hasFinder) {
            pwEaoBeanTest.println("import java.util.Collection;");
        }

        if (!isTestContext) {
            pwEao.println("import javax.ejb.Remote;");
            pwEao.println("import javax.ejb.Local;");
            pwEao.println("import javax.annotation.Generated;");
        } else if (hasFinder) {
            pwEaoBeanTest.println("import javax.annotation.Generated;");
            pwEaoBeanTest.println("import org.junit.Test;");
            pwEaoBeanTest
                    .println("import static org.junit.Assert.assertNotNull;");
            pwEaoBeanTest.println("import static org.junit.Assert.assertTrue;");
            pwEaoBeanTest.println("import com.akuacom.utils.lang.Invoker;");
            pwEaoBeanTest.println("import com.akuacom.utils.lang.FieldUtil;");
            pwEaoBeanTest.println("import com.akuacom.ejb.EntityUtil;");
            pwEaoBeanTest.println("import " + type + ";");
            pwEaoBeanTest.println("import " + eaoName + ";");
            if (ex == Extends.VersionedEntity) {
                pwEaoBeanTest
                        .println("import com.akuacom.ejb.AbstractVersionedEAOTest;");
            } else if (ex == Extends.BaseEntity) {
                pwEaoBeanTest
                        .println("import com.akuacom.ejb.AbstractBaseEAOTest;");
            }
        }

        if (!isTestContext) {
            pwEao.println("import java.util.Collection;");
            pwEao.println("import java.util.List;");
            pwEao.println("import com.akuacom.ejb.BaseEAO;");

            pwEaoBean.println();
            pwEaoBean.println("/**");
            pwEaoBean.println(" * Generated " + new Date());
            pwEaoBean.println(" *  (c) 2010 Akuacom. All rights reserved.");
            pwEaoBean
                    .println(" *  Redistribution and use in source and binary forms, with or without modification, is prohibited.");

            pwEaoBean.println(" */");
            pwEaoBean.println("@Generated(\"" + type + "\")");
        } else if (hasFinder) {
            pwEaoBeanTest.println();
            pwEaoBeanTest.println("/**");
            pwEaoBeanTest.println(" * Generated " + new Date());
            pwEaoBeanTest.println(" *  (c) 2010 Akuacom. All rights reserved.");
            pwEaoBeanTest
                    .println(" *  Redistribution and use in source and binary forms, with or without modification, is prohibited.");

            pwEaoBeanTest.println(" */");
            pwEaoBeanTest.println("@Generated(\"" + type + "\")");
            pwEaoBeanTest.print("public class " + StringUtil.dePackage(eaoName)
                    + "BeanTest");
            if (ex == Extends.VersionedEntity) {
                pwEaoBeanTest.println(" extends AbstractVersionedEAOTest<"
                        + eaoBaseName + ", " + entitySimple + "> {");
            } else if (ex == Extends.BaseEntity) {
                pwEaoBeanTest.println(" extends AbstractBaseEAOTest<"
                        + eaoBaseName + ", " + entitySimple + "> {");
            } else {
                pwEaoBeanTest.println(" {");
            }
            pwEaoBeanTest.println();
            pwEaoBeanTest.println("\tpublic " + StringUtil.dePackage(eaoName)
                    + "BeanTest() {");
            pwEaoBeanTest.println("\t\tsuper(" + StringUtil.dePackage(eaoName)
                    + "Bean.class);");
            pwEaoBeanTest.println("\t}");
            pwEaoBeanTest.println();
        }

        if (!isTestContext) {
            pwEao.println();

            pwEao.println("/**");
            pwEao.println(" * Generated " + new Date());
            pwEao.println(" *  (c) 2010 Akuacom. All rights reserved.");
            pwEao.println(" *  Redistribution and use in source and binary forms, with or without modification, is prohibited.");
            pwEao.println(" */");
            pwEao.println("@Generated(\"" + type + "\")");
        }

    }

    public void writeTestMethods(TypeElement type, PrintWriter pwEaoBeanTest,
            Extends ex) {
        String entitySimple = type.getSimpleName().toString();

        if (ex == Extends.VersionedEntity) {
            pwEaoBeanTest.println("\t@Override");
            pwEaoBeanTest
                    .println("\tprotected void assertBasePropertiesAfterCreation("
                            + entitySimple + " created) {");
            pwEaoBeanTest
                    .println("\t\tString createdBy = created.getCreator();");
            pwEaoBeanTest.println("\t\tassertNotNull(createdBy);");
            pwEaoBeanTest.println("\t}");
            pwEaoBeanTest.println();
        }

        pwEaoBeanTest.println("\tprotected void assertEntityValuesNotEquals("
                + entitySimple + " created, " + entitySimple + " found) {");
        pwEaoBeanTest
                .println("\t\tassertTrue(!FieldUtil.structurallyEquals(created,found));");
        pwEaoBeanTest.println("\t}");
        pwEaoBeanTest.println();
        pwEaoBeanTest.println("\tprotected void assertEntityValuesEquals("
                + entitySimple + " created, " + entitySimple + " found) {");
        pwEaoBeanTest
                .println("\t\tassertTrue(FieldUtil.structurallyEquals(created,found));");
        pwEaoBeanTest.println("\t}");
        pwEaoBeanTest.println();
        pwEaoBeanTest.println("\tprotected void mutate(" + entitySimple
                + " found) {");
        pwEaoBeanTest.println("\t\tEntityUtil.randomlyPopulate(found);");
        pwEaoBeanTest.println("\t}");
        pwEaoBeanTest.println();
        pwEaoBeanTest.println("\tprotected " + entitySimple
                + " generateRandomEntity() {");
        pwEaoBeanTest
                .println("\t\treturn EntityUtil.randomlyPopulate( EntityUtil.neux("
                        + entitySimple + ".class));");
        pwEaoBeanTest.println("\t}");
        pwEaoBeanTest.println();
    }

    private Tuple2<String, ReturnType> getNameAndType(NamedQuery nq) {
        String name = nq.name();
        Matcher m = queryNameAndTypeEx.matcher(name);
        ReturnType rt = ReturnType.List;
        if (m.find()) {
            String type = m.group(3);
            if ("single".equalsIgnoreCase(type)) {
                rt = ReturnType.Single;
            } else if ("set".equalsIgnoreCase(type)) {
                rt = ReturnType.Set;
            }
            return new Tuple2<String, ReturnType>(m.group(2), rt);
        }
        m  = queryNameEx.matcher(name);
        if (m.find()) {
            return new Tuple2<String, ReturnType>(m.group(2), rt);
        }
        return null;
    }

    /**
     * Called for each annotated entity, generates the EAO classes (interface,
     * implementation, and if there are named queries, the test class for them)
     * 
     * @param type
     *            entity class
     * @param eaoName
     *            base name of EAO gen sources
     * @param ex
     *            which base entity class is extended
     * @throws IOException
     */
    public void writeEaoSourceFilesForType(TypeElement type, String eaoName,
            Extends ex) throws IOException {
        try {
            String eaoBeanName = eaoName + "Bean";
            String eaoBeanTestName = eaoBeanName + "Test";
            NamedQueries nqs = type.getAnnotation(NamedQueries.class);

            boolean hasFinder = hasFinderOrDeleter(nqs);
            if(!hasFinder) {
                log.info(type + " has no finder/deleters");
            }
            Filer filer = processingEnv.getFiler();

            OutputStream osEao = (!isTestContext ? filer.createSourceFile(
                    eaoName).openOutputStream() : null);
            OutputStream osEaoBean = (!isTestContext ? filer.createSourceFile(
                    eaoBeanName).openOutputStream() : null);
            OutputStream osEaoBeanTest = ((hasFinder && isTestContext) ? filer
                    .createSourceFile(eaoBeanTestName).openOutputStream()
                    : null);

            PrintWriter pwEao = (!isTestContext ? new PrintWriter(osEao) : null);
            PrintWriter pwEaoBean = (!isTestContext ? new PrintWriter(osEaoBean)
                    : null);
            PrintWriter pwEaoBeanTest = (hasFinder && isTestContext) ? new PrintWriter(
                    osEaoBeanTest) : null;

            int lastDot = eaoName.lastIndexOf('.');
            String eaoBaseName = eaoName.substring(lastDot + 1);
            String eaoBeanBaseName = (eaoName + "Bean").substring(lastDot + 1);
            writeHeaders(pwEao, pwEaoBean, pwEaoBeanTest, type, eaoName,
                    hasFinder, ex);

            String entitySimple = type.getSimpleName().toString();

            if (!isTestContext) {
                // i/f and class declarations
                pwEao.println("public interface " + eaoBaseName + " extends "
                        + BASE_EAO_SIMPLE + "<" + entitySimple + "> {");
                pwEao.println();
                pwEao.println("\t@Remote");
                pwEao.println("\tpublic interface R extends "
                        + eaoBaseName +"{}");
                pwEao.println("\t@Local");
                pwEao.println("\tpublic interface L extends "
                        + eaoBaseName +"{}");
                pwEao.println();

                pwEaoBean.println("@Stateless");
                pwEaoBean.println("public class " + eaoBeanBaseName
                        + " extends " + BASE_EAO_BEAN_SIMPLE + "<"
                        + entitySimple + "> " + "implements " + eaoBaseName + ".R, " + eaoBaseName + ".L {");
                pwEaoBean.println();

                // constructors
                pwEaoBean.println("\tpublic " + eaoBeanBaseName + "() { super("
                        + entitySimple + ".class); } "); // constructor
                pwEaoBean.println("\tpublic " + eaoBeanBaseName + "(Class<"
                        + entitySimple
                        + "> entityClass) { super(entityClass); } "); // constructor

                pwEaoBean.println();
            }

            String eList = "List<" + entitySimple + ">";
            String eSet = "java.util.Set<" + entitySimple + ">";
            String eSingle = entitySimple;

            List<? extends Element> members = getFields(type);
            // write finders
            if (nqs != null) {
                Set<String>methodNames = new HashSet<String>();
                for (NamedQuery q : nqs.value()) {
                    // convention is if namedq starts with 'find' it's expected
                    // that it will return a collection
                    // of entities
                    // use 'get' for queries returning scalars/non-entities
                    if (q.name().indexOf(".find") != -1
                            || q.name().indexOf(".Find") != -1) {
                        Tuple2<String, ReturnType> nameAndType = getNameAndType(q);
                        
                        String method = nameAndType._1;
                        if(Character.isUpperCase(method.charAt(0))) {
                            method = Character.toLowerCase(method.charAt(0)) + method.substring(1);
                        }

                        if(methodNames.contains(method)) {
                            method = method + "_" + nameAndType._2.toString();
                        }
                        methodNames.add(method);
                        // if entity class has a field named <arg>, use its type,
                        // otherwise use Object, unless op is 'IN'
                        // then assume type is Collection<type>
                        
                        String whereClause = q.query().substring(q.query().indexOf(" where ") + 6);
                        log.info("whereClause " + whereClause);
                        List<Tuple3<String, String, String>> fieldOpParams = args(whereClause);
                        log.info("fieldOpParams " + Dbg.oS(fieldOpParams));
                        Map<String, String> declaredVars = declaredVariables(q
                                .query());
                        log.info("declaredVariables " + Dbg.oS(declaredVars));
                        StringBuffer argBuff = new StringBuffer();
                        for (Tuple3<String, String, String> fieldOpParam : fieldOpParams) {
                            String typeString = inferType(type, members, fieldOpParam,
                                    declaredVars);
                            if ("in".equalsIgnoreCase(fieldOpParam._2())) {
                                typeString = "Collection<" + typeString + ">";
                            }
                            argBuff.append(typeString);
                            argBuff.append(" ");
                            argBuff.append(fieldOpParam._3());
                            argBuff.append(", ");
                        }
                        if (fieldOpParams.size() > 0) {
                            argBuff.setLength(argBuff.length() - 2);
                        }
    
                        if (!isTestContext) {
                            switch (nameAndType._2) {
                            case Set:
                                pwEao.println("\t" + eSet + "  " + method + "("
                                        + argBuff + ");");
                                pwEaoBean.println("\tpublic " + eSet + " "
                                        + method + "(" + argBuff + ") {");
                                break;
                            case Single:
                                pwEao.println("\t" + eSingle + " " + method + "("
                                        + argBuff + ");");
                                pwEaoBean.println("\tpublic " + eSingle + " "
                                        + method + "(" + argBuff + ") {");
                                break;
                            case List:
                                pwEao.println("\t" + eList + " " + method + "("
                                        + argBuff + ");");
                                pwEaoBean.println("\tpublic " + eList + " "
                                        + method + "(" + argBuff + ") {");
                                break;
                            }
    
                            pwEaoBean
                                    .println("\t\tQuery q = em.createNamedQuery( \""
                                            + q.name() + "\" );");
                            if (!fieldOpParams.isEmpty()) {
                                for (Tuple3<String, String, String> fieldOpParam : fieldOpParams) {
                                    pwEaoBean.print("\t\tq.setParameter(\"");
                                    pwEaoBean.print(fieldOpParam._3());
                                    pwEaoBean.print("\", ");
                                    pwEaoBean.print(fieldOpParam._3());
                                    pwEaoBean.println(");");
                                }
                            }
                            switch (nameAndType._2) {
                            case Set:
                                pwEaoBean
                                .println("\t\treturn new java.util.HashSet<" + entitySimple + ">(q.getResultList());");
                                break;
                            case Single:
                                pwEaoBean
                                .println("\t\t" + eList + " val = q.getResultList();");
                                pwEaoBean
                                .println("\t\tif(val.isEmpty()) {");
                                pwEaoBean
                                .println("\t\t\treturn null;");
                                pwEaoBean
                                .println("\t\t} else if (val.size() == 1) {");
                                pwEaoBean
                                .println("\t\t\treturn val.get(0);");
                                pwEaoBean
                                .println("\t\t} else {");
                                pwEaoBean
                                .println("\t\t\tthrow new NonUniqueResultException(q.toString());" );
                                pwEaoBean
                                .println("\t\t}");
                                break;
                            case List:
                                pwEaoBean.println("\t\treturn q.getResultList();");
                                break;
                            }
                            pwEaoBean.println("\t}");
    
                            pwEaoBean.println();
                        } else {
                            pwEaoBeanTest.println("\t@Test public void test"
                                    + upperFirstChar(method) + "() {");
                            pwEaoBeanTest
                                    .println("\t\tassertNotNull(Invoker.test(eao,\""
                                            + method + "\"));");
                            pwEaoBeanTest.println("\t}");
                            pwEaoBeanTest.println();
                        }
                        
                    } else if (q.name().indexOf(".delete") != -1
                            || q.name().indexOf(".Delete") != -1) {
                        Tuple2<String, ReturnType> nameAndType = getNameAndType(q);
                        log.info("deleter:  nameAndType " + nameAndType);
                        String method = nameAndType._1;
                        if(methodNames.contains(method)) {
                            method = method + "_" + nameAndType._2.toString();
                        }
                        methodNames.add(method);
                        // if entity class has a field named <arg>, use its type,
                        // otherwise use Object, unless op is 'IN'
                        // then assume type is Collection<type>
                        List<Tuple3<String, String, String>> fieldOpParams = args(q
                                .query());
                        Map<String, String> declaredVars = declaredVariables(q
                                .query());
                        StringBuffer argBuff = new StringBuffer();
                        for (Tuple3<String, String, String> fieldOpParam : fieldOpParams) {
                            String typeString = inferType(type, members, fieldOpParam,
                                    declaredVars);
                            if ("in".equalsIgnoreCase(fieldOpParam._2())) {
                                typeString = "Collection<" + typeString + ">";
                            }
                            argBuff.append(typeString);
                            argBuff.append(" ");
                            argBuff.append(fieldOpParam._3());
                            argBuff.append(", ");
                        }
                        if (fieldOpParams.size() > 0) {
                            argBuff.setLength(argBuff.length() - 2);
                        }
    
                        if (!isTestContext) {
                            pwEao.println("\tlong " + method + "("
                                    + argBuff + ");");
                            pwEaoBean.println("\tpublic long " + method + "(" + argBuff + ") {");
                            
                            pwEaoBean
                                    .println("\t\tQuery q =  em.createNamedQuery( \""
                                            + q.name() + "\" );");
                            if (!fieldOpParams.isEmpty()) {
                                for (Tuple3<String, String, String> fieldOpParam : fieldOpParams) {
                                    pwEaoBean.print("\t\tq.setParameter(\"");
                                    pwEaoBean.print(fieldOpParam._3());
                                    pwEaoBean.print("\", ");
                                    pwEaoBean.print(fieldOpParam._3());
                                    pwEaoBean.println(");");
                                }
                            }

                            pwEaoBean
                            .println("\t\treturn (long) q.executeUpdate();");

                            pwEaoBean.println("\t}");
    
                            pwEaoBean.println();
                        } else {
                            pwEaoBeanTest.println("\t@Test public void test"
                                    + upperFirstChar(method) + "() {");
                            pwEaoBeanTest
                                    .println("\t\tassertNotNull(Invoker.test(eao,\""
                                            + method + "\"));");
                            pwEaoBeanTest.println("\t}");
                            pwEaoBeanTest.println();
                        }
                        
                    }

                }

                if (isTestContext)
                    writeTestMethods(type, pwEaoBeanTest, ex);
            }
            
            if (!isTestContext) {
                pwEao.println("}");
                pwEao.close();

                pwEaoBean.println("}");
                pwEaoBean.close();

                osEao.close();
                osEaoBean.close();
            } else if (hasFinder) {
                pwEaoBeanTest.append("}\n");
                pwEaoBeanTest.close();
                osEaoBeanTest.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
