package com.newsarticle.apps.newsapp

class NewsResult{

    var status : String? = null
    var articles : List<Article>? = null

}


class Article {

    var title : String? = null
    var description : String? = null
    var url : String? = null
    var urlToImage : String? = null
    var publishedAt : String? = null

}