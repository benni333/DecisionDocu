package at.jku.se.decisiondocu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.ViewById;

import at.jku.se.decisiondocu.asynctask.RestNetworkTasks;
import at.jku.se.decisiondocu.dialog.PhotoUploadDialog;

@EActivity(R.layout.activity_register)
public class RegisterActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int RESULT_TAKE_IMAGE = 2;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 0;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    @ViewById(R.id.register_form)
    ScrollView mRegisterForm;

    // UI references.
    @ViewById(R.id.emailregister)
    EditText mEmailView;

    @ViewById(R.id.passwordregister)
    EditText mPasswordView;

    @ViewById(R.id.firstname)
    EditText mFirstnameView;

    @ViewById(R.id.lastname)
    EditText mLastnameView;

    @ViewById(R.id.register_progress)
    View mProgressView;

    @ViewById(R.id.register_form)
    View mRegisterFormView;

    @ViewById(R.id.profilePicture)
    ImageView imageToUpload;

    PhotoUploadDialog photoDialog;

    @AfterViews
    protected void init() {

    }

    @Click(R.id.email_register_button)
    void signIn(){
        attemptRegister();
    }

    @EditorAction(R.id.passwordregister)
    boolean passwordEditorAction(int id){
        if (id == R.id.register|| id == EditorInfo.IME_NULL) {
            attemptRegister();
            return true;
        }
        return false;
    }

    @Click(R.id.upload_Profile_Picture)
    void showProfileDialog(){
        FragmentManager manager = getSupportFragmentManager();
        photoDialog = new PhotoUploadDialog(imageToUpload);
        photoDialog.show(getSupportFragmentManager(), "Profile Picture");
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptRegister() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mFirstnameView.setError(null);
        mLastnameView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String firstname = mFirstnameView.getText().toString();
        String lastname = mLastnameView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_tooshort_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (!isNameValid(lastname)) {
            mLastnameView.setError(getString(R.string.error_invalid_name));
            focusView = mLastnameView;
            cancel = true;
        }

        if (!isNameValid(firstname)) {
            mFirstnameView.setError(getString(R.string.error_invalid_name));
            focusView = mFirstnameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            Bitmap image=null;
            try {
                image = ((BitmapDrawable) imageToUpload.getDrawable()).getBitmap();
            }catch (Exception e){}
            new RestNetworkTasks.UserRegisterTask(mProgressView,mRegisterFormView,getBaseContext(),
                    firstname, lastname, email, password, image){
                @Override
                protected void onPostExecute(final Integer success) {
                    super.onPostExecute(success);
                    if (success==1) {
                        finish();
                    } else {
                        mPasswordView.setError(getString(R.string.error_incorrect_password));
                        mPasswordView.requestFocus();
                    }
                }
            }.execute();
            //mAuthTask = new UserRegisterTask(firstname, lastname, email, password, image);
            //mAuthTask.execute((Void) null);
        }
    }

    private boolean isNameValid(String name){
        return name.length()>2;
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@")&&email.contains(".")&&email.length()>7;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 7;
    }



    /**
     * Shows the progress UI and hides the login form.
     */
    /*@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    /*public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final String mfirstname;
        private final String mlastname;
        private final String mEmail;
        private final String mPassword;
        private final Bitmap mProfile;

        UserRegisterTask(String firstname, String lastname, String email, String password, Bitmap profil) {
            mfirstname=firstname;
            mlastname=lastname;
            mEmail = email;
            mPassword = password;
            mProfile = profil;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                RestClient.USERS.add(mEmail + ":" + mPassword);
                if(mProfile!=null) {
                    RestClient.safeProfilePicture(mProfile);
                }
                Thread.sleep(2000);
                return true;
            } catch (InterruptedException e) {
                return false;
            }
            //return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(photoDialog!=null) {
            photoDialog.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(photoDialog!=null) {
            photoDialog.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data !=null){
            Uri selectedImage = data.getData();
            imageToUpload.setImageURI(selectedImage);
        }else if(requestCode == RESULT_TAKE_IMAGE && resultCode == RESULT_OK && data !=null){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageToUpload.setImageBitmap(imageBitmap);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                uploadFromMediaCenter();
            }
        }
    }*/
}
