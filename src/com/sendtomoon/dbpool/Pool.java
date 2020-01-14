package com.sendtomoon.dbpool;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Properties;

public abstract class Pool {

	public String propertiesName = "connection-conf.properties";

	private static Pool instance = null;// 定义唯一实例

	protected int maxConnect = 100;// 最大链接数

	protected int normalConnect = 10; // 保持链接数

	protected String dirvername = null; // JDBC驱动字符串

	protected Driver driver = null; // JDBC驱动实例

	// 私有构造函数
	protected Pool() {
		try {
			init();
			loadDirver(dirvername);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void init() throws IOException {
		InputStream is = Pool.class.getResourceAsStream(propertiesName);
		Properties prop = new Properties();
		prop.load(is);
		this.dirvername = prop.getProperty("dirvername");
		this.maxConnect = Integer.parseInt(prop.getProperty("maxConnect"));
		this.normalConnect = Integer.parseInt(prop.getProperty("normalConnect"));
	}

	protected void loadDirver(String driverClassName) {
		try {
			driver = (Driver) Class.forName(driverClassName).newInstance();
			DriverManager.registerDriver(driver);
		} catch (Exception e) {

		}
	}

	/**
	 * 
	 */
	public abstract void createdPool();

	public static synchronized Pool getInstance()
			throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		if (instance == null) {
			instance.init();
			instance = (Pool) Class.forName("com.sendtomoon.dbpool.Pool").newInstance();
		}
		return instance;
	}

	public abstract Connection getConnection();

	public abstract Connection getConnection(long time);

	public abstract void freeConnection(Connection conn);

	public abstract int getnum();

	public abstract int getnumActive();

	// 撤销驱动
	protected synchronized void release() {
		try {
			DriverManager.deregisterDriver(driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
