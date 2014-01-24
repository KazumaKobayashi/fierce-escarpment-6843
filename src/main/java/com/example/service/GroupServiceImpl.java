package com.example.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.model.Group;
import com.example.util.EscapeUtil;


/**
 * @see com.example.service.GroupService
 */
@Service
public class GroupServiceImpl implements GroupService{
	
	@Autowired
	private CoordinateService coordinateService;

	@PersistenceContext
	EntityManager em;
	
	@Transactional
	@Override
	public Group create(String name){
		name = EscapeUtil.escape(name);
		
		//グループ登録
		Group group = new Group();
		group.setGroupname(name);
		em.persist(group);
		return group;
	}

	public Group update(Integer groupId,String name){
		Group group = em.find(Group.class,groupId);
		if(group == null){
				System.out.println("Not found.");
		}
		
		if(StringUtils.isNotBlank(name)){//nullではないかつスペースのみの文字列ではない場合。　Blank　= 空白
			group.setGroupname(name);
		}
		em.persist(group);
		return group;
	}
}