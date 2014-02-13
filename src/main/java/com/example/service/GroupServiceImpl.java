package com.example.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.exception.GroupNotFoundException;
import com.example.jackson.Response;
import com.example.model.Group;
import com.example.model.Join;
import com.example.model.JoinPK;
import com.example.util.EscapeUtil;
import com.example.util.StatusCodeUtil;


/**
 * GroupeServiceの実装クラス
 * 
 * @author Kazuma Kobayashi
 * @see com.example.service.GroupService
 */
@Service
public class GroupServiceImpl implements GroupService{
	private static final Logger logger = LoggerFactory.getLogger(GroupServiceImpl.class);

	@Autowired
	private CoordinateService coordinateService;

	@PersistenceContext
	EntityManager em;
	
	@Transactional
	@Override
	public Group create(String userId, String name){
		name = EscapeUtil.escapeSQL(name);
		//グループ登録
		Group group = new Group();
		group.setGroupname(name);
		group.setOwner(userId);
		em.persist(group);
		return group;
	}

	@Transactional
	public Response update(Integer groupId,String name) throws GroupNotFoundException{
			Group group = em.find(Group.class,groupId);
			Response response = new Response();
			if(group == null){
				logger.error("Group not found Id: {}", groupId);
				throw new GroupNotFoundException("Group not found Id: " + groupId);
			}
			
			if(StringUtils.isNotBlank(name)){//nullではないかつスペースのみの文字列ではない場合。　Blank　= 空白
				if(name.length() < Group.GROUP_NAME_MAX_LENGTH){
					group.setGroupname(name);
			}
			}else{
				response.addErrorMessage(String.format("グループ名は,%d文字以内で入力してください.",Group.GROUP_NAME_MAX_LENGTH));
			}
			em.persist(group);
			response.setStatusCode(StatusCodeUtil.getSuccessStatusCode());
			return response;
	}

	@Transactional
	public Group join(String userId, Integer groupId) throws GroupNotFoundException{
		Group group = em.find(Group.class,groupId);
		if(group == null){
			logger.error("Group not found Id: {}", groupId);
			throw new GroupNotFoundException("Group not found Id:" + groupId);
		}
		
		Join join = new Join();
		JoinPK pk = new JoinPK();		
		pk.setUserId(userId);
		pk.setGroupId(groupId);
		join.setDisplayFlag(true);
		join.setPk(pk);
		
		em.persist(join);
		
		return group;
	}
	
	public Group getGroup(Integer groupId) throws GroupNotFoundException {
		if(groupId == null){
			logger.error("Group not found Id: {}", groupId);
			throw new GroupNotFoundException("Group not found Id:"+groupId);
		}
		return em.find(Group.class, groupId);
	}
	
	public Response regist(String userId,String name){
		Response response = new Response();
		if (StringUtils.isBlank(name)){
			response.addErrorMessage("グループネームを入力してください");
		}else if(name.length() > Group.GROUP_NAME_MAX_LENGTH){
			response.addErrorMessage(String.format("グループネームは最大%s文字です。それ以下の数字で入力してください。",Group.GROUP_NAME_MAX_LENGTH));
		}else{
			response.setStatusCode(StatusCodeUtil.getSuccessStatusCode());
			response.addObjects("group",create(userId,name));
		}
			return response;
	}
}
