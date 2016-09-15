package com.akuacom.pss2.itron.wsclient;


import javax.xml.rpc.Stub;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.configuration.FileProvider;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.handler.WSHandlerConstants;
import org.apache.ws.security.message.token.UsernameToken;

import com.akuacom.pss2.itron.webservices.CurtailmentOperationsLocator;
import com.akuacom.pss2.itron.webservices.CurtailmentOperationsSoap;
import com.akuacom.pss2.itron.webservices.CurtailmentOperationsSoapStub;
import com.akuacom.pss2.itron.webservices.EventNoticeResponse;
import com.akuacom.pss2.itron.webservices.EventNoticeResponseBlock;
import com.akuacom.pss2.itron.webservices.EventNoticeResponseList;
import com.akuacom.pss2.itron.webservices.Message;
import com.akuacom.pss2.itron.webservices.MessageDataItem;
import com.akuacom.pss2.itron.webservices.RespondToEventNoticesResult;
import com.akuacom.pss2.itron.webservices.ResponseAction;


public class ItronTestClient 
{

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		try
		{
			EngineConfiguration config = new FileProvider("../resource/pss2/ItronWS.wsdd");
			CurtailmentOperationsLocator locator = 
				new CurtailmentOperationsLocator(config);
			Stub stub = (Stub) locator.getPort(CurtailmentOperationsSoap.class);
			stub._setProperty(UsernameToken.PASSWORD_TYPE, WSConstants.PASSWORD_TEXT);
			stub._setProperty(WSHandlerConstants.USER, "pgeauto-dr@gepllc.com");
			stub._setProperty(WSHandlerConstants.PW_CALLBACK_CLASS,"com.akuacom.pss2.itron.wsclient.PWCallback");
			stub._setProperty(WSHandlerConstants.MUST_UNDERSTAND, "false");
			CurtailmentOperationsSoapStub itronStub =
						(CurtailmentOperationsSoapStub)stub;
			
			//now create response list
			EventNoticeResponseList list = makeResponseList();
			
			//call method
			RespondToEventNoticesResult result = itronStub.respondToEventNotices(list);
			//print result
			System.out.println("RespondToEventNoticesResult: "+result.getResultCode());
			Message[] messages = result.getMessages();
			for ( int j=0; j < messages.length; j++)
			{
				System.out.println("message="+messages[j].getText());
				MessageDataItem[] items = messages[j].getMessageData();
				if (items != null)
				for (int k=0; k < items.length; k++)
					System.out.println("message: "+items[k].getName()+":"+items[k].getValue());
			}
		}
	    catch (Throwable t) 
	    {
	        t.printStackTrace();
	    }
	}

	//hardcoded for now
	private static String transactionID = "AKUA090607";
	private static int curtailmentEventID = 4220;
	
	private static EventNoticeResponseList makeResponseList ()
	{
		EventNoticeResponseList l = new EventNoticeResponseList();
		l.setTransactionID(transactionID);
		
		EventNoticeResponse resp = new EventNoticeResponse();
		resp.setCurtailmentEventID(curtailmentEventID);
		resp.setAccountID("1234567890_GEP_LBNL_DBP_TEST_ACCOUNT");
		resp.setResponseAction(ResponseAction.SubmitBid);
		
			//now create blocks
		int numBlocks = 2;
		double[] kws = new double[]{0.5,0.5,0.7};
		int[] hour = new int[]{18,19};
		EventNoticeResponseBlock[] blocks = new EventNoticeResponseBlock[numBlocks];
		for (int i=0; i < numBlocks; i++)
		{
			blocks[i]= new EventNoticeResponseBlock();
			blocks[i].setHour(new Integer(hour[i]));
			blocks[i].setCommittedReduction(kws[i]);
			blocks[i].setBlockAccepted(true); //for now
		}
		resp.setUseDefaultReductionForAllBlocks(false);
		resp.setResponseBlocks(blocks);
			//need array of response
		EventNoticeResponse[] resps = new EventNoticeResponse[1];
		resps[0] = resp;
		l.setEventNoticeResponses(resps);
		return l;
	}
}
