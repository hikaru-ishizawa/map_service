package actions;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.BookMarkView;
import actions.views.UserView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import constants.MessageConst;
import services.BookMarkService;

/**
 * ブックマークに関わる処理を行うActionクラス
 *
 */
public class BookMarkAction extends ActionBase {

    private BookMarkService service;

    /**
     * メソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException {

        service = new BookMarkService();

        //メソッドを実行
        invoke();

        service.close();
    }

    /**
     * 新規登録を行う
     * @throws ServletException
     * @throws IOException
     */
    public void create() throws ServletException, IOException {

        //CSRF対策 tokenのチェック
        if (checkToken()) {

            //日報の日付が入力されていなければ、今日の日付を設定
            LocalDateTime day = null;
            day = LocalDateTime.now();

            //セッションからログイン中のユーザー情報を取得
            UserView uv = (UserView) getSessionScope(AttributeConst.LOGIN_USER);

            //パラメータの値をもとにブックマーク情報のインスタンスを作成する
            BookMarkView bv = new BookMarkView(
                    getRequestParam(AttributeConst.PLACE_ID),
                    getRequestParam(AttributeConst.PLACE_NAME),
                    getRequestParam(AttributeConst.PLACE_LAT),
                    getRequestParam(AttributeConst.PLACE_LNG),
                    getRequestParam(AttributeConst.PLACE_ADDRESS),
                    uv,//ログインしている従業員を、日報作成者として登録する
                    day);


            //ブックマーク登録
            List<String> errors = service.create(bv);

            if (errors.size() > 0) {
                //登録中にエラーがあった場合

                putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
                putRequestScope(AttributeConst.BOOKMARK, bv); //入力されたブックマーク情報
                putRequestScope(AttributeConst.ERR, errors); //エラーのリスト

                //検索画面を再表示
                forward(ForwardConst.FW_MAP_INDEX);

            } else {
                //登録中にエラーがなかった場合

                //セッションに登録完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_REGISTERED.getMessage());

                //検索画面を再表示
                forward(ForwardConst.FW_MAP_INDEX);
            }

        }
    }

    /**
     * 一覧画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void index() throws ServletException, IOException {

        //指定されたページ数の一覧画面に表示するデータを取得
        int page = getPage();
        List<BookMarkView> bookmarks = service.getPerPage(page);

        //全てのブックマークデータの件数を取得
        long bookMarkCount = service.countAll();

        putRequestScope(AttributeConst.BOOKMARKS, bookmarks); //取得したブックマークデータ
        putRequestScope(AttributeConst.BOOKMARK_COUNT, bookMarkCount); //全てのブックマークデータの件数
        putRequestScope(AttributeConst.PAGE, page); //ページ数
        putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); //1ページに表示するレコードの数

        //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
        String flush = getSessionScope(AttributeConst.FLUSH);
        if (flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush);
            removeSessionScope(AttributeConst.FLUSH);
        }

        //検索画面を再表示
        forward(ForwardConst.FW_BOOKMARK_INDEX);

    }




    /**
     * 詳細画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void show() throws ServletException, IOException {

        //ユーザー名を条件に従業員データを取得する
        BookMarkView bv = service.findOne(getRequestParam(AttributeConst.USER_NAME));

        if (bv == null) {

            //データが取得できなかった場合はエラー画面を表示
            forward(ForwardConst.FW_ERR_UNKNOWN);
            return;
        }

        putRequestScope(AttributeConst.BOOKMARK, bv); //取得したユーザー情報

        //詳細画面を表示
        forward(ForwardConst.FW_BOOKMARK_SHOW);
    }

}