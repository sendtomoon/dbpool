package com.sendtomoon.dbpool.tests;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.sendtomoon.dbpool.DBConnectionPool;
import com.sendtomoon.dbpool.Pool;

public class Test1 {

	public static void main(String[] args) throws Exception {
		Pool pool = DBConnectionPool.getInstance();
		Connection conn = pool.getConnection(1);
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("select now() from dual;");
		while (rs.next()) {
			System.err.println(rs.getString(1));
		}
	}
}
