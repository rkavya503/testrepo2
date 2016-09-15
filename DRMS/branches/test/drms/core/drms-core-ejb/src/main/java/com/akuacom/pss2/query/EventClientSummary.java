package com.akuacom.pss2.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.util.EventStatus;

public class EventClientSummary implements Serializable {
    private static final long serialVersionUID = -3193816875866346028L;
    private static final String MANUAL_POSTFIX = "(MAN)";

    private String uuid;
    private String clientName;
    private String parentName;
    private String type;
    private String lastMode;
    private String lastStatus;
    private Date lastContact;
    private boolean manualControl;
    private Date startTime;

    private List<EventClientSignal> signals;
    private List<ManualSignal> manualSignals;

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getEventStatus() {
        if (this.getType().equalsIgnoreCase("MANUAL")) {
            // show current (server) status for manual clients only.
            return StringUtils.replace(getCurrentStatus(), "UNKNOWN", "?");
        }
        String eventStatus = getLastStatus();
        if (isManualControl()) {
            eventStatus = eventStatus + MANUAL_POSTFIX;
        } else if (!getLastStatus().equals(getCurrentStatus())) {
            eventStatus = getLastStatus() + " [ " + getCurrentStatus() + " ]";
        }

        return eventStatus;
    }

    public String getCurrentStatus() {
        long nowMS = System.currentTimeMillis();
        long startTimeMS = getStartTime().getTime();
        String pendingValue = getClientManualSignalValueAsString("pending");
        if (isManualControl()) {
            if (pendingValue == null) {
                // hardcoded default
                pendingValue = "off";
            }
            EventStatus status = pendingValue.equals("on") ? EventStatus.ACTIVE : EventStatus.NONE;
            return status.name();
        } else {
            if (pendingValue == null) {
                pendingValue = "off";
            }
            EventStatus status;
            if (startTimeMS < nowMS) {
                status = EventStatus.ACTIVE;
            } else if (pendingValue.equals("on")) {
                status = EventStatus.NEAR;
            } else {
                status = EventStatus.FAR;
            }
            return status.name();
        }
    }

    public Date getLastContact() {
        return lastContact;
    }

    public void setLastContact(Date lastContact) {
        this.lastContact = lastContact;
    }

    public boolean isManualControl() {
        return manualControl;
    }

    public void setManualControl(boolean manualControl) {
        this.manualControl = manualControl;
    }

    public List<ManualSignal> getManualSignals() {
        if (manualSignals == null) {
            manualSignals = new ArrayList<ManualSignal>();
        }
        return manualSignals;
    }

    public void setManualSignals(List<ManualSignal> manualSignals) {
        this.manualSignals = manualSignals;
    }

    private String getClientManualSignalValueAsString(String signalName) {
        String res = null;
        if (getManualSignals() != null) {
            for (ManualSignal signalState : getManualSignals()) {
                if (signalState != null) {
                    if (signalState.getSignalName().equals(signalName)) {
                        res = signalState.getSignalValue();
                        break;
                    }
                }
            }
        }
        return res;
    }

    public String getMode() {
        if (getType().equalsIgnoreCase("MANUAL")) {
            //show current (server) mode for manual clients only.
            return StringUtils.replace(getCurrentMode(), "UNKNOWN", "?");
        }
        String mode = getLastMode();
        if (isManualControl()) {
            mode += MANUAL_POSTFIX;
        } else if (!getLastMode().equals(getCurrentMode())) {
            mode = getLastMode() + " [ " + getCurrentMode() + " ]";
        }
        mode = mode.replaceAll("UNKNOWN", "?");
        return mode;
    }

    public String getCurrentMode() {
        String mode;
        if (isManualControl()) {
            mode = getClientManualSignalValueAsString("mode");
        } else {
            mode = getSignalValueForEventParticipantAsString("mode");
        }

        if (mode == null) {
            mode = "normal";
        }
        return mode.toUpperCase();
    }

