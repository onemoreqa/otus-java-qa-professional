package helpers;


import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;
import javax.sql.DataSource;

import java.sql.SQLException;

import static com.consol.citrus.actions.EchoAction.Builder.echo;
import static com.consol.citrus.actions.ExecuteSQLQueryAction.Builder.query;

public class SqlHelperTest extends TestNGCitrusSpringSupport {

    @Autowired
    public DataSource sqlHelper;

    private TestContext context;

    @Test(description = "Test DB", enabled = false)
    @CitrusTest
    public void getTestActions() throws SQLException {
        this.context = citrus.getCitrusContext().createTestContext();

//        query((DataSource) sqlHelper.getConnection()
//                .nativeSQL("select * from roles").);
//        query(action -> action.dataSource(sqlHelper)
//                .statement("select * from roles")
//                .extract("role_name", "current_role"));

        echo("${current_role}");
    }
}
