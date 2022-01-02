package com.example.funseum.model

import kotlinx.serialization.Serializable

@Serializable
data class EventData(val eventId: String? = "",
                    val eventName: String? = "",
                     val eventBanner: String? = "",
                     val currency: String? = "",
                    val price: Int? = null,
                    val tax: Float? = null,
                    ): java.io.Serializable


@Serializable
data class UserFrameData(val UserId: String? = "",
                         var Point: Int? = 0,
                         val Frame: List <FrameData>? = null

): java.io.Serializable

@Serializable
data class FrameData(val frameId: String? = "",
                     val frameName: String? = "",
                     val frameFile: String? = "",
                     var Active: Boolean? = false
): java.io.Serializable

@Serializable
data class TicketData(val TicketId: String? = "",
                     val UserId: String? = "",
                     val EventId: String? = "",
                     val EventName: String? = "",
                     val Quantity: Int? = null,
                      val BookingTime: String? = "",
                     val CreatedDate: String? = "",
): java.io.Serializable

@Serializable
data class QRData(val statusCode: Int? = null,
                  val songUrl: String? = ""
): java.io.Serializable







