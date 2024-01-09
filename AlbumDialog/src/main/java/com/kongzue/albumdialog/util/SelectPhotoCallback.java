package com.kongzue.albumdialog.util;

import java.util.List;

public abstract class SelectPhotoCallback {

    public void selectedPhoto(String selectedPhotos){};

    public void selectedPhotos(List<String> selectedPhotos){};
}
