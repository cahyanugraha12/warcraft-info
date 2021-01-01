package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.constant

object WarcraftInfoConstant {
    // Shared Preferences Related Constants
    const val COMMON_PREFERENCES = "id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.commonpreferences"
    const val ACCESS_TOKEN_KEY = "access_token"
    const val STATIC_TOKEN_KEY = "static_token"
    const val USE_DUMMY_DATA = "use_dummy_data"
    const val AUCTION_HOUSE_DATA_VALIDITY_KEY = "auction_house_data_validity"
    const val SOUNDTRACK_METADATA_KEY = "soundtrack_metadata"
    const val SOUNDTRACK_INDEX_KEY = "soundtrack_index"

    // API Related Constants
    // Blizzard Client ID is not a secret, therefore placing it here is not a security concern.
    const val API_CLIENT_ID = "c43ad750f8f6461b9a601da6028fa2b1"
    const val BASE_AUTH_API_URI = "https://us.battle.net"
    const val BASE_API_URI = "https://us.api.blizzard.com"
    const val TARANZHI_API_URI = "https://taranzhi.cahyanugraha12.site"
    const val AUTHORIZATION_CODE_REQUEST_PATH = "/oauth/authorize"
    const val OAUTH_REDIRECT_URI = "https://taranzhi.cahyanugraha12.site/api/login"
    const val AUTHORIZATION_CODE_REQUEST_RESPONSE_TYPE = "code"
    const val WOW_PROFILE_SCOPE = "wow.profile"
    const val LOCALE = "en_US"
    const val NAMESPACE_PROFILE = "profile-us"
    const val NAMESPACE_DYNAMIC = "dynamic-us"
    const val NAMESPACE_STATIC = "static-us"
    const val API_RESPONSE_SUCCESS = 200
    const val ACCESS_TOKEN_INVALID = 401

    const val START_ACTIVITY_FOR_RESULT_REQUEST_CODE_LOGIN = 10

    // Media Related Constants
    const val MEDIA_AVATAR_KEY = "avatar"
    const val MEDIA_MAIN_KEY = "main"
    const val MEDIA_ICON_KEY = "icon"

    // Character Equipment Related Constant
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

    // Auction House Related Constants
    const val AUCTION_HOUSE_JOB_NOTIFICATION_CHANNEL_ID = "120499"
    const val AUCTION_HOUSE_JOB_NOTIFICATION_ID = 120500
    const val AUCTION_HOUSE_JOB_SERVICE_JOB_ID = 120501

    // Soundtrack Related Constants
    const val BROADCAST_PLAY_NEW_SOUNDTRACK = "id.ac.ui.cs.mobileprogramming.warcraftinfo.soundtrack.BROADCAST_PLAY_NEW_SOUNDTRACK"
    const val BROADCAST_PAUSE_SOUNDTRACK = "id.ac.ui.cs.mobileprogramming.warcraftinfo.soundtrack.BROADCAST_PAUSE_SOUNDTRACK"
    const val BROADCAST_RESUME_SOUNDTRACK = "id.ac.ui.cs.mobileprogramming.warcraftinfo.soundtrack.BROADCAST_RESUME_SOUNDTRACK"
    const val BROADCAST_PREVIOUS_SOUNDTRACK = "id.ac.ui.cs.mobileprogramming.warcraftinfo.soundtrack.BROADCAST_PREVIOUS_SOUNDTRACK"
    const val BROADCAST_NEXT_SOUNDTRACK = "id.ac.ui.cs.mobileprogramming.warcraftinfo.soundtrack.BROADCAST_NEXT_SOUNDTRACK"
    const val ACTION_PLAY = "id.ac.ui.cs.mobileprogramming.warcraftinfo.soundtrack.ACTION_PLAY"
    const val ACTION_PAUSE = "id.ac.ui.cs.mobileprogramming.warcraftinfo.soundtrack.ACTION_PAUSE"
    const val ACTION_PREVIOUS = "id.ac.ui.cs.mobileprogramming.warcraftinfo.soundtrack.ACTION_PREVIOUS"
    const val ACTION_NEXT = "id.ac.ui.cs.mobileprogramming.warcraftinfo.soundtrack.ACTION_NEXT"
    const val ACTION_STOP = "id.ac.ui.cs.mobileprogramming.warcraftinfo.soundtrack.ACTION_STOP"
    const val SOUNDTRACK_NOTIFICATION_CHANNEL_ID = "28122020"
    const val SOUNDTRACK_NOTIFICATION_ID = 28122020

    // Location Related Constants
    const val ACCESS_LOCATION_REQUEST_CODE = 1204
}