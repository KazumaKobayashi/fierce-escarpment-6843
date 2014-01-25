package com.example.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * groupControkkerのテスト
 * 
 * @author Kazuma Kobayashi
 */
@Transactional
@TransactionConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = "classpath:testContext.xml")
public class GroupControllerTest extends AbstractControllerTest{
	private Integer id;
	private String ID;
	private String name = "Test";
	
	@Before
	public void setup() throws Exception{
		super.setup();
		//グループ登録
		mockMvc.perform(post("/groups/create")
						.param("name",name))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			//TODO: Successコードと比較
			.andExpect(jsonPath("$.code").value(0))
			.andExpect(jsonPath("$.group.name").value(name));
		}
	/**
	 * グループ情報取得テスト
	 * 
	 * @throws Exception
	 */
	@Test
	public void グループ情報を取得する() throws Exception{
		mockMvc.perform(get("/groups/"+ID+"/info")
						.param("name",name))
				.andExpect(status().isOk())
				.andExpect(content().contentType("apprication/json"))
				//TODO:正しいステータスコードを設定のこと
				.andExpect(jsonPath("$.code").value(0));
	}
	/**
	 * グループ更新テスト
	 * 
	 * @throws Exception
	 */
	@Test
	public void グループ情報を更新する() throws Exception{
		mockMvc.perform(put("/groups/"+ID+"/info")
							.param("name",name))
					.andExpect(status().isOk())
					.andExpect(content().contentType("application/json"))
					//TODO: 正しいステータスコードを設定のこと
					.andExpect(jsonPath("$.code").value(0));
	}
	
}

