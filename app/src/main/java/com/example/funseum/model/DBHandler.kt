package com.example.funseum.model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler (context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {

    private val mContext : Context =  context

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "TicketDB"

        private val TABLE_TICKET = "Ticket"
        private val KEY_ID = "Id"
        private val KEY_TICKET_ID = "TicketId"
        private val KEY_USER_ID = "UserId"
        private val KEY_EVENT_ID = "EventId"
        private val KEY_EVENT_NAME = "EventName"
        private val KEY_QUANTITY = "Quantity"
        private val KEY_BOOKING_TIME = "BookingTime"
        private val KEY_CREATED_DATE = "CreatedDate"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        //creating table with fields
        val CREATE_ID_TABLE = ("CREATE TABLE " + TABLE_TICKET + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TICKET_ID + " TEXT UNIQUE,"
                + KEY_USER_ID + " TEXT,"
                + KEY_EVENT_ID + " TEXT,"
                + KEY_EVENT_NAME + " TEXT,"
                + KEY_QUANTITY + " INTERGER,"
                + KEY_BOOKING_TIME + " TEXT,"
                + KEY_CREATED_DATE + " TEXT" + ")")
        db?.execSQL(CREATE_ID_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //create new db when upgrade //not used
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_TICKET)
        onCreate(db)
    }


    //method to insert data
    fun insertTicket(ticketValue: TicketData):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(KEY_TICKET_ID, ticketValue.TicketId)
        contentValues.put(KEY_USER_ID, ticketValue.UserId)
        contentValues.put(KEY_EVENT_ID, ticketValue.EventId)
        contentValues.put(KEY_EVENT_NAME, ticketValue.EventName)
        contentValues.put(KEY_QUANTITY, ticketValue.Quantity)
        contentValues.put(KEY_BOOKING_TIME, ticketValue.BookingTime)
        contentValues.put(KEY_CREATED_DATE, ticketValue.CreatedDate)
        // Inserting Row
        val success = db.insert(TABLE_TICKET, null, contentValues)
        db.close()
        return success
    }


    //method to read data
    fun getTicket():List<TicketData>{
        val sharedPref = mContext.getSharedPreferences("sharedpref", Context.MODE_PRIVATE)
                ?: return ArrayList()
        val userId = sharedPref.getString("userid","")
        val ticketList:ArrayList<TicketData> = ArrayList<TicketData>()
        val selectQuery = "SELECT  * FROM $TABLE_TICKET WHERE $KEY_USER_ID = '$userId'"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            return ArrayList()
        }

        // parse querydata to data class array

        var Id: Int
        var TicketId: String
        var UserId: String
        var EventId: String
        var EventName: String
        var Quantity: Int
        var BookingTime: String
        var CreatedDate: String

        if (cursor.moveToFirst()) {
            do {
                Id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                TicketId = cursor.getString(cursor.getColumnIndex(KEY_TICKET_ID))
                UserId = cursor.getString(cursor.getColumnIndex(KEY_USER_ID))
                EventId = cursor.getString(cursor.getColumnIndex(KEY_EVENT_ID))
                EventName = cursor.getString(cursor.getColumnIndex(KEY_EVENT_NAME))
                Quantity = cursor.getInt(cursor.getColumnIndex(KEY_QUANTITY))
                BookingTime = cursor.getString(cursor.getColumnIndex(KEY_BOOKING_TIME))
                CreatedDate = cursor.getString(cursor.getColumnIndex(KEY_CREATED_DATE))


                val ticketValue= TicketData(
                        TicketId = TicketId,
                        UserId = UserId,
                        EventId = EventId,
                        EventName = EventName,
                        Quantity = Quantity,
                        BookingTime = BookingTime,
                        CreatedDate = CreatedDate
                )
                ticketList.add(ticketValue)
            } while (cursor.moveToNext())
        }
        return ticketList
    }



    //method to delete data
    fun deleteAllTicket():Int{
        val sharedPref = mContext.getSharedPreferences("sharedpref", Context.MODE_PRIVATE)
                ?: return -1
        val userId = sharedPref.getString("userid","")
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_USER_ID, userId) // CardData UserId
        // Deleting Row
        val success = db.delete(TABLE_TICKET, "$KEY_USER_ID='$userId'",null)
        db.close()
        return success
    }



}
