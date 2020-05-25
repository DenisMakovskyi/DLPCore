package ua.com.wl.dlp.data.api.responses

/**
 * @author Denis Makovskyi
 */

object ResponseType {

    const val OK = "OK"

    const val TOKEN_REQUIRED = "TOKEN_REQUIRED"
    const val TOKEN_INVALID = "ERROR_JWT_INVALID"
    const val TOKEN_EXPIRED = "ERROR_JWT_EXPIRED"

    const val MOBILE_PHONE_REQUIRED = "MOBILE_PHONE_REQUIRED"
    const val MOBILE_PHONE_INVALID = "MOBILE_PHONE_INVALID"
    const val MOBILE_PHONE_REGISTERED = "MOBILE_PHONE_REGISTERED"

    const val SMS_DELIVERY_FAILURE = "SMS_DELIVERY_FAILURE"
    const val SMS_RETRIEVAL_ATTEMPTS_EXCEEDED = "PASS_RETRIEVE_LIMIT_EXCEEDED"
    const val SMS_AUTH_NOT_SUPPORTED_FOR_BUSINESS = "SMS_AUTH_NOT_SUPPORTED_FOR_BUSINESS"

    const val PASSWORD_REQUIRED = "PASSWORD_REQUIRED"
    const val PASSWORD_INVALID = "WRONG_PASSWORD"

    const val PRECONDITION_REQUEST_SPECIFIED = "PRECONDITION_REQUEST_SPECIFIED"
    const val MANUAL_REGISTRATION_ALREADY_SPECIFIED = "MANUAL_REGISTRATION_ALREADY_SPECIFIED"

    const val CITY_REQUIRED = "CITY_REQUIRED"

    const val CONSUMER_NOT_FOUND = "CONSUMER_NOT_FOUND"
    const val CONSUMER_ACCOUNT_DISABLED = "CONSUMER_ACCOUNT_DISABLED"
    const val CONSUMER_CREDENTIAL_INVALID = "CONSUMER_INVALID_CREDENTIAL"
    const val CONSUMER_SELF_INVITE = "CONSUMER_SELF_INVITE"
    const val CONSUMER_ALREADY_INVITED = "CONSUMER_ALREADY_INVITED"
    const val CONSUMER_CAN_NOT_BE_INVITED = "CONSUMER_NOT_BE_INVITED"
    const val MULTIPLE_CONSUMERS_RETURNED = "MULTIPLE_CONSUMERS_RETURNED"

    const val INVITE_CODE_REQUIRED = "INVITE_CODE_REQUIRED"
    const val INVITE_CODE_NOT_FOUND = "INVITE_CODE_NOT_FOUND"

    const val SHOP_REQUIRED = "SHOP_REQUIRED"

    const val PRE_ORDER_INVALID_TRADE_ITEMS = "PRE_ORDER_TRADE_ITEMS_INVALID"
    const val PRE_ORDER_INVALID_DELIVERY_TYPE = "DELIVERY_TYPE_INVALID_CHOICE"
    const val PRE_ORDER_ILLEGAL_FLAG_USE_BONUSES = "PAY_WITH_BONUSES_INVALID"
    const val PRE_ORDER_INCORRECT_READINESS_TIME = "READINESS_TIME_INVALID"

    const val REMOTE_SERVER_UNREACHABLE = "REMOTE_SERVER_UNREACHABLE"
    const val REMOTE_SERVER_SQL_ERROR = "REMOTE_SERVER_SQL_ERROR"
}