package com.waiter.mh;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.waiter.mh.model.BoxProdInfo;
import com.waiter.mh.model.ResponseInfo;
import com.waiter.mh.model.RfBoxProdInfo;
import com.waiter.mh.utils.Config;
import com.waiter.mh.utils.HttpUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * 盒子商品关联
 */
public class BoxProdLinkFragment extends Fragment implements View.OnClickListener {
    EditText mBoxCode, mProdCode, mPknCode;//盒子编码，商品编码，分拣单编码
    RecyclerView mRecyclerView;
    BoxProdAdapter mAdapter;
    TextView mScanQty, mUserCode;//扫描数，操作员
    LinearLayoutManager mLayoutManager;
    SharedPreferences mPreferences;
    ImageView mScanProd, mScanBox, mScanPkn;//商品条码，盒子条码，分拣单条码右边的相机图片
    Button mPackSubmit;//分拣包装完毕按钮

    public BoxProdLinkFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initControls();//初始化控件
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_box_prod_link, container, false);
        //注册广播
        IntentFilter intentFilter = new IntentFilter(Config.SCAN_ACTION);
        getActivity().registerReceiver(receiver, intentFilter);
        return view;
    }

    /**
     * 初始化控件
     */
    private void initControls() {
        mBoxCode = (EditText) getActivity().findViewById(R.id.et_box_code);
        mProdCode = (EditText) getActivity().findViewById(R.id.et_prod_code);
        mPknCode = (EditText) getActivity().findViewById(R.id.et_pkn_code);
        mScanQty = (TextView) getActivity().findViewById(R.id.scan_qty);
        mUserCode = (TextView) getActivity().findViewById(R.id.user_code);
        mScanProd = (ImageView) getActivity().findViewById(R.id.im_scan_prod);
        mScanBox = (ImageView) getActivity().findViewById(R.id.im_scan_box);
        mScanPkn = (ImageView) getActivity().findViewById(R.id.im_scan_pkn);
        mPackSubmit = (Button) getActivity().findViewById(R.id.btn_pack_submit);
        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.rv_box_prod);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new BoxProdAdapter(getActivity(), 1);
        mAdapter.setOnItemClickListener(mOnItemClickListener);
        mRecyclerView.setAdapter(mAdapter);
        mPreferences = getActivity().getSharedPreferences(Config.ACCOUNT_PASSWORD, getActivity().MODE_PRIVATE);
        mUserCode.setText(mPreferences.getString(Config.USER_CODE, null));
        //绑定点击事件
        mScanBox.setOnClickListener(this);
        mScanPkn.setOnClickListener(this);
        mScanProd.setOnClickListener(this);
        mPackSubmit.setOnClickListener(this);
    }

    /**
     * 需要自定义的广播接收器
     */
    BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String barcode = intent.getStringExtra("scan_code");
            View view = getActivity().getWindow().getDecorView();//找到焦点所在控件
            switch (view.findFocus().getId()) {
                case R.id.et_prod_code://商品条码
                    mProdCode.setText(barcode);
                    break;
                case R.id.et_box_code://盒子条码
                    mBoxCode.setText(barcode);
                    addBoxProdToList(mProdCode.getText().toString(), barcode);//将数据添加到列表
                    break;
                case R.id.et_pkn_code://分拣单号
                    mPknCode.setText(barcode);
                    break;
                default:
                    break;
            }
        }
    };

    //将扫描的盒子信息写入list列表显示
    private void addBoxProdToList(String prodCode, String boxProd) {
        BoxProdInfo info = new BoxProdInfo();
        info.setBOX_CODE(boxProd);
        info.setPROD_CODE(prodCode);
        mAdapter.addData(info);//数据传给mAdapter显示
        mScanQty.setText(Integer.toString(mAdapter.getItemCount()));//设置扫描数
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }

    private BoxProdAdapter.OnItemClickListener mOnItemClickListener = new BoxProdAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            showDeleteDialog(position);//删除选中行
        }
    };

    //删除对话框
    private void showDeleteDialog(final int position) {
        BoxProdInfo data = mAdapter.getItem(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle("确定要删除:");
        builder.setMessage(data.getBOX_CODE());
        builder.setCancelable(false);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAdapter.removeItem(position);
                mScanQty.setText(Integer.toString(mAdapter.getItemCount()));//设置扫描数
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }

        });
        builder.create().show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.im_scan_prod://商品条码右边的相机图片给点击了,扫描一次返回结果
                mProdCode.requestFocus();//焦点设置到它
                startActivity(new Intent(getActivity(), ScanActivity.class).putExtra(Config.START_SCAN, Config.ONCE_SCAN));
                break;
            case R.id.im_scan_box://盒子条码右边的相机图片给点击了,连续扫描
                if (TextUtils.isEmpty(mProdCode.getText())) {
                    Toast.makeText(getActivity(), "请先扫描商品编码", Toast.LENGTH_SHORT).show();
                    break;
                }
                mBoxCode.requestFocus();//焦点设置到它
                startActivity(new Intent(getActivity(), ScanActivity.class).putExtra(Config.START_SCAN, Config.CONTINUOUS_SCAN));
                break;
            case R.id.im_scan_pkn://分拣单右边的相机图片给点击了,扫描一次返回结果
                mPknCode.requestFocus();//焦点设置到它
                startActivity(new Intent(getActivity(), ScanActivity.class).putExtra(Config.START_SCAN, Config.ONCE_SCAN));
                break;
            case R.id.btn_pack_submit://分拣包装完毕,
                packSubmit();//提交数据到服务器
                break;
        }
    }

    /**
     * 提交盒子商品关系到后台服务器
     */
    private void packSubmit() {
        List<BoxProdInfo> prodLst = mAdapter.getmDatas();
        if (prodLst == null || prodLst.size() <= 0) {
            return;
        }
        if (TextUtils.isEmpty(mUserCode.getText())) {
            Toast.makeText(getActivity(), "操作人员不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(mPknCode.getText())) {
            Toast.makeText(getActivity(), "分拣单编号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        //显示进度条
        final ProgressDialog pd = ProgressDialog.show(getActivity(), "提示", getResources().getString(R.string.submit_data));
        List<RfBoxProdInfo> submitData = new ArrayList<>();
        for (BoxProdInfo prods : prodLst) {
            RfBoxProdInfo info = new RfBoxProdInfo();
            info.setBOX_CODE(prods.getBOX_CODE());
            info.setPROD_CODE(prods.getPROD_CODE());
            info.setUSER_CODE(mUserCode.getText().toString());
            info.setPKN_CODE(mPknCode.getText().toString());
            submitData.add(info);
        }
        HttpUtil.getInstance().insertBoxProd(submitData, new HttpUtil.ResultCallback() {
            @Override
            public void onResult(String str) {
                pd.dismiss();//取消进度条
                Type type = new TypeToken<ResponseInfo<String>>() {
                }.getType();
                ResponseInfo result = new Gson().fromJson(str, type);
                if (result == null) {
                    Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
                } else if (result != null && !result.getStatus().equals(Config.STATUS_SUCCESS)) {
                    Toast.makeText(getActivity(), result.getDescription(), Toast.LENGTH_SHORT).show();//错误原因显示出来
                } else {
                    //提交成功清空界面数据
                    clearCurrentData();
                }
            }
        });
    }

    /**
     * 清空界面中的数据
     */
    private void clearCurrentData() {
        mAdapter.clearAllData();
        mProdCode.setText("");
        mPknCode.setText("");
    }
}
