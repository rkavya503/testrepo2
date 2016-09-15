package com.akuacom.drms.performance;

import junit.framework.Assert;
import junit.framework.TestCase;
/**
 * tests methods of TestPlanGenerator
 * @author Sunil
 *
 */
public class TestPlanGeneratorTest extends TestCase{

    /**
     * tests generateInitialNode
     */
    public void testGenerateInitialNode()
    {
    	TestPlanGenerator g = new TestPlanGenerator();
    	Assert.assertNotNull(g);
    	String result = g.generateInitialNode();
    	Assert.assertNotNull(result);
    	Assert.assertTrue(result.contains("<TestPlan guiclass=\"TestPlanGui\" testclass=\"TestPlan\" testname=\"Test Plan\" enabled=\"true\">"));
    	Assert.assertTrue(result.contains("<stringProp name=\"TestPlan.user_define_classpath\"></stringProp>"));
    }

    /**
     * tests generateThreadGroup
     */
    public void testGenerateThreadGroup()
    {
    	TestPlanGenerator g = new TestPlanGenerator();
    	Assert.assertNotNull(g);
    	String result = g.generateThreadGroup(0,0,0);
    	Assert.assertNotNull(result);
    	Assert.assertTrue(result.contains("<stringProp name=\"ThreadGroup.num_threads\">" + 0 + "</stringProp>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"LoopController.loops\">" + 0 + "</stringProp>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"ThreadGroup.ramp_time\">" + 0 + "</stringProp>"));
    	result = g.generateThreadGroup(1,60,1);
    	Assert.assertNotNull(result);
    	Assert.assertTrue(result.contains("<stringProp name=\"ThreadGroup.num_threads\">" + 1 + "</stringProp>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"LoopController.loops\">" + 1 + "</stringProp>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"ThreadGroup.ramp_time\">" + 60 + "</stringProp>"));
    }

    /**
     * tests generateGetEventStatesController
     */
    public void testGenerateGetEventStatesController()
    {
    	TestPlanGenerator g = new TestPlanGenerator();
    	Assert.assertNotNull(g);
    	String result = g.generateGetEventStatesController("paul.openadr.com",8443,"test.test", "Test_1234");
    	Assert.assertNotNull(result);
    	Assert.assertTrue(result.contains("<GenericController guiclass=\"LogicControllerGui\" testclass=\"GenericController\" testname=\"GetEventStates_Controller\" enabled=\"true\"/>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"Authorization.url\">http://" + "paul.openadr.com" + "</stringProp>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"Authorization.username\">" + "test.test" + "</stringProp>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"Authorization.password\">" + "Test_1234" + "</stringProp>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"WebserviceSampler.wsdl_url\">http://openadr.lbl.gov/src/1/DRASClientSOAP.wsdl</stringProp>"));
    }
	
    /**
     * tests generateConfirmationController
     */
    public void testGenerateConfirmationController()
    {
    	TestPlanGenerator g = new TestPlanGenerator();
    	Assert.assertNotNull(g);
    	String result = g.generateConfirmationController("paul.openadr.com",8443,"test.test", "Test_1234", "RTP Agricultural");
    	Assert.assertNotNull(result);
    	Assert.assertTrue(result.contains("<GenericController guiclass=\"LogicControllerGui\" testclass=\"GenericController\" testname=\"SetEventStateConfirmation_Controller\" enabled=\"true\"/>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"Authorization.url\">http://" + "paul.openadr.com" + "</stringProp>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"Authorization.username\">" + "test.test" + "</stringProp>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"Authorization.password\">" + "Test_1234" + "</stringProp>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"WebserviceSampler.wsdl_url\">http://openadr.lbl.gov/src/1/DRASClientSOAP.wsdl</stringProp>"));
    	Assert.assertTrue(result.contains("&lt;eventStateConfirmation programName=&quot;" + "RTP Agricultural" + "&quot; eventModNumber=&quot;0&quot; eventIdentifier=&quot;&quot; drasClientID=&quot;" + "test.test" + "&quot; eventStateID=&quot;${EVENT_STATE_ID}&quot; schemaVersion=&quot;&quot; operationModeValue=&quot;NORMAL&quot; optInStatus=&quot;true&quot; currentTime=&quot;11.0&quot; /&gt;"));
    }
    
    
    /**
     * tests generateResultCollector
     */
    public void testGenerateResultCollector()
    {
    	TestPlanGenerator g = new TestPlanGenerator();
    	Assert.assertNotNull(g);
    	String filePath = "C:\\akua_docs\\jmeter\\agg_results.xml";
    	String result = g.generateResultCollector(filePath);
    	Assert.assertNotNull(result);
    	Assert.assertTrue(result.contains("<ResultCollector guiclass=\"ViewResultsFullVisualizer\" testclass=\"ResultCollector\" testname=\"View Results Tree\" enabled=\"true\">"));
    	Assert.assertTrue(result.contains("<saveAssertionResultsFailureMessage>false</saveAssertionResultsFailureMessage>"));
    	Assert.assertTrue(result.contains("<stringProp name=\"filename\">" + filePath + "</stringProp>"));
    	Assert.assertTrue(result.contains("</jmeterTestPlan>"));
    }
    
    

}
