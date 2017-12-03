package ku00015.shoutapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String GET_REQUEST = "http://192.168.0.11/androidlogreg/get.php";
    private static final String GET_LIKED_REQUEST = "http://192.168.0.11/androidlogreg/liked.php";
    static final String session = "LoginSession";
    ListView lv;
    CoordinatorLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         **   Customizing Action Bar - Before Activity Layout is Inflated.
         **/
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setCustomView(R.layout.action_bar_custom);
            actionBar.setDisplayShowCustomEnabled(true);
        }
        setContentView(R.layout.activity_main);

        relativeLayout = (CoordinatorLayout) findViewById(R.id.mainActivityRelative);

        updateList();
        /**
         **   Swipe Vertically to Refresh ListView - Calls updateList Method to Make Changes to ListView.
         **/
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipeRefreshLayout.setEnabled(true);
                    swipeRefreshLayout.setRefreshing(false);
                    updateList();
                }
            });
        }
    }

    /******************************************************
     *                   Action Bar Menu
     ******************************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent openLoginActivity = new Intent(this, UserActivity.class);
        SharedPreferences sharedPreferences = getSharedPreferences(session, MODE_PRIVATE);
        switch (item.getItemId()) {
            case R.id.action_add:
                if(sharedPreferences.contains("uid")){
                    Intent openAddActivity = new Intent(this, AddActivity.class);
                    openAddActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivityForResult(openAddActivity, 1337);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                } else{
                    startActivity(openLoginActivity);
                }
                return true;
            case R.id.action_user:
                if(sharedPreferences.contains("uid")){
                    Intent openProfile = new Intent(this, ProfileActivity.class);
                    startActivity(openProfile);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                } else {
                    startActivity(openLoginActivity);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /******************************************************
     *                   List View Methods
     ******************************************************/
    private void updateList() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, GET_REQUEST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    /**
                     ** Get the HTTP Response and Check if Script finished; which should return success=true
                     **/
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getBoolean("success")){
                        JSONArray rows = jsonObject.getJSONArray("rows");
                        /**
                         ** Fill ArrayList of Post Objects from Encapsulated JSONObject which contains JSONArray of Post Records.
                         **/
                        ArrayList<Post> postData = new ArrayList<>();
                        for(int i = 0; i < rows.length(); i++){
                            JSONObject rowtemp = rows.getJSONObject(i);
                            Post temp = new Post(rowtemp.getString("pid"), rowtemp.getString("uid"), rowtemp.getString("title"),
                                    rowtemp.getString("body"), rowtemp.getString("user"), rowtemp.getString("likes"));
                            postData.add(temp);
                        }
                        /**
                         ** Sort ArrayList by order of highest likes to lowest likes and inflate ListView with Post Data using Adapter.
                         **/
                        postData = sortByLikes(postData);
                        lv = (ListView) findViewById(R.id.lv);
                        PostListAdapter adapter = new PostListAdapter(MainActivity.this, postData);
                        if(lv != null) lv.setAdapter(adapter);
                        /**
                         ** On Item Click
                         **/
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                String pid = ((TextView) view.findViewById(R.id.lvPID)).getText().toString();
                                String uid = ((TextView) view.findViewById(R.id.lvUID)).getText().toString();
                                String title = ((TextView) view.findViewById(R.id.lvTitle)).getText().toString();
                                String body = ((TextView) view.findViewById(R.id.lvBody)).getText().toString();
                                String user = ((TextView) view.findViewById(R.id.lvUsername)).getText().toString();

                                Intent openPostActivity = new Intent(MainActivity.this, PostActivity.class);
                                openPostActivity.putExtra("pid", pid);
                                openPostActivity.putExtra("uid", uid);
                                openPostActivity.putExtra("title", title);
                                openPostActivity.putExtra("body", body);
                                openPostActivity.putExtra("user", user);

                                startActivity(openPostActivity);
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

                            }
                        });
                    }
                } catch(JSONException e){ e.printStackTrace(); }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) { }
        });
        queue.add(postRequest);
    }


    public void onLiked(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences(session, MODE_PRIVATE);
        if(sharedPreferences.contains("uid")) {

            final String uid = sharedPreferences.getString("uid", null);
            final TextView likeCounter = (TextView) view;

            View parent = (View) view.getParent();
            TextView pidTV = (TextView) parent.findViewById(R.id.lvPID);
            final String pid = pidTV.getText().toString();

            /**
             ** Volley HTTP Request to Server-Side PHP script - Liked.php
             **/
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_LIKED_REQUEST, new Response.Listener<String>() {
                /**
                 ** On Successful Connection; If Script fully runs and finishes - should return a response.
                 **/
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        if(jsonObject.getBoolean("success")){
                            String stringCount = likeCounter.getText().toString();
                            if(jsonObject.getBoolean("liked")){
                                /**
                                 ** Increment Like Counter.
                                 **/
                                int changedCounter = Integer.parseInt(stringCount) + 1;
                                likeCounter.setText(String.valueOf(changedCounter));

                            } else{
                                /**
                                 ** Decrement Like Counter.
                                 **/
                                int changedCounter = Integer.parseInt(stringCount) - 1;
                                likeCounter.setText(String.valueOf(changedCounter));
                            }
                        }

                    } catch (JSONException e){ e.printStackTrace(); }
                }
            }, new Response.ErrorListener() {
                /**
                 ** On Connection to Server Error.
                 **/
                @Override
                public void onErrorResponse(VolleyError error) { }
            }) {
                /**
                 ** Pack Params into Request and Map it for PHP script to handle.
                 **/
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("pid", pid);
                    params.put("uid", uid);
                    return params;
                }
            };
            requestQueue.add(stringRequest);
        } else{
            /**
             ** Snackbar Pop-up to inform user to Log in.
             **/
            Snackbar snackbar = Snackbar.make(relativeLayout, "You must be Logged In", Snackbar.LENGTH_SHORT);
            int snackbarTextId = android.support.design.R.id.snackbar_text;
            TextView textView = (TextView) snackbar.getView().findViewById(snackbarTextId);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            snackbar.show();
            updateList();
        }
    }

    private ArrayList<Post> sortByLikes(ArrayList<Post> postData){
        /**
         ** Sorts all retrieved posts by like counter - More liked posts come first.
         **/
        for(int i = 0; i < (postData.size() - 1); i++){
            for(int j = i + 1; j < postData.size(); j++) {
                if(Integer.parseInt(postData.get(j).getLikes()) > Integer.parseInt(postData.get(i).getLikes())){
                    Post temp = postData.get(i);
                    postData.set(i, postData.get(j));
                    postData.set(j, temp);
                }
            }
        }
        return postData;
    }


    /******************************************************
     *          Post Successful - Launch Snackbar
     ******************************************************/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1337){
            if(resultCode == RESULT_OK){
                if(relativeLayout != null){
                    Snackbar snackbar = Snackbar.make(relativeLayout, "Post Succesfully Submitted", Snackbar.LENGTH_LONG);
                    int snackbarTextId = android.support.design.R.id.snackbar_text;
                    TextView textView = (TextView) snackbar.getView().findViewById(snackbarTextId);
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    snackbar.show();
                    updateList();
                }
            }
        }
    }
}