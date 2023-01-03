package constants;

/**
 * 各出力メッセージを定義するEnumクラス
 *
 */
public enum MessageConst {

    //認証
    I_LOGINED("ログインしました"),
    E_LOGINED("ログインに失敗しました。"),
    I_LOGOUT("ログアウトしました。"),

    //DB更新
    I_REGISTERED("登録が完了しました。"),
    I_UPDATED("更新が完了しました。"),
    I_DELETED("削除が完了しました。"),
    I_FOLLOW("フォロしました。"),

    // WebサービスAPI実行
    E_NODATA("該当データがありません。"),

    //バリデーション
    E_NONAME("ユーザー名を入力してください。"),
    E_NOPASSWORD("パスワードを入力してください。"),
    E_USER_NAME_EXIST("入力されたユーザー名は既に存在しています。"),
    B_BOOKMARK_EXIST("ブックマーク済みです"),
    ;


    /**
     * 文字列
     */
    private final String text;

    /**
     * コンストラクタ
     */
    private MessageConst(final String text) {
        this.text = text;
    }

    /**
     * 値(文字列)取得
     */
    public String getMessage() {
        return this.text;
    }
}