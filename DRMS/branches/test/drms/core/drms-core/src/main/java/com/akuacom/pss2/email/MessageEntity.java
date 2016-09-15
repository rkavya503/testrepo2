/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.email.MessageVO.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.email;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.akuacom.ejb.BaseEntity;

/**
 * The Class MessageEntity.
 */
@NamedQueries({
    @NamedQuery(
            name  = "MessageEntity.findByUserName",
            query = "select m from MessageEntity m where m.userName = :userName",
            hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
    @NamedQuery(
            name  = "MessageEntity.findByDestination",
            query = "select m from MessageEntity m where UPPER(m.to) like:to",
            hints={@QueryHint(name="org.hibernate.cacheable", value="false")}),
    @NamedQuery(
            name  = "MessageEntity.findAll",
            query = "select m from MessageEntity m order by m.subject",
            hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
    @NamedQuery(
    		name  = "MessageEntity.getSimilarMsgCount",
    		query = "select count(m) from MessageEntity m " +
    				"where m.subject =:subject and m.status = :status and UPPER(m.to) like:to and m.sentTime >=:sentTime ",
            hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
    @NamedQuery(
    		name  = "MessageEntity.getSimilarMsgCountBelowPriority",
    	    query = "select count(m) from MessageEntity m " +
    			    "where m.subject =:subject and m.status = :status and UPPER(m.to) like :to " +
    			    "and m.sentTime >=:sentTime and m.priority<=:priority ",
            hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),   	
   @NamedQuery(
		    name  = "MessageEntity.getMsgCountAtPriority",
    	    query = "select count(m) from MessageEntity m " +
    			   "where  UPPER(m.to) like :to " +
    			   "and m.creationTime >=:sentTime and m.priority=:priority ",
            hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),  
   @NamedQuery(
		   name  = "MessageEntity.getMsgCountToContact",
           query = "select count(m) from MessageEntity m " +
            	   "where  UPPER(m.to) like :to " +
            	    "and m.creationTime >=:sentTime and m.priority=:priority " +
            	    "and UPPER(m.contactId)=:contact",
          hints={@QueryHint(name="org.hibernate.cacheable", value="true")}), 
    @NamedQuery(
    	    name  = "MessageEntity.clearMessage",
    	    query = "delete from MessageEntity m where m.creationTime <:sentTime",
            hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
    	    		
    @NamedQuery(
    	    name  = "MessageEntity.findBySubject",
    	    query = "select m from MessageEntity m where m.subject =:subject",
            hints={@QueryHint(name="org.hibernate.cacheable", value="true")}),
            	    		
    @NamedQuery(
    	    name  = "MessageEntity.findByDateRange",
    	    query = "select m from MessageEntity m where m.creationTime >= :start and m.creationTime <= :end order by m.creationTime",
            hints={@QueryHint(name="org.hibernate.cacheable", value="false")}),
    @NamedQuery(
            	    name  = "MessageEntity.findDigestMessage",
            	    query = "select m from MessageEntity m where m.creationTime >= :start and m.creationTime <= :end and m.to in (:operators) and m.userName = 'digester' order by m.creationTime",
                    hints={@QueryHint(name="org.hibernate.cacheable", value="false")}),            
    @NamedQuery(
            name  = "MessageEntity.findByDigest",
            	    query = "select m from MessageEntity m where m.creationTime >= :start and subject like '%Client Test event%' and m.creationTime <= :end and m.to = :to and m.userName = :userName order by m.creationTime",
                    hints={@QueryHint(name="org.hibernate.cacheable", value="false")})
})
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name="message")
@Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
@Inheritance(strategy= InheritanceType.TABLE_PER_CLASS)

public class MessageEntity extends BaseEntity implements Serializable{
   
	private static final long serialVersionUID = 2484918470058927641L;
	
	//message status
	public final static int STS_NEW       = 0;
	public final static int STS_SENT      = 1;
	public final static int STS_SUSPENDED = 2;
	public final static int STS_FAILED    = 3;
	public final static int STS_IGNORED   = 4;
	
	//message priority
	public final static int PRIORITY_NORMAL = 10;
	public final static int PRIORITY_URGENT = 20;
	
    /** The from. */
    @Column(name = "`from`")  // backticks necessary; 'from' is keyword 
    private String from;
    
    /** The to. */
    @Column(name = "`to`", columnDefinition="text")
    private String to;
    
    /** The type. */
    private String type;    // reserved for later notification types other than email;
    
    /** The subject. */
    private String subject;
    
    /** The content. */
    @Column(name = "`content`", columnDefinition="LONGTEXT")
    private String content;
    
    /** The attach file name */
    private String attachFilename;
    
    /** The attach file content */
    @Column(name = "`attachFileContent`", columnDefinition="mediumtext")
    private String attachFileContent;
    
    /** The user name. */
    private String userName;
    
    /** The program name. */
    private String programName;
    
    /** The event name. */
    private String eventName;
    
    /** The type. */
    private String contentType; 
    
    /** the status of the message */
    @Column(name = "`status`")
    private int status;
    
    /** the priority */
    @Column(name = "`priority`")
    private int priority = PRIORITY_NORMAL;
    
    /**sent time*/
    private Date sentTime;
    
    /** participant contact id, null for if it is doesn't exist **/
    private String contactId;
    
    /**
     * Gets the content.
     * 
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the content.
     * 
     * @param content the new content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Gets the from.
     * 
     * @return the from
     */
    public String getFrom() {
        return from;
    }

    /**
     * Sets the from.
     * 
     * @param from the new from
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * Gets the subject.
     * 
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets the subject.
     * 
     * @param subject the new subject
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Gets the to.
     * 
     * @return the to
     */
    public String getTo() {
        return to;
    }

    /**
     * Sets the to.
     * 
     * @param to the new to
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * Gets the type.
     * 
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type.
     * 
     * @param type the new type
     */
    public void setType(String type) {
        this.type = type;
    }

    public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getAttachFilename() {
		return attachFilename;
	}

	public void setAttachFilename(String attachFilename) {
		this.attachFilename = attachFilename;
	}

	public String getAttachFileContent() {
		return attachFileContent;
	}

	public void setAttachFileContent(String attachFileContent) {
		this.attachFileContent = attachFileContent;
	}

	/**
     * Gets the user name.
     * 
     * @return the user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the user name.
     * 
     * @param userName the new user name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets the program name.
     * 
     * @return the program name
     */
    public String getProgramName() {
        return programName;
    }

    /**
     * Sets the program name.
     * 
     * @param programName the new program name
     */
    public void setProgramName(String programName) {
        this.programName = programName;
    }

    /**
     * Gets the event name.
     * 
     * @return the event name
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * Sets the event name.
     * 
     * @param eventName the new event name
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
    
    /**
     * Status transitions of a Message 
     * <li>
     * Message not sent to any recipients will be marked as {@link #STS_NEW} 
     * <li>
     * When it sent out successfully it will be marked as {@link #STS_SENT}
     * <li>
     * If message is blocked by some filter, then it will be marked as {@link #STS_SUSPENDED}
     * <li>
     * If error occurs in sending, it will be marked as {@link #STS_FAILED}
     * <li>
     * Failed Message will be expire after a certain time span. Expired message is marked 
     * as {@link #STS_IGNORED}
     * 
     * <li> Failed Message but not expired will be recycled. It's status will change 
     * accordingly just like a new message 
     *  
     * @return status of the message
     */
    public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	/**
	 * 
	 * @return the priority of the message
	 */
	public int getPriority() {
		return priority;
	}
	
	
	public void setPriority(int priority) {
		this.priority = priority;
	}

	public Date getSentTime() {
		return sentTime;
	}

	public void setSentTime(Date sentTime) {
		this.sentTime = sentTime;
	}
	
	
	public String getContactId() {
		return contactId;
	}

	public void setContactId(String contactId) {
		this.contactId = contactId;
	}

	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
     public String toString() {
        StringBuffer sb = new StringBuffer("");
        sb.append("from = ").append(from);
        sb.append(", to = ").append(to);
        sb.append(", subject = ").append(subject);
        sb.append(", content = ").append(content);
        sb.append(", priority = ").append(priority);
        sb.append(", status = ").append(status);
        return sb.toString();
    }
}
