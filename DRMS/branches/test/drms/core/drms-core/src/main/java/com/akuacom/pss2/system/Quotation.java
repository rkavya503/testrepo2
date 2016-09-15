package com.akuacom.pss2.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.hibernate.annotations.Index;

import com.akuacom.ejb.VersionedEntity;

@NamedQueries( {
    @NamedQuery(name = "Quotation.findAll", query = "select distinct o FROM Quotation o"),
    @NamedQuery(name = "Quotation.getAllIds", query = "select distinct o.UUID FROM Quotation o"),
    @NamedQuery(name = "Quotation.count", query = "select count(*) from Quotation o"),
    @NamedQuery(name = "Quotation.findByAuthor", query = "select distinct o FROM Quotation o WHERE o.author LIKE :author"),
    @NamedQuery(name = "Quotation.findByAttribution", query = "select distinct o FROM Quotation o WHERE o.attribution LIKE :attribution"),
    @NamedQuery(name = "Quotation.findByAuthorAndAttribution", query = "select o FROM Quotation o WHERE o.author LIKE :author AND o.attribution LIKE :attribution"),
    @NamedQuery(name = "Quotation.findByAuthorAndBody", query = "select o FROM Quotation o WHERE o.author LIKE :author AND o.body LIKE :body"),
    @NamedQuery(name = "Quotation.findByBody", query = "select o FROM Quotation o WHERE o.body LIKE :body"),
    @NamedQuery(name = "Quotation.findUnattributed", query = "select o FROM Quotation o WHERE o.author is null"),
    @NamedQuery(name = "Quotation.findUnattributedIds", query = "select o.UUID FROM Quotation o WHERE o.author is null") })
@Entity
@org.hibernate.annotations.Entity(mutable = false)
public class Quotation extends VersionedEntity {
    private static final long serialVersionUID = 1L;
    public final static String SER_BIN = "quotations.ser";
    public final static String SER_XML = "quotations.xml";
    public final static String SER_IDS = "quotations.ids";
    public final static String SER = SER_BIN;

    public static Quotation NO_QUOTE = new Quotation("nobody", "nothing");

    @Column(columnDefinition="varchar(500)")
    @Index(name="author_key")
    private String author;
    
    @Column(columnDefinition="varchar(2000)")
    @Index(name="attrib_key")
    private String attribution;

    @Column(columnDefinition="varchar(4000)")
    @Index(name="body_key")
    private String body;
    
    public Quotation() {}
    public Quotation(String author, String body) {
        this.author=author;
        this.body=body;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAttribution() {
        return attribution;
    }

    public void setAttribution(String attribution) {
        this.attribution = attribution;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
 
    public static boolean nullEmptyQ(String s) {
        return s == null || "".equals(s);
    }

    @Override
    public String toString() {
        return body + " -- " + (!nullEmptyQ(attribution) ? attribution + " -- " : "") + author;  
    }
    
    public String toHtml() {
        return body + "<br/> -- " + (!nullEmptyQ(attribution) ? attribution + " -- " : "") + author + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"/adm/quotations.jsp\">search</a>";
    }
    
}
