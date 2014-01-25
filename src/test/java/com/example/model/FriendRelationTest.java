package com.example.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * FriendRelationのテスト
 *
 * @author Kazuki Hasegawa
 * @see com.example.model.FriendRelation
 */
@RunWith(JUnit4.class)
public class FriendRelationTest {
	@Test
	public void 許可されていない状態でUser2を取得する() {
		FriendRelation relation = new FriendRelation();
		User user = new User();
		// 許可されていない状態
		relation.setAllowed(false);
		relation.setUser2(user);

		// 取得する
		assertThat(relation.getUser2(), is(nullValue()));
	}
}
