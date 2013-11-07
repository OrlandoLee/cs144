create table item(item_id char(10) PRIMARY KEY, name VARCHAR(100), currently DECIMAL(8,2), buy_price DECIMAL(8,2), first_bid DECIMAL(8,2), number_of_bids int, description  VARCHAR(4000), started TIMESTAMP, ends TIMESTAMP,seller VARBINARY(100));
create table id_category(item_id char(10) references item(item_id), category varchar(100));
create table user(user_id varbinary(100) primary key, rating int, country varbinary(100), location varbinary(100));
create table bids(item_id char(10) references item(item_id), user_id varbinary(100) references user(user_id), time TIMESTAMP, amount DECIMAL(8,2),primary key (item_id,user_id,time));
