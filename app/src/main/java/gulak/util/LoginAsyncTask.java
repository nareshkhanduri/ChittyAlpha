package gulak.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import gulak.chittyalpha.MainActivity;
import gulak.chittyalpha.LoginActivity;

/**
 * Created by naresh on 4/30/15.
 */
public class LoginAsyncTask extends AsyncTask<String, Integer, String> {

    final String USER_AGENT = "Mozilla/5.0";
    boolean testing = false;
    StringBuffer resp=null;
    private ProgressDialog dialog;
    private LoginActivity activity;
    private Context context;
    DoLogin myDoLoginCallBack;
    String userRow;
    public interface DoLogin {
        void doInBackground();
        void doPostExecute(String regestrationId);
    }

    public LoginAsyncTask(DoLogin callback) {
        myDoLoginCallBack=callback;
    }

    @Override
    protected void onPreExecute() {

    }

    protected  String doInBackground(String... params) {

        myDoLoginCallBack.doInBackground();
        String url = "https://intense-plateau-9455.herokuapp.com/login";
        URL obj = null;
        if (!testing) {
            try {
                obj = new URL(url);

                HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
                String userPhone = params[0];
                String pwd = params[1];
                //add reuqest header
                con.setRequestMethod("POST");
                con.setRequestProperty("User-Agent", USER_AGENT);
                con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                //myDoSomethingCallBack.doInBackground(10);
                String urlParameters = "fromPhone=" + userPhone + "&pwd=" + pwd;
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
        } else
            resp = new StringBuffer("test****adfadfasdfyayiyauigyiu");
        //print result
        System.out.println("Hello frm Login : " + resp.toString());
        userRow=resp.toString();
        return new String(resp.toString());
    }
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        myDoLoginCallBack.doPostExecute(userRow);

    }

}