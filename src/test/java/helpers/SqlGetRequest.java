package helpers;

import static com.consol.citrus.actions.ExecuteSQLQueryAction.Builder.query;

import com.consol.citrus.TestActionRunner;
import com.consol.citrus.TestBehavior;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;

public class SqlGetRequest implements TestBehavior {

    private TestContext context;
    public String pathUri;

    public SqlGetRequest(String pathUri, TestContext context) {
        this.context = context;
        this.pathUri = pathUri;
    }

    @Autowired
    public DataSource sqlHelper;

    /***
     * По аналогии не заработал @TODO надо бы добить ApplyBehaviorSql
     **/
    @Override
    @CitrusTest
    public void apply(TestActionRunner testActionRunner) {
        testActionRunner.run(
                query(sqlHelper)
                        .statement("SELECT * FROM public.users where username = 'Oleg';")
        );
    }

}
