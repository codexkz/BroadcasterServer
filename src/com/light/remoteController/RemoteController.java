<<<<<<< HEAD
package com.light.remoteController;

import java.util.Queue;

import com.google.gson.JsonObject;
import com.light.Exception.OperationException;
import com.light.bean.Member;
import com.light.channel.Channel;
import com.light.remoteController.event.ActionEvent;
import com.light.remoteController.event.OpenMediaSocketEvent;
import com.light.remoteController.event.RenameEvent;


/*
 * 
 *  當前端以ControlerSocket 送 control request 上來後
 *  由RemoteController , 來進行事件的產生與分發執行
 *  若事件  isDependencyAction  則放進 eventQueue
 *  若事件 !isDependencyAction  則直接執行
 * 
 * */

public class RemoteController {
	Queue<ActionEvent>   eventQueue ; //for dependency action
	Channel channel ; 
	public RemoteController(Channel Channel){
		this.channel = Channel;
	}
	
	public RemoteController doAction(String action, Member member, JsonObject data) throws OperationException{
		ActionEvent event = createActionEvent ( action , member , data );
		if(event.isDependencyAction()) eventQueue.add(event); //TODO evenQueue implement
		else event.handdle();
		return this;
	}
	
	
	private ActionEvent createActionEvent( String action , Member member ,JsonObject data) throws OperationException{
		switch(action){
			case "reName":
				return new RenameEvent(member , data);
			case "openMediaSocket":
				return new OpenMediaSocketEvent(member , data);
			default:
				throw new OperationException("IlligalAction");
		}
	}
}
=======
package com.light.remoteController;

import java.util.Queue;

import com.google.gson.JsonObject;
import com.light.Exception.OperationException;
import com.light.bean.Member;
import com.light.channel.Channel;
import com.light.remoteController.event.ActionEvent;
import com.light.remoteController.event.RenameEvent;

public class RemoteController {
	Queue   eventQueue ; //for dependency action
	Channel channel ; 
	public RemoteController(Channel Channel){
		this.channel = Channel;
	}
	

	public RemoteController doAction(String action, Member member, JsonObject data) throws OperationException{
		ActionEvent event = createActionEvent ( action , member , data );
		//if(e.isDependencyAction()) eventQueue.put(event);
		//else 
		event.handdle();
		return this;
		
	}
	
	
	private ActionEvent createActionEvent( String action , Member member ,JsonObject data) throws OperationException{
		switch(action){
			case "reName":
				return new RenameEvent(member , data);
			default:
				throw new OperationException("IlligalAction");
		}
	}
}
>>>>>>> remote/master
