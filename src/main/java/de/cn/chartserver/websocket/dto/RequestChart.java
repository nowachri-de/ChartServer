package de.cn.chartserver.websocket.dto;

import com.google.gson.Gson;

public class RequestChart implements Command{
	public String command = "SendLine2DChart";
	
	public static void main(String[] args){
		RequestChart rc = new RequestChart();
		System.out.println((new Gson()).toJson(rc));
	}

	@Override
	public String getCommand() {
		return command;
	}
}
