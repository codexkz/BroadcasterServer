<<<<<<< HEAD
package com.light.remoteController.event;

import com.google.gson.JsonObject;
import com.light.Exception.OperationException;
import com.light.bean.Member;
import com.light.channel.Channel;
import com.light.channel.Channel.Callback;

public class RenameEvent extends ActionEvent{
	
	//static { isDependencyAction = false ; }
	boolean isDependencyAction = false ;
	
	public RenameEvent(Member member, JsonObject data) {
		super(member,data);
	}


	@Override
	public void handdle() throws OperationException {
		String  channelID = data.get("channelID").getAsString();
		String  name 	  = data.get("messageBody").getAsJsonObject().get("data").getAsString();
		Channel channel   = channelManager.findChannel(channelID);
		Callback callback =new Callback(){
			@Override
			public Member findMemberByNameCallback(Member result) {
				//If the name isn't used , so user can rename of it
				if(result == null ) member.setMemberName(name);
				return result;
			}
		};
		if(channel.findMemberByName(name,callback) != null) 
			throw new OperationException("IlligalData").setRetMsg("IsAlreadySameNameInChannel");
	}
}
=======
package com.light.remoteController.event;

import com.google.gson.JsonObject;
import com.light.Exception.OperationException;
import com.light.bean.Member;
import com.light.channel.Channel;
import com.light.channel.Channel.Callback;
import com.light.channel.ChannelManager;
import com.light.channel.ChannelManagerFactory;

public class RenameEvent extends ActionEvent{
	
	static { isDependencyAction = false ; }
	ChannelManager channelManager = ChannelManagerFactory.getChannelManagerInstance();
	
	protected JsonObject data   ;
	protected Member 	 member ;
	
	public RenameEvent(Member member,JsonObject data){
		this.member = member;
		this.data = data;
	}
	
	@Override
	public void handdle() throws OperationException {
		String  channelID = data.get("channelID").getAsString();
		String  name 	  = data.get("messageBody").getAsJsonObject().get("data").getAsString();
		Channel channel   = channelManager.findChannel(channelID);
		Callback callback =new Callback(){
			@Override
			public Member findMemberByNameCallback(Member result) {
				//If the name isn't used , so user can rename of it
				if(result == null ) member.setMemberName(name);
				return result;
			}
		};
		if(channel.findMemberByName(name,callback) != null) 
			throw new OperationException("IlligalData").setRetMsg("IsAlreadySameNameInChannel");
	}
}
>>>>>>> remote/master
