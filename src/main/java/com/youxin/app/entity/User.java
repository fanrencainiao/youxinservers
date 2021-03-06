package com.youxin.app.entity;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.NotSaved;
import org.mongodb.morphia.utils.IndexDirection;

import com.alibaba.fastjson.JSONObject;
import com.youxin.app.entity.exam.BaseExample;
import com.youxin.app.entity.exam.UserExample;
import com.youxin.app.utils.CollectionUtil;
import com.youxin.app.utils.DateUtil;
import com.youxin.app.utils.StringUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户
 * 
 * @author cf
 *
 */
@Entity(value = "user", noClassnameStored = true)
@Getter
@Setter
@ApiModel(value = "用户")
public class User extends BaseExample{

	@Id
	@ApiModelProperty(hidden = true)
	private Integer id;
	/**
	 * 用户帐号，最大长度32字符，必须保证一个APP内唯一
	 */
	@Indexed(unique = true)
	@ApiModelProperty(hidden = true)
	private String accid;
	/**
	 * 用户昵称，最大长度64字符，可设置为空字符串
	 */
	@ApiModelProperty(value = "用户昵称，最大长度64字符，可设置为空字符串", required = true)
	private String name;
	/**
	 * json属性，第三方可选填，最大长度1024字符
	 */
	private String props;
	/**
	 * 用户头像，最大长度1024字节，可设置为空字符串
	 */
	@ApiModelProperty(value = "用户头像，最大长度1024字节，可设置为空字符串")
	private String icon;
	/**
	 * 网易云通信ID可以指定登录token值， 最大长度128字符，并更新，如果未指定， 会自动生成token，并在创建成功后返回
	 */
	@ApiModelProperty(hidden = true)
	@Indexed(unique = true)
	private String token;
	/**
	 * 用户签名，最大长度256字符，可设置为空字符串
	 */
	@ApiModelProperty(value = " 用户签名，最大长度256字符，可设置为空字符串")
	private String sign;
	/**
	 * 用户email，最大长度64字符，可设置为空字符串
	 */
	@ApiModelProperty(value = "用户email，最大长度64字符，可设置为空字符串")
	private String email;
	/**
	 * 用户生日，最大长度16字符，可设置为空字符串
	 */
	@ApiModelProperty(value = "用户生日，最大长度16字符，可设置为空字符串")
	private String birth;
	/**
	 * 用户mobile，最大长度32字符，非中国大陆手机号码需要填写国家代码(如美国：+1-xxxxxxxxxx)或地区代码(如香港：+852-xxxxxxxx)，可设置为空字符串
	 */
	@ApiModelProperty(value = "(解密显示：（n-7）/3)用户mobile，最大长度32字符，非中国大陆手机号码需要填写国家代码(如美国：+1-xxxxxxxxxx)或地区代码(如香港：+852-xxxxxxxx)，可设置为空字符串")
	@Indexed(unique = true)
	private String mobile;
	/**
	 * 用户性别，0表示未知，1表示男，2女表示女，其它会报参数错误
	 */
	@ApiModelProperty(value = "用户性别，0表示未知，1表示男，2女表示女，其它会报参数错误")
	private int gender;
	/**
	 * 用户名片扩展字段，最大长度1024字符，用户可自行扩展，建议封装成JSON字符串，也可以设置为空字符串
	 */
	@ApiModelProperty(value = "用户名片扩展字段，最大长度1024字符，用户可自行扩展，建议封装成JSON字符串，也可以设置为空字符串")
	private String ex;

	// ============================================================================================
	// ==============================扩展字段=============================================
	// ============================================================================================
	@ApiModelProperty(value = "登录类型 0：账号密码登录，1：短信验证码登录，2.友信号密码登录", required = true)
	private int loginType;// 登录类型 0：账号密码登录，1：短信验证码登录，2.友信号密码登录
	@ApiModelProperty(value = "密码", required = true)
	private String password;
	@ApiModelProperty(value = "创建时间", hidden = true)
	private Long createTime;
	@ApiModelProperty(value = "更新时间", hidden = true)
	private Long updateTime;
	@ApiModelProperty(value = "注册ip地址", hidden = true)
	private String ip;
	
