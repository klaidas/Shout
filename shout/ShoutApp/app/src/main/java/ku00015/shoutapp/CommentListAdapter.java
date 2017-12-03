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
 * Created by Klaidas on 25/11/2017.
 */
public class CommentListAdapter extends ArrayAdapter<Comment> {

    Context context;
    ArrayList<Comment> comments;

    public CommentListAdapter(Activity context, ArrayList<Comment> dataset){
        super(context, R.layout.list_item_layout_comments, dataset);

        this.context = context;
        this.comments = dataset;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.list_item_layout_comments, null, true);

        TextView user = (TextView) row.findViewById(R.id.commentListUser);
        TextView comment = (TextView) row.findViewById(R.id.commentListComment);
        TextView cid = (TextView) row.findViewById(R.id.commentListCID);
        TextView pid = (TextView) row.findViewById(R.id.commentListPID);
        TextView uid = (TextView) row.findViewById(R.id.commentListUID);

        user.setText(comments.get(position).getUser());
        comment.setText(comments.get(position).getComment());
        cid.setText(comments.get(position).getCid());
        pid.setText(comments.get(position).getPid());
        uid.setText(comments.get(position).getUid());

        return row;
    }
}
