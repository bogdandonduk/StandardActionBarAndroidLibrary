package bogdandonduk.androidlibs.standardactionbarandroid

import android.view.MenuItem
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.appbar.AppBarLayout

interface ActionBarHost {
    var toolbar: Toolbar?

    var navDrawerToggle: ActionBarDrawerToggle?

    val optionsMenuItems: MutableMap<String, MenuItem>

    fun initializeActionBarWithDrawer(
        activity: AppCompatActivity,
        toolbar: Toolbar,
        title: String? = null,
        showHomeAsUp: Boolean = true,
        hostDrawerLayout: DrawerLayout,
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

        return navDrawerToggle
    }

    fun initializeActionBarWithBackNavigation(
        activity: AppCompatActivity,
        toolbar: Toolbar,
        title: String? = null,
        showHomeAsUp: Boolean = true,
    ) {
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

        this.toolbar!!.setNavigationOnClickListener {
            activity.onBackPressed()
        }
    }
}