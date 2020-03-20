package pageObjectTask;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Init {

    public static WebDriver driver;
    private static TestProperties properties = TestProperties.getInsance();


    public static void gettingStarted() {

        System.setProperty(properties.getProperty("driver_key","webdriver.chrome.driver"),
                properties.getProperty("driver_value","driver/chromedriver.exe"));

        if ("chrome".equals(properties.getProperty("browser")))
            driver = new ChromeDriver();
        else
            if ("firefox".equals(properties.getProperty("browser")))
                driver = new FirefoxDriver();

        driver.manage().window().maximize();
        driver.get(properties.getProperty("url", "https://www.dns-shop.ru/"));
    }

    public static void shutDown() {
        driver.quit();
    }

    public static WebDriver getDriver() {
        return driver;
    }
}
