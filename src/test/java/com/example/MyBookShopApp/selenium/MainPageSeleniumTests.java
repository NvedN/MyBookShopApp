//package com.example.MyBookShopApp.selenium;
//
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.concurrent.TimeUnit;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class MainPageSeleniumTests {
//
//    private static ChromeDriver driver;
//
//    @BeforeAll
//    static void setup() {
//        System.setProperty("webdriver.chrome.driver", "C:/Users/Nikita/Downloads/chromedriver_win32/chromedriver.exe");
//        driver = new ChromeDriver();
//        driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
//    }
//
//    @AfterAll
//    static void tearDown() {
//        driver.quit();
//    }
//
//    @Test
//    public void testMainPageAccess() throws InterruptedException {
//        MainPage mainPage = new MainPage(driver);
//        mainPage
//                .callPage()
//                .pause();
//
//        assertTrue(driver.getPageSource().contains("BOOKSHOP"));
//    }
//
//    @Test
//    public void testMainPageSearchByQuery() throws InterruptedException {
//        MainPage mainPage = new MainPage(driver);
//        mainPage
//                .callPage()
//                .pause()
//                .setUpSearchToken("Sudden")
//                .pause()
//                .submit()
//                .pause();
//
//        assertTrue(driver.getPageSource().contains("Sudden Manhattan"));
//    }
//
//}