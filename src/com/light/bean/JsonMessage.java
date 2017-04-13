package com.light.bean;
import java.io.Serializable;
import java.util.Date;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonMessage implements Serializable, Cloneable{
	 
	 private static final long serialVersionUID = 1L;
	 private JsonObject jsonMessageHeader;
	 private JsonObject jsonMessageBody;
	 
	 public JsonMessage(){
		 jsonMessageBody = new JsonObject();
		 jsonMessageBody.addProperty("action" 		, "");
		 jsonMessageBody.addProperty("actionType"	, "");
		 jsonMessageBody.addProperty("target"		, "");
		 jsonMessageBody.addProperty("timestamp" 	, "");
		 jsonMessageBody.addProperty("messagetype"	, "");
		 jsonMessageBody.addProperty("message" 		, "");
		 jsonMessageBody.addProperty("data" 		, "");
			
		 jsonMessageHeader = new JsonObject();
		 jsonMessageHeader.addProperty("retcode" 			, "100");
		 jsonMessageHeader.addProperty("userName" 			, "");
		 jsonMessageHeader.addProperty("userName" 			, "");
		 jsonMessageHeader.addProperty("channelID"			, "");
		 jsonMessageHeader.addProperty("channelPassword"	, "");
		 jsonMessageHeader.addProperty("connectMode" 		, "");
		 jsonMessageHeader.add		  ("messageBody"		, jsonMessageBody);
	 }
	 
	 public JsonObject getJsonMessageHeader(){
		 return this.jsonMessageHeader;
	 }
	 
	 public JsonObject getJsonMessageBody(){
		 return this.jsonMessageBody;
	 }
	 
	 public JsonMessage set(String property, Object value){
		 if( !this.getJsonMessageBody().has(property) ) return this;
		 if( value instanceof JsonElement) this.getJsonMessageBody().add(property, (JsonElement) value);
		 else  							   this.getJsonMessageBody().addProperty(property, (String) value);
		 return this;
	 }
	 
	 @Override
	 public String toString(){
		 JsonObject jsonObj = this.getJsonMessageHeader();
				 	jsonObj.addProperty("timestamp", new Date().getTime());
		 return jsonObj.toString();
	 }
	 
	 
	 static private JsonMessage jsonMessage ;

	 static{
		 jsonMessage = new JsonMessage();
	 }
	
	 static public JsonMessage createJsonMessage(){
		try {
			JsonMessage jsonMsg= ((JsonMessage) jsonMessage.clone());
			return jsonMsg;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	 }
	 
}

