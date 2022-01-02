package com.example.funseum.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.appcompat.app.AlertDialog

import com.example.funseum.R
import com.example.funseum.model.EventData
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_booking.*
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*


class BookingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        var eventInfo = EventData ()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)
        eventInfo = getIntent().getSerializableExtra("eventInfo") as EventData;
        lbneventname.setText(eventInfo.eventName)
        lbnprice.setText("Price: " + eventInfo.price.toString() + " (Tax not included)")
        Picasso.get().load(eventInfo.eventBanner).into(imageview)

        txtTime.setOnClickListener {
            DateTimePicker(this, true){
                val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault())
                txtTime.setText(sdf.format(it.calendar.time))
            }.show()
        }

        btnpayment.setOnClickListener {
            val intent = Intent(this, PaymentActivity::class.java)
            intent.putExtra("eventInfo",eventInfo)
            intent.putExtra("quantity",txtquantity.text.toString())
            intent.putExtra("time",txtTime.text.toString())
            startActivity (intent)
        }

    }
}

class DateTimePicker(val context: Context, var pickTime:Boolean = false, var calendar: Calendar = Calendar.getInstance(),
                     var callback: (it : DateTimePicker) -> Unit) {

    companion object{
        @JvmStatic
        fun getFormat(format : String) : String{
            when(format){
                "d" -> return "dd/MM/yyyy"
                "t" -> return "HH:mm"
                "dt" -> return "dd/MM/yyyy HH:mm"
            }
            return "dd/MM/yyyy"
        }
    }

    fun show(){
        val startYear = calendar.get(Calendar.YEAR)
        val startMonth = calendar.get(Calendar.MONTH)
        val startDay = calendar.get(Calendar.DAY_OF_MONTH)
        val startHour = calendar.get(Calendar.HOUR_OF_DAY)
        val startMinute = calendar.get(Calendar.MINUTE)

        DatePickerDialog(context, DatePickerDialog.OnDateSetListener { _, year, month, day ->
            if(pickTime) {
                TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                    calendar.set(year, month, day, hour, minute)
                    callback(this)
                }, startHour, startMinute, true).show()
            } else {
                calendar.set(year,month,day)
                callback(this)
            }
        }, startYear, startMonth, startDay).show()
    }

    fun showTime(){
        val startYear = calendar.get(Calendar.YEAR)
        val startMonth = calendar.get(Calendar.MONTH)
        val startDay = calendar.get(Calendar.DAY_OF_MONTH)
        val startHour = calendar.get(Calendar.HOUR_OF_DAY)
        val startMinute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            calendar.set(startYear, startMonth, startDay, hour, minute)
            callback(this)
        }, startHour, startMinute, true).show()
    }
}