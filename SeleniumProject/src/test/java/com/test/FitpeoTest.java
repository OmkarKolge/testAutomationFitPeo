package com.test;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class FitpeoTest {
	public WebDriver driver;

	@BeforeClass
	public void setUp() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
	}

	@AfterClass
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}

	@Test(priority = 1)
	public void NavigateToFitPeoPage() {
		driver.navigate().to("https://www.fitpeo.com/");
		driver.manage().window().maximize();
		String expectedTitle = "FitPeo helps you with";
		String text = driver.findElement(By.xpath("//*[normalize-space()='FitPeo helps you with'] ")).getText();
		Assert.assertEquals(text, expectedTitle, "Page title does not match!");
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3000));
	}

	@Test(priority = 2)
	public void navigateTotheRevenueCalculator() {
		driver.navigate().to("https://fitpeo.com/revenue-calculator");
		String currentURL = driver.getCurrentUrl();
		Assert.assertTrue(currentURL.contains("calculator"), "URL does not contain the expected word: calculator");

	}

	@Test(priority = 3)
	public void scrollDownTotheSlider() {
		WebElement element = driver.findElement(By.xpath("//h4[normalize-space()='Medicare Eligible Patients']"));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", element);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3000));

	}

	@Test(priority = 4)
	public void adjustSlider() throws InterruptedException {
		WebElement slider = driver.findElement(By.xpath(
				"//h4[normalize-space()='Medicare Eligible Patients']/parent::div/child::div/span/span[contains(@class,'MuiSlider-thumbColorPrimary MuiSlider-thumb')]"));
		Point location = slider.getLocation();
		int x = location.getX();
		int y = location.getY();
		System.out.println("X: " + x + " Y: " + y);
		Actions actions = new Actions(driver);
		actions.clickAndHold(slider).moveToLocation(906, 654).release().sendKeys(Keys.ARROW_RIGHT)
				.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.ARROW_RIGHT).perform();
		location = slider.getLocation();
		x = location.getX();
		y = location.getY();
		System.out.println("X: " + x + " Y: " + y);
		Thread.sleep(3000);
		WebElement val = driver.findElement(By.xpath(
				"//h4[normalize-space()='Medicare Eligible Patients']/parent::div/child::div/div//input[@type='number']"));
		String value = val.getAttribute("value");
		System.out.println(value);
		Assert.assertEquals(value, "820");
	}

	@Test(priority = 5)
	public void insertValue() throws InterruptedException {

		insertTestField("560");
	}

	private void insertTestField(String string) throws InterruptedException {
		WebElement val = driver.findElement(By.xpath(
				"//h4[normalize-space()='Medicare Eligible Patients']/parent::div/child::div/div//input[@type='number']"));
		System.out.println(val.getAttribute("value"));
		Thread.sleep(3000);
		Actions actions = new Actions(driver);
		actions.click(val).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).sendKeys(Keys.DELETE).perform();

		val.sendKeys(string);
		Thread.sleep(3000);
	}

	@Test(priority = 6)
	public void validateValue() {
		WebElement val = driver.findElement(By.xpath(
				"//h4[normalize-space()='Medicare Eligible Patients']/parent::div/child::div/div//input[@type='number']"));
		System.out.println(val.getAttribute("value"));
		String value = val.getAttribute("value");
		Assert.assertEquals(value, "560");
	}

	@Test(priority = 7)
	public void selectCPTCode() throws InterruptedException {
		insertTestField("820");
		WebElement scroll = driver.findElement(By.xpath("//p[normalize-space()='CPT-99091']"));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView();", scroll);
		Thread.sleep(3000);
		driver.findElement(By.xpath("//p[normalize-space()='CPT-99091']/parent::div/child::label/span[2]")).click();
		driver.findElement(By.xpath("//p[normalize-space()='CPT-99453']/parent::div/child::label/span[2]")).click();
		driver.findElement(By.xpath("//p[normalize-space()='CPT-99454']/parent::div/child::label/span[2]")).click();
		scroll = driver.findElement(By.xpath("//p[normalize-space()='CPT-99474']"));
		js.executeScript("arguments[0].scrollIntoView();", scroll);
		driver.findElement(By.xpath("//p[normalize-space()='CPT-99474']/parent::div/child::label/span[2]")).click();
		Thread.sleep(3000);
	}

	@Test(priority = 8)
	public void totalRecurringReimbursement() throws InterruptedException {
		WebElement value = driver.findElement(By.xpath("//p[contains(text(),'Total Recurring Reimbursement for')]//p"));
		Thread.sleep(3000);
		String actualValue=value.getText();
		Assert.assertEquals(actualValue, "$110700");
	}
}