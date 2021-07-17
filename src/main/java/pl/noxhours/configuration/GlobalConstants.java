package pl.noxhours.configuration;

import ch.qos.logback.classic.Level;

public class GlobalConstants {

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static final String[] PASSWORD_MATCHERS = {"\\S{8,255}", ".*[a-ząćęłńóśźż].*", ".*[A-ZĄĆĘŁŃÓŚŹŻ].*", ".*[0-9].*"};
    public static final String FIRST_NAME_REGEXP = "[a-zA-ZĄ-ćęĘŁ-ńÓóŚśŹ-ż]{2,255}";
    public static final String LAST_NAME_REGEXP = "[a-zA-ZĄ-ćęĘŁ-ńÓóŚśŹ-ż]{2,}(( ?- ?)[a-zA-ZĄ-ćęĘŁ-ńÓóŚśŹ-ż]{2,})*";

    public static final String ADDED_TIMESHEET_MSG = "użytkownik dodał nowe rozliczenie";
    public static final String EDITED_TIMESHEET_MSG = "użytkownik zmodyfikował rozliczenie";
    public static final String DELETED_TIMESHEET_MSG = "użytkownik usunął rozliczenie";
    public static final String ADDED_CLIENT_MSG = "użytkownik dodał nowego klienta";
    public static final String EDITED_CLIENT_MSG = "użytkownik zmodyfikował klienta";
    public static final String DELETED_CLIENT_MSG = "użytkownik usunął klienta";

//    TODO ustawić losowe hasło wysyłane na maila przy tworzeniu usera
    public static final String DEFAULT_PASSWORD = "Nox7Hours";

    public static final Level LOG_LEVEL = Level.ALL;

}
