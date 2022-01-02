package com.example.funseum.model

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json



class BookingModel() {

    val currentStatus: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun booking(context: Context, eventId: String, eventName: String, quantity: Int, time: String) {
        val params = RequestParams()
        val sharedPref = context.getSharedPreferences("sharedpref", Context.MODE_PRIVATE)
                ?: return
        val userId = sharedPref.getString("userid","")

        params.put("UserId", userId)
        params.put("EventId", eventId)
        params.put("EventName", eventName)
        params.put("Quantity", quantity)
        params.put("Time", time)
        params.setUseJsonStreamer(true)

        val client = AsyncHttpClient()
        client.addHeader("x-api-key", "7Nu7XDLhAb87fXii8IfgW5AhJeXvPSv05uzxXj64")
        client.post("https://zjil8ive37.execute-api.ca-central-1.amazonaws.com/dev/fm/booking",
                params, object : AsyncHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<Header>, response: ByteArray) {
                // called when response HTTP status is "200 OK"
                val strResponse = String(response)

            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, errorResponse: ByteArray, e: Throwable) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                val errorResponse = String(errorResponse)
                Toast.makeText(context, "Server error! $errorResponse", Toast.LENGTH_LONG).show()
                currentStatus.setValue("Error")
            }

        })


    }



}