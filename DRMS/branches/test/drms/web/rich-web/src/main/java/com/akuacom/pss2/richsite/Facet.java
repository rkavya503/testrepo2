
package com.akuacom.pss2.richsite;



/**
 * TODO Class description goes here.
 * @author "Andrey Markavtsov"
 *
 */

public class Facet {
	private String header;
	private String footer;
    private Long id;

	/**
	 * TODO Description goes here.
	 * @param header
	 * @param footer
	 */
	public Facet(String header, String footer) {
	    super();
	    this.header = header;
	    this.footer = footer;
	}
	/**
	 * @return the header
	 */
	public String getHeader() {
	    return header;
	}
	/**
	 * @param header the header to set
	 */
	public void setHeader(String header) {
	    this.header = header;
	}
	/**
	 * @return the footer
	 */
	public String getFooter() {
	    return footer;
	}
	/**
	 * @param footer the footer to set
	 */
	public void setFooter(String footer) {
	    this.footer = footer;
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
