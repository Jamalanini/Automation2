import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;
import java.util.List;
import java.util.Random;

public class MyTestCases  extends MyDatabase{

    WebDriver driver = new EdgeDriver();

    String myWebSite = "https://automationteststore.com/";

    String SignupPage = "https://automationteststore.com/index.php?rt=account/create";

    @BeforeTest
    public void mySetup() throws SQLException {

        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/classicmodels","root","0000");
        driver.get(myWebSite);
        driver.manage().window().maximize();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
// hi
    }

    @Test (priority = 1, enabled = true)
    public void CreateData() throws SQLException {

        String query = "Insert into customers (customerNumber, customerName, contactLastName, contactFirstName, phone, addressLine1, city, country, salesRepEmployeeNumber, creditLimit) Values (999, 'Abc Company', 'ali', 'ahmad', '962797700235', '123 Main St', 'Los Angeles', 'United States', 1370, 50000.00) ";

        stmt = con.createStatement();

        int RowsInserted = stmt.executeUpdate(query);


    }

    @Test (priority = 3, enabled = true)
    public void ReadData() throws SQLException {

        String Query = "Select * from customers where customerNumber = 999";
        stmt = con.createStatement();
        Res = stmt.executeQuery(Query);

        while (Res.next()) {
            int customerNumberInDataBase = Res.getInt("customerNumber");

            CustomerFirstNameInDataBase = Res.getString("contactFirstName").toString().trim();

            CustomerLastNameInDataBase = Res.getString("contactLastName").toString().trim();

            CustomerCountryInDataBase = Res.getString("country").toString().trim();

            CustomerTelephoneInDataBase = Res.getString("phone").toString().trim();

            CustomerAddressInDataBase = Res.getString("addressLine1").toString().trim();

            email = CustomerFirstNameInDataBase + CustomerLastNameInDataBase + "@gmail.com";

            password = "123!@#P@ssw0rd";

            Name = CustomerFirstNameInDataBase + CustomerLastNameInDataBase + customerNumberInDataBase ;


        }
    }

    @Test (priority = 2, enabled = true)
    public void UpdateData() throws SQLException {

        String Query = "UPDATE customers SET contactLastName = 'mohammad' WHERE customerNumber = 999";

        stmt = con.createStatement();

        int rowInserted = stmt.executeUpdate(Query);


    }

    @Test (priority = 4 , enabled = true)
    public void DeleteData() throws SQLException {

        String query = "delete from customers where customerNumber =999";

        stmt = con.createStatement();

        int rowInserted = stmt.executeUpdate(query);
    }

    @Test(priority = 5,enabled = true)
    public void SignupTest() throws InterruptedException {

        driver.navigate().to(SignupPage);

        //FirstElements

        WebElement FirstName = driver.findElement(By.id("AccountFrm_firstname"));
        WebElement LastName = driver.findElement(By.id("AccountFrm_lastname"));
        WebElement Email = driver.findElement(By.id("AccountFrm_email"));
        WebElement Telephone = driver.findElement(By.id("AccountFrm_telephone"));
        WebElement Fax = driver.findElement(By.id("AccountFrm_fax"));

        FirstName.sendKeys(CustomerFirstNameInDataBase);
        LastName.sendKeys(CustomerLastNameInDataBase);
        Email.sendKeys(email);
        Telephone.sendKeys(CustomerTelephoneInDataBase);
        Fax.sendKeys(TheFax);

        Thread.sleep(1500);

        //SecondElements

        WebElement AddressOne = driver.findElement(By.id("AccountFrm_address_1"));
        WebElement TheCountry = driver.findElement(By.id("AccountFrm_country_id"));
        WebElement TheState = driver.findElement(By.id("AccountFrm_zone_id"));

        Select MySelectedCountry = new Select(TheCountry);
        Select MySelectedState = new Select(TheState);

        MySelectedCountry.selectByVisibleText(CustomerCountryInDataBase);
        Thread.sleep(1500);
        MySelectedState.selectByIndex(1);
        AddressOne.sendKeys(CustomerAddressInDataBase);

        List <WebElement> AlltheStates = TheState.findElements(By.tagName("option"));

        String TheCity = AlltheStates.get(1).getText();

		WebElement TheCityInput = driver.findElement(By.id("AccountFrm_city"));
        TheCityInput.sendKeys(TheCity);

		WebElement ThePostalCode = driver.findElement(By.id("AccountFrm_postcode"));
        ThePostalCode.sendKeys(PostalCode);

        Thread.sleep(1500);

        //LoginDetails

        WebElement LoginName = driver.findElement(By.id("AccountFrm_loginname"));
		WebElement ThePassword = driver.findElement(By.id("AccountFrm_password"));
		WebElement TheConfirmPassword = driver.findElement(By.id("AccountFrm_confirm"));
		WebElement AgreeCheckBox = driver.findElement(By.id("AccountFrm_agree"));
		WebElement CountinueButton = driver.findElement(By.xpath("//button[@title='Continue']"));

        LoginName.sendKeys(Name);
        ThePassword.sendKeys(password);
        TheConfirmPassword.sendKeys(password);
        AgreeCheckBox.click();
        CountinueButton.click();

        boolean ActualMessageForSignUp = driver.getPageSource().contains(ExpectedSignUpMassage);

        Assert.assertEquals(ActualMessageForSignUp,true);




    }

    @Test (priority = 6,enabled = true)
    public void LogoutTest() throws InterruptedException {

        Thread.sleep(2000);
        driver.findElement(By.partialLinkText("Logo")).click();;




        boolean ActualValueForLogout = driver.getPageSource().contains(ExpectedLogoutMessage);

        Assert.assertEquals(ActualValueForLogout, true);
    }

    @Test(priority = 7,enabled = true)

    public void Login() {

        WebElement Login = driver.findElement(By.linkText("Login or register"));
        Login.click();

        WebElement LoginName = driver.findElement(By.cssSelector("#loginFrm_loginname"));
        WebElement Password = driver.findElement(By.cssSelector("#loginFrm_password"));

        LoginName.sendKeys(Name);
        Password.sendKeys(password);

        WebElement LoginButton = driver.findElement(By.cssSelector("button[title='Login']"));
        LoginButton.click();

        boolean ActualValueForLogin = driver.getPageSource().contains(ExpectedLoginMessage);

        Assert.assertEquals(ActualValueForLogin, true);



    }

    @Test(priority = 8,invocationCount = 4 , enabled = true)
    public void AddItemToTheCart() {
        driver.navigate().to(myWebSite);
        Random rand = new Random();

        for (int i = 0; i < 10; i++) {

            List<WebElement> items = driver.findElements(By.className("prdocutname"));
            int randomItem = rand.nextInt(items.size());
            items.get(randomItem).click();


            boolean outOfStock = driver.getPageSource().contains("Out of Stock");
            boolean blockedProduct = driver.getCurrentUrl().contains("product_id=116");

            if (!outOfStock && !blockedProduct) {
                driver.findElement(By.cssSelector(".cart")).click();
                System.out.println("Added to cart: " + driver.getCurrentUrl());
                return;
            }

            driver.navigate().back();
        }

        throw new RuntimeException("No in-stock item found after 10 attempts.");
    }
}
