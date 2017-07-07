package com.veeradeveloper.videofilter;

public class ConstantFlag {
    public static final String VIDEO_KEY = "VideoPath";
    public static final String DEFAULT_TEXT = "defaulttext";
    public static final String RECORDED_VIDEO_KEY = "Recorded_video";
    public static final String VIDEO_ROTATION = "video_rotation";
    public static final String GIF_NOTTRIM = "GifNotTrim";
    public static final String IMAGE_ORINTATION = "orintation";
    public static final String GIF_PATH = "gifpath";
    public static final String TRIM_VIDEO_PATH = "trimvideopath";
    public static final String GIF_WEB_URL = "gifurl";
    public static final String GIF_URL_ARRAY = "urlArray";
    public static final String CAMERA_VIDEO_ORINTATION = "cameravideoorintation";
    public static final String VIDEOLIST_VIDEO_ORINTATION = "videolistvideoorintation";
    public static final String ADDTEXT = "addTextText";

    public static final String NOT_DELETE = "notedelete";
    public static final String IS_VIDEO = "isvideo";
    public static final String IS_CAMEARA_VIDEO = "isCameraVideo";
    public static final String GIFLISTTO_SHAREGIF = "gifto_sharegif";

    public static final String API_URL = "http://gifmaker.store/gifs/webservice/";
    public static final String GIF_ONLINE_PATH_FB = "http://gifmaker.store/gifs/";
    public static final String GIF_ONLINE_FULL_PATH = "giffullpath";

    public static final String EXPORTVIDEO_PATH = "exportvideopath";
    public static int selectedEffect;
    public static int comeTime;
    public static final int PERMISSION_REQUEST_CODE_FOR_CAMERA = 0;
    public static final int PERMISSION_REQUEST_CODE = 1000;
    public static final int PERMISSION_GRANTED = 1001;
    public static final int PERMISSION_DENIED = 1002;

    public static final int REQUEST_CODE = 2000;

    public static final int FETCH_STARTED = 2001;
    public static final int FETCH_COMPLETED = 2002;
    public static final int ERROR = 2005;

    public static final int PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 23;

    public static final String INTENT_EXTRA_ALBUM = "album";
    public static final String INTENT_EXTRA_IMAGES = "images";
    public static final String INTENT_EXTRA_LIMIT = "limit";
    public static final int DEFAULT_LIMIT = 40;

    static {
        selectedEffect = -1;
        comeTime = PERMISSION_REQUEST_CODE_FOR_CAMERA;
    }
}
