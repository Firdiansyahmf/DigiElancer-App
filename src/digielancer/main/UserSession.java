package digielancer.main;

public class UserSession {
    private static int id;
    private static String businessName;
    private static String email;

    public static void setSession(int userId, String bName, String userEmail) {
        id = userId;
        businessName = bName;
        email = userEmail;
    }

    public static int getId() { return id; }
    public static String getBusinessName() { return businessName; }
    public static String getEmail() { return email; }

    public static void clearSession() {
        id = 0;
        businessName = null;
        email = null;
    }
}