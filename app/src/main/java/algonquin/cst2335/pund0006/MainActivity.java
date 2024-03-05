package algonquin.cst2335.pund0006;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.PatternMatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

/**This class represents the main activity of the password complexity checker app.
 * It allows users to enter a password and checks its complexity based on specific requirements.
 * If the password meets all complexity requirements, a success message is displayed; otherwise, an error message is shown.
 *
 * @author asthapundir
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    /**
     * This function checks if a character is a special character (#$%^&*!@?).
     *
     * @param c The character to be checked.
     * @return True if the character is a special character, false otherwise.
     */

    boolean isSpecialCharacter(char c) {
        switch (c) {
            case '#':
            case '$':
            case '%':
            case '^':
            case '&':
            case '*':
            case '!':
            case '@':
            case '?':
                return true;
            default:
                return false;
        }
    }
    /**
     * This function checks the complexity of a password string to ensure it meets specific requirements.
     *
     * This method checks if the provided password string contains at least one uppercase letter,
     * one lowercase letter, one digit, and one special character (#$%^&*!).
     * If any of these requirements are not met, a Toast message is shown indicating the missing requirement(s).
     *
     * @param pw The password string to be checked for complexity.
     * @return True if the password meets all complexity requirements, false otherwise.
     */
    boolean checkPasswordComplexity(String pw)
    {
        boolean foundUpperCase, foundLowerCase, foundNumber, foundSpecial;
        foundUpperCase = foundLowerCase = foundNumber = foundSpecial = false;

        for (int i = 0; i < pw.length(); i++) {
            char c = pw.charAt(i);
            if (Character.isUpperCase(c)) {
                foundUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                foundLowerCase = true;
            } else if (Character.isDigit(c)) {
                foundNumber = true;
            } else if (isSpecialCharacter(c)) {
                foundSpecial = true;
            }
        }

        if (!foundUpperCase) {
            showToast("Your password does not have a upper case");
            return false;
        } else if (!foundLowerCase) {
            showToast("Your password does not have a lower case letter");
            return false;
        } else if (!foundNumber) {
            showToast("Your password does not have a number");
            return false;
        } else if (!foundSpecial) {
            showToast("Your password does not have a special symbol");
            return false;
        } else {
            // All requirements are met
            return true;
        }
    }

    private void showToast(String s)
    {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }



/** This holds the text at the centre of the screen*/
    private TextView tv = null;

    /**This holds the password string that the user enters*/
    private EditText et = null;

    /**This is the login button*/
    private Button btn = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       tv = findViewById(R.id.textView2);
       et = findViewById(R.id.editTextPassword);
       btn= findViewById(R.id.button2);

        btn.setOnClickListener(clk ->{
            String password = et.getText().toString();

            checkPasswordComplexity(password);

            boolean isComplex = checkPasswordComplexity(password);
            if (isComplex) {
                tv.setText("Your password meets the requirements");
            } else {
                tv.setText("You shall not pass!");
            }

        });
    }

}