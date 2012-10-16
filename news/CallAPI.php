<?php
echo(strip_tags(htmlspecialchars_decode(file_get_contents("http://fullrss.net/rss/http/news.google.com/news?hl=ja&ned=us&ie=UTF-8&oe=UTF-8&output=rss&topic=w"))));
echo(strip_tags(htmlspecialchars_decode(file_get_contents("http://fullrss.net/rss/http/news.google.com/news?hl=ja&ned=us&ie=UTF-8&oe=UTF-8&output=rss&topic=n"))));
echo(strip_tags(htmlspecialchars_decode(file_get_contents("http://fullrss.net/rss/http/news.google.com/news?hl=ja&ned=us&ie=UTF-8&oe=UTF-8&output=rss&topic=t"))));
echo(strip_tags(htmlspecialchars_decode(file_get_contents("http://fullrss.net/rss/http/news.google.com/news?hl=ja&ned=us&ie=UTF-8&oe=UTF-8&output=rss&topic=s"))));
echo(strip_tags(htmlspecialchars_decode(file_get_contents("http://fullrss.net/rss/http/news.google.com/news?hl=ja&ned=us&ie=UTF-8&oe=UTF-8&output=rss&topic=e"))));

