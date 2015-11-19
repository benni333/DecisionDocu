package at.jku.se.decisiondocu.asynctask;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.View;

/**
 * Created by Benjamin on 19.11.2015.
 */
public class ProgressbarTransaction {

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static void showProgress(final boolean show, final View progressbar, final View viewToHide, Context context) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if(progressbar!=null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                int shortAnimTime = context.getResources().getInteger(android.R.integer.config_shortAnimTime);

                if(viewToHide!=null) {
                    viewToHide.setVisibility(show ? View.GONE : View.VISIBLE);
                    viewToHide.animate().setDuration(shortAnimTime).alpha(
                            show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            viewToHide.setVisibility(show ? View.GONE : View.VISIBLE);
                        }
                    });
                }

                progressbar.setVisibility(show ? View.VISIBLE : View.GONE);
                progressbar.animate().setDuration(shortAnimTime).alpha(
                        show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        progressbar.setVisibility(show ? View.VISIBLE : View.GONE);
                    }
                });
            } else {
                // The ViewPropertyAnimator APIs are not available, so simply show
                // and hide the relevant UI components.
                progressbar.setVisibility(show ? View.VISIBLE : View.GONE);
                if (viewToHide!=null)viewToHide.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        }
    }


}
