alter table item add index(seller),add index(buy_price),add index(first_bid),add index(started),add index(ends);
alter table bids add index(user_id),add index(item_id);