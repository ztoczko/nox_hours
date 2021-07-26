package pl.noxhours.configuration;

public class GlobalConstants {

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static final String[] PASSWORD_MATCHERS = {"\\S{8,255}", ".*[a-ząćęłńóśźż].*", ".*[A-ZĄĆĘŁŃÓŚŹŻ].*", ".*[0-9].*"};
    public static final String FIRST_NAME_REGEXP = "[a-zA-ZĄ-ćęĘŁ-ńÓóŚśŹ-ż]{2,255}";
    public static final String LAST_NAME_REGEXP = "[a-zA-ZĄ-ćęĘŁ-ńÓóŚśŹ-ż]{2,}(( ?- ?)[a-zA-ZĄ-ćęĘŁ-ńÓóŚśŹ-ż]{2,})*";

    public static final String ADDED_TIMESHEET_MSG = "użytkownik dodał nowe rozliczenie";
    public static final String EDITED_TIMESHEET_MSG = "użytkownik zmodyfikował rozliczenie";
    public static final String DELETED_TIMESHEET_MSG = "użytkownik usunął rozliczenie";

    public static final String EMAIL_HEAD = "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <meta content=\"width=device-width, initial-scale=1.0\" name=\"viewport\">\n" +
            "    <meta content=\"ie=edge\" http-equiv=\"X-UA-Compatible\">\n" +
            "    <title>NOX - Hours</title>\n" +
            "    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css\" rel=\"stylesheet\"\n" +
            "          integrity=\"sha384-+0n0xVW2eSR5OomGNYDnhzAbDsOXxcvSN1TPprVMTNDbiYZCxYbOOl7+AMvyTG2x\" crossorigin=\"anonymous\">\n" +
            "</head>";
}
