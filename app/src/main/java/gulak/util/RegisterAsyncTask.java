package gulak.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

import javax.net.ssl.HttpsURLConnection;

import gulak.chittyalpha.LoginActivity;
import gulak.chittyalpha.RegisterActivity;

/**
 * Created by naresh on 5/1/15.
 */
public class RegisterAsyncTask extends AsyncTask<String, Integer, String> {

    final String USER_AGENT = "Mozilla/5.0";
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    static final String TAG = "ChittyApp";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    // This is Same as google his is the project number we got
    //from the API Console, as described in "Getting Started."
    String SENDER_ID = "922750456515";

    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    SharedPreferences prefs;


    String regid;
    StringBuffer resp=null;
    private ProgressDialog dialog;
    private LoginActivity activity;
    private Context context;
    DoRegister myDoRegisterCallBack;
    public interface DoRegister {
        void doInBackground();
        String doPostExecute(String result);
    }

    public RegisterAsyncTask(DoRegister callback) {
        myDoRegisterCallBack=callback;
    }

    @Override
    protected void onPreExecute() {

    }

    protected  String doInBackground(String... params) {

        myDoRegisterCallBack.doInBackground();
        URL obj = null;
        String url = "https://intense-plateau-9455.herokuapp.com/register";

        context = ((RegisterActivity)myDoRegisterCallBack).getApplicationContext();
        if(checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(context);
            regid = registerInBackground();
            if (regid.isEmpty()) {
                Log.i( "Chitty App", " Unsucessful Resigteration" );
            }else{
                Log.i( "Chitty App", "  Resigteration id : " + regid );
            }
        }

        if(!regid.isEmpty()) {

            try {
                obj = new URL(url);

                HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
                String userPhone = params[0];
                String pwd = params[1];
                String chittyId = params[2];
                //add reuqest header
                con.setRequestMethod("POST");
                con.setRequestProperty("User-Agent", USER_AGENT);
                con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                //myDoSomethingCallBack.doInBackground(10);
                String userEmail = "someone@yahoo.com";
                String lat = "54.65";
                String strlong = "74.45";
                String deviceId = "IMEI67867";
                String key = "hkhjkhkj";
                // This is registeration Id i have to get from Google API.
                String regsid = regid;

                String urlParameters = "userPhone=" + userPhone + "&pwd=" + pwd + "&chittyId=" + chittyId + "&email=" + userEmail + "&lat=" + lat + "&long=" + strlong + "&deviceid=" + deviceId + "&key=" + key + "&regid=" + regsid;

                Log.i("Chitty App Login", urlParameters);
                System.out.println("\nSending 'POST' request to URL : " + url);
                // Send post request
                con.setDoOutput(true);

                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.write(urlParameters.getBytes());
                wr.flush();
                wr.close();
                int responseCode = con.getResponseCode();
                //myDoSomethingCallBack.doInBackground(50);
                System.out.println("\n Login 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters);
                System.out.println("Response Code : " + responseCode);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                resp = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    resp.append(inputLine);
                }
                in.close();


            } catch (Exception e) {
                e.printStackTrace();
            }

        }//print result
        System.out.println("Hello : " + resp.toString());

        return new String(regid);
    }
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        myDoRegisterCallBack.doPostExecute(regid);

    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                Log.i("ChittyApp","No Play Service");
                //GooglePlayServicesUtil.getErrorDialog(resultCode, context, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("ChittyApp","No Play Service");

            }
            return false;
        }
        return true;
    }


    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    private String registerInBackground() {

                System.out.println( "Registration registerInBackground : " + regid);
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = regid;
                    System.out.println( "Registration Id is : " + regid);
                    Log.i(TAG , "Registirng in Background : " + regid );
                    // Send the registration ID to your server over HTTP,
                    // so it can use GCM/HTTP or CCS to send messages to your app.
                    // The request to your server should be authenticated if your app
                    // is using accounts.
                    //sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device
                    // will send upstream messages to a server that echo back the
                    // message using the 'from' address in the message.

                    // Persist the registration ID - no need to RegisterActivity again.
                    //storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to RegisterActivity.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

}
