package com.example.model;

import static org.hamcrest.CoreMatchers.*;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.jayway.jsonassert.JsonAssert;


/**
 * グループモデルのテスト
 * 
 * @author Kazuma Kobayashi
 *
 */
@RunWith(JUnit4.class)
public class GroupTest extends AbstractModelTest {
	private String name =GroupTest.class.getName();
	
	@Test
	public void testWriteValueAsString() throws IOException {
	//必要なデータを生成
	Group group = new Group();
	Integer id = 12345;
	group.setId(id);
	group.setGroupname(name);
	
	String json = mapper.writeValueAsString(group);
	JsonAssert
		.with(json)
		.assertThat("$.id", is(id))
		.assertThat("$.name", is(name));
	}	
}
