package me.thanel.webmark.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein

abstract class BaseFragment(
    @LayoutRes private val layoutResId: Int
) : Fragment(), KodeinAware {

    override val kodein by kodein()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutResId, container, false)
    }
}
