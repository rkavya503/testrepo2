/**
 * 
 */
package com.akuacom.pss2.apx.servlet;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.akuacom.ejb.client.EJB3Factory;
import com.akuacom.pss2.apx.common.ApxInputServletHelper;
import com.akuacom.pss2.cache.EventStateCacheHelper;
import com.akuacom.pss2.data.usage.calcimpl.ImplFactory;
import com.akuacom.pss2.program.apx.APXManager;
import com.akuacom.pss2.program.apx.parser.APXXmlParser;
import com.akuacom.pss2.program.sceftp.MessageUtil;
import com.akuacom.pss2.system.SystemManager;

/**
 *
 */
public class APXInputServlet extends HttpServlet {

	private static final long serialVersionUID = 8411906624632123044L;
	
	private static final Logger log = Logger.getLogger(APXInputServlet.class);

	/*@Resource(mappedName = "queue/apxDispatchQueue")
	private Queue apxMessageQueue;
	@Resource(mappedName = "java:/JmsXA")
	private ConnectionFactory messageQueueFactory;*/
    
    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void doPut(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
        final ServletInputStream inputStream = request.getInputStream();
        File file=ApxInputServletHelper.saveTempFile(inputStream);
        
        int errors=0;
        String errMessage="";
        try {
        	SystemManager systemManager = EJB3Factory.getBean(SystemManager.class);
        	EventStateCacheHelper cache = EventStateCacheHelper.getInstance();
        	String utilityName = cache.getUtilityName("utilityName");
			APXManager apxManager=ImplFactory.instance().getAPXManager(utilityName);
	    	APXXmlParser parser=new APXXmlParser(file);
	    	apxManager.process(parser); // validate only
			SimpleDateFormat format=new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			log.info("::::: [START] APX Upload : Received an Event Upload File. On Date: "+ format.format(new Date()));
			log.info("::::: Event Details - EVENT Name: "+parser.getEventName()+" | PRGM Name: "+parser.getProgramName()+" | START Time: "+ parser.getEventStartTime() + " | END Time: "+parser.getEventEndTime());
			log.info("::::: [END] APX Upload");
			
            //apxMessageDispatch(parser);           
        }catch(Exception e){
        	errors=1;
        	errMessage=MessageUtil.getErrorMessage(e);
        	log.error(e);
        }
        sendResponse(request, response, errors, errMessage);
    }

    protected void sendResponse(HttpServletRequest request,
            HttpServletResponse response, int errors, String errMessage) throws ServletException, IOException {
    	final ServletOutputStream out = response.getOutputStream();
		/*StringBuilder builder=new StringBuilder();
		builder.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n");
		builder.append("<Response>\n    <Errors>");
		builder.append(errors);
		builder.append("</Errors>\n");
		if (errors>0) {
			builder.append("    <ErrorDescription>");
			builder.append(errMessage);
			builder.append("</ErrorDescription>\n");
			
		}
		builder.append("</Response>");*/
    	String responseMsg = ApxInputServletHelper.getApxResponseMessage(errors, errMessage);
		out.println(responseMsg);
		out.flush();
	}
    
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try{
			Thread.sleep(1000);	
			doPut(request, response);
		}catch(Exception ee){
	 	}
	}

   /* private File saveTempFile(InputStream in) {
		File result = null;
		Configuration conf=new Configuration();
		String tempFolder = conf.getLogPath();
		try {
			SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd_HHmmss");
			String filename="APX_interface_request_"+format.format(new Date())+".xml";
			result = new File(tempFolder, filename);
//			result = File.createTempFile(prefix, ".xml", new File(tempFolder));

			BufferedWriter w1 = new BufferedWriter(new FileWriter(result));
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = reader.readLine()) != null) {
				w1.write(line);
				w1.newLine();
			}
			reader.close();
			w1.close();
			log.debug("APX request is written to " + result.getName());
		} catch (IOException e) {
			log.warn("Failed to create temp file for sce apx request", e);
		}
		return result;
	}*/
    
	/*private void apxMessageDispatch(APXXmlParser loadedParser) {
			Connection connection = null;
			Session session = null;
			try {
				connection = messageQueueFactory.createConnection();
				session = connection.createSession(false,
						Session.AUTO_ACKNOWLEDGE);
				MessageProducer messageProducer = session.createProducer(apxMessageQueue);
				messageProducer.send(session.createObjectMessage(loadedParser));
				log.debug("Sent an apx message");
			} catch (JMSException e) {
				log.warn(
						"Exception occurred while sending message to apx dispatcher",
						e);
			} finally {
				if (session != null) {
					try {
						session.close();
					} catch (JMSException e) {
						log.warn("Cannot close session", e);
					}
				}
				if (connection != null) {
					try {
						connection.close();
					} catch (JMSException e) {
						log.warn("Cannot close connection", e);
					}
				}
			}
	}*/
    

}
