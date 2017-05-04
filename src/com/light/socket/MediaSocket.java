package com.light.socket;



import java.io.IOException;
import java.nio.ByteBuffer;

import javax.websocket.OnMessage;
import javax.websocket.Session;
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
	
	
	@OnMessage
	public void onMessage(Session userSession, ByteBuffer data ) {
		try{
			for(MediaSocket socket : mediasockpair.getReceiverList() ){
				if (socket.session.isOpen())  socket.session.getBasicRemote().sendBinary(data);
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	
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