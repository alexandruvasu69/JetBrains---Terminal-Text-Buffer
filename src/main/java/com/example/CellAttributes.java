package com.example;

public class CellAttributes implements ICellAttributes {
    private Color foregroundColor = Color.getDefaultForeground(); 
    private Color backgroundColor = Color.getDefaultBackground(); 
    private TextStyle textStyle = new TextStyle();

    CellAttributes() {}

    CellAttributes(Color foregroundColor, Color backgroundColor, TextStyle textStyle) {
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
        this.textStyle = textStyle;
    }

    private CellAttributes(CellAttributes attributes) {
        this.foregroundColor = attributes.foregroundColor;
        this.backgroundColor = attributes.backgroundColor;
        this.textStyle = TextStyle.cloneFrom(attributes.textStyle);
    }

    static CellAttributes getDefaultAttributes() {
        return new CellAttributes();
    }

    static CellAttributes cloneFrom(CellAttributes attributes) {
        return new CellAttributes(attributes);
    }

    @Override
    public Color getForegroundColor() {
        return foregroundColor;
    }

    void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    @Override
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    @Override
    public boolean isBold() {
        return this.textStyle.isBold();
    }

    @Override
    public boolean isItalic() {
        return this.textStyle.isItalic();
    }

    @Override
    public boolean isUnderline() {
        return this.textStyle.isUnderline();
    }

    void setBold(boolean isBold) {
        this.textStyle.setBold(isBold);
    }

    void setItalic(boolean isItalic) {
        this.textStyle.setItalic(isItalic);
    }

    void setUnderline(boolean isUnderline) {
        this.textStyle.setUnderline(isUnderline);
    }
}
