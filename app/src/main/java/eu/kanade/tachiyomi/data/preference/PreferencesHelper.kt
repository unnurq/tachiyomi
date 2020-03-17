package eu.kanade.tachiyomi.data.preference

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Environment
import androidx.preference.PreferenceManager
import com.f2prateek.rx.preferences.Preference
import com.f2prateek.rx.preferences.RxSharedPreferences
import eu.kanade.tachiyomi.R
import eu.kanade.tachiyomi.data.preference.PreferenceKeys as Keys
import eu.kanade.tachiyomi.data.preference.PreferenceValues as Values
import eu.kanade.tachiyomi.data.track.TrackService
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale

fun <T> Preference<T>.getOrDefault(): T = get() ?: defaultValue()!!

fun Preference<Boolean>.invert(): Boolean = getOrDefault().let { set(!it); !it }

private class DateFormatConverter : Preference.Adapter<DateFormat> {
    override fun get(key: String, preferences: SharedPreferences): DateFormat {
        val dateFormat = preferences.getString(Keys.dateFormat, "")!!

        if (dateFormat != "") {
            return SimpleDateFormat(dateFormat, Locale.getDefault())
        }

        return DateFormat.getDateInstance(DateFormat.SHORT)
    }

    override fun set(key: String, value: DateFormat, editor: SharedPreferences.Editor) {
        // No-op
    }
}

class PreferencesHelper(val context: Context) {

    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    private val rxPrefs = RxSharedPreferences.create(prefs)

    private val defaultDownloadsDir = Uri.fromFile(
            File(Environment.getExternalStorageDirectory().absolutePath + File.separator +
                    context.getString(R.string.app_name), "downloads"))

    private val defaultBackupDir = Uri.fromFile(
            File(Environment.getExternalStorageDirectory().absolutePath + File.separator +
                    context.getString(R.string.app_name), "backup"))

    fun startScreen() = prefs.getInt(Keys.startScreen, 1)

    fun useBiometricLock() = rxPrefs.getBoolean(Keys.useBiometricLock, false)

    fun lockAppAfter() = rxPrefs.getInteger(Keys.lockAppAfter, 0)

    fun lastAppUnlock() = rxPrefs.getLong(Keys.lastAppUnlock, 0)

    fun secureScreen() = rxPrefs.getBoolean(Keys.secureScreen, false)

    fun hideNotificationContent() = prefs.getBoolean(Keys.hideNotificationContent, false)

    fun clear() = prefs.edit().clear().apply()

    fun themeMode() = rxPrefs.getString(Keys.themeMode, Values.THEME_MODE_SYSTEM)

    fun themeDark() = prefs.getString(Keys.themeDark, Values.THEME_DARK_DEFAULT)

    fun rotation() = rxPrefs.getInteger(Keys.rotation, 1)

    fun pageTransitions() = rxPrefs.getBoolean(Keys.enableTransitions, true)

    fun doubleTapAnimSpeed() = rxPrefs.getInteger(Keys.doubleTapAnimationSpeed, 500)

    fun showPageNumber() = rxPrefs.getBoolean(Keys.showPageNumber, true)

    fun trueColor() = rxPrefs.getBoolean(Keys.trueColor, false)

    fun fullscreen() = rxPrefs.getBoolean(Keys.fullscreen, true)

    fun cutoutShort() = rxPrefs.getBoolean(Keys.cutoutShort, true)

    fun keepScreenOn() = rxPrefs.getBoolean(Keys.keepScreenOn, true)

    fun customBrightness() = rxPrefs.getBoolean(Keys.customBrightness, false)

    fun customBrightnessValue() = rxPrefs.getInteger(Keys.customBrightnessValue, 0)

    fun colorFilter() = rxPrefs.getBoolean(Keys.colorFilter, false)

    fun colorFilterValue() = rxPrefs.getInteger(Keys.colorFilterValue, 0)

    fun colorFilterMode() = rxPrefs.getInteger(Keys.colorFilterMode, 0)

    fun defaultViewer() = prefs.getInt(Keys.defaultViewer, 1)

    fun imageScaleType() = rxPrefs.getInteger(Keys.imageScaleType, 1)

    fun zoomStart() = rxPrefs.getInteger(Keys.zoomStart, 1)

    fun readerTheme() = rxPrefs.getInteger(Keys.readerTheme, 0)

    fun cropBorders() = rxPrefs.getBoolean(Keys.cropBorders, false)

    fun cropBordersWebtoon() = rxPrefs.getBoolean(Keys.cropBordersWebtoon, false)

    fun padPagesVertWebtoon() = rxPrefs.getBoolean(Keys.padPagesVertWebtoon, false)

    fun readWithTapping() = rxPrefs.getBoolean(Keys.readWithTapping, true)

    fun readWithLongTap() = rxPrefs.getBoolean(Keys.readWithLongTap, true)

    fun readWithVolumeKeys() = rxPrefs.getBoolean(Keys.readWithVolumeKeys, false)