	@ApiModelProperty(value = "角色", hidden = true)
	@NotSaved
	private List<Integer> role;//
	@ApiModelProperty(value = "短信验证码")
	@NotSaved
	private String smsCode;
	@ApiModelProperty(value = "第三方登录标识")
	@NotSaved
	private String loginInfo;
	@ApiModelProperty(value = "sdk类型：1qq,2微信")
	@NotSaved
	private int sdkType;
	
	@ApiModelProperty(value = "用户总余额")
	private Double balance = 0.0; // 用户余额
	@ApiModelProperty(value = "充值总金额")
	private Double totalRecharge = 0.0;// 充值总金额
	@ApiModelProperty(value = "消费总金额")
	private Double totalConsume = 0.0;// 消费总金额
	@ApiModelProperty(value = "支付密码")
	private String payPassword;

	@ApiModelProperty(value = "支付宝买家id")
	private String aliUserId;
	@ApiModelProperty(value = "支付宝买家token,非authToken")
	private String aliAppAuthToken;
	@ApiModelProperty(value = "友讯号")
	@Indexed(unique = true)
	private String account;
	@ApiModelProperty(value = "禁用用户（-1禁用 1解禁）")
	private int disableUser = 1;
	@ApiModelProperty(value = "禁用用户 标签（原因）")
	private String disableUserSign;
	@ApiModelProperty(value = "用户类型 0普通用户(默认)，2公众号,6管理员")
	private int userType = 0;
	@ApiModelProperty(value = "身份二维码标识，用于刷新二维码")
	private String codeSign = "code";
	@ApiModelProperty(value = "地理位置")
	@Indexed(value = IndexDirection.GEO2D)
	private Loc loc;
	@ApiModelProperty(value = "用户在线状态")
	private String online;
	@ApiModelProperty(value = "用户是否注销 1注销 -1未注销")
	private int isDelUser=-1;
	
	

	public String setExs() {
		JSONObject exs = new JSONObject();
//		if (!StringUtil.isEmpty(this.password))
//			exs.put("password", this.password);
		if (!StringUtil.isEmpty(this.account))
			exs.put("account", this.account);
//		if (!StringUtil.isEmpty(this.payPassword))
//			exs.put("payPassword", this.payPassword);
		if (!CollectionUtil.isEmpty(this.role))
			exs.put("role", this.role);
		exs.put("disableUser", this.disableUser);
		exs.put("createTime", this.createTime);
		exs.put("updateTime", this.updateTime);
		exs.put("id", this.id);
		this.ex = exs.toJSONString();
		return ex;
	}

	/**
	 * 用户设置
	 */
	@ApiModelProperty(value = "用户设置")
	private UserSettings settings;

	/**
	 * 用户登录日志
	 */
	@ApiModelProperty(value = "用户登录日志")
	private @NotSaved LoginLog loginLog=new LoginLog();// 登录日志

	@Data
	public static class UserSettings {
		@ApiModelProperty(value = "可根据手机号搜索（0否 1是）默认1")
		private int searchByMobile=1;
		@ApiModelProperty(value = "可根据友讯号搜索（0否 1是）默认1")
		private int searchByAccount=1;
		@ApiModelProperty(value = "允许添加方式（0所有方式，1手机号，2友讯号，3二维码）默认0")
		private int addType=0;
		@ApiModelProperty(value = "加好友是否需要验证（0不需要，1需要）默认1")
		private int isInvide=1;
		@ApiModelProperty(value = "消息加密传输（0不需要，1需要）默认0")
		private int isCodeMsg=0;
		@ApiModelProperty(value = "是否震动（0不需要，1需要）默认0")
		private int isActive=0;
		@ApiModelProperty(value = "是否向我推荐通讯录好友（0不需要，1需要）默认1")
		private int pushLocalFriends=1;
		@ApiModelProperty(value = "是否允许添加我（0不允许，1允许）默认1")
		private int isAllowAddMy=1;
		@ApiModelProperty(value = "是否已读回执（0不允许，1允许）默认1")
		private int isRead=1;
		@ApiModelProperty(value = "是否显示输入状态（0不允许，1允许）默认1")
		private int isShowInputType=1;
	}

	@Data
	public static class DeviceInfo {
		@ApiModelProperty(value = "登录时间")
		private long loginTime;
		/**
		 * 设备号 android ios web
		 */
		@ApiModelProperty(value = "设备号 android ios web")
		private String deviceKey;
		@ApiModelProperty(value = "地区标识 例 CN HK")
		private String adress;// 地区标识 例 CN HK
		@ApiModelProperty(value = "在线状态 1在线")
		private int online;// 在线状态
		@ApiModelProperty(value = "下线时间") 
		private long offlineTime;

	}

