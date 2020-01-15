package com.sendtomoon.dbpool;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DBConnectionPool extends Pool {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	private int checkedOut;// 正在使用的连接数

	private Vector<Connection> freeConnections = new Vector<Connection>();// 存放产生的连接对象容器

	private String password = null;
	private String url = null;
	private String username = null;
	private static int num = 0;// 空闲连接数
	private static int numActive = 0;// 当前可用连接数
	private int connectionTimeOut = 2000;// 等待超时时间
	private static DBConnectionPool pool = null;// 连接池实例变量

	// 生成数据连接池
	// 懒汉模式写法
	public static synchronized DBConnectionPool getInstance() {
		if (pool == null) {
			pool = new DBConnectionPool(null);
		}
		return pool;
	}

	// 生成数据连接池
	// 懒汉模式写法
	public static synchronized DBConnectionPool getInstance(DBProperties properties) {
		if (pool == null) {
			pool = new DBConnectionPool(properties);
		}
		return pool;
	}

	// 私有构造方法，获得一个数据连接实例
	private DBConnectionPool(DBProperties properties) {
		try {
			init(properties);
			for (int i = 0; i < normalConnect; i++) {
				Connection conn = newConnection();
				if (conn != null) {
					freeConnections.addElement(conn);
					num++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void init(DBProperties properties) throws IOException {
		if (properties != null) {
			this.username = properties.getUsername();
			this.password = properties.getPassword();
			this.url = properties.getUrl();
			this.dirvername = properties.getDirvername();
			this.maxConnect = properties.getMaxConnect();
			this.normalConnect = properties.getNormalConnect();
			this.connectionTimeOut = properties.getConnectionTimeOut();
			return;
		}
		InputStream is = Pool.class.getResourceAsStream(propertiesName);
		Properties prop = new Properties();
		prop.load(is);
		this.username = prop.getProperty("username");
		this.password = prop.getProperty("password");
		this.url = prop.getProperty("url");
		this.dirvername = prop.getProperty("dirvername");
		this.maxConnect = Integer.parseInt(prop.getProperty("maxConnect"));
		this.normalConnect = Integer.parseInt(prop.getProperty("normalConnect"));
		this.connectionTimeOut = Integer.parseInt(prop.getProperty("connectionTimeOut"));
	}

	@Override
	public synchronized void freeConnection(Connection conn) {
		freeConnections.addElement(conn);
		num++;
		checkedOut--;
		numActive--;
		notifyAll();
	}

	// 创建一个新的连接
	private Connection newConnection() {
		Connection conn = null;
		try {
			if (username == null) {
				conn = DriverManager.getConnection(url);
			} else {
				conn = DriverManager.getConnection(url, username, password);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	@Override
	public void createdPool() {
		pool = new DBConnectionPool(null);
		if (pool == null) {
			System.err.println("创建线程池失败");
		}
	}

	// 单例模式获取一个连接
	@Override
	public synchronized Connection getConnection() {
		Connection conn = null;
		if (freeConnections.size() > 0) {
			num--;
			conn = freeConnections.firstElement();
			freeConnections.removeElementAt(0);
			try {
				if (conn.isClosed()) {// 如果连接已经关闭，则删除无效链接
					conn = getConnection();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (maxConnect == 0 || checkedOut < maxConnect) {
			conn = newConnection();
		} else {
//			System.err.println("没有可以连接资源");
			return null;
		}
		if (conn != null) {
			checkedOut++;
		}
		numActive++;
		return conn;
	}

	@Override
	public synchronized Connection getConnection(long timeout) {
		long startTime = new Date().getTime();
		Connection conn;
		while ((conn = getConnection()) == null) {
			try {
				wait(timeout);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if ((new Date().getTime() - startTime) >= connectionTimeOut) {
				return null;
			}
		}
		return conn;
	}

	// 释放连接池
	public synchronized void release() {
		try {
			Enumeration<Connection> allConnections = freeConnections.elements();
			while (allConnections.hasMoreElements()) {
				Connection conn = allConnections.nextElement();
				try {
					conn.close();
					num--;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			freeConnections.removeAllElements();
			numActive = 0;
		} finally {
			super.release();
		}
	}

	@Override
	public int getnum() {
		return num;
	}

	@Override
	public int getnumActive() {
		return numActive;
	}

}
