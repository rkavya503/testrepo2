package com.akuacom.pss2.system;


import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.akuacom.ejb.TreeNode;

/**
 * models a task whose period is so long it is made persistent (and
 * so can survive server restarts)
 *
 */
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames= {"name", "type"}) })
@NamedQueries({
        @NamedQuery(name = "Task.findAll",
                query = "select distinct t from Task t"),
        @NamedQuery(name = "Task.getAllIds",
                query = "select distinct t.UUID from Task t"),
        @NamedQuery(name = "Task.count", query = "select count(*) from Task t"),
        @NamedQuery(name = "Task.findByName",
                query = "select distinct t from Task t where t.name like :name"),
        @NamedQuery(name = "Task.findByNameAndType.Single",
                query = "select distinct t from Task t where t.name = :name and t.type = :type"),
        @NamedQuery(
                name = "Task.findActive",
                query = "select distinct t from Task t where t.startMillis < :now and " +
                    "( (period = 0 and t.stopMillis > :now and t.lastTrigger = 0) " + // one-shot
                    "or (period <> 0 and (t.stopMillis > :now or t.stopMillis = -1)))"),
        @NamedQuery(
                name = "Task.findPending",
                query = "select distinct t from Task t where t.startMillis > :now"),
        @NamedQuery(
                name = "Task.findPast",
                query = "select distinct t from Task t where t.stopMillis < :now and t.stopMillis > 0"),
                
        @NamedQuery(
                name = "Task.findDecendants",
                query = "select distinct t from Task t where locate(:uuid, t.ancestry) > 0"),
                        
        @NamedQuery(
                name = "Task.findAncestors",
                query = "select distinct t from Task t where locate(t.UUID, :ancestry ) > 0")
        
})
@AttributeOverride(name="UUID", column=@Column(columnDefinition="char(32)"))
public class Task extends TreeNode<Task> {

    private static final long serialVersionUID = 1L;

    public static enum Type {
        LogTruncation
    }

    protected long startMillis;
    protected long stopMillis; // -1 -> never stops
    protected long lastTrigger;
    protected long period;
    @Enumerated
    protected Type type = Type.LogTruncation;

    public Task() {
    }

    public long getStartMillis() {
        return startMillis;
    }

    public void setStartMillis(long startMillis) {
        this.startMillis = startMillis;
    }

    public long getStopMillis() {
        return stopMillis;
    }

    public void setStopMillis(long stopMillis) {
        this.stopMillis = stopMillis;
    }

    public long getLastTrigger() {
        return lastTrigger;
    }

    public void setLastTrigger(long lastTrigger) {
        this.lastTrigger = lastTrigger;
    }

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
    
    public boolean aliveQ() {
        long ltime = System.currentTimeMillis();
        return startMillis < ltime && ltime < stopMillis;
    }
    
    public boolean isRecurring() {
        return period != 0;
    }

    public boolean isNeverEnding() {
        return stopMillis < 0;
    }
}
