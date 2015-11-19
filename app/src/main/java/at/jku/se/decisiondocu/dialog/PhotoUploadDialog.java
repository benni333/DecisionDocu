package at.jku.se.decisiondocu.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.widget.ImageView;

import at.jku.se.decisiondocu.R;

/**
 * Created by Benjamin on 19.11.2015.
 */
public class PhotoUploadDialog extends DialogFragment {

    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int RESULT_TAKE_IMAGE = 2;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 0;

    private ImageView imageToUpload;

    public PhotoUploadDialog(ImageView imageToUpload){
        this.imageToUpload=imageToUpload;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme_AlertDialogCustom));
        builder.setTitle(R.string.new_existing_picture)
                .setItems(R.array.picture_option, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0){
                            uploadFromMediaCenter();
                        }else{
                            uploadWithCamera();
                        }

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    private void uploadFromMediaCenter() {
        Log.i("Upload", "FromMediaCenter");
        if(mayRequestMedia()) {
            try {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                getActivity().startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private void uploadWithCamera(){
        Log.i("Upload", "WithCamera");
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            getActivity().startActivityForResult(intent, RESULT_TAKE_IMAGE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean mayRequestMedia(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Log.i("RequestMedia", "first"); //Having Permission
            return true;
        }
        else if (getActivity().checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Log.i("RequestMedia","second"); //Having Permission
            return true;
        }else {
            Log.i("RequestMedia","third");//ask for Permission
            getActivity().requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
                Log.i("Test", "Test");
                Uri selectedImage = data.getData();
                imageToUpload.setImageURI(selectedImage);
            } else if (requestCode == RESULT_TAKE_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
                Log.i("Test", "Test");
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imageToUpload.setImageBitmap(imageBitmap);
            }
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                uploadFromMediaCenter();
            }
        }
    }
}
