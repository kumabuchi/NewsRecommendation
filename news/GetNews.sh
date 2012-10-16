#!/bin/sh
export CLASSPATH=$CLASSPATH:kuromoji-0.7.7.jar
rm raw/*
rm morphol/*
php CallAPI.php > raw/article.raw

