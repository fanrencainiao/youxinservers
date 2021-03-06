package com.youxin.app.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.DBCollection;
import com.youxin.app.entity.Role;
import com.youxin.app.entity.User;
import com.youxin.app.ex.ServiceException;
import com.youxin.app.service.ConfigService;
import com.youxin.app.service.UserService;
import com.youxin.app.utils.supper.Callback;
import com.youxin.app.yx.SDKService;

/**
 * @version:（1.0）
 * @ClassName InitializationData
 * @Description: （初始化数据）
 */
@Component
public class InitializationData implements CommandLineRunner {
	protected Log log = LogFactory.getLog(InitializationData.class);

//	@Value("classpath:data/message.json")
//	private Resource resource;

	@Autowired
	@Qualifier("get")
	private Datastore dfds;

//	@Autowired
//	private UserService userService;
	@Autowired
	private ConfigService cs;

	@Override
	public void run(String... args) throws Exception {

		initSuperAdminData();
		//启动修改用户在线状态
		initOnline();
//		initErrorMassageData();

	}

	/**
	 * 初始化异常信息数据
	 * 
	 * @throws Exception
	 */
//	private void initErrorMassageData() throws Exception{
//		if(null==resource) {
//			System.out.println("error initErrorMassageData  resource is null");
//			return;
//		}
//		DBCollection errMsgCollection = getDatastore().getCollection(ErrorMessage.class);
//		
//		if(errMsgCollection == null || errMsgCollection.count()==0) {
//			
//			BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
//			StringBuffer message = new StringBuffer();
//			String line = null;
//			while ((line = br.readLine()) != null) {
//				message.append(line);
//			}
//			String defaultString = message.toString();
//			if(!StringUtil.isEmpty(defaultString)){
//				List<ErrorMessage> errorMessages = JSONObject.parseArray(defaultString, ErrorMessage.class);
//				errorMessages.forEach(errorMessage ->{
//					getDatastore().save(errorMessage);
//				});
//				
//			}
//			log.info("\n"+">>>>>>>>>>>>>>> 异常信息数据初始化完成  <<<<<<<<<<<<<");
//		}
//	}

	/**
	 * 初始化默认超级管理员数据
	 */
	private void initSuperAdminData() {

		DBCollection adminCollection = dfds.getCollection(Role.class);
		if (adminCollection == null || adminCollection.count() == 0) {
			try {
				User user = new User();
				user.setId(1000);
				user.setName("1000");
				user.setPassword(DigestUtils.md5Hex("1000"));
				user.setCreateTime(DateUtil.currentTimeSeconds());
				user = createAccid(user, "1000");
				user.setUserType(6);// 后台管理员
				dfds.save(user);
//				KXMPPServiceImpl.getInstance().registerAndXmppVersion(user.getUserId() + "", user.getPassword());
			} catch (Exception e) {
				e.printStackTrace();
			}
			Role role = new Role(1000, "1000", (byte) 6, (byte) 1, 0);
			dfds.save(role);

			// 初始化10000号
			try {
				User u = new User();
				u.setId(10000);
				u.setName("10000");
				u.setPassword(DigestUtils.md5Hex("10000"));
				u.setCreateTime(DateUtil.currentTimeSeconds());
				u = createAccid(u, "10000");
				u.setUserType(2);// 公众号-客服小助手
				dfds.save(u);
				Role role1 = new Role(10000, "10000", (byte) 2, (byte) 1, 0);
				dfds.save(role1);
//				KXMPPServiceImpl.getInstance().registerSystemNo("10000", DigestUtils.md5Hex("10000"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			log.info("\n" + ">>>>>>>>>>>>>>> 默认管理员数据初始化完成  <<<<<<<<<<<<<");
		}

		Query<User> query = dfds.createQuery(User.class);
		query.field("_id").equal(1100);
		if (query.get() == null) {
			// 初始化1100号 作为金钱相关通知系统号码
			try {
				User u = new User();
				u.setId(1100);
				u.setName("1100");
				u.setPassword(DigestUtils.md5Hex("1100"));
				u.setCreateTime(DateUtil.currentTimeSeconds());
				u = createAccid(u, "1100");
				u.setUserType(2);// 公众号-支付通知
				dfds.save(u);
				// 初始化config配置
				cs.initConfig();
//				KXMPPServiceImpl.getInstance().registerSystemNo("1100", DigestUtils.md5Hex("1100"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			log.info("\n" + ">>>>>>>>>>>>>>> 默认系统通知数据初始化完成  <<<<<<<<<<<<<");
		}

	}

	/**
	 * 初始化默认全部离线
	 */
	private void initOnline() {
		try {
			log.info("\n" + ">>>>>>>>>>>>>>> 更新用户在线状态中  <<<<<<<<<<<<<");
			DBCollection userCollection = dfds.getCollection(User.class);
			if (userCollection != null && userCollection.count() > 0) {

				Query<User> q = dfds.createQuery(User.class);
				UpdateOperations<User> ops = dfds.createUpdateOperations(User.class);
				ops.set("online",0);
				UpdateResults update = dfds.update(q, ops);
				System.out.println(update.getUpdatedCount());
				//清除用户信息
				ThreadUtil.executeInThread(new Callback() {
					@Override
					public void execute(Object obj) {
						q.filter("_id >", 10000000).forEach(u->KSessionUtil.deleteUserByUserId(u.getId()));
					}
				});
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("\n" + ">>>>>>>>>>>>>>> 更新用户在线状态失败  <<<<<<<<<<<<<");
		}

	}
	

	private User createAccid(User user, String userId) {
		String accid = Md5Util.md5HexToAccid(userId);
		user.setAccid(accid);
		user.setExs();
		com.youxin.app.yx.request.User.User u = new com.youxin.app.yx.request.User.User();
		// sdk注册
		BeanUtils.copyProperties(user, u);
		JSONObject json = SDKService.createUser(u);
		User us = JSONObject.toJavaObject(json.getJSONObject("info"), User.class);
		// 注册成功
		user.setToken(us.getToken());
		if (StringUtil.isEmpty(user.getToken())) {
			System.out.println("token缺失，注册失败");
		}
		return user;
	}

}
