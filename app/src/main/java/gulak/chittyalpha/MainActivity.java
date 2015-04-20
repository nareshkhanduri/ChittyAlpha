package gulak.chittyalpha;

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


public class MainActivity extends ActionBarActivity {

private ImageView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv=(ImageView)findViewById(R.id.transferImg);
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
        alertDialogBuilder.setTitle("Chitty Points : " + chittyText.getText());

        //final EditText editText = (EditText) promptView.findViewById(R.id.editText2);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
