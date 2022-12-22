package constants;

/**
 * リクエストパラメーターの変数名、変数値、jspファイルの名前等画面遷移に関わる値を定義するEnumクラス
 *
 */
public enum ForwardConst {

    //action
    ACT("action"),
    ACT_TOP("Top"),
    ACT_AUTH("Auth"),
    ACT_MAP("Map"),
    ACT_SAMP("Sample"),

    //command
    CMD("command"),
    CMD_NONE(""),
    CMD_INDEX("index"),
    CMD_SHOW("show"),
    CMD_SHOW_LOGIN("showLogin"),
    CMD_LOGIN("login"),
    CMD_LOGOUT("logout"),
    CMD_NEW("entryNew"),
    CMD_CREATE("create"),
    CMD_EDIT("edit"),
    CMD_UPDATE("update"),
    CMD_DESTROY("destroy"),
    CMD_SEARCH_OCHANOMIZU("searchNearByOchanomizu"),
    CMD_SHOW_PRICE("showPrice"),
    CMD_SEARCH("search"),
    //jsp
    FW_ERR_UNKNOWN("error/unknown"),
    FW_MAP_INDEX("map/index"),
    FW_INDEX("sample/index"),
    FW_SEARCH_OCHANOMIZU("sample/search_near_by_ochanomizu"),
    FW_SHOW_PRICE("sample/place")
    ;

    /**
     * 文字列
     */
    private final String text;

    /**
     * コンストラクタ
     */
    private ForwardConst(final String text) {
        this.text = text;
    }

    /**
     * 値(文字列)取得
     */
    public String getValue() {
        return this.text;
    }

}