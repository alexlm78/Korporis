package dev.kreraker.korporis;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
class KorporisApplicationTests {

   @Test
   void healthCheck() {
      given()
         .when().get("/api/departments")
         .then()
         .statusCode(200);
   }
}
