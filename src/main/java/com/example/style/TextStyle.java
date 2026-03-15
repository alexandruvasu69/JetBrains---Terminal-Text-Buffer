package com.example.style;

public class TextStyle {
    private boolean isBold;
    private boolean isItalic;
    private boolean isUnderline;

    public TextStyle() {}

    public TextStyle(boolean isBold, boolean isItalic, boolean isUnderline) {
        this.isBold = isBold;
        this.isItalic = isItalic;
        this.isUnderline = isUnderline;
    }

    private TextStyle(TextStyle textStyle) {
        isBold = textStyle.isBold;
        isItalic = textStyle.isItalic;
        isUnderline = textStyle.isUnderline;
    }

    public boolean isBold() {
        return isBold;
    }

    public void setBold(boolean isBold) {
        this.isBold = isBold;
    }

    public boolean isItalic() {
        return isItalic;
    }

    public void setItalic(boolean isItalic) {
        this.isItalic = isItalic;
    }

    public boolean isUnderline() {
        return isUnderline;
    }

    public void setUnderline(boolean isUnderline) {
        this.isUnderline = isUnderline;
    }

    public static TextStyle cloneFrom(TextStyle textStyle) {
        return new TextStyle(textStyle);
    }
    
}
