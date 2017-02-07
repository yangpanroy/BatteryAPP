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

public class ImportExportItemAdapter extends RecyclerView.Adapter{

    private LayoutInflater inflater;
    private ArrayList<HashMap<String, Object>> listItem;
    private MyItemClickListener myItemClickListener;

    public ImportExportItemAdapter(Context context, ArrayList<HashMap<String, Object>> listItem) {
        inflater = LayoutInflater.from(context);
        this.listItem = listItem;
    }//构造函数，传入数据

    //定义RecyclerView的ViewHolder
    class Holder extends RecyclerView.ViewHolder {

        TextView item_module_id, logistics_source, logistics_destination;

        public Holder(View itemView) {
            super(itemView);

            item_module_id = (TextView) itemView.findViewById(R.id.item_module_id);
            logistics_source = (TextView) itemView.findViewById(R.id.logistics_source);
            logistics_destination = (TextView) itemView.findViewById(R.id.logistics_destination);

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
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.import_export_item, parent, false);
        return new Holder(layout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof Holder) {
            ((Holder) holder).item_module_id.setText((String) listItem.get(position).get("item_module_id"));
            ((Holder) holder).logistics_source.setText((String) listItem.get(position).get("logistics_source"));
            ((Holder) holder).logistics_destination.setText((String) listItem.get(position).get("logistics_destination"));
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
