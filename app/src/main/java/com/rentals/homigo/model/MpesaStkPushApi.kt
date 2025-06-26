package com.rentals.homigo.model

import com.rentals.homigo.data.StkPushRequest
import com.rentals.homigo.data.StkPushResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface MpesaStkPushApi {
    interface MpesaStkPushApi {
        @Headers("Content-Type: application/json")
        @POST("mpesa/stkpush/v1/processrequest")
        fun sendStkPush(
            @retrofit2.http.Header("Authorization") token: String,
            @Body stkPushRequest: StkPushRequest
        ): Call<StkPushResponse>
    }
}