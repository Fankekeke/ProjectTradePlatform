package com.fate.archer.common.domain;

import java.math.BigDecimal;

/**
 * 业务常量
 * @author FanK
 */
public class BusinessConstant {

    /**
     * 默认密码
     */
    public static final String DEFAULT_PASSWORD = "1234qwer";

    /**
     * 项目需要部署-金额
     */
    public static final BigDecimal NEED_DEPLOY_PRICE = new BigDecimal(30);

    /**
     * 项目定制定金比率（全款的百分比）
     */
    public static final BigDecimal DEPOSIT_RATIO = new BigDecimal(30);

    /**
     * 项目定制状态-未审核
     */
    public static final Integer CUSTOM_STATUS_UNREVIEWED = 0;

    /**
     * 项目定制状态-沟通中
     */
    public static final Integer CUSTOM_STATUS_CONVEY = 1;

    /**
     * 项目定制状态-已接单
     */
    public static final Integer CUSTOM_STATUS_RECEIVED = 2;

    /**
     * 项目定制状态-已完成
     */
    public static final Integer CUSTOM_STATUS_COMPLETED = 3;

    /**
     * 项目定制状态-部署完成
     */
    public static final Integer CUSTOM_STATUS_DEPLOYMENT = 4;

    /**
     * 项目定制状态-项目审核完成
     */
    public static final Integer CUSTOM_STATUS_REVIEW_COMPLETED = 5;

    /**
     * 项目定制是否需要论文（需要）
     */
    public static final Integer CUSTOMIZATION_THESIS_YES = 1;

    /**
     * 文件存放地址 /usr/project/pro_cos/
     */
    public static final String FILEADDRESS = "G:/Project/项目交易平台/";

    /**
     * 附件存放地址
     */
    public static final String ANNEXADDRESS = "G:/Project/项目交易平台/db/annex/";

    /**
     * 消息提示-通知
     */
    public static final Integer NOTIFICATION = 1;

    /**
     * 消息提示-公告
     */
    public static final Integer BULLETIN = 2;

    /**
     * 消息提示-任务
     */
    public static final Integer TASK = 3;

    /**
     * 附件审核状态-正在审核
     */
    public static final Integer ANNEX_AUDIT_STATUS_UNDER_REVIEW = 1;

    /**
     * 附件审核状态-审核通过
     */
    public static final Integer ANNEX_AUDIT_STATUS_PASS = 2;

    /**
     * 附件审核状态-审核不通过
     */
    public static final Integer ANNEX_AUDIT_STATUS_REJECT = 3;

    /**
     * 附件启用状态-禁用
     */
    public static final Integer ANNEX_ENABLE_STATUS_NO = 0;

    /**
     * 附件启用状态-启用
     */
    public static final Integer ANNEX_ENABLE_STATUS_YES = 1;

    /**
     * 管理员角色Id
     */
    public static final Integer ROLE_ADMIN_ID = 74;

    /**
     * 手机短信发送
     */
    public static final Integer SEND_TYPE_SMS = 1;

    /**
     * 邮件发送
     */
    public static final Integer SEND_TYPE_EMAIL = 2;

    /**
     * 成品订单状态-已取消
     */
    public static final Integer FINISHED_ORDER_STATUS_CANCELLED = -1;

    /**
     * 成品订单状态-待支付
     */
    public static final Integer FINISHED_ORDER_STATUS_TO_PAID = 0;

    /**
     * 成品订单状态-已支付
     */
    public static final Integer FINISHED_ORDER_STATUS_PAID = 1;

    /**
     * 用户邀请人状态-是
     */
    public static final Integer USER_TYPE_INVITE_YES = 1;

    /**
     * 用户邀请人状态-否
     */
    public static final Integer USER_TYPE_INVITE_NO = 0;

    /**
     * 分配比率标识-定制
     */
    public static final Integer RATE_FLAG_CUSTOM = 1;

    /**
     * 分配比率标识-成品
     */
    public static final Integer RATE_FLAG_FINISHED = 2;

    /**
     * 角色标识-平台
     */
    public static final String ROLE_SYSTEM_FLAG = "1";

    /**
     * 角色标识-开发者
     */
    public static final String ROLE_DEVELOPER_FLAG = "2";

    /**
     * 角色标识-邀请人
     */
    public static final String ROLE_INVITE_FLAG = "3";

    /**
     * 提现申请-审核中
     */
    public static final Integer TAKE_MONEY_REVIEW_UNDER = 1;

    /**
     * 提现申请-正在交易
     */
    public static final Integer TAKE_MONEY_REVIEW_TRADING = 2;

    /**
     * 提现申请-审核完成
     */
    public static final Integer TAKE_MONEY_REVIEW_COMPLETE = 3;

    /**
     * 提现金额标准
     */
    public static final BigDecimal TASK_MONEY_STANDARD = new BigDecimal("50");
}
