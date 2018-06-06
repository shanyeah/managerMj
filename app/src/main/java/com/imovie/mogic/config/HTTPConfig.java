package com.imovie.mogic.config;


import com.imovie.mogic.MyApplication;

/**
 * Created by ${zhouxinshan} on 2016/4/12.
 */
public class HTTPConfig {

    /* 正式地址*/
//    public final static String do_main = "http://api.liandaxia.com/";
    public final static String do_main1 = "http://api.liandaxia.com";
    /* 测试地址*/
    public final static String do_main = "http://local.api.liandaxia.com/";
//    public final static String do_main1 = "http://www.liandaxia.com";

    /* 正式地址*/
//    public final static String do_main = "http://api.mjdj.cn/";
    /* 测试地址*/
//    public final static String do_main = "http://local.imp.mjdj.cn/";


    /* 登录*/
    public final static String url_login = do_main + "clerk/login.do";
    /* 首页*/
    public final static String url_home = do_main + "clerk/home.do";
    /* 商品列表 */
    public final static String url_allGoodList = do_main + "clerk/queryAllGoods.do";
    /* 查询用户 */
    public final static String url_checkUserInfo = do_main + "clerk/queryLikeMemberInfo.do";
    /* 保存定单 */
    public final static String url_saveGoodsOrder = do_main + "clerk/saveGoodsBill.do";
    /* 定单会员价 */
    public final static String url_billMemberPrice = do_main + "clerk/getGoodsBillMemberPrice.do";
    /* 定单详情 */
    public final static String url_queryGoodsBillDetail = do_main + "clerk/queryGoodsBillDetail.do";
    /* 充值定单详情 */
    public final static String url_queryRechangeDetail = do_main + "clerk/queryUserRechangeDetail.do";
    /* 支付定单 */
    public final static String url_payGoodsOrder = do_main + "clerk/payGoodsBill.do";

    /* 更新 */
    public final static String url_update = do_main + "clerk/checkAppVersion.do";
    /* 充值下单*/
    public final static String url_preqrcharge = do_main + "clerk/userRecharge.do";
    /* 赠送 */
    public final static String url_presentList = do_main + "clerk/queryUserChargePresent.do";
    /* 报表 */
    public final static String url_reportList = do_main + "report/home.do";
    /* 当月月绩 */
    public final static String url_businessDetail = do_main + "clerk/clerkBusinessDetail.do";

    /* 充值记录 */
    public final static String url_chargeRecord = do_main + "clerk/queryMyRechargeList.do";
    /* 充值次数 */
    public final static String url_chargeNum = do_main + "clerk/clerkDateCharge.do";
    /* 点餐记录 */
    public final static String url_goodsPage = do_main + "clerk/queryClerkGoodsList.do";
    /* 每日收款 */
    public final static String url_goodsNum = do_main + "/clerk/clerkDateGoods.do";
    /* 排行榜 */
    public final static String url_goodsRanking = do_main + "clerk/queryIncomeRanking.do";
    /* 定单详情 */
    public final static String url_queryGoodsDetail = do_main + "clerk/queryGoodsDetail.do";
    /* 评论列表 */
    public final static String url_reviewList = do_main + "clerk/queryNetbarReviewList.do";
    /* 改密码 */
    public final static String url_setPassword = do_main + "clerk/changePassword.do";





    /* 我的 */
    public final static String url_my = do_main + "api/clerk/clerkdetail.do";

    /* 点赞 */
    public final static String url_myQrCode = do_main + "api/clerk/createlikeurl.do";


    /* 馆列表 */
    public final static String url_hallList = do_main + "api/clerk/clerkstglist.do";

    /* 点赞URL */
    public final static String url_likeUrl = do_main + "api/clerk/createlikeurl.do";

    /* 定餐列表 */
    public final static String url_orderList = do_main + "api/goodsorderlist.do";


    /* 扫码充值 */
    public final static String url_adminQrCharge = do_main + "api/clerk/adminqrcharge.do";

    /* 扫码充值重试 */
    public final static String url_qrChargeReset = do_main + "api/clerk/readminqrcharge.do";






    /* 点赞排行 */
    public final static String url_likeRanking = do_main + "api/clerk/clerklikeranking.do";

    /* 充值排行 */
    public final static String url_chargeRanking = do_main + "api/clerk/clerkchargeranking.do";



    /* 点赞记录 */
    public final static String url_likePage = do_main + "api/clerk/clerklikepage.do";





    /* 点赞次数 */
    public final static String url_likeNum = do_main + "api/clerk/clerkdatelike.do";



