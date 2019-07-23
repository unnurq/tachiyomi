package eu.kanade.tachiyomi.ui.reader.viewer.pager

import android.graphics.RectF
import eu.kanade.tachiyomi.ui.reader.viewer.ViewerNavigation


class PagerNavigationHelper {

    companion object {
        // Lazy init, in case we have a lot of them in future
        val default by lazy { PagerDefaultNavigation() }

        val getNavigator = fun(i: Int) : ViewerNavigation {
            return when (i) {
                0 -> default

                else -> default
            }
        }
    }

    class PagerDefaultNavigation : ViewerNavigation() {
        override var menuRegion = listOf<RectF>(
                RectF(0.33f, 0f, 0.66f, 1f)
        )
        override var nextRegion = listOf<RectF>(
                RectF(0.66f, 0f, 1f, 1f)
        )
        override var prevRegion = listOf<RectF>(
                RectF(0f, 0f, 0.33f, 1f)
        )
    }
}
