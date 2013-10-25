create table item(item_id char(10) PRIMARY KEY, name VARCHAR(1000), currently DECIMAL(8,2), buy_price DECIMAL(8,2), first_bid DECIMAL(8,2), number_of_bids int, description  VARCHAR(4000), started TIMESTAMP, ends TIMESTAMP,seller VARCHAR(200));
create table id_category(item_id char(10), category varchar(1000));
create table user(user_id varchar(1000) primary key, rating int, country varchar(100), location varchar(500));
create table bids(item_id char(10), user_id varchar(1000), time TIMESTAMP, amount DECIMAL(8,2));