/*
 TUIO Java GUI Demo
 Copyright (c) 2005-2014 Martin Kaltenbrunner <martin@tuio.org>
 
 Permission is hereby granted, free of charge, to any person obtaining
 a copy of this software and associated documentation files
 (the "Software"), to deal in the Software without restriction,
 including without limitation the rights to use, copy, modify, merge,
 publish, distribute, sublicense, and/or sell copies of the Software,
 and to permit persons to whom the Software is furnished to do so,
 subject to the following conditions:
 
 The above copyright notice and this permission notice shall be
 included in all copies or substantial portions of the Software.
 
 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

import javax.swing.*;
import java.awt.geom.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import TUIO.*;
import java.awt.image.ImageObserver;
import java.io.File;
import javax.imageio.ImageIO;

public class TuioDemoObject extends TuioObject {

	private Shape square;
        public int type;
        public int answerID;
        public int state;

	public TuioDemoObject(TuioObject tobj) {
             
		super(tobj);
		int size = TuioDemoComponent.object_size;
		square = new Rectangle2D.Float(-size/2,-size/2,size,size);
		
		AffineTransform transform = new AffineTransform();
		transform.translate(xpos,ypos);
		transform.rotate(angle,xpos,ypos);
		square = transform.createTransformedShape(square);
                
                
                //markers with id 5 and 6 are questions, all other markers are answers
                if(this.getSymbolID() == 5 || this.getSymbolID()== 6)
                {
                    type = 0;
                    if(this.getSymbolID() == 5) answerID = 7;
                    if(this.getSymbolID() == 6) answerID = 8;
                }
                else{
                    type = 1;
                    answerID=0;
                }
                state = this.state;
	}
	
	public void paint(Graphics2D g, int width, int height) {
	
		float Xpos = xpos*width;
		float Ypos = ypos*height;
		float scale = height/(float)TuioDemoComponent.table_size;

		AffineTransform trans = new AffineTransform();
		trans.translate(-xpos,-ypos);
		trans.translate(Xpos,Ypos);
		trans.scale(scale,scale);
		Shape s = trans.createTransformedShape(square);
                
                
		if(state == 1)g.setPaint(Color.green);
                if(state == 2)g.setPaint(Color.red);
                if(state ==0)g.setPaint(Color.blue);
                
		g.fill(s);
		g.setPaint(Color.red);
		g.drawString("ID= "+symbol_id+"",Xpos-10,Ypos);
                
                //String path = "img.png";
                String path = getCardImage(symbol_id);
                File imgFile = new File(path);
                
                int newX = (int) Xpos;
                int newY = (int) Ypos;
                try{
                        Image img = ImageIO.read(imgFile);
                        
                        //g.drawImage(img, newX, newY, null);
                        g.drawImage(img, newX - (img.getWidth(null)/4), newY, img.getWidth(null)/2, img.getHeight(null)/2, null);
                    }catch(Exception ex){
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }


	}
        
        public String getCardImage(int id)
        {
            if(id == 5) return "q1.png";
            if(id == 6) return "q2.png";
            
            if(id == 7) return "a1.png";
            if(id == 8) return "a2.png";
            if(id == 11) return "a3.png";
            
            return "a1.png";
        }
        
        public void drawImage(int x, int y, String path)
        {
            JPanel pnl = new JPanel(){
                @Override
                public void paintComponent(Graphics g){
                    Graphics2D g2 = (Graphics2D)g;
                    File imgFile = new File(path);
                    try{
                        Image img = ImageIO.read(imgFile);
                        g2.drawImage(img, x, y, this);

                    }catch(Exception ex){
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }
                }
            };
            
        }

	public void update(TuioObject tobj) {
		
		float dx = tobj.getX() - xpos;
		float dy = tobj.getY() - ypos;
		float da = tobj.getAngle() - angle;

		if ((dx!=0) || (dy!=0)) {
			AffineTransform trans = AffineTransform.getTranslateInstance(dx,dy);
			square = trans.createTransformedShape(square);
		}
		
		if (da!=0) {
			AffineTransform trans = AffineTransform.getRotateInstance(da,tobj.getX(),tobj.getY());
			square = trans.createTransformedShape(square);
		}

		super.update(tobj);
	}

}
