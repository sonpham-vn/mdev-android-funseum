package com.example.funseum.model

import android.content.Context
import android.widget.Toast
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.json.JSONObject



class FrameModel(context: Context) {

    private val mContext : Context =  context



    fun getEvent(onSuccess:(currentEvent: UserFrameData) -> Unit ) {
        var frameData : UserFrameData = UserFrameData()
        //fetching cards json from server
        val client = AsyncHttpClient()
        val sharedPref = mContext.getSharedPreferences("sharedpref", Context.MODE_PRIVATE)
                ?: return
        val userId = sharedPref.getString("userid","")

        client.addHeader("x-api-key", "7Nu7XDLhAb87fXii8IfgW5AhJeXvPSv05uzxXj64")
        client.get("https://zjil8ive37.execute-api.ca-central-1.amazonaws.com/dev/fm/frameinfo?UserId=$userId",
            object : AsyncHttpResponseHandler() {

                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<Header>,
                    response: ByteArray
                ) {
                    // called when response HTTP status is "200 OK"
                    val strResponse = String(response)
                    try {
                        frameData = Json.decodeFromString<UserFrameData>(strResponse)

                    } catch (e: SerializationException) {
                        null
                    }
                    onSuccess(frameData)
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<Header>,
                    errorResponse: ByteArray,
                    e: Throwable
                ) {
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    val errorResponse = String(errorResponse);
                    Toast.makeText(mContext, "Server error! $errorResponse", Toast.LENGTH_LONG)
                        .show()
                }

            })
    }

    fun unlockFrame(context: Context, frameArray: List <FrameData>, onSuccess:(status: Int) -> Unit ) {
        val params = RequestParams()
        val sharedPref = context.getSharedPreferences("sharedpref", Context.MODE_PRIVATE)
                ?: return
        val userId = sharedPref.getString("userid","")

        params.put("UserId", userId)
        params.put("FrameArray", Json.encodeToString(frameArray))
        params.setUseJsonStreamer(true)

        val client = AsyncHttpClient()
        client.addHeader("x-api-key", "7Nu7XDLhAb87fXii8IfgW5AhJeXvPSv05uzxXj64")
        client.post("https://zjil8ive37.execute-api.ca-central-1.amazonaws.com/dev/fm/unlockframe",
                params, object : AsyncHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>, response: ByteArray) {
                // called when response HTTP status is "200 OK"
                val strResponse = String(response)
                try {
                    onSuccess(200)
                } catch (e: SerializationException) { null }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, errorResponse: ByteArray, e: Throwable) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                val errorResponse = String(errorResponse)
                Toast.makeText(context, "Server error! $errorResponse", Toast.LENGTH_LONG).show()
            }

        })




    }
}