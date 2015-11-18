package at.jku.se.decisiondocu.restclient;

import android.graphics.Bitmap;
import android.util.Log;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import java.io.ByteArrayOutputStream;

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

    public static void safeProfilePicture(Bitmap image){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        //InputStream inputStream= new ByteArrayInputStream(byteArrayOutputStream.toByteArray());


        try {
            System.out.println(uploadProfilePicture(byteArrayOutputStream.toByteArray()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static String uploadProfilePicture(byte[] fileContent)throws Exception {
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
            webTarget= webTarget.path("document").path("upload");
            Log.i("URI", webTarget.getUri().getHost() + webTarget.getUri().getPath());

            // set file upload values
            //streamDataBodyPart = new StreamDataBodyPart("uploadFile", inputStream, "01.jpeg", MediaType.APPLICATION_OCTET_STREAM_TYPE);
            formDataMultiPart = new FormDataMultiPart();
            formDataMultiPart.field("uploadFile", fileContent, MediaType.APPLICATION_OCTET_STREAM_TYPE);

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
            //formDataMultiPart.cleanup();
            //formDataMultiPart.close();
            //response.close();
        }
        return responseString;


    }
}
