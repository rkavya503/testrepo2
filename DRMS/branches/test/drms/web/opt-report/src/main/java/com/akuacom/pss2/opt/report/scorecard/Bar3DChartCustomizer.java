package com.akuacom.pss2.opt.report.scorecard;

import java.awt.Color;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.renderer.category.BarRenderer;

import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartCustomizer;

public class Bar3DChartCustomizer implements JRChartCustomizer{
	//DRMS-5891
	@Override
	public void customize(JFreeChart chart, JRChart jasperChart) {
		BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
		renderer.setSeriesPaint(0, Color.CYAN);
		renderer.setSeriesPaint(1, Color.DARK_GRAY);
	}

}
