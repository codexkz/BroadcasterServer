package com.light.socket;



import javax.websocket.server.ServerEndpoint;
import com.light.bean.Member;

@ServerEndpoint("/MediaSocket/{uuid}/{channel}/{password}/{mediasockettype}/{mediasockpairid}")
public class MediaSocket extends BasicSocket {
	
	private static final long serialVersionUID = 1L;
	
	MediaSocketPair mediasockpair ;
	
	@Override
	public void doAfterStart(){
		String mediasockettype = this.getSession().getPathParameters().get("mediasockettype");
		String mediasockpairid = this.getSession().getPathParameters().get("mediasockpairid");
		mediasockpair = this.channel.getMediaSocketPairMap().get(mediasockpairid);
		if("sender".equals(mediasockettype)){
			  mediasockpair.sender = this ;
			  return;
		} 
		if("receiver".equals(mediasockettype)){
			  mediasockpair.receiverList.add(this);
			  return;
		}
	}
	
	@Override
	public void doAfterEnd(){
		String mediasockettype = this.getSession().getPathParameters().get("mediasockettype");
		String mediasockpairid = this.getSession().getPathParameters().get("mediasockpairid");
		
		mediasockpair = this.channel.getMediaSocketPairMap().get(mediasockpairid);
		if("sender".equals(mediasockettype)){
			mediasockpair.sender = null ;
			this.channel.getMediaSocketPairMap().remove(mediasockpairid);
			return ;
		}   
		if("receiver".equals(mediasockettype)){
			mediasockpair.receiverList.remove(this);
			this.channel.getMediaSocketPairMap().remove(mediasockpairid);
			return ;
		}
	}
	
	
//	@OnMessage
//	public void onMessage(Session userSession, String jsonString ) {
//		JsonObject jsonMessage   = jsonParser.parse(jsonString).getAsJsonObject();
//		if(!jsonMessage.get("userUUID").getAsString().equals(member.getMemberUUID())) return;
//		
//		String 	   target  		 = jsonMessage.get("messageBody").getAsJsonObject().get("target").getAsString();
//		if("all".equals(target)) doBeforeSendMessageToAllMember(jsonMessage); 
//		else 					 doBeforeSendMessageToOneMember(jsonMessage,target);
//	}
//	
//	public void sendMessageToAllMember(JsonObject rsJson){
//		try{
//			rsJson.addProperty("userUUID", "");
//			System.out.println(rsJson.toString());
//			Map<String, Member>  memberMap =   channel.getMemberMap();
//			for( String  memberUUID  : memberMap.keySet() ){
//				Session session= chooseSocket(memberMap.get(memberUUID)).getSession();
//				if (session.isOpen()) session.getBasicRemote().sendText(rsJson.toString());
//			}
//		}catch(IOException e){
//			e.printStackTrace();
//		}
//	};
//	
//	public void sendMessageToOneMember(JsonObject rsJson,String target){
//		try{
//			rsJson.addProperty("userUUID", "");
//			System.out.println(rsJson.toString());
//			Member targetMember = channel.findMemberByName(target);
//			if(targetMember != null){
//				Session session= chooseSocket(targetMember).getSession();
//				if (session.isOpen()) session.getBasicRemote().sendText(rsJson.toString());
//			}
//		}catch(IOException e){
//			e.printStackTrace();
//		}
//	};
//	
//	@Override
//	public BasicSocket chooseSocket(Member member){
//		return member.getMediaReceiveSocketMap();
//	};
	
	
	
	
	
	@Override
	public void addSocketToMember(Member member, BasicSocket socket) {
		String mediasockettype = this.getSession().getPathParameters().get("mediasockettype");
		 
		if("sender".equals(mediasockettype))
			member.getMediaSendSocketMap().put(socket.getSession().getId(), socket);
		if("receiver".equals(mediasockettype))
			member.getMediaReceiveSocketMap().put(socket.getSession().getId(), socket);
	}

	@Override
	public void removeSocketFromMember(Member member, BasicSocket socket) {
		String mediasockettype = this.getSession().getPathParameters().get("mediasockettype");
		
		if("sender".equals(mediasockettype))
			member.getMediaSendSocketMap().remove(socket.getSession().getId(), socket);
		if("receiver".equals(mediasockettype))
			member.getMediaReceiveSocketMap().remove(socket.getSession().getId(), socket);
	}

}