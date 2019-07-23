package eu.kanade.tachiyomi.ui.reader.viewer

import android.graphics.RectF

abstract class ViewerNavigation {

    companion object {
        const val MENU = 0
        const val NEXT = 1
        const val PREV = 2
    }

    abstract var menuRegion : List<RectF>

    abstract var nextRegion : List<RectF>

    abstract var prevRegion : List<RectF>

    fun getAction(pos : Pair<Float, Float>) : Int {
        val (x, y) = pos
        val action = when {
            menuRegion.any { it.contains(x, y) } -> MENU
            nextRegion.any { it.contains(x, y) } -> NEXT
            prevRegion.any { it.contains(x, y) } -> PREV

            else -> MENU
        }
        return action
    }
}
