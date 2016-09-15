--
-- clp migration
--


ALTER TABLE baseline_config 
ADD eventStartTime int(11) DEFAULT '43200000';
ALTER TABLE baseline_config 
ADD eventEndTime int(11) DEFAULT '72000000';
ALTER TABLE baseline_config 
ADD maStartTime int(11) DEFAULT '28800000';
ALTER TABLE baseline_config 
ADD maEndTime int(11) DEFAULT '39600000';
ALTER TABLE baseline_config 
ADD minMARate float DEFAULT '0';
ALTER TABLE baseline_config 
ADD maxMARate float DEFAULT '0';
ALTER TABLE baseline_config 
ADD excludeEventDay tinyint(1) DEFAULT '1';


CREATE TABLE `Task` (
  `uuid` char(32) NOT NULL,
  `creationTime` datetime DEFAULT NULL,
  `version` int(11) NOT NULL,
  `modifiedTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  `ancestry` varchar(320) DEFAULT NULL,
  `startMillis` bigint(20) NOT NULL,
  `stopMillis` bigint(20) NOT NULL,
  `lastTrigger` bigint(20) NOT NULL,
  `period` bigint(20) NOT NULL,
  `type` int(11) DEFAULT NULL,
  `parent_uuid` char(32) DEFAULT NULL,
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `name` (`name`,`type`),
  KEY `FK27A9A590799D78` (`parent_uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `drwebsite_property` (
  `uuid` varchar(32) NOT NULL,
  `version` int(11) DEFAULT NULL,
  `propertyName` varchar(255) NOT NULL,
  `stringValue` varchar(255) DEFAULT NULL,
  `textValue` text,
  `creator` varchar(255) DEFAULT NULL,
  `modifier` varchar(255) DEFAULT NULL,
  `creationTime` datetime DEFAULT NULL,
  `modifiedTime` datetime DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `drwebsite_property` (`uuid`, `version`, `propertyName`, `stringValue`, `textValue`, `creator`, `modifier`, `creationTime`, `modifiedTime`) VALUES ('400e7e04bc8611e097d4c402baa3011a',NULL,'COPYRIGHT_RTP',NULL,'*Actual billed prices may vary based on updated temperature information.Refer to the RTP tariff at <a href=\"http://www.sce.com/tariffbooks\">www.sce.com/tariffbooks</a> for current rate schedules.SCE is not responsible for any loss or damages resulting from inaccurate or changed information.','script',NULL,'2011-08-01 14:36:00',NULL),('400e92e0bc8611e097d4c402baa3011a',NULL,'COPYRIGHT_SCE',NULL,'Copyright @ 2010 Southern California Edison. All Rights Reserved','script',NULL,'2011-08-01 14:36:00',NULL),('400ea604bc8611e097d4c402baa3011a',NULL,'COPYRIGHT_ACTIVE_EVENTS_TITLE',NULL,'Welcome to the SCE DR Event Website.  It is recommended that you reload or refresh this site often to ensure you are viewing the most recent available information. This website is not intended as a substitute for receiving SCE event notifications.','script',NULL,'2011-08-01 14:36:00',NULL),('400eb496bc8611e097d4c402baa3011a',NULL,'COPYRIGHT_HISTORY_EVENTS_TITLE',NULL,'Search for event history by program and date range.  Past events do not indicate nor guarantee future event activity.','script',NULL,'2011-08-01 14:36:00',NULL),('400ec53abc8611e097d4c402baa3011a',NULL,'COPYRIGHT_ACTIVE_EVENTS_DBP',NULL,'DBP Events scheduled for tomorrow will not appear until after 6PM today.','script',NULL,'2011-08-01 14:36:00',NULL),('400ed84abc8611e097d4c402baa3011a',NULL,'COPYRIGHT_HISTORY_EVENTS_SEARCH_TITLE',NULL,'Search and export event histories by program.','script',NULL,'2011-08-01 14:36:00',NULL),('400ee740bc8611e097d4c402baa3011a',NULL,'TITLE_EVENT_STATUS_PAGE',NULL,'Demand Response Event Status','script',NULL,'2011-08-01 14:36:00',NULL),('400ef564bc8611e097d4c402baa3011a',NULL,'TITLE_EVENT_HISTORY_PAGE',NULL,'Event History','script',NULL,'2011-08-01 14:36:00',NULL),('400f0356bc8611e097d4c402baa3011a',NULL,'TITLE_EVENT_STATUS_RTP',NULL,'Based on Applicable Temperature of:','script',NULL,'2011-08-01 14:36:00',NULL),('400f10f8bc8611e097d4c402baa3011a',NULL,'CONTACT_NO',NULL,'1-866-334-7827','script',NULL,'2011-08-01 14:36:00',NULL),('400f1ea4bc8611e097d4c402baa3011a',NULL,'TBLCOL_PROGRAM_NAME',NULL,'Program Name','script',NULL,'2011-08-01 14:36:00',NULL),('400f2c46bc8611e097d4c402baa3011a',NULL,'TBLCOL_REGIONS',NULL,'Regions Affected','script',NULL,'2011-08-01 14:36:00',NULL),('400f39f2bc8611e097d4c402baa3011a',NULL,'TBLCOL_DATE',NULL,'Date','script',NULL,'2011-08-01 14:36:00',NULL),('400f4910bc8611e097d4c402baa3011a',NULL,'TBLCOL_START_TIME',NULL,'Start Time','script',NULL,'2011-08-01 14:36:00',NULL),('400f595abc8611e097d4c402baa3011a',NULL,'TBLCOL_END_TIME',NULL,'End Time','script',NULL,'2011-08-01 14:36:00',NULL),('400f6bdebc8611e097d4c402baa3011a',NULL,'TBLCOL_TEMPERATURE',NULL,'Applicable <br>Temperature','script',NULL,'2011-08-01 14:36:00',NULL),('400f7e4ebc8611e097d4c402baa3011a',NULL,'TBLCOL_PRICE_CATEGORY',NULL,'Expected Pricing Category*','script',NULL,'2011-08-01 14:36:00',NULL),('cfbff33475da11e098f8cce30e9adb7c',NULL,'COPYRIGHT_DRC',NULL,'Contact your DRC Aggregator for more information on DRC events.  <br><br>For more information on SCE\'s DRC program, please visit <a href=\"http://www.sce.com/drc\">www.sce.com/drc</a>.','script',NULL,'2011-05-03 16:12:26',NULL),('cfbff4w475da11e092f8cce30e9adb7c',NULL,'TBLCOL_PRICE_CATEGORY_HISTORY',NULL,'Pricing Category*','script',NULL,'2011-05-03 16:12:26',NULL),('cfbff4w475da11e098f8cce30e9adb7c',NULL,'TBLCOL_Date_Of_Usage',NULL,'Date of Usage','script',NULL,'2011-05-03 16:12:26',NULL),('cfbff9e475da11e098f8cce30e9adb7c',NULL,'COPYRIGHT_RTPFORECAST',NULL,'*SCE considers Expected Pricing Categories to be forward-looking statements, which are subject to risk and uncertainty; actual billed prices will be based on actual recorded high temperatures.   SCE is not responsible for any loss or damages resulting from inaccurate or changed information.  Refer to the RTP tariff at <a href=\"http://www.sce.com/tariffbooks\">www.sce.com/tariffbooks</a> for current rate schedules.','script',NULL,'2011-05-03 16:12:26',NULL),('cfbffbe475da11e098f8cce30e9adb7c',NULL,'RTPFORECAST_SUBTITLE',NULL,'5-Day Pricing Category Forecast','script',NULL,'2011-05-03 16:12:26',NULL),('cfbffwe475da11e098f8cce30e9adb7c',NULL,'COPYRIGHT_HISTORY_RTP_SEARCH',NULL,'*Historical data is provided as a reference only and is not intended to be a promise or guarantee of future activity or pricing.','script',NULL,'2011-05-03 16:12:26',NULL);





