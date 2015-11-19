package at.jku.se.decisiondocu.asynctask;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import at.jku.se.decisiondocu.login.SaveSharedPreference;
import at.jku.se.decisiondocu.restclient.RestClient;

/**
 * Created by Benjamin on 19.11.2015.
 */
public class RestNetworkTasks {

    public static abstract class UserLoginTask extends NetworkTask {

        private final String mEmail;
        private final String mPassword;
        private String token;

        protected UserLoginTask(View progressBar, View viewToHide, Context context, String email, String password) {
            super(progressBar,viewToHide,context);
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            token = RestClient.getToken(mEmail,mPassword);
            if(token!=null){
                SaveSharedPreference.setUserEmail(context,mEmail);
                return 1;
            }
            return 0;
        }
    }

    public static abstract class UserRegisterTask  extends NetworkTask {

        private final String mfirstname;
        private final String mlastname;
        private final String mEmail;
        private final String mPassword;
        private final Bitmap mProfile;

        protected UserRegisterTask(View progressBar, View viewToHide, Context context,
                         String firstname, String lastname, String email, String password, Bitmap profil) {
            super(progressBar,viewToHide,context);
            mfirstname=firstname;
            mlastname=lastname;
            mEmail = email;
            mPassword = password;
            mProfile = profil;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            if(RestClient.registerUser(mfirstname, mlastname, mEmail, mPassword, mProfile)){
                return 1;
            }
            return 0;
        }
    }

}
