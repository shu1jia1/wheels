CREATE TABLE cm_device (
  devno VARCHAR (50),
  NAME VARCHAR (60) NOT NULL,
  devtype INT NOT NULL,
  ip VARCHAR (32) NULL,
  remarks VARCHAR (100) NULL,
  positionId VARCHAR (50) NULL,
  updateTime DATETIME NULL,
  PRIMARY KEY (devno)
) ENGINE = INNODB ;

CREATE TABLE cm_geo_device (
  geoNo VARCHAR (50),
  devno VARCHAR (50),
  UNIQUE KEY `geo_dev` (`geoNo`,`devno`)
) ENGINE = INNODB ;
 