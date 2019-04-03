package me.thanel.webmark.ext

import androidx.lifecycle.MutableLiveData

fun <T : Any> mutableLiveDataOf(value: T) = MutableLiveData<T>().apply {
    this.value = value
}
