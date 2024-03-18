package citrusTest.tests;

import com.consol.citrus.actions.ExecuteSQLAction;
import com.github.javafaker.Faker;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import javax.sql.DataSource;

import static com.consol.citrus.actions.EchoAction.Builder.echo;
import static com.consol.citrus.actions.ExecuteSQLQueryAction.Builder.query;


public class SqlTest extends TestNGCitrusSpringSupport {

    @Autowired
    public DataSource sqlHelper;
    private TestContext context;

    @Test(testName = "Проверка курса из базы", enabled = true, dataProvider = "usersFromPostgres")
    @CitrusTest
    public void checkUserCourseFromDB(String username, String course) {

        run(
            query(sqlHelper)
                    .statement("SELECT * FROM public.users where username = '" + username + "';")
                    .validate("COURSE", course)
        );
    }

    @Test(testName = "Добавление нового пользователя", enabled = true, dataProvider = "temporaryUsersForPostgres")
    @CitrusTest
    public void addNewUserIntoDB(String username, String newCourse, String email, String age) {

        run(
                ExecuteSQLAction.Builder.sql(sqlHelper)
                                .statement("INSERT INTO public.users\n" +
                                        "(user_id, username, course, email, age)\n" +
                                        "VALUES(nextval('users_user_id_seq'::regclass), '" + username + "', '" + newCourse + "', '" + email + "', " + age + ");")
        );

        run(
                query(sqlHelper)
                        .statement("SELECT * FROM public.users where username = '" + username + "';")
                        .validate("COURSE", newCourse)
                        .validate("EMAIL", email)
        );

    }

    @Test(testName = "Удаление последнего пользователя", enabled = true)
    @CitrusTest
    public void removeLastUserFromDB() {

        run(
                query(sqlHelper)
                        .statement("SELECT Max(user_id) FROM public.users;")
                        .extract("max", "userIndexBeforeRemoving")
        );

        run(
                ExecuteSQLAction.Builder.sql(sqlHelper)
                        .statement("delete from public.users where user_id = (SELECT Max(user_id) FROM public.users);")
        );

        run(
                query(sqlHelper)
                        .statement("SELECT Max(user_id) FROM public.users;")
                        .extract("max", "userIndexAfterRemoving")
        );

        run(
                echo("Index before removing = ${userIndexBeforeRemoving} \n Index after removing = ${userIndexAfterRemoving}")
        );

    }

    @DataProvider(name = "usersFromPostgres")
    public Object[][] cardTypeProvider() {
        return new Object[][]{
                new Object[]{"Oleg", "QA junior"},
                new Object[]{"Alex", "QA senior"},
                new Object[]{"Ivan", "QA middle"},
        };
    }

    @DataProvider(name = "temporaryUsersForPostgres")
    public Object[][] fakeUsersProvider() {
        Faker faker = new Faker();
        return new Object[][]{
                new Object[]{faker.name().name(),
                            faker.beer().name(),
                            faker.bothify("????##@gmail.com"),
                            faker.number().digits(2)}
        };
    }
}
