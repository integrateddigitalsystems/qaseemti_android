package com.ids.qasemti.utils

/**
 * Created by Ibrahim on 8/23/2017.
 */

object AppConstants {



    const val MDPI = "32"
    const val HDPI = "24"
    const val XHDPI = "48"
    const val XXHDPI = "72"
    const val XXXHDPI = "96"

    const val ORDER_TYPE_ACTIVE = "active"
    const val ORDER_TYPE_COMPLETED ="completed"
    const val ORDER_TYPE_UPCOMING ="upcoming"
    const val ORDER_TYPE_CANCELED ="cancelled"
    const val ORDER_TYPE_FAILED="failed"
    const val CENTER_WHITE = 2
    const val LEFT_BLACK = 4
    const val SERVICES = 10
    const val FAILURE_REQUEST = 0

    const val OneDecimal = "#,##0.0"
    const val TwoDecimal = "#,##0.00"
    const val ThreeDecimal = "#.000"
    const val OneDecimalThousandsSeparator = "#,###.0"
    const val OneDecimalSeparator = "#.0"
    const val NoDecimalSeparator = "#"
    const val TwoDecimalThousandsSeparator = "#,##0.00"
    const val ThreeDecimalThousandsSeparator = "#,##0.000"
    const val NoDecimalThousandsSeparator = "#,###"

    const val LANG_ENGLISH = "en"
    const val LANG_ARABIC = "ar"
    const val MULTI_LINKS = "MultiServersLink"
    const val SELECTED_LANGUAGE = "key_language_code"
    const val FIRST_TIME = "firstTime"
    const val BASE_URL = "base_url"
    const val DEVICE_ID = "DEVICEID"
    const val FIREBASE_URLS = "urls"
    const val USER_ID = "user_id"
    const val SIGNED_IN ="signed_in"
    const val ARRAY_CARTS ="array_carts"
    const val PHONE_NUMBER ="phone_number"
    const val MAPPING ="mapping"
    const val PERMISSION ="permission"
    const val TERMS_CONDITIONS = "termsConditions"
    const val TRACKING_ORDER_ID = "trackingOrderId"
    const val GENERAL_NOTF ="generalNotf"
    const val REQUEST_LOCATION_PERMISSION = 111
    const val NOTF_TYPE = "notf_type"
    const val FIREBASE_FORCE_UPDATE = "android_force_update"
    const val FIREBASE_VERSION_CODE = "android_version_code"
    const val FIREBASE_LOCALIZE = "localize_msg"
    const val FIREBASE_GOVS = "governantes_kuwait"
    const val BANNER_TIME = "banner_time"
    const val CURRENCY = "currency"
    const val FIREBASE_LINKS ="htmlUrls"
    const val FIREBASE_LINKS_STAGING ="htmlPageUrlStaging"
    const val COORDINATES = "kuwait_coordinates"
    const val FIREBASE_PARAMS ="paymentGatewayParameters"
    const val FIREBASE_ENABLE = "enableMultiCountry"
    const val FIREBASE_COUNTRY_NAME_CODE = "countryCodeMap"
    const val FIREBASE_SALT = "salt"
    const val FIREBASE_MOBILE_CONFIGURATION = "mobileConfigurations"
    const val FRAGMENT_HOME_CLIENT ="fragmentHomeClient"
    const val FRAGMENT_HOME_SP ="fragmentHomeSP"
    const val FRAGMENT_SERVICE_DETAILS="fragmentServiceDetails"
    const val FRAGMENT_MY_SERVICES ="fragmentMyServices"
    const val FRAGMENT_SETTINGS = "fragmentSttings"
    const val FRAGMENT_ORDER = "fragmentOrder"
    const val FRAGMENT_ORDER_FROM="fragmentOrderFromService"
    const val FRAGMENT_PROFILE = "fragmentProfile"
    const val FRAGMENT_ACCOUNT = "fragmentAccount"
    const val FRAGMENT_NOTFICATIONS = "fragmentNotifications"
    const val FRAGMENT_PROD = "fragmentProducts"
    const val FRAGMENT_CART="fragmentCart"
    const val CHECKOUT = "checkout"
    const val TRACKING_LIST = "trackingList"
    const val APP_ALIVE="appAlive"

    const val UPDATE_PROFILE_CLIENT = 0
    const val UPDATE_PROFILE_SERVICE_PROVIDER = 1
    const val API_USER_STATUS = 2
    const val API_USER_INFO = 3
    const val ORDER_BY_ID = 4
    const val MAP_SEARCH = 5
    const val GET_CATEGORIES = 6
    const val UPDATE_DEVICE =7
    const val ADDRESS_GEO = 8
    const val TYPE_PURCHASE = "purchase"
    const val TYPE_RENTAL = "rental"
    const val TYPE_RENTAL_ID ="343"
    const val TYPE_PURCHASE_ID ="345"

    const val MEDIA_TYPE_IMAGE = 1
    const val MEDIA_TYPE_YOUTUBE = 2

    const val SP_FOUND = "sp_found"
    const val ORDER_ID = "order_id"

    const val PLACE_ORDER_NO_SP = 1
    const val PLACE_ORDER_AVAILABLE_IN= 2
    const val PLACE_ORDER_AVAILABLE_OUT = 3

    const val NOTF_TYPE_BROADCAST_ORDERS = 1
    const val NOTF_TYPE_ACCOUNT_ACTIVATE_DEACTIVATE = 2
    const val NOTF_TYPE_NORMAL = 3
    const val NOTF_TYPE_SERVICE = 4
    const val NOTF_TYPE_ACCEPT_ORDER = 5
    const val NOTF_TYPE_SUGGEST_NEW_DATE = 6
    const val NOTF_PAYMENT_ADDED = 7

    const val PAYMENT_SUCCESS = "CAPTURED"
}
