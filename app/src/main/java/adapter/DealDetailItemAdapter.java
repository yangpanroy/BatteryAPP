package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.novadata.batteryapp.R;

import java.util.ArrayList;
import java.util.HashMap;

public class DealDetailItemAdapter extends RecyclerView.Adapter{
    private ArrayList<HashMap<String, Object>> listItem;
    private MyItemClickListener myItemClickListener;

    public DealDetailItemAdapter(Context context, ArrayList<HashMap<String, Object>> listItem) {
        LayoutInflater inflater = LayoutInflater.from(context);
        this.listItem = listItem;
    }//构造函数，传入数据

    //定义RecyclerView的ViewHolder
    class Holder extends RecyclerView.ViewHolder {

        TextView detail_item_module_date, detail_logistics_source, detail_logistics_destination, detail_item_module_id;

        public Holder(View itemView) {
            super(itemView);

            detail_item_module_date = (TextView) itemView.findViewById(R.id.detail_item_module_date);
            detail_logistics_source = (TextView) itemView.findViewById(R.id.detail_logistics_source);
            detail_logistics_destination = (TextView) itemView.findViewById(R.id.detail_logistics_destination);
            detail_item_module_id = (TextView) itemView.findViewById(R.id.detail_item_module_id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myItemClickListener != null)
                        myItemClickListener.onItemClick(v, getPosition());
                }

            }//监听到点击就回调MainActivity的onItemClick函数
            );
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.deal_detail_item, parent, false);
        return new Holder(layout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof Holder) {
            ((Holder) holder).detail_item_module_date.setText((String) listItem.get(position).get("detail_item_module_date"));
            ((Holder) holder).detail_logistics_source.setText((String) listItem.get(position).get("detail_logistics_source"));
            ((Holder) holder).detail_logistics_destination.setText((String) listItem.get(position).get("detail_logistics_destination"));
            ((Holder) holder).detail_item_module_id.setText((String) listItem.get(position).get("detail_item_module_id"));
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
