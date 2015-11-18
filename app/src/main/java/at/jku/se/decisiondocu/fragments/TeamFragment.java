package at.jku.se.decisiondocu.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import at.jku.se.decisiondocu.R;

@EFragment(R.layout.fragment_team)
public class TeamFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */

    private static final String ARG_SECTION_NUMBER = "section_number";

    public TeamFragment() {
    }

    @AfterViews
    void init(){
        Log.i("Test2", "Test2");
    }


}