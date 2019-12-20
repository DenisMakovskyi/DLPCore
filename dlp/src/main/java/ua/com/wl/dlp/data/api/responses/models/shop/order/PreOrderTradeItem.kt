package ua.com.wl.dlp.data.api.responses.models.shop.order

import com.google.gson.annotations.SerializedName

import ua.com.wl.dlp.data.api.models.order.BasePreOrderTradeItem

/**
 * @author Denis Makovskyi
 */

data class PreOrderTradeItem constructor(
    @SerializedName("price")
    val price: String,

    @SerializedName("offer")
    val offer: PreOrderOffer) : BasePreOrderTradeItem()