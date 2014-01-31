package com.example.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.exception.GroupNotFoundException;
import com.example.model.Group;
import com.example.model.Join;
import com.example.model.JoinPK;
import com.example.util.EscapeUtil;


/**
 * GroupeServiceの実装クラス
 * 
 * @author Kazuma Kobayashi
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
	public Group create(String userId, String name){
		name = EscapeUtil.escapeSQL(name);
		// TODO: グループの存在確認

		//グループ登録
		Group group = new Group();
		group.setGroupname(name);
		group.setOwner(userId);
		em.persist(group);
		return group;
	}

	public Group update(Integer groupId,String name) throws GroupNotFoundException{
		Group group = em.find(Group.class,groupId);
		if(group == null){
			throw new GroupNotFoundException("Group not found Id:"+groupId);
				// TODO: 存在しない例外処理
		}
		
		if(StringUtils.isNotBlank(name)){//nullではないかつスペースのみの文字列ではない場合。　Blank　= 空白
			group.setGroupname(name);
		}
		em.persist(group);
		return group;
	}

	public Group join(String userId,Integer groupId) throws GroupNotFoundException{
		Group group = em.find(Group.class,groupId);
		if( group == null){
			throw new GroupNotFoundException("Group not found Id:"+groupId);
				// TODO:存在しない例外処理
		}
		
		Join join = new Join();
		JoinPK pk = new JoinPK();
		
		pk.setUserId(userId);
		pk.setGroupId(groupId);
		
		join.setPk(pk);
		em.persist(join);
		
		return group;
	}
	public Group getGroup(Integer groupId) throws GroupNotFoundException{
		if(groupId == null){
			throw new GroupNotFoundException("Group not found Id:"+groupId);
			// TODO: 存在しない例外処理
		}
		return em.find(Group.class, groupId);
	}
}