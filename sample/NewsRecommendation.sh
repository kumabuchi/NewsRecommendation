#!/bin/sh
export CLASSPATH=$CLASSPATH:kuromoji-0.7.7.jar
rm new/*
rm morphol/*
php CallAPI.php > article.raw
# compile
javac NewsExtractor.java
java NewsExtractor

# compile
javac NewsRecommendation.java
java NewsRecommendation favorite/myfavorite.txt

cp new/* past/

