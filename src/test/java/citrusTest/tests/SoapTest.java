package citrusTest.tests;

import helpers.MockSoapDollarsResponse;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SoapTest extends TestNGCitrusSpringSupport {

    public TestContext context;

    @Test(testName = "Проверка английского на примере $", dataProvider = "dollarsPairs")
    @CitrusTest
    public void checkDollarsCountString(String dollarCount, String dollarString) {
        run(applyBehavior(new MockSoapDollarsResponse("http://www.dataaccess.com/webservicesserver/",
                dollarCount, dollarString, context)));
    }

    @DataProvider(name = "dollarsPairs")
    public Object[][] cardTypeProvider() {
        return new Object[][]{
                new Object[]{"15", "fifteen dollars"},
                new Object[]{"1000000", "one million dollars"},
                new Object[]{"999", "nine hundred and ninety nine dollars"},
        };
    }

}
