package ku00015.shoutapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class AddActivity extends AppCompatActivity {

    private static final String ADD_REQUEST = "http://localhost/add.php";
    static final String session = "LoginSession";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
            Change Action bar before the activity layout is inflated:
                        Hide Activity Title
                        Show Custom View Enabled
                        Custom View Updated in Add_Activity_Actionbar Layout
         */
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setCustomView(R.layout.add_activity_actionbar);
            actionBar.setDisplayShowCustomEnabled(true);
        }
        setContentView(R.layout.activity_add);
    }

    @Override
    public void onBackPressed() {
        /*
            When Back Button is pressed,
         */
        finishAfterTransition();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        super.onBackPressed();
    }

    /******************************************************
     *                   Action Bar Menu
     ******************************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishAfterTransition();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                return true;
            case R.id.action_submit:
                onSubmitPost();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /***************************************************************
     *  Validate Data, If Data is Valid; Submit to Database.
     ***************************************************************/
    public void onSubmitPost() {
        EditText titleInput = (EditText) findViewById(R.id.postTitle);
        EditText bodyInput = (EditText) findViewById(R.id.postBody);
        SharedPreferences sharedPreferences = getSharedPreferences(session, MODE_PRIVATE);
        CoordinatorLayout relativeLayout = (CoordinatorLayout) findViewById(R.id.addActivityRelative);

        /*
         * If views aren't null.
         */
        if((titleInput != null) && (bodyInput != null) && (relativeLayout != null)){
            final String uid = sharedPreferences.getString("uid", null);
            final String title = titleInput.getText().toString();
            final String body = bodyInput.getText().toString();

            /*
             * Set up Snackbar for Error Messages.
             */
            final Snackbar snackbar = Snackbar.make(relativeLayout, "Enter a Valid Title", Snackbar.LENGTH_INDEFINITE);
            TextView textView = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            /*
             * If title isn't empty.
             */
            if(titleInput.getText().toString().trim().length() > 0){
                /*
                 * Set up HTTP Request Queue so that Requests and Responses are asynchronous.
                 */
                RequestQueue queue = Volley.newRequestQueue(this);
                /*
                 * Make a Request to be added to Queue.
                 */
                StringRequest postRequest = new StringRequest(Request.Method.POST, ADD_REQUEST, new Response.Listener<String>() {

                    /*
                     * When Server-Side Script/ PHP has finished; finish the activity/ kill AddActivity Child.
                     */
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getBoolean("success")){
                                Intent returnIntent = new Intent();
                                setResult(RESULT_OK, returnIntent);
                                finishAfterTransition();
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                            }
                        } catch(JSONException e){ e.printStackTrace(); }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        snackbar.setText("Connection Error - Please Check your Permissions");
                        snackbar.show();
                    }
                }){
                    /*
                     * Packing Post info into Mapped Params to be Sent to Server.
                     */
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("uid", uid);
                        params.put("title", title);
                        params.put("body", body);
                        return params;
                    }
                };
                queue.add(postRequest);
            } else{
                snackbar.setText("Enter a Valid Title");
                snackbar.show();
            }
        }
    }
}
