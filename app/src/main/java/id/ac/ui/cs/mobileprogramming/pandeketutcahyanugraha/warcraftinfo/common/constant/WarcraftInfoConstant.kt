package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.constant

object WarcraftInfoConstant {
    // Blizzard Client ID is not a secret, therefore placing it here is not a security concern.
    const val API_CLIENT_ID = "c43ad750f8f6461b9a601da6028fa2b1"
    const val BASE_AUTH_API_URI = "https://us.battle.net"
    const val BASE_API_URI = "https://us.api.blizzard.com"
    const val AUTHORIZATION_CODE_REQUEST_PATH = "/oauth/authorize"
    const val OAUTH_REDIRECT_URI = "https://taranzhi.cahyanugraha12.site/api/login"
    const val AUTHORIZATION_CODE_REQUEST_RESPONSE_TYPE = "code"
    const val WOW_PROFILE_SCOPE = "wow.profile"
    const val LOCALE = "en_US"
    const val NAMESPACE_PROFILE = "profile-us"
    const val NAMESPACE_STATIC = "static-us"

    const val API_RESPONSE_SUCCESS = 200
    const val ACCESS_TOKEN_INVALID = 401

    const val MEDIA_AVATAR_KEY = "avatar"
    const val MEDIA_MAIN_KEY = "main"
    const val MEDIA_ICON_KEY = "icon"

    const val SLOT_HEAD = "HEAD"
    const val SLOT_NECK = "NECK"
    const val SLOT_SHOULDER = "SHOULDER"
    const val SLOT_BACK = "BACK"
    const val SLOT_CHEST = "CHEST"
    const val SLOT_SHIRT = "SHIRT"
    const val SLOT_TABARD = "TABARD"
    const val SLOT_WRIST = "WRIST"
    const val SLOT_HANDS = "HANDS"
    const val SLOT_WAIST = "WAIST"
    const val SLOT_LEGS = "LEGS"
    const val SLOT_FEET = "FEET"
    const val SLOT_FINGER_1 = "FINGER_1"
    const val SLOT_FINGER_2 = "FINGER_2"
    const val SLOT_TRINKET_1 = "TRINKET_1"
    const val SLOT_TRINKET_2 = "TRINKET_2"
    const val SLOT_MAIN_HAND = "MAIN_HAND"
    const val SLOT_OFF_HAND = "OFF_HAND"
}