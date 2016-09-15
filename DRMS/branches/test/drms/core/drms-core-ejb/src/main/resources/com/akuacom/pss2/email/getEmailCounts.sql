SELECT count(uuid)
 FROM message 
 WHERE message.type='email'
 AND message.creationTime >= ${startTime}
 AND message.creationTime <= ${endTIme}