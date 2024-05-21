import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.todos.TodosApplication;
import com.todos.model.Resource;
import com.todos.model.TodoResource;
import lombok.val;
import org.assertj.core.api.Assertions;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.UnsupportedEncodingException;
import java.util.Collection;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = TodosApplication.class)
@AutoConfigureMockMvc
@Sql({"/test-schema.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TodoIntegrationTest {

    private final ObjectMapper mapper = new ObjectMapper();
    private final EasyRandom generator = new EasyRandom();

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCreateTodo() throws Exception {
        val todoResource = generator.nextObject(TodoResource.class);
        createTodo(todoResource).andExpect(status().isCreated());
    }

    @Test
    void testGetTodo() throws Exception {
        val todoResource = generator.nextObject(TodoResource.class);
        final ResultActions resultActions = createTodo(todoResource);
        Resource<TodoResource> resource = getTodoResource(resultActions);

        this.mockMvc.perform(get("/todos/" + resource.getContent().getId()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllTodo() throws Exception {
        createTodo(generator.nextObject(TodoResource.class));
        createTodo(generator.nextObject(TodoResource.class));

        final ResultActions resultActions = this.mockMvc.perform(get("/todos"))
                .andDo(print())
                .andExpect(status().isOk());

        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        final Collection<Resource<TodoResource>> resources = mapper.readValue(contentAsString, new TypeReference<>() {
        });

        Assertions.assertThat(resources.size()).isEqualTo(2);
    }

    @Test
    void testDeleteTodo() throws Exception {
        val todoResource = generator.nextObject(TodoResource.class);
        final ResultActions resultActions = createTodo(todoResource);
        Resource<TodoResource> resource = getTodoResource(resultActions);

        this.mockMvc.perform(delete("/todos/" + resource.getContent().getId()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteAllTodo() throws Exception {
        createTodo(generator.nextObject(TodoResource.class));
        createTodo(generator.nextObject(TodoResource.class));

        this.mockMvc.perform(delete("/todos"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }


    @Test
    void testUpdateTodo() throws Exception {
        val oldTodoResource = generator.nextObject(TodoResource.class);
        final ResultActions resultActions = createTodo(oldTodoResource);
        Resource<TodoResource> resource = getTodoResource(resultActions);

        final TodoResource newTodoResource = generator.nextObject(TodoResource.class);
        newTodoResource.setId(oldTodoResource.getId());

        final ResultActions updatedResultActions = this.mockMvc.perform(patch("/todos/" + resource.getContent().getId())
                .content(mapper.writeValueAsBytes(newTodoResource))
                .contentType(MediaType.APPLICATION_JSON)
        );

        //Then
        updatedResultActions.andExpect(status().isOk());
        final Resource<TodoResource> updatedTodoResource = getTodoResource(updatedResultActions);
        Assertions.assertThat(updatedTodoResource.getContent().getTitle()).isEqualTo(newTodoResource.getTitle());

    }

    private Resource<TodoResource> getTodoResource(ResultActions resultActions) throws UnsupportedEncodingException, JsonProcessingException {
        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        return mapper.readValue(contentAsString, new TypeReference<>() {
        });
    }

    private ResultActions createTodo(TodoResource todoResource) throws Exception {
        return this.mockMvc.perform(
                MockMvcRequestBuilders.post("/todos")
                        .content(mapper.writeValueAsBytes(todoResource))
                        .contentType(MediaType.APPLICATION_JSON)

        );
    }


}
