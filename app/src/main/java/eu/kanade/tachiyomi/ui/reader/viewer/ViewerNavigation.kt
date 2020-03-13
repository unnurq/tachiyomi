package eu.kanade.tachiyomi.ui.reader.viewer

import android.graphics.PointF
import android.graphics.RectF

abstract class ViewerNavigation {

    companion object {
        const val MENU = 0
        const val NEXT = 1
        const val PREV = 2
    }

    private var constantMenuRegion : RectF = RectF(0f, 0f, 1f, 0.1f)

    abstract var nextRegion : List<RectF>

    abstract var prevRegion : List<RectF>

    fun getAction(pos: PointF) : Int {
        val x = pos.x
        val y = pos.y
        val action = when {
            constantMenuRegion.contains(x, y) -> MENU
            menuRegion.any { it.contains(x, y) } -> MENU
            nextRegion.any { it.contains(x, y) } -> NEXT
            prevRegion.any { it.contains(x, y) } -> PREV

            else -> MENU
        }
        return action
    }
}
