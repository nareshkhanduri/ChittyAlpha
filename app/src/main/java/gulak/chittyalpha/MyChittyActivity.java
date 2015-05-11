package gulak.chittyalpha;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONObject;
import android.app.Activity;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import gulak.util.ChittysAsyncTask;
import gulak.util.ReturnChittyAsyncTask;
import gulak.util.RegisterAsyncTask;
import gulak.util.ChittyRowAdapter;
import gulak.util.ChittyRow;

public class MyChittyActivity extends ActionBarActivity implements ChittysAsyncTask.DoChittys{

    private ListView mychittysList ;


    public ProgressDialog progressDialog;
    ChittysAsyncTask myAsyncTask;
    ReturnChittyAsyncTask pushTransactionAsync;
    TextView tvChitty;
    String regid=new String();
    String phNumber=new String();
    String chittyVal=new String();
    String toPhone = new String("");
    String chittyId=new String("");
    ArrayList<ChittyRow> values = new ArrayList<ChittyRow>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_chitty);
        Bundle bundle = getIntent().getExtras();
        regid = bundle.getString("regid");
        phNumber=bundle.getString("myPhone");
         /*tvChitty=(TextView)findViewById(R.id.txtChittyDetails);

        System.out.println ( " check this out : " + chittyVal);
        tvChitty.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showConfirmDialog();
            }
        });
*/
        mychittysList = (ListView)findViewById(R.id.myChittysListView);

        // Create and populate a List of planet names.
        progressDialog = ProgressDialog.show(MyChittyActivity.this, "Chitty App", "Getting Chittys...");
        myAsyncTask = new ChittysAsyncTask(MyChittyActivity.this);
        System.out.println("Get Chittys for " + phNumber);
        myAsyncTask.execute(phNumber);


    }

    public void showConfirmDialog(View view) {
        View v = (View) view.getParent();
        tvChitty=(TextView)v.findViewById(R.id.txtChittyDetails);
        System.out.println(" check this out " + tvChitty.getTag().toString());
        String indexofarray=tvChitty.getTag().toString();
        int indexval=  Integer.parseInt(indexofarray);
        ChittyRow cr= values.get(indexval);

        toPhone=cr.getFromPhone();
        chittyVal=cr.getChittyVal();
        chittyId=cr.getChittyId();
        System.out.println("tophone:" + toPhone +"chittyval:" +chittyVal);
       LayoutInflater layoutInflater = LayoutInflater.from(MyChittyActivity.this);
        View promptView = layoutInflater.inflate(R.layout.recipentdetails, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MyChittyActivity.this);
        alertDialogBuilder.setTitle("Confirm Transfering : " + chittyVal + " To : " +chittyVal);
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(), "Transfering" , Toast.LENGTH_SHORT).show();
                        ReturnChittyAsyncTask rc = new ReturnChittyAsyncTask();
                        try {

                            rc.execute(phNumber,toPhone,chittyVal,chittyId);
                            Toast.makeText(getApplicationContext(), "done transfer" , Toast.LENGTH_SHORT).show();
                            TextView myValText = (TextView)findViewById(R.id.textview);
                            myValText.setText("0");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //editText.setText("Hello");
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_chitty, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        // Handle presses on the action bar items
        switch (id) {

            case R.id.action_logout:
                logout();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }


    public void logout(){
        //Intent myChittysActivityIntent= new Intent(MyChittyActivity.this,LoginActivity.class);
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage( getBaseContext().getPackageName() );
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //myChittysActivityIntent.putExtra("regid","");
        //myChittysActivityIntent.putExtra("myPhone","");
        startActivity(i);

    }

    @Override
    public void doInBackground() {

    }

    @Override
    public String doPostExecute(String result) {
        progressDialog.dismiss();
        JSONObject jo;
        JSONArray jArray;
        System.out.println("Got All Chittys" + result);

        //ChittyRow[] values =null;
        try {
            StringBuffer sb =new StringBuffer("{\"chittyResults\":");
            if(!result.isEmpty()&& result.length()>5)
                sb.append(result);
            else
                sb.append("[{\"fromphone\":\"No\",\"chittyval\":\"Chittys\"}]");

            sb.append("}");
            jo = new JSONObject(sb.toString());
            jArray = jo.getJSONArray("chittyResults");
            System.out.println("Got All Chittys " + jArray.length());
            //values=new ChittyRow[jArray.length()];
            for ( int i=0;i<jArray.length();i++) {
                ChittyRow ro=new ChittyRow();
                ro.setFromPhone(jArray.getJSONObject(i).getString("fromphone"));
                ro.setChittyVal(jArray.getJSONObject(i).getString("chittyval"));
                ro.setChittyId(jArray.getJSONObject(i).getString("id"));
                ro.setAtIndex(i);
                values.add(ro);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        ChittyRowAdapter listAdapter = new ChittyRowAdapter(this, values);
        //listAdapter = new ArrayAdapter<String>(this, R.layout.chittyrow, R.id.txtChittyDetails,chittyList);
        // Set the ArrayAdapter as the ListView's adapter.
        mychittysList.setAdapter( listAdapter );

        return result;
    }
}
