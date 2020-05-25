package ua.com.wl.dlp.data.api.responses.models.consumer.history.transactions

import com.google.gson.annotations.SerializedName

import ua.com.wl.dlp.data.api.responses.models.consumer.history.transactions.types.OperationType
import ua.com.wl.dlp.data.api.responses.models.consumer.history.transactions.endpoints.OfferTransactionDetails
import ua.com.wl.dlp.data.api.responses.models.consumer.history.transactions.endpoints.ArticleTransactionDetails

/**
 * @author Denis Makovskyi
 */

data class BalanceChange(
    @SerializedName("operation_type")
    val type: OperationType,

    @SerializedName("id")
    val id: Int,

    @SerializedName("amount")
    val amount: Int,

    @SerializedName("account")
    val account: Int,

    @SerializedName("transfer")
    val transfer: Int,

    @SerializedName("initial_value")
    val initialBalance: Long,

    @SerializedName("resulting_value")
    val resultingBalance: Long,

    @SerializedName("initial_value_money")
    val initialMoneyAmount: String,

    @SerializedName("resulting_value_money")
    val resultingMoneyAmount: String,

    @SerializedName("comment")
    val comment: String?,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("offer")
    val offerDetails: OfferTransactionDetails?,

    @SerializedName("news_item")
    val articleDetails: ArticleTransactionDetails?)