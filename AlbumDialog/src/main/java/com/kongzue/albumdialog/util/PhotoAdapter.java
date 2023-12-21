package com.kongzue.albumdialog.util;

import static com.kongzue.dialogx.interfaces.BaseDialog.isNull;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    private Context context;
    private List<String> imageUrls; // 图片的 URL 列表
    private int imageSize;
    private int errorPhotoDrawableRes;
    private List<String> selectedPhotos;

    public PhotoAdapter(Context context, List<String> imageUrls, int imageSize, int errorPhotoDrawableRes, List<String> selectedPhotos) {
        this.context = context;
        this.imageUrls = imageUrls;
        this.imageSize = imageSize;
        this.selectedPhotos = selectedPhotos;
        this.errorPhotoDrawableRes = errorPhotoDrawableRes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PhotoSelectImageView imageView = new PhotoSelectImageView(context);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                imageSize
        ));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return new ViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(context)
                .load(imageUrls.get(position))
                .override(imageSize)
                .error(errorPhotoDrawableRes)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.itemView.setTag(null);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into((PhotoSelectImageView) holder.itemView);
        holder.itemView.setTag(imageUrls.get(position));
        ((PhotoSelectImageView) holder.itemView).setSelectState(selectedPhotos.contains(imageUrls.get(position)));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = (String) v.getTag();
                if (isNull(uri)) return;
                if (selectedPhotos.contains(uri)) {
                    selectedPhotos.remove(uri);
                    ((PhotoSelectImageView) holder.itemView).setSelectState(false);
                    if (!onSelectPhoto(uri, selectedPhotos, false)){
                        selectedPhotos.add(uri);
                        ((PhotoSelectImageView) holder.itemView).setSelectState(true);
                    }
                } else {
                    selectedPhotos.add(uri);
                    ((PhotoSelectImageView) holder.itemView).setSelectState(true);
                    if (!onSelectPhoto(uri, selectedPhotos, true)){
                        selectedPhotos.remove(uri);
                        ((PhotoSelectImageView) holder.itemView).setSelectState(false);
                    }
                }
            }
        });
    }

    public boolean onSelectPhoto(String uri, List<String> selectedPhotos, boolean add) {
        return true;
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(ImageView itemView) {
            super(itemView);
        }
    }
}
