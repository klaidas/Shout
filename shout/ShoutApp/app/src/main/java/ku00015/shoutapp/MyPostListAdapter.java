package ku00015.shoutapp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Klaidas on 27/11/2017.
 */
public class MyPostListAdapter extends ArrayAdapter<Post> {

    Context context;
    ArrayList<Post> posts;

    public MyPostListAdapter(Activity context, ArrayList<Post> dataset) {
        super(context, R.layout.list_item_layout_my_posts,dataset);

        this.context = context;
        this.posts = dataset;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.list_item_layout_my_posts, null, true);

        TextView myPostsTitle = (TextView) row.findViewById(R.id.myPostsListTITLE);
        TextView myPostsPID = (TextView) row.findViewById(R.id.myPostsListPID);

        myPostsTitle.setText(posts.get(position).getPostTitle());
        myPostsPID.setText(posts.get(position).getPid());

        return row;
    }
}
