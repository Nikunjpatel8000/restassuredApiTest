import java.lang.reflect.Array;
import java.util.List;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.URI;
import java.util.ArrayList;

import static  io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

public class StudentAPITests {

    /*given()
    arrange (base uri, path, parameters etc)
    when()
    set methods get, post, put, delete etc
    then()
    check response
     */
    @BeforeClass
    public static void Setup()
    {
        RestAssured.baseURI ="http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "student";
    }

    @Test
    public void GetStudentById_CheckResponse()
    {
        Response response = given()
                //.baseUri("http://localhost:8080")
                //.basePath("student")
                .when()
                .get("/2");
        System.out.println(response.statusCode());
        System.out.println(response.asString());
        System.out.println(response.prettyPrint());
    }

    @Test
    public void GetStudentById_UsingPathParameter() {
        /**/

        Response response = given()
                .pathParam("id", 5)
                .when()
                .get("{id}");
        System.out.println(response.statusCode());
    }

    @Test
    public void GetById_InLoop()
    {
        for (int i = 1; i < 5; i++) {
            given()
                    .pathParam("id", i)
                    .when()
                    .get("{id}")
                    .then()
                    .statusCode(200);
            System.out.println("id: " + i + " tested");
        }

    }

    @Test
    public void WhenId_NotFound_ItShouldReturn_404()
    {
            given()
                    .pathParam("id", 0)
                    .when()
                    .get("{id}")
                    .then()
                    .statusCode(404);
    }

    @Test
    public void WhenGetById_IWantToTestName()
    {
        given()
                .pathParam("id", 100)
                .when()
                .get("{id}")
                .then()
                .body("firstName", is("Oscac"));
    }

    @Test
    public void WhenGetByEmail_IWantT0TestEmailAddress()

    {
        Response response = given()
                .pathParam("id", 1)
                .when()
                .get("{id}");

        //System.out.println(response.as(Student.Class));

        given()
                .pathParam("id",1)
                .when()
                .get("{id}")
                .then()
                .body("email",equalTo("egestas.rhoncus.Proin@massaQuisqueporttitor.org"));
    }

        @Test
    public void It_Should_GetAllStudent()
    {
        given()
            .baseUri("http://localhost:8080")
            .basePath("student")
            .when()
            .get("list")
            .then()
            .statusCode(200);
    }

    /*********  POST ********/

    @Test
    public void Post_ToCreateNewStudent()
    {
        String bodyString = "{\"firstName\":\"Test4\",\"lastName\":\"Harper\",\"email\":\"test22@rest3.org\",\"programme\":\"Computer Science\",\"courses\":[\"JAVA\",\"C++\"]}";
        Response post = given()
                .contentType(ContentType.JSON)
                .body(bodyString)
                .post()
                ;
        post.then().statusCode(201);

        //System.out.println(post.asString());

        Student student = GetLastStudent();
    }

    @Test //POJO - plain old java object
    public void Post_ToCreateNewStudentUsing_POJO()
    {
        Student student = new Student();
        student.setFirstName("test2");
        student.setLastName("test last");
        student.setEmail("mytest@gmail.com");
        student.setProgramme("Finance Maths");
        ArrayList<String> courses = new ArrayList<String>();
        courses.add("JAVA");
        courses.add("c#");
        student.setCourses(courses);

        Response post = given()
                .contentType(ContentType.JSON)
                .body(student)
                .post();
        System.out.println(post.asString());
    }

    @Test
    public void PUT_ToUpdateStudent()
    {
        String bodyString = "{\"id\":101,\"firstName\":\"Test2 Updated\",\"lastName\":\"Harper\",\"email\":\"test1@rest1.org\",\"programme\":\"Computer Science\",\"courses\":[\"JAVA\",\"C++\"]},{\"id\":103,\"firstName\":\"test2\",\"lastName\":\"test last\",\"email\":\"mytest@gmail.com\",\"programme\":\"Finance Maths\",\"courses\":[\"JAVA\",\"c#\"]}]";

        Response post = given()
                .contentType(ContentType.JSON)
                .body(bodyString)
                .put("/101");
        System.out.println(post.asString());
    }

    @Test
    public void DELETE_ToUpdateStudent()
    {
        //First Create
        Response post = given()
                .pathParam("id", 101)
                .delete("{id}");
        System.out.println(post.asString());
    }

    @Test
    public void Get_ApplyFilter_usingQueryString()
    {
        Response response = given()
                .param("programme", "Computer Science")
                .param("limit", 2)
                .get("list");

        System.out.println(response.asString());
    }

    @Test
    public void Get_Into_POJO()
    {
        Response response = given()
                .pathParam("id", 1)
                .get("{id}");

        System.out.println(response.asString());
        Student student = response.as(Student.class);

        Assert.assertEquals("Vernon", student.getFirstName());
    }

    @Test
    public void Get_Into_POJOList() {
        Response response = given()
                .get("list");

        System.out.println(response.asString());
        Students students = response.as(Students.class);

    }

    private int GetStudentsCount()
    {
        Response response = given()
                .get("list");

        System.out.println(response.asString());
        Students students = response.as(Students.class);
        return students.size();
    }

    private Student GetLastStudent()
    {
        Response response = given()
                .get("list");

        System.out.println(response.asString());
        Students students = response.as(Students.class);
        Student last = students.get(students.size() - 1);
        return last;
    }
}
