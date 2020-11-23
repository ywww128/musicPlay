package com.example.musicplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.musicplayer.R;
import com.example.musicplayer.bean.CommunityItemBean;
import com.example.musicplayer.utils.DataUtil;
import com.xuexiang.xui.widget.button.shinebutton.ShineButton;
import com.xuexiang.xui.widget.imageview.RadiusImageView;

import java.util.Arrays;
import java.util.List;

/**
 * 设置社区界面的数据
 * @author czc
 */
public class CommunityAdapter extends BaseAdapter implements OnClickListener {

    private LayoutInflater inflater;
    private List<CommunityItemBean> list;
    private Callback callback;

    public CommunityAdapter(Context context, List<CommunityItemBean> list, Callback callback) {
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        this.callback = callback;
    }
    /**
     * 自定义接口，用于回调按钮点击事件到Activity
     */
    public interface Callback{
        /**
         * 点击触发的方法
         * @param v 发生单击事件所在的视图
         */
        void click(View v);
    }

    /**
     * @return 返回ListView显示的总数量
     */
    @Override
    public int getCount() {
        return list.size();
    }


    /**
     * @param position Item在ListView中的索引
     * @return //获取ListView中单个Item
     */
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 对每个Item中的内容进行设置，并对Item中的Button设置监听器
     * @param position Item对应的索引
     * @param convertView Item对应的视图
     * @param parent viewgroup
     * @return 返回每一个Item显示的内容
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.community, null);

            viewHolder.avatar = convertView.findViewById(R.id.avatar);
            viewHolder.name = convertView.findViewById(R.id.name);
            viewHolder.time = convertView.findViewById(R.id.time);
            viewHolder.content = convertView.findViewById(R.id.content);
            viewHolder.btnLike = convertView.findViewById(R.id.btn_like);
            viewHolder.btnLike.setChecked(true);
            viewHolder.btnComment = convertView.findViewById(R.id.btn_comment);
            viewHolder.comments = convertView.findViewById(R.id.comments);

            //将converView与viewHolder进行关联，之后可以直接取出viewHolder，取出对应组件
            convertView.setTag(viewHolder);
        }else{
            //有converView的缓存，可以直接取出其中的viewHolder
            viewHolder = (ViewHolder) convertView.getTag();
        }

        CommunityItemBean item = list.get(position);

        //设置View中组件的内容
        viewHolder.avatar.setImageResource(item.getAvatar());
        viewHolder.name.setText(item.getName());
        viewHolder.time.setText(item.getTime());
        viewHolder.content.setText(item.getContent());
        viewHolder.comments.setAdapter(item.getCommentAdapter());
        viewHolder.btnLike.setOnClickListener(this);
        viewHolder.btnLike.setTag(position+",like");
        viewHolder.btnLike.setChecked(isLike(String.valueOf(position)));
        viewHolder.btnComment.setOnClickListener(this);
        viewHolder.btnComment.setTag(position+",comment");
        setListViewHeightBasedOnChildren(viewHolder.comments);

        return convertView;
    }

    @Override
    public void onClick(View v) {
        callback.click(v);
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    /**
     * 判断当前评论是否已经点击like按钮
     * @param position 当前评论的位置
     * @return 已经点击返回true，否则返回false
     */
    private boolean isLike(String position){
        for(int i = 0; i < DataUtil.like_situation.size(); i++){
            String[] likes = DataUtil.like_situation.get(i).split(" ");
            if(position.equals(likes[0])) {
                List<String> users = Arrays.asList(likes[1].split(","));
                if(users.contains("zicai")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 存储每个评论的组件
     */
    class ViewHolder{
        RadiusImageView  avatar;
        TextView name;
        TextView time;
        TextView content;
        ShineButton btnLike;
        ShineButton btnComment;
        ListView comments;
    }
}
