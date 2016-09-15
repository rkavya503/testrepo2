package com.akuacom.drms.performance;
import java.io.File;

import org.junit.Ignore;
import org.junit.Test;

import junit.framework.Assert;


import com.akuacom.drms.performance.PerformanceTool;


/**
 * Tests methods in class PerformanceTool
 * @author Sunil
 *
 */
public class PerformanceToolTest{
	
    /**
     * tests createPlan
     * @throws Exception 
     */
    @Test
	public void testCreatePlan() throws Exception
    {
    	PerformanceTool t = new PerformanceTool();
    	Assert.assertNotNull(t);
    	String filePath = "C:\\akua_docs\\jmeter\\agg_results.xml";
    	String result = t.createPlan(1, 60, 1, "paul.openadr.com", 8443, "Test_1234", true, "test", "test", 1, "RTP Agricultural", filePath);
    	Assert.assertNotNull(result);
    	Assert.assertTrue(result.contains("<TestPlan guiclass=\"TestPlanGui\" testclass=\"TestPlan\" testname=\"Test Plan\" enabled=\"true\">"));
    	Assert.assertTrue(result.contains("<stringProp name=\"TestPlan.user_define_classpath\"></stringProp>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"ThreadGroup.num_threads\">" + 1 + "</stringProp>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"LoopController.loops\">" + 1 + "</stringProp>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"ThreadGroup.ramp_time\">" + 60 + "</stringProp>"));
    	Assert.assertTrue(result.contains("<GenericController guiclass=\"LogicControllerGui\" testclass=\"GenericController\" testname=\"GetEventStates_Controller\" enabled=\"true\"/>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"Authorization.url\">http://" + "paul.openadr.com" + "</stringProp>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"Authorization.username\">" + "test0.test0" + "</stringProp>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"Authorization.password\">" + "Test_1234" + "</stringProp>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"WebserviceSampler.wsdl_url\">http://openadr.lbl.gov/src/1/DRASClientSOAP.wsdl</stringProp>"));
    	Assert.assertTrue(result.contains("<GenericController guiclass=\"LogicControllerGui\" testclass=\"GenericController\" testname=\"SetEventStateConfirmation_Controller\" enabled=\"true\"/>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"Authorization.url\">http://" + "paul.openadr.com" + "</stringProp>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"Authorization.username\">" + "test0.test0" + "</stringProp>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"Authorization.password\">" + "Test_1234" + "</stringProp>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"WebserviceSampler.wsdl_url\">http://openadr.lbl.gov/src/1/DRASClientSOAP.wsdl</stringProp>"));
    	Assert.assertTrue(result.contains("&lt;eventStateConfirmation programName=&quot;" + "RTP Agricultural" + "&quot; eventModNumber=&quot;0&quot; eventIdentifier=&quot;&quot; drasClientID=&quot;" + "test0.test0" + "&quot; eventStateID=&quot;${EVENT_STATE_ID}&quot; schemaVersion=&quot;&quot; operationModeValue=&quot;NORMAL&quot; optInStatus=&quot;true&quot; currentTime=&quot;11.0&quot; /&gt;"));
    	Assert.assertTrue(result.contains("<ResultCollector guiclass=\"ViewResultsFullVisualizer\" testclass=\"ResultCollector\" testname=\"View Results Tree\" enabled=\"true\">"));
    	Assert.assertTrue(result.contains("<saveAssertionResultsFailureMessage>false</saveAssertionResultsFailureMessage>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"filename\">" + filePath + "</stringProp>"));
    	Assert.assertTrue(result.contains("</jmeterTestPlan>"));

    	
    }


