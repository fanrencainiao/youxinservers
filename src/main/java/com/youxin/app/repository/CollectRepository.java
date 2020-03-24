package com.youxin.app.repository;



import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.youxin.app.entity.UserCollect;


@Repository
public class CollectRepository  extends BasicDAO<UserCollect, ObjectId>{
	
	@Autowired
	public CollectRepository(@Qualifier("get") Datastore ds) {
		super(ds);
	}
	

}
