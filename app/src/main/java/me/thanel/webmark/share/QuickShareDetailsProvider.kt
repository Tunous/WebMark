package me.thanel.webmark.share

import android.content.ComponentName
import android.graphics.drawable.Drawable

interface QuickShareDetailsProvider {
    suspend fun getQuickShareIconAsync(component: ComponentName): Drawable?
}
