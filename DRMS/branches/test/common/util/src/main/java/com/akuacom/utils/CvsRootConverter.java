/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.utils.CvsRootConverter.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.utils;

import java.util.LinkedList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The Class CvsRootConverter.
 */
public class CvsRootConverter {
    
    /** The method. */
    private String method = "pserver";
    
    /** The user. */
    private String user = "root";
    
    /** The port. */
    private String port = "2401";
    
    /** The host. */
    private String host = "localhost";
    
    /** The repository. */
    private String repository = "/";

    /**
     * Gets the host.
     * 
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * Sets the host.
     * 
     * @param host the new host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Gets the method.
     * 
     * @return the method
     */
    public String getMethod() {
        return method;
    }

    /**
     * Sets the method.
     * 
     * @param method the new method
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * Gets the port.
     * 
     * @return the port
     */
    public String getPort() {
        return port;
    }

    /**
     * Sets the port.
     * 
     * @param port the new port
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * Gets the repository.
     * 
     * @return the repository
     */
    public String getRepository() {
        return repository;
    }

    /**
     * Sets the repository.
     * 
     * @param repository the new repository
     */
    public void setRepository(String repository) {
        this.repository = repository;
    }

    /**
     * Gets the user.
     * 
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * Sets the user.
     * 
     * @param user the new user
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Gets the cvs root string.
     * 
     * @return the cvs root string
     */
    public String getCvsRootString() {
        return ":" + method + ":" + user + "@" + host +":" + port + repository + "\n";
    }

    /**
     * The main method.
     * 
     * @param arg the arguments
     */
    public static void main(String[] arg) {
        CvsRootConverter c = new CvsRootConverter();
        c.setHost("192.168.149.52");
        c.setRepository("/var/lib/cvsroot");
        c.setUser("dichen");
        final String s = c.getCvsRootString();
        System.out.println(s);

        LinkedList<File> list = new LinkedList<File>();
        File dir = new File("d:/java/pss2-100113");
        File file[] = dir.listFiles();
        for (File aFile : file) {
            if (aFile.isDirectory()) {
                list.add(aFile);
            } else {
                writeFile(aFile, s);
            }
        }
        File tmp;
        while (!list.isEmpty()) {
            tmp = list.removeFirst();
            if (tmp.isDirectory()) {
                file = tmp.listFiles();
                if (file == null) continue;
                for (File aFile : file) {
                    if (aFile.isDirectory()) {
                        list.add(aFile);
                    } else {
                        writeFile(aFile, s);
                    }
                }
            } else {
                writeFile(tmp, s);
            }
        }
    }

    /**
     * Write file.
     * 
     * @param file the file
     * @param cvsRootString the cvs root string
     */
    private static void writeFile(File file, String cvsRootString) {
        try {
            if (file.getName().equals("Root")) {
                FileWriter fw = new FileWriter(file);
                fw.write(cvsRootString);
                fw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
