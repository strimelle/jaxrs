package lt.viko.eif.imazolevska.jaxrs.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Unit tests for {@link CharAdapter}.
 */
class CharAdapterTest {

    private final CharAdapter adapter = new CharAdapter();

    @Test
    void unmarshalReturnsFirstCharacter() {
        assertEquals('S', adapter.unmarshal("Sci-fi"));
    }

    @Test
    void unmarshalReturnsNullForEmptyText() {
        assertNull(adapter.unmarshal(""));
        assertNull(adapter.unmarshal(null));
    }

    @Test
    void marshalReturnsCharacterAsText() {
        assertEquals("D", adapter.marshal('D'));
    }

    @Test
    void marshalReturnsNullForNull() {
        assertNull(adapter.marshal(null));
    }
}
