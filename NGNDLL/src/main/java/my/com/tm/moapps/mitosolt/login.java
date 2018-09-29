package my.com.tm.moapps.mitosolt;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class login extends AppCompatActivity  {
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    public static String token = "";
    public static login loginactivityinstance;
    String uuid;
    private FirebaseAuth mAuth;

    //for loading bar progress
    AlertDialog.Builder alertDialogprogress;
    AlertDialog alertprogress;
    String username;
    String basketuser;
    String loginuser;
    String passworduser;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
            // Do something for lollipop and above versions
            askpermission();

          //  new rememberuser().execute(Config.APP_SERVER_URL6, uuid);


        } else{


            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_PHONE_STATE)) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_PHONE_STATE},
                            29);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }


        }

        loginactivityinstance = this;


        //get user previous login

//get username and basket and group from uuid using get user class





    }

    //function to request multiple permission

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    private void askpermission() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION))
            permissionsNeeded.add("GPS");
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("MY LOCATION");
        if (!addPermission(permissionsList,android.Manifest.permission.CAMERA))
            permissionsNeeded.add("Use Camera");
        if (!addPermission(permissionsList, Manifest.permission.GET_ACCOUNTS))
            permissionsNeeded.add("Read Contacts");
        if (!addPermission(permissionsList, Manifest.permission.READ_PHONE_STATE))
            permissionsNeeded.add("PHONE STATE");


        if (!addPermission(permissionsList, android.Manifest.permission.RECORD_AUDIO))
            permissionsNeeded.add("RECORD AUDIO");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("SAVE PHOTO");
        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("SAVE PHOTO");
        if (!addPermission(permissionsList, Manifest.permission.PROCESS_OUTGOING_CALLS))
            permissionsNeeded.add("Save Number");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
                return;
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);


            return;
        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
            {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                        perms.put(Manifest.permission.GET_ACCOUNTS, PackageManager.PERMISSION_GRANTED);
                        perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                        perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                         perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                        perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                        perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                         perms.put(Manifest.permission.RECORD_AUDIO, PackageManager.PERMISSION_GRANTED);
                        perms.put(Manifest.permission.PROCESS_OUTGOING_CALLS, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.PROCESS_OUTGOING_CALLS) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted
                    // insertDummyContact();
                    TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
                    uuid = telephonyManager.getDeviceId();


                } else {
                    // Permission Denied
                    Toast.makeText(login.this, "Pls Allow Permission To access full Feature", Toast.LENGTH_SHORT)
                            .show();

                    Button loginbutton = (Button)findViewById(R.id.btnlogin);
                    loginbutton.setVisibility(View.INVISIBLE);
                }
            }
            break;

            case 29:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
                    uuid = telephonyManager.getDeviceId();
                    Toast.makeText(login.this, uuid,
                            Toast.LENGTH_SHORT).show();


                }
                return;



            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(login.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    public static login getInstance() {

        return loginactivityinstance;
    }

   public void loginaction(View view){

       progressdialogshow();

       EditText username = (EditText)findViewById(R.id.usernameTv);
       EditText password = (EditText)findViewById(R.id.passwordTv);


       String usernamestr = String.valueOf(username.getText());
       String passwordstr = String.valueOf(password.getText());

       getfirebasetoken tokenidw = new getfirebasetoken();

       tokenidw.execute(Config.APP_SERVER_URL24, uuid,usernamestr,passwordstr);

    }




    void returnloginresult(String token){


        TextView displayresult = (TextView)findViewById(R.id.loginstatusdisplaytv);
        displayresult.setText(token);
        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithCustomToken(token)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TOKEN FIREBASE", "signInWithCustomToken:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TOKEN FIREBASE", "signInWithCustomToken:failure", task.getException());
                            Toast.makeText(login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);

                            progressbarhide();
                        }
                    }
                });


    }



    void updateUI(FirebaseUser user){

        if(user!=null) {

            TextView displayresult = (TextView) findViewById(R.id.loginstatusdisplaytv);
            displayresult.setText(user.getUid());



            displayresult.setText("Sign In Success");

            progressbarhide();

            //go to register building activity

            Intent i = new Intent(getApplicationContext(), MyActivity.class);
            startActivity(i);

            this.finish();
        }
        if(user == null){

            this.finish();
        }

    }




    public void progressdialogshow(){


        alertDialogprogress = new AlertDialog.Builder(login.this);

        alertprogress = alertDialogprogress.create();

        alertprogress.setTitle("Loading");

        LayoutInflater inflater = getLayoutInflater();

        // inflate the custom popup layout
        final View convertView = inflater.inflate(R.layout.progressdialog, null);
        // find the ListView in the popup layout
        final ProgressBar progressBar = (ProgressBar)convertView.findViewById(R.id.progressBar4);

        // setSimpleList(listView, comment);

        alertprogress.setView(convertView);
        alertprogress.setCanceledOnTouchOutside(false);

        if(alertprogress == null) {
            alertprogress.show();
        }

    }

    public void progressbarhide(){

        alertprogress.dismiss();

    }


    public void setuser(String login,String password){

        //set the username and baskt of the user....this function was call from get user post execute class

       if(login != null || login != "") {
           loginuser = login;
           passworduser = password;


           EditText usernameet = (EditText) findViewById(R.id.usernameTv);
           EditText passwordet = (EditText) findViewById(R.id.passwordTv);

           usernameet.setText(loginuser);
           passwordet.setText(passworduser);


           progressdialogshow();

           EditText usernameetr = (EditText)findViewById(R.id.usernameTv);
           EditText passwordetr = (EditText)findViewById(R.id.passwordTv);


           String usernamestr = String.valueOf(usernameetr.getText());
           String passwordstr = String.valueOf(passwordetr.getText());

           getfirebasetoken tokenidw = new getfirebasetoken();

           tokenidw.execute(Config.APP_SERVER_URL24, uuid,usernamestr,passwordstr);


       }

    }




    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        progressdialogshow();
        if(currentUser!=null){
        updateUI(currentUser);
        }
    }


}
