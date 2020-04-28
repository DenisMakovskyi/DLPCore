package ua.com.wl.dlp.data.api.responses.models.orders.order.pre_order

import com.google.gson.annotations.SerializedName

/**
 * @author Denis Makovskyi
 */

data class PreOrderTradeItem(
    @SerializedName("trade_item")
    val tradeId: Int,

    @SerializedName("count")
    val count: Int,

    @SerializedName("price")
    val price: String,

    @SerializedName("offer")
    val offer: PreOrderOffer)