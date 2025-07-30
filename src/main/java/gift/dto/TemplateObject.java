package gift.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.LinkedMultiValueMap;

public class TemplateObject {

    @JsonProperty("object_type")
    private String objectType;
    private String text;
    private LinkObject link;
    @JsonProperty("button_title")
    private String buttonTitle;

    public TemplateObject(String objectType, String text, String url, String buttonTitle) {
        this.objectType = objectType;
        this.text = text;
        this.link = new LinkObject(url,url);
        this.buttonTitle = buttonTitle;
    }

    public LinkedMultiValueMap<String, String> makeBody() {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            body.add("template_object", objectMapper.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return body;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LinkObject getLink() {
        return link;
    }

    public void setLink(LinkObject link) {
        this.link = link;
    }

    public String getButtonTitle() {
        return buttonTitle;
    }

    public void setButtonTitle(String buttonTitle) {
        this.buttonTitle = buttonTitle;
    }
}
