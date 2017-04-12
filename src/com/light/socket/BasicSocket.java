package com.light.socket;
import java.io.*;
import java.util.Map;

import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.light.bean.Member;
import com.light.channel.Channel;
import com.light.channel.ChannelManager;
import com.light.channel.ChannelManagerFactory;

import javax.websocket.Session;
import javax.websocket.OnOpen;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnClose;
import javax.websocket.CloseReason;

@ServerEndpoint(value ="/BasicSocket")
public class BasicSocket implements Serializable{
	
	private static final long serialVersionUID = 1L;
	protected ChannelManager channelManager = ChannelManagerFactory.getChannelManagerInstance();
	protected JsonParser  	 jsonParser     = new JsonParser();
	protected Channel 	   	 channel		;
	protected Member 	   	 member			;
	protected Session 	   	 session		;
	
	
	/* lifecycle method */
	@OnOpen
	public void start(  @PathParam("uuid") 		String memberUUID,
						@PathParam("channel")  	String channelID,
		       		   	@PathParam("password") 	String channelPassword,
		       		   							Session userSession) throws IOException {
		System.out.println(channelID);
		if(channelManager.verificationConnect(channelID,channelPassword,memberUUID)){
			channel = channelManager.findChannel(channelID) ;
			member  = channel.findMember(memberUUID) ;
			session = userSession ;
			addSocketToMember( member ,this);
			doAfterStart();
			System.out.println(memberUUID + ": success connect");
		    return ;
		}
		
		//close connect
		end(memberUUID, userSession, new CloseReason( CloseReason.CloseCodes.CANNOT_ACCEPT, "VerificationError"));
		return ;
	}
	
	public void doAfterStart() {}
	

	@OnError
	public void error( @PathParam("uuid") 	String memberUUID,
						 					Session userSession, 
						 					Throwable e){
		e.printStackTrace();
	}
	
	@OnClose
	public void end(@PathParam("uuid") 		String memberUUID,
											Session userSession, 
											CloseReason reason) {
		try {
			if(channel!=null) removeSocketFromMember(channel.findMember(memberUUID),this);
			userSession.close(reason);
			doAfterEnd();
			System.out.println(memberUUID + ": Disconnected: " + reason.toString());
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}
	
	public void doAfterEnd() {}

	/* onMessage working flow  */
	@OnMessage
	public void onMessage(Session userSession, String jsonString ) {
		JsonObject jsonMessage   = jsonParser.parse(jsonString).getAsJsonObject();
		if(!jsonMessage.get("userUUID").getAsString().equals(member.getMemberUUID())) return;
		
		String 	   target  		 = jsonMessage.get("messageBody").getAsJsonObject().get("target").getAsString();
		if("all".equals(target)) doBeforeSendMessageToAllMember(jsonMessage); 
		else 					 doBeforeSendMessageToOneMember(jsonMessage,target);
	}
	
	public void doBeforeSendMessageToAllMember(JsonObject rsJson){
		sendMessageToAllMember(rsJson);
	}
	
	public void doBeforeSendMessageToOneMember(JsonObject rsJson,String target){
		sendMessageToOneMember(rsJson, target);
	}
	
	public void sendMessageToAllMember(JsonObject rsJson){
		try{
			rsJson.addProperty("userUUID", "");
			System.out.println(rsJson.toString());
			Map<String, Member>  memberMap =   channel.getMemberMap();
			for( String  memberUUID  : memberMap.keySet() ){
				Session session= chooseSocket(memberMap.get(memberUUID)).getSession();
				if (session.isOpen()) session.getBasicRemote().sendText(rsJson.toString());
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	};
	
	public void sendMessageToOneMember(JsonObject rsJson,String target){
		try{
			rsJson.addProperty("userUUID", "");
			System.out.println(rsJson.toString());
			Member targetMember = channel.findMemberByName(target);
			if(targetMember != null){
				Session session= chooseSocket(targetMember).getSession();
				if (session.isOpen()) session.getBasicRemote().sendText(rsJson.toString());
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	};
	
	public BasicSocket chooseSocket(Member member){
		return member.getChatSocket();
	};
	
	
	/* add & remove socket to member */
	public  void addSocketToMember(Member member,BasicSocket socket){};
	public  void removeSocketFromMember(Member member,BasicSocket socket){};
	
	
	/* getter & setter  */
	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}
	
}
