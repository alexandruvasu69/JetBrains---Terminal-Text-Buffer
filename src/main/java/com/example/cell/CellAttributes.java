package com.example.cell;

import com.example.style.Color;
import com.example.style.TextStyle;

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

    public static CellAttributes getDefaultAttributes() {
        return new CellAttributes();
    }

    public static CellAttributes cloneFrom(CellAttributes attributes) {
        return new CellAttributes(attributes);
    }

    @Override
    public Color getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    @Override
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
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

    public void setBold(boolean isBold) {
        this.textStyle.setBold(isBold);
    }

    public void setItalic(boolean isItalic) {
        this.textStyle.setItalic(isItalic);
    }

    public void setUnderline(boolean isUnderline) {
        this.textStyle.setUnderline(isUnderline);
    }
}
