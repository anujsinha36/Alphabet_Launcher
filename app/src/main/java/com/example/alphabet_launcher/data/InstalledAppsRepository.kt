package com.example.alphabet_launcher.data

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import kotlin.math.roundToInt

class InstalledAppsRepository(context: Context) {

    private val appContext = context.applicationContext
    private val packageManager = appContext.packageManager

    @Volatile
    private var cachedApps: List<AppInfo>? = null

    fun getInstalledApps(): List<AppInfo> {
        cachedApps?.let { return it }

        val iconSizePx = (ICON_SIZE_DP * appContext.resources.displayMetrics.density).roundToInt()
        val launcherIntent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)

        val apps = packageManager.queryIntentActivities(launcherIntent, 0)
            .asSequence()
            .mapNotNull { resolveInfo ->
                val label = resolveInfo.loadLabel(packageManager).toString().trim()
                if (label.isEmpty() || resolveInfo.activityInfo == null) return@mapNotNull null
                val packageName = resolveInfo.activityInfo.packageName
                if (packageName == appContext.packageName) return@mapNotNull null
                AppInfo(
                    label = label,
                    packageName = packageName,
                    icon = resolveInfo.loadIcon(packageManager).toBitmap(iconSizePx),
                )
            }
            .distinctBy { it.packageName }
            .sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.label })
            .toList()

        cachedApps = apps
        return apps
    }

    fun launchIntentFor(packageName: String): Intent? =
        packageManager.getLaunchIntentForPackage(packageName)

    /**
     * Opens the app. Returns false if the app no longer has a launchable
     * activity (e.g. it was uninstalled after the list was cached).
     */
    fun launchApp(app: AppInfo): Boolean {
        val intent = packageManager.getLaunchIntentForPackage(app.packageName) ?: return false
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        appContext.startActivity(intent)
        return true
    }

    private fun Drawable.toBitmap(sizePx: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        setBounds(0, 0, sizePx, sizePx)
        draw(canvas)
        return bitmap
    }

    private companion object {
        const val ICON_SIZE_DP = 48f
    }
}