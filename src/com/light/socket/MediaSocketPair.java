package com.light.socket;

import java.util.ArrayList;
import java.util.List;

public class MediaSocketPair {
	MediaSocket 	   sender ;
	List<MediaSocket>  receiverList  ;
	
	public MediaSocketPair(){
		this.sender = null ;
		this.receiverList =  new ArrayList<MediaSocket>() ;
	};
	
	public MediaSocket getSender() {
		return sender;
	}
	public void setSender(MediaSocket sender) {
		this.sender = sender;
	}
	public List<MediaSocket> getReceiverList() {
		return receiverList;
	}
	public void setReceiverList(List<MediaSocket> receiverList) {
		this.receiverList = receiverList;
	}
}
