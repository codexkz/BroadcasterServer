package com.light.remoteController.event;

import com.google.gson.JsonObject;
import com.light.Exception.OperationException;
import com.light.bean.Member;
import com.light.channel.ChannelManager;
import com.light.channel.ChannelManagerFactory;

public abstract class ActionEvent {
	
	//static boolean isDependencyAction ;
	boolean isDependencyAction ;
	
	ChannelManager channelManager = ChannelManagerFactory.getChannelManagerInstance();

	protected JsonObject data   ;
	protected Member 	 member ;
	
	public ActionEvent(){}
	
	
	public ActionEvent(Member member,JsonObject data){
		this.member = member;
		this.data = data;
	}
	
	public Boolean isDependencyAction(){
		return isDependencyAction ;
	}
	
	public abstract void handdle() throws OperationException;
}
