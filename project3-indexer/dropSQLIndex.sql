alter table item DROP INDEX seller,DROP INDEX buy_price,DROP INDEX first_bid,DROP INDEX started,DROP INDEX ends;
alter table bids DROP INDEX user_id,DROP INDEX item_id;