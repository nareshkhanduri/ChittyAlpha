package gulak.util;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by naresh on 5/10/15.
 */
public class ReturnChittyAsyncTask extends AsyncTask<String, Integer, String> {
    private final String USER_AGENT = "Mozilla/5.0";
    StringBuffer resp=null;
    // HTTP POST request
    public  String doInBackground(String... params) {

        String fromPhone  = params[0];
        String toPhone = params[1];
        String chittyVal=params[2];
        String chittyId=params[3];
        String lat ="54.32";
        String strlong ="73.34";

        /// I have to get Reg id for toPhone Number

        String url = "https://intense-plateau-9455.herokuapp.com/returnchitty";
        URL obj = null;
        try {
            obj = new URL(url);
            String urlParameters = "fromPhone=" + fromPhone + "&tophone="+ toPhone + "&lat=" + lat +"&long=" + strlong + "&chittyval=" + chittyVal + "&chittyId=" + chittyId;
            System.out.println("\nSending 'POST' request to URL : " + urlParameters);

            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            //add reuqest header
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
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

        System.out.println("Hello");
        return resp.toString();
    }



}
