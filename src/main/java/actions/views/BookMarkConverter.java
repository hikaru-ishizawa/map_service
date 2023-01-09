package actions.views;

import java.util.ArrayList;
import java.util.List;

import models.Bookmark;

/**
 * 従業員データのDTOモデル⇔Viewモデルの変換を行うクラス
 *
 */
public class BookMarkConverter {

    /**
     * ViewモデルのインスタンスからDTOモデルのインスタンスを作成する
     * @param bv BookMarkViewのインスタンス
     * @return Bookmarkのインスタンス
     */
    public static Bookmark toModel(BookMarkView bv) {

        return new Bookmark(
                bv.getId(),
                bv.getPlaceId(),
                bv.getPlaceName(),
                bv.getLat(),
                bv.getLng(),
                bv.getAddress(),
                UserConverter.toModel(bv.getUserName()), //UserView→User型へ型変換
                bv.getCreatedAt()
                );
    }

    /**
     * DTOモデルのインスタンスからViewモデルのインスタンスを作成する
     * @param b Bookmarkのインスタンス
     * @return BookMarkViewのインスタンス
     */
    public static BookMarkView toView(Bookmark b) {

        if(b == null) {
            return null;
        }

        return new BookMarkView(
                b.getId(),
                b.getPlaceId(),
                b.getPlaceName(),
                b.getLat(),
                b.getLng(),
                b.getAddress(),
                UserConverter.toView(b.getUserName()),
                b.getCreatedAt()
                );

    }

    /**
     * DTOモデルのリストからViewモデルのリストを作成する
     * @param list DTOモデルのリスト
     * @return Viewモデルのリスト
     */
    public static List<BookMarkView> toViewList(List<Bookmark> list) {
        List<BookMarkView> bvs = new ArrayList<>();

        for (Bookmark b : list) {
            bvs.add(toView(b));
        }

        return bvs;
    }

    /**
     * Viewモデルの全フィールドの内容をDTOモデルのフィールドにコピーする
     * @param e DTOモデル(コピー先)
     * @param ev Viewモデル(コピー元)
     */
    public static void copyViewToModel(Bookmark b, BookMarkView bv) {
        b.setId(bv.getId());
        b.setPlaceId(bv.getPlaceId());
        b.setPlaceName(bv.getPlaceName());
        b.setLat(bv.getLat());
        b.setLng(bv.getLng());
        b.setAddress(bv.getAddress());
        b.setUserName(UserConverter.toModel(bv.getUserName()));
        b.setCreatedAt(bv.getCreatedAt());

    }

}