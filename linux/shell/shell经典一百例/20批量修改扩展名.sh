#!/bin/sh


for i in `ls *.$1`
do
	echo ${i%.*}.$2;
	#mv ${i} ${i%.*}.$2;
done;