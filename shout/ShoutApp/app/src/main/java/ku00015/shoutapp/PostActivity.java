package ku00015.shoutapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class PostActivity extends AppCompatActivity {

    String postUser;
    String postID;
    String postTitle;
    String postBody;
    static final String session = "LoginSession";
    private static final String COMMENT_REQUEST = "http://localhost/comment.php";
    private static final String GET_COMMENTS_REQUEST = "http://localhost/getcomments.php";
    ListView commentsList;
    View header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setCustomView(R.layout.action_bar_post);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setElevation(0);
        }
        setContentView(R.layout.activity_post);

        /**
         ** Inject Post details into Fields
         **/
        Intent intent = getIntent();
        postUser = intent.getStringExtra("user");
        postID = intent.getStringExtra("pid");
        postTitle = intent.getStringExtra("title");
        postBody = intent.getStringExtra("body");

        header = View.inflate(this, R.layout.list_header, null);
        commentsList = (ListView) findViewById(R.id.commentsList);
        if(commentsList != null) commentsList.addHeaderView(header);

        TextView userPostText = (TextView) header.findViewById(R.id.postActivityUser);
        TextView titlePostText = (TextView) header.findViewById(R.id.postActivityTitle);
        TextView bodyPostText = (TextView) header.findViewById(R.id.postActivityBody);
        if(userPostText != null && titlePostText != null && bodyPostText != null) {
            userPostText.setText(postUser);
            titlePostText.setText(postTitle);
            bodyPostText.setText(postBody);
        }

        /**
         ** Inflate List View of Comments
         **/
        updateComments();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishAfterTransition();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        finishAfterTransition();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        super.onBackPressed();
    }

    /**
     ** When Send Button to Comment is Pressed.
     **/
    public void onComment(View view) {
        final EditText commentET = (EditText) findViewById(R.id.commentPost);
        if(commentET != null){
            /**
             ** Check if Comment EditText is empty/ spaced.
             **/
            final String comment = commentET.getText().toString();
            if(!comment.trim().equals("") && comment.trim().length() > 0){
                /**
                 ** Check if User is Logged In (Only allowed to Comment if Logged In).
                 **/
                SharedPreferences prefs = getSharedPreferences(session, MODE_PRIVATE);
                if(prefs.contains("uid")){
                    final String userid = prefs.getString("uid", null);
                    /**
                     ** HTTP Request via POST Method with Comment and UID packed into a Hashmap and
                     ** and passed to Request to be dealt with Server-Side with PHP.
                     **/
                    RequestQueue requestQueue = Volley.newRequestQueue(this);
                    StringRequest request = new StringRequest(Request.Method.POST, COMMENT_REQUEST, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try{
                                JSONObject jsonObject = new JSONObject(response);
                                if(jsonObject.getBoolean("success")){
                                    commentET.setText("");
                                    updateComments();
                                }
                            } catch (JSONException e){ e.printStackTrace(); }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();

                            params.put("pid", postID);
                            params.put("uid", userid);
                            params.put("comment", comment);

                            return params;
                        }
                    };
                    requestQueue.add(request);
                }
            }
        }

    }

    public void updateComments(){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, GET_COMMENTS_REQUEST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getBoolean("success")){
                        JSONArray rows = jsonObject.getJSONArray("rows");
                        ArrayList<Comment> comments = new ArrayList<>();
                        for(int i = 0; i < rows.length(); i++){
                            JSONObject temp = rows.getJSONObject(i);
                            comments.add(new Comment(temp.getString("cid"), temp.getString("pid"), temp.getString("uid"),
                                    temp.getString("comment"), temp.getString("user")));
                        }
                        CommentListAdapter adapter = new CommentListAdapter(PostActivity.this, comments);
                        if(commentsList != null) commentsList.setAdapter(adapter);
                    }
                } catch(JSONException e){ e.printStackTrace(); }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("pid", postID);
                return params;
            }
        };
        queue.add(request);
    }
}
