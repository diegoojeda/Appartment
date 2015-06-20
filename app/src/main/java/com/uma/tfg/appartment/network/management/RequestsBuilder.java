package com.uma.tfg.appartment.network.management;

import com.uma.tfg.appartment.model.Receipt;
import com.uma.tfg.appartment.network.model.Request;
import com.uma.tfg.appartment.network.requests.IniPost;
import com.uma.tfg.appartment.network.requests.groups.GroupDetailsGet;
import com.uma.tfg.appartment.network.requests.groups.GroupsGet;
import com.uma.tfg.appartment.network.requests.groups.GroupsPost;
import com.uma.tfg.appartment.network.requests.receipts.ReceiptDetailsGet;
import com.uma.tfg.appartment.network.requests.receipts.ReceiptsGet;
import com.uma.tfg.appartment.network.requests.receipts.ReceiptsPost;
import com.uma.tfg.appartment.network.requests.user.LoginPost;

import java.util.List;

public class RequestsBuilder {

    public static void sendIniPostRequest(String deviceId, String lang, String pushToken, int appVersion, IniPost.IniPostListener listener){
        IniPost request = new IniPost(deviceId, lang, pushToken, appVersion, listener);
        RequestsManager.getInstance().queueRequest(request);
    }

    public static void sendLoginPostRequest(String userId, String userEmail, String userName, String userPicture, LoginPost.LoginPostListener listener) {
        LoginPost request = new LoginPost(userId, userEmail, userName, userPicture, listener);
        RequestsManager.getInstance().queueRequest(request);
    }

    public static void sendGroupsGetRequest(GroupsGet.GroupsGetListener listener){
        GroupsGet request = new GroupsGet(listener);
        RequestsManager.getInstance().queueRequest(request);
    }

    public static void sendGetGroupsDetailsRequest(String groupId, GroupDetailsGet.GroupDetailsGetListener listener){
        GroupDetailsGet request = new GroupDetailsGet(groupId, listener);
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

    public static void sendGetReceiptDetailsRequest(String receiptId, ReceiptDetailsGet.ReceiptDetailsGetListener listener){
        ReceiptDetailsGet request = new ReceiptDetailsGet(receiptId, listener);
        RequestsManager.getInstance().queueRequest(request);
    }

    public static void sendGetReceiptsRequest(String groupId, ReceiptsGet.ReceiptsGetListener listener){
        ReceiptsGet request = new ReceiptsGet(groupId, listener);
        RequestsManager.getInstance().queueRequest(request);
    }

    public static void sendCreateReceiptRequest(String groupId, double amount, List<String> debtors,
                                                String receiptName, ReceiptsPost.ReceiptsPostListener listener){
        ReceiptsPost request = new ReceiptsPost(ReceiptsPost.ReceiptsPostActions.INSERT, listener);
        request.mAmount = amount;
        request.mDebtorsIdsList = debtors;
        request.mGroupId = groupId;
        request.mReceiptName = receiptName;

        RequestsManager.getInstance().queueRequest(request);
    }

    public static void sendDeleteReceiptRequest(String receiptId, ReceiptsPost.ReceiptsPostListener listener){
        ReceiptsPost request = new ReceiptsPost(ReceiptsPost.ReceiptsPostActions.DELETE, listener);
        request.mReceiptId = receiptId;

        RequestsManager.getInstance().queueRequest(request);
    }

    public static void sendUserHasPaidReceiptRequest(String receiptId, ReceiptsPost.ReceiptsPostListener listener){
        ReceiptsPost request = new ReceiptsPost(ReceiptsPost.ReceiptPostUpdateTypes.DEBTORS, listener);
        request.mReceiptId = receiptId;

        RequestsManager.getInstance().queueRequest(request);
    }
}
