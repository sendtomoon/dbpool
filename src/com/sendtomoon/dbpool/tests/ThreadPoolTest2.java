package com.sendtomoon.dbpool.tests;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sendtomoon.dbpool.DBConnectionPool;
import com.sendtomoon.dbpool.Pool;

public class ThreadPoolTest2 {

	public static void main(String[] args) throws Exception {
		ExecutorService es = Executors.newFixedThreadPool(100);
		for (int i = 1; i <= 1000; i++) {
			es.execute(new Runnable() {
				@Override
				public void run() {
					try {
						return;
					} catch (Exception e) {
						e.printStackTrace();
					}
					es.shutdown();
				}
			});

		}
	}
}
