package selenium.login;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class DashboardLogin {
	static Map finalmap = null;
	static String Frequency_Type = "";
	static String Job_Title = "";
	static FileReader reader = null;
	static Properties prop = null;

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		Select select = null;
		finalmap = getvaluefromExcel();
		boolean Frequency_Type_check;
		boolean Job_Title_check;

		for (int i = 1; i <= finalmap.size(); i++) {

			Frequency_Type_check = false;
			Job_Title_check = false;

			System.setProperty(prop.getProperty("WebDriver"), prop.getProperty("WebDriverPath"));
			WebDriver driver = new FirefoxDriver();

			driver.get(prop.getProperty("WebsiteURL"));
			driver.manage().window().maximize();
			select = new Select(driver.findElement(By.cssSelector("#ddl_LoginLanguageName")));

			select.selectByVisibleText("Users");
			driver.findElement(By.xpath("//input[@name=\"username\"]")).sendKeys(prop.getProperty("userName"));
			driver.findElement(By.xpath("//input[@name=\"userpassword\"]")).sendKeys(prop.getProperty("password"));

			driver.findElement(By.xpath("//button[@id=\"btn_Loginbutton\"]")).click();

			Thread.sleep(2000);

			if (i % 2 != 0) {
				Frequency_Type = (String) finalmap.get(i);
			}
			if ((i = i + 1) % 2 == 0) {
				Job_Title = (String) finalmap.get(i);
			}

			driver.get("http://23.101.22.93/testfms/#/AddNewEmployeeAppraisal");
			Thread.sleep(1000);
			select = new Select(driver.findElement(By.xpath("//select[@name=\"TypeID\"][@id=\"ddl_Frequencytype\"]")));
			Thread.sleep(1000);
			if (Frequency_Type.equals("Yearly") || Frequency_Type.equals("Monthly")) {

				select.selectByVisibleText(Frequency_Type);
				Frequency_Type_check = true;
			}

			select = new Select(driver.findElement(By.xpath("//select[@name=\"TypeID\"][@id=\"ddl_assetstype\"]")));
			Thread.sleep(1000);
			if (Job_Title.equals("Electrician") || Job_Title.equals("Tester") || Job_Title.equals("Develeoper")) {
				select.selectByVisibleText(Job_Title);
				Job_Title_check = true;
			}
			driver.findElement(By.xpath("//input[@type=\"checkbox\"]")).click();

			if (Frequency_Type_check != false && Job_Title_check != false) {
				// we need to click on save button

				System.out.println(
						" TestCase ---> PASS  : Frequency_Type:" + Frequency_Type + " Job_Title :" + Job_Title);
				System.out.println("Click on SAVE");
			}

			else {
				System.out.println("TestCase ---> FAILED : Frequency_Type:" + Frequency_Type + " Job_Title :"
						+ Job_Title + " Didn't Match in site");
			}
			Thread.sleep(2000);
			driver.quit();
		}

	}

	private static Map getvaluefromExcel() {
		// TODO Auto-generated method stub
		try {

			reader = new FileReader("Testing.properties");
			prop = new Properties();
			prop.load(reader);
			Cell cell = null;
			String xmlpath = (String) prop.get("xmlpath");
			File file = new File(xmlpath);
			System.out.println(prop.get("xmlpath"));
			FileInputStream fis = new FileInputStream(file);
			XSSFWorkbook wb = new XSSFWorkbook(fis);
			XSSFSheet sheet = wb.getSheetAt(0);
			Iterator<Row> itr = sheet.rowIterator(); // iterating over excel file
			int count = 0;
			finalmap = new HashMap();
			while (itr.hasNext()) {

				Row row = itr.next();
				Iterator<Cell> cellIterator = row.cellIterator(); // iterating over each column
				Map map = new HashMap();
				while (cellIterator.hasNext() && !cellIterator.equals(null)) {
					if (count == 0) {
						break;
					}

					cell = cellIterator.next();

					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_STRING: // field that represents string
						System.out.print(cell + " ");
						map.put(count, cell.getStringCellValue());
						count++;
						break;

					case Cell.CELL_TYPE_BLANK:
						break;
					default:
					}

				}
				if (count == 0) {
					count++;
				}
				finalmap.putAll(map);
				System.out.println("");
			}
		} catch (Exception e) {
			System.out.println("Exception occured" + e);
		}
		return finalmap;

	}

}
