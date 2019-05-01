package me.thanel.webmark.ui.view

import android.content.Context
import android.util.AttributeSet
import android.widget.Checkable
import androidx.appcompat.widget.AppCompatImageView

class CheckableImageView(context: Context, attrs: AttributeSet) :
    AppCompatImageView(context, attrs),
    Checkable {

    private var checked: Boolean = false

    var onCheckedChanged: (Boolean) -> Unit = {}

    init {
        setOnClickListener {
            toggle()
        }
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        if (isChecked) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET)
        }
        return drawableState
    }

    override fun toggle() {
        isChecked = !checked
    }

    override fun isChecked(): Boolean {
        return checked
    }

    override fun setChecked(checked: Boolean) {
        if (this.checked == checked) {
            return
        }
        this.checked = checked
        refreshDrawableState()
        onCheckedChanged(checked)
    }

    companion object {
        private val CHECKED_STATE_SET = intArrayOf(android.R.attr.state_checked)
    }
}
