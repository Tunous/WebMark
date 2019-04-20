package me.thanel.webmark.ui.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMarginsRelative
import com.google.android.material.snackbar.Snackbar
import me.thanel.webmark.R

class AttachedSnackbarBehavior(
    context: Context,
    attrs: AttributeSet
) : CoordinatorLayout.Behavior<View>(context, attrs) {

    private val snackBarBottomMargin =
        context.resources.getDimensionPixelSize(R.dimen.snackbar_bottom_margin)

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        if (dependency is Snackbar.SnackbarLayout) {
            updateSnackbar(child, dependency)
        }
        return super.layoutDependsOn(parent, child, dependency)
    }

    private fun updateSnackbar(child: View, snackbarLayout: Snackbar.SnackbarLayout) {
        if (snackbarLayout.layoutParams !is CoordinatorLayout.LayoutParams) return
        snackbarLayout.updateLayoutParams<CoordinatorLayout.LayoutParams> {
            anchorId = child.id
            updateMarginsRelative(bottom = child.height + snackBarBottomMargin)
        }
    }
}
