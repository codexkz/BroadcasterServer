package com.light.servlet;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.light.Exception.OperationException;
import com.light.bean.JsonMessage;
import com.light.bean.Member;
import com.light.channel.Channel;
import com.light.channel.ChannelManager;
import com.light.channel.ChannelManagerFactory;

/**
 * Servlet implementation class ConectorServlet
 */
@WebServlet("/Connect.do")
public class ConectorServlet extends HttpServlet implements HttpSessionListener {
	
	private static final long serialVersionUID = 1L;
	ChannelManager channelManager = ChannelManagerFactory.getChannelManagerInstance();
       
    public ConectorServlet() {
        super();
    }

    /*
	 * For  HttpServlet
	 * 
	 * */
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		InputStream inputStream = request.getInputStream();
//		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream , StandardCharsets.UTF_8));
		
		/* init parameter  */
		String requerstCharset = StandardCharsets.UTF_8.toString();
		//response.setCharacterEncoding(requerstCharset);
		HttpSession rqUser       	= request.getSession();
		JsonObject  rqJson  		= new JsonParser().parse(request.getReader().readLine()).getAsJsonObject();
		String  rqChannelID 	   	= java.net.URLEncoder.encode( rqJson.get("id").getAsString() , requerstCharset);
		String  rqChannelPassword 	= java.net.URLEncoder.encode( rqJson.get("pw").getAsString() , requerstCharset);
		System.out.println("rqChannelID : "+rqChannelID +" , rqChannelPassword : "+rqChannelPassword);
		
		try{
			
			/* verification  */
			Channel rqChannel = verificationChannel(rqChannelID , rqChannelPassword) ;
			Member  rqMember  = verificationMember (rqUser);
			if(rqMember.getMemberName() == null ) rqMember.setMemberName( "Visitor" + (rqChannel.getMemberCount() + 1 ) );
			rqChannel.putMember(rqMember);
			rqUser.setAttribute("member" , rqMember  );
			rqUser.setAttribute("channel", rqChannel );
			
			
			/* create response json  */
			JsonObject rsJson = new JsonObject();
					   rsJson.addProperty( "uuid" 	, rqMember.getMemberUUID());
					   rsJson.addProperty( "name" 	, rqMember.getMemberName());
					   rsJson.add( "members" 	, rqChannel.getMemberArray());
			response.getWriter().append(JsonMessage.createJsonMessage()
											.set("action"	 , "connectSuccessInit")
											.set("actionType", "channelInfo")
											.set("data" 	 , rsJson)
											.toString());
			
			
			/* notify all member in channel that new one join us */
			rqChannel.sendInfoToAllMemberInChannel(JsonMessage.createJsonMessage()
													.set("action"	 , "memberIn")
													.set("actionType", "channelInfo")
													.set("data" 	 , rqMember.getMemberName())
													.toString());
			
		}catch(OperationException e ){
			JsonObject rsJson = new JsonObject();
			rsJson.addProperty("retcode", e.getRetCode());
			rsJson.addProperty("retmsg" , e.getRetMsg());
			response.getWriter().append(rsJson.toString());
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	/*
	 * For  Verification
	 * 
	 * */
	
	private synchronized Channel verificationChannel(String channelID ,String channelPassword) throws OperationException{
		Channel channel = channelManager.findChannel(channelID ) ;
		if( channel == null ) channel = channelManager.putChannel(channelID, new Channel( channelID , channelPassword ));
		System.out.println(channel.getChannelPassword() +"/" + channelPassword);
		if(!channel.getChannelPassword().equals(channelPassword) ) throw new OperationException("ChannelVerificationFail") ;
		return channel ;
	}

	private synchronized Member verificationMember(HttpSession rqUser) throws OperationException  {
		Member member = (Member) rqUser.getAttribute("member");
		if(member == null) member = new Member(rqUser.getId()) ;
		if(!member.getMemberUUID().equals(rqUser.getId())) throw new OperationException("MemberVerificationFail") ;
		return member ;
	}
	
	/*
	 * For  Session Control
	 * 
	 * */
	
	@Override
	public void sessionCreated(HttpSessionEvent event) {
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		Member  userMemeber = (Member) event.getSession().getAttribute("member")   ;
		Channel userChannel = (Channel) event.getSession().getAttribute("channel") ;
		userChannel.removeMember(userMemeber.getMemberUUID());
		
		/* notify all member in channel that  one leave us */
		userChannel.sendInfoToAllMemberInChannel(JsonMessage.createJsonMessage()
												.set("action"	 ,"memberOut")
												.set("actiontype", "channelInfo")
												.set("data" 	 , userMemeber.getMemberName())
												.toString());
	}

}
