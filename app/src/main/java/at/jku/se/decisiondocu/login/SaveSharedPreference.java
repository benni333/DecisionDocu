package at.jku.se.decisiondocu.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Benjamin on 17.11.2015.
 */
public class SaveSharedPreference
{
    static final String PREF_USER_EMAIL= "";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserEmail(Context ctx, String userEmail)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_EMAIL, userEmail);
        editor.commit();
    }

    public static String getUserEmail(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_EMAIL, "");
    }

    public static void clearUserName(Context ctx)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear(); //clear all stored data
        editor.commit();
    }

    public static boolean isValidUser(Context ctx){
        //TODO: Syntax überlegen wann kein Login notwending ist, DB-Abfrage, evt. zeitliche begrenzung
        return getUserEmail(ctx)!=""&&getUserEmail(ctx).length()>0;
    }
}
