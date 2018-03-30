package com.foxconn.permissionstestapplication;

/**
 TODO: Add Class Header
 */
interface AccessibilityActionPerformer {
    /**
     Mimic's {@link android.accessibilityservice.AccessibilityService#performGlobalAction(int)},
     but this way we can test.

     Performs a global action. Such an action can be performed
     at any moment regardless of the current application or user
     location in that application. For example going back, going
     home, opening recents, etc.

     @param action The action to perform.

     @return Whether the action was successfully performed.

     @see {@link android.accessibilityservice.AccessibilityService#GLOBAL_ACTION_BACK}
     @see {@link android.accessibilityservice.AccessibilityService#GLOBAL_ACTION_HOME}
     @see {@link android.accessibilityservice.AccessibilityService#GLOBAL_ACTION_NOTIFICATIONS}
     @see {@link android.accessibilityservice.AccessibilityService#GLOBAL_ACTION_RECENTS}
     */
    public boolean performGlobalAction(int action);
}
