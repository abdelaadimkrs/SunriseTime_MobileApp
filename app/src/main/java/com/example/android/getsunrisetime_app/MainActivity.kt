package com.example.android.getsunrisetime_app

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.util.zip.CheckedInputStream

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun sunrise(view: View) {

        val city= editPays.text.toString()
        val url = "https://query.yahooapis.com/v1/public/ylq?q=select item.condition from weather.forecast where woeid=$city&format=json&callback=done"
        MyAsyncTask().execute(url)
    }

    inner class MyAsyncTask:AsyncTask<String,String,String>()
    {
        override fun onPreExecute() {

        }

        override fun doInBackground(vararg p0: String?): String {
            try {
                val url =  URL(p0[0])
                val urlConnect = url.openConnection() as HttpURLConnection
                urlConnect.connectTimeout = 700
                val dataJsonAsStr = convertStreamToString(urlConnect.inputStream)
                publishProgress(dataJsonAsStr)

            }catch (ex:Exception)
            {

            }

            return ""
        }

        override fun onProgressUpdate(vararg values: String?) {

            val json = JSONObject(values[0])
            val query = json.getJSONObject("query")
            val results = query.getJSONObject("results")
            val channel = results.getJSONObject("channel")
            val astronomy = channel.getJSONObject("astronomy")
            val sunrise = astronomy.getString("sunrise")
            rslt.text="Sunrise Time is :" + sunrise



        }
        override fun onPostExecute(result: String?) {

        }


    }

    fun convertStreamToString(inputStream: InputStream):String{

        val bufferReader = BufferedReader(InputStreamReader(inputStream))
        var line:String
        var allString:String=""

        try {
            do{
                line=bufferReader.readLine()
                if(line!=null)
                    allString+=line
            }while(line!=null)
            bufferReader.close()
        }catch (ex:Exception){}



        return allString
    }
}