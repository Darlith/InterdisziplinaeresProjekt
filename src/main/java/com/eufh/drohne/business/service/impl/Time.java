package com.eufh.drohne.business.service.impl;

public class Time {
	private int minutes;
	private int seconds;
	
	public Time(int minutes, int seconds)
	{
		this.minutes = minutes;
		this.seconds = seconds;
	}
	
	public Time()
	{
		this.minutes = 0;
		this.seconds = 0;
	}
	
	public void plusMinutes(int minutes)
	{
		this.minutes += minutes;
	}
	public void minusMinutes(int minutes)
	{
		this.minutes -= minutes;
	}
	public void plusSeconds(int seconds)
	{
		this.seconds += seconds;
	}
	public void minusSeconds(int seconds)
	{
		this.seconds -= seconds;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	public int getSeconds() {
		return seconds;
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}

	@Override
	public String toString() {
		return "Time [minutes=" + minutes + ", seconds=" + seconds + "]";
	}
	
	
}
