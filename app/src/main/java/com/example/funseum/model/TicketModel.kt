package com.example.funseum.model

import android.content.Context
import android.widget.Toast
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject



class TicketModel(context: Context) {

    private val mContext : Context =  context


    fun getTicketList(onSuccess:(ticketList: List <TicketData>) -> Unit ) {
        var ticketList : List <TicketData> = ArrayList <TicketData>()
        val sharedPref = mContext.getSharedPreferences("sharedpref", Context.MODE_PRIVATE)
                ?: return
        val userId = sharedPref.getString("userid","")
        //fetching cards json from server
        val client = AsyncHttpClient()
        client.addHeader("x-api-key", "7Nu7XDLhAb87fXii8IfgW5AhJeXvPSv05uzxXj64")
        client.get("https://zjil8ive37.execute-api.ca-central-1.amazonaws.com/dev/fm/ticket?UserId=$userId",
            object : AsyncHttpResponseHandler() {

                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<Header>,
                    response: ByteArray
                ) {
                    // called when response HTTP status is "200 OK"
                    val strResponse = String(response)
                    val dbHandler: DatabaseHandler = DatabaseHandler(mContext)

                    try {
                        ticketList = Json.decodeFromString<List<TicketData>>(strResponse)

                    } catch (e: SerializationException) {
                        null
                    }
                    var status: Long = 0
                    status = dbHandler.deleteAllTicket().toLong()
                    for (i in ticketList.indices) {
                        status = dbHandler.insertTicket(ticketList[i])
                    }
                    onSuccess(ticketList)
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
}