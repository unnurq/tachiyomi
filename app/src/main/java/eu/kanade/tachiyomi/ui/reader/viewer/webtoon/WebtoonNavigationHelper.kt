package eu.kanade.tachiyomi.ui.reader.viewer.webtoon

import android.graphics.RectF
import eu.kanade.tachiyomi.ui.reader.viewer.ViewerNavigation
import eu.kanade.tachiyomi.ui.reader.viewer.navigation.LNavigation

class WebtoonNavigationHelper {

    companion object {
        // Lazy init, in case we have a lot of them in future
        val default by lazy { WebtoonDefaultNavigation() }
        private val lNavigation by lazy { LNavigation() }

        val getNavigator = fun(i: Int) : ViewerNavigation {
            return when (i) {
                0 -> default
                1 -> lNavigation

                else -> default
            }
        }

    }

    class WebtoonDefaultNavigation : ViewerNavigation(){
        override var menuRegion = listOf(
                RectF(0f, 0.33f, 1f, 0.66f)
        )

        override var nextRegion = listOf(
                RectF(0f, 0.66f, 1f, 1f)
        )

        override var prevRegion = listOf(
                RectF(0f, 0f, 1f, 0.33f)
        )
    }
}
