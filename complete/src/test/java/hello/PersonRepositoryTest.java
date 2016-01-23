package hello;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class PersonRepositoryTest {

    private MockMvc mockMvc;

    @Autowired
    private PersonRepository repository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp(){
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        Person homer = new Person();
        homer.setFirstName("Homer");
        homer.setLastName("Simpson");

        Person bart = new Person();
        bart.setFirstName("Bart");
        bart.setLastName("Simpson");

        repository.save(homer);
        repository.save(bart);
    }

    @Test
    public void validJsonPost() throws Exception {
        mockMvc.perform(post("/people/").
                content("{\"firstName\": \"Marge\", \"lastName\": \"Simpson\"}")).
                andExpect(status().isCreated());
    }

    @Test
    public void invalidJsonPost() throws Exception {
        mockMvc.perform(post("/people/").
                content("{\"unknownPost\": \"unknown\", " +
                        "\"firstName\": \"Lisa\", \"lastName\": \"Simpson\"}")).
                andExpect(status().isBadRequest());
    }

    @Test
    public void validJsonPut() throws Exception {
        mockMvc.perform(put("/people/1").
                content("{\"firstName\": \"Homer\", \"lastName\": \"Simpson\"}")).
                andExpect(status().isNoContent());
    }

    @Test
    public void invalidJsonPut() throws Exception {
        mockMvc.perform(put("/people/2").
                content("{\"unknownPut\": \"unknown\", " +
                        "\"firstName\": \"Bart\", \"lastName\": \"Simpson\"}")).
                andExpect(status().isBadRequest());
    }

    @Test
    public void invalidJsonPutNoObjectWithId() throws Exception {
        mockMvc.perform(put("/people/10000").
                content("{\"unknownPutNoObjectWithId\": \"unknown\", " +
                        "\"firstName\": \"Maggie\", \"lastName\": \"Simpson\"}")).
                andExpect(status().isBadRequest());
    }


}
