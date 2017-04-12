package com.light.socket;

import java.io.IOException;
import javax.websocket.server.ServerEndpoint;
import com.google.gson.JsonObject;
import com.light.Exception.OperationException;
import com.light.bean.Member;

@ServerEndpoint( value ="/ControlerSockect/{uuid}/{channel}/{password}")
public class ControlerSockect extends BasicSocket {
	
	private static final long serialVersionUID = 1L;
	String action ;
	Object data   ;
	
	@Override
	public void doBeforeSendMessageToAllMember(JsonObject rsJson) {
		try{try{
			action = rsJson.get("messageBody").getAsJsonObject().get("action").getAsString();
			channel.getControler().doAction( action , member , rsJson);
			sendMessageToAllMember(rsJson);
			
		}catch(OperationException e ){
			
			JsonObject rsExceptionJson = new JsonObject();
			rsExceptionJson.addProperty("retcode", e.getRetCode());
			rsExceptionJson.addProperty("retmsg" , e.getRetMsg());
			session.getBasicRemote().sendText(rsJson.toString());
			
		}}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void doBeforeSendMessageToOneMember(JsonObject rsJson,String target){
		try{try{
			action = rsJson.get("messageBody").getAsJsonObject().get("action").getAsString();
			channel.getControler().doAction( action , member , rsJson);
			sendMessageToOneMember(rsJson, target);
			
		}catch(OperationException e ){
			
			JsonObject rsExceptionJson = new JsonObject();
			rsExceptionJson.addProperty("retcode", e.getRetCode());
			rsExceptionJson.addProperty("retmsg" , e.getRetMsg());
			session.getBasicRemote().sendText(rsJson.toString());
			
		}}catch(IOException e){
			e.printStackTrace();
		}
	}

	@Override
	public BasicSocket chooseSocket(Member member){
		return member.getControlerSocket();
	};
	
	
	@Override
	public void addSocketToMember(Member member,BasicSocket socket) {
		member.setControlerSocket(socket);
	}
	
	
}


