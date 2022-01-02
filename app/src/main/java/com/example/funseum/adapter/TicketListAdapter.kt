package com.example.funseum.adapter
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.funseum.R
import com.example.funseum.model.TicketData


class TicketListAdapter(private val context: Activity, private val ticketList: List<TicketData>)
    : ArrayAdapter<TicketData>(context, R.layout.customview_ticketlist, ticketList) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.customview_ticketlist, null, true)

        val ticketDetail = rowView.findViewById(R.id.ticketdetail) as TextView

        ticketDetail.text = "Ticket Code: ${ticketList[position].TicketId!!.substring(0, 7)} \n" +
                "Event: ${ticketList[position].EventName} \n" +
                "Time: ${ticketList[position].BookingTime} \n" +
                "Number of people: ${ticketList[position].Quantity.toString()} \n"
        return rowView
    }
}