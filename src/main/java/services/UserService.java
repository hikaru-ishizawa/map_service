package services;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.NoResultException;

import actions.views.UserConverter;
import actions.views.UserView;
import constants.JpaConst;
import models.User;
import models.validators.UserValidator;
import utils.EncryptUtil;

/**
 * ユーザーテーブルの操作に関わる処理を行うクラス
 */
public class UserService extends ServiceBase {

    /**
     * 指定されたページ数の一覧画面に表示するデータを取得し、UserViewのリストで返却する
     * @param page ページ数
     * @return 表示するデータのリスト
     */
    public List<UserView> getPerPage(int page) {
        List<User> users = em.createNamedQuery(JpaConst.Q_USER_GET_ALL, User.class)
                .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                .setMaxResults(JpaConst.ROW_PER_PAGE)
                .getResultList();

        return UserConverter.toViewList(users);
    }

    /**
     * ユーザーテーブルのデータの件数を取得し、返却する
     * @return ユーザーテーブルのデータの件数
     */
    public long countAll() {
        long userCount = (long) em.createNamedQuery(JpaConst.Q_USER_COUNT, Long.class)
                .getSingleResult();

        return userCount;
    }

    /**
     * ユーザー名、パスワードを条件に取得したデータをUserViewのインスタンスで返却する
     * @param name ユーザー名
     * @param plainPass パスワード文字列
     * @param pepper pepper文字列
     * @return 取得データのインスタンス 取得できない場合null
     */
    public UserView findOne(String name, String plainPass, String pepper) {
        User u = null;
        try {
            //パスワードのハッシュ化
            String pass = EncryptUtil.getPasswordEncrypt(plainPass, pepper);

            //ユーザー名とハッシュ化済パスワードを条件に未削除のユーザーを1件取得する
            u = em.createNamedQuery(JpaConst.Q_USER_GET_BY_NAME_AND_PASS, User.class)
                    .setParameter(JpaConst.JPQL_PARM_NAME, name)
                    .setParameter(JpaConst.JPQL_PARM_PASSWORD, pass)
                    .getSingleResult();

        } catch (NoResultException ex) {
        }

        return UserConverter.toView(u);

    }

    /**
     * ユーザー名を条件に取得したデータをUserViewのインスタンスで返却する
     * @param name
     * @return 取得データのインスタンス
     */
//    public UserView findOne(String name) {
//        User u = findOneInternal(name);
//        return UserConverter.toView(u);
//    }
    public UserView findOne(int id) {
        return UserConverter.toView(findOneInternal(id));
    }

    /**
     * ユーザー名を条件に該当するデータの件数を取得し、返却する
     * @param name ユーザー名
     * @return 該当するデータの件数
     */
    public long countByName(String name) {

        //指定したユーザー名を保持するユーザーの件数を取得する
        long users_count = (long) em.createNamedQuery(JpaConst.Q_USER_COUNT_REGISTERED_BY_NAME, Long.class)
                .setParameter(JpaConst.JPQL_PARM_NAME, name)
                .getSingleResult();
        return users_count;
    }

    /**
     * 画面から入力されたユーザーの登録内容を元にデータを1件作成し、ユーザーテーブルに登録する
     * @param uv 画面から入力されたユーザーの登録内容
     * @param pepper pepper文字列
     * @return バリデーションや登録処理中に発生したエラーのリスト
     */
    public List<String> create(UserView uv, String pepper) {

        //パスワードをハッシュ化して設定
        String pass = EncryptUtil.getPasswordEncrypt(uv.getPassword(), pepper);
        uv.setPassword(pass);

        //登録日時、更新日時は現在時刻を設定する
        LocalDateTime now = LocalDateTime.now();
        uv.setCreatedAt(now);
        uv.setUpdatedAt(now);

        //登録内容のバリデーションを行う
        List<String> errors = UserValidator.validate(this, uv, true, true);

        //バリデーションエラーがなければデータを登録する
        if (errors.size() == 0) {
            create(uv);
        }

        //エラーを返却（エラーがなければ0件の空リスト）
        return errors;
    }

