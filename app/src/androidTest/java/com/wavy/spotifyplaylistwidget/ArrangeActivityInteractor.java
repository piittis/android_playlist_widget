package com.wavy.spotifyplaylistwidget;


import android.view.View;

import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.GeneralLocation;
import androidx.test.espresso.action.GeneralSwipeAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Swipe;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

public class ArrangeActivityInteractor {

    private ArrangeActivity mActivity;

    public ArrangeActivityInteractor(ArrangeActivity activity) {
        mActivity = activity;
    }

    public void moveDown(String name) {
        onView(allOf(withId(R.id.playlist_drag_handle), withParent(hasDescendant(withText(name)))))
                .perform(down());
    }

    public void moveUp(String name) {
        onView(allOf(withId(R.id.playlist_drag_handle), withParent(hasDescendant(withText(name)))))
                .perform(up());
    }

    private static ViewAction up() {
        return new GeneralSwipeAction(Swipe.SLOW, GeneralLocation.CENTER,
                view -> new float[] {screenX(view), screenY(view) - 500 }, Press.FINGER);
    }

    private static ViewAction down() {
        return new GeneralSwipeAction(Swipe.SLOW, GeneralLocation.CENTER,
                view -> new float[] {screenX(view), screenY(view) + 500 }, Press.FINGER);
    }

    private static int screenX(View view) {
        int [] xy = new int[2];
        view.getLocationOnScreen(xy);
        return xy[0];
    }
    private static int screenY(View view) {
        int [] xy = new int[2];
        view.getLocationOnScreen(xy);
        return xy[1];
    }

}
