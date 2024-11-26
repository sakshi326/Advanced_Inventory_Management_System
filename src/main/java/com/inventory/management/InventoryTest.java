package com.inventory.management;

import java.util.List;
import java.util.Scanner;

public class InventoryTest {
    public static void main(String[] args) {
        InventoryManager manager1 = new InventoryManager(10);
        InventoryManager manager2 = new InventoryManager(10);

        // Predefined test cases
        manager1.addOrUpdateItem("101", "Laptop", "Electronics", 50);
        manager1.addOrUpdateItem("102", "Table", "Furniture", 5);
        manager1.addOrUpdateItem("103", "Chair", "Furniture", 20);
        manager1.addOrUpdateItem("104", "TV", "Electronics", 8);
        manager1.addOrUpdateItem("105", "Rice", "Groceries", 100);

        manager2.addOrUpdateItem("101", "Laptop", "Electronics", 30); // Duplicate ID
        manager2.addOrUpdateItem("106", "Desk", "Furniture", 15);
        manager2.addOrUpdateItem("107", "Fan", "Electronics", 10);
        manager2.addOrUpdateItem("108", "Milk", "Groceries", 50);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Inventory Management System ===");
            System.out.println("1. Display All Items");
            System.out.println("2. Search Item by ID");
            System.out.println("3. Add Item");
            System.out.println("4. Update Item");
            System.out.println("5. Delete Item by ID");
            System.out.println("6. Display Top K Items by Category");
            System.out.println("7. Merge Another Inventory");
            System.out.println("8. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    handleDisplayItems(manager1, manager2);
                    break;
                case 2:
                    handleSearchById(scanner, manager1, manager2);
                    break;
                case 3:
                    handleAddItem(scanner, manager1, manager2);
                    break;
                case 4:
                    handleUpdateItem(scanner, manager1, manager2);
                    break;
                case 5:
                    handleDeleteItem(scanner, manager1, manager2);
                    break;
                case 6:
                    handleTopKItems(scanner, manager1, manager2);
                    break;
                case 7:
                    handleMergeInventory(manager1, manager2);
                    break;
                case 8:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void handleDisplayItems(InventoryManager manager1, InventoryManager manager2) {
        System.out.println("\nAll Items in First Inventory:");
        manager1.displayAllItems();
        System.out.println("\nAll Items in Second Inventory:");
        manager2.displayAllItems();
    }

    private static void handleSearchById(Scanner scanner, InventoryManager manager1, InventoryManager manager2) {
        System.out.print("\nEnter Inventory (1 for First, 2 for Second): ");
        int inventoryChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter Item ID to search: ");
        String searchId = scanner.nextLine();
        InventoryManager selectedManager = (inventoryChoice == 2) ? manager2 : manager1;
        InventoryItem item = selectedManager.getItemById(searchId);
        if (item != null) {
            System.out.println("Found: " + item);
        } else {
            System.out.println("Item with ID " + searchId + " not found.");
        }
    }

    private static void handleAddItem(Scanner scanner, InventoryManager manager1, InventoryManager manager2) {
        System.out.print("\nEnter Inventory (1 for First, 2 for Second): ");
        int inventoryChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        InventoryManager selectedManager = (inventoryChoice == 2) ? manager2 : manager1;
        System.out.print("Enter ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Category: ");
        String category = scanner.nextLine();
        System.out.print("Enter Quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        selectedManager.addOrUpdateItem(id, name, category, quantity);
        System.out.println("Item added successfully.");
    }

    private static void handleUpdateItem(Scanner scanner, InventoryManager manager1, InventoryManager manager2) {
        System.out.print("\nEnter Inventory (1 for First, 2 for Second): ");
        int inventoryChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        InventoryManager selectedManager = (inventoryChoice == 2) ? manager2 : manager1;
        System.out.print("Enter ID of item to update: ");
        String updateId = scanner.nextLine();
        InventoryItem existingItem = selectedManager.getItemById(updateId);
        if (existingItem != null) {
            System.out.print("Enter New Name (current: " + existingItem.getName() + "): ");
            String newName = scanner.nextLine();
            System.out.print("Enter New Category (current: " + existingItem.getCategory() + "): ");
            String newCategory = scanner.nextLine();
            System.out.print("Enter New Quantity (current: " + existingItem.getQuantity() + "): ");
            int newQuantity = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            selectedManager.addOrUpdateItem(updateId, newName, newCategory, newQuantity);
            System.out.println("Item updated successfully.");
        } else {
            System.out.println("Item with ID " + updateId + " not found.");
        }
    }

    private static void handleDeleteItem(Scanner scanner, InventoryManager manager1, InventoryManager manager2) {
        System.out.print("\nEnter Inventory (1 for First, 2 for Second): ");
        int inventoryChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        InventoryManager selectedManager = (inventoryChoice == 2) ? manager2 : manager1;
        System.out.print("Enter ID of item to delete: ");
        String deleteId = scanner.nextLine();
        selectedManager.removeItem(deleteId);
        System.out.println("Item removed successfully.");
    }

    private static void handleTopKItems(Scanner scanner, InventoryManager manager1, InventoryManager manager2) {
        System.out.print("\nEnter Inventory (1 for First, 2 for Second): ");
        int inventoryChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter the category name: ");
        String category = scanner.nextLine();
        System.out.print("Enter the value of k: ");
        int k = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        InventoryManager selectedManager = (inventoryChoice == 2) ? manager2 : manager1;
        System.out.println("Top " + k + " items for category '" + category + "':");
        List<InventoryItem> topItems = selectedManager.getTopKItemsByCategory(category, k);
        topItems.forEach(System.out::println);
    }

    private static void handleMergeInventory(InventoryManager manager1, InventoryManager manager2) {
        // Merge the inventory of manager2 into manager1
        manager1.mergeInventory(manager2);

        // Confirm the merge operation
        System.out.println("Inventories have been successfully merged.");

        // Display the updated inventory for manager1
        System.out.println("Updated Inventory:");
        manager1.displayAllItems();
    }

}
