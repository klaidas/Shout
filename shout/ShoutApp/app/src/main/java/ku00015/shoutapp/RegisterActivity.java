package ku00015.shoutapp;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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

public class RegisterActivity extends AppCompatActivity {

    EditText user;
    EditText email;
    EditText pass;
    EditText passconf;
    private static final String REGISTER_REQUEST = "http://localhost/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        user = (EditText) findViewById(R.id.usernameRegister);
        email = (EditText) findViewById(R.id.emailRegister);
        pass = (EditText) findViewById(R.id.passwordRegister);
        passconf = (EditText) findViewById(R.id.passConfirmRegister);
    }

    public void onCloseClick(View view) {
        finishAfterTransition();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        super.onNavigateUp();
    }

    @Override
    public void onBackPressed() {
        finishAfterTransition();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        super.onBackPressed();
    }

    public void onRegisterClicked(View view) {

        //EDIT TEXTS
        final String username = user.getText().toString();
        final String emailAddress = email.getText().toString();
        final String password = pass.getText().toString();
        final String passwordConfirm = passconf.getText().toString();

        //OTHER VIEWS
        CoordinatorLayout rootReg = (CoordinatorLayout) findViewById(R.id.registerRoot);

        if (rootReg != null) {

            final Snackbar snackbar = Snackbar.make(rootReg, "Error", Snackbar.LENGTH_LONG);
            int snackbarTextId = android.support.design.R.id.snackbar_text;
            TextView textView = (TextView) snackbar.getView().findViewById(snackbarTextId);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            if (username.trim().length() > 0 && emailAddress.trim().length() > 0
                    && password.trim().length() > 0 && passwordConfirm.trim().length() > 0) {
                if (password.equals(passwordConfirm)) {
                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
                        RequestQueue queue = Volley.newRequestQueue(this);
                        StringRequest postRequest = new StringRequest(Request.Method.POST, REGISTER_REQUEST, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if(jsonObject.getBoolean("success")){
                                        Intent backToUser = new Intent();
                                        setResult(RESULT_OK, backToUser);
                                        finishAfterTransition();
                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                                    } else{
                                        snackbar.setText("Username already exists");
                                        snackbar.show();
                                    }
                                } catch (JSONException e){}

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                snackbar.setText("Connection Error, Please Check Permissions");
                                snackbar.show();
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String>  params = new HashMap<>();
                                params.put("user", username);
                                params.put("pass", password);
                                params.put("email", emailAddress);
                                return params;
                            }
                        };
                        queue.add(postRequest);

                    } else{
                        snackbar.setText("Email Address is invalid");
                        snackbar.show();
                    }
                } else{
                    snackbar.setText("Password do not Match");
                    snackbar.show();
                }
            } else{
                snackbar.setText("One or More fields are Empty");
                snackbar.show();
            }
        }
    }
}
