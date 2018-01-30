package ku00015.shoutapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserActivity extends AppCompatActivity {

    private static final String LOGIN_REQUEST = "http://localhost/login.php";
    static final String session = "LoginSession";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
    }

    public void onCloseClick(View view) {
        super.onNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onLogIn(View view) {
        EditText username = (EditText) findViewById(R.id.usernameLogin);
        EditText password = (EditText) findViewById(R.id.passwordLogin);
        CoordinatorLayout activityRoot = (CoordinatorLayout) findViewById(R.id.userActivityRoot);

        if(activityRoot != null && username != null && password != null){
            final String user = username.getText().toString();
            final String pass = password.getText().toString();

            final Snackbar snackbar = Snackbar.make(activityRoot, "Invalid Username and/or Password", Snackbar.LENGTH_LONG);
            int snackbarTextId = android.support.design.R.id.snackbar_text;
            TextView textView = (TextView) snackbar.getView().findViewById(snackbarTextId);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            if(user.trim().length() > 0 && pass.trim().length() > 0) {
                RequestQueue queue = Volley.newRequestQueue(this);
                StringRequest postRequest = new StringRequest(Request.Method.POST, LOGIN_REQUEST, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {
                                String dbUID = jsonObject.getString("uid");
                                String dbUSER = jsonObject.getString("user");
                                String dbEmail = jsonObject.getString("email");

                                SharedPreferences sharedPreferences = getSharedPreferences(session, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                editor.putString("uid", dbUID);
                                editor.putString("username", dbUSER);
                                editor.putString("email", dbEmail);
                                editor.commit();

                                finish();
                            } else {
                                snackbar.setText("Invalid Username and/or Password");
                                snackbar.show();
                            }
                        } catch (JSONException e) { e.printStackTrace(); }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        snackbar.setText("Connection Failed, Please Check your Permissions");
                        snackbar.show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("user", user);
                        params.put("pass", pass);
                        return params;
                    }
                };
                queue.add(postRequest);
            } else{
                snackbar.setText("Invalid Input");
                snackbar.show();
            }
        }
    }

    public void onNeedToRegister(View view) {
        Intent openRegisterActivity = new Intent(this, RegisterActivity.class);
        startActivityForResult(openRegisterActivity, 420);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    /**********************************************************************************
                        Shows SnackBar after Register Activity Ends
     **********************************************************************************/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 420){
            if(resultCode == RESULT_OK){
                CoordinatorLayout userCoord = (CoordinatorLayout) findViewById(R.id.userActivityRoot);
                if(userCoord != null){
                    Snackbar snackbar = Snackbar.make(userCoord, "Successfully Registered", Snackbar.LENGTH_LONG);
                    int snackbarTextId = android.support.design.R.id.snackbar_text;
                    TextView textView = (TextView) snackbar.getView().findViewById(snackbarTextId);
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    snackbar.show();
                }
            }
        }
    }
}
