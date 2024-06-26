package algonquin.cst2335.pund0006;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void mainActivityTest() {
        ViewInteraction appCompatEditText = onView
                (withId(R.id.editTextPassword)  );
        appCompatEditText.perform(replaceText("12345"), closeSoftKeyboard());


        ViewInteraction materialButton = onView(
                withId(R.id.button2) );
        materialButton.perform(click());

        ViewInteraction textView = onView(
                withId(R.id.textView2) );
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * Test case to check if the password has a upper case letter.
     */

    @Test
    public void testFindMissingUpperCase()
    {
        //find the view
        ViewInteraction appCompatEditText = onView
                (withId(R.id.editTextPassword)  );
        //type in password123#$*
        appCompatEditText.perform(replaceText("password123#$*"));

        //find the button
        ViewInteraction materialButton = onView( withId(R.id.button2) );
        //click the button
        materialButton.perform(click());


        //find the text view
        ViewInteraction textView = onView( withId(R.id.textView2) );
        //check the text
        textView.check(matches(withText("You shall not pass!"))) ;
    }
    /**
     * Test case to check if the password is missing a lower case letter.
     */
    public void testMissingLowerCase() {
        // Find the view
        ViewInteraction appCompatEditText = onView(withId(R.id.editTextPassword));

        // Type in a password without a lower case letter
        appCompatEditText.perform(replaceText("PASSWORD123#$*"));

        // Find the button
        ViewInteraction materialButton = onView(withId(R.id.button2));

        // Click the button
        materialButton.perform(click());

        // Find the text view
        ViewInteraction textView = onView(withId(R.id.textView2));

        // Check the text
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * Test case to check if the password is missing a digit.
     */
    @Test
    public void testMissingDigit() {
        // Find the view
        ViewInteraction appCompatEditText = onView(withId(R.id.editTextPassword));

        // Type in a password without a digit
        appCompatEditText.perform(replaceText("PasswordNoDigit"));

        // Find the button
        ViewInteraction materialButton = onView(withId(R.id.button2));

        // Click the button
        materialButton.perform(click());

        // Find the text view
        ViewInteraction textView = onView(withId(R.id.textView2));

        // Check the text
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * Test case to check if the password is missing a special character.
     */
    @Test
    public void testMissingSpecialCharacter() {
        // Find the view
        ViewInteraction appCompatEditText = onView(withId(R.id.editTextPassword));

        // Type in a password without a special character
        appCompatEditText.perform(replaceText("PasswordNoSpecialChar123"));

        // Find the button
        ViewInteraction materialButton = onView(withId(R.id.button2));

        // Click the button
        materialButton.perform(click());

        // Find the text view
        ViewInteraction textView = onView(withId(R.id.textView2));

        // Check the text
        textView.check(matches(withText("You shall not pass!")));
    }


    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
