package com.akuacom.pss2.asynch;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

public class BasetHoldingRunnable implements HoldingRunnable, Serializable {

	private static final long serialVersionUID = 1L;

	private Date created;
	
	private Date lastUpdated;
	
	private String id;
	
	private long minHold = 500;
	
	private long maxHold =60*60*1000;
	
	private String status ="PENDDING";
	
	private int priority = 1;
	
	private boolean bool1;
	private boolean bool2;
	private String str1;
	private String str2;
	
	private AsynchRunable runnable;
	
	public BasetHoldingRunnable(){
	}
	
	public BasetHoldingRunnable(AsynchRunable runnable,String id, long minHold, long maxHold){
		this(id,minHold,maxHold);
		this.setRunnable(runnable);
	}
	
	public BasetHoldingRunnable(String id, long minHold, long maxHold){
		if( id ==null)
			throw new IllegalArgumentException("id can not be null");
		this.created = new Date();
		this.lastUpdated = this.created;
		this.minHold = minHold;
		this.maxHold = maxHold;
		this.id = id;
	}
	
	public void setRunnable(AsynchRunable runnable){
		this.runnable = runnable;
	}
	
	public void run() {
		runnable.run();
	}
	
	/*@Override
	public HoldingRunnable merge(HoldingRunnable another) {
		if(another.getId().equals(getId())){
			setLastUpdate(getLastUpdate().getTime()>another.getLastUpdate().getTime()?getLastUpdate():another.getLastUpdate());
			return this;
		}
		return null;
	}*/
	
	@Override
	public String toString() {
		return "HoldingRunnable [id=" + id + "]";
	}
	
	public String  getMergeSQLTempleate(){
		return MERGE_SQL_TEMPLATE;
	}
	
	public Map<String,Object> getMergeSQLParam(){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("id", getId());
		params.put("minHold", getMinHold());
		params.put("maxHold", getMaxHold());
		params.put("runnable",getSerializedRunnable());
		return params;
	}
	
	private static final String  MERGE_SQL_TEMPLATE= 
		"INSERT INTO asyn_task(id, creationTime,lastUpdate, minHold,maxHold,runnable)" 
		+" VALUES (${id}, NOW(),NOW(),${minHold}, ${maxHold},${runnable}) " 
		+" ON DUPLICATE KEY UPDATE lastUpdate= GREATEST(NOW(),lastUpdate)";
	
	
	public Blob getSerializedRunnable(){
		ByteArrayOutputStream bos  = null;
		ObjectOutputStream oos = null;
		try{
			bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
			oos.writeObject(runnable);
			return new SerialBlob(bos.toByteArray());
		} catch (IOException e) {
			return null;
		} catch (SerialException e) {
			e.printStackTrace();
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			if(oos!=null) try {oos.close();}  catch(Exception e){};
			if(bos!=null) try {bos.close();} catch(Exception e){};
		}
	}
	
	public void setSerializedRunnable(byte[] bytes){
		ByteArrayInputStream bis  = null;
		ObjectInputStream ois = null;
		try{
			bis = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bis);
			AsynchRunable runnable = (AsynchRunable) ois.readObject();
			this.setRunnable(runnable);
		} catch (IOException e) {
		} catch (ClassNotFoundException e) {
			
		}finally{
			if(ois!=null) try {ois.close();}  catch(Exception e){};
			if(bis!=null) try {bis.close();} catch(Exception e){};
		}
	}
	
	@Override
	public AsynchRunable getRunnable() {
		if(this.runnable==null){
			return null;
		}
		this.runnable.setWrapper(this);
		return this.runnable;
	}
	
	@Override
	public void setCreationTime(Date created){
		this.created=created;
	}
	
	@Override
	public Date getCreationTime() {
		return created;
	}

	@Override
	public Date getLastUpdate() {
		return lastUpdated;
	}
	
	@Override
	public void setLastUpdate(Date date) {
		this.lastUpdated= date;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public long getMaxHold() {
		return maxHold;
	}
	
	public void setMaxHold(long maxHold) {
		this.maxHold = maxHold;
	}
	
	public long getMinHold() {
		return minHold;
	}
	
	public void setMinHold(long mergeTimeFrame) {
		this.minHold = mergeTimeFrame;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	
	public boolean isBool1() {
		return bool1;
	}

	public void setBool1(boolean bool1) {
		this.bool1 = bool1;
	}

	public boolean isBool2() {
		return bool2;
	}

	public void setBool2(boolean bool2) {
		this.bool2 = bool2;
	}

	public String getStr1() {
		return str1;
	}

	public void setStr1(String str1) {
		this.str1 = str1;
	}

	public String getStr2() {
		return str2;
	}

	public void setStr2(String str2) {
		this.str2 = str2;
	}
	
}
