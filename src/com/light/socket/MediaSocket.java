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
	private  final static  int BINARY_MESSAGE_BUFFER_SIZE = 300 * 1024 * 1024;  // 300 MB
	
	MediaSocketPair mediasockpair ;
	
	@Override
	public void doAfterStart(){
		this.session.setMaxBinaryMessageBufferSize(BINARY_MESSAGE_BUFFER_SIZE);
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
	public void onMessage(Session userSession, byte[]  data ) {
		try{
			System.out.println(this.member.getMemberUUID()+" is sending ByteBuffer : "+data.length);
			mediasockpair.sender.session.getBasicRemote().sendBinary(ByteBuffer.wrap(data));
			for(MediaSocket socket : mediasockpair.getReceiverList() ){
				if (socket.session.isOpen())  socket.session.getBasicRemote().sendBinary(ByteBuffer.wrap(data));
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