    /* 回复评论 */
    public final static String url_reply = do_main + "api/clerk/reply.do";
    /* 扫码授权 */
    public final static String url_scanAuthCode = do_main + "api/clerk/scanauthcode.do";

    /* 授权确认 */
    public final static String url_confirmAuth = do_main + "api/clerk/confirmauth.do";
    /* 授权列表 */
    public final static String url_authCodeList = do_main + "api/clerk/authcodelist.do";

    /* 考勤范围 */
    public final static String url_attendArea = do_main + "attend/attendarea.do";
    /* 考勤信息 */
    public final static String url_attendInfo = do_main + "attend/attendbasicinfo.do";
    /* 出勤列表 */
    public final static String url_attendRecords = do_main + "attend/attendrecords.do";
    /* 打卡 */
    public final static String url_clock = do_main + "attend/clock.do";
    /* 统计 */
    public final static String url_myAttend = do_main + "attend/my.do";
    /* 打卡月历 */
    public final static String url_myCalendar = do_main + "attend/mycalendar.do";
    /* 补卡 */
    public final static String url_recheckClock = do_main + "attend/recheckapply.do";
    /* 补卡信息 */
    public final static String url_recheckInfo = do_main + "attend/attendextrarecords.do";






    /* 收入汇总 */
    public final static String url_incomeSummary = do_main + "api/clerk/queryClerkRewardSummary.do";

    /* 充值明细 */
    public final static String url_incomeCharge = do_main + "api/clerk/queryRewardDetail4Charge.do";
    /* 点赞明细 */
    public final static String url_incomePraise = do_main + "api/clerk/queryRewardDetail4UserLike.do";
    /* 点餐明细 */
    public final static String url_incomeOrder = do_main + "api/clerk/queryRewardDetail4Goods.do";

    /* 上传图片 */
    public final static String url_uploadImage = do_main + "api/clerk/changeclerkinfo.do";






    /* 验证码 */
    public final static String url_sendRegAuth= do_main + "api/sendRegAuth.do";
    public final static String url_sendResetAuth= do_main + "api/resetpwdsms.do";

    /* 注册 */
    public final static String url_reg = do_main + "api/reg.do";
    /* 改登录密码*/
    public final static String url_resetLoginPassword = do_main + "api/resetpwd.do";



    /* 地区列表 */
    public final static String url_cityList = do_main + "api/stgcitylist.do";

    /* 馆详情 */
    public final static String url_stgDetail = do_main + "api/stgdetail.do";


    /* 电影列表 */
    public final static String url_movieList = do_main + "api/movielist.do";
    /* 电影信息 */
    public final static String url_movieDetail = do_main + "api/moviedetail.do";


    /* 网吧列表 */
    public final static String url_movieStgList = do_main + "api/moviestglist.do";
    public final static String url_roomCategoryList = do_main + "api/roomcategorylist.do";
    public final static String url_bookMovie = do_main + "api/bookmovie.do";

    /* 影片条件 */
    public final static String url_Condition = do_main + "api/moviequerycondition.do";




    /* 下单 */
    public final static String url_payWechatId = do_main + "pay/wxapp/wxchargeprepayid.do";
    /* 订单查询 */
    public final static String url_queryWechatOrder = do_main + "pay/wxapp/wxorderquery.do";
    /* 卡明细 */
    public final static String url_cardDetail = do_main + "api/carddetail.do";






    /* 活动列表 */
    public final static String url_schemeList = do_main + "api/schemelist.do";

    /* 充值成功 */
    public final static String url_chargeDetail = do_main + "api/cardchargedetail.do";





    /* 支付码 */
    public final static String url_getQRcode = do_main + "api/genqrcode.do";

    /* 头条广告 */
    public final static String url_getHeadLineList = do_main + "api/headlinelist.do";


    public static String getUrlData(String url){
        StringBuffer data = new StringBuffer();
        data.append(url);
//        long time = DateHelper.getSystemTime();
//        String sign = MD5Helper.encode(time + "265365" + "ldb787vbc0");
//        data.append("?time=" + time);
//        data.append("&nonce=" + "265365");
//        data.append("&sign=" + sign);
        data.append("?token=" + MyApplication.getInstance().mPref.getString("token",""));
        data.append("&ver=12");
        data.append("&appId=10001");
//        data.append("&token=" + MyApplication.getInstance().mPref.getString("userToken",""));
//        data.append("&lat=" + MyApplication.getInstance().getCoordinate().latitude);
//        data.append("&lng=" + MyApplication.getInstance().getCoordinate().longitude);
//        data.append("&appver=1-" + AppConfig.getVersionName()+"-12");
        return data.toString();
    }




}
