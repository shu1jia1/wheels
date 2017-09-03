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
 

insert into `cm_geo_device` (`geoNo`, `devno`) values('geo1','gd101');
insert into `cm_geo_device` (`geoNo`, `devno`) values('geo1','gd102');
insert into `cm_device` (`devno`, `name`, `devtype`, `ip`, `remarks`, `positionId`, `updateTime`) values('Gd101','Gd101','1',NULL,NULL,NULL,NULL);
insert into `cm_device` (`devno`, `name`, `devtype`, `ip`, `remarks`, `positionId`, `updateTime`) values('Gd102','Gd103','1',NULL,NULL,NULL,NULL);
insert into `cm_device` (`devno`, `name`, `devtype`, `ip`, `remarks`, `positionId`, `updateTime`) values('geo1','geo1','0',NULL,NULL,NULL,NULL);
insert into `cm_device` (`devno`, `name`, `devtype`, `ip`, `remarks`, `positionId`, `updateTime`) values('geo2','geo2','0',NULL,NULL,NULL,NULL);
