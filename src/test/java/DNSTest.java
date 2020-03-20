import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pageObjectTask.Init;
import pageObjectTask.pages.BasketPage;
import pageObjectTask.pages.ItemPage;
import pageObjectTask.pages.MainPage;
import pageObjectTask.pages.SearchResultsPage;

public class DNSTest {

    @Before
    public void gettingStarted() {
        Init.gettingStarted();
    }

    @Test
    public void pageObjectTest() {
        //  1) открыть dns-shop
        MainPage main = new MainPage();

        //  2) в поиске найти playstation
        SearchResultsPage results = (SearchResultsPage) main.search("PlayStation");

        //  3) кликнуть по playstation 4 slim black
        //  4) запомнить цену
        ItemPage item = results.selectItem("PlayStation 4 Slim Black");

        //  5) доп.гарантия - выбрать 2 года
        //  6) дождаться изменения цены и запомнить цену с гарантией
        item.selectWarranty(2);

        //  7) Нажать Купить
        item.buy();

        //  8) выполнить поиск Detroit
        //  9) запомнить цену
        item = (ItemPage) item.search("Detroit");

        // 10) нажать купить
        item.buy();

        // 11) проверить что цена корзины стала равна сумме покупок
        item.checkTotalPrice();

        // 12) перейри в корзину
        BasketPage basket = item.goToBasket();

        // 13) проверить, что для приставки выбрана гарантия на 2 года
        basket.checkWarranty("PlayStation");

        // 14) проверить цену каждого из товаров и сумму
        basket.checkItemPrice("PlayStation");
        basket.checkItemPrice("Detroit");
        basket.checkTotalPrice();

        // 15) удалить из корзины Detroit
        basket.remove("Detroit");

        // 16) проверить что Detroit нет больше в корзине и что сумма уменьшилась на цену Detroit
        basket.checkItemAbsence("Detroit");
        basket.checkTotalPrice();

        // 17) добавить еще 2 playstation (кнопкой +) и проверить что сумма верна (равна трем ценам playstation)
        basket.add("PlayStation");
        basket.add("PlayStation");
        basket.checkTotalPrice();

        // 18) нажать вернуть удаленный товар, проверить что Detroit появился в корзине и сумма увеличилась на его значение
        basket.getBackRemovedItem();
        basket.checkItemPresence("Detroit");
        basket.checkTotalPrice();
    }

    @After
    public void shutDown() {
        Init.shutDown();
    }
}
