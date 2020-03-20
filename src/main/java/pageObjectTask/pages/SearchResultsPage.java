package pageObjectTask.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.util.List;

public class SearchResultsPage extends BasePage {

    @FindBy(xpath = "//a[@class='ui-link']")
    private List<WebElement> itemLinks;

    public ItemPage selectItem(String keywords) {

        waitForElements(itemLinks)
                .stream()
                .filter(element -> element.getText().contains(keywords))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("<" + keywords + "> отсутствует."))
                .click();
        return new ItemPage();
    }
}
