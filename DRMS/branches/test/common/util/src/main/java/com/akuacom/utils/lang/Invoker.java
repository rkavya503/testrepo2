package com.akuacom.utils.lang;

/*
 * www.akuacom.com - Automating Demand Response
 * 
 * Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import scala.Tuple2;

/**
 * Helper that supports automated test generation calling
 * Invoker.test(className, methodName) will cause: a newInstance on class
 * 'className' invocation of method 'methodName' on above instance with random
 * argument values from Invoker.populate
 * 
 */
public class Invoker {
	static Map<Class<?>, Map<Tuple2<String, Class<?>[]>, Method>> classLinealMethodNameMethod = new HashMap<Class<?>, Map<Tuple2<String, Class<?>[]>, Method>>();
	static Map<Class<?>, Set<Method>> classLinealMethod = new HashMap<Class<?>, Set<Method>>();

    static Logger log = Logger.getLogger(Invoker.class);


	/**
	 * Generates random values for arguments
	 * 
	 * @param m
	 * @return
	 */
	public static Object[] randArgsForMethod(Method m) {
		Class<?>[] argSig = m.getParameterTypes();
		Object[] args = new Object[argSig.length];
		FieldUtil.populate(args, argSig);
		return args;
	}
	
    /**
     * Invokes (test) method methodName on instance
     * used by the test generator part of the entity annotation
     * processor
     * @param instance
     * @param methodName
     * @return
     */
    public static Object test(Object instance, String methodName) {
        Object result = null;
        Class<?> c = instance.getClass();
        String className = c.getSimpleName();
        try {
            Method m = null;
            Method[] declared = c.getDeclaredMethods();
            for (Method d : declared) {
                if (d.getName().equals(methodName)) {
                    m = d;
                    break;
                }
            }
            if (m != null) {
                log.debug("invoking " + className + "." + methodName);
                result = m.invoke(instance, randArgsForMethod(m));
                log.debug("->" + result);
            } else {
                log.debug("no method " + className + "." + methodName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("problem with " + className + "." + methodName);
        } 
        return result;
    }

    public static <T> void thread(Method m, Object actor, Collection<T> args) {
        Collection<T> copy = new ArrayList<T>(args);
        for (Object curr : copy) {
            Object[] flat_args = new Object[1];
            try {
                flat_args[0] = curr;
                m.invoke(actor, flat_args);
            } catch (Exception e) {
                log.error(e);
            }
        }
    }

    public static interface TimeLimiter<T> {
        T newProxy(T target, Class<T> interfaceType, long timeoutDuration,
                TimeUnit timeoutUnit);

        T callWithTimeout(Callable<T> callable, long timeoutDuration,
                TimeUnit timeoutUnit, boolean interruptible) throws Exception;
    }

    static private final ExecutorService executor = Executors
            .newCachedThreadPool();

    /**
     * This method effects 'weaving' of 'around advice' (in this case, a timeout) 
     * on arbitrary methods by creating an invocation handler (method interceptor) 
     * and sticking in on a proxy class (the woven binary) 
     *   
     * @param target
     * @param interfaceType
     * @param timeoutDuration
     * @param timeoutUnit
     * @return
     */
    public static <T> T timeoutableProxy(final T target, Class<T> interfaceType,
            final long timeoutDuration, final TimeUnit timeoutUnit) {

        final Set<Method> interruptibleMethods = findInterruptibleMethods(interfaceType);

        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object obj, final Method method,
                    final Object[] args) throws Throwable {
                Callable<Object> callable = new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        try {
                            return method.invoke(target, args);
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                            throw e;
                        }
                    }
                };
                return callWithTimeout(callable, timeoutDuration, timeoutUnit,
                        interruptibleMethods.contains(method));
            }
        };
        return newProxy(interfaceType, handler);
    }

    static public interface UninterruptibleFuture<V> extends Future<V> {
        @Override
        V get() throws ExecutionException;

        @Override
        V get(long timeout, TimeUnit unit) throws ExecutionException,
                TimeoutException;
    }

    /**
     * Timeoutable <==> interruptible
     * 'weaves' in the throwing of InterruptedExceptions so the
     * method doesn't blow up inside when interrupted
     * @param future
     * @return
     */
    public static <V> UninterruptibleFuture<V> makeUninterruptible(
            final Future<V> future) {
        if (future instanceof UninterruptibleFuture<?>) {
            return (UninterruptibleFuture<V>) future;
        }
        return new UninterruptibleFuture<V>() {
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                return future.cancel(mayInterruptIfRunning);
            }

            @Override
            public boolean isCancelled() {
                return future.isCancelled();
            }

            @Override
            public boolean isDone() {
                return future.isDone();
            }

            @Override
            public V get(long originalTimeout, TimeUnit originalUnit)
                    throws TimeoutException, ExecutionException {
                boolean interrupted = false;
                try {
                    long end = System.nanoTime()
                            + originalUnit.toNanos(originalTimeout);
                    while (true) {
                        try {
                            // Future treats negative timeouts just like zero.
                            return future.get(end - System.nanoTime(),
                                    TimeUnit.NANOSECONDS);
                        } catch (InterruptedException e) {
                            interrupted = true;
                        }
                    }
                } finally {
                    if (interrupted) {
                        Thread.currentThread().interrupt();
                    }
                }
            }

            @Override
            public V get() throws ExecutionException {
                boolean interrupted = false;
                try {
                    while (true) {
                        try {
                            return future.get();
                        } catch (InterruptedException ignored) {
                            interrupted = true;
                        }
                    }
                } finally {
                    if (interrupted) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        };
    }

    // @Override
    public static <T> T callWithTimeout(Callable<T> callable, long timeoutDuration,
            TimeUnit timeoutUnit, boolean amInterruptible) throws Exception {
        Future<T> future = executor.submit(callable);
        try {
            if (amInterruptible) {
                try {
                    return future.get(timeoutDuration, timeoutUnit);
                } catch (InterruptedException e) {
                    future.cancel(true);
                    throw e;
                }
            } else {
                Future<T> uninterruptible = makeUninterruptible(future);
                return uninterruptible.get(timeoutDuration, timeoutUnit);
            }
        } catch (ExecutionException e) {
            throw e;
        } catch (TimeoutException e) {
            future.cancel(true);
            throw e;
        }
    }

    private static Set<Method> findInterruptibleMethods(Class<?> interfaceType) {
        Set<Method> set = new HashSet<Method>();
        for (Method m : interfaceType.getMethods()) {
            if (declaresInterruptedQ(m)) {
                set.add(m);
            }
        }
        return set;
    }

    private static boolean declaresInterruptedQ(Method method) {
        for (Class<?> exType : method.getExceptionTypes()) {
            if (exType.isAssignableFrom(InterruptedException.class)) {
                return true;
            }
        }
        return false;
    }

    private static <T> T newProxy(Class<T> interfaceType,
            InvocationHandler handler) {
        Object object = Proxy.newProxyInstance(interfaceType.getClassLoader(),
                new Class<?>[] { interfaceType }, handler);
        return interfaceType.cast(object);
    }

}
