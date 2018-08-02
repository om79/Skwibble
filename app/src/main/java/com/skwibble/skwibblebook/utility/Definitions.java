package com.skwibble.skwibblebook.utility;

import android.content.Context;

import com.skwibble.skwibblebook.volley.VolleyWebLayer;

public class Definitions {

    // ====================================== Definitions ====================================== //
    public static final String user_name = "user_name" ;
    public static final String child_name = "child_name" ;
    public static final String app_version = "android_v3" ;
    public static final String version = "application/json;version=3" ;
    public static final String user_image = "user_image" ;
    public static final String user_email = "user_email" ;
    public static final String auth_token = "auth_token" ;
    public static final String user_dob = "user_dob" ;
    public static final String user_role_id = "user_role_id" ;
    public static final String id = "id" ;
    public static final String firebase_token= "cmtnmUCBnxc:APA91bHjoHve09FvrZtJMbWB47m3WU2bo0Xd_6dt7eRbOQmewXqYI5gdoMaXY_RT6XiMFo0r9KJmhDvtOsL_Xv6KJ7arI33syH738q_J8luoFiIMAFNnGrRcleZ9ovhz8_nuU05i2Rqi" ;
    public static final String has_first_post = "first_post" ;
    public static final String show_child_form = "show_child_form" ;
    public static final String has_child = "has_child" ;

    public static final String show_add_post  = "show_add_post" ;
    public static final String show_add_child  = "show_add_child" ;
    public static final String show_journal  = "show_journal" ;
    public static final String show_playpen  = "show_playpen" ;
    public static final String show_child_profile  = "show_child_profile" ;
    public static final String show_feed_invite  = "show_feed_invite" ;
    public static final String show_playpen_setting  = "show_playpen_setting" ;
    public static final String show_story_invite  = "show_story_invite" ;

    public static final String facebook_login = "facebook_login" ;
    public static final String badge_count = "badge_count" ;
    public static final String has_badges = "has_badges" ;
    public static final String app_install_first  = "app_install_first" ;

    public static final String current_noti_request  = "current_noti_request" ;
    public static final String current_noti_id  = "current_noti_id" ;
    public static final String current_journal_year  = "current_journal_year" ;
    public static final String current_event_msg  = "current_event_msg" ;

    public static final int REQUEST_CODE_MEDIA_SELECT = 200;
    public static final int REQUEST_CODE_MEDIA_CROP = 500;
    public static final String IMAGE_REQUEST  = "IMAGE_REQUEST" ;
    public static final String EXTRA_VIDEO_PATH  = "EXTRA_VIDEO_PATH" ;
    public static final String POST_DETAILS_REQUEST="POST_DETAILS_REQUEST";
    public static final int SINGLE_MEDIA_COUNT = 1;
    public static final int MULTI_MEDIA_COUNT = 6;

    public static final String STORY_FILE_UPLOAD_URL="/story_posts";
    public static final String JOURNAL_UPLOAD_URL="/jrnl_attachments";
    public static final String PLAYPEN_UPLOAD_URL="/playpen_posts";

    public static final String CURRENT_POSITION = "CURRENT_POSITION";
    public static final String CURRENT_LIKE = "CURRENT_LIKE";
    public static final String CURRENT_COMMENT = "CURRENT_COMMENT";
    public static final String CURRENT_USER_LIKE = "CURRENT_USER_LIKE";
    public static final String TERMS = "/terms?in_app=true";


//  public static final String APIdomain = "http://192.168.1.203:3000";//staging
    public static final String APIdomain = "http://139.59.186.196";//development
//    public static final String APIdomain = "https://www.skwibble.com";//live


    // ============================================ Others ============================================ //
    public static Context appContext;
    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";




    // ============================================ Initializer ============================================ //
    public static void initializeApplication(Context _appContext){
        appContext = _appContext;
        VolleyWebLayer.initialize();
    }
}
