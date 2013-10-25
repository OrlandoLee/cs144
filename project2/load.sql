load data local infile "~/item.dat" into table item  FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"';
load data local infile "~/id_category.txt" into table id_category  FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"';
load data local infile "~/user.txt" into table user  FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"';
load data local infile "~/bid.txt" into table bids  FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"';