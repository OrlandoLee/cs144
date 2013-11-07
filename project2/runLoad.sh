#!/bin/bash
mysql CS144 < drop.sql
mysql CS144 < create.sql
ant
ant run-all
sort -u user.tmp -o user.dat
sort -u id_category.tmp -o id_category.dat
mysql CS144 < load.sql
rm *.dat
rm *.tmp
rm -r bin 
mysql CS144 < queries.sql
