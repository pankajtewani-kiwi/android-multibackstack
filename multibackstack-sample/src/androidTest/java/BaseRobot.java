import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.assertion.ViewAssertions;
import android.view.View;

import org.hamcrest.Matcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static java.lang.Thread.sleep;

public class BaseRobot {

    private final int waitMillis = 5000;
    private final long waitMillisPerTry = 100;
    private final boolean checkForInvisible = false;

    void doOnView(Matcher<View> matcher , ViewAction... actions) throws Exception {
        for(ViewAction action:actions){
            waitForView(matcher,waitMillis,waitMillisPerTry,checkForInvisible).perform(action);
        }
    }

    void assertOnView(Matcher<View> matcher , ViewAssertion... viewAssertions) throws Exception {
        for(ViewAssertion assertion:viewAssertions){
            waitForView(matcher,waitMillis,waitMillisPerTry,checkForInvisible).check(assertion);
        }
    }

    /**
     * Perform action of implicitly waiting for a certain view.
     * This differs from EspressoExtensions.searchFor in that,
     * upon failure to locate an element, it will fetch a new root view
     * in which to traverse searching for our @param match
     *
     * @param viewMatcher ViewMatcher used to find our view
     */
    ViewInteraction waitForView(
            Matcher<View> viewMatcher,
            int waitMillis ,
            long waitMillisPerTry,
            boolean checkForInvisible
    ) throws Exception {

        // Derive the max tries
        int maxTries = waitMillis / (int)waitMillisPerTry;

        int tries = 0;

        for (int i=0;i<maxTries;i++)
            try {
                // Track the amount of times we've tried
                tries++;

                // Search the root for the view
                onView(isRoot()).perform(EspressoHelper.searchFor(viewMatcher, checkForInvisible));

                // If we're here, we found our view. Now return it
                return onView(viewMatcher);

            } catch ( Exception ex) {

            if (tries == maxTries) {
                throw ex;
            }
            sleep(waitMillisPerTry);
        }

        throw new Exception("Error finding a view matching $viewMatcher");
    }
}
