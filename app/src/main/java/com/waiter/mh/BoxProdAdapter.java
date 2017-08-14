package com.waiter.mh;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.waiter.mh.model.BoxProdInfo;

import java.util.ArrayList;

/**
 * Created by shiju on 2017/6/23.
 */
public class BoxProdAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<BoxProdInfo> mDatas = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;
    private Context mContext;
    private int mSwitchFragment;//记录时那个界面启动的扫描

    public BoxProdAdapter(Context context, int switchFragment) {
        this.mContext = context;
        mSwitchFragment = switchFragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //商品盒子关联，显示的列为：行号，商品编码，盒子条码
        if (mSwitchFragment == 1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_prod_item, parent, false);
            ItemViewHolder viewHolder = new ItemViewHolder(view);
            return viewHolder;
        }
        else  if(mSwitchFragment == 2){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.prod_item, parent, false);
            ItemViewHolder viewHolder = new ItemViewHolder(view);
            return viewHolder;
        }
        //其他列表显示的列为：行号，盒子编码，扫描时间
        else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.box_item, parent, false);
            ItemViewHolder viewHolder = new ItemViewHolder(view);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            BoxProdInfo data = mDatas.get(position);
            //设置显示的数据
            if (mSwitchFragment == 1) {
                ((ItemViewHolder) holder).mLineNo.setText(Integer.toString(data.getLINE_NO()));//int类型的放入需要转换成字符串
                ((ItemViewHolder) holder).mProdCode.setText(data.getPROD_CODE());
                ((ItemViewHolder) holder).mBoxCode.setText(data.getBOX_CODE());
            } else {
                ((ItemViewHolder) holder).mLineNo.setText(Integer.toString(data.getLINE_NO()));//int类型的放入需要转换成字符串
                ((ItemViewHolder) holder).mScanTime.setText(data.getSCAN_TIME());
                ((ItemViewHolder) holder).mBoxCode.setText(data.getBOX_CODE());
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void setDatas(ArrayList<BoxProdInfo> datas) {
        this.mDatas = datas;
        this.notifyDataSetChanged();
    }

    /**
     * 扫描单个添加单号，限制了添加重复盒子编码
     *
     * @param info
     */
    public void addData(BoxProdInfo info) {
        boolean isNotExist = true;
        for (BoxProdInfo i : mDatas) {
            if (i.getBOX_CODE().equals(info.getBOX_CODE())) {
                isNotExist = false;
                break;
            }
        }
        if (isNotExist) {
            info.setLINE_NO(mDatas.size() + 1);
            this.mDatas.add(info);
            this.notifyDataSetChanged();
        }
    }

    public void removeItem(int position) {
        this.mDatas.remove(position);
        for (int i = 0; i < mDatas.size(); i++) {
            mDatas.get(i).setLINE_NO(i + 1);
        }
        this.notifyDataSetChanged();
    }

    public ArrayList<BoxProdInfo> getmDatas() {
        return this.mDatas;
    }

    public void clearAllData() {
        this.mDatas.clear();
        this.notifyDataSetChanged();
    }

    public BoxProdInfo getItem(int position) {
        return mDatas == null ? null : mDatas.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mProdCode, mLineNo, mBoxCode, mScanTime;

        public ItemViewHolder(View itemView) {
            super(itemView);
            if (mSwitchFragment == 1) {
                mProdCode = (TextView) itemView.findViewById(R.id.tx_prod_code);
                mLineNo = (TextView) itemView.findViewById(R.id.tx_line_no);
                mBoxCode = (TextView) itemView.findViewById(R.id.tx_box_code);
            } else {
                mScanTime = (TextView) itemView.findViewById(R.id.tx_scan_time);
                mLineNo = (TextView) itemView.findViewById(R.id.tx_line_no);
                mBoxCode = (TextView) itemView.findViewById(R.id.tx_box_code);
            }
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(view, this.getAdapterPosition());
            }
        }
    }
}
