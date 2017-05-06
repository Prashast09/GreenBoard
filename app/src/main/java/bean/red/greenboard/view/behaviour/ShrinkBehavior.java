package bean.red.greenboard.view.behaviour;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import java.util.List;

/**
 * Created by Shivam Seth on 6/26/2016.
 */
public class ShrinkBehavior extends CoordinatorLayout.Behavior<Button> {

    public ShrinkBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, Button child, View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, Button child, View dependency) {

        float translationY = getTranslationYForSnackbar(parent, child);
            float percentComplete = -translationY / dependency.getHeight();
            float scaleFactor = 1 - percentComplete;

            child.setScaleX(scaleFactor);
            child.setScaleY(scaleFactor);
            return false;
        }
    private float getTranslationYForSnackbar(CoordinatorLayout parent,
                                                Button fab) {
        float minOffset = 0;
        final List<View> dependencies = parent.getDependencies(fab);
        for (int i = 0, z = dependencies.size(); i < z; i++) {
            final View view = dependencies.get(i);
            if (view instanceof Snackbar.SnackbarLayout && parent.doViewsOverlap(fab, view)) {
                minOffset = Math.min(minOffset,
                        ViewCompat.getTranslationY(view) - view.getHeight());
            }
        }

        return minOffset;
    }
}