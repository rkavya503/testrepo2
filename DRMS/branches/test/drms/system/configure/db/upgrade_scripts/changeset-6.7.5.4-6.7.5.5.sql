ALTER TABLE `datasource` ADD COLUMN ownerType INT DEFAULT 0;

ALTER TABLE `dataset`   ADD  COLUMN `description` varchar(255) DEFAULT NULL;
ALTER TABLE `dataset`   DROP COLUMN `sync`;
ALTER TABLE `dataset`   ADD  COLUMN `sync` int DEFAULT 0;

ALTER TABLE `dataset`   ADD  COLUMN `graphType` int DEFAULT 0;
ALTER TABLE `dataset`   ADD  COLUMN `valuePrecision` int DEFAULT 0;
ALTER TABLE `dataset`   ADD  COLUMN `colorHint` varchar(20) DEFAULT NULL;
ALTER TABLE `dataset`   ADD  COLUMN `interpolated` boolean DEFAULT 0;
ALTER TABLE `dataset`   ADD  COLUMN `dataType` int DEFAULT 0;
