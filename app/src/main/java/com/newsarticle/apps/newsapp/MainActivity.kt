package com.newsarticle.apps.newsapp

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), NewsFetchedListener {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeRecyclerView()

        fetchNewsItems()

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(
                        "TAG", "getInstanceId failed",
                        task.exception
                    )
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token
                Log.d("TOKEN", "onCreate: $token")
            })
    }


    private fun initializeRecyclerView(){

        recyclerView = recycler_view
        val recyclerViewAdapter = RecyclerViewAdapter(null, this)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(
                context,
                RecyclerView.VERTICAL, false
            )

            adapter = recyclerViewAdapter
        }
    }



    private fun fetchNewsItems( query : String = getString(R.string.default_search_text)){

        val n = NewsFetchingAsyncTask(query, this )
        n.execute()
    }

    override fun whenNewsFetchedSuccessfully(articles: List<Article>?) {

        val adapter = recyclerView.adapter as RecyclerViewAdapter
        adapter.refreshNewsItems(articles)

    }

    override fun whenNewsFetchedOnError(error: String?) {
        val t = Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT)
        t.setGravity(Gravity.TOP, 0, 500)
        t.show()

    }
}