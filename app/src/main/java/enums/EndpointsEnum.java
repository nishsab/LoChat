package enums;

public enum EndpointsEnum {
    URL("https://lochat.codyleyhan.com"),
    REGISTER(String.format("%s/api/v1/auth/register",EndpointsEnum.URL.getValue())),
    LOGIN(String.format("%s/api/v1/auth/login",EndpointsEnum.URL.getValue())),
    CREATE_ROOM(String.format("%s/api/v1/rooms",EndpointsEnum.URL.getValue())),
    SEARCH_ROOMS(String.format("%s/api/v1/rooms/search",EndpointsEnum.URL.getValue())),
    GET_MESSAGES(String.format("%s/api/v1/rooms/:room/messages",EndpointsEnum.URL.getValue())),
    SEND_MESSAGE(String.format("%s/api/v1/rooms/:room/messages",EndpointsEnum.URL.getValue()));

    private final String value;

    EndpointsEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}