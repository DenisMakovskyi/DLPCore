package ua.com.wl.dlp.data.events.prefs

import ua.com.wl.dlp.data.api.responses.models.auth.City

/**
 * @author Denis Makovskyi
 */

class ConsumerPrefsEvent(
    saved: Boolean,
    val field: Field,
    val fieldValue: FieldValue) : PrefsEvent(saved) {

    enum class Field {
        FIRST_NAME,
        PATRONYMIC,
        LAST_NAME,
        CITY,
        PHONE,
        EMAIL,
        BALANCE,
        QR_CODE,
        INVITE_CODE,
        REFERRAL_CODE
    }

    sealed class FieldValue {

        data class LongValue(val value: Long?) : FieldValue()

        data class StringValue(val value: String?) : FieldValue()

        data class CityObjectValue(val value: City?) : FieldValue()
    }
}