package me.thanel.webmark.test.base

import android.content.ComponentName
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import me.thanel.webmark.share.QuickShareDetailsProvider

object TestQuickShareDetailsProvider : QuickShareDetailsProvider {
    override suspend fun getQuickShareIconAsync(component: ComponentName): Drawable? {
        return ColorDrawable(Color.RED)
    }
}
