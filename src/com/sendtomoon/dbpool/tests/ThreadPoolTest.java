package com.sendtomoon.dbpool.tests;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sendtomoon.dbpool.DBConnectionPool;
import com.sendtomoon.dbpool.Pool;

public class ThreadPoolTest {

	public static void main(String[] args) throws Exception {
		ExecutorService es = Executors.newFixedThreadPool(100);
		for (int i = 1; i <= 1000; i++) {
			Pool pool = DBConnectionPool.getInstance();
			es.execute(new Runnable() {
				@Override
				public void run() {
					try {
						Connection conn = pool.getConnection(1000);
						if (conn == null) {
							return;
						}
						Statement st = conn.createStatement();
						ResultSet rs = st.executeQuery("select now() from dual;");
						while (rs.next()) {
							System.err.println(rs.getString(1));
						}
						pool.freeConnection(conn);
//						System.err.println(Thread.currentThread().getName() + pool);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

		}
		es.shutdown();
	}
}
