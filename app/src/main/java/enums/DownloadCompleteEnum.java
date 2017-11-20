package enums;

public enum DownloadCompleteEnum {
    GET_TOKEN(0),
    GET_ROOMS(1);

    private final int value;

    DownloadCompleteEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return this.name();
    }
}
