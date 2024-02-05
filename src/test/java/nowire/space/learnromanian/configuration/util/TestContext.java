package nowire.space.learnromanian.configuration.util;

import java.util.HashMap;
import java.util.Map;

import static java.lang.ThreadLocal.withInitial;

public enum TestContext {

    CONTEXT;

    private static final String PAYLOAD = "PAYLOAD";
    private static final String RESPONSE_CODE = "RESPONSE_CODE";
    private static final String BEARER_TOKEN = "BEARER_TOKEN";
    private final ThreadLocal<Map<String, Object>> testContexts = withInitial(HashMap::new);

    public <T> T get(String name) {
        return (T) testContexts.get()
                .get(name);
    }

    public <T> T set(String name, T object) {
        testContexts.get()
                .put(name, object);
        return object;
    }

    public <T> void setResponseCode(T object) {
        set(RESPONSE_CODE, object);
    }

    public Object getResponseCode() {
        return get(RESPONSE_CODE);
    }

    public <T> void setBearerToken(T object) {
        set(BEARER_TOKEN, object);
    }

    public Object getBearerToken() {
        return get(BEARER_TOKEN);
    }

    public Object getPayload() {
        return get(PAYLOAD);
    }

    public <T> void setPayload(T object) {
        set(PAYLOAD, object);
    }

    public void reset() {
        testContexts.get()
                .clear();
    }
}
