package models.validators;

import java.util.ArrayList;
import java.util.List;

import actions.views.BookMarkView;
import constants.MessageConst;
import services.BookMarkService;

/**
 * ブックマークインスタンスに設定されている値のバリデーションを行うクラス
 *
 */
public class BookMarkValidator {

    /**
     * ブックマークインスタンスの各項目についてバリデーションを行う
     * @param service 呼び出し元Serviceクラスのインスタンス
     * @param bv BookMarkViewのインスタンス
     * @param placeIdDuplicateCheckFlag 場所IDの重複チェックを実施するかどうか(実施する:true 実施しない:false)
     * @return エラーのリスト
     */
    public static List<String> validate(
            BookMarkService service, BookMarkView bv, Boolean placeIdDuplicateCheckFlag) {
        List<String> errors = new ArrayList<String>();

        //場所IDのチェック
        String nameError = validatePlaceId(service, bv.getPlaceId(), placeIdDuplicateCheckFlag);
        if (!nameError.equals("")) {
            errors.add(nameError);
        }

        return errors;
    }

    /**
     * 場所IDの入力チェックを行い、エラーメッセージを返却
     * @param service BookMarkServiceのインスタンス
     * @param placeId 場所ID
     * @param placeIdDuplicateCheckFlag ユーザー名の重複チェックを実施するかどうか(実施する:true 実施しない:false)
     * @return エラーメッセージ
     */
    private static String validatePlaceId(BookMarkService service, String placeId, Boolean placeIdDuplicateCheckFlag) {

        if (placeIdDuplicateCheckFlag) {
            //場所IDの重複チェックを実施

            long bookMarksCount = isDuplicateBookMark(service, placeId);

            //同一ブックマークが既に登録されている場合はエラーメッセージを返却
            if (bookMarksCount > 0) {
                return MessageConst.B_BOOKMARK_EXIST.getMessage();
            }
        }

        //エラーがない場合は空文字を返却
        return "";
    }

    /**
     * @param service BookMarkServiceのインスタンス
     * @param placeId 場所ID
     * @return ブックマークテーブルに登録されている同一ブックマークのデータの件数
     */
    private static long isDuplicateBookMark(BookMarkService service, String placeId) {

        long bookMarksCount = service.countByPlaceId(placeId);
        return bookMarksCount;
    }

}