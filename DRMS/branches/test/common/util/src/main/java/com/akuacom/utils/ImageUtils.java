/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.utils.ImageUtils.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * The Class ImageUtils.
 */
public class ImageUtils
{
	
	/**
	 * Instantiates a new image utils.
	 */
	public ImageUtils()
	{
	}

	/**
	 * Write jpeg image.
	 * 
	 * @param file the file
	 * @param bi the bi
	 */
	public static void writeJPEGImage(File file, BufferedImage bi)
    {
	    // write the map image
	    byte[] image = null;       
	    try {
	        ByteArrayOutputStream encodeOut = new ByteArrayOutputStream();
	        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(encodeOut);
	        encoder.encode(bi);
	        image = encodeOut.toByteArray();
	        encodeOut.close();
	
	        DataOutputStream out = 
	          new DataOutputStream(new FileOutputStream(file));
	        out.write(image);
	        out.close();            
	    } 
	    catch (Exception e) 
	    {
	        e.printStackTrace(); 
	    }
    }

}
