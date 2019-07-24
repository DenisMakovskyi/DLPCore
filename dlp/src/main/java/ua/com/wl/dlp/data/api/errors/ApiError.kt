package ua.com.wl.dlp.data.api.errors

import com.google.gson.annotations.SerializedName

/**
 * @author Denis Makovskyi
 */

data class ApiError(@SerializedName(value = "type", alternate = ["status"]) val type: String)