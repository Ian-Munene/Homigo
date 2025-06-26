package com.rentals.homigo.model

import com.rentals.homigo.data.AccessTokenResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface MpesaAuthApi {
    @GET("oauth/v1/generate?grant_type=client_credentials")
    fun getAccessToken(
        @Header("Authorization") authHeader: String
    ): Call<AccessTokenResponse>
}