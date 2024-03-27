package by.clevertec.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

    public static final String ROLE_USER = "ROLE_USER";
    public static final String USERNAME_NOT_BLANK = "Enter username";
    public static final String PASSWORD_NOT_BLANK = "Enter password";
    public static final String USERNAME_PATTERN = "[a-zA-Z0-9]{3,30}";
    public static final String PASSWORD_PATTERN = "[a-zA-Z0-9]{4,30}";
    public static final String AUTHOR_PATTERN = "[A-Z]*+([a-zA-Z-`]+)*+\\s*+[A-Z]*+([a-zA-Z]+)*";
    public static final String USERNAME_COMMENT_PATTERN = "[A-Z]*+([a-zA-Z-`]+)*+\\s*+[A-Z]*+([a-zA-Z]+)*";
}
