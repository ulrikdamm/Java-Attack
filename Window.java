package game;

/*
 * Window klassen, som extender JPanel, er den som tegner spillet
*/
public class Window extends javax.swing.JPanel{
	//initialiserer alle nÃ¸dvendige variabler
	Controller parent; //reference til controlleren
	java.awt.GradientPaint gp1, gp2; //to gradiente farver
	java.awt.geom.Rectangle2D r1, r2, r3, r4; //fire rektangler
	java.awt.BasicStroke bs1; //et basicstroke objekt
	java.awt.geom.Line2D s1, s2; //to linier
	java.awt.Font f1, f2; //to skrifttyper
	java.awt.geom.RoundRectangle2D rr1, rr2; //to afrundede rektangler
	//konstruktÃ¸ren, som tager en reference til controlleren som argument
	public Window(Controller c){
		//sÃ¦tter parent til referencen
		parent = c;
		//sÃ¦tter fortrukne stÃ¸rrelse pÃ¥ panelet, som bliver dens stÃ¸rrelse nÃ¥r vinduet pakkes
		setPreferredSize(new java.awt.Dimension(500, 500));
		//himlens farve
		gp1 = new java.awt.GradientPaint(0, 0, new java.awt.Color(50, 200, 255), 0, 400, java.awt.Color.BLUE);
		//jordens farve
		gp2 = new java.awt.GradientPaint(0, 100, new java.awt.Color(150, 255, 150), 0, 500, new java.awt.Color(50, 200, 50));
		//himlens figur
		r1 = new java.awt.geom.Rectangle2D.Double(0, 0, 500, 400);
		//jordens figur
		r2 = new java.awt.geom.Rectangle2D.Double(0, 400, 500, 100);
		//helbredsmÃ¥ler
		r3 = new java.awt.geom.Rectangle2D.Double(0, 0, 450, 50);
		//cooldownmÃ¥ler
		r4 = new java.awt.geom.Rectangle2D.Double(0, 0, 450, 5);
		//cursors streg
		bs1 = new java.awt.BasicStroke(1);
		//cursors figur
		s1 = new java.awt.geom.Line2D.Double(-16, 0, 16, 0);
		s2 = new java.awt.geom.Line2D.Double(0, -16, 0, 16);
		//skriftyper
		f1 = new java.awt.Font("Arial", 0, 40);
		f2 = new java.awt.Font("Arial", 0, 20);
		//vindue efter spillet
		rr1 = new java.awt.geom.RoundRectangle2D.Double(100, 100, 300, 300, 10, 10);
		rr2 = new java.awt.geom.RoundRectangle2D.Double(150, 300, 200, 50, 10, 10);
	}
	//paint funktionen bliver kaldt nÃ¥r vinduet skal gentegnes. Ved at tegne alting i update funktionen og kalde den fra paint fÃ¥r man en meget nem og simpel form for souble buffering
	public void paint(java.awt.Graphics g){
		update(g);
	}
	//update funktionen, tegner alting
	public void update(java.awt.Graphics g2){
		//opretter et graphics2d objekt ud fra det normale graphics objekt
		java.awt.Graphics2D g = (java.awt.Graphics2D) g2;
		//sÃ¦tter graphics objektet til at tegne alting med anti-aliasing
		g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
		//hvis rysteeffekt er i gang
		if(parent.shaking > 0){
			//sÃ¦t tÃ¦lleren en tak ned
			parent.shaking--;
			//flyt hele billedet som tegnes lidt i en tilfÃ¦lfig retning
			g.translate(Math.random()*5, Math.random()*5);
		}
		//sÃ¦tter g til at tegne med gradient1 objektet
		g.setPaint(gp1);
		//tegner himlen udfyldt
		g.fill(r1);
		//sÃ¦tter g til at tegne med gradient2 objektet
		g.setPaint(gp2);
		//tegner jorden udfyldt
		g.fill(r2);
		//kÃ¸rer igennem alle fjenderne
		for(int i = 0; i < parent.e.size(); i++){
			//tegner fjenden
			parent.e.get(i).paint(g);
		}
		//tegner spilleren
		parent.p.paint(g);
		//kÃ¸rer igennem alle skuddene
		for(int i = 0; i < parent.s.size(); i++){
			//tegner skuddet
			parent.s.get(i).paint(g);
		}
		//hvis spillet ikke er slut
		if(!parent.p.dead){
			//tegner alting relativt til punkt (25, 25)
			g.translate(25, 25);
				//sÃ¦tter r3's lÃ¦ngde til det antal liv man har tilbage
				r3.setRect(0, 0, parent.health, 25);
				//hvis man har mere end 225 liv tilbage
				if(parent.health > 225){
					//tegn grÃ¸nt
					g.setPaint(java.awt.Color.GREEN);
				//ellers hvis man har mere end 100 liv tilbage
				}else if(parent.health > 100){
					//tegn gult
					g.setPaint(java.awt.Color.YELLOW);
				//ellers
				}else{
					//tegn rÃ¸dt
					g.setPaint(java.awt.Color.RED);
				}
				//tegn r3 udfyldt
				g.fill(r3);
				//tegner med sort
				g.setPaint(java.awt.Color.BLACK);
				//tegn kanten til r3
				g.draw(r3);
				//hvis der er nedkÃ¸ling pÃ¥ supervÃ¥bnet
				if(parent.cooldown > 0){
					//tegn 25 pixels lÃ¦ngere nede
					g.translate(0, 25);
						//tegn med rÃ¸dt
						g.setPaint(java.awt.Color.RED);
						//sÃ¦tter r4's lÃ¦ngde til at passe med nedkÃ¸ling tilbage
						r4.setRect(0, 0, parent.cooldown, 5);
						//tegn r4 udfyldt
						g.fill(r4);
						//tegn med sort
						g.setPaint(java.awt.Color.BLACK);
						//tegn kanten til r4
						g.draw(r4);
					//sÃ¦t tegneudgangspunktet tilbage
					g.translate(0, -25);
				}
				//tegn relativt til midten af liv-mÃ¥leren
				g.translate(225, 33);
					//sÃ¦t skrifttypen til f2
					g.setFont(f2);
					//opret et fontmetrics objekt til f2
					java.awt.FontMetrics fm = g.getFontMetrics();
					//tegn en streng med score og level midt pÃ¥ liv-mÃ¥leren
					g.drawString("Score: "+parent.score+"   Level: "+parent.level, -fm.stringWidth("Score: "+parent.score+" : Level: "+parent.level)/2, -fm.getHeight()/2);
				//sÃ¦t tegneudgangspunkt tilbage
				g.translate(-225, -37);
			//sÃ¦t tegneudgangspunkt tilbage
			g.translate(-25, -25);
		}
		//hvis levelup nedtÃ¦lleren er i gang
		if(parent.levelup > 0){
			//fÃ¥ en tilfÃ¦ldig vÃ¦rdi for rotation af teksten
			double rot = (Math.random()-0.5)/3;
			//hvis der er mindre end 50 frames tilbage af animationen
			if(parent.levelup < 50){
				//sÃ¦t positionen sÃ¥ at teksten i animationen kÃ¸rer op
				g.translate(250, 250-(-parent.levelup+50)*5);
			}else{
				//sÃ¦t tegneudgangspunktet midt pÃ¥ skÃ¦rmen
				g.translate(250, 250);
			}
			//sÃ¦t skrifttypen til f1
			g.setFont(f1);
			//opret et fontmetrics objekt til f1
			java.awt.FontMetrics fm = g.getFontMetrics();
			//rotÃ©r rot grader
			g.rotate(rot);
				//skriv "LEVEL UP" midt pÃ¥ skÃ¦rmen
				g.drawString("LEVEL UP", -fm.stringWidth("LEVEL UP")/2, -fm.getHeight());
			//rotÃ©r tilbage
			g.rotate(-rot);
			//sÃ¦tter tegneudgangspunktet tilbage
			if(parent.levelup < 50){
				g.translate(-250, -(250-(-parent.levelup+50)*5));
			}else{
				g.translate(-250, -250);
			}
			//gÃ¥r en frame videre i animationen
			parent.levelup--;
		}
		//hvis spillet er helt slut og slutskÃ¦rmen skal vises (nÃ¥r spillerens position er mere end 2000)
		if(parent.p.y > 2000){
			//sÃ¦t bevÃ¦gelseshastigheden i y til 0
			parent.p.vy = 0;
			//sÃ¦t positionen til mere end 2000
			parent.p.y = 2001;
			//tegn hvidt
			g.setPaint(java.awt.Color.WHITE);
			//tegn rr1 fyldt
			g.fill(rr1);
			//tegn med sort
			g.setPaint(java.awt.Color.BLACK);
			//tegn kanten til rr1
			g.draw(rr1);
			//sÃ¦tter skrifttypen til f1
			g.setFont(f1);
			//opretter et fontmetrics objekt
			java.awt.FontMetrics fm = g.getFontMetrics();
			//tegner strengen "GAME OVER!"
			g.drawString("GAME OVER!", 250-fm.stringWidth("GAME OVER!")/2, 175-fm.getHeight()/2);
			//tenger ens score
			g.drawString(""+parent.score, 250-fm.stringWidth(""+parent.score)/2, 275-fm.getHeight()/2);
			//sÃ¦tter skrifttypen til f2
			g.setFont(f2);
			//Ã¦ndrer fm til at tÃ¦lle for den nye skrifttype
			fm = g.getFontMetrics();
			//tegner strengen "Final Score:"
			g.drawString("Final Score:", 250-fm.stringWidth("final Score:")/2, 220-fm.getHeight()/2);
			//tegner strengen "Quit" som stÃ¥r pÃ¥ knappen
			g.drawString("Quit", 250-fm.stringWidth("Quit")/2, 345-fm.getHeight()/2);
			//tegner kanten til rr2
			g.draw(rr2);
		}
		//hvis spillet er pÃ¥ pause
		if(parent.pause){
			//sÃ¦t farven til hvid
			g.setPaint(java.awt.Color.WHITE);
			//tegner rr2 udfyldt
			g.fill(rr2);
			//sÃ¦tter farven til sort
			g.setPaint(java.awt.Color.BLACK);
			//tegner kanten pÃ¥ rr2
			g.draw(rr2);
			//sÃ¦tter skrifttypen til f2
			g.setFont(f2);
			//laver et fontmetricsobjekt
			java.awt.FontMetrics fm = g.getFontMetrics();
			//skriver "pause" pÃ¥ skÃ¦rmen
			g.drawString("Pause", 250-fm.stringWidth("Pause")/2, 345-fm.getHeight()/2);
		}
		//sÃ¦tter tegneudgangspunktet til musens position
		g.translate(parent.mouse_pos.x, parent.mouse_pos.y);
		//sÃ¦tter stregtype til bs1
		g.setStroke(bs1);
		//sÃ¦tter farven til sort
		g.setPaint(java.awt.Color.BLACK);
		//tegner de to linier til cursoren
		g.draw(s1);
		g.draw(s2);
	}
}
