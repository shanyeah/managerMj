package com.imovie.mogic.chat.net;

import com.imovie.mogic.chat.model.ContactModel;
import com.imovie.mogic.chat.model.GroupModel;
import com.imovie.mogic.chat.model.LabelModel;
import com.imovie.mogic.config.HTTPConfig;
import com.imovie.mogic.login.model.BaseReqParamNetMap;
import com.imovie.mogic.login.model.TestModel;
import com.imovie.mogic.web.HttpWebHelper;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.http.HttpHelper;

import java.util.List;

/**
 * Created by $zhou on 2017/6/22 0022.
 */

public class ChatWebHelper extends HttpWebHelper {

    /**
     * 好友列表
     * @param listener
     */

    public static void getUserFriendList(String nickName,int pageNum,int pageSize,IModelResultListener<ContactModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        StringBuffer data = new StringBuffer();
//        data.append(HTTPConfig.getUrlData(HTTPConfig.url_queryUserFriend));
        data.append("&pageNum=" + pageNum);
        data.append("&pageSize=" + pageSize);
        data.append("&nickName=" + nickName);
        new ChatWebHelper().sendPostWithTranslate(ContactModel.class, data.toString(), HttpHelper.TYPE_3, HttpWebHelper.TYPE_3,baseReqParamNetMap, listener);
    }

