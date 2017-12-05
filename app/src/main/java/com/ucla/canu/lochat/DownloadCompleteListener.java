package com.ucla.canu.lochat;

import enums.DownloadCompleteEnum;

/**
 * Created by nishsab on 11/17/17.
 */

public interface DownloadCompleteListener {
    void downloadComplete(String response, String error);
    void getRoomsComplete(String message);
    void createRoomsComplete(String message, Integer statusCode);
    void getMessagesComplete(String message, Integer statusCode);
    void createMessagesComplete(String message, Integer statusCode);
}