    /**
     * tests createPlan with default values
     * @throws Exception 
     */
    @Test
    public void testCreatePlanDefault() throws Exception
    {
    	PerformanceTool t = new PerformanceTool();
    	Assert.assertNotNull(t);
    	String filePath = "/tmp/agg_results.xml";
    	String result = t.createPlan(0, 0, 0, null, 0, null, true, null, null, 0, null, null);
    	Assert.assertNotNull(result);
    	Assert.assertTrue(result.contains("<TestPlan guiclass=\"TestPlanGui\" testclass=\"TestPlan\" testname=\"Test Plan\" enabled=\"true\">"));
    	Assert.assertTrue(result.contains("<stringProp name=\"TestPlan.user_define_classpath\"></stringProp>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"ThreadGroup.num_threads\">" + 1 + "</stringProp>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"LoopController.loops\">" + 1 + "</stringProp>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"ThreadGroup.ramp_time\">" + 60 + "</stringProp>"));
    	Assert.assertTrue(result.contains("<GenericController guiclass=\"LogicControllerGui\" testclass=\"GenericController\" testname=\"GetEventStates_Controller\" enabled=\"true\"/>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"Authorization.url\">http://" + "localhost" + "</stringProp>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"Authorization.username\">" + "test0.test0" + "</stringProp>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"Authorization.password\">" + "Test_1234" + "</stringProp>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"WebserviceSampler.wsdl_url\">http://openadr.lbl.gov/src/1/DRASClientSOAP.wsdl</stringProp>"));
    	Assert.assertTrue(result.contains("<GenericController guiclass=\"LogicControllerGui\" testclass=\"GenericController\" testname=\"SetEventStateConfirmation_Controller\" enabled=\"true\"/>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"Authorization.url\">http://" + "localhost" + "</stringProp>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"Authorization.username\">" + "test0.test0" + "</stringProp>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"Authorization.password\">" + "Test_1234" + "</stringProp>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"WebserviceSampler.wsdl_url\">http://openadr.lbl.gov/src/1/DRASClientSOAP.wsdl</stringProp>"));
    	Assert.assertTrue(result.contains("&lt;eventStateConfirmation programName=&quot;" + "RTP Agricultural" + "&quot; eventModNumber=&quot;0&quot; eventIdentifier=&quot;&quot; drasClientID=&quot;" + "test0.test0" + "&quot; eventStateID=&quot;${EVENT_STATE_ID}&quot; schemaVersion=&quot;&quot; operationModeValue=&quot;NORMAL&quot; optInStatus=&quot;true&quot; currentTime=&quot;11.0&quot; /&gt;"));
    	Assert.assertTrue(result.contains("<ResultCollector guiclass=\"ViewResultsFullVisualizer\" testclass=\"ResultCollector\" testname=\"View Results Tree\" enabled=\"true\">"));
    	Assert.assertTrue(result.contains("<saveAssertionResultsFailureMessage>false</saveAssertionResultsFailureMessage>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"filename\">" + filePath + "</stringProp>"));
    	Assert.assertTrue(result.contains("</jmeterTestPlan>"));

    	
    }
    
    
    
