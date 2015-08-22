package com.zykj.xishuashua.http;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


/**
 * Created by csh on 15-7-21.
 */
public class HttpUtils {

    private HttpUtils(){

    }

    private static AsyncHttpClient client = new AsyncHttpClient();

    static {
        //使用默认的 cacheThreadPool
        client.setTimeout(15);
        client.setConnectTimeout(15);
        client.setMaxConnections(20);
        client.setResponseTimeout(20);
    }
    
    /*用户登录*/
    public static void login(AsyncHttpResponseHandler handler, RequestParams params){
        client.post(UrlContants.getUrl(UrlContants.LOGIN), params, handler);
    }
    
    /*用户注册*/
    public static void register(AsyncHttpResponseHandler handler, RequestParams params){
        client.post(UrlContants.getUrl(UrlContants.REGISTER), params, handler);
    }
    
    /*忘记密码*/
    public static void forgetPwd(AsyncHttpResponseHandler handler, RequestParams params){
        client.post(UrlContants.getUrl(UrlContants.FORGETPWD), params, handler);
    }
    
    /*上传头像*/
    public static void uploadAvatar(AsyncHttpResponseHandler handler, RequestParams params){
        client.post(UrlContants.getUrl(UrlContants.AVATAR), params, handler);
    }
    
    /*上传头像*/
    public static void uploadMemberInfo(AsyncHttpResponseHandler handler, RequestParams params){
        client.post(UrlContants.getUrl(UrlContants.UPDATEUSERINFO), params, handler);
    }

    /*获取轮播图*/
    public static void getSpecialList(AsyncHttpResponseHandler handler){
        client.post(UrlContants.getUrl(UrlContants.SPECIALLIST), handler);
    }

    /*获取新闻资讯*/
    public static void getnewslist(AsyncHttpResponseHandler handler, RequestParams params){
        client.post(UrlContants.getUrl(UrlContants.NEWSLIST), params, handler);
    }

    /*获取红包列表*/
    public static void getEnveList(AsyncHttpResponseHandler handler, RequestParams params){
        client.post(UrlContants.getUrl(UrlContants.ENVELIST), params, handler);
    }

    /*获取推送设置和消息提示音状态*/
    public static void getpushandhint(AsyncHttpResponseHandler handler){
        client.post(UrlContants.getUrl(UrlContants.GEIPUSHANDHINT), handler);
    }

    /*设置推送设置和消息提示音状态*/
    public static void setpushandhint(AsyncHttpResponseHandler handler, String push, String hint){
        client.post(UrlContants.getUrl(UrlContants.SEIPUSHANDHINT+"&push="+push+"&hint="+hint), handler);
    }

    /*红包详情*/
    public static void getGoodDetail(AsyncHttpResponseHandler handler, RequestParams params){
        client.post(UrlContants.getUrl(UrlContants.GOODDETAIL), params, handler);
    }

    /*我的红包*/
    public static void getmemberenvelopes(AsyncHttpResponseHandler handler){
        client.post(UrlContants.getUrl(UrlContants.MEMBERENVELOPE), handler);
    }

    /*我的收藏*/
    public static void getmembercollect(AsyncHttpResponseHandler handler){
        client.post(UrlContants.getUrl(UrlContants.GETMEMBERCOLLECT), handler);
    }

    /*兴趣标签列表*/
    public static void getAllInterests(AsyncHttpResponseHandler handler){
        client.post(UrlContants.getUrl(UrlContants.GETALLINTEREST), handler);
    }

    /*获取用户已选标签*/
    public static void getmemberinterests(AsyncHttpResponseHandler handler){
        client.get(UrlContants.getUrl(UrlContants.MEMBERINTEREST), handler);
    }

    /*提交用户选择的标签*/
    public static void addinterestsmeta(AsyncHttpResponseHandler handler, RequestParams params){
        client.post(UrlContants.getUrl(UrlContants.ADDINTERESTSMETA), params, handler);
    }

    /*用户点赞点赞*/
    public static void clickfavorite(AsyncHttpResponseHandler handler, String giftId){
        client.post(UrlContants.getUrl(UrlContants.CLICKFAVORITE+"&goods_id="+giftId), handler);
    }

    /*用户收藏红包*/
    public static void clickCollect(AsyncHttpResponseHandler handler, String giftId){
        client.post(UrlContants.getUrl(UrlContants.CLICKCOLLECT+"&goods_id="+giftId), handler);
    }

    /*用户评论红包*/
    public static void submitcomment(AsyncHttpResponseHandler handler, RequestParams params){
        client.post(UrlContants.getUrl(UrlContants.SUBMITCOMMENT), params, handler);
    }

    /*获取红包评论列表*/
    public static void getcomment(AsyncHttpResponseHandler handler, RequestParams params){
        client.post(UrlContants.getUrl(UrlContants.GETCOMMENT), params, handler);
    }

    /*首页即时红包*/
    public static void getsomekindenvelist(AsyncHttpResponseHandler handler, RequestParams params){
        client.post(UrlContants.getUrl(UrlContants.SOMEKINDENVELIST), params, handler);
    }
    
}
