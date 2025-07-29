package gift.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KakaoUserInfoResponseDto {

    private Long id;

    @JsonProperty("connected_at")
    private String connectedAt;

    public Long getId() {
        return id;
    }

    public String getConnectedAt() {
        return connectedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setConnectedAt(String connectedAt) {
        this.connectedAt = connectedAt;
    }
}
