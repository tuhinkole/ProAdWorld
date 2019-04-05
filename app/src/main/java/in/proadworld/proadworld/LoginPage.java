package in.proadworld.proadworld;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginPage extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
    public static final String MyPREFERENCES = "MyPrefs";
    private static final int REQUEST_SIGNUP = 0;
    String login_url = "https://proadworld.in/api_new/login.php";
    String joining = "https://proadworld.in/api_new/joining.php";
    EditText _emailText;
    EditText _passwordText;
    Button _loginButton;
//    ProgressDialog prog;
    ProgressDialog progressDialog;
    SharedPreferences sharedpreferences;
    private static final int REQUEST = 112;

    private static final String PREFS_NAME = "preferences";
    private static final String PREF_UNAME = "Username";
    private static final String PREF_PASSWORD = "Password";

    private final String DefaultUnameValue = "";
    private String UnameValue;

    private final String DefaultPasswordValue = "";
    private String PasswordValue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        progressDialog = new ProgressDialog(LoginPage.this);
        MobileAds.initialize(this, getString(R.string.admob_banner_id));



        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        _loginButton = (Button) findViewById(R.id.btn_login);
        _emailText = (EditText) findViewById(R.id.input_email);
        _passwordText = (EditText) findViewById(R.id.input_password);
        _loginButton.setOnClickListener(this);





    }

    @Override
    public void onPause() {
        super.onPause();
        savePreferences();

    }

    @Override
    public void onResume() {
        super.onResume();
        loadPreferences();
    }


    private void savePreferences() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        // Edit and commit
        UnameValue = _emailText.getText().toString();
        PasswordValue =  _passwordText.getText().toString();
        System.out.println("onPause save name: " + UnameValue);
        System.out.println("onPause save password: " + PasswordValue);
        editor.putString(PREF_UNAME, UnameValue);
        editor.putString(PREF_PASSWORD, PasswordValue);
        editor.commit();
    }

    private void loadPreferences() {

        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        // Get value
        UnameValue = settings.getString(PREF_UNAME, DefaultUnameValue);
        PasswordValue = settings.getString(PREF_PASSWORD, DefaultPasswordValue);
        _emailText.setText(UnameValue);
        _passwordText.setText(PasswordValue);
        System.out.println("onResume load name: " + UnameValue);
        System.out.println("onResume load password: " + PasswordValue);
    }





    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                login();
                break;
        }
    }

    public void login() {
//        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

//        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        UnameValue = _emailText.getText().toString();
        PasswordValue =  _passwordText.getText().toString();

//        final String email = _emailText.getText().toString();
//        final String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.

        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
//                        onLoginSuccess(email,password);
                        onLoginSuccess(UnameValue,PasswordValue);
                        // onLoginFailed();
//                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess(String email, String password) {


        _loginButton.setEnabled(true);
//        if(isNetworkAvailable()) {

            progressDialog.setMessage("Please Wait");
            progressDialog.show();
            dologin(email,password);
            Runnable r = new Runnable() {
                @Override
                public void run() {

//                    prog.dismiss();


                }
            };

            Handler h = new Handler();
            h.postDelayed(r, 2000);
        }

//    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

//        String email = _emailText.getText().toString();
//        String password = _passwordText.getText().toString();

        UnameValue = _emailText.getText().toString();
        PasswordValue =  _passwordText.getText().toString();

