package com.example;

public class CellAttributes {
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

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public TextStyle getTextStyle() {
        return textStyle;
    }

    public void setTextStyle(TextStyle textStyle) {
        this.textStyle = textStyle;
    }
    
}
