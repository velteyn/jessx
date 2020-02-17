// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.business.institutions;

import java.awt.Color;

class ColoredCell
{
    private int side;
    private int col;
    private int row;
    private int iRed;
    private int iGreen;
    private int iBlue;
    private int fRed;
    private int fGreen;
    private int fBlue;
    private int stateNum;
    private int step;
    private Color cColor;
    
    public boolean isBidSide() {
        return this.side == 1;
    }
    
    public int getCol() {
        return this.col;
    }
    
    public int getRow() {
        return this.row;
    }
    
    public int getStep() {
        return this.step;
    }
    
    public Color getCurrentColor() {
        return this.cColor;
    }
    
    public ColoredCell(final int side, final int col, final int row, final Color iColor, final Color fColor, final int stateNum) {
        this.step = 0;
        this.side = side;
        this.col = col;
        this.row = row;
        this.stateNum = stateNum;
        this.iRed = iColor.getRed();
        this.iGreen = iColor.getGreen();
        this.iBlue = iColor.getBlue();
        this.fRed = fColor.getRed();
        this.fGreen = fColor.getGreen();
        this.fBlue = fColor.getBlue();
        this.cColor = iColor;
    }
    
    public void nextColor() {
        int cBlue = this.cColor.getBlue();
        int cRed = this.cColor.getRed();
        int cGreen = this.cColor.getGreen();
        cBlue = this.iBlue + (this.fBlue - this.iBlue) * this.step / this.stateNum;
        cRed = this.iRed + (this.fRed - this.iRed) * this.step / this.stateNum;
        cGreen = this.iGreen + (this.fGreen - this.iGreen) * this.step / this.stateNum;
        this.cColor = new Color(cRed, cGreen, cBlue);
        ++this.step;
    }
}
