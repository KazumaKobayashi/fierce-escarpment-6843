package com.example.controller;

import java.io.IOException;

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
import com.example.service.GroupService;
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
			//TODO:正しいステータスコードを設定のこと
			res.setStatusCode(0);
			res.addObjects("group",group);
		} catch (GroupNotFoundException e) {
			//TODO:正しいエラーコードを設定のこと(Excepitonを対応したものを作成し割り当てる)
			res.setStatusCode(-1);
			res.addErrorMessage(e.toString());
		}
		//返却する値
		response.setContentType("application/json");
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
			Group group = groupService.update(groupId,groupname);
			//TODO:正しいステータスコードを設定のこと
			res.setStatusCode(0);
			res.addObjects("group",group);
		
		} catch(Exception e){
			//TODO :正しいエラーコードを設定のこと　また例外を作成する
		res.setStatusCode(-1);
		}
		//返却する値
		response.setContentType("application/json");
		response.getWriter().print(res.getResponseJson());
	}

	/**
	 * 
	 * @param groupId
	 * @param groupname
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/create", method=RequestMethod.POST)
   	public void create(
   			@RequestParam("name") String groupname,
   			HttpServletResponse response) throws IOException {
		Response res = new Response();
		try {
			Group group = groupService.create(groupname);
			//TODO:正しいステータスコードを作成し設定のこと
			res.setStatusCode(0);
			res.addObjects("group", group);
		} catch (Exception e){//例外用のデータを作成しあてはめていく
			//TODO:正しいステータスコードを作成し設定のこと
			res.setStatusCode(-1);
			res.addErrorMessage(e.toString());
		}
		//レスポンスの設定
		response.setContentType("application/json");
		response.getWriter().print(res.getResponseJson());	
	}
}
