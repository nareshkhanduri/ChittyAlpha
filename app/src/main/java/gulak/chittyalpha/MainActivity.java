package gulak.chittyalpha;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.content.ClipData;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.util.Log;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.app.AlertDialog;
import android.view.View.OnClickListener;
import android.content.DialogInterface;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import java.util.concurrent.atomic.AtomicInteger;
import android.content.SharedPreferences;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import gulak.util.PushTransaction;
import android.os.AsyncTask;
import java.io.IOException;


public class MainActivity extends ActionBarActivity {

private ImageView tv;
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    SharedPreferences prefs;
    Context context;

    String regid=new String();
    String phNumber=new String();
    String chittyVal=new String();
    String userType=new String("");
    EditText toPhone;
    // This is Same as google his is the project number we got
    //from the API Console, as described in "Getting Started."
    String SENDER_ID = "922750456515";
    static final String TAG = "ChittyApp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        regid = bundle.getString("regid");
        phNumber=bundle.getString("myPhone");
        userType=bundle.getString("usertype");
        //GoogleApiClient
        System.out.println("Main Activity Got Regid from Bundle : " + regid);
        context = getApplicationContext();
        if(checkPlayServices())
            Log.i("CParchi App" , " Got Play servces");
        setContentView(R.layout.activity_main);

            if (regid.isEmpty()) {
                Toast.makeText(this,"Not Registered ...",Toast.LENGTH_LONG).show();
                finish();
            }
            tv = (ImageView) findViewById(R.id.transferImg);
            tv.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    showInputDialog();
                }
            });
            findViewById(R.id.imageView).setOnTouchListener(new MyTouchOne());
            findViewById(R.id.imageButton2).setOnTouchListener(new MyTouchFive());
            findViewById(R.id.imageView2).setOnTouchListener(new MyTouchTwo());
            findViewById(R.id.transferImg).setOnDragListener(new MyDragListener());

    }

    protected void showInputDialog(){
        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        View promptView = layoutInflater.inflate(R.layout.recipentdetails, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(promptView);
        TextView chittyText = (TextView)findViewById(R.id.textview);
        alertDialogBuilder.setTitle("Issue " + chittyText.getText() + " Parchi Points");
        chittyVal= chittyText.getText().toString();
        toPhone=(EditText)promptView.findViewById(R.id.toPhone);
        //final EditText editText = (EditText) promptView.findViewById(R.id.editText2);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(), "Transfering" , Toast.LENGTH_SHORT).show();
                        PushTransaction pu = new PushTransaction();
                        try {

                            pu.execute(phNumber,toPhone.getText().toString(),chittyVal);
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
    private final class MyTouchFive implements OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("5", "5");
                DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                view.setVisibility(View.VISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }

    private final class MyTouchOne implements OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("1", "1");
                DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                view.setVisibility(View.VISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }

    private final class MyTouchTwo implements OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("2", "2");
                DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                view.setVisibility(View.VISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }

    class MyDragListener implements OnDragListener {
        //Drawable enterShape = getResources().getDrawable(R.drawable.abc_ab_share_pack_mtrl_alpha);
        //Drawable normalShape = getResources().getDrawable(R.drawable.abc_btn_check_material);

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:

                    break;
                case DragEvent.ACTION_DRAG_EXITED:

                    break;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign View to ViewGroup
                    ClipData clipData=event.getClipData();
                    TextView myValueText = (TextView)findViewById(R.id.textview);
                    String value=myValueText.getText().toString();
                    int val=new Integer(value).intValue();
                    String newVal=clipData.getItemAt(0).getText().toString();
                    int finalVal=val + new Integer(newVal).intValue();
                    if(finalVal>=10) {
                        finalVal=val;
                        Toast.makeText(getApplicationContext(), "Max Chitty allowd 9" , Toast.LENGTH_SHORT).show();
                    }
                    String currentVal=String.valueOf(finalVal);
                    myValueText.setText(currentVal);
                    //int newVal=Integer.parseInt(String.valueOf(myValueText.getText()));
                    //myValueText.setText(newVal);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:

                default:
                    break;
            }
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        // Handle presses on the action bar items
        switch (id) {
            case R.id.action_mychitty:
                openMyChitiys();
                return true;
            case R.id.action_logout:
                logout();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }

    private void openMyChitiys() {
        Intent myChittysActivityIntent= new Intent(MainActivity.this,IssuedParchi.class);
        myChittysActivityIntent.putExtra("regid",regid);
        myChittysActivityIntent.putExtra("myPhone",phNumber);
        myChittysActivityIntent.putExtra("usertype",userType);
        startActivity(myChittysActivityIntent);
    }

    public void logout(){
        Intent myChittysActivityIntent= new Intent(MainActivity.this,LoginActivity.class);
        myChittysActivityIntent.putExtra("regid","");
        myChittysActivityIntent.putExtra("myPhone","");
        startActivity(myChittysActivityIntent);

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
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
}
