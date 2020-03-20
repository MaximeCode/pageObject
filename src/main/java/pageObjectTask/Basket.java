package pageObjectTask;

import java.util.HashMap;
import java.util.Map;

public class Basket {

    private static Map<Integer, Product> basketStructure = new HashMap<>();
    private static Map<Integer, Integer> basket = new HashMap<>();
    private static Product lastRemovedItem;

    public static void add(Product product) {
        basketStructure.putIfAbsent(product.getCode(), product);
        basket.put(product.getCode(), basket.getOrDefault(product.getCode(), 0) + 1);
    }

    public static void add(String keywords) {
        add(basketStructure.get(getCode(keywords)));
    }

    public static void remove(String keywords) {
        int code = getCode(keywords);
        int numberOfItems = basket.getOrDefault(code, 0);
        if (numberOfItems > 1)
            basket.put(code, numberOfItems - 1);
        else
            if (numberOfItems == 1) {
                lastRemovedItem = basketStructure.get(code);
                basket.remove(code);
                basketStructure.remove(code);
            }
    }

    public static int getTotalPrice() {
        return basket
                .keySet()
                .stream()
                .map(element -> basketStructure.get(element).getPrice() * basket.get(element))
                .mapToInt(a -> a)
                .sum();
    }

    public static int getItemPrice(String keywords) {
        int code = getCode(keywords);
        return basketStructure.get(code).getPrice() * basket.get(code);
    }

    public static int getItemWarranty(String keywords) {
        return basketStructure.get(getCode(keywords)).getWarranty();
    }

    public static Product getLastRemovedItem() {
        return lastRemovedItem;
    }

    public static boolean isAbsent (String keywords) {
        return basket.containsKey(getCode(keywords));
    }

    public static Map<Integer, Product> getBasketStructure() {
        return basketStructure;
    }

    public static Map<Integer, Integer> getBasket() {
        return basket;
    }

    private static int getCode(String keywords) {
        return basket
                .keySet()
                .stream()
                .filter((element) -> basketStructure.get(element).getName().contains(keywords))
                .map((element) -> basketStructure.get(element).getCode())
                .findFirst()
                .orElse(-1);
    }
}
