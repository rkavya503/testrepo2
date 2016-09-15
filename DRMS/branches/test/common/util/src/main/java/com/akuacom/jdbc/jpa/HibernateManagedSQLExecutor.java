package com.akuacom.jdbc.jpa;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import org.hibernate.Session;

import com.akuacom.jdbc.Converter;
import com.akuacom.jdbc.SQLExecutor;

public abstract class HibernateManagedSQLExecutor {

	protected abstract Session  getHibernateSession(); 
	protected abstract SQLExecutor getSQLExecutor();
	
	public <T> T doNativeQuery(final String sql, final Object[] params,
			final Converter<T> factory) throws SQLException {
		Session session = getHibernateSession(); 
		// To perform JDBC related work using the Connection managed by
		// hibernate Session.
		NativeHibernateWork<T> work = new NativeHibernateWork<T>() {
			@Override
			public void execute(Connection connection) throws SQLException {
				expectResult = getSQLExecutor().doNativeQuery(connection, sql, params,
						factory);
			}
		};
		session.doWork(work);
		return work.getExpectResult();
	}

	public <T> T doNativeQuery(final String sql, final Converter<T> factory)
			throws SQLException {
		Session session =getHibernateSession(); 
		NativeHibernateWork<T> work = new NativeHibernateWork<T>() {
			@Override
			public void execute(Connection connection) throws SQLException {
				expectResult = getSQLExecutor().doNativeQuery(connection, sql, factory);
			}
		};
		session.doWork(work);
		return work.getExpectResult();
	}

	public <T> T doNativeQuery(final String sql,
			final Map<String, Object> namedParameters,
			final Converter<T> converter) throws SQLException {
		Session session= getHibernateSession(); 
		NativeHibernateWork<T> work = new NativeHibernateWork<T>() {
			@Override
			public void execute(Connection connection) throws SQLException {
				expectResult = getSQLExecutor().doNativeQuery(connection, sql,
						namedParameters, converter);
			}
		};
		session.doWork(work);
		return work.getExpectResult();
	}

	public int execute(final String sql) throws SQLException {
		Session session= getHibernateSession(); 
		NativeHibernateWork<Integer> work = new NativeHibernateWork<Integer>() {
			@Override
			public void execute(Connection connection) throws SQLException {
				expectResult = getSQLExecutor().execute(connection, sql);
			}
		};
		session.doWork(work);
		return work.getExpectResult();
	}

	public int execute(final String sql,
			final Map<String, Object> namedParameters) throws SQLException {
		Session session= getHibernateSession(); 
		NativeHibernateWork<Integer> work = new NativeHibernateWork<Integer>() {
			@Override
			public void execute(Connection connection) throws SQLException {
				expectResult = getSQLExecutor().execute(connection, sql,
						namedParameters);
			}
		};
		session.doWork(work);
		return work.getExpectResult();
	}

	public int execute(final String sql, final Object[] parameters)
			throws SQLException {
		Session session= getHibernateSession(); 
		NativeHibernateWork<Integer> work = new NativeHibernateWork<Integer>() {
			@Override
			public void execute(Connection connection) throws SQLException {
				expectResult = getSQLExecutor().execute(connection, sql, parameters);
			}
		};
		session.doWork(work);
		return work.getExpectResult();
	}

}
