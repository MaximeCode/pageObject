package pageObjectTask.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import pageObjectTask.Basket;
import pageObjectTask.Product;

public class ItemPage extends BasePage {

    @FindBy(xpath = "//h1[contains(@class, 'page-title')]")
    private WebElement nameText;

    @FindBy(xpath = "//div[contains(text(), 'Код товара')]/span")
    private WebElement codeText;

    @FindBy(xpath = "//div[@class='clearfix']//span[@class='current-price-value']")
    private WebElement priceText;

    @FindBy(xpath = "//h2[contains(text(), 'Описание')]/../p")
    private WebElement descriptionText;

    @FindBy(xpath = "//div[contains(text(), 'гарантия')]//select")
    private WebElement warrantySelect;

    @FindBy(xpath = "//div[contains(@class, 'buttons-wrapper')]//span[contains(text(), 'Купить')]")
    private WebElement buyButton;

    private Product product;


    public ItemPage() {
        super();
        product = new Product();
        product.setCode(textElementToInt(waitForElement(codeText)));
        product.setName(waitForElement(nameText).getText());
        product.setPrice(textElementToInt(waitForElement(priceText)));
        product.setDescription(waitForElement(descriptionText).getText());
    }

    public void selectWarranty(int warranty) {
        int price = textElementToInt(waitForElement(priceText));
        new Select(waitForElement(warrantySelect)).selectByIndex(warranty);
        wait.until((driver -> price != textElementToInt(priceText)));
        product.setWarranty(warranty);
        product.setPrice(textElementToInt(waitForElement(priceText)));
    }

    public void buy() {
        if (Basket.getBasketStructure().isEmpty()) {
            waitForElement(buyButton).click();
            wait.until(ExpectedConditions.visibilityOf(basketPriceText));
        }
        else {
            int currentBasketPrice = textElementToInt(waitForElement(basketPriceText));
            waitForElement(buyButton).click();
            wait.until((driver -> currentBasketPrice != textElementToInt(wait
                    .until(ExpectedConditions.presenceOfElementLocated(By.xpath(basketPriceTextXpath))))));
        }
        Basket.add(product);
    }
}
