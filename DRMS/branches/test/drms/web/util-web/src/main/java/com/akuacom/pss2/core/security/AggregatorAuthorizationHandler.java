package com.akuacom.pss2.core.security;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.servlet.http.HttpServletRequest;

import com.akuacom.pss2.core.security.proxy.AuthorizationCollection;
import com.akuacom.pss2.core.security.proxy.AuthorizationCollectionImpl;

public class AggregatorAuthorizationHandler implements AuthorizationHandler {

	private static final long serialVersionUID = 1L;
	private AuthorizationHandler authHandler = null;
	private AuthorizationCollection authCollection = null;
	
	public AggregatorAuthorizationHandler(AuthorizationHandler authHandler,final String authenticatedUser) {
		this.authHandler = authHandler;
		this.authCollection = (AuthorizationCollection) Proxy
				.newProxyInstance(
						AuthorizationCollection.class.getClassLoader(),
						new Class<?>[] { AuthorizationCollection.class },
						new InvocationHandler() {
							private Object originalRef;
							
							@Override
							public Object invoke(Object proxy, Method method, Object[] args)
							        throws Throwable {
								if (originalRef == null) {
									originalRef = loadObject();
								}
								
							   return method.invoke(originalRef, args);
							}
							protected Object loadObject() {
								return new AuthorizationCollectionImpl(authenticatedUser);
							}
						});
		
	}

	@Override
	public boolean isAuthPermission(HttpServletRequest request) {
		Object targetUser = request.getParameter("user");
		if (targetUser == null)
			return Boolean.TRUE;

		if (authHandler.isAuthPermission(request)){
			return Boolean.TRUE;
		}

		if (authCollection.getPermittedResource(request).contains(targetUser)){
			return Boolean.TRUE;
		}
		authCollection.refresh();
		if (authCollection.getPermittedResource(request).contains(targetUser)){
			return Boolean.TRUE;
		}
		
		return false;
	}

}
