select COUNT(*) from user;
select COUNT(*) from user where location = "New York";
select COUNT(*) from (select count(*) as counts  from id_category group by item_id) as categoryCount where counts = 4;
select bids.item_id from bids, (select max(amount) as amount from bids inner join item on item.item_id = bids.item_id where date(ends) >= "2001-12-20") as maxbid where bids.amount = maxbid.amount;
select COUNT(*) from user, (select distinct(seller) from item) as sellers where user_id = seller and rating > 1000;
select COUNT(*) from user, (select distinct(user_id) from bids) as bidders, (select distinct(seller) from item) as sellers where user.user_id = bidders.user_id and user.user_id = sellers.seller;
select COUNT(DISTINCT CATEGORY) from id_category where item_id in (select distinct(item_id) from bids where amount > 100);