    /**
     * tests createPlan with no confirmation
     * @throws Exception 
     */
    @Test
    public void testCreatePlanNoConfirmation() throws Exception
    {
    	PerformanceTool t = new PerformanceTool();
    	Assert.assertNotNull(t);
    	String filePath = "C:\\akua_docs\\jmeter\\agg_results.xml";
    	String result = t.createPlan(1, 60, 1, "paul.openadr.com", 8443, "Test_1234", false, "test", "test", 1, "RTP Agricultural", filePath);
    	Assert.assertNotNull(result);
    	Assert.assertTrue(result.contains("<TestPlan guiclass=\"TestPlanGui\" testclass=\"TestPlan\" testname=\"Test Plan\" enabled=\"true\">"));
    	Assert.assertTrue(result.contains("<stringProp name=\"TestPlan.user_define_classpath\"></stringProp>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"ThreadGroup.num_threads\">" + 1 + "</stringProp>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"LoopController.loops\">" + 1 + "</stringProp>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"ThreadGroup.ramp_time\">" + 60 + "</stringProp>"));
    	Assert.assertTrue(result.contains("<GenericController guiclass=\"LogicControllerGui\" testclass=\"GenericController\" testname=\"GetEventStates_Controller\" enabled=\"true\"/>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"Authorization.url\">http://" + "paul.openadr.com" + "</stringProp>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"Authorization.username\">" + "test0.test0" + "</stringProp>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"Authorization.password\">" + "Test_1234" + "</stringProp>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"WebserviceSampler.wsdl_url\">http://openadr.lbl.gov/src/1/DRASClientSOAP.wsdl</stringProp>"));
    	Assert.assertFalse(result.contains("<GenericController guiclass=\"LogicControllerGui\" testclass=\"GenericController\" testname=\"SetEventStateConfirmation_Controller\" enabled=\"true\"/>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"Authorization.url\">http://" + "paul.openadr.com" + "</stringProp>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"Authorization.username\">" + "test0.test0" + "</stringProp>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"Authorization.password\">" + "Test_1234" + "</stringProp>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"WebserviceSampler.wsdl_url\">http://openadr.lbl.gov/src/1/DRASClientSOAP.wsdl</stringProp>"));
    	Assert.assertFalse(result.contains("&lt;eventStateConfirmation programName=&quot;" + "RTP Agricultural" + "&quot; eventModNumber=&quot;0&quot; eventIdentifier=&quot;&quot; drasClientID=&quot;" + "test0.test0" + "&quot; eventStateID=&quot;${EVENT_STATE_ID}&quot; schemaVersion=&quot;&quot; operationModeValue=&quot;NORMAL&quot; optInStatus=&quot;true&quot; currentTime=&quot;11.0&quot; /&gt;"));
    	Assert.assertTrue(result.contains("<ResultCollector guiclass=\"ViewResultsFullVisualizer\" testclass=\"ResultCollector\" testname=\"View Results Tree\" enabled=\"true\">"));
    	Assert.assertTrue(result.contains("<saveAssertionResultsFailureMessage>false</saveAssertionResultsFailureMessage>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"filename\">" + filePath + "</stringProp>"));
    	Assert.assertTrue(result.contains("</jmeterTestPlan>"));

    	
    }

    
    /**
     * tests presistTestPlan with default values
     * @throws Exception 
     */
    @Test
    public void testPresistTestPlanDefault() throws Exception
    {
    	PerformanceTool t = new PerformanceTool();
    	Assert.assertNotNull(t);
    	String filePath = "/tmp/agg_results.xml";
    	String result = t.createPlan(0, 0, 0, null, 0, null, true, null, null, 0, null, null);
    	Assert.assertNotNull(result);
    	Assert.assertTrue(result.contains("<TestPlan guiclass=\"TestPlanGui\" testclass=\"TestPlan\" testname=\"Test Plan\" enabled=\"true\">"));
    	Assert.assertTrue(result.contains("<stringProp name=\"TestPlan.user_define_classpath\"></stringProp>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"ThreadGroup.num_threads\">" + 1 + "</stringProp>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"LoopController.loops\">" + 1 + "</stringProp>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"ThreadGroup.ramp_time\">" + 60 + "</stringProp>"));
    	Assert.assertTrue(result.contains("<GenericController guiclass=\"LogicControllerGui\" testclass=\"GenericController\" testname=\"GetEventStates_Controller\" enabled=\"true\"/>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"Authorization.url\">http://" + "localhost" + "</stringProp>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"Authorization.username\">" + "test0.test0" + "</stringProp>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"Authorization.password\">" + "Test_1234" + "</stringProp>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"WebserviceSampler.wsdl_url\">http://openadr.lbl.gov/src/1/DRASClientSOAP.wsdl</stringProp>"));
    	Assert.assertTrue(result.contains("<GenericController guiclass=\"LogicControllerGui\" testclass=\"GenericController\" testname=\"SetEventStateConfirmation_Controller\" enabled=\"true\"/>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"Authorization.url\">http://" + "localhost" + "</stringProp>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"Authorization.username\">" + "test0.test0" + "</stringProp>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"Authorization.password\">" + "Test_1234" + "</stringProp>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"WebserviceSampler.wsdl_url\">http://openadr.lbl.gov/src/1/DRASClientSOAP.wsdl</stringProp>"));
    	Assert.assertTrue(result.contains("&lt;eventStateConfirmation programName=&quot;" + "RTP Agricultural" + "&quot; eventModNumber=&quot;0&quot; eventIdentifier=&quot;&quot; drasClientID=&quot;" + "test0.test0" + "&quot; eventStateID=&quot;${EVENT_STATE_ID}&quot; schemaVersion=&quot;&quot; operationModeValue=&quot;NORMAL&quot; optInStatus=&quot;true&quot; currentTime=&quot;11.0&quot; /&gt;"));
    	Assert.assertTrue(result.contains("<ResultCollector guiclass=\"ViewResultsFullVisualizer\" testclass=\"ResultCollector\" testname=\"View Results Tree\" enabled=\"true\">"));
    	Assert.assertTrue(result.contains("<saveAssertionResultsFailureMessage>false</saveAssertionResultsFailureMessage>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"filename\">" + filePath + "</stringProp>"));
    	Assert.assertTrue(result.contains("</jmeterTestPlan>"));
    	t.presistTestPlan(null, result);
    	Assert.assertTrue((new File("target/akua_test_plan.jmx")).exists());

    	
    }

    /**
     * tests main with default values
     * @throws Exception 
     */
    @Test
    @Ignore("Remove ignore if you want to run this test - make sure your DRAS is running locally")
    public void testMainDefault() throws Exception
    {
    	PerformanceTool.main(null);
    	Assert.assertTrue((new File("target/akua_test_plan.jmx")).exists());
    }

    
    /**
     * tests main with default values
     * @throws Exception 
     */
    @Test
    @Ignore("Remove ignore if you want to run this test - make sure your DRAS is running locally")
    public void testMain() throws Exception
    {

    	String[] args = new String[] {"-noThreads" , "1" , "-rampTime" , "60" ,  "-loopCount" ,  "1" ,  "-host" , "paul.openadr.com" , "-port" , "80" , "-pwd" , "Test_1234" ,  "-confirmation" , "true" , "-partPrefix" , "test" ,  "-clientPrefix" , "test" ,  "-noParticipants"  , "10" , "-program" , "RTP Agricultural" ,  "-filePath" ,  "/tmp/x.xml" ,  "-testPlanFilePath" , "target/TestPlan10.jmx"};
    	PerformanceTool.main(args);
    	Assert.assertTrue((new File("target/TestPlan10.jmx")).exists());
    }
    
    
    
}
