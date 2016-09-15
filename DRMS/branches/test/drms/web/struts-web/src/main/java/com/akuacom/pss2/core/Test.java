/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.Test.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.core;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;

/**
 * The Class Test.
 */
public class Test {
    
    /**
     * The main method.
     * 
     * @param args the arguments
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void main(String[] args) throws IOException {
        final long start = System.currentTimeMillis();
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < 10; i++) {
            dataset.addValue(i, "Y category", "x : " + i);
        }
        JFreeChart chart = ChartFactory.createLineChart(
                "Test",
                "X axis?",
                "Y axis?",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                false,
                false
        );
        final BufferedImage image = chart.createBufferedImage(800, 600, null);
        ImageIO.write(image, "jpg", new FileOutputStream("Test.jpg"));
        final long l = System.currentTimeMillis() - start;
        System.out.println(NumberFormat.getInstance().format(l) + " ms");
    }
}
