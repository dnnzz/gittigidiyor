import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class GittiPage {
    WebDriver driver;
    final static Logger logger = Logger.getLogger(GittiGidiyor.class.getName());
    public String url = "https://www.gittigidiyor.com/";


    // Ürünün stoklarda 2 den az olması durumunu kontrol eden yardımcı fonksiyon.
    public boolean isOptionExisted(String option , WebElement dropdownWebElement) {
        boolean result=true;
        try {

            Select selection= new Select(dropdownWebElement);
            selection.selectByVisibleText(option);

        }catch(Exception e) {
            result = false;
        }
        return result;
    }
    //---------------------------------------------------------------------------

    // Ana sayfa kontrolünü sağlayan adım.
    public void anaSayfa(){
        String mainPage =  driver.getCurrentUrl();
        assertEquals(url , mainPage);
        logger.info("Ana Sayfa kontrol !");
    }
    // html ID lere göre kullanıcı bilgilerini sayfaya yolluyoruz.
    public void giris(){
        driver.findElement(By.name("profile")).click();
        driver.findElement(By.cssSelector("a[data-cy=\"header-login-button\"]"))
                .click();
        driver.findElement(By.id("L-UserNameField"))
                .sendKeys("dnnzz0@icloud.com");
        driver.findElement(By.id("L-PasswordField"))
                .sendKeys("test123456");
        driver.findElement(
                By.id("gg-login-enter"))
                .click();
        logger.info("Giriş başarılı.");
    }
    // Giris kontrol.
    public void girisYapildiMi(){
        String username = driver.findElement(By.cssSelector("[class='gekhq4-4 egoSnI'] span")).getText();
        assertEquals(username, "denizfirat767800");
        logger.info("Giriş kontrol başarılı.");
    }
    // unique data-cy attiribute sayesinde anahtar kelimemizi sayfada arattık.
    public void urunAra(){
        driver.findElement(By.cssSelector("input[data-cy=header-search-input]")).
                sendKeys("bilgisayar");

        driver.findElement(By.cssSelector("button[data-cy=\"search-find-button\"]"))
                .click();
        logger.info("bilgisayar aratıldı.");

        JavascriptExecutor js = ((JavascriptExecutor) driver);
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        // cssSelector ile 2.sayfaya yonlendiren anchor html tagi bulup tıklattık.
        driver.findElement(By.cssSelector("a[href=\"/arama/?k=bilgisayar&sf=2\"]"))
                .click();
        js.executeScript("window.scrollTo(0,500)");
        logger.info("2.sayfaya geçildi.");
    }
    /* Sayfa basi 48 urun oldugundan dolayi random bir sayi cagirdik
    1-48 e kadar sayi ureten random classindan bir integer urettik.
    cssSelector yardimi ile urun listesinin icinde bulunan random bir
    nth.child li elemanini sectik.
    Ardından ürünün indirimli olup olmadığını kontrol edip sayfa fiyatını aldık.
    Sayfa fiyati alindiktan sonra sepete eklendi ve sepete gidildi.
    Sepet fiyati alindi ve üründen stokta 2 adet olup olmadigi kontrol edildi.
    Sepet boşaltıldı ve test bitirildi.
     */
    public void sepetTest() throws InterruptedException {
        int upper=47;
        Random random = new Random();
        int rand_item= random.nextInt(upper) + 1;
        String price = "";

        JavascriptExecutor js = ((JavascriptExecutor) driver);

        driver.findElement(By.cssSelector(".clearfix > ul > li:nth-child("+rand_item+")"))
                .click();
        Boolean exist = driver.findElements(By.cssSelector("[class=\"lowPrice hidden\"]")).size() > 0;
        System.out.println(exist);
        if(exist){
            WebElement productPrice = driver.findElement(By.id("sp-price-highPrice"));
            price = productPrice.getText();
            logger.info("Ürünün Sayfa Fiyatı : " + price);
        }
        if(!exist){
            WebElement productPrice = driver.findElement(By.id("sp-price-lowPrice"));
            price = productPrice.getText();
            logger.info("Ürünün Sayfa Fiyatı : " + price);
        }
        js.executeScript("window.scrollTo(0,650)");

        WebElement basketBtn = driver.findElement(By.cssSelector("[id='add-to-basket']"));
        JavascriptExecutor Basket = (JavascriptExecutor) driver;
        Basket.executeScript("arguments[0].click();", basketBtn);
        logger.info("Sepete eklendi !");
        Thread.sleep(1000);
        driver.findElement(By.className("header-cart-hidden-link")).click();
        Thread.sleep(1000);

        logger.info("Sepete gidildi !");

        WebElement basketPrice = driver.findElement(By.cssSelector("p[class=\"new-price\"]"));
        Thread.sleep(1000);
        String basket_price =  basketPrice.getText();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        logger.info("Ürünün sepet fiyatı : " + basket_price);
        Assert.assertEquals(price , basket_price);
        logger.info("Fiyat doğrulaması yapıldı");

        WebElement selectTagRef = driver.findElement(By.cssSelector("select.amount"));
        if(isOptionExisted("2",selectTagRef )){
            Select amount = new Select(selectTagRef);
            amount.selectByValue("2");
            Thread.sleep(1000);
            logger.info("Ürün adedi 2 yapıldı.");
        }else{
            logger.info("Ürün stokta 2 tane yok !");
        }
        driver.findElement(By.cssSelector("a.btn-delete")).click();
        logger.info("Sepet boş !");
    }
}
