package Resources;

public enum imageTypeEnum {
    SHAPES("Shapes"), TEXTURES("Textures"), BOTH("Both");

    private String type;

    imageTypeEnum(String s) {
        this.type = s;
    }

    public String getType() {
        return this.type;
    }
}
