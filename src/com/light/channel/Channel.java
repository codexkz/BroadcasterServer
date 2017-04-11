package com.light.channel;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.light.bean.Member;
import com.light.remoteController.RemoteController;
import com.light.socket.BasicSocket;

public class Channel {

	 //private TransationLock 			memberMapLock                       ;
	 private Map < String , Member >  	memberMap                           ;
	 private Map < String , String >  	chatMessageMap   	  				;
	 private ChatMessageCounter 	    chatMessageCounter   	  		    ;
	 private String 	  				channelID       					;
	 private String 	  				channelPassword 					;
	 private RemoteController    		controller	 	  					;
	 
	 public Channel( String channelID , String channelPassword ){
		 this.memberMap 		= Collections.synchronizedMap(new LinkedHashMap<String,Member>()) ;
		 this.chatMessageCounter= new  ChatMessageCounter();
		 this.chatMessageMap 	= Collections.synchronizedMap(new LinkedHashMap<String,String>()) ;
		 this.channelID			= channelID ;
		 this.channelPassword	= channelPassword ;
		 this.controller		= new RemoteController(this) ;
	 }

	 public class  ChatMessageCounter {
		 private Object   lock   = new Object()  ;
		 private Integer  count  = 0  ;
		 public Object getLock(){ return this.lock ; };
		 public Integer add(){ return this.count++ ; };
	 }
	 
	 public Integer getMemberCount(){
		 return memberMap.size();
	 }
	 
	 public synchronized Member findMemberByName(String memberName){
		 for(String memberUUID :  memberMap.keySet())
			  if (memberMap.get(memberUUID).getMemberName().equals(memberName))
				  return  memberMap.get(memberUUID);
		  return null;
	 }
	 
	 public synchronized Member findMemberByName(String memberName ,Callback callback){
		  for(String memberUUID :  memberMap.keySet())
			  if (memberMap.get(memberUUID).getMemberName().equals(memberName))
				  return  callback.findMemberByNameCallback(memberMap.get(memberUUID));
		  return callback.findMemberByNameCallback(null);
	 }
	 
	 public interface Callback{
		  public Member findMemberByNameCallback(Member result);
	 }
	 
	 public Boolean hasMember(String memberUUID){
		  return memberMap.containsKey(memberUUID);
	 }
	 
	 public Member findMember(String memberUUID){
		  return memberMap.get(memberUUID);
	 }
	 
	 public Member putMember(Member member){
			memberMap.put(member.getMemberUUID(), member);
			return member ;
	}
	 
	 
	 public Member removeMember(String memberUUID) {
		    return memberMap.remove(memberUUID);
	 }
	 
	 public Channel sendInfoToAllMemberInChannel(String info){
		 for(String memberUUID :  memberMap.keySet()){
			try {
				BasicSocket socket = memberMap.get(memberUUID).getChatSocket();
				if(socket != null && socket.getSession().isOpen()) 
					socket.getSession().getBasicRemote().sendText(info);
			} catch (IOException e) {
				e.printStackTrace();
			} 
		 }
		 return this;
	 }
	 
	 public synchronized JsonArray getMemberArray(){
		 JsonArray jsonArray = new JsonArray();
		 for( String memberUUID : this.getMemberMap().keySet() ){
			JsonObject memberJson = new JsonObject();
			memberJson.addProperty("name", this.getMemberMap().get(memberUUID).getMemberName());
			jsonArray.add(memberJson);
		 }
		 return jsonArray;
	 }
	 
	 /*getter & setter*/
	 
	 
	 public Map<String,Member> getMemberMap() {
		return memberMap;
	 }
	
	 public void setMemberMap(LinkedHashMap<String,Member> memberMap) {
		this.memberMap = memberMap;
	 }
	 
	 public ChatMessageCounter getChatMessageCounter() {
		return chatMessageCounter;
	 }

	public void setChatMessageCounter(ChatMessageCounter chatMessageCounter) {
		this.chatMessageCounter = chatMessageCounter;
	 }
	 
	 public Map<String, String> getChatMessageMap() {
		return chatMessageMap;
	 }


	 public void setChatMessageMap(Map<String, String> chatMessageMap) {
		this.chatMessageMap = chatMessageMap;
	 }
	 
	 
	 public String getChannelID() {
		return channelID;
	 }
	
	 public void setChannelID(String channelID) {
		this.channelID = channelID;
	 }
	
	 public String getChannelPassword() {
		return channelPassword;
	 }
	
	 public void setChannelPassword(String channelPassword) {
		this.channelPassword = channelPassword;
	 }
	
	 public RemoteController getControler() {
		return controller;
	 }
	
	 public void setControler(RemoteController controller) {
		this.controller = controller;
	 }
}

 
