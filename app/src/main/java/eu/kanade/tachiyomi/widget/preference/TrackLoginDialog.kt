package eu.kanade.tachiyomi.widget.preference

import android.os.Bundle
import android.view.View
import eu.kanade.tachiyomi.R
import eu.kanade.tachiyomi.data.track.TrackManager
import eu.kanade.tachiyomi.data.track.TrackService
import eu.kanade.tachiyomi.util.system.toast
import kotlinx.android.synthetic.main.pref_account_login.view.dialog_title
import kotlinx.android.synthetic.main.pref_account_login.view.login
import kotlinx.android.synthetic.main.pref_account_login.view.password
import kotlinx.android.synthetic.main.pref_account_login.view.username
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

class TrackLoginDialog(usernameLabel: String? = null, bundle: Bundle? = null) :
        LoginDialogPreference(usernameLabel, bundle) {

    private val service = Injekt.get<TrackManager>().getService(args.getInt("key"))!!

    constructor(service: TrackService) : this(service, null)

    constructor(service: TrackService, usernameLabel: String?) :
            this(usernameLabel, Bundle().apply { putInt("key", service.id) })

    override fun setCredentialsOnView(view: View) = with(view) {
        dialog_title.text = context.getString(R.string.login_title, service.name)
        username.setText(service.getUsername())
        password.setText(service.getPassword())
    }

    override fun checkLogin() {
        requestSubscription?.unsubscribe()

        v?.apply {
            if (username.text.isNullOrEmpty() || password.text.isNullOrEmpty())
                return

            login.progress = 1
            val user = username.text.toString()
            val pass = password.text.toString()

            requestSubscription = service.login(user, pass)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        dialog?.dismiss()
                        context.toast(R.string.login_success)
                    }, { error ->
                        login.progress = -1
                        login.setText(R.string.unknown_error)
                        error.message?.let { context.toast(it) }
                    })
        }
    }

    override fun onDialogClosed() {
        super.onDialogClosed()
        (targetController as? Listener)?.trackLoginDialogClosed(service)
    }

    interface Listener {
        fun trackLoginDialogClosed(service: TrackService)
    }
}
