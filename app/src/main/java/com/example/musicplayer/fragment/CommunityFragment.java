package com.example.musicplayer.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.example.musicplayer.R;
import com.example.musicplayer.activity.MainActivity;
import com.example.musicplayer.adapter.CommunityAdapter;
import com.example.musicplayer.adapter.CommunityAdapter.Callback;
import com.example.musicplayer.bean.Comment;
import com.example.musicplayer.bean.CommunityItemBean;
import com.example.musicplayer.bean.Post;
import com.example.musicplayer.bean.User;
import com.example.musicplayer.listener.NormalErrorListener;
import com.example.musicplayer.listener.NormalResponseListener;
import com.example.musicplayer.util.DataUtils;
import com.example.musicplayer.util.RequestQueueUtils;
import com.example.musicplayer.utils.DataUtil;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.header.TaurusHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xuexiang.xui.utils.SnackbarUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.imageview.RadiusImageView;
import com.xuexiang.xui.widget.statelayout.MultipleStatusView;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author czc
 */
public class CommunityFragment extends Fragment implements OnItemClickListener, Callback {

    private static final String USERNAME = "username";
    private String username;

    private RadiusImageView publish;
    private ListView listView;

    private SmartRefreshLayout smartRefreshLayout;
    private MultipleStatusView multipleStatusView;
    private Handler mLoadingHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (multipleStatusView.getViewStatus() == MultipleStatusView.STATUS_LOADING) {
                multipleStatusView.showContent();
            }
            return true;
        }
    });
    private List<CommunityItemBean> list = new ArrayList<>();
    private List<Comment> comments;
    private Map<String, String> users = new HashMap<>(16);
    private RequestQueue requestQueue;

    private ThreadFactory HttpThreadFactory = new ThreadFactoryBuilder().setNameFormat("HttpReqeust-%d").build();
    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            3,
            10,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingDeque<>(3),
            HttpThreadFactory,
            new ThreadPoolExecutor.AbortPolicy());

    public CommunityFragment() { }

    /**
     * 将用户的用户名保存起来
     * @param username 用户名
     * @return 返回一个CommunityFragment类，并将参数保存在类中
     */
    public static CommunityFragment newInstance(String username){
        CommunityFragment communityFragment = new CommunityFragment();
        Bundle args = new Bundle();
        args.putString(USERNAME, username);
        communityFragment.setArguments(args);
        return communityFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            username = getArguments().getString(USERNAME);
        }

        requestQueue = RequestQueueUtils.getRequestQueue(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_loading, container, false);
        //初始化控件
        publish = view.findViewById(R.id.publish);
        listView = view.findViewById(R.id.list1);
        smartRefreshLayout = view.findViewById(R.id.refreshLayout);
        multipleStatusView = view.findViewById(R.id.multiple_status_view);
        init();
        return view;
    }

    /**
     * 将数据通过SimpleAdapter封装到ListView中
     */
    private void init() {
        multipleStatusView.showLoading();
        getUsers();
        getComments();

        //设置SmartRefreshLayout的header样式
        smartRefreshLayout.setRefreshHeader(new TaurusHeader(getActivity()));
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(1200);
                getComments();
            }
        });

        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("".equals(username) || username == null) {
                    SnackbarUtils.Short(v, "您还未登陆，请先登陆").gravityFrameLayout(Gravity.TOP)
                            .messageCenter().warning().show();
                }
                else {
                    CommunityFragment communityFragment = (CommunityFragment) getActivity().getSupportFragmentManager().findFragmentByTag("cf");
                    getActivity().getSupportFragmentManager().beginTransaction().hide(communityFragment).commit();
                    //跳转到PublishFragment,并将参数username传递给PublishFragment，设置tag为pf
                    PublishFragment publishFragment = (PublishFragment) getActivity().getSupportFragmentManager().findFragmentByTag("pf");
                    if (publishFragment == null) {
                        publishFragment = PublishFragment.newInstance(users.get(username), username);
                        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.content_panel, publishFragment, "pf").commit();
                    } else {
                        getActivity().getSupportFragmentManager().beginTransaction().show(publishFragment).commit();
                    }
                }
            }
        });
    }

    /**
     * 处理ListView中item中的button点击事件
     * @param v view
     */
    @Override
    public void click(View v) {
        String[] tags = ((String)v.getTag()).split(",");
        switch (tags[1]){
            case "like":
                Log.i("Community-like","position:"+tags[0]);

                break;
            case "comment":
                showInputDialog(tags[0]);
                Log.i("Community-comment","position:"+tags[0]);
                break;
            default:
                break;
        }
    }

    /**
     * 处理ListView中item点击事件
     * @param parent parent
     * @param view item的view
     * @param position item的位置
     * @param id item的id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    /**
     * 带输入框的对话框
     */
    private void showInputDialog(String position) {
        new MaterialDialog.Builder(getContext())
                .title(R.string.comment)
                .inputType(
                        InputType.TYPE_CLASS_TEXT)
                .input(getString(R.string.hint_comment), "", false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {

                    }
                })
                .inputRange(1,50)
                .positiveText(R.string.confirm)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String info = dialog.getInputEditText().getText().toString();
                        //点击确认按钮触发事件
                        //将评论写回
                        threadPoolExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                                writeComment(info, position);
                            }
                        });

                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //刷新fragment中的数据
                        getComments();
                        }
                })
                .cancelable(false)
                .show();
    }

    /**
     * 将发表的评论写回数据库
     * @param content 内容
     * @param position 对应动态的位置，可以通过position获取对应动态的信息
     */
    private void writeComment(String content, String position){
        int index = Integer.parseInt(position);
        CommunityItemBean item = list.get(index);
        String postId = item.getPostId();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Comment comment = new Comment(null, "102", content, postId,df.format(new Date()));
        Gson gson = new Gson();
        String info = gson.toJson(comment);
        String url = "http://116.62.109.242:9988/comment/insert";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new NormalResponseListener()
                                                        , new NormalErrorListener())
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>(16);
                map.put("comment", info);
                return map;
            }
        };
        requestQueue.add(stringRequest);

        Log.i("comment",comment.toString());
    }

    public void initData(){
        //清空ListView中的数据
        listView.setAdapter(null);
        list.clear();
        //获取评论数据用户数据

        //返回数据监听器
        Response.Listener<String> getAllPostListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Type postListType = new TypeToken<ArrayList<Post>>(){}.getType();
                List<Post> posts = gson.fromJson(response, postListType);
                //评论内容适配器
                SimpleAdapter commentAdapter = null;
                for(Post p : posts){
                    String postId = p.getPostId();
                    //获取动态对应的评论
                    List<Map<String, Object>> itemComment = new ArrayList<>();
                    for(Comment item : comments) {
                        Map<String, Object> map = new HashMap<>(16);
                        if(item.getCommentPostId().equals(postId)) {
                            map.put("comment_name", users.get(item.getCommentUserId()));
                            map.put("comment_content", item.getCommentText());
                            itemComment.add(map);
                        }
                    }
                    commentAdapter = new SimpleAdapter(getActivity(), itemComment, R.layout.comment
                                        , new String[]{"comment_name", "comment_content"}
                                        , new int[]{R.id.comment_name, R.id.comment_content});
                    list.add(new CommunityItemBean(2131230854, p.getPostCreateTime(), users.get(p.getPostAuthorId()), p.getPostText()
                            , 0, commentAdapter, p.getPostId()));
                }
                listView.setAdapter(new CommunityAdapter(getActivity(), list, CommunityFragment.this::click));
                listView.setOnItemClickListener(CommunityFragment.this::onItemClick);
            }
        };
        //发生错误时的监听器
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("CommunityFragment", error.getMessage(), error);
            }
        };

        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://116.62.109.242:9988/post/findAll"
                , getAllPostListener, errorListener){
            //修改编码格式，防止出现乱码
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String info = new String(response.data,"UTF-8");
                    return Response.success(info,
                            HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        requestQueue.add(postRequest);
    }

    public void getComments() {
        String commentUrl = "http://116.62.109.242:9988/comment/findAll";
        StringRequest commentRequest = new StringRequest(Request.Method.POST, commentUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        Type commentListType = new TypeToken<ArrayList<Comment>>(){}.getType();
                        comments = gson.fromJson(response, commentListType);
                        initData();
                        mLoadingHandler.sendEmptyMessageDelayed(0,500);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("CommunityFragment", error.getMessage(), error);
            }
        }){
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
        requestQueue.add(commentRequest);
    }
    private void getUsers() {
        String commentUrl = "http://116.62.109.242:9988/user/findAll";
        StringRequest commentRequest = new StringRequest(Request.Method.POST, commentUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<User> listUsers = new ArrayList<>();
                        Gson gson = new Gson();
                        Type userListType = new TypeToken<ArrayList<User>>(){}.getType();
                        listUsers = gson.fromJson(response, userListType);
                        for(User u : listUsers){
                            users.put(u.getUserId(), u.getUserName());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("CommunityFragment", error.getMessage(), error);
            }
        }){
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
        requestQueue.add(commentRequest);
    }

    /**
     * 展示加载界面
     */
    public void loading(){
        multipleStatusView.showLoading();
    }
}