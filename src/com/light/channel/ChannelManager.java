package com.light.channel;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class ChannelManager {
	
	private Map<String,Channel> channelMap = Collections.synchronizedMap(new LinkedHashMap<String,Channel>());
	
    public ChannelManager() {} 
	
	public Boolean hasChannel(String channelID){
		return channelMap.containsKey(channelID);
	}
	
	public Channel findChannel(String channelID ){
		return channelMap.get(channelID) ;
	}
	
	public Channel putChannel(String channelID , Channel channel){
		channelMap.put(channelID, channel);
		return channel ;
	}
	
	
	public Channel createNewChannel(String channelID , String channelPassword){
		return new Channel( channelID , channelPassword );
	}
	
	public Boolean verificationConnect(String channelID,String channelPassword,String memberUUID) {
		Channel channel = channelMap.get(channelID);
		if(channel == null ) return false ;
		if(channel.getChannelPassword().equals(channelPassword)) return channel.hasMember(memberUUID) ;
		return false;
	}
	
}
