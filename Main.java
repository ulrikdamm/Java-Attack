package game;

/*
 * Main klassen, holder main metoden som bliver kaldt nÃ¥r programmet starter
 */
public class Main{
    public static void main(String[] args){
		//opretter en ny JFrame, til vinduet, og giver det titlen "Java spil"
		javax.swing.JFrame f = new javax.swing.JFrame("Java spil");
		//sÃ¦tter vinduet til at kunne ses
		f.setVisible(true);
		//gÃ¸r at programmet afslutter nÃ¥r vinduet lukkes
		f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
		//sÃ¦tter at vinduet ikke kan Ã¦ndre stÃ¸rrelse
		f.setResizable(false);
		/* transperant cursor script fra http://www.izyt.com/freode/viewcode.php?code=43 */
		int[] pixels = new int[16 * 16];
		java.awt.Image image = java.awt.Toolkit.getDefaultToolkit().createImage(new java.awt.image.MemoryImageSource(16, 16, pixels, 0, 16));
		java.awt.Cursor transparentCursor = java.awt.Toolkit.getDefaultToolkit().createCustomCursor(image, new java.awt.Point(0, 0), "invisiblecursor");
		f.setCursor(transparentCursor);
		/* end of script */
		//sÃ¦tter layout til borderlayout
		f.getContentPane().setLayout(new java.awt.BorderLayout());
		//sÃ¦tter vinduet til hele tiden at vÃ¦re over andre vinduer
		f.setAlwaysOnTop(true);
		//opretter en ny controller instans med vores frame som argument
		new Controller(f);
    }
}