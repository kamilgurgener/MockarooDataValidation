package mockarooproject;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertFalse;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class MockarooDataValidation {
    WebDriver driver;

    @BeforeClass // runs once for all tests
    public void setUp() {
	System.out.println("Setting up WebDriver in BeforeClass...");
	WebDriverManager.chromedriver().setup();
	driver = new ChromeDriver();
	driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
	// driver.manage().window().fullscreen();
    }
    @AfterClass
    public void tearDown() {
	driver.close();
    }

    
    @Test
    public void dataValidation() throws InterruptedException, IOException {
	//==============================  #2  ========================================

	driver.get("https://mockaroo.com");
	//==============================  #3  ========================================

	assertEquals(driver.getTitle(),
		"Mockaroo - Random Data Generator and API Mocking Tool | JSON / CSV / SQL / Excel");
	//==============================  #4  ========================================

	assertEquals(driver.findElement(By.xpath("//div[@class='brand']")).getText(), "mockaroo");
	assertEquals(driver.findElement(By.xpath("//div[@class='tagline']")).getText(), "realistic data generator");
	
	//==============================  #5  ========================================

	for (int i = 6; i >= 1; i--) {
	    driver.findElement(By.xpath("(//a[@class='close remove-field remove_nested_fields'])[" + i + "]")).click();
	}
	
	//==============================  #6  ========================================

	assertEquals(driver.findElement(By.xpath("//div[@class = 'column column-header column-name']")).getText(),
		"Field Name");
	assertEquals(driver.findElement(By.xpath("//div[@class = 'column column-header column-type']")).getText(),
		"Type");
	assertEquals(driver.findElement(By.xpath("//div[@class = 'column column-header column-options']")).getText(),
		"Options");
	//==============================  #7  ========================================

	assertTrue(driver.findElement(By.xpath("//a[@class = 'btn btn-default add-column-btn add_nested_fields']"))
		.isEnabled());
	//==============================  #8  ========================================

	assertEquals(driver.findElement(By.id("num_rows")).getAttribute("value"), "1000");
	
	//==============================  #9  ========================================

	Select select = new Select(driver.findElement(By.id("schema_file_format")));
	assertEquals(select.getFirstSelectedOption().getText(), "CSV");
	
	//==============================  #10 ========================================

	Select select1 = new Select(driver.findElement(By.id("schema_line_ending")));
	assertEquals(select1.getFirstSelectedOption().getText(), "Unix (LF)");
	
	//==============================  #11 ========================================


	assertTrue(driver.findElement(By.id("schema_include_header")).isSelected());
	assertFalse(driver.findElement(By.id("schema_bom")).isSelected());
	
	//==============================  #12  ========================================

	driver.findElement(By.xpath("//a[@class = 'btn btn-default add-column-btn add_nested_fields']"))
		.sendKeys(Keys.ENTER + "city");
	
	//==============================  #13  ========================================
	
	driver.findElement(By.xpath("//div[@id='fields']/div[7]/div[3]/input[3]")).click();
	Thread.sleep(1000);
	assertEquals(driver.findElement(By.xpath("//div[@id='type_dialog']/div/div/div[1]/h3")).getText(),
		"Choose a Type");
	
	//==============================  #14  ========================================
	
	driver.findElement(By.id("type_search_field")).sendKeys("city");
	driver.findElement(By.className("type-name")).click();
	
	//==============================  #15  ========================================

	driver.findElement(By.xpath("//a[@class = 'btn btn-default add-column-btn add_nested_fields']"))
		.sendKeys(Keys.ENTER + "country");
	Thread.sleep(1000);
	driver.findElement(By.xpath("//div[@id='fields']/div[8]/div[3]/input[3]")).click();
	Thread.sleep(1000);

	driver.findElement(By.id("type_search_field")).clear();
	driver.findElement(By.id("type_search_field")).sendKeys("country");
	driver.findElement(By.xpath("//div[@class = 'type-name' and .='Country']")).click();
	Thread.sleep(1000);
	
	//==============================  #16  ========================================

	driver.findElement(By.id("download")).click();

	//==============================  #17  ========================================
	Thread.sleep(2000);
	BufferedReader bf = new BufferedReader(new FileReader("C:\\Users\\kote4\\Downloads\\MOCK_DATA.csv"));
	List<String> data = new ArrayList<>();
	String temp = bf.readLine();
	while (temp != null) {
	    data.add(temp);
	    temp = bf.readLine();
	}
	//==============================  #18 ========================================

	assertEquals(data.get(0), "city,country");
	
	//==============================  #19  ========================================

	data.remove(0);
	assertEquals(data.size(), 1000);

	//==============================  #20  ========================================

	List<String> cities = new ArrayList<>();
	for (String str : data) {
	    cities.add(str.substring(0, str.indexOf(",")));
	}

	//==============================  #21  ========================================

	List<String> countries = new ArrayList<>();
	for (String str : data) {
	    countries.add(str.substring(str.indexOf(",") + 1));
	}

	//==============================  #22  ========================================

	Collections.sort(cities);
	String cityShort = cities.get(0);
	String cityLong = cities.get(0);
	for (int i = 1; i < cities.size(); i++) {
	    if (cityShort.length() > cities.get(i).length()) {
		cityShort = cities.get(i);
	    }
	    if (cities.get(i).length() > cityLong.length()) {
		cityLong = cities.get(i);
		}
	}
	System.out.println("The city with shortest name in the list is: "+cityShort);
	System.out.println("The city with longest name in the list is: "+cityLong);

	//==============================  #23  ========================================

	SortedSet<String> sortedCountry = new TreeSet<>(countries);
	for (String str : sortedCountry) {
		System.out.println(str + " was listed " + Collections.frequency(countries, str)+" times");
	}
	
	//==============================  #24  ========================================
	
	Set<String> citiesSet = new HashSet<>(cities);	
	
	//==============================  #25  ========================================

	int uniqueCityCount = 0;
	for (int i = 0; i < cities.size(); i++) {
		if (i == cities.lastIndexOf(cities.get(i)))
			uniqueCityCount++;
	}
	assertEquals(uniqueCityCount, citiesSet.size());
	//==============================  #26   ========================================
	
	Set<String> countrySet = new HashSet<>(countries);
	
	//==============================  #27   ========================================

	int uniqueCountryCount = 0;
	for (int i = 0; i < countries.size(); i++) {
		if (i == countries.lastIndexOf(countries.get(i)))
			uniqueCountryCount++;
	}
	assertEquals(uniqueCountryCount, countrySet.size());
    }
}
