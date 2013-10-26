UID: 804126172 
Name: Zongsheng Li
UID: 304138441
Name: Chang Zhao

schema:
item( item_id PRIMARY KEY,
       name,
       currently,
       buy_price, 
       first_bid, 
       number_of_bids, 
       description,
       started, 
       ends,
       seller(user_id of the user))

id_category( item_id, 
             category)

user( user_id primary key,
      rating, 
      country, 
      location)

bids( item_id references item(item_id) , 
      user_id references user(user_id), 
      time, 
      amount,
      Primary key(item_id, user_id, time))

item_id -> name, currently, buy_price, first_bid, number_of_bids, description, started, ends, seller
item_id, user_id, time -> amount
user_id -> rating, location, country

This database relation is in BCNF.
