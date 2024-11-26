package com.inventory.management;

import java.util.*;

public class InventoryManager {
    private final Map<String, InventoryItem> inventoryMap; // Stores items by ID
    private final Map<String, PriorityQueue<InventoryItem>> categoryMap; // Category-wise sorted items
    private final int restockThreshold; // Threshold for restocking notifications

    public InventoryManager(int restockThreshold) {
        this.inventoryMap = new HashMap<>();
        this.categoryMap = new HashMap<>();
        this.restockThreshold = restockThreshold;
    }

    public List<InventoryItem> getTopKItemsByCategory(String category, int k) {
        PriorityQueue<InventoryItem> queue = categoryMap.get(category);
        if (queue == null) {
            return Collections.emptyList();
        }
        List<InventoryItem> result = new ArrayList<>();
        int count = 0;
        while (!queue.isEmpty() && count < k) {
            result.add(queue.poll());
            count++;
        }
        return result;
    }

    public void clearAllItems() {
        inventoryMap.clear();
        categoryMap.clear();
        System.out.println("All items have been cleared.");
    }

    public void addOrUpdateItem(String id, String name, String category, int quantity) {
        InventoryItem item = inventoryMap.get(id);
        if (item == null) {
            // New item
            item = new InventoryItem(id, name, category, quantity);
            inventoryMap.put(id, item);
            categoryMap.putIfAbsent(category, new PriorityQueue<>((a, b) -> b.getQuantity() - a.getQuantity()));
            categoryMap.get(category).add(item);
        } else {
            // Update existing item
            categoryMap.get(item.getCategory()).remove(item); // Remove from old category queue
            item.setName(name);
            item.setCategory(category);
            item.setQuantity(quantity);
            categoryMap.putIfAbsent(category, new PriorityQueue<>((a, b) -> b.getQuantity() - a.getQuantity()));
            categoryMap.get(category).add(item); // Add to new category queue
        }
        if (quantity < restockThreshold) {
            System.out.printf("Restock Notification: Item '%s' (ID: %s) is below threshold with quantity %d.%n",
                    item.getName(), item.getId(), item.getQuantity());
        }
    }

    public InventoryItem getItemById(String id) {
        return inventoryMap.get(id);
    }

    public List<InventoryItem> getAllItems() {
        return new ArrayList<>(inventoryMap.values());
    }

    public void removeItem(String id) {
        InventoryItem item = inventoryMap.remove(id);
        if (item != null) {
            categoryMap.get(item.getCategory()).remove(item);
        }
    }

    public void displayAllItems() {
        if (inventoryMap.isEmpty()) {
            System.out.println("No items in inventory.");
        } else {
            inventoryMap.values().forEach(System.out::println);
        }
    }

    public void mergeInventory(InventoryManager otherManager) {
        for (InventoryItem otherItem : otherManager.getAllItems()) {
            InventoryItem existingItem = inventoryMap.get(otherItem.getId());
            if (existingItem == null) {
                inventoryMap.put(otherItem.getId(), otherItem);
                categoryMap.putIfAbsent(otherItem.getCategory(),
                        new PriorityQueue<>((a, b) -> b.getQuantity() - a.getQuantity()));
                categoryMap.get(otherItem.getCategory()).add(otherItem);
            } else {
                // If duplicate ID found, retain item with higher quantity
                if (otherItem.getQuantity() > existingItem.getQuantity()) {
                    categoryMap.get(existingItem.getCategory()).remove(existingItem); // Removes the old item from its category
                    inventoryMap.put(otherItem.getId(), otherItem); // Updates the inventory map with the new item

                    // Corrected categoryMap.put() with PriorityQueue
                    categoryMap.putIfAbsent(otherItem.getCategory(), new PriorityQueue<>(Comparator.comparingInt(InventoryItem::getQuantity))); // Ensure category exists
                    categoryMap.get(otherItem.getCategory()).add(otherItem); // Add the new item to its category
                }


            }
        }
        otherManager.clearAllItems();
        System.out.println("Inventories merged successfully. Duplicate IDs retain items with higher quantities.");
    }
}
