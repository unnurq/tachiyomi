package eu.kanade.tachiyomi.ui.base.activity

import android.os.Bundle
import eu.kanade.tachiyomi.ui.base.presenter.BasePresenter
import eu.kanade.tachiyomi.ui.security.SecureActivityDelegate
import eu.kanade.tachiyomi.util.system.LocaleHelper
import nucleus.view.NucleusAppCompatActivity

abstract class BaseRxActivity<P : BasePresenter<*>> : NucleusAppCompatActivity<P>() {

    @Suppress("LeakingThis")
    private val secureActivityDelegate = SecureActivityDelegate(this)

    init {
        @Suppress("LeakingThis")
        LocaleHelper.updateConfiguration(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        secureActivityDelegate.onCreate()
    }

    override fun onResume() {
        super.onResume()

        secureActivityDelegate.onResume()
    }

    override fun onDestroy() {
        secureActivityDelegate.onDestroy()

        super.onDestroy()
    }
}
