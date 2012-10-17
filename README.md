NewsRecommendation
==================

News Recommendation Program using TF-IDF  
  
自分が興味のあるニュース記事を選んでおくと、新着記事の中からtf-idfを使って  
自動的におすすめのニュースをランキングしてくれるプログラム。  
コア部分はjavaで書いていますが、サンプルプログラムを動かすにはPHPが必要です。  

1.ディレクトリ past/に含まれる記事の中から好きな記事のタイトルを myfavorite.txtに書いておきます。  
2. NewsRecommendation.shを実行すると、PHPプログラムが自動的にGoogleニュースを取得し、全記事をおすすめ順でランキングしてくれます。(結果はmyfavorite.txt.outとして出力)  
3. 新着の記事はディレクトリ new/に、morphol/は記事の解析データか格納されます。また、new/とmorphol/ディレクトリは実行ごとにリフレッシュされ、実行後に新着記事は自動的にpast/にコピーされます。  
  
ちょくちょく改良していきます^^  
 
