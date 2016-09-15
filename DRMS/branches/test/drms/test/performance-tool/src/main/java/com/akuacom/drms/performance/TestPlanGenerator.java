package com.akuacom.drms.performance;

/**
 * Generates JMeter test plan
 * @author Sunil
 *
 */
public class TestPlanGenerator {

	/**
	 * New Line character
	 */
	public static String newline = System.getProperty("line.separator");
	
	/**
	 * Generates the initial node
	 * @return
	 */
	public String generateInitialNode(){
		StringBuffer sb = new StringBuffer();
		sb.append(createNewLine("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
		sb.append(createNewLine("<jmeterTestPlan version=\"1.2\" properties=\"2.1\">"));
		sb.append(createNewLine("<hashTree>"));
		sb.append(createNewLine("<TestPlan guiclass=\"TestPlanGui\" testclass=\"TestPlan\" testname=\"Test Plan\" enabled=\"true\">"));
		sb.append(createNewLine("<stringProp name=\"TestPlan.comments\"></stringProp>"));
		sb.append(createNewLine("<boolProp name=\"TestPlan.functional_mode\">false</boolProp>"));
		sb.append(createNewLine("<boolProp name=\"TestPlan.serialize_threadgroups\">false</boolProp>"));
		sb.append(createNewLine("<elementProp name=\"TestPlan.user_defined_variables\" elementType=\"Arguments\" guiclass=\"ArgumentsPanel\" testclass=\"Arguments\" testname=\"User Defined Variables\" enabled=\"true\">"));
		sb.append(createNewLine("<collectionProp name=\"Arguments.arguments\"/>"));
		sb.append(createNewLine("</elementProp>"));
		sb.append(createNewLine("<stringProp name=\"TestPlan.user_define_classpath\"></stringProp>"));
		sb.append(createNewLine("</TestPlan>"));
		sb.append(createNewLine("<hashTree>"));
		return sb.toString();
	}

	/**
	 * Adds new line character to the end of the string
	 * @param text string
	 * @return new line appended string
	 */
	private String createNewLine(String text){
		return text + newline;
	}
	
	
	/**
	 * Generates the thread group node
	 * @param numberOfThreads Number of simultaneous threads - default is 1
	 * @param rampUpTime Ramp up time is seconds - default is 60 sec
	 * @param loopCount Loop count - default is 1
	 * @return Thread group node
	 */
	public String generateThreadGroup(int numberOfThreads, int rampUpTime, int loopCount){
		StringBuffer sb = new StringBuffer();
		sb.append(createNewLine("<ThreadGroup guiclass=\"ThreadGroupGui\" testclass=\"ThreadGroup\" testname=\"Web Service Tests\" enabled=\"true\">"));
		sb.append(createNewLine("<elementProp name=\"ThreadGroup.main_controller\" elementType=\"LoopController\" guiclass=\"LoopControlPanel\" testclass=\"LoopController\" testname=\"Loop Controller\" enabled=\"true\">"));
		sb.append(createNewLine("<boolProp name=\"LoopController.continue_forever\">false</boolProp>"));
		sb.append(createNewLine("<stringProp name=\"LoopController.loops\">" + loopCount + "</stringProp>"));
		sb.append(createNewLine("</elementProp>"));
		sb.append(createNewLine("<stringProp name=\"ThreadGroup.num_threads\">" + numberOfThreads + "</stringProp>"));
		sb.append(createNewLine("<stringProp name=\"ThreadGroup.ramp_time\">" + rampUpTime + "</stringProp>"));
		sb.append(createNewLine("<longProp name=\"ThreadGroup.start_time\">1269467587000</longProp>"));
		sb.append(createNewLine("<longProp name=\"ThreadGroup.end_time\">1269467587000</longProp>"));
		sb.append(createNewLine("<boolProp name=\"ThreadGroup.scheduler\">false</boolProp>"));
		sb.append(createNewLine("<stringProp name=\"ThreadGroup.on_sample_error\">continue</stringProp>"));
		sb.append(createNewLine("<stringProp name=\"ThreadGroup.duration\"></stringProp>"));
		sb.append(createNewLine("<stringProp name=\"ThreadGroup.delay\"></stringProp>"));
		sb.append(createNewLine("<stringProp name=\"TestPlan.comments\">SOAP</stringProp>"));
		sb.append(createNewLine("</ThreadGroup>"));
		sb.append(createNewLine("<hashTree>"));
		sb.append(createNewLine(""));
		return sb.toString();
	}
	

	/**
	 * Generate Get Event State Controller 
	 * @param drasHostName DRAS Host name e.g. paul.openadr.com
	 * @param drasPort Dras port
	 * @param participantName Participant name e.g. test.test
	 * @param participantPassword Participant password
	 * @return Get Event State Controller string
	 */
	public String generateGetEventStatesController(String drasHostName, int drasPort, String participantName, String participantPassword){
		StringBuffer sb = new StringBuffer();
		sb.append(createNewLine("<GenericController guiclass=\"LogicControllerGui\" testclass=\"GenericController\" testname=\"GetEventStates_Controller\" enabled=\"true\"/>"));
		sb.append(createNewLine("<hashTree>"));
		sb.append(createNewLine("<AuthManager guiclass=\"AuthPanel\" testclass=\"AuthManager\" testname=\"HTTP Authorization Manager\" enabled=\"true\">"));
		sb.append(createNewLine("<collectionProp name=\"AuthManager.auth_list\">"));
		sb.append(createNewLine("<elementProp name=\"\" elementType=\"Authorization\">"));
		sb.append(createNewLine("<stringProp name=\"Authorization.url\">http://" + drasHostName + "</stringProp>"));
		sb.append(createNewLine("<stringProp name=\"Authorization.username\">" + participantName + "</stringProp>"));
		sb.append(createNewLine("<stringProp name=\"Authorization.password\">" + participantPassword + "</stringProp>"));
		sb.append(createNewLine("<stringProp name=\"Authorization.domain\">/</stringProp>"));
		sb.append(createNewLine("<stringProp name=\"Authorization.realm\">PSS2WS</stringProp>"));
		sb.append(createNewLine("</elementProp>"));
		sb.append(createNewLine("</collectionProp>"));
		sb.append(createNewLine("</AuthManager>"));
		sb.append(createNewLine("<hashTree/>"));
		sb.append(createNewLine("<WebServiceSampler guiclass=\"WebServiceSamplerGui\" testclass=\"WebServiceSampler\" testname=\"GetEventStates\" enabled=\"true\">"));
		sb.append(createNewLine("<elementProp name=\"HTTPsampler.Arguments\" elementType=\"Arguments\">"));
		sb.append(createNewLine("<collectionProp name=\"Arguments.arguments\"/>"));
		sb.append(createNewLine("</elementProp>"));
		sb.append(createNewLine("<stringProp name=\"HTTPSampler.domain\">" + drasHostName + "</stringProp>"));
		sb.append(createNewLine("<stringProp name=\"HTTPSampler.port\">" + drasPort + "</stringProp>"));
		sb.append(createNewLine("<stringProp name=\"HTTPSampler.protocol\">http</stringProp>"));
		sb.append(createNewLine("<stringProp name=\"HTTPSampler.path\">SOAPClientWS/nossl/soap2</stringProp>"));
		sb.append(createNewLine("<stringProp name=\"WebserviceSampler.wsdl_url\">http://openadr.lbl.gov/src/1/DRASClientSOAP.wsdl</stringProp>"));
		sb.append(createNewLine("<stringProp name=\"HTTPSampler.method\">POST</stringProp>"));
		sb.append(createNewLine("<stringProp name=\"Soap.Action\">GetEventStates</stringProp>"));
		sb.append(createNewLine("<stringProp name=\"HTTPSamper.xml_data\">&lt;soapenv:Envelope xmlns:soapenv=&quot;http://schemas.xmlsoap.org/soap/envelope/&quot; xmlns:dras=&quot;http://www.openadr.org/DRAS/DRASClientSOAP/&quot;&gt;"));
		sb.append(createNewLine("&lt;soapenv:Header/&gt;"));
		sb.append(createNewLine("&lt;soapenv:Body&gt;"));
		sb.append(createNewLine("&lt;dras:GetEventStates&gt;"));
		sb.append(createNewLine("&lt;empty&gt;?&lt;/empty&gt;"));
		sb.append(createNewLine("&lt;/dras:GetEventStates&gt;"));
		sb.append(createNewLine("&lt;/soapenv:Body&gt;"));
		sb.append(createNewLine("&lt;/soapenv:Envelope&gt;</stringProp>"));
		sb.append(createNewLine("<stringProp name=\"WebServiceSampler.xml_data_file\"></stringProp>"));
		sb.append(createNewLine("<stringProp name=\"WebServiceSampler.xml_path_loc\"></stringProp>"));
		sb.append(createNewLine("<stringProp name=\"WebserviceSampler.timeout\"></stringProp>"));
		sb.append(createNewLine("<stringProp name=\"WebServiceSampler.memory_cache\">true</stringProp>"));
		sb.append(createNewLine("<stringProp name=\"WebServiceSampler.read_response\">true</stringProp>"));
		sb.append(createNewLine("<stringProp name=\"WebServiceSampler.use_proxy\">false</stringProp>"));
		sb.append(createNewLine("<stringProp name=\"WebServiceSampler.proxy_host\"></stringProp>"));
		sb.append(createNewLine("<stringProp name=\"WebServiceSampler.proxy_port\"></stringProp>"));
		sb.append(createNewLine("</WebServiceSampler>"));
		sb.append(createNewLine("<hashTree/>"));
		sb.append(createNewLine("<ResponseAssertion guiclass=\"AssertionGui\" testclass=\"ResponseAssertion\" testname=\"Response Assertion\" enabled=\"true\">"));
		sb.append(createNewLine("<collectionProp name=\"Asserion.test_strings\">"));
		sb.append(createNewLine("<stringProp name=\"49586\">200</stringProp>"));
		sb.append(createNewLine("</collectionProp>"));
		sb.append(createNewLine("<stringProp name=\"Assertion.test_field\">Assertion.response_code</stringProp>"));
		sb.append(createNewLine("<boolProp name=\"Assertion.assume_success\">false</boolProp>"));
		sb.append(createNewLine("<intProp name=\"Assertion.test_type\">2</intProp>"));
		sb.append(createNewLine("</ResponseAssertion>"));
		sb.append(createNewLine("<hashTree/>"));
		sb.append(createNewLine("<XPathAssertion guiclass=\"XPathAssertionGui\" testclass=\"XPathAssertion\" testname=\"XPath Assertion\" enabled=\"true\">"));
		sb.append(createNewLine("<boolProp name=\"XPath.negate\">true</boolProp>"));
		sb.append(createNewLine("<stringProp name=\"XPath.xpath\">//faultstring</stringProp>"));
		sb.append(createNewLine("<boolProp name=\"XPath.validate\">false</boolProp>"));
		sb.append(createNewLine("<boolProp name=\"XPath.whitespace\">false</boolProp>"));
		sb.append(createNewLine("<boolProp name=\"XPath.tolerant\">false</boolProp>"));
		sb.append(createNewLine("<boolProp name=\"XPath.namespace\">true</boolProp>"));
		sb.append(createNewLine("</XPathAssertion>"));
		sb.append(createNewLine("<hashTree/>"));
		sb.append(createNewLine("<XPathExtractor guiclass=\"XPathExtractorGui\" testclass=\"XPathExtractor\" testname=\"Extract_ID\" enabled=\"true\">"));
		sb.append(createNewLine("<stringProp name=\"XPathExtractor.default\">2092925224</stringProp>"));
		sb.append(createNewLine("<stringProp name=\"XPathExtractor.refname\">EVENT_STATE_ID</stringProp>"));
		sb.append(createNewLine("<stringProp name=\"XPathExtractor.xpathQuery\">//eventStates/@eventStateID</stringProp>"));
		sb.append(createNewLine("<boolProp name=\"XPathExtractor.validate\">false</boolProp>"));
		sb.append(createNewLine("<boolProp name=\"XPathExtractor.tolerant\">false</boolProp>"));
		sb.append(createNewLine("<boolProp name=\"XPathExtractor.namespace\">false</boolProp>"));
		sb.append(createNewLine("</XPathExtractor>"));
		sb.append(createNewLine("<hashTree/>"));
		sb.append(createNewLine("</hashTree>"));
		return sb.toString();
	}
	
	/**
	 * Generate Event State Conformation Controller 
	 * @param drasHostName DRAS Host name e.g. paul.openadr.com
	 * @param drasPort Dras port
	 * @param participantName Participant name e.g. test.test
	 * @param participantPassword Participant password
	 * @param programName - Program Name - Default RTP Agricultural
	 * @return Get Event State Controller string
	 */
	public String generateConfirmationController(String drasHostName, int drasPort, String participantName, String participantPassword, String programName){
		StringBuffer sb = new StringBuffer();
		sb.append(createNewLine("<GenericController guiclass=\"LogicControllerGui\" testclass=\"GenericController\" testname=\"SetEventStateConfirmation_Controller\" enabled=\"true\"/>"));
		sb.append(createNewLine("<hashTree>"));
		sb.append(createNewLine("<AuthManager guiclass=\"AuthPanel\" testclass=\"AuthManager\" testname=\"HTTP Authorization Manager\" enabled=\"true\">"));
		sb.append(createNewLine("<collectionProp name=\"AuthManager.auth_list\">"));
		sb.append(createNewLine("<elementProp name=\"\" elementType=\"Authorization\">"));
		sb.append(createNewLine("<stringProp name=\"Authorization.url\">http://" + drasHostName + "</stringProp>"));
		sb.append(createNewLine("<stringProp name=\"Authorization.username\">" + participantName + "</stringProp>"));
		sb.append(createNewLine("<stringProp name=\"Authorization.password\">" + participantPassword + "</stringProp>"));
		sb.append(createNewLine("<stringProp name=\"Authorization.domain\">/</stringProp>"));
		sb.append(createNewLine("<stringProp name=\"Authorization.realm\">PSS2WS</stringProp>"));
		sb.append(createNewLine("</elementProp>"));
		sb.append(createNewLine("</collectionProp>"));
		sb.append(createNewLine("</AuthManager>"));
		sb.append(createNewLine("<hashTree/>"));
		sb.append(createNewLine("<WebServiceSampler guiclass=\"WebServiceSamplerGui\" testclass=\"WebServiceSampler\" testname=\"SetEventStateConfirmation\" enabled=\"true\">"));
		sb.append(createNewLine("<elementProp name=\"HTTPsampler.Arguments\" elementType=\"Arguments\">"));
		sb.append(createNewLine("<collectionProp name=\"Arguments.arguments\"/>"));
		sb.append(createNewLine("</elementProp>"));
		sb.append(createNewLine("<stringProp name=\"HTTPSampler.domain\">" + drasHostName + "</stringProp>"));
		sb.append(createNewLine("<stringProp name=\"HTTPSampler.port\">" + drasPort + "</stringProp>"));
		sb.append(createNewLine("<stringProp name=\"HTTPSampler.protocol\">http</stringProp>"));
		sb.append(createNewLine("<stringProp name=\"HTTPSampler.path\">SOAPClientWS/nossl/soap2</stringProp>"));
		sb.append(createNewLine("<stringProp name=\"WebserviceSampler.wsdl_url\">http://openadr.lbl.gov/src/1/DRASClientSOAP.wsdl</stringProp>"));
		sb.append(createNewLine("<stringProp name=\"HTTPSampler.method\">POST</stringProp>"));
		sb.append(createNewLine("<stringProp name=\"Soap.Action\">SetEventStateConfirmation</stringProp>"));
		sb.append(createNewLine("<stringProp name=\"HTTPSamper.xml_data\">&lt;soapenv:Envelope xmlns:soapenv=&quot;http://schemas.xmlsoap.org/soap/envelope/&quot; xmlns:dras=&quot;http://www.openadr.org/DRAS/DRASClientSOAP/&quot;&gt;"));
		sb.append(createNewLine("&lt;soapenv:Header/&gt;"));
		sb.append(createNewLine("&lt;soapenv:Body&gt;"));
		sb.append(createNewLine("&lt;dras:SetEventStateConfirmation&gt;"));
		sb.append(createNewLine("&lt;eventStateConfirmation programName=&quot;" + programName + "&quot; eventModNumber=&quot;0&quot; eventIdentifier=&quot;&quot; drasClientID=&quot;" + participantName + "&quot; eventStateID=&quot;${EVENT_STATE_ID}&quot; schemaVersion=&quot;&quot; operationModeValue=&quot;NORMAL&quot; optInStatus=&quot;true&quot; currentTime=&quot;11.0&quot; /&gt;"));
		sb.append(createNewLine("&lt;/dras:SetEventStateConfirmation&gt;"));
		sb.append(createNewLine("&lt;/soapenv:Body&gt;"));
		sb.append(createNewLine("&lt;/soapenv:Envelope&gt;"));
		sb.append(createNewLine("</stringProp>"));
		sb.append(createNewLine("<stringProp name=\"WebServiceSampler.xml_data_file\"></stringProp>"));
		sb.append(createNewLine("<stringProp name=\"WebServiceSampler.xml_path_loc\"></stringProp>"));
		sb.append(createNewLine("<stringProp name=\"WebserviceSampler.timeout\"></stringProp>"));
		sb.append(createNewLine("<stringProp name=\"WebServiceSampler.memory_cache\">true</stringProp>"));
		sb.append(createNewLine("<stringProp name=\"WebServiceSampler.read_response\">true</stringProp>"));
		sb.append(createNewLine("<stringProp name=\"WebServiceSampler.use_proxy\">false</stringProp>"));
		sb.append(createNewLine("<stringProp name=\"WebServiceSampler.proxy_host\"></stringProp>"));
		sb.append(createNewLine("<stringProp name=\"WebServiceSampler.proxy_port\"></stringProp>"));
		sb.append(createNewLine("</WebServiceSampler>"));
		sb.append(createNewLine("<hashTree/>"));
		sb.append(createNewLine("<ResponseAssertion guiclass=\"AssertionGui\" testclass=\"ResponseAssertion\" testname=\"Response Assertion\" enabled=\"true\">"));
		sb.append(createNewLine("<collectionProp name=\"Asserion.test_strings\">"));
		sb.append(createNewLine("<stringProp name=\"49586\">200</stringProp>"));
		sb.append(createNewLine("</collectionProp>"));
		sb.append(createNewLine("<stringProp name=\"Assertion.test_field\">Assertion.response_code</stringProp>"));
		sb.append(createNewLine("<boolProp name=\"Assertion.assume_success\">false</boolProp>"));
		sb.append(createNewLine("<intProp name=\"Assertion.test_type\">2</intProp>"));
		sb.append(createNewLine("</ResponseAssertion>"));
		sb.append(createNewLine("<hashTree/>"));
		sb.append(createNewLine("<XPathAssertion guiclass=\"XPathAssertionGui\" testclass=\"XPathAssertion\" testname=\"XPath Assertion\" enabled=\"true\">"));
		sb.append(createNewLine("<boolProp name=\"XPath.negate\">true</boolProp>"));
		sb.append(createNewLine("<stringProp name=\"XPath.xpath\">//faultstring</stringProp>"));
		sb.append(createNewLine("<boolProp name=\"XPath.validate\">false</boolProp>"));
		sb.append(createNewLine("<boolProp name=\"XPath.whitespace\">false</boolProp>"));
		sb.append(createNewLine("<boolProp name=\"XPath.tolerant\">false</boolProp>"));
		sb.append(createNewLine("<boolProp name=\"XPath.namespace\">true</boolProp>"));
		sb.append(createNewLine("</XPathAssertion>"));
		sb.append(createNewLine("<hashTree/>"));
		sb.append(createNewLine("</hashTree>"));
		return sb.toString();
	}



	/**
	 * Generates the result collectors
	 * @param filePath File path to save the results
	 * @return Generated resul collectors as a string
	 */
	public String generateResultCollector(String filePath){
		StringBuffer sb = new StringBuffer();
		sb.append(createNewLine("<ResultCollector guiclass=\"ViewResultsFullVisualizer\" testclass=\"ResultCollector\" testname=\"View Results Tree\" enabled=\"true\">"));
		sb.append(createNewLine("<boolProp name=\"ResultCollector.error_logging\">false</boolProp>"));
		sb.append(createNewLine("<objProp>"));
		sb.append(createNewLine("<name>saveConfig</name>"));
		sb.append(createNewLine("<value class=\"SampleSaveConfiguration\">"));
		sb.append(createNewLine("<time>true</time>"));
		sb.append(createNewLine("<latency>true</latency>"));
		sb.append(createNewLine("<timestamp>true</timestamp>"));
		sb.append(createNewLine("<success>true</success>"));
		sb.append(createNewLine("<label>true</label>"));
		sb.append(createNewLine("<code>true</code>"));
		sb.append(createNewLine("<message>true</message>"));
		sb.append(createNewLine("<threadName>true</threadName>"));
		sb.append(createNewLine("<dataType>true</dataType>"));
		sb.append(createNewLine("<encoding>false</encoding>"));
		sb.append(createNewLine("<assertions>true</assertions>"));
		sb.append(createNewLine("<subresults>true</subresults>"));
		sb.append(createNewLine("<responseData>false</responseData>"));
		sb.append(createNewLine("<samplerData>false</samplerData>"));
		sb.append(createNewLine("<xml>true</xml>"));
		sb.append(createNewLine("<fieldNames>false</fieldNames>"));
		sb.append(createNewLine("<responseHeaders>false</responseHeaders>"));
		sb.append(createNewLine("<requestHeaders>false</requestHeaders>"));
		sb.append(createNewLine("<responseDataOnError>false</responseDataOnError>"));
		sb.append(createNewLine("<saveAssertionResultsFailureMessage>false</saveAssertionResultsFailureMessage>"));
		sb.append(createNewLine("<assertionsResultsToSave>0</assertionsResultsToSave>"));
		sb.append(createNewLine("<bytes>true</bytes>"));
		sb.append(createNewLine("</value>"));
		sb.append(createNewLine("</objProp>"));
		sb.append(createNewLine("<stringProp name=\"filename\"></stringProp>"));
		sb.append(createNewLine("</ResultCollector>"));
		sb.append(createNewLine("<hashTree/>"));
		sb.append(createNewLine("<ResultCollector guiclass=\"StatVisualizer\" testclass=\"ResultCollector\" testname=\"Aggregate Report\" enabled=\"true\">"));
		sb.append(createNewLine("<boolProp name=\"ResultCollector.error_logging\">false</boolProp>"));
		sb.append(createNewLine("<objProp>"));
		sb.append(createNewLine("<name>saveConfig</name>"));
		sb.append(createNewLine("<value class=\"SampleSaveConfiguration\">"));
		sb.append(createNewLine("<time>true</time>"));
		sb.append(createNewLine("<latency>true</latency>"));
		sb.append(createNewLine("<timestamp>true</timestamp>"));
		sb.append(createNewLine("<success>true</success>"));
		sb.append(createNewLine("<label>true</label>"));
		sb.append(createNewLine("<code>true</code>"));
		sb.append(createNewLine("<message>true</message>"));
		sb.append(createNewLine("<threadName>true</threadName>"));
		sb.append(createNewLine("<dataType>true</dataType>"));
		sb.append(createNewLine("<encoding>false</encoding>"));
		sb.append(createNewLine("<assertions>true</assertions>"));
		sb.append(createNewLine("<subresults>true</subresults>"));
		sb.append(createNewLine("<responseData>true</responseData>"));
		sb.append(createNewLine("<samplerData>false</samplerData>"));
		sb.append(createNewLine("<xml>true</xml>"));
		sb.append(createNewLine("<fieldNames>false</fieldNames>"));
		sb.append(createNewLine("<responseHeaders>true</responseHeaders>"));
		sb.append(createNewLine("<requestHeaders>true</requestHeaders>"));
		sb.append(createNewLine("<responseDataOnError>false</responseDataOnError>"));
		sb.append(createNewLine("<saveAssertionResultsFailureMessage>false</saveAssertionResultsFailureMessage>"));
		sb.append(createNewLine("<assertionsResultsToSave>0</assertionsResultsToSave>"));
		sb.append(createNewLine("<bytes>true</bytes>"));
		sb.append(createNewLine("<hostname>true</hostname>"));
		sb.append(createNewLine("<threadCounts>true</threadCounts>"));
		sb.append(createNewLine("<sampleCount>true</sampleCount>"));
		sb.append(createNewLine("</value>"));
		sb.append(createNewLine("</objProp>"));
		sb.append(createNewLine("<stringProp name=\"filename\">" + filePath + "</stringProp>"));
		sb.append(createNewLine("</ResultCollector>"));
		sb.append(createNewLine("<hashTree/>"));
		sb.append(createNewLine("<ResultCollector guiclass=\"GraphVisualizer\" testclass=\"ResultCollector\" testname=\"Graph Results\" enabled=\"true\">"));
		sb.append(createNewLine("<boolProp name=\"ResultCollector.error_logging\">false</boolProp>"));
		sb.append(createNewLine("<objProp>"));
		sb.append(createNewLine("<name>saveConfig</name>"));
		sb.append(createNewLine("<value class=\"SampleSaveConfiguration\">"));
		sb.append(createNewLine("<time>true</time>"));
		sb.append(createNewLine("<latency>true</latency>"));
		sb.append(createNewLine("<timestamp>true</timestamp>"));
		sb.append(createNewLine("<success>true</success>"));
		sb.append(createNewLine("<label>true</label>"));
		sb.append(createNewLine("<code>true</code>"));
		sb.append(createNewLine("<message>true</message>"));
		sb.append(createNewLine("<threadName>true</threadName>"));
		sb.append(createNewLine("<dataType>true</dataType>"));
		sb.append(createNewLine("<encoding>false</encoding>"));
		sb.append(createNewLine("<assertions>true</assertions>"));
		sb.append(createNewLine("<subresults>true</subresults>"));
		sb.append(createNewLine("<responseData>false</responseData>"));
		sb.append(createNewLine("<samplerData>false</samplerData>"));
		sb.append(createNewLine("<xml>true</xml>"));
		sb.append(createNewLine("<fieldNames>false</fieldNames>"));
		sb.append(createNewLine("<responseHeaders>false</responseHeaders>"));
		sb.append(createNewLine("<requestHeaders>false</requestHeaders>"));
		sb.append(createNewLine("<responseDataOnError>false</responseDataOnError>"));
		sb.append(createNewLine("<saveAssertionResultsFailureMessage>false</saveAssertionResultsFailureMessage>"));
		sb.append(createNewLine("<assertionsResultsToSave>0</assertionsResultsToSave>"));
		sb.append(createNewLine("<bytes>true</bytes>"));
		sb.append(createNewLine("</value>"));
		sb.append(createNewLine("</objProp>"));
		sb.append(createNewLine("<stringProp name=\"filename\">" + filePath + "</stringProp>"));
		sb.append(createNewLine("</ResultCollector>"));
		sb.append(createNewLine("<hashTree/>"));
		sb.append(createNewLine("</hashTree>"));
		sb.append(createNewLine("</hashTree>"));
		sb.append(createNewLine("</hashTree>"));
		sb.append(createNewLine("</jmeterTestPlan>"));
		return sb.toString();
	}
}