    fun readWithVolumeKeysInverted() = rxPrefs.getBoolean(Keys.readWithVolumeKeysInverted, false)

    fun navigationModePager() = rxPrefs.getInteger(Keys.navigationModePager, 0)

    fun navigationModeWebtoon() = rxPrefs.getInteger(Keys.navigationModeWebtoon, 0)

    fun portraitColumns() = rxPrefs.getInteger(Keys.portraitColumns, 0)

    fun landscapeColumns() = rxPrefs.getInteger(Keys.landscapeColumns, 0)

    fun updateOnlyNonCompleted() = prefs.getBoolean(Keys.updateOnlyNonCompleted, false)

    fun autoUpdateTrack() = prefs.getBoolean(Keys.autoUpdateTrack, true)

    fun lastUsedCatalogueSource() = rxPrefs.getLong(Keys.lastUsedCatalogueSource, -1)

    fun lastUsedCategory() = rxPrefs.getInteger(Keys.lastUsedCategory, 0)

    fun lastVersionCode() = rxPrefs.getInteger("last_version_code", 0)

    fun catalogueAsList() = rxPrefs.getBoolean(Keys.catalogueAsList, false)

    fun enabledLanguages() = rxPrefs.getStringSet(Keys.enabledLanguages, setOf("en", Locale.getDefault().language))

    fun trackUsername(sync: TrackService) = prefs.getString(Keys.trackUsername(sync.id), "")

    fun trackPassword(sync: TrackService) = prefs.getString(Keys.trackPassword(sync.id), "")

    fun setTrackCredentials(sync: TrackService, username: String, password: String) {
        prefs.edit()
                .putString(Keys.trackUsername(sync.id), username)
                .putString(Keys.trackPassword(sync.id), password)
                .apply()
    }

    fun trackToken(sync: TrackService) = rxPrefs.getString(Keys.trackToken(sync.id), "")

    fun anilistScoreType() = rxPrefs.getString("anilist_score_type", "POINT_10")

    fun backupsDirectory() = rxPrefs.getString(Keys.backupDirectory, defaultBackupDir.toString())

    fun dateFormat() = rxPrefs.getObject(Keys.dateFormat, DateFormat.getDateInstance(DateFormat.SHORT), DateFormatConverter())

    fun downloadsDirectory() = rxPrefs.getString(Keys.downloadsDirectory, defaultDownloadsDir.toString())

    fun downloadOnlyOverWifi() = prefs.getBoolean(Keys.downloadOnlyOverWifi, true)

    fun numberOfBackups() = rxPrefs.getInteger(Keys.numberOfBackups, 1)

    fun backupInterval() = rxPrefs.getInteger(Keys.backupInterval, 0)

    fun removeAfterReadSlots() = prefs.getInt(Keys.removeAfterReadSlots, -1)

    fun removeAfterMarkedAsRead() = prefs.getBoolean(Keys.removeAfterMarkedAsRead, false)

    fun libraryUpdateInterval() = rxPrefs.getInteger(Keys.libraryUpdateInterval, 0)

    fun libraryUpdateRestriction() = prefs.getStringSet(Keys.libraryUpdateRestriction, emptySet())

    fun libraryUpdateCategories() = rxPrefs.getStringSet(Keys.libraryUpdateCategories, emptySet())

    fun libraryUpdatePrioritization() = rxPrefs.getInteger(Keys.libraryUpdatePrioritization, 0)

    fun libraryAsList() = rxPrefs.getBoolean(Keys.libraryAsList, false)

    fun downloadBadge() = rxPrefs.getBoolean(Keys.downloadBadge, false)

    fun filterDownloaded() = rxPrefs.getBoolean(Keys.filterDownloaded, false)

    fun filterUnread() = rxPrefs.getBoolean(Keys.filterUnread, false)

    fun filterCompleted() = rxPrefs.getBoolean(Keys.filterCompleted, false)

    fun librarySortingMode() = rxPrefs.getInteger(Keys.librarySortingMode, 0)

    fun librarySortingAscending() = rxPrefs.getBoolean("library_sorting_ascending", true)

    fun automaticUpdates() = prefs.getBoolean(Keys.automaticUpdates, true)

    fun hiddenCatalogues() = rxPrefs.getStringSet("hidden_catalogues", emptySet())

    fun downloadNew() = rxPrefs.getBoolean(Keys.downloadNew, false)

    fun downloadNewCategories() = rxPrefs.getStringSet(Keys.downloadNewCategories, emptySet())

    fun lang() = prefs.getString(Keys.lang, "")

    fun defaultCategory() = prefs.getInt(Keys.defaultCategory, -1)

    fun skipRead() = prefs.getBoolean(Keys.skipRead, false)

    fun migrateFlags() = rxPrefs.getInteger("migrate_flags", Int.MAX_VALUE)

    fun trustedSignatures() = rxPrefs.getStringSet("trusted_signatures", emptySet())

    fun alwaysShowChapterTransition() = rxPrefs.getBoolean(Keys.alwaysShowChapterTransition, true)
}
