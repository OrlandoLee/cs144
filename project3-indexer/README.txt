UID: 804126172
Name: Zongsheng Li

UID: 304138441
Name: Chang Zhao
Besides primary key which is item_id for item. bidder,item_id,time for bids table.I created following indexes.


MySQL indexes: seller, buy price,  ending time, first_bids, started.

Lucene indexes: name, category, discription and also content which is name+category+discription.

The reason why we chose to index these attributes is that they all needs to be searched. For seller, buy price, bidder, ending time, these attributes are simple that do not have a long string therefore they are well supported by MySQL. But for name, category, discription, usually they have a long string in which there are several substrings need to be searched. Therefore we need to create invertedindex on these attributes.
