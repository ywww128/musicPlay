package com.example.musicplayer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.musicplayer.R;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

/**
 * @author czc
 */
public class PictureSelectAdapter extends RecyclerView.Adapter<PictureSelectAdapter.ViewHolder> {
    private List<LocalMedia> images;
    private int maxSelectNum = 9;
    private OnAddViewClickListen addViewClickListen;

    public interface OnAddViewClickListen{
        void onAddViewClick();
    }

    public PictureSelectAdapter(List<LocalMedia> images, OnAddViewClickListen onaddViewClickListen) {
        this.images = images;
        this.addViewClickListen = onaddViewClickListen;
    }

    public void setImages(List<LocalMedia> images) {
        this.images = images;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_img, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        //position==images.size(),则当前图片为添加按钮
        if(position == images.size()){
            holder.img.setImageResource(R.drawable.ic_add_image);
            //添加图片的按钮上不显示删除按钮
            holder.delete.setVisibility(View.INVISIBLE);
            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addViewClickListen.onAddViewClick();
                }
            });
        }else {
            holder.delete.setVisibility(View.VISIBLE);
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = holder.getAdapterPosition();
                    if(index != RecyclerView.NO_POSITION){
                        images.remove(index);
                        notifyItemRemoved(index);
                        notifyItemRangeChanged(index, images.size());
                    }
                }
            });
            LocalMedia media = images.get(position);

            String path;
            if (media.isCut() && !media.isCompressed()) {
                // 图片裁剪过，获取被裁剪过的路径
                path = media.getCutPath();
            } else if (media.isCompressed() || (media.isCut() && media.isCompressed())) {
                // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                path = media.getCompressPath();
            } else {
                // 原图
                path = media.getPath();
            }
            //设置图片加载策略
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.color.color_f4)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);

            //将图片加载到对应的ImageView中
            Glide.with(holder.itemView.getContext())
                    .load(path)
                    .apply(options)
                    .into(holder.img);
        }
    }

    @Override
    public int getItemCount() {
        if(images.size() < maxSelectNum){
            return images.size() + 1;
        }else {
            return images.size();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        LinearLayout delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.iv_select_pic);
            delete = itemView.findViewById(R.id.ll_delete);
        }
    }
}
