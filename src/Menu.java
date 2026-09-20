public class Menu {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("    UNIVERSITY CAFETERIA MENU");
        System.out.println("========================================");
        System.out.println();

        System.out.println("FOOD ITEMS:");
        System.out.println("------------------");
        Food.display();
        System.out.println();

        System.out.println("BEVERAGES:");
        System.out.println("------------------");
        Drinks.display();
        System.out.println();

        System.out.println("TODAY'S SPECIALS:");
        System.out.println("------------------");
        DailySpecials.display();
        System.out.println();

        System.out.println("========================================");
        System.out.println("    Thank you for dining with us!");
        System.out.println("========================================");
    }
}