package com.youxin.app.service.impl;

import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.youxin.app.entity.BankRecord;
import com.youxin.app.entity.Role;
import com.youxin.app.entity.User;
import com.youxin.app.entity.User.LoginLog;
import com.youxin.app.repository.UserRepository;
import com.youxin.app.service.AdminConsoleService;
import com.youxin.app.service.UserService;
import com.youxin.app.utils.DateUtil;
import com.youxin.app.utils.MongoUtil;
import com.youxin.app.utils.PageResult;
import com.youxin.app.utils.ReqUtil;
import com.youxin.app.utils.StringUtil;



@Service
public class AdminConsoleServiceImpl implements AdminConsoleService {
	@Autowired
	private UserRepository repository;
	@Autowired
	private UserService userService;
	@Autowired
	@Qualifier("get")
	private Datastore dfds;

	@Override
	public User login(String userId, String pwd) {
		
		User user = repository.findOne("_id", Integer.valueOf(userId));
		if(user!=null&&pwd.equals(user.getPassword())) {
			user.setPassword("");
			user.setPayPassword("");
			user.setLoginLog(userService.getLogin(user.getId()));
			Role role = dfds.createQuery(Role.class).field("userId").equal(user.getId())
					.field("role").equal(6).get();
			if(role!=null) 
				return user;
		}
		return null;
	}
	@Override
	public PageResult<BankRecord> getBankRecordList(String bankCard,int userId, int pageIndex, int limit) {
		PageResult<BankRecord> result = new PageResult<BankRecord>();
		Query<BankRecord> q=dfds.createQuery(BankRecord.class);
		if(!StringUtil.isEmpty(bankCard))
			q.field("bankCard").equal(bankCard);
		if(userId>0)
			q.field("userId").equal(userId);
		q.order("status,-createTime");
		result.setCount(q.count());
		result.setData(q.asList(MongoUtil.pageFindOption(pageIndex, limit)));
		return result;
	}
	
	/**
	 * 今日提现记录
	 * @return
	 */
	@Override
	public List<BankRecord> getToDayBankRecordList() {
		Query<BankRecord> q=dfds.createQuery(BankRecord.class);
		q.field("userId").equal(ReqUtil.getUserId());
		q.field("createTime").greaterThanOrEq(DateUtil.getTodayMorning().getTime()/1000);
		q.field("createTime").lessThanOrEq(DateUtil.getTodayNight().getTime()/1000);
		return q.asList();
	}

}
