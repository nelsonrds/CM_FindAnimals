package com.example.helder.client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.helder.client.Adapter.MyAnimalListAdapter;
import com.example.helder.client.DataBase.Animal;
import com.example.helder.client.DataBase.Location;
import com.example.helder.client.WebServices.Singleton;
import com.google.firebase.auth.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.accounts.AccountManager.KEY_PASSWORD;

public class LoginActivity extends AppCompatActivity {
    private String userID;

    EditText editUsername;
    EditText editPassword;

    public static final String KEY_USERNAME = "user";
    public static final String KEY_PASSWORD = "password";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userID = "Null";

        editUsername = (EditText)findViewById(R.id.edit_username);
        editPassword = (EditText)findViewById(R.id.edit_password);
    }

    public void Enter(View v){
        Toast.makeText(this, "Funcou!", Toast.LENGTH_SHORT).show();

        if(true){
            verifyLogin();
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra(Utils.param_Userid, userID);
            startActivity(i);
        }else{
            Toast.makeText(this, "erro!", Toast.LENGTH_LONG).show();
        }
    }


    private void verifyLogin(){
        final String url = "http://eurogather.net:3000/api/loginCheck";
        final String user = editUsername.getText().toString().trim();
        //final String password = editPassword.getText().toString().trim();
        final String password = "b74e86682b5e0b2a3b53a8816cdfe217b74fccc1";

            Map<String,String> params = new HashMap<String, String>();
            params.put(KEY_USERNAME, user);
            params.put(KEY_PASSWORD, password);


        JsonObjectRequest getRequest = new JsonObjectRequest(url, new JSONObject(params) ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // display response
                try{
                    //Toast.makeText(LoginActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                    JSONObject jobj= response.getJSONObject("user");

                    final String s1 = jobj.getString("_id");

                    userID = s1;

                }catch(Exception ex){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, ""+error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        Singleton.getInstance(this).addToRequestQeueu(getRequest);

    }

}
