package eu.kanade.tachiyomi.ui.category

import android.view.View
import eu.kanade.tachiyomi.data.database.models.Category
import eu.kanade.tachiyomi.ui.base.holder.BaseFlexibleViewHolder
import eu.kanade.tachiyomi.util.view.getRound
import kotlinx.android.synthetic.main.categories_item.image
import kotlinx.android.synthetic.main.categories_item.reorder
import kotlinx.android.synthetic.main.categories_item.title

/**
 * Holder used to display category items.
 *
 * @param view The view used by category items.
 * @param adapter The adapter containing this holder.
 */
class CategoryHolder(view: View, val adapter: CategoryAdapter) : BaseFlexibleViewHolder(view, adapter) {

    init {
        // Create round letter image onclick to simulate long click
        image.setOnClickListener {
            // Simulate long click on this view to enter selection mode
            onLongClick(view)
        }

        setDragHandleView(reorder)
    }

    /**
     * Binds this holder with the given category.
     *
     * @param category The category to bind.
     */
    fun bind(category: Category) {
        // Set capitalized title.
        title.text = category.name.capitalize()

        // Update circle letter image.
        itemView.post {
            image.setImageDrawable(image.getRound(category.name.take(1).toUpperCase(), false))
        }
    }

    /**
     * Called when an item is released.
     *
     * @param position The position of the released item.
     */
    override fun onItemReleased(position: Int) {
        super.onItemReleased(position)
        adapter.onItemReleaseListener.onItemReleased(position)
    }
}
