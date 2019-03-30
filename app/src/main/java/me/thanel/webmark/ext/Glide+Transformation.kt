package me.thanel.webmark.ext

import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.BaseRequestOptions
import com.bumptech.glide.request.RequestOptions

fun <T : BaseRequestOptions<T>> BaseRequestOptions<T>.roundedCorners(roundingRadius: Int): T =
    apply(RequestOptions.bitmapTransform(RoundedCorners(roundingRadius)))
