package lt.viko.eif.imazolevska.jaxrs.model;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Converts a {@code char} value to text in XML and back.
 */
public class CharAdapter extends XmlAdapter<String, Character> {

    /**
     * Converts XML text to a character.
     *
     * @param value text from XML
     * @return first character of the text, or {@code null} if the text is empty
     */

    @Override
    public Character unmarshal(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return value.charAt(0);
    }

    /**
     * Converts a character to XML text.
     *
     * @param value character to convert
     * @return character as text, or {@code null} if the value is {@code null}
     */

    @Override
    public String marshal(Character value) {
        if (value == null) {
            return null;
        }
        return value.toString();
    }
}