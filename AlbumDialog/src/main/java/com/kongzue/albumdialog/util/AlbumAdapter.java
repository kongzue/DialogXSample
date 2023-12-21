package com.kongzue.albumdialog.util;

import static com.kongzue.albumdialog.util.AlbumUtil.getLatestPhotoFromAlbum;
import static com.kongzue.dialogx.interfaces.BaseDialog.isNull;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.kongzue.albumdialog.R;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private Context context;
    private List<String> albumNames;
    private int layoutHeight;
    private int errorPhotoDrawableRes;
    private String selectedAlbum;

    public AlbumAdapter(Context context, List<String> albumNames, int layoutHeight, int errorPhotoDrawableRes, String selectedAlbum) {
        this.context = context;
        this.albumNames = albumNames;
        this.layoutHeight = layoutHeight;
        this.errorPhotoDrawableRes = errorPhotoDrawableRes;
        this.selectedAlbum = selectedAlbum;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout albumView = new LinearLayout(context);
        albumView.setOrientation(LinearLayout.VERTICAL);
        albumView.setPadding(dip2px(10), 0, dip2px(10), dip2px(10));
        albumView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, layoutHeight));

        PhotoSelectImageView albumImageView = new PhotoSelectImageView(context);
        albumImageView.setRadius(dip2px(5));
        albumImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        LinearLayout.LayoutParams albumImageViewLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        albumImageViewLp.weight = 1;
        albumView.addView(albumImageView, albumImageViewLp);

        TextView labelView = new TextView(context);
        labelView.setTextColor(context.getResources().getColor(R.color.albumDefaultLabelColor));
        labelView.setGravity(Gravity.CENTER);
        labelView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        labelView.setPadding(dip2px(15), dip2px(5), dip2px(15), dip2px(15));
        albumView.addView(labelView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        return new ViewHolder(albumView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(context)
                .load(getLatestPhotoFromAlbum(context, albumNames.get(position)))
                .override(layoutHeight)
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
                .into(holder.albumImageView);
        holder.albumImageView.setSelectState(albumNames.get(position).equals(selectedAlbum) || (isNull(selectedAlbum) && position == 0));
        holder.itemView.setTag(albumNames.get(position));
        holder.albumLabelView.setText(albumNames.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChangeAlbum((String) v.getTag());
            }
        });
    }

    public void onChangeAlbum(String albumName) {

    }

    @Override
    public int getItemCount() {
        return albumNames.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout itemView;
        PhotoSelectImageView albumImageView;
        TextView albumLabelView;

        public ViewHolder(LinearLayout itemView) {
            super(itemView);
            this.itemView = itemView;
            albumImageView = (PhotoSelectImageView) itemView.getChildAt(0);
            albumLabelView = (TextView) itemView.getChildAt(1);
        }
    }

    private int dip2px(float dpValue) {
        return (int) (0.5f + dpValue * Resources.getSystem().getDisplayMetrics().density);
    }
}
