package com.akuacom.pss2.email;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.akuacom.ejb.BaseEntity;

@Entity
@Table(name="peakchoice_message")
@Cache (usage=CacheConcurrencyStrategy.TRANSACTIONAL)
public class PeakChoiceMessageEntity extends BaseEntity {
	
	/*
	 * DRMS-2166 Remove duplicate code (both relate to an email domain) between this and MessageEntity
	 */

	private static final long serialVersionUID = -3179589116440602819L;

	/** The from. */
    @Column(name = "`from`")  	// backticks necessary; 'from' is keyword 
    private String from;

    /** The to. */
    @Column(name="`to`", columnDefinition="text") 		// backticks necessary; 'from' is keyword
    private String to;

    /** The type. */
    private String type;    	// reserved for later notification types other than email;

    /** The subject. */
    private String subject;

    /** The content. */
    @Column(columnDefinition="text")
    private String content;

    /** The user name. */
    private String userName;

    /** The program name. */
    private String programName;

    /** The event name. */
    private String eventName;

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
        return sb.toString();
    }
}
