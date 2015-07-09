package com.propertyforest.propertyuploader;

import android.util.Log;
import android.widget.ProgressBar;

/**
 * Created by fash on 13/4/15.
 */
public class UploadInfo {
    private final static String TAG = UploadInfo.class.getSimpleName();

    public String getRentId() {
        return rent_id;
    }

    public enum DownloadState {
        NOT_STARTED,
        QUEUED,
        UPLOADING,
        COMPLETE
    }
    private volatile DownloadState mDownloadState = DownloadState.NOT_STARTED;
    private final String mFilename;
    private volatile Integer mProgress;
    private final Long mFileSize;
    private volatile ProgressBar mProgressBar;
    private final String rent_id;

    public UploadInfo(String filename, long size, String rent_id) {
        mFilename = filename;
        mProgress = 0;
        mFileSize = size;
        mProgressBar = null;
        this.rent_id = rent_id;
    }

    public ProgressBar getProgressBar() {
        return mProgressBar;
    }
    public void setProgressBar(ProgressBar progressBar) {
        Log.d(TAG, "setProgressBar " + mFilename + " to " + progressBar);
        mProgressBar = progressBar;
    }

    public void setDownloadState(DownloadState state) {
        mDownloadState = state;
    }
    public DownloadState getDownloadState() {
        return mDownloadState;
    }

    public Integer getProgress() {
        return mProgress;
    }

    public void setProgress(Integer progress) {
        this.mProgress = progress;
    }

    public Long getFileSize() {
        return mFileSize;
    }

    public String getFilename() {
        return mFilename;
    }
}
