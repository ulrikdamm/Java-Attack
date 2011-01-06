package game;

/*
 * Player klassen, spilleren
*/
public class Player{
	//deklarerer nÃ¸dvendige variabler
	public int x, y, w, h, cox, coy; //position, hÃ¸jde/bredde, kanonens position
	public double vx, vy; //hastighed
	java.awt.GradientPaint gp1; //gradient
	java.awt.geom.Ellipse2D e1, e2, e3; //ellipser
	java.awt.geom.Rectangle2D r1; //rektangel
	java.awt.BasicStroke bs1; //basicstroke
	java.awt.geom.Line2D l1, l2, l3, l4, l5, l6; //linier
	Controller parent; //reference til controlleren
	int damage; //nedtÃ¦ller nÃ¥r man tager skade
	boolean dead; //om man har tabt
	//konstruktÃ¸ren med controller referencen
	public Player(Controller parent){
		//sÃ¦tter standartvÃ¦rdier til alle variablerne
		x = 200;
		y = 100;
		w = 75;
		h = 75;
		cox = 30;
		coy = 45;
		damage = 0;
		//sÃ¦tter referencevariablen
		this.parent = parent;
		//opretter alle tegneobjekterne
		gp1 = new java.awt.GradientPaint(x, y, new java.awt.Color(255, 200, 0), x, y+h, new java.awt.Color(255, 75, 0), true);
		e1 = new java.awt.geom.Ellipse2D.Double(0, 0, w, h);
		e2 = new java.awt.geom.Ellipse2D.Double(0, 0, 20, 30);
		e3 = new java.awt.geom.Ellipse2D.Double(0, 0, 8, 8);
		r1 = new java.awt.geom.Rectangle2D.Double(-25, -10, 75, 25);
		bs1 = new java.awt.BasicStroke(2);
		l1 = new java.awt.geom.Line2D.Double(0, 0, 40, 20);
		l2 = new java.awt.geom.Line2D.Double(0, 20, 40, 0);
		l3 = new java.awt.geom.Line2D.Double(0, 20, 20, 0);
		l4 = new java.awt.geom.Line2D.Double(0, 0, 20, 20);
		l5 = new java.awt.geom.Line2D.Double(20, 20, 40, 0);
		l6 = new java.awt.geom.Line2D.Double(20, 0, 40, 20);
	}
	//funktion som returnerer koordinater til det punkt kanonen skal pege pÃ¥
	public int target(int i){
		//hvis i er lig 0
		if(i == 0){
			//returner musens x, da kanonen skal peje mod musen
			return parent.mouse_pos.x;
		//ellers
		}else{
			//returner musens y
			return parent.mouse_pos.y;
		}
	}
	//paint funktionen, tegner spilleren
	public void paint(java.awt.Graphics2D g){
		//opretter lokale variabler til positionsvariablerne, da disse kan blive Ã¦ndret imens funktionen kÃ¸rer
		int tx = x, ty = y, tcox = cox, tcoy = coy;
		//sÃ¦t tegneudgangspunkt til spillerens position
		g.translate(tx, ty);
			//tegner med gradient gp1
			g.setPaint(gp1);
			//tegner e1 udfyldt
			g.fill(e1);
			//sÃ¦tter steg til bs1
			g.setStroke(bs1);
			//sÃ¦tter farve til sort
			g.setPaint(java.awt.Color.BLACK);
			//tegner kanten til e1;
			g.draw(e1);
			//flytter tegneudgangspunket lidt ind
			g.translate(17, 10);
				//hvis spilleren er dÃ¸d
				if(dead){
					//tegn strejerne l3, l4, l5 og l6
					g.draw(l3);
					g.draw(l4);
					g.draw(l5);
					g.draw(l6);
				//ellers hvis skadenedtÃ¦lleren er i gang
				}else if(damage > 0){
					//sÃ¦t den til Ã©n mindre
					damage--;
					//tegn stregerne l1 og l2
					g.draw(l1);
					g.draw(l2);
				//ellers
				}else{
					//tegn med hvidt
					g.setPaint(java.awt.Color.WHITE);
					//tegner e2 udfyldt
					g.fill(e2);
					//sÃ¦tter farven til sort
					g.setPaint(java.awt.Color.BLACK);
					//tegn kanten til e2
					g.draw(e2);
					//flyt tegneudgangspunktet lidt ind
					g.translate(6, 10);
						//tegn e3 udfyldt
						g.fill(e3);
					//flyt tegneudgangspunktet tilbage
					g.translate(-6, -10);
					//flyt tegneudgangspunket ind (til nÃ¦ste Ã¸je)
					g.translate(22, 0);
						//sÃ¦t farven til hvid
						g.setPaint(java.awt.Color.WHITE);
						//tegn e2 udfyldt
						g.fill(e2);
						//sÃ¦t farven til sort
						g.setPaint(java.awt.Color.BLACK);
						//tegn kanten til e2
						g.draw(e2);
						//flyt tegneudgangspunktet lidt ind
						g.translate(6, 10);
							//tegn e3 udfyldt
							g.fill(e3);
						//flyt tegneudgangspunktet tilbage
						g.translate(-6, -10);
					//flyt tegneudgangspunktet tilbage
					g.translate(-22, 0);
				}
			//flyt tegneudgangspunktet tilbage
			g.translate(-17, -10);
			//flyt tegneudgangspunktet til kannonens position
			g.translate(tcox, tcoy);
				//beregn x og y forskellen mellem positionen kannonen pejer mod og kannonens position
				double xdiff = target(0)-(tx+tcox);
				double ydiff = target(1)-(ty+tcoy);
				//hvis y forskellen ikke er lig 0 (hvilket ville dividere med 0)
				if(ydiff != 0){
					//rotÃ©r sÃ¥ kanonnen pejer mod mÃ¥let
					g.rotate(Math.atan(ydiff/xdiff));
					//hvis den pejer mod venstre
					if(xdiff < 0){
						//roter 180 grader mere (180 grader = PI radianer)
						g.rotate(Math.PI);
					}
				}
				//sÃ¦t farven til grÃ¥
				g.setPaint(java.awt.Color.GRAY);
				//tegn r1 udfyldt
				g.fill(r1);
				//sÃ¦t farven til sort
				g.setPaint(java.awt.Color.BLACK);
				//tegn kanten til r1
				g.draw(r1);
				//gentag rotationen, bare omvendt, for at rotere alt andet tilbage
				if(ydiff != 0){
					g.rotate(-Math.atan(ydiff/xdiff));
					if(xdiff < 0){
						g.rotate(-Math.PI);
					}
				}
			//flyt tegneudgangspunktet tilbage
			g.translate(-tcox, -tcoy);
		//flyt tegneudgangspunktet tilbage
		g.translate(-tx, -ty);
	}
	//run funktionen, bliver kaldet hvert frame
	public void run(){
		//hvis men har tabt
		if(dead){
			//fald nedad
			vy += 0.2;
		//ellers
		}else{
			//hvis man er i luften
			if(y+h < 400){
				//sÃ¦t tyngekraft til
				vy += 0.2;
			//ellers
			}else if(y+h > 400){
				//sÃ¦t tyngdekraften til 0
				vy = 0;
				//fastsÃ¦t positionen
				y = 400-h;
			}
		}
		//bevÃ¦g spilleren
		x += Math.ceil(vx);
		y += Math.ceil(vy);
		//hvis man er gÃ¥et ud for vinduet
		if(x > 500-h){
			//sÃ¦t en tilbage
			x = 500-h;
		//og det samme for den anden side af vinduet
		}else if(x < 0){
			x = 0;
		}
	}
}
