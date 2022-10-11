package com.newsarticle.apps.newsapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy


fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

class RecyclerViewAdapter ( private var  articles : List <Article>?,private val context : Context? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {



    inner class SearchViewHolder(itemView : View) :
        RecyclerView.ViewHolder(itemView) {
    }


    inner class NewsItemViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {

        val newsImage : ImageView = itemView.findViewById(R.id.newsImage)

        val newsTitle : TextView = itemView.findViewById(R.id.newsTitle)

        val newsDescription : TextView = itemView.findViewById(R.id.newsDescription)

        val newsDate : TextView = itemView.findViewById(R.id.newsDate)

        init {

            itemView.setOnClickListener {

                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(articles?.get(position-1)?.url))
                context?.startActivity(browserIntent)
            }
        }
    }

    internal fun refreshNewsItems ( articles: List<Article>?){


        val initialSize = this.articles?.size ?: 0

        this.articles = articles
        if ( articles != null ){

            notifyItemRangeChanged(1,articles?.size ?: 0)
            if ( articles.size < initialSize ){

                val sizeDifference = initialSize - articles.size
                notifyItemRangeRemoved(articles?.size, sizeDifference)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {

        if ( position == 0) return 0
        return 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if ( viewType == 0 ){

            val v = parent.inflate(R.layout.news_search_layout)
            SearchViewHolder(v)
        } else {

            val v = parent.inflate(R.layout.news_item_layout)
            NewsItemViewHolder(v)
        }
    }

    override fun getItemCount(): Int {

        return ( 1 + (articles?.size ?: 0) )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (position >= 1) {

            if (articles != null) {


                val idx = position - 1

                if (articles?.indices?.contains(idx) == true) {

                    val a = articles!![idx]
                    val newsItemViewHolder = (holder as NewsItemViewHolder)
                    newsItemViewHolder.newsTitle.text = a.title
                    newsItemViewHolder.newsDescription.text = a.description
                    newsItemViewHolder.newsDate.text = a.publishedAt


                    if (context != null) {
                        Glide.with(context).load(a.urlToImage)
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .placeholder(R.drawable.image)
                            .error(R.drawable.image)
                            .fallback(R.drawable.image)
                            .into(newsItemViewHolder.newsImage)

                    }
                }

            }
        }

    }
}