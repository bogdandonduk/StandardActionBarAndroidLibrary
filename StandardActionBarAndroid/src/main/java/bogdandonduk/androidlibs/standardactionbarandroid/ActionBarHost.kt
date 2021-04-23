package bogdandonduk.androidlibs.standardactionbarandroid

import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.appbar.AppBarLayout

interface ActionBarHost {
    var actionBarRootAppBarLayout: AppBarLayout?

    var toolbar: Toolbar?

    var navDrawerToggle: ActionBarDrawerToggle?

    fun initializeActionBar(
        activity: AppCompatActivity,
        toolbar: Toolbar,
        title: String? = null,
        showHomeAsUp: Boolean = true,
        hostDrawerLayout: DrawerLayout? = null,
        @StringRes openDrawerContentDescStringResId: Int = R.string.open_menu,
        @StringRes closeDrawerContentDescStringResId: Int = R.string.close_menu,
        mainContentView: View? = null,
        mainContentSlideOffsetToggleReference: Boolean
    ) : ActionBarDrawerToggle? {
        activity.setSupportActionBar(toolbar)

        with(activity.supportActionBar!!) {
            setDisplayHomeAsUpEnabled(showHomeAsUp)

            if(title != null) {
                setDisplayShowTitleEnabled(true)
                this.title = title
            } else
                setDisplayShowTitleEnabled(false)
        }

        this.toolbar = toolbar

        return if(showHomeAsUp) {
            if(hostDrawerLayout != null) {
                navDrawerToggle = object : ActionBarDrawerToggle(
                    activity,
                    hostDrawerLayout,
                    openDrawerContentDescStringResId,
                    closeDrawerContentDescStringResId
                ) {
                    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                        super.onDrawerSlide(drawerView, slideOffset)

                        if(mainContentSlideOffsetToggleReference)
                            with(mainContentView) {
                                this?.translationX = drawerView.width * slideOffset
                            }
                    }
                }

                navDrawerToggle
            } else {
                this.toolbar?.setNavigationOnClickListener {
                    activity.onBackPressed()
                }

                null
            }
        } else null
    }
}