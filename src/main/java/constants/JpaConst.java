package constants;

/**
 * DB関連の項目値を定義するインターフェース
 * ※インターフェイスに定義した変数は public static final 修飾子がついているとみなされる
 */
public interface JpaConst {

    //persistence-unit名
    String PERSISTENCE_UNIT_NAME = "map_service";

    //データ取得件数の最大値
    int ROW_PER_PAGE = 15; //1ページに表示するレコードの数

    //ユーザーテーブル
    String TABLE_USER = "users"; //テーブル名

    //ブックマークテーブル
    String TABLE_BOOKMARK = "bookmarks"; //テーブル名

    //テーブルカラム
    //ユーザーテーブルカラム
    String USER_COL_ID = "id"; //id
    String USER_COL_NAME = "name"; //ユーザー名
    String USER_COL_PASS = "password"; //パスワード
    String USER_COL_ADMIN_FLAG = "admin_flag"; //管理者権限
    String USER_COL_CREATED_AT = "created_at"; //登録日時
    String USER_COL_UPDATED_AT = "updated_at"; //更新日時
    String USER_COL_DELETE_FLAG = "delete_flag"; //削除フラグ

    //ブックマークテーブルカラム
    String BOOKMARK_COL_ID = "id"; //ID
    String BOOKMARK_COL_PLACE_ID = "place_id"; //Place ID
    String BOOKMARK_COL_USER_NAME = "user_name"; //ユーザー名
    String BOOKMARK_COL_CREATED_AT = "created_at"; //登録日時
    String BOOKMARK_COL_PLACE_NAME = "place_name"; //場所名
    String BOOKMARK_COL_LAT = "latitude"; //緯度
    String BOOKMARK_COL_LNG = "longtitude"; //経度
    String BOOKMARK_COL_ADDRESS = "adress"; //付近の情報


    int ROLE_ADMIN = 1; //管理者権限ON(管理者)
    int ROLE_GENERAL = 0; //管理者権限OFF(一般)
    int USER_DEL_TRUE = 1; //削除フラグON(削除済み)
    int USER_DEL_FALSE = 0; //削除フラグOFF(現役)

    //Entity名
    String ENTITY_USER = "user"; //ユーザー
    String ENTITY_BOOKMARK = "bookmark"; //ブックマーク

    //JPQL内パラメータ
    String JPQL_PARM_PASSWORD = "password"; //パスワード
    String JPQL_PARM_NAME = "name"; //ユーザー名
    String JPQL_PARM_PLACE_ID = "placeId"; //場所ID
    String JPQL_PARM_USER = "user"; //ユーザー
    String JPQL_PARM_BOOKMARK_ID = "id";

    //NamedQueryの nameとquery
    //全てのユーザーをidの降順に取得する
    String Q_USER_GET_ALL = ENTITY_USER + ".getAll"; //name
    String Q_USER_GET_ALL_DEF = "SELECT u FROM User AS u ORDER BY u.id DESC"; //query

    //全てのユーザーの件数を取得する
    String Q_USER_COUNT = ENTITY_USER + ".count";
    String Q_USER_COUNT_DEF = "SELECT COUNT(u) FROM User AS u";

    //ユーザー名とハッシュ化済パスワードを条件に未削除のユーザーを取得する（ユーザーがログインするときにユーザー名とパスワードが正しいかをチェックする）
    String Q_USER_GET_BY_NAME_AND_PASS = ENTITY_USER + ".getByNameAndPass";
    String Q_USER_GET_BY_NAME_AND_PASS_DEF = "SELECT u FROM User AS u WHERE u.deleteFlag = 0 AND u.name = :" + JPQL_PARM_NAME + " AND u.password = :" + JPQL_PARM_PASSWORD;

    //指定したユーザー名を保持するユーザーの件数を取得する（指定されたユーザーがすでにデータベースに存在しているかを調べる）
    String Q_USER_COUNT_REGISTERED_BY_NAME = ENTITY_USER + ".countRegisteredByName";
    String Q_USER_COUNT_REGISTERED_BY_NAME_DEF = "SELECT COUNT(u) FROM User AS u WHERE u.name = :" + JPQL_PARM_NAME;

    //全てのブックマークを登録日順に取得する
    String Q_BOOKMARK_GET_ALL = ENTITY_BOOKMARK + ".getAll"; //bookmark
    String Q_BOOKMARK_GET_ALL_DEF = "SELECT b FROM Bookmark AS b ORDER BY b.createdAt DESC"; //query

    //指定したPlace IDを保持するブックマークの件数を取得する（指定されたブックマークがすでにデータベースに存在しているかを調べる）
    String Q_BOOKMARK_COUNT_REGISTERED_BY_ID = ENTITY_BOOKMARK + ".countRegisteredById";
    String Q_BOOKMARK_COUNT_REGISTERED_BY_ID_DEF = "SELECT COUNT(b) FROM Bookmark AS b WHERE b.placeId = :" + JPQL_PARM_PLACE_ID;

    //全てのユーザーの件数を取得する
    String Q_BOOKMARK_COUNT = ENTITY_BOOKMARK + ".count";
    String Q_BOOKMARK_COUNT_DEF = "SELECT COUNT(b) FROM Bookmark AS b";

    //指定したユーザーが登録した日報を全件登録日の降順で取得する
    String Q_BOOKMARK_GET_ALL_MINE = ENTITY_BOOKMARK + ".getAllMine";
    String Q_BOOKMARK_GET_ALL_MINE_DEF = "SELECT b FROM Bookmark AS b WHERE b.userName = :" + JPQL_PARM_USER + " ORDER BY b.createdAt DESC";

    //指定したユーザーが登録した日報の件数を取得する
    String Q_BOOKMARK_COUNT_ALL_MINE = ENTITY_BOOKMARK + ".countAllMine";
    String Q_BOOKMARK_COUNT_ALL_MINE_DEF = "SELECT COUNT(b) FROM Bookmark AS b WHERE b.userName = :" + JPQL_PARM_USER;

}