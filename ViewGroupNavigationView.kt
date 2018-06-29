import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavOptions
import children

/**
 * Since NavigationUi class only contains functions that support a small set of
 * UI components in the Navigation, we need to create our own functions for
 * our custom views in order to support navigation.
 *
 * There are two steps to do that:
 *
 * Step 1:
 * - Handle the click events for our custom UI component and use the Nav controller
 * to navigate to the desired destination.
 *
 * Step 2:
 * - Listen to the completion of the Navigation inside the addOnNavigatedListener
 * and do the work required after the navigation is done.
 */

private const val ignoreTag = "ignore"

/**
 * Sets up a {@link ViewGroup} for use with a {@link NavController}. This will call
 * {@link #onNavDestinationSelected(View, NavController)} when a menu item is selected.
 * The selected item in the ViewGroup will automatically be updated when the destination
 * changes.
 *
 * Note that any view in the {@link ViewGroup} that has tag "ignore", will be ignored.
 *
 * @param viewGroup The ViewGroup that should be kept in sync with changes to the
 *                       NavController.
 * @param navController The NavController that supplies the primary and secondary menu.
 *                      Navigation actions on this NavController will be reflected in the
 *                      selected item in the NavigationView.
 */

fun setupWithNavController(
        viewGroup: ViewGroup,
        navController: NavController) {
    // Handle the click events for our custom UI component and use the Nav controller
    // to navigate to the desired destination.
    val menuItems = viewGroup.children
            .filter { it.tag != ignoreTag }

    menuItems.forEach {
        it.setOnClickListener { onNavDestinationSelected(it, navController, popUp = true) }
    }

    // Listen to the completion of the Navigation inside the addOnNavigatedListener
    // and update every item's selected flag
    navController.addOnNavigatedListener { _, destination ->
        menuItems.forEach {
            it.isSelected = (it.id == destination.id)
        }
    }
}

/**
 * Attempt to navigate to the {@link NavDestination} associated with the given View.
 *
 * <p>Importantly, it assumes the {@link View#getId() view id} matches a valid
 * {@link NavDestination#getAction(int) action id} or
 * {@link NavDestination#getId() destination id} to be navigated to.</p>
 *
 * @param item The selected View.
 * @param navController The NavController that hosts the destination.
 * @return True if the {@link NavController} was able to navigate to the destination
 * associated with the given View.
 */
fun onNavDestinationSelected(item: View,
                             navController: NavController,
                             popUp: Boolean = false): Boolean {
    val builder = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
            .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
            .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
            .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
    if (popUp) {
        builder.setPopUpTo(findStartDestination(navController.graph).id, false)
    }
    val options = builder.build()
    return try {
        //TODO provide proper API instead of using Exceptions as Control-Flow.
        navController.navigate(item.id, null, options)
        true
    } catch (e: IllegalArgumentException) {
        false
    }
}

/**
 * Finds the actual start destination of the graph, handling cases where the graph's starting
 * destination is itself a NavGraph.
 */
private fun findStartDestination(graph: NavGraph): NavDestination {
    var startDestination: NavDestination = graph
    while (startDestination is NavGraph) {
        val parent = startDestination
        startDestination = parent.findNode(parent.startDestination)
    }
    return startDestination
}
