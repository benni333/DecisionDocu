package at.jku.se.decisiondocu.restclient;

import android.graphics.Bitmap;
import android.util.Log;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.file.StreamDataBodyPart;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Benjamin on 18.11.2015.
 */
public class RestClient {

    //private final static String httpURL = "http://localhost:8080/DecisionDocu/api/document/upload";

    public static List<String> USERS = new ArrayList<String>();
    static{
        if(USERS.size()==0) {
            USERS.add("foo@example.com:hello");
            USERS.add("bar@example.com:world");
        }
    }

    public static String getToken(String email, String password){
        try {
            // Simulate network access.
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            return null;
        }
        for (String credential : USERS) {
            String[] pieces = credential.split(":");
            if (pieces[0].equals(email)) {
                if(pieces[1].equals(password)){
                    return "AllowedToken";
                }
            }
        }
        return null;
    }

    public static boolean registerUser(String firstname, String lastname, String email, String password, Bitmap profil){
        try {
            USERS.add(email + ":" + password);
            if(profil!=null) {
                RestClient.safeProfilePicture(profil, USERS.size());
            }
            //Thread.sleep(2000);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    private static void safeProfilePicture(Bitmap image, int id){
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            System.out.println(uploadProfilePicture(byteArrayOutputStream.toByteArray(),id));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static String uploadProfilePicture(byte[] fileContent, int id)throws Exception {
        // local variables
        WebTarget webTarget = null;
        Invocation.Builder invocationBuilder = null;
        Response response = null;
        FormDataMultiPart formDataMultiPart = null;
        int responseCode;
        String responseMessageFromServer = null;
        String responseString = null;

        try{
            // invoke service after setting necessary parameters
            webTarget = RestHelper.getWebTargetWithMultiFeature();
            webTarget= webTarget.path("uploadDocument").path("profilePicture");
            Log.i("URI", webTarget.getUri().getHost() + webTarget.getUri().getPath());

            // set file upload values
            //streamDataBodyPart = new StreamDataBodyPart("uploadFile", inputStream, "01.jpeg", MediaType.APPLICATION_OCTET_STREAM_TYPE);
            formDataMultiPart = new FormDataMultiPart();
            StreamDataBodyPart bodyPart =new StreamDataBodyPart("uploadFile",new ByteArrayInputStream(fileContent),id+".jpg",MediaType.APPLICATION_OCTET_STREAM_TYPE);
            //formDataMultiPart.field("uploadFile", fileContent, MediaType.APPLICATION_OCTET_STREAM_TYPE);

            formDataMultiPart.bodyPart(bodyPart);

            response = webTarget.request().post(Entity.entity(formDataMultiPart, MediaType.MULTIPART_FORM_DATA));

            // get response code
            responseCode = response.getStatus();
            System.out.println("Response code: " + responseCode);

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed with HTTP error code : " + responseCode);
            }

            // get response message
            responseMessageFromServer = response.getStatusInfo().getReasonPhrase();
            System.out.println("ResponseMessageFromServer: " + responseMessageFromServer);

            // get response string
            responseString = response.readEntity(String.class);
        }
        catch(Exception ex) {
            throw ex;
        }
        finally{
            // release resources, if any
            //streamDataBodyPart.cleanup();
            formDataMultiPart.cleanup();
            formDataMultiPart.close();
            response.close();
        }
        return responseString;


    }

}
