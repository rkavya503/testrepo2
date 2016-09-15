package com.akuacom.jsf.renderkit;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.context.AjaxContext;
import org.ajax4jsf.event.AjaxEvent;
import org.ajax4jsf.renderkit.AjaxRendererUtils;
import org.ajax4jsf.renderkit.ComponentVariables;
import org.ajax4jsf.renderkit.ComponentsVariableResolver;
import org.ajax4jsf.resource.InternetResource;
import org.apache.log4j.Logger;
import org.richfaces.component.UIDatascroller;
import org.richfaces.event.DataScrollerEvent;
import org.richfaces.renderkit.html.DatascrollerTemplate;

public class AccessibleDatascrollerRenderer extends DatascrollerTemplate {

	private static final Logger log = Logger.getLogger(AccessibleDatascrollerRenderer.class);
	public final static String DATASCROLLER_CLICKED_ELEMENT_ID="DATASCROLLER_CLICKED_ELEMENT_ID";
	

	public AccessibleDatascrollerRenderer() {
		super();
	}

	// 
	// Declarations
	//
	private final InternetResource[] styles = { getResource("css/datascroller.xcss") };

	private InternetResource[] stylesAll = null;

	protected InternetResource[] getStyles() {
		synchronized (this) {
			if (stylesAll == null) {
				InternetResource[] rsrcs = super.getStyles();
				boolean ignoreSuper = rsrcs == null || rsrcs.length == 0;
				boolean ignoreThis = styles == null || styles.length == 0;

				if (ignoreSuper) {
					if (ignoreThis) {
						stylesAll = new InternetResource[0];
					} else {
						stylesAll = styles;
					}
				} else {
					if (ignoreThis) {
						stylesAll = rsrcs;
					} else {
						java.util.Set rsrcsSet = new java.util.LinkedHashSet();

						for (int i = 0; i < rsrcs.length; i++) {
							rsrcsSet.add(rsrcs[i]);
						}

						for (int i = 0; i < styles.length; i++) {
							rsrcsSet.add(styles[i]);
						}

						stylesAll = (InternetResource[]) rsrcsSet
								.toArray(new InternetResource[rsrcsSet.size()]);
					}
				}
			}
		}

		return stylesAll;
	}

	private final InternetResource[] scripts = {
			new org.ajax4jsf.javascript.PrototypeScript(),
			new org.ajax4jsf.javascript.AjaxScript(),
			getResource("/org/richfaces/renderkit/html/scripts/datascroller.js") };

	private InternetResource[] scriptsAll = null;

	protected InternetResource[] getScripts() {
		synchronized (this) {
			if (scriptsAll == null) {
				InternetResource[] rsrcs = super.getScripts();
				boolean ignoreSuper = rsrcs == null || rsrcs.length == 0;
				boolean ignoreThis = scripts == null || scripts.length == 0;

				if (ignoreSuper) {
					if (ignoreThis) {
						scriptsAll = new InternetResource[0];
					} else {
						scriptsAll = scripts;
					}
				} else {
					if (ignoreThis) {
						scriptsAll = rsrcs;
					} else {
						java.util.Set rsrcsSet = new java.util.LinkedHashSet();

						for (int i = 0; i < rsrcs.length; i++) {
							rsrcsSet.add(rsrcs[i]);
						}

						for (int i = 0; i < scripts.length; i++) {
							rsrcsSet.add(scripts[i]);
						}

						scriptsAll = (InternetResource[]) rsrcsSet
								.toArray(new InternetResource[rsrcsSet.size()]);
					}
				}
			}
		}

		return scriptsAll;
	}

	// 
	// 
	//

	private String convertToString(Object obj) {
		return (obj == null ? "" : obj.toString());
	}

	private String convertToString(boolean b) {
		return String.valueOf(b);
	}

	private String convertToString(int b) {
		return b != Integer.MIN_VALUE ? String.valueOf(b) : "";
	}

	private String convertToString(long b) {
		return b != Long.MIN_VALUE ? String.valueOf(b) : "";
	}

	/**
	 * Get base component class, targetted for this renderer. Used for check
	 * arguments in decode/encode.
	 * 
	 * @return
	 */
	protected Class getComponentClass() {
		return org.richfaces.component.UIDatascroller.class;
	}

