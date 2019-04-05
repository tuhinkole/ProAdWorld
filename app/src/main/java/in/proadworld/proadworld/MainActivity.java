package in.proadworld.proadworld;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.ImageView;
import android.widget.TextView;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button submit;
    EditText editText;
    String url;
    TextView textView,count_et;
    ProgressDialog progressDialog;
    String web_url = "https://proadworld.in/api_new/web_url.php";
    ArrayList<String> Array_url = new ArrayList<String>();
    ArrayList<String> Array_desc = new ArrayList<String>();
    int hit = 1;
    public static final String MyPREFERENCES = "MyPrefs" ;
    String get_count_url = "https://proadworld.in/api_new/getcount.php";
    String post_count_url = "https://proadworld.in/api_new/updatecount.php";
    String post_getboost = "https://proadworld.in/api_new/boost.php";
    String partyid;
    String addcount;
    int count_info;
    ImageView logout;
    boolean doubleBackToExitPressedOnce = false;
    String date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        progressDialog = new ProgressDialog(MainActivity.this);
        MobileAds.initialize(this, getString(R.string.admob_banner_id));



        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        SharedPreferences preferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        partyid = preferences.getString("partyid", "");
//        Log.e("partyid","partyid"+partyid);

        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

//        Log.e("date",date+"msg");

        textView = (TextView)findViewById(R.id.text);

        submit = (Button)findViewById(R.id.submit);
        editText = (EditText) findViewById(R.id.texturl);
        count_et = (TextView) findViewById(R.id.count_et);
        logout = (ImageView)findViewById(R.id.logout);
        logout.setOnClickListener(this);
        editText.setEnabled(false);
        count_et.setEnabled(false);

        if(partyid!=null){


            progressDialog.setMessage("Please Wait");
            progressDialog.show();
            getboost(partyid);
            getCount_url(partyid);
            Runnable r = new Runnable() {
                @Override
                public void run() {

//                    prog.dismiss();


                }
            };

            Handler h = new Handler();
            h.postDelayed(r, 2000);

        }





        submit.setOnClickListener(this);




    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit:

                if(url.toString()!=null || url.toString()!=""){

                    Intent intent = new Intent(MainActivity.this,SecondActivity.class);
//                    Log.d("URL",url);
                    intent.putExtra("url",url);
                    startActivity(intent);

                    hit=Integer.parseInt(addcount);
                    hit++;
//                    Log.e("HIT","HIT"+hit);
                    post_count_url(partyid,hit);
                    if(hit  < Array_url.size())
                    {
                        editText.setText(Array_desc.get(hit));
                        url=Array_url.get(hit);

                    }


                }

                break;

            case R.id.logout:
                Intent intent = new Intent(MainActivity.this,LoginPage.class);
                startActivity(intent);
                break;
        }
    }


    private void geturl(final int i) {
//        if (progressDialog!=null){
//            progressDialog.setMessage("Please wait");
//            progressDialog.setCancelable(false);
//            progressDialog.show();
//        }
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.GET,web_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.e("Response",response);
                        try{
                            JSONObject jsonObject  = new JSONObject(response);

                            {



                                JSONArray FEATURES = jsonObject.getJSONArray("data");
                                for (int j = 0; j < FEATURES.length(); j++) {
                                    JSONObject json_obj111 = FEATURES.getJSONObject(j);
                                    String link = json_obj111.getString("link");
                                    String desc = json_obj111.getString("desc1");
                                    Array_url.add(link);
                                    Array_desc.add(desc);



                                }
                                editText.setText(Array_desc.get(i-hit));
                                url=Array_url.get(i-hit);
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
//                Log.e("Error","volley error"+error.getMessage());
                if (progressDialog!=null){
                    progressDialog.dismiss();
                }

            }
        }) ;
        requestQueue.add(stringRequest);
    }


    public void getboost(final String partyid){

//        Log.e("partyidpartyid","partyidpartyid"+partyid);
//        if (progressDialog!=null){
//            progressDialog.setMessage("Please wait");
//            progressDialog.setCancelable(false);
//            progressDialog.show();
//        }
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST,post_getboost ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.e("boost info",response);
                        try{
                            final JSONObject jsonObject=new JSONObject(response);
                            String statusCode=jsonObject.getString("status");
                            String msg = jsonObject.getString("msg");
//                            Log.e("Msg code",msg);
//                            Log.e("Login Details ",statusCode + "--"+ msg+"--");
                            if (Integer.parseInt(statusCode)==200){
                                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                count_info = jsonObject1.getInt("boost");
//                                Log.d("count_info","info"+count_info);


                            }else {
                                AlertDialog.Builder builder;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(MainActivity.this);
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

                Toast.makeText(getApplicationContext(),"Network Error Please Try Again",Toast.LENGTH_SHORT).show();


            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("partyid", partyid);

                return params;
            }
        };
        requestQueue.add(stringRequest);



    }


    public void getCount_url(final String id){
//        Log.e("IIDD","IDD"+id);
//        if (progressDialog!=null){
//            progressDialog.setMessage("Please wait");
//            progressDialog.setCancelable(false);
//            progressDialog.show();
//        }
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, get_count_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.e("Count_info",response);
                        try{
                            final JSONObject jsonObject=new JSONObject(response);
                            String statusCode=jsonObject.getString("status");
                            String msg = jsonObject.getString("msg");
//                            Log.e("Msg code",msg);
//                            Log.e("Login Details ",statusCode + "--"+ msg+"--");
                            if (Integer.parseInt(statusCode)==200){
                                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                addcount = jsonObject1.getString("addcount");
//                                Log.e("Integer.parseInt",Integer.parseInt(addcount)-1+"hello");

                                String a = String.valueOf(Integer.parseInt(addcount)-1);


                                count_et.setText(a);


                                if(count_info==1){
                                    if(Integer.parseInt(addcount) >= 41){
                                        submit.setVisibility(View.GONE);
                                        textView.setVisibility(View.VISIBLE);
                                        editText.setVisibility(View.GONE);

                                    }

                                    if(Integer.parseInt(addcount) < 41){
                                        if(Array_url.size()==0){
                                            geturl(Integer.parseInt(addcount));

                                        }

                                    }


                                }else if(count_info==0){

                                    if(Integer.parseInt(addcount) >= 21){
                                        submit.setVisibility(View.GONE);
                                        textView.setVisibility(View.VISIBLE);
                                        editText.setVisibility(View.GONE);

                                    }

                                    if(Integer.parseInt(addcount) < 21){
                                        if(Array_url.size()==0){
                                            geturl(Integer.parseInt(addcount));

                                        }

                                    }
                                }






                            }else {
                                AlertDialog.Builder builder;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(MainActivity.this);
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

                Toast.makeText(getApplicationContext(),"Network Error Please Try Again",Toast.LENGTH_SHORT).show();
//                if(isNetworkAvailable()){
//                    AlertDialog.Builder builder;
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
//                    } else {
//                        builder = new AlertDialog.Builder(MainActivity.this);
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
//                if (progressDialog!=null){
//                    progressDialog.dismiss();
//                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("partyid", id);

                return params;
            }
        };
        requestQueue.add(stringRequest);
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


    public void post_count_url(final  String partyid, final  int hit){
//        Log.e("IIDD","IDD"+partyid);
//        if (progressDialog!=null){
//            progressDialog.setMessage("Please wait");
//            progressDialog.setCancelable(false);
//            progressDialog.show();
//        }
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, post_count_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.e("Count_info",response);
                        try{
                            final JSONObject jsonObject=new JSONObject(response);
                            String statusCode=jsonObject.getString("status");
                            String msg = jsonObject.getString("msg");
//                            Log.e("Msg code",msg);
//                            Log.e("Login Details ",statusCode + "--"+ msg+"--");
                            if (Integer.parseInt(statusCode)==200){
                                getCount_url(partyid);

                            }else {
                                AlertDialog.Builder builder;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(MainActivity.this);
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
                Toast.makeText(getApplicationContext(),"Network Error Please Try Again",Toast.LENGTH_SHORT).show();

//                if(isNetworkAvailable()){
//                    AlertDialog.Builder builder;
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
//                    } else {
//                        builder = new AlertDialog.Builder(MainActivity.this);
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
//                if (progressDialog!=null){
//                    progressDialog.dismiss();
//                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("partyid", partyid);
                params.put("addcount", String.valueOf(hit));
                params.put("date",date);

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);

    }
}
