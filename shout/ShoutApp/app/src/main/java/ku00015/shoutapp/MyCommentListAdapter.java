package ku00015.shoutapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Klaidas on 27/11/2017.
 */
public class MyCommentListAdapter extends ArrayAdapter<Comment> {

    Context context;
    ArrayList<Comment> comments;

    public MyCommentListAdapter(Activity context, ArrayList<Comment> dataset) {
        super(context, R.layout.list_item_layout_my_comments,dataset);

        this.context = context;
        this.comments = dataset;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.list_item_layout_my_comments, null, true);

        TextView myCommentsComment = (TextView) row.findViewById(R.id.myCommentListComment);
        TextView myCommentsCID = (TextView) row.findViewById(R.id.myCommentListCID);

        myCommentsComment.setText(comments.get(position).getComment());
        myCommentsCID.setText(comments.get(position).getCid());

        return row;
    }
}
