package gulak.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import gulak.chittyalpha.LoginActivity;

/**
 * Created by naresh on 5/14/15.
 */
public class RecivedChittyAsyncTask extends AsyncTask<String, Integer, String> {

    final String USER_AGENT = "Mozilla/5.0";
    StringBuffer resp=null;
    private ProgressDialog dialog;
    private LoginActivity activity;
    private Context context;
    DoRecivedChittys myDoChittysCallBack;
    String allrows;

    public interface DoRecivedChittys {


        String doPostRecivedExecute(String result);
    }

    public RecivedChittyAsyncTask(DoRecivedChittys callback) {
        myDoChittysCallBack=callback;
    }

    protected  String doInBackground(String... params) {



        boolean testing =false;
        URL obj = null;
        String url = "https://intense-plateau-9455.herokuapp.com/mychittys";
        HttpsURLConnection con;
        String userPhone = params[0];
        String userType = params[1];
        String urlParameters = "userPhone=" + userPhone + "&userType=" + userType ;
        Log.i("Chitty App Trans", urlParameters);
        System.out.println("\nSending 'POST' request to URL : " + url);
        // Send post request
        if (!testing) {
            try {
                obj = new URL(url);
                con = (HttpsURLConnection) obj.openConnection();
                //add reuqest header
                con.setRequestMethod("POST");
                con.setRequestProperty("User-Agent", USER_AGENT);
                con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                con.setDoOutput(true);

                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.write(urlParameters.getBytes());
                wr.flush();
                wr.close();
                int responseCode = con.getResponseCode();
                //myDoSomethingCallBack.doInBackground(50);
                System.out.println("\n Get chittys 'POST' request to URL : " + url);
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
                System.out.println("Response for Trans : " + resp.toString());


            } catch (Exception ex) {

            }
        }else
            resp = new StringBuffer("[{\"fromphone\":\"9686570587\",\"tophone\":\"9008428674\",\"lat\":\"54.32\",\"long\":\"73.34\",\"chittyval\":8,\"chittytoken\":null,\"id\":2},{\"fromphone\":\"9686570587\",\"tophone\":\"9008428674\",\"lat\":\"54.32\",\"long\":\"73.34\",\"chittyval\":5,\"chittytoken\":null,\"id\":2}]");
        allrows = resp.toString();
        return resp.toString();

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        myDoChittysCallBack.doPostRecivedExecute(allrows);

    }
}
