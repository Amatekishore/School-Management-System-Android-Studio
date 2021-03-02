package com.example.muhammadobaid.sims;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity   {
    public static final String SchoolNameArray = "school_name";
    public static final String SchoolName  = "school_name";
    public static final String SchoolLogo = "school_logo";
    public static final String JSON_ARRAY = "result";
    private static final String URL = "http://192.168.10.4/sims/schools";
    public   static final String Login_url="http://192.168.10.4/sims/login";
    private JSONArray result;
    Spinner spinner;
    String login_name,login_password;
    ImageView imageView;
    private ArrayList<String> arrayList;

     Button login;
      EditText username,password;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
			.build();
        ImageLoader.getInstance().init(config);
        initializations();
        spinner = findViewById(R.id.spinner);
        arrayList = new ArrayList<>();
        getdata();
       spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(LoginActivity.this, ""+id, Toast.LENGTH_SHORT).show();
                String url=getSchoolLogo(position);
                ImageLoader imageLoader=ImageLoader.getInstance();
                imageLoader.displayImage(url,imageView);
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    //Login with String request...Username and password attached in Hashmap

    public void login(View view){
        login_name=username.getText().toString();
        login_password=password.getText().toString();
        StringRequest stringRequest=new StringRequest(Request.Method.POST,Login_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("1")) {
                    Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent (LoginActivity.this,Profile.class));
                }
                else{
                    Toast.makeText(LoginActivity.this, "Username or Password incorrect", Toast.LENGTH_SHORT).show();
                     username.setError("Username");
                     password.setError("Password");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        })
        {
            @Override
            protected Map<String, String> getParams ()  {
            Map<String, String> params = new HashMap<>();
            params.put("user_name", login_name);
            params.put("user_pass", login_password);
            return params;
        }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //login code end



    private void getdata() {
        StringRequest stringRequest = new StringRequest(URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j ;
                        try {
                            j = new JSONObject(response);
                            result = j.getJSONArray(JSON_ARRAY);
                            empdetails(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void empdetails(JSONArray j) {
        for (int i = 0; i < j.length(); i++) {
            try {
                JSONObject json = j.getJSONObject(i);
                arrayList.add(json.getString(SchoolNameArray));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // arrayList.add(0,"Select Employee");
        spinner.setAdapter(new ArrayAdapter<>(LoginActivity.this, android.R.layout.simple_spinner_dropdown_item, arrayList));
    }
    //Method to get student name of a particular position
    private String getStudentName(int position){
        String name="";
        try {
            //Getting object of given index
            JSONObject json = result.getJSONObject(position);
            //Fetching name from that object
            name = json.getString(SchoolName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return name;
    }
    //Doing the same with this method as we did with getName()
    private String getSchoolLogo(int position){
        String logo="";
        try {
            JSONObject json = result.getJSONObject(position);
            logo = json.getString(SchoolLogo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return logo;

    }

        public void initializations () {
            login=findViewById(R.id.btn_login);
            imageView=findViewById(R.id.imageView2);
            login = findViewById(R.id.btn_login);
            username = findViewById(R.id.tv_username);
            password = findViewById(R.id.tv_password);

        }

}

