package me.thanel.webmark.ext

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders

inline fun <reified T : ViewModel> Fragment.viewModel() = lazy {
    ViewModelProviders.of(this).get(T::class.java)
}
