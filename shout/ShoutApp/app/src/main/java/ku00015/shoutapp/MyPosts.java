package ku00015.shoutapp;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MyPosts extends Fragment {

    static final String session = "LoginSession";
    private static final String GET_USER_POSTS_REQUEST = "http://192.168.0.11/androidlogreg/getuserposts.php";
    private static final String DELETE_USER_POSTS_REQUEST = "http://192.168.0.11/androidlogreg/deleteuserpost.php";

    ListView myPostsList;
    String uid;

    public MyPosts() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        updateMyPosts();
        View root = inflater.inflate(R.layout.fragment_my_posts, container, false);
        myPostsList = (ListView) root.findViewById(R.id.myPostsList);
        return root;
    }

    private void updateMyPosts(){
        SharedPreferences prefs = this.getContext().getSharedPreferences(session, Context.MODE_PRIVATE);
        uid = prefs.getString("uid", null);

        final RequestQueue queue = Volley.newRequestQueue(this.getContext());
        StringRequest request = new StringRequest(Request.Method.POST, GET_USER_POSTS_REQUEST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getBoolean("success")){
                        JSONArray rows = jsonObject.getJSONArray("rows");
                        ArrayList<Post> posts = new ArrayList<>();
                        for(int i = 0; i < rows.length(); i++){
                            JSONObject temp = rows.getJSONObject(i);
                            posts.add(new Post(temp.getString("pid"), temp.getString("uid"), temp.getString("title"),
                                    temp.getString("body"), "", ""));
                        }
                        MyPostListAdapter myPostListAdapter = new MyPostListAdapter(getActivity(), posts);
                        if(myPostsList != null) myPostsList.setAdapter(myPostListAdapter);
                        myPostsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                final String pid = ((TextView) view.findViewById(R.id.myPostsListPID)).getText().toString();

                                final AlertDialog alertDialog = new AlertDialog.Builder(MyPosts.this.getContext()).create();
                                alertDialog.setMessage("Are you sure you want to delete this Post?");
                                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        alertDialog.dismiss();
                                    }
                                });
                                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        StringRequest deleteRequest = new StringRequest(Request.Method.POST, DELETE_USER_POSTS_REQUEST,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        try {
                                                            JSONObject jsonResponse = new JSONObject(response);
                                                            if(jsonResponse.getBoolean("success")){
                                                                alertDialog.dismiss();
                                                                updateMyPosts();
                                                            }
                                                        } catch(JSONException e){}
                                                    }
                                                }, null){
                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                Map<String, String> params = new HashMap<>();
                                                params.put("pid", pid);
                                                return params;
                                            }
                                        };
                                        queue.add(deleteRequest);
                                    }
                                });
                                alertDialog.show();
                            }
                        });
                    }
                } catch (JSONException e) {}
            }
        }, null){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("uid", uid);
                return params;
            }
        };
        queue.add(request);
    }
}
