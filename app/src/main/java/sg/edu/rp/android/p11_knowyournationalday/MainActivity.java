package sg.edu.rp.android.p11_knowyournationalday;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;


import android.support.design.widget.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lv;
    ArrayList<String> arrayList = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.SEND_SMS);

        if (permissionCheck == PermissionChecker.PERMISSION_GRANTED) {
        } else {
            Log.e("SMS - Permission", "SMS access has not been granted");
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.SEND_SMS}, 0);
            // stops the action from proceeding further as permission not granted yet
            return;
        }

        lv = (ListView)findViewById(R.id.lv);
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);

        arrayList.add("Singapore National Day is on 9 Aug");
        arrayList.add("Singapore is 52 years old");
        arrayList.add("Theme is '#OneNationTogether'");

        lv.setAdapter(arrayAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String loginId = pref.getString("loginId","");
        Log.d("Resume",loginId);
        if(loginId.equals("738964")){

        }else{

            //Access Code
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout passPhrase =
                    (LinearLayout) inflater.inflate(R.layout.access, null);
            final EditText etPassphrase = (EditText) passPhrase
                    .findViewById(R.id.editTextPassPhrase);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please login")
                    .setView(passPhrase)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            if(etPassphrase.getText().toString().equals("738964")){
                                String loginId = etPassphrase.getText().toString();
                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                                SharedPreferences.Editor prefedit = prefs.edit();
                                prefedit.putString("loginId",loginId);
                                prefedit.commit();
                            }else{
                                finish();
                            }
                        }
                    });
            // Configure the 'Negative' button for button dialog
            builder.setNegativeButton("No access code",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog,int which){

                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Tally against the respective action item clicked
        //  and implement the appropriate action
        if (item.getItemId() == R.id.quit){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Quit?")
                    // Set text for the positive button and the corresponding
                    //  OnClickListener when it is clicked
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    // Set text for the negative button and the corresponding
                    //  OnClickListener when it is clicked
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            // Create the AlertDialog object and return it
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }else if (item.getItemId() == R.id.send) {
            String [] list = new String[] { "Email", "SMS" };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select the way to enrich your friend")
                    // Set the list of items easily by just supplying an
                    //  array of the items
                    .setItems(list, new DialogInterface.OnClickListener() {
                        // The parameter "which" is the item index
                        // clicked, starting from 0
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {

                                //email
                                Intent email = new Intent(Intent.ACTION_SEND);
                                email.putExtra(Intent.EXTRA_EMAIL,
                                        new String[]{"ivriah12@gmail.com"});
                                email.putExtra(Intent.EXTRA_SUBJECT, "Know Your National Day");

                                String data ="";
                                for (int i=0;i<arrayList.size();i++){
                                    data += arrayList.get(i).toString()+"\n";
                                }

                                email.putExtra(Intent.EXTRA_TEXT, data);
                                email.setType("message/rfc822");
                                startActivity(Intent.createChooser(email,"Choose an Email client :"));


                                Snackbar sb = Snackbar.make(findViewById(android.R.id.content), "Email sent", Snackbar.LENGTH_LONG);
                                sb.show();

                            } else {
                                String data ="";
                                for (int i=0;i<arrayList.size();i++){
                                    data = arrayList.get(i).toString();
                                }

                                //SMS
                                SmsManager smsManager = SmsManager.getDefault();
                                smsManager.sendTextMessage("5554", null, data, null, null);

                                Snackbar sb = Snackbar.make(findViewById(android.R.id.content), "SMS sent", Snackbar.LENGTH_LONG);

                                sb.show();
                            }
                        }
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        } else if (item.getItemId() == R.id.quiz) {
            //Quiz
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout passPhrase =
                    (LinearLayout) inflater.inflate(R.layout.quiz, null);


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Test Yourself!")
                    .setView(passPhrase)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                            //Toast.makeText(MainActivity.this, "You score "+result, Toast.LENGTH_SHORT).show();

                        }
                    });
            // Configure the 'Negative' button for button dialog
            builder.setNegativeButton("Don't Know Lah",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog,int which){

                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action, menu);
        return true;
    }


}
