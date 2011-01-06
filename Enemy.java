package game;

/*
 * Enemy klassen, som er fjenderne i spillet. Extender player, da de deler en masse ting, f.eks. udseende
*/
public class Enemy extends Player{
	//deklarer nÃ¸dvendige variabler (de fleste er allerede i palyer klassen og behÃ¸ver ikke ogsÃ¥ at initialiseres her)
	int health; //liv tilbage
	//konstruktÃ¸ren med controllerreference som argument
	public Enemy(Controller parent){
		//kalder player klassens konstruktÃ¸r
		super(parent);
		//sÃ¦tter gp1 til en anden farve, for at man kan se forskel fra dem og en selv
		gp1 = new java.awt.GradientPaint(x, y, new java.awt.Color(255, 50, 0), x, y+h, new java.awt.Color(150, 50, 0), true);
		//sÃ¦tter standartvÃ¦rdier
		x = 0;
		y = 0;
		health = 1;
	}
	//overrider target funktionen, da kannonen skal pege pÃ¥ spilleren i stedet for musen
	public int target(int i){
		if(i == 0){
			//returnerer spillerens x-position
			return parent.p.x+parent.p.cox;
		}else{
			//returnerer spillerens y-position
			return parent.p.y+parent.p.coy;
		}
	}
	//kÃ¸rer run funktionen som bliver kÃ¸rt i hvert frame
	public void run(){
		//hvis dÃ¸d
		if(dead){
			//sÃ¦t tyngdekraft og bevÃ¦g efter tyndgekraften
			vy += 0.2;
			y += vy;
		//ellers
		}else{
			//hvis spillet er slut
			if(parent.p.dead){
				//stop funktionen
				return;
			}
			//beregner x og y forskellen fra spilleren og fjenden
			int xdiff = (parent.p.x+parent.p.cox)-(x+cox);
			int ydiff = (parent.p.y+parent.p.coy)-(y+coy);
			//hvis fjenden er til hÃ¸jre for spilleren
			if(xdiff > 0){
				//bevÃ¦g til venstre
				x += 2;
			//ellers hvis fjenden er til venstre for spilleren
			}else if(xdiff < 0){
				//flyt til hÃ¸jre
				x -= 2;
			}
			//hvis fjenden er over spilleren
			if(ydiff > 0){
				//flyt nedad
				y += 2;
			//ellers hvis fjenden er under spilleren
			}else if(ydiff < 0){
				//flyt opad
				y -= 2;
			}
			//hvis bÃ¥de x og y forskellen er under 50
			if(Math.abs(xdiff) < 50 && Math.abs(ydiff) < 50){
				//mister Ã©t liv
				parent.health--;
				//sÃ¦tter 10 frames rysteeffekt
				parent.shaking = 10;
				//sÃ¦tter 10 frames skade effekt
				parent.p.damage = 10;
				//hvis spilleren ikke har mere liv
				if(parent.health <= 0){
					//spillet er slut
					parent.p.dead = true;
					//fÃ¥ spilleren til at lave en hoppeeffekt
					parent.p.vy = -10;
					//sÃ¦t horisontalbevÃ¦gelse til 0
					parent.p.vx = 0;
				}
			}
			//med en chance baseret pÃ¥ level, og hvis spillet ikker slut
			if(Math.random() < ((0.002*parent.level*parent.level)/10)+0.005 && !parent.p.dead){
				//beregn x og y forskellen pÃ¥ fjenden og spilleren
				xdiff = (parent.p.x+parent.p.cox)-(x+cox);
				ydiff = (parent.p.y+parent.p.coy)-(y+coy);
				//beregn afstenden mellem spilleren og fjenden
				double length = Math.sqrt(xdiff*xdiff+ydiff*ydiff);
				//opret et fjendtligt skud pÃ¥ fjendens position med retning mod spilleren.
				parent.s.add(new Shot(x+cox, y+coy, (int)(xdiff*(5/length)), (int)(ydiff*(5/length)), false));
			}
		}
	}
}
