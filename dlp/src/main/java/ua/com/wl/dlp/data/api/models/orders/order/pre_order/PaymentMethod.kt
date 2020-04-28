package ua.com.wl.dlp.data.api.models.orders.order.pre_order

import com.google.gson.annotations.SerializedName

/**
 * @author Denis Makovskyi
 */

enum class PaymentMethod(val value: String) {
    @SerializedName("CASH")
    CASH("CASH"),
    @SerializedName("BY_CARD")
    CARD("BY_CARD"),
    @SerializedName("ONLINE")
    ONLINE("ONLINE")
}