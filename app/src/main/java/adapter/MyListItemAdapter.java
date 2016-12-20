package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.novadata.batteryapp.R;

import java.util.ArrayList;
import java.util.HashMap;

public class MyListItemAdapter extends RecyclerView.Adapter {
    private LayoutInflater inflater;
    private ArrayList<HashMap<String, Object>> listItem;
    private MyItemClickListener myItemClickListener;


    public MyListItemAdapter(Context context, ArrayList<HashMap<String, Object>> listItem) {
        inflater = LayoutInflater.from(context);
        this.listItem = listItem;
    }//构造函数，传入数据

    //定义RecyclerView的ViewHolder
    class Holder extends RecyclerView.ViewHolder {

        private TextView Title, Text1, Text2,Text3;
        private ImageView ima;

        public Holder(View itemView) {
            super(itemView);

            Title = (TextView) itemView.findViewById(R.id.Itemtitle);
            Text1 = (TextView) itemView.findViewById(R.id.Itemtext1);
            Text2 = (TextView) itemView.findViewById(R.id.Itemtext2);
            Text3 = (TextView) itemView.findViewById(R.id.Itemtext3);
            ima = (ImageView) itemView.findViewById(R.id.ItemImage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myItemClickListener != null)
                        myItemClickListener.onItemClick(v, getPosition());
                }

            }//监听到点击就回调MainActivity的onItemClick函数
            );
        }

        public TextView getTitle() {
            return Title;
        }

        public TextView getText1() {
            return Text1;
        }

        public TextView getText2() {
            return Text2;
        }

        public TextView getText3() {
            return Text3;
        }

        public ImageView getIma() {
            return ima;
        }

    }

    //加载Item View的时候根据不同TYPE加载不同的布局
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cell, parent, false);
        return new Holder(layout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof Holder) {
            ((Holder) holder).Title.setText((String) listItem.get(position).get("ItemTitle"));
            ((Holder) holder).Text1.setText((String) listItem.get(position).get("ItemText1"));
            ((Holder) holder).Text2.setText((String) listItem.get(position).get("ItemText2"));
            ((Holder) holder).Text3.setText((String) listItem.get(position).get("ItemText3"));
            ((Holder) holder).ima.setImageResource((Integer) listItem.get(position).get("ItemImage"));
        }
    }//在这里绑定数据到ViewHolder里面

    @Override
    public int getItemCount() {
        return listItem.size();
    }//返回Item数目

    public void setOnItemClickListener(MyItemClickListener listener){
        myItemClickListener = listener;
    }//绑定MainActivity传进来的点击监听器

}
