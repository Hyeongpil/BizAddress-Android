package com.kosign.bizaddress.main.group;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kosign.bizaddress.R;

import java.util.ArrayList;

/**
 * Created by Hyeongpil on 2016. 8. 9..
 */
public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder>{
    private Context mContext;
    private View.OnClickListener listener;
    private ArrayList<String> mListData = null;

    public GroupAdapter(Context mContext, View.OnClickListener listener) {
        this.mContext = mContext;
        this.listener = listener;
    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item, parent, false);
        itemView.setOnClickListener(listener);
        return new GroupViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GroupViewHolder holder, int position) {
        String data = mListData.get(position);
        holder.getGroup_name().setText(data);
    }

    @Override
    public int getItemCount() {
        if(mListData == null){
            return 0;
        }else{
            return mListData.size();
        }
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder{
        public TextView group_name;

        public GroupViewHolder(View itemView) {
            super(itemView);
            group_name = (TextView)itemView.findViewById(R.id.group_name);
        }
        public TextView getGroup_name() {return group_name;}
    }

}
