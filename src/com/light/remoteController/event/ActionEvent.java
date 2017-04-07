package com.light.remoteController.event;

import com.light.Exception.OperationException;
import com.light.channel.ChannelManager;
import com.light.channel.ChannelManagerFactory;

public abstract class ActionEvent {
	
	static boolean isDependencyAction ;
	ChannelManager channelManager = ChannelManagerFactory.getChannelManagerInstance();
	
	public ActionEvent(){}
	public Boolean isDependencyAction(){
		return isDependencyAction ;
	}
	
	public abstract void handdle() throws OperationException;
}
