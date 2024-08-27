package io.orkes.examples;

import com.netflix.conductor.common.metadata.tasks.TaskResult;
import com.netflix.conductor.common.metadata.workflow.StartWorkflowRequest;
import com.netflix.conductor.common.run.Workflow;
import io.orkes.conductor.client.ApiClient;
import io.orkes.conductor.client.OrkesClients;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.util.Map;

@Service
@RestController
public class BackendService {
    private static final String KEY = "KEY";
    private static final String SECRET = "SECRET";
    private static final String BASE_PATH = "https://___.orkesconductor.io/api";
    private final OrkesClients orkesClients;

    public BackendService() {
        ApiClient client = ApiClient.builder()
                .basePath(BASE_PATH)
                .keyId(KEY)
                .keySecret(SECRET)
                .build();
        this.orkesClients = new OrkesClients(client);
    }

    @PostMapping("start")
    public String doStart() {
        var req = new StartWorkflowRequest().withName("MultiStageFlow");
        return orkesClients.getWorkflowClient().startWorkflow(req);
    }

    @PostMapping("done/{workflowId}/{stage}")
    public String doDone(@PathVariable("workflowId") String workflowId, @PathVariable("stage") String stage) {
        orkesClients.getTaskClient().updateTask(workflowId, "wait_ref" + stage, TaskResult.Status.COMPLETED, Map.of());
        return "ok";
    }

    @GetMapping("details/{workflowId}")
    public Workflow getDetails(@PathVariable("workflowId") String workflowId) {
        return orkesClients.getWorkflowClient().getWorkflow(workflowId, true);
    }

    SseEmitter emitter = new SseEmitter(-1L);

    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter handleSse() {
        try {
            emitter.send("Hello from Server-Sent Events!");
        } catch (IOException e) {
            emitter.completeWithError(e);
        }
        return emitter;
    }

    @PostMapping("webhook")
    public void webhook() {
        try {
            emitter.send("webhook");
        } catch (IOException e) {
            emitter.completeWithError(e);
        }
    }
}