	@Override
	public void doEncodeEnd(ResponseWriter writer, FacesContext context,
			org.richfaces.component.UIDatascroller component,
			ComponentVariables variables) throws IOException {

		java.lang.String clientId = component.getClientId(context);

		org.richfaces.component.util.FormUtil.throwEnclFormReqExceptionIfNeed(
				context, component);

		java.lang.String singlePageRenderStyle = "";

		int maxPages = component.getMaxPages();
		if (maxPages <= 1) {
			maxPages = 1;
		}

		int pageCount = component.getPageCount();
		int pageIndex = component.getPage();

		org.richfaces.renderkit.html.ControlsState controlsState = getControlsState(
				context, component, pageIndex, pageCount);
		boolean singlePageRender = true;

		if (pageCount == 1 && !component.isRenderIfSinglePage()) {
			singlePageRenderStyle = "; display: none";
			singlePageRender = false;
		} else if (!controlsState.isFirstRendered()
				&& !controlsState.isFastRewindRendered()
				&& !controlsState.isPreviousRendered()
				&& !controlsState.isNextRendered()
				&& !controlsState.isFastForwardRendered()
				&& !controlsState.isLastRendered() && pageCount <= 1) {
			singlePageRenderStyle = "; display: none";
			singlePageRender = false;
		}

		writer.startElement("div", component);
		getUtils().writeAttribute(
				writer,
				"class",
				"rich-datascr "
						+ convertToString(component.getAttributes().get(
								"styleClass")));
		getUtils().writeAttribute(writer, "id", clientId);
		getUtils().writeAttribute(
				writer,
				"style",
				convertToString(component.getAttributes().get("style")) + " "
						+ convertToString(singlePageRenderStyle));
		//
		// pass thru attributes
		//
		getUtils().encodeAttributesFromArray(
				context,
				component,
				new String[] { "align", "dir", "lang", "onclick", "ondblclick",
						"onkeydown", "onkeypress", "onkeyup", "onmousedown",
						"onmousemove", "onmouseout", "onmouseover",
						"onmouseup", "title", "xml:lang" });
		//
		//
		//

		if (singlePageRender) {
			renderPages(context, component, pageIndex, pageCount);

			writer.startElement("table", component);
			getUtils().writeAttribute(writer, "border", "0");
			getUtils().writeAttribute(writer, "cellpadding", "0");
			getUtils().writeAttribute(writer, "cellspacing", "1");
			getUtils().writeAttribute(
					writer,
					"class",
					"rich-dtascroller-table "
							+ convertToString(component.getAttributes().get(
									"tableStyleClass")));
			getUtils().writeAttribute(writer, "id",
					convertToString(clientId) + "_table");
			getUtils().writeAttribute(
					writer,
					"style",
					"text-align:"
							+ convertToString(component.getAttributes().get(
									"align")));

			writer.startElement("tbody", component);

			writer.startElement("tr", component);

			String facet;

			if (controlsState.isFirstRendered()) {
				if (controlsState.isFirstEnabled()) {
					variables.setVariable("buttonClass", "");
					variables.setVariable("onclick",
							getOnClick(component.FIRST_FACET_NAME));
					variables.setVariable("facet", component.FIRST_FACET_NAME);
					facet = component.FIRST_FACET_NAME;
				} else {
					variables.setVariable("buttonClass",
							"rich-datascr-button-dsbld");
					variables.setVariable("onclick", "");
					variables.setVariable("facet",
							component.FIRST_DISABLED_FACET_NAME);
					facet = component.FIRST_DISABLED_FACET_NAME;
				}
				;
				if (component.getFacet(facet) != null) {

					writer.startElement("td", component);
					getUtils().writeAttribute(
							writer,
							"class",
							convertToString(variables
									.getVariable("buttonClass"))
									+ " rich-datascr-button");
					getUtils().writeAttribute(writer, "onclick",
							variables.getVariable("onclick"));

					if (component.FIRST_FACET_NAME.equals(facet)) {

						UIComponent indexChildren_11 = component
								.getFacet("first");
						if (null != indexChildren_11
								&& indexChildren_11.isRendered()) {
							renderChild(context, indexChildren_11);
						}

					} else {

						UIComponent indexChildren_12 = component
								.getFacet("first_disabled");
						if (null != indexChildren_12
								&& indexChildren_12.isRendered()) {
							renderChild(context, indexChildren_12);
						}

					}

					writer.endElement("td");

				} else {

					writer.startElement("td", component);
					getUtils().writeAttribute(
							writer,
							"class",
							convertToString(variables
									.getVariable("buttonClass"))
									+ " rich-datascr-button");
					getUtils().writeAttribute(writer, "onclick",
							variables.getVariable("onclick"));

					// Enable Tab on page scroll link.
					if (controlsState.isFirstEnabled()) {
						writer.write("<a href='javascript:{}' title='First Page' " 
								//+ "onclick='if(this.parentElement.onclick) this.parentElement.onclick();' " 
								+ "id='" + component.getClientId(context) + "_first'>First</a>");

					String id=(String) context.getExternalContext().getSessionMap().get(DATASCROLLER_CLICKED_ELEMENT_ID);
					if(id != null){
						writer.write("<script>");
						writer.write("	var f=document.getElementById(\""+ id +"\"); if(f) focus();");
						writer.write("</script>");
					}

					} else {
						writer.write("First");
					}

					writer.endElement("td");

				}
				if (controlsState.isControlsSeparatorRendered()
						&& (controlsState.isFastRewindRendered() || controlsState
								.isPreviousRendered())) {

					writer.startElement("td", component);
					getUtils().writeAttribute(writer, "class",
							"rich-datascr-ctrls-separator");

					UIComponent indexChildren_13 = component
							.getFacet("controlsSeparator");
					if (null != indexChildren_13
							&& indexChildren_13.isRendered()) {
						renderChild(context, indexChildren_13);
					}

					writer.endElement("td");

				}
			}

			if (controlsState.isPreviousRendered()) {
				if (controlsState.isPreviousEnabled()) {
					variables.setVariable("buttonClass", "");
					variables.setVariable("onclick",
							getOnClick(component.PREVIOUS_FACET_NAME));
					variables.setVariable("facet",
							component.PREVIOUS_FACET_NAME);
					facet = component.PREVIOUS_FACET_NAME;
				} else {
					variables.setVariable("onclick", "");
					variables.setVariable("buttonClass",
							"rich-datascr-button-dsbld");
					variables.setVariable("facet",
							component.PREVIOUS_DISABLED_FACET_NAME);
					facet = component.PREVIOUS_DISABLED_FACET_NAME;
				}
				if (component.getFacet(facet) != null) {

					writer.startElement("td", component);
					getUtils().writeAttribute(
							writer,
							"class",
							convertToString(variables
									.getVariable("buttonClass"))
									+ " rich-datascr-button");
					getUtils().writeAttribute(writer, "onclick",
							variables.getVariable("onclick"));

					if (component.PREVIOUS_FACET_NAME.equals(facet)) {

						UIComponent indexChildren_17 = component
								.getFacet("previous");
						if (null != indexChildren_17
								&& indexChildren_17.isRendered()) {
							renderChild(context, indexChildren_17);
						}

					} else {

						UIComponent indexChildren_18 = component
								.getFacet("previous_disabled");
						if (null != indexChildren_18
								&& indexChildren_18.isRendered()) {
							renderChild(context, indexChildren_18);
						}

					}

					writer.endElement("td");

				} else {

					writer.startElement("td", component);
					getUtils().writeAttribute(
							writer,
							"class",
							convertToString(variables
									.getVariable("buttonClass"))
									+ " rich-datascr-button");
					getUtils().writeAttribute(writer, "onclick",
							variables.getVariable("onclick"));

					
					// Enable Tab on page scroll link.
					if (controlsState.isPreviousEnabled()) {
						
						writer.write("<a href='javascript:{}' title='Previous Page' " 
								//+ "onclick='if(this.parentElement.onclick) this.parentElement.onclick();' " 
								+ "id='" + component.getClientId(context) + "_previous'>Previous</a>");

						String id=(String) context.getExternalContext().getSessionMap().get(DATASCROLLER_CLICKED_ELEMENT_ID);
						if(id != null){
							writer.write("<script>");
							writer.write("var f=document.getElementById(\""+ id +"\"); if(f) focus();");
							writer.write("</script>");
						}

					} else {
						writer.write("Previous");
					}

					writer.endElement("td");
					
				}
			}

			UIComponent pagesFacet = component.getFacet("pages");
			if (pagesFacet != null && pagesFacet.isRendered()) {

				writer.startElement("td", component);

				renderChild(context, pagesFacet);

				writer.endElement("td");

			} else {
				renderPager(context, component, pageIndex, pageCount);
			}

			if (controlsState.isNextRendered()) {
				if (controlsState.isNextEnabled()) {
					variables.setVariable("onclick",
							getOnClick(component.NEXT_FACET_NAME));
					variables.setVariable("buttonClass", "");
					variables.setVariable("facet", component.NEXT_FACET_NAME);
					facet = component.NEXT_FACET_NAME;
				} else {
					variables.setVariable("onclick", "");
					variables.setVariable("buttonClass",
							"rich-datascr-button-dsbld");
					variables.setVariable("facet",
							component.NEXT_DISABLED_FACET_NAME);
					facet = component.NEXT_DISABLED_FACET_NAME;
				}
				if (component.getFacet(facet) != null) {

					writer.startElement("td", component);
					getUtils().writeAttribute(
							writer,
							"class",
							convertToString(variables
									.getVariable("buttonClass"))
									+ " rich-datascr-button");
					getUtils().writeAttribute(writer, "onclick",
							variables.getVariable("onclick"));

					if (component.NEXT_FACET_NAME.equals(facet)) {

						UIComponent indexChildren_19 = component
								.getFacet("next");
						if (null != indexChildren_19
								&& indexChildren_19.isRendered()) {
							renderChild(context, indexChildren_19);
						}

					} else {

						UIComponent indexChildren_20 = component
								.getFacet("next_disabled");
						if (null != indexChildren_20
								&& indexChildren_20.isRendered()) {
							renderChild(context, indexChildren_20);
						}

					}

					writer.endElement("td");

				} else {

					writer.startElement("td", component);
					getUtils().writeAttribute(
							writer,
							"class",
							convertToString(variables
									.getVariable("buttonClass"))
									+ " rich-datascr-button");
					getUtils().writeAttribute(writer, "onclick",
							variables.getVariable("onclick"));

					
					// Enable Tab on page scroll link.
					if (controlsState.isNextEnabled()) {
						
						writer.write("<a href='javascript:{void(0)}' title='Next Page' " 
								//+ "onclick='if(this.parentElement.onclick) this.parentElement.onclick();' " 
								+ "id='" + component.getClientId(context) + "_next'>Next</a>");

						String id=(String) context.getExternalContext().getSessionMap().get(DATASCROLLER_CLICKED_ELEMENT_ID);
						if(id != null){
							writer.write("<script>");
							writer.write("	var f=document.getElementById(\""+ id +"\"); if(f) focus();");
							writer.write("</script>");
						}

					} else {
						writer.write("Next");
					}

					writer.endElement("td");

				}

				if (controlsState.isControlsSeparatorRendered()
						&& (controlsState.isFastForwardRendered() || controlsState
								.isLastRendered())) {

					writer.startElement("td", component);
					getUtils().writeAttribute(writer, "class",
							"rich-datascr-ctrls-separator");

					UIComponent indexChildren_21 = component
							.getFacet("controlsSeparator");
					if (null != indexChildren_21
							&& indexChildren_21.isRendered()) {
						renderChild(context, indexChildren_21);
					}

					writer.endElement("td");

				}
			}


			if (controlsState.isLastRendered()) {

				if (controlsState.isLastEnabled()) {
					variables.setVariable("onclick",
							getOnClick(component.LAST_FACET_NAME));
					variables.setVariable("buttonClass", "");
					variables.setVariable("facet", component.LAST_FACET_NAME);
					facet = component.LAST_FACET_NAME;
				} else {
					variables.setVariable("onclick", "");
					variables.setVariable("buttonClass",
							"rich-datascr-button-dsbld");
					variables.setVariable("facet",
							component.LAST_DISABLED_FACET_NAME);
					facet = component.LAST_DISABLED_FACET_NAME;
				}
				if (component.getFacet(facet) != null) {

					writer.startElement("td", component);
					getUtils().writeAttribute(
							writer,
							"class",
							convertToString(variables
									.getVariable("buttonClass"))
									+ " rich-datascr-button");
					getUtils().writeAttribute(writer, "onclick",
							variables.getVariable("onclick"));

					if (component.LAST_FACET_NAME.equals(facet)) {

						UIComponent indexChildren_25 = component
								.getFacet("last");
						if (null != indexChildren_25
								&& indexChildren_25.isRendered()) {
							renderChild(context, indexChildren_25);
						}

					} else {

						UIComponent indexChildren_26 = component
								.getFacet("last_disabled");
						if (null != indexChildren_26
								&& indexChildren_26.isRendered()) {
							renderChild(context, indexChildren_26);
						}

					}

					writer.endElement("td");

				} else {

					writer.startElement("td", component);
					getUtils().writeAttribute(
							writer,
							"class",
							convertToString(variables
									.getVariable("buttonClass"))
									+ " rich-datascr-button");
					getUtils().writeAttribute(writer, "onclick",
							variables.getVariable("onclick"));

					// Enable Tab on page scroll link.
					if (controlsState.isLastEnabled()) {
						//writer.write("<a href='javascript:{}' title='Last Page' onclick='if(this.parentElement.onclick) this.parentElement.onclick();'>Last</a>");
						writer.write("<a href='javascript:{}' title='Last Page' " 
								//+ "onclick='if(this.parentElement.onclick) this.parentElement.onclick();' " 
								+ "id='" + component.getClientId(context) + "_last'>Last</a>");

						String id=(String) context.getExternalContext().getSessionMap().get(DATASCROLLER_CLICKED_ELEMENT_ID);
						if(id != null){
							writer.write("<script>");
							writer.write("	var f=document.getElementById(\""+ id +"\"); if(f) focus();");
							writer.write("</script>");
						}
						
					} else {
						writer.write("Last");
					}

					writer.endElement("td");
				}
			}

			writer.endElement("tr");
			writer.endElement("tbody");
			writer.endElement("table");

		}

		writer.startElement("script", component);
		getUtils().writeAttribute(writer, "type", "text/javascript");

		writer
				.writeText(
						convertToString("new Richfaces.Datascroller('"
								+ convertToString(clientId)
								+ "', "
								+ convertToString(getSubmitFunction(context,
										component)) + ");"), null);

		writer.endElement("script");
		writer.endElement("div");

	}

