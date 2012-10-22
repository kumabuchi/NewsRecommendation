#!/bin/sh
export CLASSPATH=$CLASSPATH:kuromoji-0.7.7.jar
rm new/*
rm morphol/*
# compile
javac NewsRecommendation.java
java NewsRecommendation favorite/myfavorite.txt
cp new/* past/

