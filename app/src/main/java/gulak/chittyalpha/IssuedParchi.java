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
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import gulak.util.ChittyRow;
import gulak.util.ChittyRowAdapter;
import gulak.util.ChittysAsyncTask;
import gulak.util.IssuedParchiRowAdapter;
import gulak.util.ReturnChittyAsyncTask;


public class IssuedParchi extends ActionBarActivity implements ChittysAsyncTask.DoChittys,ReturnChittyAsyncTask.DoReturnChittys{

    private ListView myParchiList ;


    public ProgressDialog progressDialog;
    public ProgressDialog progressReturnDialog;
    ChittysAsyncTask myAsyncTask;
    ReturnChittyAsyncTask pushTransactionAsync;
    TextView tvChitty;
    String userType=new String();
    String regid=new String();
    String phNumber=new String();
    String chittyVal=new String();
    String toPhone = new String("");
    String chittyId=new String("");
    String real_phno="";
    ArrayList<ChittyRow> values = new ArrayList<ChittyRow>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issued_parchi);
        Bundle bundle = getIntent().getExtras();
        regid = bundle.getString("regid");
        phNumber=bundle.getString("myPhone");
        userType=bundle.getString("usertype");
         /*tvChitty=(TextView)findViewById(R.id.txtChittyDetails);

        System.out.println ( " check this out : " + chittyVal);
        tvChitty.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showConfirmDialog();
            }
        });
*/
        myParchiList = (ListView)findViewById(R.id.myIssuedParchisView);

        // Create and populate a List of planet names.
        progressDialog = ProgressDialog.show(IssuedParchi.this, "Parchi App", "Getting Parchi's...");
        myAsyncTask = new ChittysAsyncTask(IssuedParchi.this);
        System.out.println("Get Chittys for " + phNumber);
        myAsyncTask.execute(phNumber,userType);


    }


    public void showIssuedConfirmDialog(View view) {
        View v = (View) view.getParent();
        tvChitty=(TextView)v.findViewById(R.id.txtParchiDetails);
        //System.out.println(" check this out " + tvChitty.getTag().toString());
        String indexofarray=tvChitty.getTag().toString();
        int indexval=  Integer.parseInt(indexofarray);
        ChittyRow cr= values.get(indexval);

        toPhone=cr.getFromPhone();
        chittyVal=cr.getChittyVal();
        chittyId=cr.getChittyId();

        real_phno=cr.getRealph_no();

        System.out.println("tophone:" + toPhone +"chittyval:" +chittyVal+real_phno);
        LayoutInflater layoutInflater = LayoutInflater.from(IssuedParchi.this);
        View promptView = layoutInflater.inflate(R.layout.recipentdetails, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(IssuedParchi.this);
        alertDialogBuilder.setTitle("Redeem : " + chittyVal + " From : " +toPhone);
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(), "Transfering" , Toast.LENGTH_SHORT).show();
                        ReturnChittyAsyncTask rc = new ReturnChittyAsyncTask(IssuedParchi.this);
                        progressReturnDialog = ProgressDialog.show(IssuedParchi.this, "Parchi App", "Redemming Parchis...");

                        try {

                            rc.execute(phNumber,toPhone,chittyVal,chittyId,real_phno);
                            Toast.makeText(getApplicationContext(), "Done transfer" , Toast.LENGTH_SHORT).show();
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
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
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
                ro.setRealph_no(jArray.getJSONObject(i).getString("tophone"));
                ro.setFromPhone(jArray.getJSONObject(i).getString("fromphone"));
                ro.setChittyVal(jArray.getJSONObject(i).getString("chittyval"));
                ro.setToPhone(jArray.getJSONObject(i).getString("tophone"));
                ro.setChittyId(jArray.getJSONObject(i).getString("id"));
                ro.setAtIndex(i);
                values.add(ro);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        IssuedParchiRowAdapter listAdapter = new IssuedParchiRowAdapter(this, values);
        //listAdapter = new ArrayAdapter<String>(this, R.layout.chittyrow, R.id.txtChittyDetails,chittyList);
        // Set the ArrayAdapter as the ListView's adapter.
        myParchiList.setAdapter( listAdapter );

        return result;
    }



    @Override
    public String doPostReturn(String result) {
        progressReturnDialog.dismiss();
        JSONObject jo;
        JSONArray jArray;
        System.out.println("Got All Chittys" + result);
        values.clear();
        //ChittyRow[] values =null;
        try {
            StringBuffer sb =new StringBuffer("{\"chittyResults\":");
            if(!result.isEmpty()&& result.length()>5)
                sb.append(result);
            else
                sb.append("[{\"fromphone\":\"No Chittys\",\"chittyval\":\"0\",\"id\":\"0\",\"businessname\":\"0\",\"transactiontime\":\"0\"}]");

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
                ro.setBusinessname(jArray.getJSONObject(i).getString("businessname"));
                ro.setTranstime(jArray.getJSONObject(i).getString("transactiontime"));
                ro.setAtIndex(i);
                values.add(ro);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        IssuedParchiRowAdapter listAdapter = new IssuedParchiRowAdapter(this, values);
        //listAdapter = new ArrayAdapter<String>(this, R.layout.chittyrow, R.id.txtChittyDetails,chittyList);
        // Set the ArrayAdapter as the ListView's adapter.
        myParchiList.setAdapter( listAdapter );

        return result;
    }
}
