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



class QRModel()  {

    val currentStatus: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun qrcall(context: Context, qrcode: String, onSuccess:(qrInfo: QRData) -> Unit) {
        val params = RequestParams()
        val sharedPref = context.getSharedPreferences("sharedpref", Context.MODE_PRIVATE)
                ?: return
        val userId = sharedPref.getString("userid","")

        params.put("UserId", userId)
        params.put("QrCode", qrcode)
        params.setUseJsonStreamer(true)

        val client = AsyncHttpClient()
        client.addHeader("x-api-key", "7Nu7XDLhAb87fXii8IfgW5AhJeXvPSv05uzxXj64")
        client.post("https://zjil8ive37.execute-api.ca-central-1.amazonaws.com/dev/fm/qr",
            params, object : AsyncHttpResponseHandler() {

                override fun onSuccess(statusCode: Int, headers: Array<Header>, response: ByteArray) {
                    // called when response HTTP status is "200 OK"
                    val strResponse = String(response)
                    val qrInfo = Json.decodeFromString<QRData>(strResponse)
                    onSuccess(qrInfo)
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