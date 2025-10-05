import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("--- CRMS Setup & Launch Console ---");

        // The main method starts here and calls the menu.
        displayMenu();
    }

    private static void displayMenu() {
        while (true) {
            System.out.println("\nSelect an Option:");
            System.out.println("1. (RE-RUN) Database Initializer (Run this first!)");
            System.out.println("2. Create New Admin/Officer Account");
            System.out.println("3. Launch CRMS Application (Login)");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");

            try {
                int choice = scanner.nextInt();
                // Consume the newline
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        DatabaseInitializer.main(null);
                        break;
                    case 2:
                        AdminAccountCreator.main(null);
                        break;
                    case 3:
                        // --- LAUNCHES THE LOGIN UI ---
                        CRMSApplication.main(null);
                        break;
                    case 0:
                        System.out.println("Exiting CRMS setup launcher. Goodbye!");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input
            }
        }
    }
}
