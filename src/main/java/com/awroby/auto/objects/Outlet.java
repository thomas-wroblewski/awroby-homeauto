package com.awroby.auto.objects;

import org.springframework.data.annotation.Id;

public class Outlet {

	@Id
	private String id;
	private int onCode = 0;
	private int offCode = 0;
	private String name = "Outlet";
	private int pulse = 188;
	private boolean running = false;
	


	public Outlet(){
		
	}
	
	public Outlet(int onCode, int offCode, int pulse, String name){
		this.onCode = onCode;
		this.offCode = offCode;
		this.pulse = pulse;
		this.name = name;
	}
	
	
	public int getOnCode() {
		return onCode;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setOnCode(int onCode) {
		this.onCode = onCode;
	}
	public int getOffCode() {
		return offCode;
	}
	public void setOffCode(int offCode) {
		this.offCode = offCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPulse() {
		return pulse;
	}
	public void setPulse(int pulse) {
		this.pulse = pulse;
	}
	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	@Override
	public String toString() {
		return "Outlet [id=" + id + ", onCode=" + onCode + ", offCode=" + offCode + ", name=" + name + ", pulse="
				+ pulse + ", running=" + running + "]";
	}
	
	
}
