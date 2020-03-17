package eu.kanade.tachiyomi.ui.manga.track

import android.annotation.SuppressLint
import android.view.View
import eu.kanade.tachiyomi.ui.base.holder.BaseViewHolder
import eu.kanade.tachiyomi.util.view.visibleIf
import kotlinx.android.synthetic.main.track_item.chapters_container
import kotlinx.android.synthetic.main.track_item.logo_container
import kotlinx.android.synthetic.main.track_item.score_container
import kotlinx.android.synthetic.main.track_item.status_container
import kotlinx.android.synthetic.main.track_item.track_chapters
import kotlinx.android.synthetic.main.track_item.track_details
import kotlinx.android.synthetic.main.track_item.track_logo
import kotlinx.android.synthetic.main.track_item.track_score
import kotlinx.android.synthetic.main.track_item.track_set
import kotlinx.android.synthetic.main.track_item.track_status

class TrackHolder(view: View, adapter: TrackAdapter) : BaseViewHolder(view) {

    init {
        val listener = adapter.rowClickListener

        logo_container.setOnClickListener { listener.onLogoClick(adapterPosition) }
        track_set.setOnClickListener { listener.onSetClick(adapterPosition) }
        status_container.setOnClickListener { listener.onStatusClick(adapterPosition) }
        chapters_container.setOnClickListener { listener.onChaptersClick(adapterPosition) }
        score_container.setOnClickListener { listener.onScoreClick(adapterPosition) }
    }

    @SuppressLint("SetTextI18n")
    fun bind(item: TrackItem) {
        val track = item.track
        track_logo.setImageResource(item.service.getLogo())
        logo_container.setBackgroundColor(item.service.getLogoColor())

        track_details.visibleIf { track != null }
        if (track != null) {
            track_chapters.text = "${track.last_chapter_read}/" +
                    if (track.total_chapters > 0) track.total_chapters else "-"
            track_status.text = item.service.getStatus(track.status)
            track_score.text = if (track.score == 0f) "-" else item.service.displayScore(track)
        }
    }
}
