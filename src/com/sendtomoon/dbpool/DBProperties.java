package com.sendtomoon.dbpool;

public class DBProperties {

	private String username;
	private String password;
	private String url;
	private String dirvername;
	private Integer maxConnect;
	private Integer normalConnect;
	private Integer connectionTimeOut;

	public DBProperties(String username, String password, String url, String dirvername) {
		super();
		this.username = username;
		this.password = password;
		this.url = url;
		this.dirvername = dirvername;
		this.maxConnect = 10;
		this.normalConnect = 1;
		this.connectionTimeOut = 3000;
	}

	public DBProperties(String username, String password, String url, String dirvername, Integer maxConnect,
			Integer normalConnect, Integer connectionTimeOut) {
		super();
		this.username = username;
		this.password = password;
		this.url = url;
		this.dirvername = dirvername;
		this.maxConnect = maxConnect;
		this.normalConnect = normalConnect;
		this.connectionTimeOut = connectionTimeOut;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDirvername() {
		return dirvername;
	}

	public void setDirvername(String dirvername) {
		this.dirvername = dirvername;
	}

	public Integer getMaxConnect() {
		return maxConnect;
	}

	public void setMaxConnect(Integer maxConnect) {
		this.maxConnect = maxConnect;
	}

	public Integer getNormalConnect() {
		return normalConnect;
	}

	public void setNormalConnect(Integer normalConnect) {
		this.normalConnect = normalConnect;
	}

	public Integer getConnectionTimeOut() {
		return connectionTimeOut;
	}

	public void setConnectionTimeOut(Integer connectionTimeOut) {
		this.connectionTimeOut = connectionTimeOut;
	}

}
