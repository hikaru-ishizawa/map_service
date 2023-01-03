package services;

import java.time.LocalDateTime;
import java.util.List;

import actions.views.BookMarkConverter;
import actions.views.BookMarkView;
import constants.JpaConst;
import models.Bookmark;
import models.validators.BookMarkValidator;

/**
 * ブックマークテーブルの操作に関わる処理を行うクラス
 */
public class BookMarkService extends ServiceBase {

    /**
     * 指定されたページ数の一覧画面に表示するデータを取得し、UserViewのリストで返却する
     * @param page ページ数
     * @return 表示するデータのリスト
     */
    public List<BookMarkView> getPerPage(int page) {
        List<Bookmark> bookMarks = em.createNamedQuery(JpaConst.Q_BOOKMARK_GET_ALL, Bookmark.class)
                .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                .setMaxResults(JpaConst.ROW_PER_PAGE)
                .getResultList();

        return BookMarkConverter.toViewList(bookMarks);
    }

    /**
     * ブックマークテーブルのデータの件数を取得し、返却する
     * @return ブックマークテーブルのデータの件数
     */
    public long countAll() {
        long bookMarkCount = (long) em.createNamedQuery(JpaConst.Q_BOOKMARK_COUNT, Long.class)
                .getSingleResult();

        return bookMarkCount;
    }

    /**
     * 場所IDを条件に取得したデータをBookMarkViewのインスタンスで返却する
     * @param placeId
     * @return 取得データのインスタンス
     */
    public BookMarkView findOne(String placeId) {
        Bookmark b = findOneInternal(placeId);
        return BookMarkConverter.toView(b);
    }

    /**
     * 場所IDを条件に該当するデータの件数を取得し、返却する
     * @param placeId 場所ID
     * @return 該当するデータの件数
     */
    public long countByPlaceId(String placeId) {

        //指定した場所IDを保持するブックマークの件数を取得する
        long bookMarks_count = (long) em.createNamedQuery(JpaConst.Q_BOOKMARK_COUNT_REGISTERED_BY_ID, Long.class)
                .setParameter(JpaConst.JPQL_PARM_PLACE_ID, placeId)
                .getSingleResult();
        return bookMarks_count;
    }

    /**
     * 画面から入力された場所の登録内容を元にデータを1件作成しブックマークテーブルに登録する
     * @param bv 画面から入力されたブックマークの登録内容
     * @return バリデーションや登録処理中に発生したエラーのリスト
     */
    public List<String> create(BookMarkView bv) {

        //登録日時は現在時刻を設定する
        LocalDateTime now = LocalDateTime.now();
        bv.setCreatedAt(now);

        //登録内容のバリデーションを行う
        List<String> errors = BookMarkValidator.validate(this, bv, true);

        //バリデーションエラーがなければデータを登録する
        if (errors.size() == 0) {
            create(bv);
        }

        //エラーを返却（エラーがなければ0件の空リスト）
        return errors;
    }


    /**
     * ユーザー名を条件にデータを1件取得し、Userのインスタンスで返却する
     * @param userName
     * @return 取得データのインスタンス
     */
    private Bookmark findOneInternal(String userName) {
        Bookmark b = em.find(Bookmark.class, userName);

        return b;
    }

    /**
     * 場所IDを条件にブックマークデータを物理削除する
     * @param placeId
     */
//    public void destroy(String placeId) {
//
//        //idを条件に登録済みの従業員情報を取得する
//        BookMarkView savedBookMark = findOne(placeId);
//
//        //更新処理を行う
//        update(savedBookMark);
//
//    }

}