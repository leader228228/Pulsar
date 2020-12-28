package moduleCore.handlers;

import moduleCore.dto.CreateClientHostDTO;
import moduleCore.entities.User;
import moduleCore.entities.UserStatus;
import moduleCore.entities.client.ClientHost;
import moduleCore.services.ClientHostService;
import moduleCore.services.UserService;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashSet;
import java.util.function.Function;

import static org.mockito.Mockito.*;

public class CreateClientHostHandlerTest {
    @Mock
    Logger LOGGER;
    @Mock
    UserService userService;
    @Mock
    ClientHostService clientHostService;
    @Mock
    Function<String, CreateClientHostDTO> bodyConverter;
    @InjectMocks
    CreateClientHostHandler createClientHostHandler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testHandle() throws Exception {
        when(userService.findByEmail(anyString())).thenReturn(new User(new User.UserID("email"), "username", "firstName", "lastName", 0, "phoneNumber", "password", new HashSet<UserStatus>(Arrays.asList(new UserStatus("status")))));
        when(clientHostService.createForUserRequest(any(), any())).thenReturn(new ClientHost(null, "publicKey", new User(new User.UserID("email"), "username", "firstName", "lastName", 0, "phoneNumber", "password", new HashSet<UserStatus>(Arrays.asList(new UserStatus("status")))), "name"));

        createClientHostHandler.handle(null);
    }
}
