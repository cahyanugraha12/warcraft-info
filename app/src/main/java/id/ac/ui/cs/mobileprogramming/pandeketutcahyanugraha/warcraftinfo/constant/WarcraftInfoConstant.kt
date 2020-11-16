package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.constant

object WarcraftInfoConstant {
    // Blizzard Client ID is not a secret, therefore placing it here is not a security concern.
    const val API_CLIENT_ID = "c43ad750f8f6461b9a601da6028fa2b1"
    //TODO MOVE TO SHARED PREFERENCE AND GENERATE NEW SECRET
    const val API_CLIENT_SECRET = "J9V9eqQqA48MRwE5ZxZ7h30K7db1JZz1"
    const val BASE_API_URI = "https://us.battle.net"
    const val AUTHORIZATION_CODE_REQUEST_PATH = "/oauth/authorize"
    const val OAUTH_REDIRECT_URI = "https://taranzhi.cahyanugraha12.site/api/login"
    const val AUTHORIZATION_CODE_REQUEST_RESPONSE_TYPE = "code"
    const val ACCESS_TOKEN_GRANT_TYPE = "authorization_code"
    const val WOW_PROFILE_SCOPE = "wow.profile"
}