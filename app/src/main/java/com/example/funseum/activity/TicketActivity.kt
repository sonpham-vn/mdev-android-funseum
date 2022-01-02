package com.example.funseum.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.appcompat.app.AlertDialog

import com.example.funseum.R
import com.example.funseum.adapter.TicketListAdapter
import com.example.funseum.model.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_ticket.*


class TicketActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val ticketModel = TicketModel (this)
        var ticketList : List<TicketData> = ArrayList <TicketData> ()
        val dbHandler = DatabaseHandler (this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket)
        //get ticket list from db first
        ticketList = dbHandler.getTicket()

        val cardListAdapter = TicketListAdapter(
                this,
                ticketList
        )
        ticketlistview.adapter = cardListAdapter

        //get ticket list online
        ticketModel.getTicketList {
            ticketList = it
            val cardListAdapter = TicketListAdapter(
                this,
                ticketList
            )
            ticketlistview.adapter = cardListAdapter
        }



    }

}