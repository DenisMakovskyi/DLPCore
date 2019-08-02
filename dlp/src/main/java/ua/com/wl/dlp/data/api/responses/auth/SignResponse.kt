package ua.com.wl.dlp.data.api.responses.auth

import com.google.gson.annotations.SerializedName

data class SignResponse(
    @SerializedName("token") val token: String,
    @SerializedName("refresh_token") val refreshToken: String)