	@Data
	public static class LoginLog {

		private int isFirstLogin;
		private long loginTime;
		private String apiVersion;
		private String osVersion;
		private String model;
		private String serial;
		private double latitude;
		private double longitude;
		private String location;
		private String address;

		private long offlineTime;
	}
	/**
	 * 我保存的群
	 * @author cf
	 * @date 2020年2月6日 上午10:37:32
	 */
	@Entity(value = "myTeam", noClassnameStored = true)
	@Data
	public static class MyTeam {

		@Id
		private Integer userId;
		
		private Set<Long> teams;
		
	}
	/**
	 * 我的好友添加方式
	 * @author cf
	 * @date 2020年2月14日 上午10:44:39
	 */
	@Entity(value = "myFreids", noClassnameStored = true)
	@Data
	public static class MyFreids {

		@Id
		private ObjectId id;
		@ApiModelProperty("用户id")
		private Integer userId;
		@ApiModelProperty("好友id")
		private Integer toUserId;
		@ApiModelProperty("添加方式：1二维码添加对方-1对方二维码添加我，2搜索手机号-2，3有讯号-3，4群聊-4，5名片分享-5，6附近的人-6")
		private int addType;
		
	}
	/**
	 * 群二维码
	 * @author cf
	 * @date 2020年2月14日 下午12:03:11
	 */
	@Entity(value = "myItemCode", noClassnameStored = true)
	@Data
	public static class MyItemCode {

		@Id
		@ApiModelProperty("群id")
		private Long teamId;
		@ApiModelProperty("群二维码")
		private String erCode;
		
	}

	@Entity(value = "userLoginLog", noClassnameStored = true)
	@Data
	public static class UserLoginLog {

		@Id
		private Integer userId;
		/**
		 * 登陆日志信息
		 */
		@Embedded
		private LoginLog loginLog;

		/**
		 * 登陆设备列表 web DeviceInfo android DeviceInfo ios DeviceInfo
		 */
		private Map<String, DeviceInfo> deviceMap;

		public UserLoginLog() {
			super();
		}

		public static LoginLog init(UserExample example, boolean isFirst) {
			LoginLog info = new LoginLog();
			info.setIsFirstLogin(isFirst ? 1 : 0);
			info.setLoginTime(DateUtil.currentTimeSeconds());
			info.setApiVersion(example.getApiVersion());
			info.setOsVersion(example.getOsVersion());

			info.setModel(example.getModel());
			info.setSerial(example.getSerial());
			info.setLatitude(example.getLatitude());
			info.setLongitude(example.getLongitude());
			info.setLocation(example.getLocation());
			info.setAddress(example.getAddress());
			info.setOfflineTime(0);

			return info;
		}

	}
	
	/**
	 * 用户银行卡
	 */
	@Data
	@Entity(value = "myCard", noClassnameStored = true)
	public static class MyCard {
		@Id
		private ObjectId id;
		@ApiModelProperty(value = "用户id")
		private Integer userId;

		@ApiModelProperty(value = "银行卡号")
		private String bankCard;
		@ApiModelProperty(value = "身份证")
		private String idCard;
		@ApiModelProperty(value = "实名")
		private String name;
		@ApiModelProperty(value = "银行预留手机号")
		private String phone;
		@ApiModelProperty(value = "银行卡名称")
		private String bankName;
		@ApiModelProperty(value = "银行卡状态0删除，1正常")
		private Integer state;
		@ApiModelProperty(value = "未知属性")
		private String type;
		
		private Long createTime;
		private Long updateTime;
	}
	/**
	 * 坐标
	 *
	 */
	public static class Loc {
		public Loc() {
			super();
		}

		public Loc(double lng, double lat) {
			super();
			this.lng = lng;
			this.lat = lat;
		}
		@ApiModelProperty(value = "经度")
		private double lng;// longitude 
		@ApiModelProperty(value = "纬度")
		private double lat;// latitude 

		public double getLng() {
			return lng;
		}

		public void setLng(double lng) {
			this.lng = lng;
		}

		public double getLat() {
			return lat;
		}

		public void setLat(double lat) {
			this.lat = lat;
		}

	}

}
