package nowire.space.learnromanian.util;

public class Enum {
    public class Role {
        public static final String ADMIN = "ADMIN";
        public static final String MODERATOR = "MODERATOR";
        public static final String PROFESSOR = "PROFESSOR";
        public static final String STUDENT = "STUDENT";
    }

    public class Role_Id {
        public static final Integer ADMIN = 1;
        public static final Integer MODERATOR = 2;
        public static final Integer PROFESSOR = 3;
        public static final Integer STUDENT = 4;
    }

    public class DBRole {
        public static final String ADMIN = "ROLE_ADMIN";
        public static final String MODERATOR = "ROLE_MODERATOR";
        public static final String PROFESSOR = "ROLE_PROFESSOR";
        public static final String STUDENT = "ROLE_STUDENT";
    }
}