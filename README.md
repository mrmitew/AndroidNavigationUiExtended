# ViewGroupNavigationView
Group of functions that allow you to treat any ViewGroup as a custom navigation menu that works with the Android's Navigation Component.

# Introduction
Since NavigationUi class only contains functions that support a small set of UI components in the Navigation, we need to create our own functions for our custom views in order to support navigation.
 
With the utility functions, provided in this repo, one can setup any ViewGroup to work with Android's Navigation Component. Every children in the given ViewGroup will be treated as a navigation menu item (similarly to NavigationView, with the exception that those items are not MenuItem-s, but regular views). 

# Ignore certain views

If you want to ignore particular views from being treated as navigation items (say you have a separtor view), you can tag them with "ignore".

# How is this working?

We handle the click events for our custom UI component and use a Nav controller to navigate to the desired destination.
In addition, we listen to the completion of the Navigation inside the addOnNavigatedListener and do the work required after the navigation is done, namely updating `isSelected` property of each View.

Note that, similarly to how NavigationView works with the Navigation Component, this library assumes that the View's id matches a valid action/destination id to be navigated to. If there is no match, nothing would happen.

# Dependencies
This project is using the [ViewGroup extensions](https://github.com/android/android-ktx/blob/master/src/main/java/androidx/core/view/ViewGroup.kt) provided by the [android-ktx](https://github.com/android/android-ktx/) library.

# Contribution
Yes, please :)
