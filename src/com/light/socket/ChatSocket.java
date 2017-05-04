package com.light.socket;



import javax.websocket.server.ServerEndpoint;
import com.google.gson.JsonObject;
import com.light.Exception.OperationException;
import com.light.bean.Member;

@ServerEndpoint(value ="/ChatSocket/{uuid}/{channel}/{password}")
public class ChatSocket extends BasicSocket {
	
	private static final long serialVersionUID = 1L;
	
	public BasicSocket chooseSocket(Member member){
		return member.getChatSocket();
	};
	
	@Override
	public void doBeforeSendMessageToAllMember(JsonObject rqJson) {
		try{
			switch(rqJson.get("messageBody").getAsJsonObject().get("actionType").getAsString()){
			case "insert":
				messageInsert(rqJson);
				break;
			case "modify":
				messageModify(rqJson);
				break;
			case "delete":
				messageDelete(rqJson);
				break;
			}
			
			sendMessageToAllMember(rqJson);
		}catch(OperationException e ){
			e.printStackTrace();
		}
	}
	
	@Override
	public void doBeforeSendMessageToOneMember(JsonObject rqJson,String target){
		sendMessageToOneMember(rqJson, target);
	}
	
	public void messageInsert(JsonObject rqJson){
		synchronized(channel.getChatMessageCounter().getLock()){
			Integer msgid= channel.getChatMessageCounter().add() ;
			channel.getChatMessageMap().put( msgid.toString() , rqJson.get("userUUID").getAsString());
			rqJson.get("messageBody").getAsJsonObject().addProperty("messageID", msgid.toString());
		}
	}
	
	public void messageModify(JsonObject rqJson) throws OperationException{
		synchronized(channel.getChatMessageCounter().getLock()){
			String msgid = rqJson.get("messageBody").getAsJsonObject().get("messageID").getAsString();
			if(channel.getChatMessageMap().get(msgid).equals(rqJson.get("userUUID").getAsString())) return;
			throw new OperationException("IlligalUserControl");
		}
	}
	
	public String messageDelete(JsonObject rqJson) throws OperationException{
		synchronized(channel.getChatMessageCounter().getLock()){
			String msgid = rqJson.get("messageBody").getAsJsonObject().get("messageID").getAsString();
			if(channel.getChatMessageMap().get(msgid).equals(rqJson.get("userUUID").getAsString()))
				return channel.getChatMessageMap().remove(msgid);
			throw new OperationException("IlligalUserControl");
		}
	}
	
	
	
	@Override
	public void addSocketToMember(Member member, BasicSocket socket) {
		member.setChatSocket(socket);
	}
	
}


