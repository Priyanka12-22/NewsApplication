package com.newsarticle.apps.newsapp

import android.os.AsyncTask
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


interface NewsFetchedListener {

    fun whenNewsFetchedSuccessfully ( articles : List<Article>?)

    fun whenNewsFetchedOnError ( error: String? )

}

class NewsFetchingAsyncTask (private val q : String? = null, private val newsFetchedListener: NewsFetchedListener? = null  ) : AsyncTask<String, String, String>(){

    @Throws(IOException::class)
    private fun sendGet(url: String): String {
        val obj = URL(url)
        val con = obj.openConnection() as HttpURLConnection
        con.requestMethod = "GET"
        val responseCode = con.responseCode

        if (responseCode == HttpURLConnection.HTTP_OK) { // connection ok
            val ins = BufferedReader(InputStreamReader(con.inputStream))
            val response = StringBuffer()

            var line : String?

            do {

                line = ins.readLine()

                if (line == null)
                    break

                response.append(line)


            } while (true)

            ins.close()
            return response.toString()
        } else {
            return ""
        }
    }

    override fun doInBackground(vararg p0: String?): String {

        val myurl = "https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json"

        val s = this.sendGet(myurl)

        return s
    }

    override fun onPostExecute(result: String?) {

        if ( result != null ){

            parseReturnedJsonData(result)
        }
    }


    private fun parseReturnedJsonData(s: String) {


        val p = Gson()
        val rt = p.fromJson(s, NewsResult::class.java)

        if ( rt.status == "ok" ){

            newsFetchedListener?.whenNewsFetchedSuccessfully(rt.articles)
        }
        else {

            newsFetchedListener?.whenNewsFetchedOnError("Error")
        }
    }

}
