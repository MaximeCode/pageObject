package pageObjectTask.pages;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageObjectTask.Basket;
import pageObjectTask.Init;
import java.util.List;

abstract class BasePage {

    static WebDriver driver = Init.getDriver();
    static WebDriverWait wait = new WebDriverWait(driver, 10);

    @FindBy(xpath = "//nav//input")
    private WebElement searchInput;

    @FindBy(xpath = "//nav//form//span[contains(@class, 'ui-input-search__icon ui-input-search__icon_search')]")
    private WebElement searchButton;

    @FindBy(xpath = "//a[@class='ui-link cart-link']")
    private WebElement basketLink;

    final String basketPriceTextXpath = "//span[@class='cart-link__price']";
    @FindBy(xpath = basketPriceTextXpath)
    WebElement basketPriceText;

    @FindBy(xpath = "//h1")
    private WebElement h1;  //  вспомогательное для метода search()

    BasePage() {
        PageFactory.initElements(driver, this);
    }

    public BasePage search(String keywords) {
        waitForElement(searchInput).sendKeys(keywords);
        waitForElement(searchButton).click();
        if (waitForElement(h1).getText().contains("Найдено"))
            return new SearchResultsPage();
        return new ItemPage();
    }

    public BasketPage goToBasket() {
        waitForElement(basketLink).click();
        return new BasketPage();
    }

    WebElement waitForElement(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    List<WebElement> waitForElements(List<WebElement> elements) {
        return wait.until(ExpectedConditions.visibilityOfAllElements(elements));
    }

    public void checkTotalPrice() {
            Assert.assertEquals("Цена покупок не соответствует цене корзины.",
                    Basket.getTotalPrice(), textElementToInt(waitForElement(basketPriceText)));
    }

    int textElementToInt(WebElement textElement) {
        return Integer.parseInt(waitForElement(textElement)
                .getText().replaceAll("\\D", ""));
    }
}
