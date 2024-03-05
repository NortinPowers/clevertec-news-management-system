package by.clevertec.gateway.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

    public static final String SECURITY_SWAGGER = "bearerAuth";
    public static final String BEARER = "Bearer ";

    @UtilityClass
    public class AuthConstants {

        public static final String ROLES = "roles";
    }

    @UtilityClass
    public class HandlerConstants {

        public static final String STATUS = "status";
        public static final String MESSAGE = "message";
        public static final String TYPE = "type";
    }
}
