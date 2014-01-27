package com.example.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.example.exception.EmailExistsException;
import com.example.exception.FriendRelationExistsException;
import com.example.exception.FriendRelationNotFoundException;
import com.example.exception.InvalidEmailException;
import com.example.exception.UserExistsException;
import com.example.exception.UserNotFoundException;
import com.example.model.FriendRelation;

import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * FriendServiceのテスト　
 *
 * @author Kazuki Hasegawa
 * @see com.example.service.FriendService
 */
@Transactional
@TransactionConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:testContext.xml")
public class FriendServiceTest {
	@Autowired
	private FriendService service;
	@Autowired
	private UserService userService;

	private String id1 = FriendServiceTest.class.getName() + "1";
	private String id2 = FriendServiceTest.class.getName() + "2";
	private String email1 = "example1@example.com";
	private String email2 = "example2@example.com";
	private String password = "kazumakobayashi";

	@Before
	public void setup() throws UserExistsException, InvalidEmailException, EmailExistsException, FriendRelationExistsException, UserNotFoundException {
		// ユーザの作成
		userService.create(id1, email1, password);
		userService.create(id2, email2, password);
		// フレンド申請
		service.create(id1, id2);
	}

	/**
	 * フレンド関係取得テスト
	 * この段階では、許可されていないので、User2を取得するとnullが帰ってくるはず
	 *
	 * @throws FriendRelationNotFoundException
	 */
	@Test
	public void フレンド関係を取得する() {
		FriendRelation relation = service.getFriendRelation(id1, id2);
		assertThat(relation, is(notNullValue()));
		assertThat(relation.getPk(), is(notNullValue()));
		assertThat(relation.getPk().getId1(), is(id1));
		assertThat(relation.getPk().getId2(), is(id2));
	}

	/**
	 * idが逆でも取得出来ることをチェックする
	 *
	 * @throws FriendRelationNotFoundException
	 */
	@Test
	public void 引数に渡すidを交換してフレンド関係を取得する() {
		FriendRelation relation = service.getFriendRelation(id2, id1);
		assertThat(relation, is(notNullValue()));
		assertThat(relation.getPk(), is(notNullValue()));
		assertThat(relation.getPk().getId1(), is(id1));
		assertThat(relation.getPk().getId2(), is(id2));
	}

	/**
	 * すでに申請されているユーザが申請するテスト
	 * 挙動は、既存のリレーションを許可するようにする
	 *
	 * @throws FriendRelationExistsException
	 * @throws UserNotFoundException
	 * @throws FriendRelationNotFoundException
	 */
	@Test
	public void id2のユーザがid1にフレンド申請をする() throws FriendRelationExistsException, UserNotFoundException, FriendRelationNotFoundException {
		// 取得
		FriendRelation relation = service.getFriendRelation(id1, id2);
		assertThat(relation, is(notNullValue()));
		assertThat(relation.getPk().getId1(), is(id1));
		assertThat(relation.getPk().getId2(), is(id2));
		assertThat(relation.isAllowed(), is(false));

		// 申請する
		// すでにid1 -> id2が存在しているため、id1 -> id2のフレンド申請が許可される動作になるはず
		relation = service.create(id2, id1);
		assertThat(relation, is(notNullValue()));
		assertThat(relation.getPk().getId1(), is(id1));
		assertThat(relation.getPk().getId2(), is(id2));
		assertThat(relation.isAllowed(), is(true));
	}

	/**
	 * フレンド申請を許可する
	 *
	 * @throws FriendRelationNotFoundException
	 */
	@Test
	public void id2のユーザがフレンド申請を許可する() throws FriendRelationNotFoundException {
		// 取得
		FriendRelation relation = service.getFriendRelation(id1, id2);
		assertThat(relation.getPk().getId1(), is(id1));
		assertThat(relation.getPk().getId2(), is(id2));

		// フレンド申請を許可
		service.allow(id1, id2);

		// 再取得
		relation = service.getFriendRelation(id1, id2);
		assertThat(relation.getPk().getId1(), is(id1));
		assertThat(relation.getPk().getId2(), is(id2));
	}

