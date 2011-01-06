package game;

/*
 * Controller klassen, som har alle objekter i brugerfladen og styrer mus- og keyboard hÃ¦ndelser og main-trÃ¥den som kÃ¸rer hele programmet.
*/
public class Controller implements java.awt.event.KeyListener, java.awt.event.MouseListener, java.awt.event.MouseMotionListener{
	//opretter alle variabler der skal bruges
	Window w; //window klassen som bruges til at tegne med
	Player p; //Playeren, som man styrer rundt
	java.util.Vector<Enemy> e; //en vektor til fjenderne. en vektor er et dynamisk array
	java.util.Vector<Shot> s; //en vektor til skudene
	javax.swing.JFrame f; //reference til vinduet
	java.awt.Point mouse_pos; //point er et objekt med en x og y variabel
	int health; //holder hvor meget liv man har tilbage
	int shaking; //en nedtÃ¦ller til nÃ¥r skÃ¦rmen skal ryste
	int level; //variabel til ens level
	int xp; //variabel til xp, som man bruger til at stige i level
	int levelup; //nedtÃ¦ller til "level up"-animationen
	int score; //variabel til ens score
	int cooldown; //variabel til nedtÃ¦lling, indtil man kan bruge super-angreb
	int waittime; //variabel til at tÃ¦lle hvor lang tid siden der er der sidst kom en fjende
	boolean pause; //boolean om spillet er pauset
	//konstruktÃ¸reren til klassen, tager en JFrame som argument, som er selve vinduet programmet skal tegnes i.
	Controller(javax.swing.JFrame f){
		mouse_pos = new java.awt.Point(0, 0); //opretter et point til musens positioner og sÃ¦tter det til position (0, 0)
		w = new Window(this); //opretter en window instans, og giver denne instans som argument for at kunne referere til det
		p = new Player(this); //opretter palyer instansen og giver denne instans som argument
		e = new java.util.Vector<Enemy>(); //Opretter en ny vektor til fjenderne
		s = new java.util.Vector<Shot>(); //opretter en ny vektor til skudene
		//sÃ¦tter standartvÃ¦rdier pÃ¥ alle variablerne
		health = 450;
		shaking = 0;
		level = 1;
		xp = 0;
		levelup = 0;
		score = 0;
		cooldown = 0;
		waittime = 0;
		this.f = f; //sÃ¦tter denne klasse's f variabel til den som blev givet som argument. F er sÃ¥ en reference til selve vinduet.
		this.f.getContentPane().add(w, java.awt.BorderLayout.CENTER); //tilfÃ¸jer window instansen i midten af JFrame vinduet
		this.f.pack(); //pakker vinduet, gÃ¸r at det fÃ¥r en optimal stÃ¸rrelse til indholdet
		this.f.addMouseListener(this); //sÃ¦tter en musselytter i gang til vinduet
		this.f.addMouseMotionListener(this); //sÃ¦tter en mussebevÃ¦gelseslytter i gang til vinduet
		this.f.addKeyListener(this); //sÃ¦tter en tasteturlytter i gang til vinduet
		run(); //starter spillet's lÃ¸kke
	}
	// spillet's hovedlÃ¸kke, som kÃ¸rer hele tiden, og sÃ¸rger for at alting bevÃ¦ger sig rigtigt. Er void da den ikke returnerer noget.
	void run(){
		while(true){ //kÃ¸rer altid
			//hvis spillet er pauset
			if(pause){
				//tegn skÃ¦rmen
				w.repaint();
				//vent i 100 millisekunder
				try{
					Thread.sleep(100);
				}catch(Exception e){}
				//starter lÃ¸kken igen
				continue;
			}
			//hvis cooldown-variablen er stÃ¸rre end 0, sÃ¥ sÃ¦t den en tak ned
			if(cooldown > 0){
				cooldown--;
			}
			//dette er koden som opretter nye fjender pÃ¥ tilfÃ¦ldige tidspunkter
			//chancen bliver hÃ¸jere med waittime
			if(Math.random() < ((0.002*level*level)/10)+0.0001*waittime && !p.dead){ //hvis der ved en chance skulle oprettes en ny fjende, og at spillet ikke er slut
				e.add(new Enemy(this)); //opret ny fjende og lÃ¦g den i vektoreren
				//nulstiller waittime
				waittime = 0;
				if(Math.random() < 0.66){ //2/3 chance
					//sÃ¦t fjendens position til en tilfÃ¦ldig ude i siderne af skÃ¦rmen
					e.get(e.size()-1).x = (Math.random() < 0.5? -50: 550);
					e.get(e.size()-1).y = (int)(Math.random()*400);
				}else{ //ellers
					//sÃ¦t fjendens position til et tilfÃ¦ldigt sted over skÃ¦rmen
					e.get(e.size()-1).x = (int)(Math.random()*400);
					e.get(e.size()-1).y = -50;
				}
				e.get(e.size()-1).health = 5*level*level; //sÃ¦t fjendens liv i forhold til svÃ¦rhedsgraden
			}else{
				//hvis der ikke kommer en fjende, sÃ¦t waittime en tak op
				waittime++;
			}
			//kÃ¸rer spiller-objektet's hovedlÃ¸kke, som syrer bl.a. bevÃ¦gelse
			p.run();
			//kÃ¸rer igennem hele fjende-vektoren
			for(int i = 0; i < e.size(); i++){
				//kÃ¸rer fjenden's hovedlykke
				e.get(i).run();
				//hvis fjenden er blevet skudt
				if(e.get(i).dead){
					//hvis y er over 500 (den er flÃ¸jet ud af skÃ¦rmen)
					if(e.get(i).y > 500){
						//fjern den fra vektoren
						e.remove(i);
						//gÃ¥ en tilbage i vektoren (da den er blevet forskubbet en plads)
						i--;
					}
				}
			}
			//kÃ¸rer igennem skud-vektoren
			for(int i = 0; i < s.size(); i++){
				//kÃ¸rer hovedlÃ¸kken for skudet
				s.get(i).run();
				//hvis skudet er flÃ¸jet ud for skÃ¦rmen
				if(s.get(i).x > 500 || s.get(i).y > 500 || s.get(i).x < 0 || s.get(i).y < 0){
					//fjern skuddet
					s.remove(i);
					//gÃ¥ en tilbage i vektoren
					i--;
				//hvis skuddet stadig er pÃ¥ skÃ¦rmen, og det er en fjende som har skudt det af sted
				}else if(!s.get(i).friendly){
					//beregn x og y forskellen pÃ¥ skuddet og spilleren
					int xdiff = (p.x+p.cox)-(s.get(i).x);
					int ydiff = (p.y+p.coy)-(s.get(i).y);
					//hvis bÃ¥de x og y forskellen er mindre end 50
					if(Math.abs(xdiff) < 50 && Math.abs(ydiff) < 50){
						//fjern skuddet
						s.remove(i);
						//gÃ¥ en tilbage i vektoren
						i--;
						//sÃ¦t skade nedtÃ¦lleren til 20 frames
						p.damage = 20;
						//fjerner 25 liv
						health -= 25;
						//sÃ¦tter ryste effekten til at kÃ¸re 20 frames
						shaking = 20;
						//hvis man ikke har mere liv tilbage
						if(health <= 0){
							//spillet er slut
							p.dead = true;
							//fÃ¥ spilleren til at lave en hoppeeffekt
							p.vy = -10;
							//sÃ¦t horisontalbevÃ¦gelse til 0
							p.vx = 0;
						}
					}
				//hvis skuddet er pÃ¥ skÃ¦rmen, og man selv har skudt det af
				}else{
					//kÃ¸r igennem alle fjende-objekterne
					for(int j = 0; j < e.size(); j++){
						//hvis fjenden ikke er dÃ¸d
						if(!e.get(j).dead){
							//beregn x og y forskellen
							int xdiff = (e.get(j).x+e.get(j).cox)-(s.get(i).x);
							int ydiff = (e.get(j).y+e.get(j).coy)-(s.get(i).y);
							//hvis bÃ¥de x og y forskellen er mindre end 50
							if(Math.abs(xdiff) < 50 && Math.abs(ydiff) < 50){
								//fjern skuddet
								s.remove(i);
								//gÃ¥ en tilbage i vektoren
								i--;
								//sat skade nedtÃ¦lleren pÃ¥ fjenden til 20
								e.get(j).damage = 20;
								//fjern 10 af fjendens liv
								e.get(j).health -= 10;
								//hvis fjenden ikke har mere liv tilbage
								if(e.get(j).health <= 0){
									//fjenden er dÃ¸d
									e.get(j).dead = true;
									//sÃ¦t den til at lave et lille hop
									e.get(j).vy = -10;
									//tilfÃ¸j 250 point ganget med hvilket level man er i
									score += 250*level;
									//giv en op i xp
									xp++;
									//hvis man har nok xp til at stige i level
									if(xp > level*6){
										//nulstil xp tÃ¦lleren
										xp -= level*6;
										//sÃ¦t level en op
										level++;
										//sÃ¦tter "level up"-animationen til at kÃ¸re 150 frames
										levelup = 150;
										//giver en masse point
										score += 10000*level;
									}
								}
								//stopper med at loope igennem fjender
								break;
							}
						}
					}
				}
			}
			//gentegner skÃ¦rmen
			w.repaint();
			//venter i 20 millisekunder fÃ¸r lÃ¸kken kÃ¸rer igen, for at fÃ¥ spillet til ikke at kÃ¸re for hurtigt og for at computeren skal kunne fÃ¸lge med og kÃ¸re mere flydene
			try{
				Thread.sleep(20);
			}catch(Exception e){}
		}
	}
	//funktion som bliver kaldt nÃ¥r en tasteturtast bliver sluppet. Implementeret fra KeyListener
	public void keyReleased(java.awt.event.KeyEvent e){
		//hvis man ikke har tabt
		if(!p.dead){
			//hvis man har sluppet A (keycode 65) eller D (keycode 68)
			if(e.getKeyCode() == 65 || e.getKeyCode() == 68){
				//sÃ¦tter den horisontale hastighed til 0
				p.vx = 0;
			}
		}
	}
	//funktion som bliver kaldt nÃ¥r en tasteturtast bliver trykket ned. Implementeret fra KeyListener
	public void keyPressed(java.awt.event.KeyEvent e){
		//hvis man ikke har tabt
		if(!p.dead){
			//hvis man har trykket A
			if(e.getKeyCode() == 65){
				//sÃ¦tter horisontalhastigheden til -4 (4 til venstre)
				p.vx = -4;
			//hvis man har trykket D
			}else if(e.getKeyCode() == 68){
				//sÃ¦tter horisontalhastigheden til 4 (til hÃ¸jre)
				p.vx = 4;
			//hvis man har trykket mellemrum (keycode 32)
			}else if(e.getKeyCode() == 32){
				//hvis man stÃ¥r pÃ¥ jorden
				if(p.y == 400-p.h){
					//lÃ¸fter en 10 op
					p.y -= 10;
					//sÃ¦tter vertikalhastigheden til -10 (10 opad)
					p.vy = -10;
				}
			//hvis man har trykket pÃ¥ P
			}else if(e.getKeyCode() == 80){
				//hvis spillet er pauset, stop pause, ellers start pause
				if(pause){
					pause = false;
				}else{
					pause = true;
				}
			}
		}
	}
	//funktionerne keyTyped, mouseExited, mouseEntered og mouseClicked bruger vi ikke, men skal implementeres alligevel. Kommer fra KeyListener og MouseListener
	public void keyTyped(java.awt.event.KeyEvent e){}
	public void mouseExited(java.awt.event.MouseEvent e){}
	public void mouseEntered(java.awt.event.MouseEvent e){}
	public void mouseClicked(java.awt.event.MouseEvent e){}
	//funktion som bliver kaldt nÃ¥r en musetast bliver sluppet. Implementeret fra MouseListener
	public void mouseReleased(java.awt.event.MouseEvent e){
		//hvis personens y er over 2000 (altsÃ¥ at game over-skÃ¦rmen er vist)
		if(p.y > 2000){
			//hvis man har klikket pÃ¥ venste musetast
			if(e.getButton() == e.BUTTON1){
				//hvis musens position er indenfor knappen pÃ¥ game over-skÃ¦rmen
				if(mouse_pos.x > 150 && mouse_pos.x < 350 && mouse_pos.y > 300 && mouse_pos.y < 350){
					//afslut spillet
					System.exit(0);
				}
			}
		}
	}
	//funktion som bliver kaldt nÃ¥r en mustetast bliver trykket ned. Implementeret fra MouseListener
	public void mousePressed(java.awt.event.MouseEvent e){
		//hvis spillet ikke er slt og ikke pÃ¥ pause
		if(!p.dead && !pause){
			//hvis man har klikket pÃ¥ venste musetast
			if(e.getButton() == e.BUTTON1){
				//beregn x og y forskellen fra spilleren til musen's position
				int xdiff = mouse_pos.x-(p.x+p.cox);
				int ydiff = mouse_pos.y-(p.y+p.coy);
				//beregn afstanden fra spilleren til musen
				double length = Math.sqrt(xdiff*xdiff+ydiff*ydiff);
				//opretter et nyt skud, med position pÃ¥ spilleren, som bevÃ¦ger sig mod musen, og lÃ¦gger det i skud-vektoren
				s.add(new Shot(p.x+p.cox, p.y+p.coy, (int)(xdiff*(5/length)), (int)(ydiff*(5/length)), true));
			//ellers hvis men har hÃ¸jre-klikket (button2 er klik med scrollhjulet)
			}else if(e.getButton() == e.BUTTON3){
				//hvis der ikke er nedkÃ¸ling pÃ¥ superangrebet
				if(cooldown == 0){
					//kÃ¸rer igennem 360 grader, og opretter et nyt skud 40 gange pÃ¥ vejen, hvilket giver 40 skud som bevÃ¦ger sig i hver sin retning
					for(int i = 0; i < 360; i += 360/40){
						s.add(new Shot(p.x+p.cox, p.y+p.coy, (int)(Math.cos(i)*10), (int)(Math.sin(i)*10), true));
					}
					//sÃ¦tter neddkÃ¸ling pÃ¥ vÃ¥bnet. NedkÃ¸lingstiden falder jo lÃ¦ngere man kommer i spillet
					cooldown = 450-level*15;
				}
			}
		}
	}
	//funktion som bliver faldt nÃ¥r musen bevÃ¦ges. Implementeret fra MouseMotionListener
	public void mouseMoved(java.awt.event.MouseEvent e){
		//sÃ¦tter mouse_pos koordinaterne til musens koordinater
		mouse_pos.setLocation(e.getX(), e.getY());
	}
	//funktion som bliver kaldt nÃ¥r musen flyttes, mens en musetast holdes nede. Implementeret fra MouseMotionListener
	public void mouseDragged(java.awt.event.MouseEvent e){
		//sÃ¦tter mouse_pos koordinaterne til musens koordinater
		mouse_pos.setLocation(e.getX(), e.getY());
	}
}
