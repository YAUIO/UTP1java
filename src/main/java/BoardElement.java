import javax.swing.*;
import java.awt.*;

public class BoardElement extends JPanel {
    private final int xSize;
    private final int ySize;
    private final int xTile;
    private final int yTile;
    private boolean isSelected;
    private final Game game;

    BoardElement(int xSize, int ySize, int xTile, int yTile, boolean isSelected, Game game) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.xTile = xTile;
        this.yTile = yTile;
        this.isSelected = isSelected;
        this.game = game;
    }

    public void selected(){
        if (!isSelected){
            isSelected = true;
            this.repaint();
        }
    }

    public void eSelected(){
        if (isSelected){
            isSelected = false;
            this.repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (isSelected) {
            g.setColor(Color.GREEN);

            int offset = 4;

            g.drawLine(offset,offset,xSize-offset,offset);
            g.drawLine(xSize-offset,offset,xSize-offset,ySize-offset);
            g.drawLine(xSize-offset,ySize-offset,offset,ySize-offset);
            g.drawLine(offset,ySize-offset,offset,offset);
        }

        g.setColor(Color.BLACK);

        g.drawLine(0,0,xSize,0);
        g.drawLine(xSize,0,xSize,ySize);
        g.drawLine(xSize,ySize,0,ySize);
        g.drawLine(0,ySize,0,0);

        int tileValue = game.getValue(yTile,xTile);

        if(tileValue>0 && tileValue<3){
            if (tileValue==1){
                g.setColor(Color.RED);
                g.drawLine(0,0,xSize,ySize);
                g.drawLine(xSize,0,0,ySize);
            } else {
                g.setColor(Color.BLUE);
                g.drawOval(xSize/4,ySize/4,(int)(0.5*(double)xSize),(int)(0.5*(double)ySize));
            }
        }
    }
}