	/**
	 * フレンド申請を断る
	 *
	 * @throws FriendRelationNotFoundException
	 */
	@Test
	public void id2のユーザがフレンド申請を断る() throws FriendRelationNotFoundException {
		// 取得
		FriendRelation relation = service.getFriendRelation(id1, id2);
		assertThat(relation.getPk().getId1(), is(id1));
		assertThat(relation.getPk().getId2(), is(id2));

		// フレンド申請を削除する
		service.forbid(id1, id2);

		// 再取得
		relation = service.getFriendRelation(id1, id2);
		assertThat(relation, is(nullValue()));
	}


	/**
	 * すでに申請しているが、もう一度申請を試みて例外が出るテスト
	 *
	 * @throws FriendRelationExistsException
	 * @throws UserNotFoundException
	 */
	@Test(expected=FriendRelationExistsException.class)
	public void フレンド申請をもう一度する() throws FriendRelationExistsException, UserNotFoundException {
		service.create(id1, id2);
	}

	/**
	 * 存在しないユーザidでid1にフレンド申請を試みて例外が出るテスト
	 *
	 * @throws FriendRelationExistsException
	 * @throws UserNotFoundException
	 */
	@Test(expected=UserNotFoundException.class)
	public void 存在しないユーザidでid1にフレンド申請をする() throws FriendRelationExistsException, UserNotFoundException {
		String id = "kazuma";
		// 取得
		service.create(id, id1);
	}

	/**
	 * 存在しないユーザidのフレンド申請を許可を試みて例外が出るテスト
	 *
	 * @throws FriendRelationNotFoundException
	 */
	@Test(expected=FriendRelationNotFoundException.class)
	public void 存在しないユーザidのフレンド申請を許可する() throws FriendRelationNotFoundException {
		String id = "kazuma";
		// 取得
		service.allow(id, id1);
	}

	/**
	 * 存在しないユーザidのフレンド申請を却下を試みて例外が出るテスト
	 *
	 * @throws FriendRelationNotFoundException
	 */
	@Test(expected=FriendRelationNotFoundException.class)
	public void 存在しないユーザidのフレンド申請を却下する() throws FriendRelationNotFoundException {
		String id = "kazuma";
		// 取得
		service.forbid(id, id2);
	}

	/**
	 * フレンド申請がされていないユーザのフレンド申請を試みて例外が出るテスト
	 * まず、カラムが存在しないので発生しないはず
	 *
	 * @throws FriendRelationNotFoundException
	 * @throws UserExistsException
	 * @throws InvalidEmailException
	 * @throws EmailExistsException
	 */
	@Test(expected=FriendRelationNotFoundException.class)
	public void フレンド申請されていないフレンド申請をする() throws FriendRelationNotFoundException, UserExistsException, InvalidEmailException, EmailExistsException {
		String id = "kazuma", email = "kazuma@kazuma.com", password = "kazuma";
		userService.create(id, email, password);

		// 許可する
		service.allow(id, id2);
	}

	/**
	 * フレンド申請がされていないユーザのフレンド申請を却下を試みて例外が出るテスト
	 * まず、存在しないので例外が発生するはず
	 *
	 * @throws UserExistsException
	 * @throws InvalidEmailException
	 * @throws EmailExistsException
	 * @throws FriendRelationNotFoundException
	 */
	@Test(expected=FriendRelationNotFoundException.class)
	public void フレンド申請がされていないフレンド申請を却下する() throws UserExistsException, InvalidEmailException, EmailExistsException, FriendRelationNotFoundException {
		String id = "kazuma", email = "kazuma@kazuma.com", password = "kazuma";
		userService.create(id, email, password);

		// 許可する
		service.allow(id, id2);
	}
}
