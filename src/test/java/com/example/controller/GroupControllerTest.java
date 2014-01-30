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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.jayway.jsonpath.JsonPath;

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
public class GroupControllerTest extends AbstractControllerTest {
	private Integer id;
	private String name = "Test";
	private String owner = "owner";
	
	@Before
	public void setup() throws Exception {
		super.setup();
		//グループ登録
		MvcResult result
			= mockMvc.perform(post("/groups/create")//urlへのpostリクエスト
							.param("name", name)
							.param("owner",owner))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				//TODO: Successコードと比較
				.andExpect(jsonPath("$.code").value(0))
				.andExpect(jsonPath("$.group.name").value(name))
				.andExpect(jsonPath("$.group.owner").value(owner))
				.andReturn();

		id = JsonPath.read(result.getResponse().getContentAsString(), "$.group.id");
	}

	/**
	 * グループ情報取得テスト
	 * 
	 * @throws Exception
	 */
	//条件と合致しているか(andExpect)
	@Test
	public void グループ情報を取得する() throws Exception{
		mockMvc.perform(get("/groups/" + id + "/info"))//urlへのgetリクエスト
			.andExpect(status().isOk())//statusが200(OK)になって返ってきてるか
			.andExpect(content().contentType("application/json"))//contentが指定されたcontentTypeで作成されているか
			//TODO:正しいステータスコードを設定のこと
			.andExpect(jsonPath("$.code").value(0))
			.andExpect(jsonPath("$.group.id").value(id))
			.andExpect(jsonPath("$.group.name").value(name));
	}

	/**
	 * グループ更新テスト
	 * 
	 * @throws Exception
	 */
	@Test
	public void グループ情報を更新する() throws Exception {
		mockMvc.perform(put("/groups/" + id + "/info")//urlへのputリクエスト
						.param("name",name))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			//TODO: 正しいステータスコードを設定のこと
			.andExpect(jsonPath("$.code").value(0));
	}
}

