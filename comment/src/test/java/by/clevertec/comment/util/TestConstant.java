package by.clevertec.comment.util;

import java.time.LocalDateTime;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestConstant {

    public static final Long NEWS_ID = 1L;
    public static final Long COMMENT_ID = 1L;
    public static final LocalDateTime NEWS_TIME = LocalDateTime.of(2024, 2, 20, 11, 17, 15);
    public static final LocalDateTime COMMENT_TIME = LocalDateTime.of(2024, 2, 21, 20, 15, 13);
    public static final String NEWS_TITLE = "news title";
    public static final String COMMENT_USERNAME = "Piter Parker";
    public static final String NEWS_TEXT = "news text";
    public static final String COMMENT_TEXT = "comment text";
    public static final Long AUTHOR_ID = 1L;
    public static final String AUTHOR_NAME = "Ben Brown";
    public static final Long CORRECT_ID = 1L;
    public static final Long INCORRECT_ID = 30L;
    public static final int PAGE_NUMBER = 0;
    public static final int PAGE_SIZE = 15;
}
