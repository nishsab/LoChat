package com.ucla.canu.lochat;

import enums.DownloadCompleteEnum;

/**
 * Created by nishsab on 11/17/17.
 */

public interface DownloadCompleteListener {
    void downloadComplete(DownloadCompleteEnum stage, String response, String error);
    void getRoomsComplete(String message);
}