	public void doEncodeEnd(ResponseWriter writer, FacesContext context,
			UIComponent component) throws IOException {
		ComponentVariables variables = ComponentsVariableResolver.getVariables(
				this, component);
		doEncodeEnd(writer, context,
				(org.richfaces.component.UIDatascroller) component, variables);

		ComponentsVariableResolver.removeVariables(this, component);
	}

	@Override
	public void renderPager(FacesContext context, UIComponent component,
			int pageIndex, int count) throws IOException {
		ResponseWriter out = context.getResponseWriter();
		UIDatascroller scroller = (UIDatascroller) component;
		int currentPage = pageIndex;

		int maxPages = scroller.getMaxPages();
		if (maxPages <= 1) {
			maxPages = 1;
		}

		int pageCount = count;
		if (pageCount <= 1) {
			return;
		}

		int delta = maxPages / 2;

		int pages;
		int start;
		if (pageCount > maxPages && currentPage > delta) {
			pages = maxPages;
			start = currentPage - pages / 2 - 1;
			if (start + pages > pageCount) {
				start = pageCount - pages;
			}
		} else {
			pages = pageCount < maxPages ? pageCount : maxPages;
			start = 0;
		}

		for (int i = start, size = start + pages; i < size; i++) {

			boolean isCurrentPage = (i + 1 == currentPage);
			String styleClass;
			String style;
			if (isCurrentPage) {
				styleClass = scroller.getSelectedStyleClass();
				style = scroller.getSelectedStyle();
			} else {
				styleClass = scroller.getInactiveStyleClass();
				style = scroller.getInactiveStyle();
			}
			if (styleClass == null) {
				styleClass = "";
			}

			out.startElement("td", component);

			if (isCurrentPage) {
				out.writeAttribute("class", "rich-datascr-act " + styleClass,
						null);
			} else {
				out.writeAttribute("class", "rich-datascr-inact " + styleClass,
						null);
				out.writeAttribute("onclick", getOnClick(Integer
						.toString(i + 1)), null);
			}
			if (null != style)
				out.writeAttribute("style", style, null);

			
			out.write("<a href='javascript:{}' " 
						//+ "onclick='if(this.parentElement.onclick) this.parentElement.onclick();'" 
						+ "id='" + component.getClientId(context) + "_"	+ (i+1)	+ "' title='Go to page "+(i+1) +"'>" + Integer.toString(i + 1) + "</a>");

			String id=(String) context.getExternalContext().getSessionMap().get(DATASCROLLER_CLICKED_ELEMENT_ID);
			if(id != null){
				out.write("<script>");
				out.write("	var f=document.getElementById(\""+ id +"\"); if(f) focus();");
				out.write("</script>");
			}			
			out.endElement("td");
		}

	}

	@Override
	public void doDecode(FacesContext context, UIComponent component) {
		Map paramMap = getParamMap(context);
		String clientId = component.getClientId(context);
		String param = (String) paramMap.get(clientId);
		if (param != null) {
			UIDatascroller scroller = (UIDatascroller) component;
			int newPage = scroller.getPageForFacet(param);
			int page = scroller.getPage();
			if (newPage != 0 && newPage != page) {
				DataScrollerEvent event = new DataScrollerEvent(scroller, String.valueOf(page), param, newPage);
				event.queue();

			}

			if (AjaxRendererUtils.isAjaxRequest(context)) {
				AjaxContext.getCurrentInstance(context).addAreasToProcessFromComponent(context, component);
			}
			context.getExternalContext().getSessionMap().put(DATASCROLLER_CLICKED_ELEMENT_ID, clientId + "_" + param);
			new AjaxEvent(component).queue();
		}
	}

	private Map getParamMap(FacesContext context) {
		return context.getExternalContext().getRequestParameterMap();
	}

}
