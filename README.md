NewsRecommendation
==================

News Recommendation Program using TF-IDF  
  
自分が興味のあるニュース記事を選んでおくと、新着記事の中からtf-idfを使って  
自動的におすすめのニュースをランキングしてくれるプログラム。  
コア部分はjavaで書いていますが、サンプルプログラムを動かすにはPHPが必要です。  
  
階層構造と使用方法  
core  
├── NewsRecommendation.java (コアプログラム)  
├── NewsRecommendation.sh (実行スクリプト)  
├── favorite
│   └── myfavorite.txt (興味のある記事一覧ファイル)  
├── kuromoji-0.7.7.jar (形態素解析器)  
├── morphol (新着記事の形態素解析結果格納ディレクトリ)  
├── new (新着記事格納ディレクトリ)  
├── output (出力格納ディレクトリ)  
└── past (過去記事格納ディレクトリ)  

サンプルプログラム sample/  
1.ディレクトリ past/に含まれる記事の中から好きな記事のタイトルを favorite/myfavorite.txtに書いておきます。  
2. NewsRecommendation.shを実行すると、PHPプログラムが自動的にGoogleニュースをnew/に取得し、全記事をおすすめ順でランキングしてくれます。(結果はoutput/myfavorite.txt.outとして出力)  
  
[注1] new/とmorphol/ディレクトリは実行ごとにリフレッシュされ、実行後に新着記事は自動的にpast/にコピーされます。  
[注2] new/及びpast/内の記事ファイルは、記事タイトルをファイル名、記事本文を内容とし、行頭に#をつけることでアルゴリズムから無視させることができます。形態素解析器は日本語のみ対応しているので、リンク文などは無視させることが望ましい。  
[注3] output/ディレクトリには最終結果と共に拡張子.termのファイル(興味のある記事の解析結果)が出力されます。  
  
ちょくちょく改良していきます^^  
 
