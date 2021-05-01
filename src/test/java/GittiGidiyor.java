import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


import java.util.concurrent.TimeUnit;


public class GittiGidiyor extends GittiPage {
    @Test
    public void Test() throws InterruptedException {
        /* webdriver dosya yolu ve ayarlar  */
        ChromeOptions  options = new ChromeOptions();
        options.addArguments("start-maximized");
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver(options);
        driver.get(url);
        logger.info("||||| Test Başladı |||||");
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        anaSayfa();
        giris();
        girisYapildiMi();
        urunAra();
        sepetTest();
    }

    @After
    public void finish() {
        driver.quit();
        logger.info("||||| TEST SONLANDI |||||");

    }

}
