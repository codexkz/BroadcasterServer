package com.light.socket;

import java.util.Map;

import javax.websocket.server.ServerEndpoint;

import com.light.bean.Member;

@ServerEndpoint("/MediaSendSocket/{uuid}/{channel}/{password}")
public class MediaSendSocket extends BasicSocket {

	@Override
	public void addSocketToMember(Member member, BasicSocket socket) {
		member.getMediaSendSocketMap().put(socket.getSession().getId(), socket);
	}

	@Override
	public void removeSocketFromMember(Member member, BasicSocket socket) {
		member.getMediaSendSocketMap().remove(socket.getSession().getId(), socket);
	}
}