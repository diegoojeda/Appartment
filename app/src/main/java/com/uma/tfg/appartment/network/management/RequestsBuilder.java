package com.uma.tfg.appartment.network.management;

import com.uma.tfg.appartment.network.requests.IniPost;
import com.uma.tfg.appartment.network.requests.groups.GroupsGet;
import com.uma.tfg.appartment.network.requests.groups.GroupsPost;
import com.uma.tfg.appartment.network.requests.user.LoginPost;

public class RequestsBuilder {

    public static void sendIniPostRequest(String deviceId, String lang, String pushToken, int appVersion, IniPost.IniPostListener listener){
        IniPost request = new IniPost(deviceId, lang, pushToken, appVersion, listener);
        RequestsManager.getInstance().queueRequest(request);
    }

    public static void sendLoginPostRequest(String userId, String userEmail, String userName, LoginPost.LoginPostListener listener) {
        LoginPost request = new LoginPost(userId, userEmail, userName, listener);
        RequestsManager.getInstance().queueRequest(request);
    }

    public static void sendGroupsGetRequest(GroupsGet.GroupsGetListener listener){
        GroupsGet request = new GroupsGet(listener);
        RequestsManager.getInstance().queueRequest(request);
    }

    public static void sendCreateGroupRequest(String groupName, GroupsPost.GroupsPostListener listener){
        GroupsPost request = new GroupsPost(GroupsPost.GroupsPostAction.INSERT);
        request.mName = groupName;
        request.mListener = listener;
        RequestsManager.getInstance().queueRequest(request);
    }

    public static void sendDeleteGroupRequest(String groupId, GroupsPost.GroupsPostListener listener) {
        GroupsPost request = new GroupsPost(GroupsPost.GroupsPostAction.DELETE);
        request.mId = groupId;
        request.mListener = listener;
        RequestsManager.getInstance().queueRequest(request);
    }
}
