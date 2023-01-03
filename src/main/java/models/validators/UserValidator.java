package models.validators;

import java.util.ArrayList;
import java.util.List;

import actions.views.UserView;
import constants.MessageConst;
import services.UserService;

/**
 * ユーザーインスタンスに設定されている値のバリデーションを行うクラス
 *
 */
public class UserValidator {

    /**
     * ユーザーインスタンスの各項目についてバリデーションを行う
     * @param service 呼び出し元Serviceクラスのインスタンス
     * @param uv UserViewのインスタンス
     * @param nameDuplicateCheckFlag ユーザー名の重複チェックを実施するかどうか(実施する:true 実施しない:false)
     * @param passwordCheckFlag パスワードの入力チェックを実施するかどうか(実施する:true 実施しない:false)
     * @return エラーのリスト
     */
    public static List<String> validate(
            UserService service, UserView uv, Boolean nameDuplicateCheckFlag, Boolean passwordCheckFlag) {
        List<String> errors = new ArrayList<String>();

        //ユーザー名のチェック
        String nameError = validateName(service, uv.getName(), nameDuplicateCheckFlag);
        if (!nameError.equals("")) {
            errors.add(nameError);
        }

        //パスワードのチェック
        String passError = validatePassword(uv.getPassword(), passwordCheckFlag);
        if (!passError.equals("")) {
            errors.add(passError);
        }

        return errors;
    }

    /**
     * ユーザー名の入力チェックを行い、エラーメッセージを返却
     * @param service UserServiceのインスタンス
     * @param name ユーザー名
     * @param nameDuplicateCheckFlag ユーザー名の重複チェックを実施するかどうか(実施する:true 実施しない:false)
     * @return エラーメッセージ
     */
    private static String validateName(UserService service, String userName, Boolean nameDuplicateCheckFlag) {

        //入力値がなければエラーメッセージを返却
        if (userName == null || userName.equals("")) {
            return MessageConst.E_NONAME.getMessage();
        }

        if (nameDuplicateCheckFlag) {
            //ユーザー名の重複チェックを実施

            long usersCount = isDuplicateUser(service, userName);

            //同一ユーザー名が既に登録されている場合はエラーメッセージを返却
            if (usersCount > 0) {
                return MessageConst.E_USER_NAME_EXIST.getMessage();
            }
        }

        //エラーがない場合は空文字を返却
        return "";
    }

    /**
     * @param service UserServiceのインスタンス
     * @param name ユーザー名
     * @return ユーザーテーブルに登録されている同一ユーザー名のデータの件数
     */
    private static long isDuplicateUser(UserService service, String name) {

        long usersCount = service.countByName(name);
        return usersCount;
    }

    /**
     * パスワードの入力チェックを行い、エラーメッセージを返却
     * @param password パスワード
     * @param passwordCheckFlag パスワードの入力チェックを実施するかどうか(実施する:true 実施しない:false)
     * @return エラーメッセージ
     */
    private static String validatePassword(String password, Boolean passwordCheckFlag) {

        //入力チェックを実施 かつ 入力値がなければエラーメッセージを返却
        if (passwordCheckFlag && (password == null || password.equals(""))) {
            return MessageConst.E_NOPASSWORD.getMessage();
        }

        //エラーがない場合は空文字を返却
        return "";
    }
}