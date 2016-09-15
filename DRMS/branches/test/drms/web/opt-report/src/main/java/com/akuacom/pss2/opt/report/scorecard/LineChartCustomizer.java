package com.akuacom.pss2.opt.report.scorecard;

import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRChartCustomizer;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

public class LineChartCustomizer implements JRChartCustomizer{

	@Override
	public void customize(JFreeChart chart, JRChart jasperChart) {
		
		XYPlot plot = (XYPlot) chart.getPlot();
		XYLineAndShapeRenderer render = (XYLineAndShapeRenderer) plot.getRenderer();
		plot.setRenderer(new ContinuousLineRender(render.getBaseLinesVisible(),render.getBaseShapesVisible()));
		
	}

}
