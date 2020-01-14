package com.sendtomoon.dbpool;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Properties;

public abstract class Pool {

	public String propertiesName = "connection-conf.properties";

	private static Pool instance = null;// 定义唯一实例

	protected int maxConnect = 100;// 最大链接数

	protected int normalConnect = 10; // 保持链接数

	protected String driverName = null; // JDBC驱动字符串

	protected Driver driver = null; // JDBC驱动实例

	// 私有构造函数
	protected Pool() {
		try {
			init();
			loadDirver(driverName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void init() throws IOException {
		InputStream is = Pool.class.getResourceAsStream(propertiesName);
		Properties prop = new Properties();
		prop.load(is);
		this.driverName = prop.getProperty("dirverName");
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

}
