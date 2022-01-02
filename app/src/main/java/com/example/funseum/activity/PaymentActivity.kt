package com.example.funseum.activity

import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

import com.example.funseum.R
import com.example.funseum.model.BookingModel
import com.example.funseum.model.EventData
import com.example.funseum.model.EventModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_booking.*
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*


class PaymentActivity : AppCompatActivity() {

    private var notificationId = 1
    private var notificationTitle = "We're looking forward to see you soon"
    private var notificationContent = "You got a booking to our museum soon!"
    private var notificationButton = "Default"

    override fun onCreate(savedInstanceState: Bundle?) {
        var eventInfo = EventData ()
        val bookingModel = BookingModel ()
        createNotificationChannel()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        eventInfo = intent.getSerializableExtra("eventInfo") as EventData
        val extras = intent.extras
        val quantity = extras!!.getString("quantity")
        val time = extras!!.getString("time")!!


        val totalPrice = eventInfo.price!! * quantity!!.toInt()
        val totalTax = totalPrice * eventInfo.tax!!

        lbnpaymentinfo.setText("\nEvent Name: " + eventInfo.eventName +"\n"
                + "Time: " + time + "\n"
                + "------------------------\n"
                + "Total Price: " + totalPrice.toString() + "\n"
                + "Tax: " + totalTax.toString() + "\n"
                + "------------------------\n"
                + "Total Charge: " + (totalPrice + totalTax).toString() + "\n\n"
        )

        btnpaynow.setOnClickListener {
            bookingModel.booking(this,eventInfo.eventId!!,eventInfo.eventName!!,quantity.toInt(),time)
            var builder = NotificationCompat.Builder(this, "MUSEUM_CHANNEL_ID")
                    .setSmallIcon(R.drawable.notification_icon)
                    .setContentTitle(notificationTitle)
                    .setContentText(notificationContent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            with(NotificationManagerCompat.from(this)) {
                // notificationId is a unique int for each notification that you must define
                notify(notificationId++, builder.build())
            }
            val intent = Intent(this, MainActivity::class.java)
            startActivity (intent)
        }





    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel("MUSEUM_CHANNEL_ID", name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }
}

