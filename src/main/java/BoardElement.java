import javax.swing.*;
import java.awt.*;

public class BoardElement extends JPanel {
    private final int x;
    private final int y;
    public final boolean isCrossed;
    private final boolean isCross;
    private boolean isSelected;

    BoardElement(int x, int y, boolean isCrossed, boolean isCross, boolean isSelected) {
        this.x = x;
        this.y = y;
        this.isCrossed = isCrossed;
        this.isCross = isCross;
        this.isSelected = isSelected;
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

            g.drawLine(offset,offset,x-offset,offset);
            g.drawLine(x-offset,offset,x-offset,y-offset);
            g.drawLine(x-offset,y-offset,offset,y-offset);
            g.drawLine(offset,y-offset,offset,offset);
        }

        g.setColor(Color.BLACK);

        g.drawLine(0,0,x,0);
        g.drawLine(x,0,x,y);
        g.drawLine(x,y,0,y);
        g.drawLine(0,y,0,0);

        if(isCrossed){
            if (isCross){
                g.setColor(Color.RED);
                g.drawLine(0,0,x,y);
                g.drawLine(x,0,0,y);
            } else {
                g.setColor(Color.BLUE);
                g.drawOval(x/4,y/4,(int)(0.5*(double)x),(int)(0.5*(double)y));
            }
        }
    }
}
