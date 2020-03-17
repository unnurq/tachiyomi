package eu.kanade.tachiyomi.ui.reader.viewer.webtoon

import com.f2prateek.rx.preferences.Preference
import eu.kanade.tachiyomi.data.preference.PreferencesHelper
import eu.kanade.tachiyomi.ui.reader.viewer.ViewerNavigation
import eu.kanade.tachiyomi.ui.reader.viewer.navigation.KindlishNavigation
import eu.kanade.tachiyomi.ui.reader.viewer.navigation.LNavigation
import eu.kanade.tachiyomi.util.lang.addTo
import rx.subscriptions.CompositeSubscription
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

/**
 * Configuration used by webtoon viewers.
 */
class WebtoonConfig(preferences: PreferencesHelper = Injekt.get()) {

    private val subscriptions = CompositeSubscription()

    var imagePropertyChangedListener: (() -> Unit)? = null

    var tappingEnabled = true
        private set

    var longTapEnabled = true
        private set

    var volumeKeysEnabled = false
        private set

    var volumeKeysInverted = false
        private set

    var imageCropBorders = false
        private set

    var padPagesVert = false
        private set

    var doubleTapAnimDuration = 500
        private set

    var navigationMode : ViewerNavigation = WebtoonDefaultNavigation()
        private set

    var alwaysShowChapterTransition = true
        private set

    init {
        preferences.readWithTapping()
                .register({ tappingEnabled = it })

        preferences.readWithLongTap()
                .register({ longTapEnabled = it })

        preferences.cropBordersWebtoon()
                .register({ imageCropBorders = it }, { imagePropertyChangedListener?.invoke() })

        preferences.padPagesVertWebtoon()
                .register({ padPagesVert = it }, { imagePropertyChangedListener?.invoke() })

        preferences.doubleTapAnimSpeed()
                .register({ doubleTapAnimDuration = it })

        preferences.readWithVolumeKeys()
                .register({ volumeKeysEnabled = it })

        preferences.readWithVolumeKeysInverted()
            .register({ volumeKeysInverted = it })

        preferences.navigationModeWebtoon()
            .register({
                navigationMode = when (it) {
                    0 -> WebtoonDefaultNavigation()
                    1 -> LNavigation()
                    2 -> KindlishNavigation()

                    else -> WebtoonDefaultNavigation()
                }
            })

        preferences.alwaysShowChapterTransition()
                .register({ alwaysShowChapterTransition = it })
    }

    fun unsubscribe() {
        subscriptions.unsubscribe()
    }

    private fun <T> Preference<T>.register(
        valueAssignment: (T) -> Unit,
        onChanged: (T) -> Unit = {}
    ) {
        asObservable()
                .doOnNext(valueAssignment)
                .skip(1)
                .distinctUntilChanged()
                .doOnNext(onChanged)
                .subscribe()
                .addTo(subscriptions)
    }
}
