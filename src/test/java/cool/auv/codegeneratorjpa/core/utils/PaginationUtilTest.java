package cool.auv.codegeneratorjpa.core.utils;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PaginationUtilTest {

    @Test
    void generatePaginationHttpHeadersTest() {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("/api/users");
        List<String> content = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            content.add("User " + i);
        }
        Page<String> page = new PageImpl<>(content, org.springframework.data.domain.PageRequest.of(1, 5), 40);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder, page);

        assertNotNull(headers);
        assertEquals("40", headers.getFirst("X-Total-Count"));

        String linkHeader = headers.getFirst(HttpHeaders.LINK);
        assertNotNull(linkHeader);

        // Example of what the link header should look like. A full assertion would be more complex.
        // <http://localhost/api/users?page=2&size=5>; rel="next",<http://localhost/api/users?page=0&size=5>; rel="prev",<http://localhost/api/users?page=7&size=5>; rel="last",<http://localhost/api/users?page=0&size=5>; rel="first"
        // For simplicity, we just check for the presence of rel types
        assert(linkHeader.contains("rel=\"next\""));
        assert(linkHeader.contains("rel=\"prev\""));
        assert(linkHeader.contains("rel=\"last\""));
        assert(linkHeader.contains("rel=\"first\""));
    }
}