    public String getSignalValueForEventParticipantAsString(String signalName) {
        String res = null;
        List<EventClientSignal> signalEntries = getSignals();
        Collections.sort(signalEntries);
        Collections.reverse(signalEntries);
        for (EventClientSignal signalEntry : signalEntries) {
            if (signalEntry.getSignalStartTime().getTime() < System.currentTimeMillis()
                    && signalEntry.getSignalName().equals(signalName)) {
                if (signalEntry.isLevelSignal()) {
                    res = signalEntry.getStringValue();
                    break;
                } else {
                    res = Double.toString(signalEntry.getNumberValue());
                    break;
                }
            }
        }
        return res;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public List<EventClientSignal> getSignals() {
        if (signals == null) {
            signals = new ArrayList<EventClientSignal>();
        }
        return signals;
    }

    public void setSignals(List<EventClientSignal> signals) {
        this.signals = signals;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeString() {
        if (Integer.parseInt(type) == Participant.TYPE_AUTO) {
            return "AUTO";
        } else if (Integer.parseInt(type) == Participant.TYPE_MANUAL) {
            return "MANUAL";
        } else {
            return "unknown";
        }
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getLastMode() {
        return lastMode;
    }

    public void setLastMode(String lastMode) {
        this.lastMode = lastMode;
    }

    public String getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(String lastStatus) {
        this.lastStatus = lastStatus;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public static class EventClientSignal implements Serializable, Comparable<EventClientSignal> {
        private static final long serialVersionUID = -899647311050855440L;
        private String signalName;
        private boolean levelSignal;
        private double numberValue;
        private String stringValue;
        private Date signalStartTime;

        public String getSignalName() {
            return signalName;
        }

        public void setSignalName(String signalName) {
            this.signalName = signalName;
        }

        public boolean isLevelSignal() {
            return levelSignal;
        }

        public void setLevelSignal(boolean levelSignal) {
            this.levelSignal = levelSignal;
        }

        public double getNumberValue() {
            return numberValue;
        }

        public void setNumberValue(double numberValue) {
            this.numberValue = numberValue;
        }

        public Date getSignalStartTime() {
            return signalStartTime;
        }

        public void setSignalStartTime(Date signalStartTime) {
            this.signalStartTime = signalStartTime;
        }

        public String getStringValue() {
            return stringValue;
        }

        public void setStringValue(String stringValue) {
            this.stringValue = stringValue;
        }

        @Override
        public int compareTo(EventClientSignal o) {
            if (o == null) {
                return 1;
            }
            return signalStartTime.compareTo(o.getSignalStartTime());
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result
                    + ((signalName == null) ? 0 : signalName.hashCode());
            result = prime
                    * result
                    + ((signalStartTime == null) ? 0 : signalStartTime
                    .hashCode());
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
            EventClientSignal other = (EventClientSignal) obj;
            if (signalName == null) {
                if (other.signalName != null)
                    return false;
            } else if (!signalName.equals(other.signalName))
                return false;
            if (signalStartTime == null) {
                if (other.signalStartTime != null)
                    return false;
            } else if (!signalStartTime.equals(other.signalStartTime))
                return false;
            return true;
        }
    }

    public static class ManualSignal implements Serializable {
        private static final long serialVersionUID = 6192848238394204262L;
        private String signalName;
        private String signalValue;

        public String getSignalName() {
            return signalName;
        }

        public void setSignalName(String signalName) {
            this.signalName = signalName;
        }

        public String getSignalValue() {
            return signalValue;
        }

        public void setSignalValue(String signalValue) {
            this.signalValue = signalValue;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result
                    + ((signalName == null) ? 0 : signalName.hashCode());
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
            ManualSignal other = (ManualSignal) obj;
            if (signalName == null) {
                if (other.signalName != null)
                    return false;
            } else if (!signalName.equals(other.signalName))
                return false;
            return true;
        }
    }
}