    public static String getUserFriendList(String nickName,int pageNum,int pageSize) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        String result = "";
        StringBuffer data = new StringBuffer();
//        data.append(HTTPConfig.getUrlData(HTTPConfig.url_queryUserFriend));
        data.append("&pageNum=" + pageNum);
        data.append("&pageSize=" + pageSize);
        data.append("&nickName=" + nickName);
        result = HttpHelper.getHttpURLResult(data.toString());
        return result;

    }


    /**
     * 附近的人
     * @param listener
     */

    public static void getNearbyUserList(double longitude,double latitude,String nickName,String gender,String ageArea,int pageNum,int pageSize,IModelResultListener<ContactModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        StringBuffer data = new StringBuffer();
//        data.append(HTTPConfig.getUrlData(HTTPConfig.url_queryNearbyUser));
        data.append("&pageNum=" + pageNum);
        data.append("&pageSize=" + pageSize);
        data.append("&nickName=" + nickName);
        data.append("&gender=" + gender);
        data.append("&ageArea=" + ageArea);
        data.append("&longitude=" + longitude);
        data.append("&latitude=" + latitude);
        new ChatWebHelper().sendPostWithTranslate(ContactModel.class, data.toString(), HttpHelper.TYPE_3, HttpWebHelper.TYPE_3,baseReqParamNetMap, listener);
    }

    /**
     * 关注
     * @param listener
     */

    public static void getAttention(int attentUserId,int source,IModelResultListener<TestModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        StringBuffer data = new StringBuffer();
//        data.append(HTTPConfig.getUrlData(HTTPConfig.url_userAttention));
        data.append("&attentUserId=" + attentUserId);
        data.append("&source=" + source);
        new ChatWebHelper().sendPostWithTranslate(TestModel.class, data.toString(), HttpHelper.TYPE_2, HttpWebHelper.TYPE_0,baseReqParamNetMap, listener);
    }

    /**
     * 取消关注
     * @param listener
     */

    public static void getCancelAttention(int attentUserId,int source,IModelResultListener<TestModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        StringBuffer data = new StringBuffer();
//        data.append(HTTPConfig.getUrlData(HTTPConfig.url_cancelAttention));
        data.append("&attentUserId=" + attentUserId);
//        data.append("&source=" + source);
        new ChatWebHelper().sendPostWithTranslate(TestModel.class, data.toString(), HttpHelper.TYPE_5, HttpWebHelper.TYPE_0,baseReqParamNetMap, listener);
    }

    /**
     * 群组列表
     * @param listener
     */

    public static void getGroupList(double longitude,double latitude,String nickName,int pageNum,int pageSize,IModelResultListener<ContactModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        StringBuffer data = new StringBuffer();
//        data.append(HTTPConfig.getUrlData(HTTPConfig.url_getNearbyGroup));
        data.append("&pageNum=" + pageNum);
        data.append("&pageSize=" + pageSize);
        data.append("&name=" + nickName);
        data.append("&longitude=" + longitude);
        data.append("&latitude=" + latitude);

        new ChatWebHelper().sendPostWithTranslate(ContactModel.class, data.toString(), HttpHelper.TYPE_3, HttpWebHelper.TYPE_3,baseReqParamNetMap, listener);
    }

    /**
     * 我的群列表
     * @param listener
     */

    public static void getMyGroupList(int pageNum,int pageSize,IModelResultListener<ContactModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        StringBuffer data = new StringBuffer();
//        data.append(HTTPConfig.getUrlData(HTTPConfig.url_queryGroupList));
        data.append("&pageNum=" + pageNum);
        data.append("&pageSize=" + pageSize);

        new ChatWebHelper().sendPostWithTranslate(ContactModel.class, data.toString(), HttpHelper.TYPE_3, HttpWebHelper.TYPE_3,baseReqParamNetMap, listener);
    }




    /**
     * 群组详情
     * @param listener
     */

    public static void getGroupDetail(String emGroupId,IModelResultListener<GroupModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        StringBuffer data = new StringBuffer();
//        data.append(HTTPConfig.getUrlData(HTTPConfig.url_getGroupDetail));
        data.append("&emGroupId=" + emGroupId);
        new ChatWebHelper().sendPostWithTranslate(GroupModel.class, data.toString(), HttpHelper.TYPE_3, HttpWebHelper.TYPE_3,baseReqParamNetMap, listener);
    }

    /**
     * 加入群组
     * @param listener
     */

    public static void getJoinGroup(int groupId,IModelResultListener<TestModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        StringBuffer data = new StringBuffer();
//        data.append(HTTPConfig.getUrlData(HTTPConfig.url_joinGroupRequest));
        data.append("&groupId=" + groupId);
        new ChatWebHelper().sendPostWithTranslate(TestModel.class, data.toString(), HttpHelper.TYPE_4, HttpWebHelper.TYPE_3,baseReqParamNetMap, listener);
    }

    /**
     * 退出群组
     * @param listener
     */

    public static void getExitGroup(int groupId,IModelResultListener<TestModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        StringBuffer data = new StringBuffer();
//        data.append(HTTPConfig.getUrlData(HTTPConfig.url_exitGroup));
        data.append("&groupId=" + groupId);
        new ChatWebHelper().sendPostWithTranslate(TestModel.class, data.toString(), HttpHelper.TYPE_5, HttpWebHelper.TYPE_3,baseReqParamNetMap, listener);
    }

    /**
     * 群成员
     * @param listener
     */

    public static void getGroupMembers(int groupId,int pageNum,int pageSize,IModelResultListener<ContactModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        StringBuffer data = new StringBuffer();
//        data.append(HTTPConfig.getUrlData(HTTPConfig.url_groupMember));
        data.append("&groupId=" + groupId);
        data.append("&pageNum=" + pageNum);
        data.append("&pageSize=" + pageSize);
        new ChatWebHelper().sendPostWithTranslate(ContactModel.class, data.toString(), HttpHelper.TYPE_3, HttpWebHelper.TYPE_3,baseReqParamNetMap, listener);
    }

    /**
     * 创建群组
     * @param listener
     */

    public static void getCreateGroup(String name, String groupAddress, String groupPosition, String imageUid, String description, List<LabelModel> labels, IModelResultListener<GroupModel> listener) {
        BaseReqParamNetMap baseReqParamNetMap = new BaseReqParamNetMap();
        baseReqParamNetMap.put("name",name);
        baseReqParamNetMap.put("groupAddress",groupAddress);
        baseReqParamNetMap.put("groupPosition",groupPosition);
        baseReqParamNetMap.put("imageUid",imageUid);
        baseReqParamNetMap.put("description",description);
        baseReqParamNetMap.put("labels",labels);
        StringBuffer data = new StringBuffer();
//        data.append(HTTPConfig.getUrlData(HTTPConfig.url_createGroup));
        new ChatWebHelper().sendPostWithTranslate(GroupModel.class, data.toString(), HttpHelper.TYPE_2, HttpWebHelper.TYPE_3,baseReqParamNetMap, listener);
    }




}
