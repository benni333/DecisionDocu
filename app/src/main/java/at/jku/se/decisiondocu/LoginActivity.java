package at.jku.se.decisiondocu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.ViewById;

import at.jku.se.decisiondocu.asynctask.RestNetworkTasks;

/**
 * A login screen that offers login via email/password.
 */
@EActivity(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    @ViewById(R.id.email)
    EditText mEmailView;

    @ViewById(R.id.password)
    EditText mPasswordView;

    @ViewById(R.id.login_progress)
    View mProgressView;

    @ViewById(R.id.login_form)
    View mLoginFormView;

    @ViewById(R.id.email_sign_in_button)
    Button mEmailSignInButton;

    @AfterViews
    protected void init() {
        /*new AsyncTask<Void, Void, Boolean>(){
            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    Client client = ClientBuilder.newClient().register(RestHelper.AndroidFriendlyFeature.class);
                    System.out.println(client.target("http://oracle.com").request().get(String.class));
                }catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();*/
    }

    @Click(R.id.email_sign_in_button)
    void signIn(){
        attemptLogin();
    }

    @Click(R.id.login_register_button)
    void goToRegisterForm(){
        Intent intent = new Intent(this, RegisterActivity_.class);
        startActivity(intent);
    }

    @EditorAction(R.id.password)
    boolean passwordEditorAction(int id){
        if (id == R.id.login || id == EditorInfo.IME_NULL) {
            attemptLogin();
            return true;
        }
        return false;
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            new RestNetworkTasks.UserLoginTask(mProgressView, mLoginFormView, getBaseContext(), email, password){
                @Override
                protected void onPostExecute(Integer success) {
                    super.onPostExecute(success);
                    if (success>0) {
                        Intent intent = new Intent(getBaseContext(),MainActivity_.class);
                        startActivity(intent);
                        finish();
                    } else {
                        mPasswordView.setError(getString(R.string.error_incorrect_password));
                        mPasswordView.requestFocus();
                    }
                }
            }.execute();
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@")&&email.contains(".")&&email.length()>5;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

}

