package com.light.channel;

public class ChannelManagerFactory {
	
	static{
		ChannelManagerFactory.channelManager = new ChannelManager();
	}
	
	static ChannelManager channelManager = null ;
	
	static  public ChannelManager getChannelManagerInstance(){
		if (channelManager == null) {
			channelManager = new ChannelManager(); 
        }
		return channelManager ;
	}
	
}
