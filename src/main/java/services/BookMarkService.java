package services;

import java.time.LocalDateTime;
import java.util.List;

import actions.views.BookMarkConverter;
import actions.views.BookMarkView;
import actions.views.UserConverter;
import actions.views.UserView;
import constants.JpaConst;
import models.Bookmark;
import models.validators.BookMarkValidator;

/**
 * ブックマークテーブルの操作に関わる処理を行うクラス
 */
public class BookMarkService extends ServiceBase {

    /**
     * 指定したユーザーが作成したブックマークデータを、指定されたページ数の一覧画面に表示する分取得しBookMarkViewのリストで返却する
     * @param user ユーザー
     * @param page ページ数
     * @return 一覧画面に表示するデータのリスト
     */
    public List<BookMarkView> getMinePerPage(UserView user, int page) {

        List<Bookmark> bookMarks = em.createNamedQuery(JpaConst.Q_BOOKMARK_GET_ALL_MINE, Bookmark.class)
                .setParameter(JpaConst.JPQL_PARM_USER, UserConverter.toModel(user))
                .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                .setMaxResults(JpaConst.ROW_PER_PAGE)
                .getResultList();
        return BookMarkConverter.toViewList(bookMarks);
    }

    /**
     * 指定したユーザーが作成したブックマークデータの件数を取得し、返却する
     * @param user
     * @return ブックマークデータの件数
     */
    public long countAllMine(UserView user) {

        long count = (long) em.createNamedQuery(JpaConst.Q_BOOKMARK_COUNT_ALL_MINE, Long.class)
                .setParameter(JpaConst.JPQL_PARM_USER, UserConverter.toModel(user))
                .getSingleResult();

        return count;
    }

    /**
     * 指定されたページ数の一覧画面に表示するブックマークデータを取得し、BookMarkViewのリストで返却する
     * @param page ページ数
     * @return 一覧画面に表示するデータのリスト
     */
    public List<BookMarkView> getAllPerPage(int page) {

        List<Bookmark> bookMarks = em.createNamedQuery(JpaConst.Q_BOOKMARK_GET_ALL, Bookmark.class)
                .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                .setMaxResults(JpaConst.ROW_PER_PAGE)
                .getResultList();
        return BookMarkConverter.toViewList(bookMarks);
    }

    /**
     * ブックマークテーブルのデータの件数を取得し、返却する
     * @return データの件数
     */
    public long countAll(UserView user ) {
        long bookMarks_count = (long) em.createNamedQuery(JpaConst.Q_BOOKMARK_COUNT, Long.class)
                .getSingleResult();
        return bookMarks_count;
    }

    /**
     * idを条件に取得したデータをBookMarkViewのインスタンスで返却する
     * @param id
     * @return 取得データのインスタンス
     */
    public BookMarkView findOne(int id) {
        return BookMarkConverter.toView(findOneInternal(id));
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

            em.getTransaction().begin();
            em.persist(BookMarkConverter.toModel(bv));
            em.getTransaction().commit();
        }

        //エラーを返却（エラーがなければ0件の空リスト）
        return errors;
    }


    /**
     * idを条件にデータを1件取得し、Bookmarkのインスタンスで返却する
     * @param id
     * @return 取得データのインスタンス
     */
    private Bookmark findOneInternal(int id) {
        Bookmark b = em.find(Bookmark.class, id);

        return b;
    }

//    /**
//     * ユーザー名を条件にデータを1件取得し、Bookmarkのインスタンスで返却する
//     * @param userName
//     * @return 取得データのインスタンス
//     */
//    private Bookmark findOneInternal(String userName) {
//        Bookmark b = em.find(Bookmark.class, userName);
//
//        return b;
//    }

    /**
     * 場所IDを条件にブックマークデータを物理削除する
     * @param placeId
     */
    public void destroy(int id) {

        //idを条件に登録済みのユーザー情報を取得する
        //物理削除のためEntityManager#remove()を実行
//        BookMarkView savedBookMark = findOne(id);
//        em.getTransaction().begin();
//        Bookmark bm = BookMarkConverter.toModel(savedBookMark);
//        em.remove(bm);  //データ削除
//        em.getTransaction().commit();

        em.getTransaction().begin();
        em.remove(em.find(Bookmark.class, id));  //データ削除
        em.getTransaction().commit();

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


}