package com.light.remoteController.event;

import com.google.gson.JsonObject;
import com.light.Exception.OperationException;
import com.light.bean.Member;
import com.light.channel.Channel;
import com.light.socket.MediaSocketPair;

public class OpenMediaSocketEvent extends ActionEvent{
	
	//static { isDependencyAction = false ; }
	boolean isDependencyAction = false ;
	
	public OpenMediaSocketEvent(Member member, JsonObject data) {
		super(member,data);
	}
	
	@Override
	public void handdle() throws OperationException {
		String  channelID = data.get("channelID").getAsString();
		Channel channel   = channelManager.findChannel(channelID);
		//String  name 	  = data.get("messageBody").getAsJsonObject().get("data").getAsString();
		
		synchronized(channel.getMediaSocketPairCounter().getLock()){
			Integer mediaSocketPairid = channel.getChatMessageCounter().add() ;
			channel.getMediaSocketPairMap().put( mediaSocketPairid.toString() , new MediaSocketPair());
			data.get("messageBody").getAsJsonObject().addProperty("mediaSocketPairID", mediaSocketPairid.toString());
		}
		
	}

}
