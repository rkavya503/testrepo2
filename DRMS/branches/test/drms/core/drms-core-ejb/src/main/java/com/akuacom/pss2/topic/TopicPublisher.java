package com.akuacom.pss2.topic;

import java.io.Serializable;

import javax.ejb.Local;
import javax.ejb.Remote;

public interface TopicPublisher {
		
 @Remote
 public interface R extends TopicPublisher {}
 @Local
 public interface L extends TopicPublisher {}

 void publish(Serializable message );
}
