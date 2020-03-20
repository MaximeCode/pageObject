package pageObjectTask.pages;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pageObjectTask.Basket;
import pageObjectTask.Product;

import java.util.List;

public class BasketPage extends BasePage {

    @FindBy(xpath = "//div[@class='cart-items__product']")
    private List<WebElement> itemLinks;

    private final String totalPriceXpath = "//div[@class='total-amount']//span[@class='price__current']";
    @FindBy(xpath = totalPriceXpath)
    private WebElement totalPriceText;

    @FindBy(xpath = "//span[contains(text(), 'удалённый')]")
    private WebElement removedItemLink;

    private By warrantyTextLocator = By.xpath(".//span[contains(text(), 'гарантия')]");
    private By priceTextLocator = By.xpath(".//span[@class='price__current']");
    private By codeTextLocator = By.xpath(".//div[@class='cart-items__product-code']/div");
    private By nameLinkLocator = By.xpath(".//a[@class='cart-items__product-name-link']");
    private By minusButtonLocator = By.xpath(".//button[contains(@class, 'count-buttons__button_minus')]");
    private By plusButtonLocator = By.xpath(".//button[contains(@class, 'count-buttons__button_plus')]");


    public void checkWarranty(String keywords) {
        Assert.assertEquals("Гарантия выбрана неверно.",
                getItem(keywords)
                .findElement(warrantyTextLocator)
                .getText()
                .replaceAll("мес.+", "")
                .replaceAll("\\D", ""),
                Basket.getBasketStructure()
                        .get(textElementToInt(getItem(keywords).findElement(codeTextLocator)))
                        .getWarranty() * 12 + "");
    }

    public void checkItemPrice(String keywords) {
        WebElement element = getItem(keywords);
        int priceOfWarranty = 0;
        if (Basket.getItemWarranty(keywords) != 0)
            priceOfWarranty = Integer.parseInt(element.findElement(warrantyTextLocator)
                    .getText()
                    .replaceAll(".*\\+", "")
                    .replaceAll("\\D", ""));
        Assert.assertEquals("Цена товара неверная.",
                Basket.getItemPrice(keywords),
                textElementToInt(element.findElement(priceTextLocator))
                        + priceOfWarranty);
    }

    public void checkTotalPrice() {
        Assert.assertEquals("Цена покупок не соответствует цене корзины.",
                Basket.getTotalPrice(), textElementToInt(waitForElement(totalPriceText)));
    }

    public void checkItemAbsence(String keywords) {
        Assert.assertNull("<" + keywords + "> по-прежнему в корзине.", getItem(keywords));
    }

    public void checkItemPresence(String keywords) {
        Assert.assertNotNull("<" + keywords + "> нет в корзине.", getItem(keywords));
    }

    public void remove(String keywords) {
        int currentTotalPrice = textElementToInt(waitForElement(totalPriceText));
        getItem(keywords).findElement(minusButtonLocator).click();
        Basket.remove(keywords);
        wait.until((driver -> currentTotalPrice != textElementToInt(wait
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath(totalPriceXpath))))));
        WebElement element = getItem(keywords);
        if (Basket.isAbsent(keywords) && element != null)
            wait.until(ExpectedConditions.invisibilityOf(element));
    }

    public void add(String keywords) {
        int currentTotalPrice = textElementToInt(waitForElement(totalPriceText));
        getItem(keywords).findElement(plusButtonLocator).click();
        Basket.add(keywords);
        wait.until((driver -> currentTotalPrice != textElementToInt(wait
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath(totalPriceXpath))))));
    }

    public void getBackRemovedItem() {
        int currentTotalPrice = textElementToInt(waitForElement(totalPriceText));
        Product product = Basket.getLastRemovedItem();
        if (product != null) {
            waitForElement(removedItemLink).click();
            Basket.add(Basket.getLastRemovedItem());
            wait.until((driver -> currentTotalPrice != textElementToInt(wait
                    .until(ExpectedConditions.presenceOfElementLocated(By.xpath(totalPriceXpath))))));
        }
    }

    private WebElement getItem(String keywords) {
        return waitForElements(itemLinks)
                .stream()
                .filter(element -> element.findElement(nameLinkLocator).getText().contains(keywords))
                .findFirst().orElse(null);
    }
}
