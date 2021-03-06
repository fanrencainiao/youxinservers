package com.youxin.app.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mongodb.morphia.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.WriteResult;
import com.youxin.app.entity.MessageReceive;
import com.youxin.app.entity.User;
import com.youxin.app.repository.MessageReceiveRepository;
import com.youxin.app.service.MessageReceiveService;
import com.youxin.app.service.UserService;
import com.youxin.app.utils.MongoUtil;
import com.youxin.app.utils.PageResult;
import com.youxin.app.utils.StringUtil;

@Service
public class MessageReceiveServiceImpl implements MessageReceiveService {
	private static Log log=LogFactory.getLog(MessageReceiveServiceImpl.class);
	
	@Autowired
	private MessageReceiveRepository mrr;
	@Autowired
	private UserService userService;

	@Override
	public PageResult<MessageReceive> getList(String fromAccount,String to,String eventType,String convType,Integer pageSize, Integer pageNum, Long startTime, Long endTime) {
	
		Query<MessageReceive> q = mrr.createQuery();
		if(startTime>0)
			q.field("msgTimestamp").greaterThanOrEq(startTime);
		if(endTime>0)
			q.field("msgTimestamp").lessThanOrEq(endTime);
		//抄送消息类型
		if(!StringUtil.isEmpty(eventType))
			q.field("eventType").equal(eventType);
		//消息会话具体类型
		if(!StringUtil.isEmpty(convType))
			q.field("convType").equal(convType);
		if(!StringUtil.isEmpty(fromAccount))
			q.field("fromAccount").equal(fromAccount);
		if(!StringUtil.isEmpty(to))
			q.field("to").equal(to);
		q.order("-msgTimestamp");
		List<MessageReceive> mrList = q.asList(MongoUtil.pageFindOption(pageNum, pageSize));
		for(int i=0;i<mrList.size();i++) {
			String faccid = mrList.get(i).getFromAccount();
			User userFromDB = userService.getUserFromDB(faccid);
			if(userFromDB != null) 
				mrList.get(i).setIcon(userFromDB.getIcon()==null?"":userFromDB.getIcon());
			else
				mrList.get(i).setIcon("");
			
		}
		long count = q.count();
		PageResult<MessageReceive> pr=new PageResult<>(mrList, count);
		return pr;
	}
	
	@Override
	public void delMessage(String fromAccount,String to,String eventType,String convType, Long startTime, Long endTime) {
	
		Query<MessageReceive> q = mrr.createQuery();
		if(startTime>0)
			q.field("msgTimestamp").greaterThanOrEq(startTime);
		if(endTime>0)
			q.field("msgTimestamp").lessThanOrEq(endTime);
		//抄送消息类型
		if(!StringUtil.isEmpty(eventType))
			q.field("eventType").equal(eventType);
		//消息会话具体类型
		if(!StringUtil.isEmpty(convType))
			q.field("convType").equal(convType);
		if(!StringUtil.isEmpty(fromAccount))
			q.field("fromAccount").equal(fromAccount);
		if(!StringUtil.isEmpty(to))
			q.field("to").equal(to);
		mrr.deleteByQuery(q);
	}
	
	
}
