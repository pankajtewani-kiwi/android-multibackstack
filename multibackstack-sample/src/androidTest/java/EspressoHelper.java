import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.util.TreeIterables;
import android.util.Log;
import android.view.View;

import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.isRoot;

public class EspressoHelper {

    static ViewAction searchFor(Matcher<View> matcher, boolean checkForInvisible) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return String.format("searching for view %s in the root view", matcher);
            }

            @Override
            public void perform(UiController uiController, View view) {
                int tries = 0;
                Iterable<View> childViews = TreeIterables.breadthFirstViewTraversal(view);

                // Look for the match in the tree of childviews

                for (View child : childViews) {
                    tries++;
                    if (!checkForInvisible) {
                        if (matcher.matches(child)) {
                            // found the view
                            return;
                        }
                    } else {
                        if (matcher.matches(child) && (child.getVisibility() == View.VISIBLE)) {
                            return;
                        }
                    }
                }

                throw new NoMatchingViewException.Builder()
                        .withRootView(view)
                        .withViewMatcher(matcher)
                        .build();
            }
        };
    }
}
