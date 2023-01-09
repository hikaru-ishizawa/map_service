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
//    public void create() throws ServletException, IOException {
//
//        putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
//
//        //ブックマーク情報の空インスタンスに、ブックマークした日付＝今日の日付を設定する
//        BooKMarkView bv = new BookMarkView();
//        bv.setReportDate(LocalDate.now());
//        putRequestScope(AttributeConst.REPORT, rv); //日付のみ設定済みの日報インスタンス
//
//        //CSRF対策 tokenのチェック
//        if (checkToken()) {
//
//            //登録日に今日の日付を設定
//            LocalDateTime day = null;
//            day = LocalDateTime.now();
//
//            //セッションからログイン中のユーザー情報を取得
//            UserView uv = (UserView) getSessionScope(AttributeConst.LOGIN_USER);
//
//            //パラメータの値をもとにブックマーク情報のインスタンスを作成する
//            BookMarkView bv = new BookMarkView(
//                    getRequestParam(AttributeConst.PLACE_ID),
//                    getRequestParam(AttributeConst.PLACE_NAME),
//                    getRequestParam(AttributeConst.PLACE_LAT),
//                    getRequestParam(AttributeConst.PLACE_LNG),
//                    getRequestParam(AttributeConst.PLACE_ADDRESS),
//                    uv,//ログインしている従業員を、日報作成者として登録する
//                    day);
//
//
//            //ブックマーク登録
//            List<String> errors = service.create(bv);
//
//            if (errors.size() > 0) {
//                //登録中にエラーがあった場合
//
//                putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
//                putRequestScope(AttributeConst.BOOKMARK, bv); //入力されたブックマーク情報
//                putRequestScope(AttributeConst.ERR, errors); //エラーのリスト
//
//                //検索画面を再表示
//                forward(ForwardConst.FW_MAP_INDEX);
//
//            } else {
//                //登録中にエラーがなかった場合
//
//                //セッションに登録完了のフラッシュメッセージを設定
//                putSessionScope(AttributeConst.FLUSH, MessageConst.I_REGISTERED.getMessage());
//
//                //検索画面を再表示
//                forward(ForwardConst.FW_MAP_INDEX);
//            }
//
//        }
//    }
    public void create() throws ServletException, IOException {

        //CSRF対策 tokenのチェック
        if (checkToken()) {

            //登録日に今日の日付を設定
            LocalDateTime day = null;
            day = LocalDateTime.now();

            //セッションからログイン中のユーザー情報を取得
            UserView uv = (UserView) getSessionScope(AttributeConst.LOGIN_USER);

            //パラメータの値をもとにブックマーク情報のインスタンスを作成する
            BookMarkView bv = new BookMarkView(
                    null,
                    getRequestParam(AttributeConst.PLACE_ID),
                    getRequestParam(AttributeConst.PLACE_NAME),
                    getRequestParam(AttributeConst.PLACE_LAT),
                    getRequestParam(AttributeConst.PLACE_LNG),
                    getRequestParam(AttributeConst.PLACE_ADDRESS),
                    uv,//ログインしているユーザーを、ブックマーク登録者として登録する
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

        //セッションからログイン中のユーザー情報を取得
        UserView loginUser = (UserView) getSessionScope(AttributeConst.LOGIN_USER);

        //ログイン中の従業員が作成した日報データを、指定されたページ数の一覧画面に表示する分取得する
        int page = getPage();
        List<BookMarkView> bookMarks = service.getMinePerPage(loginUser, page);

        //ログイン中のユーザーが登録したブックマークデータの件数を取得
        long myBookMarkCount = service.countAllMine(loginUser);

        putRequestScope(AttributeConst.BOOKMARKS, bookMarks); //取得したブックマークデータ
        putRequestScope(AttributeConst.BOOKMARK_COUNT, myBookMarkCount); //ログイン中のユーザーが登録したブックマークデータの件数
        putRequestScope(AttributeConst.PAGE, page); //ページ数
        putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); //1ページに表示するレコードの数

        //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
        String flush = getSessionScope(AttributeConst.FLUSH);
        if (flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush);
            removeSessionScope(AttributeConst.FLUSH);
        }

        //一覧画面を表示
        forward(ForwardConst.FW_BOOKMARK_INDEX);

    }

    /**
     * 詳細画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void show() throws ServletException, IOException {

        //場所idを条件にブックマークデータを取得する
        BookMarkView bv = service.findOne(toNumber(getRequestParam(AttributeConst.BOOKMARK_ID)));

        if (bv == null) {
            //該当のブックマークデータが存在しない場合はエラー画面を表示
            forward(ForwardConst.FW_ERR_UNKNOWN);

        } else {

            putRequestScope(AttributeConst.BOOKMARK, bv); //取得したブックマークデータ

            //詳細画面を表示
            forward(ForwardConst.FW_BOOKMARK_SHOW);
        }
    }


    /**
     * ブックマークを物理削除する
     * @throws ServletException
     * @throws IOException
     */
    public void destroy() throws ServletException, IOException {

            service.destroy(toNumber(getRequestParam(AttributeConst.BOOKMARK_ID)));

            //セッションにデリート時のフラッシュメッセージを追加
            putSessionScope(AttributeConst.FLUSH, MessageConst.I_DELETED.getMessage());

            //一覧画面を表示
            redirect(ForwardConst.ACT_BOOKMARK, ForwardConst.CMD_INDEX);

    }

}