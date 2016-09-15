/*
 * www.akuacom.com - Automating Demand Response
 *
 * com.akuacom.pss2.itron.pge.dbp.in.CDataTest.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.itron.pge.dbp.in;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.net.URL;

/**
 * The Class CDataTest.
 */
public class CDataTest extends TestCase {

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) throws Exception{
        new CDataTest().testItronFileStructure();
    }

    /**
     * Test c data.
     */
    public void testCData() throws Exception{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();

			URL testFile =
				Thread.currentThread().getContextClassLoader().getResource("test.xml");



            Document document = builder.parse(new File(testFile.toURI()));

            NodeList list = document.getElementsByTagName("Subject");
            int l = list.getLength();
            for (int i = 0; i < l; i++) {
                Node node = list.item(i);
                System.out.println(node.getTextContent().trim());
                System.out.println(node.getNodeValue());
            }
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }

    }

    /**
     * Test itron file structure.
     */
    public void testItronFileStructure() throws Exception{

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();


			URL testFile =
				Thread.currentThread().getContextClassLoader().getResource("sample_dbp_DA.xml");



        Document document = builder.parse(new File(testFile.toURI()));

        final Node jobNode = document.getFirstChild();
        Assert.assertEquals("Job", jobNode.getNodeName());

        final Node userReferenceNode = jobNode.getFirstChild();
        Assert.assertEquals("UserReference", userReferenceNode.getNodeName());
        System.out.println(userReferenceNode.getTextContent());

        final Node commandNode = userReferenceNode.getNextSibling();
        Assert.assertEquals("Command", commandNode.getNodeName());
        System.out.println(commandNode.getTextContent());

        Node n = commandNode;
        while( n.getNextSibling() != null){
            Node messageNode = n.getNextSibling();
            Assert.assertNotNull(messageNode);
            testMessageNode(messageNode);

            Node contactNode = messageNode.getNextSibling();
            Assert.assertNotNull(contactNode);

            Node blockNode = contactNode.getNextSibling();
            Assert.assertNotNull(blockNode);

            n = blockNode;
        }
    }

    /**
     * Test message node.
     *
     * @param messageNode the message node
     */
    private void testMessageNode(Node messageNode) {
        Node node = messageNode.getFirstChild();
        Assert.assertNotNull(node);
        Assert.assertEquals("Subject", node.getNodeName());

        Map<String, String> map = new HashMap<String, String>();
        node = node.getNextSibling();
        int i = 0;
        while (node != null) {
            Assert.assertEquals("MessageArg", node.getNodeName());
            Node nameNode = node.getFirstChild();
            Assert.assertEquals("Name", nameNode.getNodeName());
            String name = nameNode.getTextContent();

            Node valueNode = node.getLastChild();
            Assert.assertEquals("Value", valueNode.getNodeName());
            node = node.getNextSibling();
            String value = valueNode.getTextContent();

            map.put(name, value);
            i++;
        }
        // no duplicates in message arg names
        Assert.assertEquals(i, map.size());

        final Set<String> set = map.keySet();
        for (String s : set) {
            System.out.println("private static final String " + s + " = \"" + s + "\";");
        }
        System.out.println("\n\n\n");
    }

    /**
     * Gets the document.
     *
     * @param filename the filename
     *
     * @return the document
     */
    private Document getDocument(String filename) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document = null;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(new File(filename));
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        return document;
    }

}