    /**
     * 画面から入力されたユーザーの更新内容を元にデータを1件作成し、ユーザーテーブルを更新する
     * @param uv 画面から入力されたユーザーの登録内容
     * @param pepper pepper文字列
     * @return バリデーションや更新処理中に発生したエラーのリスト
     */
    public List<String> update(UserView uv, String pepper) {

        //ユーザー名を条件に登録済みのユーザー情報を取得する
        UserView savedUser = findOne(uv.getId());

        boolean validateName = false;
        if (!savedUser.getId().equals(uv.getId())) {
            //ユーザー名を更新する場合

            //ユーザー名についてのバリデーションを行う
            validateName = true;
            //変更後のユーザー名を設定する
            savedUser.setName(uv.getName());
        }

        boolean validatePass = false;
        if (uv.getPassword() != null && !uv.getPassword().equals("")) {
            //パスワードに入力がある場合

            //パスワードについてのバリデーションを行う
            validatePass = true;

            //変更後のパスワードをハッシュ化し設定する
            savedUser.setPassword(
                    EncryptUtil.getPasswordEncrypt(uv.getPassword(), pepper));
        }

        savedUser.setName(uv.getName()); //変更後の氏名を設定する
        savedUser.setAdminFlag(uv.getAdminFlag()); //変更後の管理者フラグを設定する

        //更新日時に現在時刻を設定する
        LocalDateTime today = LocalDateTime.now();
        savedUser.setUpdatedAt(today);

        //更新内容についてバリデーションを行う
        List<String> errors = UserValidator.validate(this, savedUser, validateName, validatePass);

        //バリデーションエラーがなければデータを更新する
        if (errors.size() == 0) {
            update(savedUser);
        }

        //エラーを返却（エラーがなければ0件の空リスト）
        return errors;
    }

    /**
     * ユーザー名を条件に従業員データを論理削除する
     * @param name
     */
    public void destroy(int id) {

        //idを条件に登録済みのユーザー情報を取得する
        UserView savedUser = findOne(id);

        //更新日時に現在時刻を設定する
        LocalDateTime today = LocalDateTime.now();
        savedUser.setUpdatedAt(today);

        //論理削除フラグをたてる
        savedUser.setDeleteFlag(JpaConst.USER_DEL_TRUE);

        //更新処理を行う
        update(savedUser);

    }

    /**
     * ユーザー名とパスワードを条件に検索し、データが取得できるかどうかで認証結果を返却する
     * @param name ユーザー名
     * @param plainPass パスワード
     * @param pepper pepper文字列
     * @return 認証結果を返却す(成功:true 失敗:false)
     */
    public Boolean validateLogin(String name, String plainPass, String pepper) {

        boolean isValidUser = false;
        if (name != null && !name.equals("") && plainPass != null && !plainPass.equals("")) {
            UserView uv = findOne(name, plainPass, pepper);

            if (uv != null && uv.getId() != null) {

                //データが取得できた場合、認証成功
                isValidUser = true;
            }
        }

        //認証結果を返却する
        return isValidUser;
    }

    /**
     * ユーザー名を条件にデータを1件取得し、Userのインスタンスで返却する
     * @param name
     * @return 取得データのインスタンス
     */
    private User findOneInternal(int id) {
        User u = em.find(User.class, id);

        return u;
    }

    /**
     * ユーザーデータを1件登録する
     * @param uv ユーザーデータ
     * @return 登録結果(成功:true 失敗:false)
     */
    private void create(UserView uv) {

        em.getTransaction().begin();
        em.persist(UserConverter.toModel(uv));
        em.getTransaction().commit();

    }

    /**
     * ユーザーデータを更新する
     * @param uv 画面から入力されたユーザーの登録内容
     */
    private void update(UserView uv) {

        em.getTransaction().begin();
        User u = findOneInternal(uv.getId());
        UserConverter.copyViewToModel(u, uv);
        em.getTransaction().commit();

    }

}