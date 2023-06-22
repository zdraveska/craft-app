package mk.ukim.finki.draftcraft.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;

public abstract class BaseIntegrationTest extends BaseTestData {

    @Autowired
    protected MockMvc mockMvc;

    protected ObjectMapper objectMapper = new ObjectMapper().configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(new JavaTimeModule());

    protected final GreenMail smtpServer = new GreenMail(new ServerSetup(3025, "localhost", "smtp"));

//    @RegisterExtension
//    protected static GreenMailExtension smtpServer = new GreenMailExtension(ServerSetupTest.SMTP)
//            .withConfiguration(GreenMailConfiguration.aConfig().withUser("test@gmail.com", "springboot"))
//            .withPerMethodLifecycle(false);
    @BeforeEach
    public void beforeAll() {
        smtpServer.start();
    }

    @AfterEach
    public void afterAll(){
        smtpServer.stop();
    }

    // utils
    public <T> T parseResponse(MvcResult mvcResult, Class<T> responseClass){
        try {
            String contentAsString = mvcResult.getResponse().getContentAsString();
            return objectMapper.readValue(contentAsString, responseClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
