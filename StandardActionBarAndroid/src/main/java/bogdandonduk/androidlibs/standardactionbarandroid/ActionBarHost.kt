package bogdandonduk.androidlibs.standardactionbarandroid

import android.view.MenuItem
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.navigation.NavigationView

interface ActionBarHost {
    companion object {
        val onDrawerSlideMainContentFullOffsetBehavior = { drawerView: View, slideOffset: Float, mainContentView: View ->
            mainContentView.translationX = drawerView.width * slideOffset
        }

        val onDrawerSlideMainContentPartialOffsetBehavior = { drawerView: View, slideOffset: Float, mainContentView: View ->
            mainContentView.translationX = (drawerView.width * slideOffset) / 10
            mainContentView.scaleX = 1f - slideOffset / 100
            mainContentView.scaleY = 1f - slideOffset / 100
        }
    }

    var actionBarRootAppBarLayout: AppBarLayout?

    var toolbar: Toolbar?

    var navDrawerToggle: ActionBarDrawerToggle?

    val optionsMenuItems: MutableMap<String, Pair<MenuItem?, (itemKey: String, menuItem: MenuItem?) -> Unit>>

    var navDrawerRootNavigationView: NavigationView?

    fun initializeOptionsMenu(themeAction: (() -> Unit)?) {
        optionsMenuItems.forEach { mapEntry ->
            mapEntry.run {
                value.let {
                    it.first?.setOnMenuItemClickListener { menuItem ->
                        it.second.invoke(mapEntry.key, menuItem)

                        false
                    }
                }
            }
        }

        themeAction?.invoke()
    }

    fun initializeActionBarWithDrawer(
        activity: AppCompatActivity,
        toolbar: Toolbar,
        title: String? = null,
        showHomeAsUp: Boolean = true,
        hostDrawerLayout: DrawerLayout,
        @StringRes openDrawerContentDescStringResId: Int = R.string.open_menu,
        @StringRes closeDrawerContentDescStringResId: Int = R.string.close_menu,
        mainContentView: View,
        onDrawerSlideBehavior: ((drawerView: View, slideOffset: Float, mainContentView: View) -> Unit)? = null,
        navDrawerView: NavigationView
    ) : ActionBarDrawerToggle? {
        this.toolbar = toolbar
        navDrawerRootNavigationView = navDrawerView

        activity.setSupportActionBar(this.toolbar)

        with(activity.supportActionBar!!) {
            setDisplayHomeAsUpEnabled(showHomeAsUp)

            if(title != null) {
                setDisplayShowTitleEnabled(true)
                this.title = title
            } else
                setDisplayShowTitleEnabled(false)
        }

        navDrawerToggle = object : ActionBarDrawerToggle(
                activity,
                hostDrawerLayout,
                openDrawerContentDescStringResId,
                closeDrawerContentDescStringResId
        ) {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)

                onDrawerSlideBehavior?.invoke(drawerView, slideOffset, mainContentView)
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
        this.toolbar = toolbar

        activity.setSupportActionBar(this.toolbar)

        with(activity.supportActionBar!!) {
            setDisplayHomeAsUpEnabled(showHomeAsUp)

            if(title != null) {
                setDisplayShowTitleEnabled(true)
                this.title = title
            } else
                setDisplayShowTitleEnabled(false)
        }

        this.toolbar!!.setNavigationOnClickListener {
            activity.onBackPressed()
        }
    }
}