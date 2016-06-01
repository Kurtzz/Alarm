package pl.edu.agh.ki.io.alarm.server.communication;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MessageSerializerTest {

    private MessageSerializer instance;

    @Before
    public void before() {
        this.instance = new MessageSerializer();
    }

    @Test
    public void shouldSerializeMessage() throws JsonProcessingException {

        // given
        GcmMessage message = new GcmMessage();
        message.setTo("token");

        GcmMessageData data = new GcmMessageData();
        data.setMessage("message");
        data.setGroupName("someGroup");
        data.setSenderNick("sender");
        data.setSenderUID("uid");

        message.setData(data);

        // when
        String serializedMessage = instance.serialize(message);

        // then
        assertNotNull(serializedMessage);
        String expected = "{\"data\":{\"message\":\"message\",\"senderNick\":\"sender\",\"senderUID\":\"uid\",\"groupName\":\"someGroup\"},\"to\":\"token\"}";
        assertEquals(serializedMessage.replace("\\s*", ""), expected);
    }

}