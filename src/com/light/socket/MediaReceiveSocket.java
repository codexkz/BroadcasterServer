package com.light.socket;

import java.util.Map;

import javax.websocket.server.ServerEndpoint;

import com.light.bean.Member;

@ServerEndpoint("/MediaReceiveSocket/{uuid}/{channel}/{password}")
public class MediaReceiveSocket extends BasicSocket {
	
	private static final long serialVersionUID = 1L;

	@Override
	public void addSocketToMember(Member member, BasicSocket socket) {
		member.getMediaReceiveSocketMap().put(socket.getSession().getId(), socket);
		
	}

	@Override
	public void removeSocketFromMember(Member member, BasicSocket socket) {
		member.getMediaReceiveSocketMap().remove(socket.getSession().getId(), socket);
		
	}

}