package com.light.Exception;

public class OperationException extends Exception{

	private static final long serialVersionUID = 1L;
	private String retCode = "";
	private String retMsg  = "";
	
	static String ChannelVerificationFailCode = "901";
	static String ChannelVerificationFailMsg  = "ChannelVerificationFail";
	
	static String MemberVerificationFailCode  = "902";
	static String MemberVerificationFailMsg   = "MemberVerificationFail";
	
	static String IlligalActionCode  = "903";
	static String IlligalActionMsg   = "IlligalAction";
	
	static String IlligalDataCode  = "904";
	static String IlligalDataMsg   = "IlligalData";
	
	static String IlligalUserControlCode  = "905";
	static String IlligalUserControlMsg   = "IlligalUserControl";
	
	
	public OperationException(String reason ){
		switch(reason){
			case "ChannelVerificationFail":
				retCode = ChannelVerificationFailCode;
				retMsg  = ChannelVerificationFailMsg;
				break ;
			case "MemberVerificationFail":
				retCode = MemberVerificationFailCode;
				retMsg  = MemberVerificationFailMsg;
				break ;
			case "IlligalAction":
				retCode = IlligalActionCode;
				retMsg  = IlligalActionMsg;
				break ;
			case "IlligalData":
				retCode = IlligalDataCode;
				retMsg  = IlligalDataMsg ;
				break ;
			case "IlligalUserControl":
				retCode = IlligalUserControlCode;
				retMsg  = IlligalUserControlMsg ;
				break;
		}	
	}
	
	
	
	
	
	public String getRetCode() {
		return retCode;
	}

	public OperationException setRetCode(String retCode) {
		this.retCode = retCode;
		return this ;
	}

	public String getRetMsg() {
		return retMsg;
	}

	public OperationException setRetMsg(String retMsg) {
		this.retMsg = retMsg;
		return this ;
	}
}


