package com.lhgpds.algometa.internal.auth.oauth2;


import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Resource Owner의 정보를 담을 객체.
 * 정적 메소드를 활용해 구글, 깃허브 등  Resource owner 정보를 담는다.
 */
@Getter
@ToString
@Builder(access = AccessLevel.PRIVATE)
public class OAuth2Attribute {

    private Map<String, Object> attributes;
    private String attributeKey;
    private String email;

    public static OAuth2Attribute of(String provider, String attributeKey,
        Map<String, Object> attributes) {
        switch (provider) {
            case "google":
                return ofGoogle(attributeKey, attributes);
            default:
                throw new RuntimeException();
        }
    }

    private static OAuth2Attribute ofGoogle(String attributeKey,
        Map<String, Object> attributes) {
        return OAuth2Attribute.builder()
            .email((String) attributes.get("email"))
            .attributes(attributes)
            .attributeKey(attributeKey)
            .build();
    }

    public Map<String, Object> convertToMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", attributeKey);
        map.put("key", attributeKey);
        map.put("email", email);

        return map;
    }
}
