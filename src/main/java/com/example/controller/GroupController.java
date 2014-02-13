package com.example.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.exception.GroupNotFoundException;
import com.example.jackson.Response;
import com.example.model.Group;
import com.example.model.LoginToken;
import com.example.service.GroupService;
import com.example.util.StatusCodeUtil;
/**
 * グループに関するコントローラ
 * 
 * @author Kazuma Kobayashi
 * @author Kazuki Hasegawa
 */
@RequestMapping("/groups")
@Controller
public class GroupController {
	@Autowired
	private GroupService groupService;
    /**
     * グループの情報を取得
     * 
     * @param groupId
     * @param response
     * 
     * @return
     */
	@RequestMapping(value="/{id}/info",method = RequestMethod.GET)
	public void group(@PathVariable("id") Integer groupId,HttpServletResponse response) throws IOException{
		Response res = new Response();
		try {
			Group group = groupService.getGroup(groupId);
			res.setStatusCode(StatusCodeUtil.getSuccessStatusCode());
			res.addObjects("group",group);
		} catch (GroupNotFoundException e) {
			res.setStatusCode(StatusCodeUtil.getStatusCode(e.getClass()));
			res.addErrorMessage(e.toString());
		}
		//返却する値
		response.getWriter().print(res.getResponseJson());
	}

	/**
	 *グループ情報を更新
	 *
	 *@param groupId
	 *@param response
	 *@param groupname
	 *@return 
	 *@IOException
	 */
	@RequestMapping(value="/{id}/info",method = RequestMethod.PUT)//処理が書かれているメソッドを作成してからアノテーションを書く
	public void groupUpdate(
			@PathVariable("id") Integer groupId,
			@RequestParam("name") String groupname,
			HttpServletResponse response) throws IOException {
		Response res = new Response();
		try{
			res = groupService.update(groupId,groupname);
		
		} catch(GroupNotFoundException e){
			res.setStatusCode(StatusCodeUtil.getStatusCode(e.getClass()));
		}
		//返却する値
		response.getWriter().print(res.getResponseJson());
	}

	/**
	 * グループの作成
	 * 
	 * @param groupname
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/create", method=RequestMethod.POST)
   	public void create(
   			@RequestParam("name") String groupname,
   			HttpServletRequest request,
   			HttpServletResponse response) throws IOException {
		Response res = new Response();
		LoginToken token = (LoginToken) request.getSession().getAttribute("token");
		
		res = groupService.regist(token.getUserId(),groupname);
		//レスポンスの設定
		response.getWriter().print(res.getResponseJson());	
	}
	
	/**
	 * ユーザのグループへの参加
	 * 
	 * @param userId
	 * @param groupId
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/{id}/join", method=RequestMethod.POST)
	public void join(
			@PathVariable("id") Integer id,
			HttpServletRequest request,
			HttpServletResponse response) throws IOException{
		Response res = new Response();
		LoginToken token = (LoginToken) request.getSession().getAttribute("token");

		try{
			Group group = groupService.join(token.getUserId(),id);
			res.setStatusCode(StatusCodeUtil.getSuccessStatusCode());
			res.addObjects("group",group);
		}catch(GroupNotFoundException e){
			res.setStatusCode(StatusCodeUtil.getStatusCode(e.getClass()));
			res.addErrorMessage(res.getResponseJson());
		}
		
		//レスポンスの設定
		response.getWriter().print(res.getResponseJson());
	}
}