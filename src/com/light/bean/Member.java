package com.light.bean;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import com.light.socket.BasicSocket;

public class Member implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String 			  		  	memberUUID 			   ;
	private String                      memberName             ;
	private BasicSocket 	  	 	 	controlerSocket   	   ; 
	private BasicSocket 	  	  		chatSocket        	   ;
	private Map<String , BasicSocket>   mediaSendSocketMap     ; //use only stream mode
	private Map<String , BasicSocket>   mediaReceiveSocketMap  ; //use only stream mode
	
	public Member(){
		this.memberUUID 			= null ;
		this.mediaSendSocketMap 	= null ;
		this.mediaReceiveSocketMap  = null ;
	}
	
	public Member( String memberUUID ){
		this.memberUUID 			= memberUUID ;
		this.memberName 			= null ;
		this.mediaSendSocketMap 	= Collections.synchronizedMap(new LinkedHashMap<String,BasicSocket>()) ;
		this.mediaReceiveSocketMap  = Collections.synchronizedMap(new LinkedHashMap<String,BasicSocket>()) ;
	}
	
	public String getMemberUUID() {
		return memberUUID;
	}

	public void setMemberUUID(String memberUUID) {
		this.memberUUID = memberUUID;
	}
	
	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	
	
	public BasicSocket getControlerSocket() {
		return controlerSocket;
	}

	public void setControlerSocket(BasicSocket controlerSocket) {
		this.controlerSocket = controlerSocket;
	}

	public BasicSocket getChatSocket() {
		return chatSocket;
	}

	public void setChatSocket(BasicSocket chatSocket) {
		this.chatSocket = chatSocket;
	}

	public Map<String, BasicSocket> getMediaSendSocketMap() {
		return mediaSendSocketMap;
	}

	public void setMediaSendSocketMap(Map<String, BasicSocket> mediaSendSocketMap) {
		this.mediaSendSocketMap = mediaSendSocketMap;
	}

	public Map<String, BasicSocket> getMediaReceiveSocketMap() {
		return mediaReceiveSocketMap;
	}

	public void setMediaReceiveSocketMap(Map<String, BasicSocket> mediaReceiveSocketMap) {
		this.mediaReceiveSocketMap = mediaReceiveSocketMap;
	}
	
}
