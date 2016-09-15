SELECT message.to AS contactAddress,message.subject AS subject, message.creationTime AS createTime,
  participant.participantName AS clientName,participant.parent AS participantName
 ,IF(contact.firstName IS NULL,participant_contact.description ,contact.firstName) AS contactName 
 FROM message LEFT JOIN contact ON message.contactId = contact.uuid
 LEFT JOIN participant_contact ON message.contactId = participant_contact.uuid
 LEFT JOIN participant ON participant_contact.participant_uuid = participant.uuid
 WHERE message.status<>1 AND message.type='email'
 AND message.creationTime >= ${startTime}
 AND message.creationTime <= ${endTIme}
 ORDER BY createTime,contactName