//        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            _emailText.setError("enter a valid email address");
//            valid = false;
//        } else {
//            _emailText.setError(null);
//        }

        if (UnameValue.isEmpty()) {
            _emailText.setError("enter a valid  username");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (PasswordValue.isEmpty() || PasswordValue.length() < 4 || PasswordValue.length() > 15) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

//    public boolean isNetworkAvailable() {
//        boolean connect=false;
//        ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(this.CONNECTIVITY_SERVICE);
//        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
//        if (netInfo == null){
//            new AlertDialog.Builder(this)
//                    .setTitle("Network Unreachable")
//                    .setMessage("No internet connection on your device." +
//                            " Please connect to the internet.")
////                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
////                        @Override
////                        public void onClick(DialogInterface dialogInterface, int i) {
//////                            finish();
////                            dialogInterface.dismiss();
////                        }
////                    })
//                    .setCancelable(false)
//                    .setNegativeButton("Try again", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            isNetworkAvailable();
//                        }
//                    }).show();
//            connect=false;
//        }else{
//            connect= true;
//        }
//        return connect;
//    }

    public void dologin(final String email, final String password){
//        if (progressDialog!=null){
//            progressDialog.setMessage("Please wait");
//            progressDialog.setCancelable(false);
//            progressDialog.show();
//        }
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.e("Response11",response);
                        try{
                            final JSONObject jsonObject=new JSONObject(response);
                            String statusCode=jsonObject.getString("status");
                            String msg = jsonObject.getString("msg");
//                            Log.e("Msg code",msg);
//                            Log.e("Login Details ",statusCode + "--"+ msg+"--");
                            if (Integer.parseInt(statusCode)==200){

                                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                String partyid = jsonObject1.getString("partyid");
                                String uid = jsonObject1.getString("uid");

//                                Log.e("uid","uid"+partyid+uid);

                                SharedPreferences.Editor editor = sharedpreferences.edit();

                                editor.putString("partyid", partyid);
                                editor.putString("uid", uid);

                                editor.commit();

                                JoiningDetails(partyid);



//                                    Intent intent  = new Intent(getApplicationContext(), MainActivity.class);
//                                    finish();
//                                    startActivity(intent);


//                            finish();

                            }else {
                                AlertDialog.Builder builder;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    builder = new AlertDialog.Builder(LoginPage.this, android.R.style.Theme_Material_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(LoginPage.this);
                                }
                                builder.setTitle("Sorry")
                                        .setMessage(msg)
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                            }
                            if (progressDialog!=null){
                                progressDialog.dismiss();
                            }
                        }catch (Exception e){
                            if (progressDialog!=null){
                                progressDialog.dismiss();
                            }

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.e("Error",error.toString());
                Toast.makeText(getApplicationContext(),"Network Error Please Try Again",Toast.LENGTH_SHORT).show();

//                if(isNetworkAvailable()){
//                    AlertDialog.Builder builder;
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        builder = new AlertDialog.Builder(LoginPage.this, android.R.style.Theme_Material_Dialog_Alert);
//                    } else {
//                        builder = new AlertDialog.Builder(LoginPage.this);
//                    }
//                    builder.setTitle("Sorry")
//                            .setMessage("Please try again")
//                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                }
//                            })
//                            .setIcon(android.R.drawable.ic_dialog_alert)
//                            .show();
//
//                }else {
//
//                }
//                Log.e("Error","volley error"+error.getMessage());
                if (progressDialog!=null){
                    progressDialog.dismiss();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }
        };
        requestQueue.add(stringRequest);

    }

    public void JoiningDetails(final String partyid){




//        if (progressDialog!=null){
//            progressDialog.setMessage("Please wait");
//            progressDialog.setCancelable(false);
//            progressDialog.show(LoginPage.this, null, "Loading");
//        }
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, joining,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.e("Response11",response);
                        try{
                            final JSONObject jsonObject=new JSONObject(response);
                            Log.e("JsonObject",jsonObject+"");
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            String active = jsonObject1.getString("active");
                            Log.e("active",active+"");

                            if (Integer.parseInt(active)==2){
                                Intent intent  = new Intent(getApplicationContext(), MainActivity.class);
                                finish();
                                startActivity(intent);

                            }else {



                                    Toast.makeText(getApplicationContext(),"You can't login, are already a Team Member",Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();



                            }


                        }catch (Exception e){
                            if (progressDialog!=null){
                                progressDialog.dismiss();
                            }

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.e("Error",error.toString());
                Toast.makeText(getApplicationContext(),"Network Error Please Try Again",Toast.LENGTH_SHORT).show();

                if (progressDialog!=null){
                    progressDialog.dismiss();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("partyid", partyid );

                return params;
            }
        };
        requestQueue.add(stringRequest);




    }




}

