load data local infile "item.dat" ignore into table item  FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"';
load data local infile "id_category.dat" ignore into table id_category  FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"';
load data local infile "user.dat" ignore into table user  FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"';
load data local infile "bids.dat" ignore into table bids  FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"';
