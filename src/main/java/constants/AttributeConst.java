package constants;

/**
 * 画面の項目値等を定義するEnumクラス
 *
 */
public enum AttributeConst {

    //フラッシュメッセージ
    FLUSH("flush"),

    //一覧画面共通
    MAX_ROW("maxRow"),
    PAGE("page"),

    //入力フォーム共通
    TOKEN("_token"),
    ERR("errors"),

    //ログイン中のユーザー
    LOGIN_USER("login_user"),

    //ログイン画面
    LOGIN_ERR("loginError"),

    //ユーザー管理
    USER("user"),
    USERS("users"),
    USER_COUNT("users_count"),
    USER_ID("id"),
    USER_PASS("password"),
    USER_NAME("name"),
    USER_ADMIN_FLG("admin_flag"),

    //ブックマーク関係
    BOOKMARK("bookmark"),
    BOOKMARKS("bookmarks"),
    BOOKMARK_COUNT("bookmarks_count"),
    BOOKMARK_REGISTER_DATE("bookmark_register_date"),
    BOOKMARK_ID("id"),

    //管理者フラグ
    ROLE_ADMIN(1),
    ROLE_GENERAL(0),

    //削除フラグ
    DEL_FLAG_TRUE(1),
    DEL_FLAG_FALSE(0),

    //半径の初期値
    RADIUS("radius"),

    // サンプル
    PLACE_ID("placeId"),
    PLACE("place"),
    PLACES("places"),
    PLACE_NAME("placeName"),
    PLACE_LAT("placeLat"),
    PLACE_LNG("placeLng"),
    PLACE_ADDRESS("placeAddress"),
    ;

    private final String text;
    private final Integer i;

    private AttributeConst(final String text) {
        this.text = text;
        this.i = null;
    }

    private AttributeConst(final Integer i) {
        this.text = null;
        this.i = i;
    }

    public String getValue() {
        return this.text;
    }

    public Integer getIntegerValue() {
        return this.i;
    }

}