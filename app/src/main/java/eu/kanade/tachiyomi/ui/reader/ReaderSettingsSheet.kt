package eu.kanade.tachiyomi.ui.reader

import android.os.Build
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.Spinner
import androidx.core.widget.NestedScrollView
import com.f2prateek.rx.preferences.Preference
import com.google.android.material.bottomsheet.BottomSheetDialog
import eu.kanade.tachiyomi.R
import eu.kanade.tachiyomi.data.preference.PreferencesHelper
import eu.kanade.tachiyomi.data.preference.getOrDefault
import eu.kanade.tachiyomi.ui.reader.viewer.pager.PagerViewer
import eu.kanade.tachiyomi.ui.reader.viewer.webtoon.WebtoonViewer
import eu.kanade.tachiyomi.util.view.invisible
import eu.kanade.tachiyomi.util.view.visible
import eu.kanade.tachiyomi.widget.IgnoreFirstSpinnerListener
import kotlinx.android.synthetic.main.reader_settings_sheet.always_show_chapter_transition
import kotlinx.android.synthetic.main.reader_settings_sheet.background_color
import kotlinx.android.synthetic.main.reader_settings_sheet.crop_borders
import kotlinx.android.synthetic.main.reader_settings_sheet.crop_borders_webtoon
import kotlinx.android.synthetic.main.reader_settings_sheet.cutout_short
import kotlinx.android.synthetic.main.reader_settings_sheet.fullscreen
import kotlinx.android.synthetic.main.reader_settings_sheet.keepscreen
import kotlinx.android.synthetic.main.reader_settings_sheet.long_tap
import kotlinx.android.synthetic.main.reader_settings_sheet.pad_pages_vert_webtoon
import kotlinx.android.synthetic.main.reader_settings_sheet.page_transitions
import kotlinx.android.synthetic.main.reader_settings_sheet.pager_prefs_group
import kotlinx.android.synthetic.main.reader_settings_sheet.rotation_mode
import kotlinx.android.synthetic.main.reader_settings_sheet.scale_type
import kotlinx.android.synthetic.main.reader_settings_sheet.show_page_number
import kotlinx.android.synthetic.main.reader_settings_sheet.viewer
import kotlinx.android.synthetic.main.reader_settings_sheet.webtoon_prefs_group
import kotlinx.android.synthetic.main.reader_settings_sheet.zoom_start
import uy.kohesive.injekt.injectLazy

/**
 * Sheet to show reader and viewer preferences.
 */
class ReaderSettingsSheet(private val activity: ReaderActivity) : BottomSheetDialog(activity) {

    /**
     * Preferences helper.
     */
    private val preferences by injectLazy<PreferencesHelper>()

    init {
        // Use activity theme for this layout
        val view = activity.layoutInflater.inflate(R.layout.reader_settings_sheet, null)
        val scroll = NestedScrollView(activity)
        scroll.addView(view)
        setContentView(scroll)
    }

    /**
     * Called when the sheet is created. It initializes the listeners and values of the preferences.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initGeneralPreferences()

        when (activity.viewer) {
            is PagerViewer -> initPagerPreferences()
            is WebtoonViewer -> initWebtoonPreferences()
        }
    }

    /**
     * Init general reader preferences.
     */
    private fun initGeneralPreferences() {
        viewer.onItemSelectedListener = IgnoreFirstSpinnerListener { position ->
            activity.presenter.setMangaViewer(position)

            if (activity.presenter.getMangaViewer() == ReaderActivity.WEBTOON) {
                initWebtoonPreferences()
            } else {
                initPagerPreferences()
            }
        }
        viewer.setSelection(activity.presenter.manga?.viewer ?: 0, false)

        rotation_mode.bindToPreference(preferences.rotation(), 1)
        background_color.bindToPreference(preferences.readerTheme())
        show_page_number.bindToPreference(preferences.showPageNumber())
        fullscreen.bindToPreference(preferences.fullscreen())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            cutout_short.visible()
            cutout_short.bindToPreference(preferences.cutoutShort())
        }
        keepscreen.bindToPreference(preferences.keepScreenOn())
        long_tap.bindToPreference(preferences.readWithLongTap())
        always_show_chapter_transition.bindToPreference(preferences.alwaysShowChapterTransition())
    }

    /**
     * Init the preferences for the pager reader.
     */
    private fun initPagerPreferences() {
        webtoon_prefs_group.invisible()
        pager_prefs_group.visible()
        pager_nav.bindToPreference(preferences.navigationModePager())
        scale_type.bindToPreference(preferences.imageScaleType(), 1)
        zoom_start.bindToPreference(preferences.zoomStart(), 1)
        crop_borders.bindToPreference(preferences.cropBorders())
        pad_pages_vert_webtoon.bindToPreference(preferences.padPagesVertWebtoon())
        page_transitions.bindToPreference(preferences.pageTransitions())
    }

    /**
     * Init the preferences for the webtoon reader.
     */
    private fun initWebtoonPreferences() {
        pager_prefs_group.invisible()
        webtoon_prefs_group.visible()
        webtoon_nav.bindToPreference(preferences.navigationModeWebtoon())
        crop_borders_webtoon.bindToPreference(preferences.cropBordersWebtoon())
        pad_pages_vert_webtoon.bindToPreference(preferences.padPagesVertWebtoon())
    }

    /**
     * Binds a checkbox or switch view with a boolean preference.
     */
    private fun CompoundButton.bindToPreference(pref: Preference<Boolean>) {
        isChecked = pref.getOrDefault()
        setOnCheckedChangeListener { _, isChecked -> pref.set(isChecked) }
    }

    /**
     * Binds a spinner to an int preference with an optional offset for the value.
     */
    private fun Spinner.bindToPreference(pref: Preference<Int>, offset: Int = 0) {
        onItemSelectedListener = IgnoreFirstSpinnerListener { position ->
            pref.set(position + offset)
        }
        setSelection(pref.getOrDefault() - offset, false)
    }
}
