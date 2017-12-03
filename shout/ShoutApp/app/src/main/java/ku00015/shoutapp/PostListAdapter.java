package ku00015.shoutapp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

public class PostListAdapter extends ArrayAdapter<Post> {

    Context context;
    ArrayList<Post> posts;

    public PostListAdapter(Activity context, ArrayList<Post> dataset){
        super(context, R.layout.list_item_layout_custom, dataset);
        this.context = context;
        this.posts = dataset;
    }

    /*
     * Sets every row in ListView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.list_item_layout_custom, null, true);

        TextView title = (TextView) row.findViewById(R.id.lvTitle);
        TextView body = (TextView) row.findViewById(R.id.lvBody);
        TextView pid = (TextView) row.findViewById(R.id.lvPID);
        TextView uid = (TextView) row.findViewById(R.id.lvUID);
        TextView user = (TextView) row.findViewById(R.id.lvUsername);
        TextView likes = (TextView) row.findViewById(R.id.likeCount);

        title.setText(posts.get(position).getPostTitle());
        body.setText(posts.get(position).getBody());
        pid.setText(posts.get(position).getPid());
        uid.setText(posts.get(position).getUid());
        user.setText(posts.get(position).getUser());
        likes.setText(posts.get(position).getLikes());

        return row;
    }

}
