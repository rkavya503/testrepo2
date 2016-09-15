<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<%@taglib prefix="rich" uri="http://richfaces.org/rich"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<f:view>
	<head>
<meta http-equiv="refresh" content="60" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Demand Limiting Dashboard - Scorecard for <h:outputText
		value="#{demandLimitingDashboard.participantName}" /> - ©2011.
	Honeywell International Inc.</title>
<!--[if lt IE 9]><script language="javascript" type="text/javascript" src="resources/jqplot/excanvas.js"></script><![endif]-->

<link rel="stylesheet" type="text/css"
	href="resources/jqplot/jquery.jqplot.css" />

<script language="javascript" type="text/javascript"
	src="resources/jquery/jquery-1.6.3.js"></script>
<script type="text/javascript" src="resources/interface/interface.js"></script>
<!--[if lt IE 7]>
            <style type="text/css">
            .dock img { behavior: url(resources/images/iepngfix.htc) }
            </style>
           <![endif]-->
<link href="resources/dock/style.css" rel="stylesheet" type="text/css" />

<script language="javascript" type="text/javascript"
	src="resources/jqplot/jquery.jqplot.js"></script>
<script language="javascript" type="text/javascript"
	src="resources/jqplot/plugins/jqplot.meterGaugeRenderer.js"></script>
<script language="javascript" type="text/javascript"
	src="resources/jqplot/plugins/jqplot.highlighter.js"></script>


<style type="text/css">
.plot {
	margin-bottom: 30px;
	margin-left: auto;
	margin-right: auto;
}

#chart5 .jqplot-meterGauge-tick,#chart5 .jqplot-meterGauge-label {
	font-size: 9pt;
}
</style>


<script type="text/javascript">
	jQuery.noConflict();
	jQuery(document).ready(function() {
		jQuery('#dock2').Fisheye({
			maxWidth : 60,
			items : 'a',
			itemsText : 'span',
			container : '.dock-container2',
			itemWidth : 40,
			proximity : 80,
			alignment : 'left',
			valign : 'bottom',
			halign : 'center'
		})
	});
</script>

<script src="resources/badge/script.js" type="text/javascript"
	charset="utf-8"></script>

<link rel="stylesheet" href="resources/badge/style.css" type="text/css"
	media="screen" charset="utf-8">
<link rel="stylesheet" href="resources/badge/style-transitions.css"
	type="text/css" media="screen" charset="utf-8">

	</head>
	<body>

		<h:form id="dashboardForm">
			<rich:panel>
				<h:inputHidden id="thresholdPercentage"
					value="#{demandLimitingDashboard.demandLimitingProgramParticipantState.thresholdPercentage}" />
				<h:inputHidden id="tariffLimit"
					value="#{demandLimitingDashboard.demandLimitingProgramParticipantState.tariffLimit}" />
				<h:inputHidden id="normalThresholdPercentage"
					value="#{demandLimitingDashboard.demandLimitingProgramParticipantState.normalThresholdPercentage}" />
				<h:inputHidden id="warningThresholdPercentage"
					value="#{demandLimitingDashboard.demandLimitingProgramParticipantState.warningThresholdPercentage}" />
				<h:inputHidden id="moderateThresholdPercentage"
					value="#{demandLimitingDashboard.demandLimitingProgramParticipantState.moderateThresholdPercentage}" />
				<h:inputHidden id="highThresholdPercentage"
					value="#{demandLimitingDashboard.demandLimitingProgramParticipantState.highThresholdPercentage}" />
				<h:inputHidden id="exceededThresholdPercentage"
					value="#{demandLimitingDashboard.demandLimitingProgramParticipantState.exceededThresholdPercentage}" />
				<h:inputHidden id="intervalLoadColor"
					value="#{demandLimitingDashboard.intervalLoadColor}" />
				<h:inputHidden id="meterMaxPercentage"
					value="#{demandLimitingDashboard.meterMaxPercentage}" />

				<div id="chart5" class="plot" style="width: 700px; height: 450px;"></div>

				<script type="text/javascript">
					jQuery.noConflict();
					var plot5;
					jQuery(document)
							.ready(
									function() {
										s1 = [ <h:outputText
												value="#{demandLimitingDashboard.demandLimitingProgramParticipantState.thresholdPercentage}" /> ];
										plot5 = jQuery
												.jqplot(
														'chart5',
														[ s1 ],
														{
															seriesDefaults : {
																renderer : jQuery.jqplot.MeterGaugeRenderer,
																rendererOptions : {
																	label : 'Interval Load Threshold Percentage',
																	labelPosition : 'bottom',
																	intervalOuterRadius : 150,
																	ticks : [
																			0,
																			parseFloat(
																					jQuery(
																							'#dashboardForm\\:meterMaxPercentage')
																							.val() * .25)
																					.toFixed(
																							2),
																			parseFloat(
																					jQuery(
																							'#dashboardForm\\:meterMaxPercentage')
																							.val() * .5)
																					.toFixed(
																							2),
																			parseFloat(
																					jQuery(
																							'#dashboardForm\\:meterMaxPercentage')
																							.val() * .75)
																					.toFixed(
																							2),
																			parseFloat(
																					jQuery(
																							'#dashboardForm\\:meterMaxPercentage')
																							.val())
																					.toFixed(
																							2) ],
																	intervals : [ parseFloat(
																			jQuery(
																					'#dashboardForm\\:thresholdPercentage')
																					.val())
																			.toFixed(
																					2) ],
																	intervalColors : [ jQuery(
																			'#dashboardForm\\:intervalLoadColor')
																			.val() ]
																}
															},
															legend : {
																show : false
															},
															highlighter : {
																showTooltip : true
															}
														});
									});
				</script>


				<div id="badge_container">
					<div id="badge">
						<h1 id="badge_text">
							<h:outputText
								value="#{demandLimitingDashboard.demandLimitingProgramParticipantState.thresholdPercentage}" />
							%
						</h1>
					</div>
					<div id="badge_shadow"></div>
				</div>

				<div id="sticky_container">
					<div id="sticky">
						Your load is currently at
						<h:outputText
							value="#{demandLimitingDashboard.currentThresholdStr}" />
						threshold
					</div>
				</div>

			</rich:panel>
		</h:form>

		<div class="dock" id="dock2">
			<div class="dock-container2">
				<a class="dock-item2" href="demandLimitingDashboard.jsf"><span>Monitor Load kW</span><img
					src="resources/images/home.png" alt="Home" /> </a> <a
					class="dock-item2" href="demandLimitingDashboardScorecard.jsf"><span>Monitor Threshold %</span><img
					src="resources/images/portfolio.png" alt="Scorecard" /> </a><a
					class="dock-item2" href="../programs.jsf"><span>Exit</span><img
					src="resources/images/link.png" alt="Exit" /> </a>
			</div>
		</div>

	</body>
</f:view>
