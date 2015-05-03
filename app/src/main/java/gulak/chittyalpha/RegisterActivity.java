package gulak.chittyalpha;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import gulak.util.RegisterAsyncTask;


public class RegisterActivity extends ActionBarActivity implements RegisterAsyncTask.DoRegister {
    private Button btnRegister ;
    private EditText phonerNumber;
    private EditText password;
    private EditText rePassword;
    private EditText chittyId;
    public ProgressDialog progressDialog;
    public RegisterAsyncTask myAsyncTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btnRegister = (Button) findViewById(R.id.btnReg);
        phonerNumber=(EditText)findViewById(R.id.etRegPhNumber);
        password = (EditText)findViewById(R.id.etRegPass);
        rePassword=(EditText)findViewById(R.id.etRegPass2);
        chittyId=(EditText)findViewById(R.id.etChittyId);

        btnRegister.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {
                String phNumber = phonerNumber.getText().toString();
                String pass= password.getText().toString();
                String rePass=rePassword.getText().toString();
                String chittyIdStr= chittyId.getText().toString();
                if (phNumber.trim().length() > 0 && pass.trim().length() > 0 && chittyIdStr.trim().length()>0) {

                    myAsyncTask = new RegisterAsyncTask(RegisterActivity.this);
                    myAsyncTask.execute(phNumber,pass,chittyIdStr);
                    progressDialog = ProgressDialog.show(RegisterActivity.this, "Chitty App", "Registering...");

                } else{
                    Toast.makeText(getApplicationContext(),
                            "Please enter All details to Register ...", Toast.LENGTH_LONG)
                            .show();
                }

            }

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void doInBackground() {

    }

    @Override
    public String doPostExecute(String result) {
        progressDialog.dismiss();
        Intent loginActivityIntent= new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(loginActivityIntent);
        System.out.println("Got Reg id in Register Activity" + result);
        if(result.isEmpty())
            finish();
        return result;
    }
}
