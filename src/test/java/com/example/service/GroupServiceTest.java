package com.example.service;

import static org.hamcrest.core.Is.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.example.exception.GroupNotFoundException;
import com.example.model.Group;

/**
 * GroupServiceのテスト
 * 
 * @author KazumaKobayashi
 * @author Kazuki Hasegawa
 * @see com.example.service.GroupService
 */
@Transactional
@TransactionConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:testContext.xml")
public class GroupServiceTest {
	@Autowired
	private GroupService service;

	private String name  = GroupServiceTest.class.getName();
	private String owner = "Test";
	private Integer id;

	@Before //このアノテーションは@Testのアノテーションが実行されるたびに実行される。（@Testよりも前に）
	public void setup() {
		//グループの作成
		Group group = service.create(owner,name);
		id = group.getId();
	}

	@Test
	public void グループ情報を更新する() throws GroupNotFoundException {
		Group group = service.getGroup(id);
		assertThat(group.getGroupname(),is(name));
		
		//名前を変更する
		String groupname = "KK";
		service.update(id, groupname);

		//再取得
		group = service.getGroup(id);
		assertThat(group.getGroupname(), is(groupname));
	}
	/**
	 * 存在しないグループを更新を試みて例外が出るテスト
	 * 
	 * @throws GroupNotFoundException
	 */
	@Test(expected=GroupNotFoundException.class)
	public void 存在しないグループidを更新する() throws GroupNotFoundException{
		String groupname = "Test";
		service.getGroup(12345);
		service.update(12345,groupname);
	}
}