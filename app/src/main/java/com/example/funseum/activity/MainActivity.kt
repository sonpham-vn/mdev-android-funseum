package com.example.funseum.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.appcompat.app.AlertDialog

import com.example.funseum.R
import com.example.funseum.model.EventData
import com.example.funseum.model.EventModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val eventModel = EventModel (this)
        var eventInfo = EventData ()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        //set banner
        eventModel.getEvent {
            eventInfo = it
            Picasso.get().load(it.eventBanner).into(imageView)
        }


        //Button Card List
        btnbooking.setOnClickListener {
            val intent = Intent(this, BookingActivity::class.java)
            intent.putExtra("eventInfo",eventInfo)
            startActivity (intent)
        }

        btnticketlist.setOnClickListener {
            val intent = Intent(this, TicketActivity::class.java)
            startActivity (intent)
        }

        btnqrcode.setOnClickListener {
            val intent = Intent(this, QRCodeActivity::class.java)
            startActivity (intent)
        }

        btngallery.setOnClickListener {
            val intent = Intent(this, GalleryActivity::class.java)
            startActivity (intent)
        }


    }

    fun loadPlanner() {

    }
}