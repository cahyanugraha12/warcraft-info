package id.ac.ui.cs.mobileprogramming.pandeketutcahyanugraha.warcraftinfo.common.api

sealed class APIResponse<T>(
    val data: T? = null,
    val code: Int? = null
) {
    class Success<T>(data: T, code: Int) : APIResponse<T>(data, code)
    class Failed<T>(data: T? = null, code: Int? = null) : APIResponse<T>(data, code)
}