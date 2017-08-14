package com.waiter.mh;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
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
import com.waiter.mh.model.BoxRequestInfo;
import com.waiter.mh.model.ResponseInfo;
import com.waiter.mh.utils.Config;
import com.waiter.mh.utils.HttpUtil;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * 盒子网点回收
 */
public class BoxRecoverFragment extends Fragment implements View.OnClickListener {

    EditText mBoxCode, mWarehCode;//盒子编码，网点编码
    RecyclerView mRecyclerView;
    BoxProdAdapter mAdapter;
    TextView mScanQty, mUserCode;//扫描数，操作员
    LinearLayoutManager mLayoutManager;
    SharedPreferences mPreferences;
    ImageView mScanBox, mScanWareh;//盒子条码，网点编码右边的相机图片
    Button mBtnSubmit;//提交按钮

    public BoxRecoverFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initControls();//初始化控件
        //注册广播
        IntentFilter intentFilter = new IntentFilter(Config.SCAN_ACTION);
        getActivity().registerReceiver(receiver, intentFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_box_recover, container, false);
    }

    /**
     * 初始化控件
     */
    private void initControls() {
        mBoxCode = (EditText) getActivity().findViewById(R.id.et_box_code);
        mWarehCode = (EditText) getActivity().findViewById(R.id.et_wareh_code);
        mScanQty = (TextView) getActivity().findViewById(R.id.scan_qty);
        mUserCode = (TextView) getActivity().findViewById(R.id.user_code);

        mScanBox = (ImageView) getActivity().findViewById(R.id.im_scan_box);
        mScanWareh = (ImageView) getActivity().findViewById(R.id.im_scan_wareh);
        mBtnSubmit = (Button) getActivity().findViewById(R.id.btn_submit);
        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.rv_box_prod);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new BoxProdAdapter(getActivity(), 0);
        mAdapter.setOnItemClickListener(mOnItemClickListener);
        mRecyclerView.setAdapter(mAdapter);
        mPreferences = getActivity().getSharedPreferences(Config.ACCOUNT_PASSWORD, getActivity().MODE_PRIVATE);
        mUserCode.setText(mPreferences.getString(Config.USER_CODE, null));
        //绑定点击事件
        mScanBox.setOnClickListener(this);
        mScanWareh.setOnClickListener(this);
        mBtnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.im_scan_box://盒子条码右边的相机图片给点击了,连续扫描
                mBoxCode.requestFocus();//焦点设置到它
                startActivity(new Intent(getActivity(), ScanActivity.class).putExtra(Config.START_SCAN, Config.CONTINUOUS_SCAN));
                break;
            case R.id.im_scan_wareh://网点单右边的相机图片给点击了，只扫描一次返回结果
                mWarehCode.requestFocus();//焦点设置到它
                startActivity(new Intent(getActivity(), ScanActivity.class).putExtra(Config.START_SCAN, Config.ONCE_SCAN));
                break;
            case R.id.btn_submit://分拣包装完毕,
                submitData();//提交数据到服务器
                break;
        }
    }

    /**
     * 提交数据到服务器
     */
    private void submitData() {
        List<BoxProdInfo> prodLst = mAdapter.getmDatas();
        if (prodLst == null || prodLst.size() <= 0) {
            return;
        }
        String userCode = mUserCode.getText().toString();
        if (TextUtils.isEmpty(userCode)) {
            Toast.makeText(getActivity(), "操作人员不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        String warehCode = mWarehCode.getText().toString();
        if (TextUtils.isEmpty(warehCode)) {
            Toast.makeText(getActivity(), "网点编号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        //显示进度条
        final ProgressDialog pd = ProgressDialog.show(getActivity(), "提示", getResources().getString(R.string.submit_data));
        List<BoxRequestInfo> submitData = new ArrayList<>();
        for (BoxProdInfo prods : prodLst) {
            BoxRequestInfo info = new BoxRequestInfo();
            info.setBOX_CODE(prods.getBOX_CODE());
            info.setWAREH_CODE(warehCode);
            info.setUSER_CODE(userCode);
            submitData.add(info);
        }
        HttpUtil.getInstance().recoverBox(submitData, new HttpUtil.SuccessCallback() {
            @Override
            public void onSuccess(String str) {
                pd.dismiss();//取消进度条
                Type type = new TypeToken<ResponseInfo<String>>() {
                }.getType();
                ResponseInfo result = new Gson().fromJson(str, type);
                if (result == null) {
                    Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
                } else if (result != null && !result.getStatus().equals(Config.STATUS_SUCCESS)) {
                    Toast.makeText(getActivity(), result.getDescription(), Toast.LENGTH_SHORT).show();//错误原因显示出来
                } else {
                    Toast.makeText(getActivity(), "数据提交成功", Toast.LENGTH_SHORT).show();
                    //提交成功清空界面数据
                    clearCurrentData();
                }
            }
        }, new HttpUtil.FailCallback() {
            @Override
            public void onFail(String failMsg) {
                pd.dismiss();//取消进度条
                Toast.makeText(getActivity(), "提交数据异常：" + failMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 清空界面中的数据
     */
    private void clearCurrentData() {
        mAdapter.clearAllData();
        mBoxCode.setText("");
        mWarehCode.setText("");
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
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
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
                case R.id.et_box_code://盒子条码
                    mBoxCode.setText(barcode);
                    addBoxToList(barcode);//将数据添加到列表
                    break;
                case R.id.et_wareh_code://网点号
                    mWarehCode.setText(barcode);
                    break;
                default:
                    break;
            }
        }
    };

    //将扫描的盒子信息写入list列表显示
    private void addBoxToList(String boxProd) {
        BoxProdInfo info = new BoxProdInfo();
        info.setBOX_CODE(boxProd);
        info.setSCAN_TIME(getCurrentTime());
        mAdapter.addData(info);//数据传给mAdapter显示
        mScanQty.setText(Integer.toString(mAdapter.getItemCount()));//设置扫描数
    }

    public String getCurrentTime() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return sDateFormat.format(new java.util.Date());
    }

}
