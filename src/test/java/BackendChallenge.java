import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.UUID;
import io.github.cdimascio.dotenv.Dotenv;

public class BackendChallenge {
    private static final Dotenv dotenv = Dotenv.load();
    private final String BASE_URL = dotenv.get("BASE_URL");
    private final String TOKEN = dotenv.get("TOKEN");
    private static int userId;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    public void createNewUser() {
        // Generar email único
        String uniqueEmail = "user_" + UUID.randomUUID().toString() + "@gorest.com";

        String requestBody = String.format("""
            {
                "name": "Valentina Dlr",
                "email": "%s",
                "gender": "female",
                "status": "active"
            }
        """, uniqueEmail);

        Response response = RestAssured
                .given()
                .header("Authorization", TOKEN)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .post();

        // Valida código de respuesta esperado
        Assert.assertEquals(201, response.getStatusCode());
        System.out.println("User created: " + response.asString());

        // Guardar el ID del usuario generado para pruebas futuras
        userId = response.jsonPath().getInt("id");
        System.out.println("User created: " + userId);
    }

    @Test
    public void getAllUsers() {
        Response response = RestAssured
                .given()
                .header("Authorization", TOKEN)
                .get();

        // Valida el código de respuesta esperado
        Assert.assertEquals(200, response.getStatusCode());
        System.out.println("Users list: " + response.asString());
    }

    @Test
    public void getUserDetails() {
        // Reutiliza el mismo Id previamente generado en el test "createNewUser"

        Response response = RestAssured
                .given()
                .header("Authorization", TOKEN)
                .get("/" + userId);

        // Valida el código de respuesta esperado
        Assert.assertEquals(200, response.getStatusCode());
        System.out.println("User details: " + response.asString());
    }
}
