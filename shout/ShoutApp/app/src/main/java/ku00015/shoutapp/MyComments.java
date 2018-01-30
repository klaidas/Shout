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

public class MyComments extends Fragment {

    static final String session = "LoginSession";
    private static final String GET_USER_COMMENTS_REQUEST = "http://localhost/getusercomments.php";
    private static final String DELETE_USER_COMMENTS_REQUEST = "http://localhost/deleteusercomment.php";
    ListView lv;
    String uid;

    public MyComments() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        updateMyComments();
        View root = inflater.inflate(R.layout.fragment_my_comments, container, false);
        lv = (ListView) root.findViewById(R.id.myCommentsList);
        return root;
    }

    private void updateMyComments(){
        SharedPreferences sharedPreferences = this.getContext().getSharedPreferences(session, Context.MODE_PRIVATE);
        uid = sharedPreferences.getString("uid", null);

        final RequestQueue queue = Volley.newRequestQueue(this.getContext());
        StringRequest request = new StringRequest(Request.Method.POST, GET_USER_COMMENTS_REQUEST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getBoolean("success")){
                        JSONArray rows = jsonObject.getJSONArray("rows");
                        ArrayList<Comment> comments = new ArrayList<>();
                        for(int i = 0; i < rows.length(); i++){
                            JSONObject row = rows.getJSONObject(i);
                            comments.add(new Comment(row.getString("cid"), row.getString("pid"), row.getString("uid"),
                                    row.getString("comment"), uid));
                        }
                        MyCommentListAdapter myCommentListAdapter = new MyCommentListAdapter(getActivity(), comments);
                        if(lv != null) lv.setAdapter(myCommentListAdapter);
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                final String cid = ((TextView) view.findViewById(R.id.myCommentListCID)).getText().toString();
                                final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                                alertDialog.setMessage("Are you sure you want to delete this Comment?");
                                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        alertDialog.dismiss();
                                    }
                                });
                                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        StringRequest deleteRequest = new StringRequest(Request.Method.POST, DELETE_USER_COMMENTS_REQUEST, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try{
                                                    JSONObject responseObject = new JSONObject(response);
                                                    if(responseObject.getBoolean("success")){
                                                        alertDialog.dismiss();
                                                        updateMyComments();
                                                    }
                                                } catch(JSONException e) {}
                                            }
                                        }, null){
                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                Map<String, String> params = new HashMap<String, String>();
                                                params.put("cid", cid);
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
                } catch(JSONException e){}
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
