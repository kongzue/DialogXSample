package com.kongzue.albumdialog.util;

import static com.kongzue.dialogx.interfaces.BaseDialog.isNull;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.kongzue.albumdialog.PhotoAlbumDialog;
import com.kongzue.albumdialog.R;

import java.util.ArrayList;
import java.util.List;

public class AlbumUtil {
    public static List<String> getAllAlbums(Context context) {
        List<String> albums = new ArrayList<>();
        String[] projection = new String[]{
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_ID
        };

        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cur = context.getContentResolver().query(images,
                projection, // Which columns to return
                null,       // Which rows to return (all rows)
                null,       // Selection arguments (none)
                null        // Ordering
        );

        if (cur != null && cur.moveToFirst()) {
            int bucketColumn = cur.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

            do {
                String albumName = cur.getString(bucketColumn);
                if (!albums.contains(albumName) && !isNull(albumName)) albums.add(albumName);
            } while (cur.moveToNext());
        }

        if (cur != null) {
            cur.close();
        }

        return albums;
    }

    public static List<String> getPhotosByAlbum(Context context, String albumName, String sortBy, PhotoAlbumDialog.SORT_MODE sortMode) {
        List<String> photos = new ArrayList<>();
        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = new String[]{
                MediaStore.Images.Media.DATA, // 图片的文件路径
                MediaStore.Images.Media.DATE_ADDED // 图片的拍摄日期
        };
        String selection;
        String[] selectionArgs;
        if (isNull(albumName) || context.getString(R.string.album_dialog_default_album_name).equals(albumName)) {
            selection = null;
            selectionArgs = null;
        } else {
            selection = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " = ?";
            selectionArgs = new String[]{albumName};
        }
        String sortOrder = sortBy + " " + sortMode.name();
        Cursor cur = context.getContentResolver().query(images,
                projection,
                selection,
                selectionArgs,
                sortOrder);

        if (cur != null && cur.moveToFirst()) {
            int dataColumn = cur.getColumnIndex(MediaStore.Images.Media.DATA);
            do {
                String photoPath = cur.getString(dataColumn);
                photos.add(photoPath);
            } while (cur.moveToNext());
        }

        if (cur != null) {
            cur.close();
        }

        return photos;
    }

    public static String getLatestPhotoFromAlbum(Context context, String albumName) {
        Uri imagesUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = new String[]{
                MediaStore.Images.Media.DATA, // 图片的文件路径
                MediaStore.Images.Media.DATE_ADDED // 图片的拍摄日期
        };
        String selection;
        String[] selectionArgs;
        if (isNull(albumName) || context.getString(R.string.album_dialog_default_album_name).equals(albumName)) {
            selection = null;
            selectionArgs = null;
        } else {
            selection = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " = ?";
            selectionArgs = new String[]{albumName};
        }

        String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC"; // 按日期降序排序

        try (Cursor cursor = context.getContentResolver().query(
                imagesUri,
                projection,
                selection,
                selectionArgs,
                sortOrder)) {
            if (cursor != null && cursor.moveToFirst()) {
                // 获取最新一张照片的文件路径
                return cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // 没有找到照片或出现异常
    }
}
