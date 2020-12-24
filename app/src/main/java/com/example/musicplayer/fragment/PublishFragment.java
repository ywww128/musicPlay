package com.example.musicplayer.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.example.musicplayer.R;
import com.example.musicplayer.adapter.PictureSelectAdapter;
import com.example.musicplayer.bean.Post;
import com.example.musicplayer.listener.NormalErrorListener;
import com.example.musicplayer.listener.NormalResponseListener;
import com.example.musicplayer.util.GlideEngine;
import com.example.musicplayer.util.RequestQueueUtils;
import com.example.musicplayer.utils.DataUtil;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.xuexiang.xui.utils.SnackbarUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.edittext.MultiLineEditText;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发表动态fragment，对应fragment_publish
 * @author czc
 */
public class PublishFragment extends Fragment implements PictureSelectAdapter.OnAddViewClickListen {
    private static final String USERNAME = "username";
    private static final String USERID = "userId";
    private RecyclerView recyclerView;
    private PictureSelectAdapter adapter;
    private TitleBar titleBar;
    private MultiLineEditText editText;

    private String username;
    private String userId;
    private List<LocalMedia> images = new ArrayList<>();

    public PublishFragment() {
        // Required empty public constructor
    }


    public static PublishFragment newInstance(String username, String userId){
        PublishFragment publishFragment = new PublishFragment();
        Bundle args = new Bundle();
        args.putString(USERNAME, username);
        args.putString(USERID, userId);
        publishFragment.setArguments(args);
        return publishFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            username = getArguments().getString(USERNAME);
            userId = getArguments().getString(USERID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_publish, container, false);
        init(view);
        return view;
    }

    /**
     * 初始化控件
     * @param view 包含控件的视图
     */
    private void init(View view){
        titleBar = view.findViewById(R.id.tb_publish);
        editText = view.findViewById(R.id.publish_text);
        editText.setContentText(null);
        TextView tv_username = view.findViewById(R.id.username);
        tv_username.setText(username);

        //初始化RecyclerView，设置布局设置适配器
        recyclerView = view.findViewById(R.id.publish_images);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3
                                                                    , RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new PictureSelectAdapter(images, this);
        recyclerView.setAdapter(adapter);

        //设置标题栏左边按钮监听器，点击返回社区主界面
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublishFragment publishFragment = (PublishFragment) getActivity().getSupportFragmentManager().findFragmentByTag("pf");
                CommunityFragment communityFragment = (CommunityFragment) getActivity().getSupportFragmentManager().findFragmentByTag("cf");

                getActivity().getSupportFragmentManager().beginTransaction().hide(publishFragment).commit();
                getActivity().getSupportFragmentManager().beginTransaction().show(communityFragment).commit();
                Toast.makeText(getActivity(), "点击了返回按钮", Toast.LENGTH_SHORT).show();
            }
            //设置标题栏右边按钮监听器,点击发布动态
        }).addAction(new TitleBar.ImageAction(R.drawable.confirm) {
            @Override
            public void performAction(View view) {
                String info = editText.getContentText();
                Toast.makeText(getActivity(), "您输入了:"+info, Toast.LENGTH_SHORT).show();
                //判断输入书否为空,内容为空跳出提示信息
                if(info.equals("")){
                    //设置内容在顶部显示
                    SnackbarUtils.Short(view, "发布动态内容不能为空").gravityFrameLayout(Gravity.TOP)
                            .messageCenter().warning().show();
                }else{
                    writeSituation(info);
                    PublishFragment publishFragment = (PublishFragment) getActivity().getSupportFragmentManager().findFragmentByTag("pf");
                    CommunityFragment communityFragment = (CommunityFragment) getActivity().getSupportFragmentManager().findFragmentByTag("cf");

                    communityFragment.loading();
                    communityFragment.getComments();
                    getActivity().getSupportFragmentManager().beginTransaction().hide(publishFragment).commit();
                    getActivity().getSupportFragmentManager().beginTransaction().show(communityFragment).commit();
                }
            }
        });
    }

    /**
     * 将用户发布的内容写回,将images中的图片写入数据库
     */
    private void writeSituation(String content){
        Date now = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Post post = new Post(null, 0, df.format(now), userId, content, null);
        Log.i("PublishFragment", post.toString());
        String url = "http://10.0.2.2:8080/post/insert";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url
                                , new NormalResponseListener(), new NormalErrorListener()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>(16);
                Gson gson = new Gson();
                String info = gson.toJson(post);
                map.put("post", info);
                return map;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String info =  new String(response.data,"UTF-8");
                    return Response.success(info,
                            HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        RequestQueue requestQueue = RequestQueueUtils.getRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }


    @Override
    public void onAddViewClick() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .imageEngine(GlideEngine.createGlideEngine())
                .maxSelectNum(9)
                .minSelectNum(1)
                .imageSpanCount(4)
                .selectionMode(PictureConfig.MULTIPLE)
                .isPreviewImage(true)
                .isCamera(false)
                .isEnableCrop(false)
                .isCompress(true)
                .selectionData(images)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("PublishFragment", "---163");
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    Log.i("PublishFragment", "---167");
                    images = PictureSelector.obtainMultipleResult(data);
                    adapter.setImages(images);
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        editText.setContentText(null);
        super.onHiddenChanged(hidden);
    }
}