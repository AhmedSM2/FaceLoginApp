package com.example.ahmed.gp_posts;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import com.bumptech.glide.Glide;

public class RecyclerViewAdapter  extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {


    Context context;
    List<Posts> MainImageUploadInfoList;

    public RecyclerViewAdapter(Context context, List<Posts> TempList) {

        this.MainImageUploadInfoList = TempList;

        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_all_posts, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Posts UploadInfo = MainImageUploadInfoList.get(position);

        final String PostKey = MainImageUploadInfoList.get(position).toString();
        holder.imageNameTextView.setText(UploadInfo.getDescription());
        holder.post_time.setText(UploadInfo.getTime());
        holder.post_date.setText(UploadInfo.getDate());
        holder.post_username.setText(UploadInfo.getFullname());

        //Loading image from Glide library.
        Glide.with(context).load(UploadInfo.getPostimage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return MainImageUploadInfoList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView imageNameTextView;

        public TextView post_time;
        public TextView post_date;
        public TextView post_username;
        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.post_image);

            imageNameTextView = (TextView) itemView.findViewById(R.id.post_desc);
            post_time =(TextView) itemView.findViewById(R.id.post_time);
            post_date =(TextView) itemView.findViewById(R.id.post_date);
            post_username =(TextView) itemView.findViewById(R.id.post_username);

        }
    }

}
