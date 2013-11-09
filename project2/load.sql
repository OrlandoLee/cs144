load data local infile "item.dat" into table item  FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"' ESCAPED BY '"';
load data local infile "id_category.dat" into table id_category  FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"' ESCAPED BY '"';
load data local infile "user.dat" into table user  FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"' escaped by '"';
load data local infile "bids.dat" into table bids  FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"' escaped by '"';
