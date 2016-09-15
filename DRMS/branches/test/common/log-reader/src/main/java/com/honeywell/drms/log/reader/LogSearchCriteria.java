package com.honeywell.drms.log.reader;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class LogSearchCriteria implements Serializable{
	
	private static final long serialVersionUID = 5304015116219295063L;
	
	private Date startDate;
	private Date endDate;
	private int level;
	private List<String> userNames;
	private String category;
	private String program;
	private String descriptionword;
	private String uuid;
	
	public LogSearchCriteria(Date startDate, Date endDate, int level,
			String userNames, String category, String program,
			String descriptionword) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
		this.level = level;
		if(userNames!=null && !userNames.trim().equals("")){
			this.userNames= Arrays.asList(userNames.split(";"));
		}
		this.category = category;
		this.program = program;
		this.descriptionword = descriptionword;
	}

	public LogSearchCriteria(String uuid) {
		this.uuid = uuid;
	}
	
	
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public List<String> getUserNames() {
		return userNames;
	}

	public void setUserNames(List<String> userNames) {
		this.userNames = userNames;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getProgram() {
		return program;
	}

	public void setProgram(String program) {
		this.program = program;
	}

	public String getDescriptionword() {
		return descriptionword;
	}

	public void setDescriptionword(String descriptionword) {
		this.descriptionword = descriptionword;
	}
	
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public static boolean needSearchAgain(LogSearchCriteria oldSC,LogSearchCriteria newSC){
		if(oldSC==null) return true;
		if(oldSC.equals(newSC)){
			//if end date is later than current time, need to search again since log is keeping growing
			if(oldSC.getEndDate().after(new Date())) 
				return true;
			else
				return false;
		}else{
			//search criteria changed, need search again
			return true;
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((category == null) ? 0 : category.hashCode());
		result = prime * result
				+ ((descriptionword == null) ? 0 : descriptionword.hashCode());
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + level;
		result = prime * result + ((program == null) ? 0 : program.hashCode());
		result = prime * result
				+ ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result
				+ ((userNames == null) ? 0 : userNames.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LogSearchCriteria other = (LogSearchCriteria) obj;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (descriptionword == null) {
			if (other.descriptionword != null)
				return false;
		} else if (!descriptionword.equals(other.descriptionword))
			return false;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (level != other.level)
			return false;
		if (program == null) {
			if (other.program != null)
				return false;
		} else if (!program.equals(other.program))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		if (userNames == null) {
			if (other.userNames != null)
				return false;
		} else if (!userNames.equals(other.userNames))
			return false;
		return true;
	}
}
