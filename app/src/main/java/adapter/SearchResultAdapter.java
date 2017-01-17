package adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.novadata.batteryapp.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter{

    private LayoutInflater inflater;
    private ArrayList<HashMap<String, Object>> listItem;
    private MyItemClickListener myItemClickListener;
    private String imageUrl;
    private Bitmap bitmap;
    private ImageView imageView;

    public SearchResultAdapter(Context context, ArrayList<HashMap<String, Object>> listItem) {
        inflater = LayoutInflater.from(context);
        this.listItem = listItem;
    }//构造函数，传入数据

    //定义RecyclerView的ViewHolder
    class Holder extends RecyclerView.ViewHolder {

        TextView searchResult_Itemtitle, searchResult_Itemtext1, searchResult_Itemtext2, searchResult_Itemtext3, searchResult_Itemtext4, searchResult_Itemtext5;

        public Holder(View itemView) {
            super(itemView);

            searchResult_Itemtitle = (TextView) itemView.findViewById(R.id.searchResult_Itemtitle);
            searchResult_Itemtext1 = (TextView) itemView.findViewById(R.id.searchResult_Itemtext1);
            searchResult_Itemtext2 = (TextView) itemView.findViewById(R.id.searchResult_Itemtext2);
            searchResult_Itemtext3 = (TextView) itemView.findViewById(R.id.searchResult_Itemtext3);
            searchResult_Itemtext4 = (TextView) itemView.findViewById(R.id.searchResult_Itemtext4);
            searchResult_Itemtext5 = (TextView) itemView.findViewById(R.id.searchResult_Itemtext5);
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
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_item, parent, false);
        return new Holder(layout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof Holder) {
            ((Holder) holder).searchResult_Itemtitle.setText((String) listItem.get(position).get("ItemTitle"));
            ((Holder) holder).searchResult_Itemtext1.setText((String) listItem.get(position).get("ItemText1"));
            ((Holder) holder).searchResult_Itemtext2.setText((String) listItem.get(position).get("ItemText2"));
            ((Holder) holder).searchResult_Itemtext3.setText((String) listItem.get(position).get("ItemText3"));
            ((Holder) holder).searchResult_Itemtext4.setText((String) listItem.get(position).get("ItemText4"));
            ((Holder) holder).searchResult_Itemtext5.setText((String) listItem.get(position).get("ItemText5"));
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
