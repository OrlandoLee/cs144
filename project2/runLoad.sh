#!/bin/bash
mysql CS144 < drop.sql
mysql CS144 < create.sql
ant
ant run-all
sort -u -o user.dat user.dat
sort -u -o id_category.dat id_category.dat
mysql CS144 < load.sql
rm *.dat
rm -r bin 
mysql CS144 < queries.sql
