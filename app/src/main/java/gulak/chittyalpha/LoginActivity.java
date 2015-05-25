package gulak.chittyalpha;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import android.widget.TextView;
import android.app.ProgressDialog;
import gulak.util.AppStatus;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicInteger;

import gulak.util.ChittyRow;
import gulak.util.LoginAsyncTask;
import gulak.util.LoginAsyncTask.DoLogin;

public class LoginActivity extends Activity implements DoLogin{

    private Button btnLogin;
    private EditText inputUserPhone;
    private EditText inputPassword;
    public String data;
    public String userType;
    public LoginAsyncTask myAsyncTask;
    public ProgressDialog progressDialog;

    public static final String EXTRA_MESSAGE = "message";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    SharedPreferences prefs;
    Context context;

    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
    String SENDER_ID = "922750456515";



    //public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    static final String TAG = "ChittyApp";
    String regid;
    String phNumber=new String();
    //public LoginHelper lh;
    boolean done=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin=(Button) findViewById(R.id.btnSingIn);
        inputUserPhone=(EditText)findViewById(R.id.etUserName);
        inputPassword=(EditText)findViewById(R.id.etPass);
        TextView register= (TextView) findViewById(R.id.reg);
        register.setOnClickListener(new View.OnClickListener(){
          public void onClick(View v){
              Intent registerAct = new Intent(getApplicationContext(),RegisterActivity.class);
              startActivity(registerAct);
                  }
          }

        );


        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {
            //private ProgressDialog progressd = null;
            public void onClick(View view) {
                phNumber = inputUserPhone.getText().toString();
                String password = inputPassword.getText().toString();

                // Check for empty data in the form
                if (phNumber.trim().length() > 0 && password.trim().length() > 0) {
                    // login user Naresh
                    //LoginTask lh=new LoginTask(LoginActivity.this);
                    // Naresh this.progressd = ProgressDialog.show(getApplicationContext(), "Working..", "Downloading Data...", true, false);
                    //lh.execute(phNumber,password);
                    //progressDialog = ProgressDialog.show(LoginActivity.this, "Wait", "Downloading...");
                    if (AppStatus.getInstance(getApplicationContext()).isOnline()){
                        myAsyncTask = new LoginAsyncTask(LoginActivity.this);
                        myAsyncTask.execute(phNumber,password);
                        progressDialog = ProgressDialog.show(LoginActivity.this, "Parchi ", "Login In...");
                    } else

                        Toast.makeText(getApplicationContext(),
                                "Not Network Access, Please check your Internet Connection", Toast.LENGTH_LONG)
                                .show();



                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }
    });
    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {



            default:
                return super.onOptionsItemSelected(item);
        }


    }



    @Override
    public void doInBackground() {

    }

    @Override
    public void doPostExecute(String userRow) {
        progressDialog.dismiss();
        //String regsid=new String();


        if (userRow.isEmpty()|| userRow.equalsIgnoreCase("0")) {
            System.out.println("User Not found" + userRow);
            Toast.makeText(getApplicationContext(),
                    "Could not login, Pls check your password", Toast.LENGTH_LONG)
                    .show();
        }
        else {
            System.out.println("Login Found Reg id from DB " + userRow);

            JSONObject jo;
            JSONArray jArray;

            try {
                StringBuffer sb = new StringBuffer("{\"userResults\":[");
                if (!userRow.isEmpty() && !(userRow.equalsIgnoreCase("0")))
                    sb.append(userRow);
                else
                    sb.append("[{\"userphone\":\"No\",\"regid\":\"AZxcvvbbGT\"}]");

                sb.append("]}");
                jo = new JSONObject(sb.toString());
                jArray = jo.getJSONArray("userResults");
                System.out.println("Got users " + jArray.length());
                //values=new ChittyRow[jArray.length()];
                for (int i = 0; i < jArray.length(); i++) {
                    //ChittyRow ro=new ChittyRow();
                    regid = jArray.getJSONObject(i).getString("regid");
                    userType = jArray.getJSONObject(i).getString("usertype");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(" Reg id , User Type " + regid + "   :  " + userType);
            Intent mainActivityIntent = null;
            if (userType.equals("1"))
                mainActivityIntent = new Intent(LoginActivity.this, MainActivity.class);
            if (userType.equals("0"))
                mainActivityIntent = new Intent(LoginActivity.this, MyChittyActivity.class);

            mainActivityIntent.putExtra("regid", regid);
            mainActivityIntent.putExtra("myPhone", phNumber);
            mainActivityIntent.putExtra("usertype", userType);
            startActivity(mainActivityIntent);
        }
    }


}// end of class
    // Naresh
    /*class LoginTask extends AsyncTask<String, Integer, String> {

        final String USER_AGENT = "Mozilla/5.0";
        StringBuffer resp=null;
        private ProgressDialog dialog;
        private LoginActivity activity;
        private Context context;



        private LoginTask(Context context) {
            this.context = context.getApplicationContext();
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setMessage("Authenticating user...");
            dialog.show();
        }

        protected  String doInBackground(String... params) {


            String url = "https://intense-plateau-9455.herokuapp.com/login";
            URL obj = null;
            try {
                obj = new URL(url);

                HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
                String userPhone=params[0];
                String pwd=params[1];
                //add reuqest header
                con.setRequestMethod("POST");
                con.setRequestProperty("User-Agent", USER_AGENT);
                con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                //myDoSomethingCallBack.doInBackground(10);
                String urlParameters = "fromPhone=" + userPhone + "&pwd="+ pwd;
                Log.i("Chitty App Login" , urlParameters);
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
            //print result
            System.out.println("Hello : " + resp.toString());

            return new String(resp.toString());
        }
        @Override
        protected void onPostExecute(String s) {

            dialog.dismiss();
            Intent i = new Intent (context, MainActivity.class);
            //Dangerous Piece of Code Not recommended
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);

        }

    }*/

