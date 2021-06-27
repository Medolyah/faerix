package gamehub;

import java.util.ArrayDeque;
import java.util.Deque;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Ellipse;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.StateBasedGame;

import de.faerix.base.GameObject;
import de.faerix.base.enums.StoneEnum;
import de.faerix.base.faerie.AttackSparkle;
import de.faerix.base.faerie.Faerie;
import de.faerix.base.stages.GameStage;
import de.faerix.base.stages.StagesHandler;
import map_objects.Portal;
import map_objects.Stone;

public class GameHub extends GameObject{
	
	
	public HpDisplay display; 
	private QButton qButton;
	private Faerie faerie;
	
	
	private static GameHub gamehub = null;
	
	
	public static GameHub getInstance(Faerie faerie) throws SlickException {
		if(gamehub == null) gamehub = new GameHub(faerie);
		return gamehub;
	}
	
	public GameHub(Faerie faerie) throws SlickException {
		this.faerie = faerie;
		this.display = new HpDisplay();
		this.qButton = new QButton();
		this.updateDisplayWithFaerie();
	}


	private void updateDisplayWithFaerie() {
		this.display.setManaDivider(this.faerie.maxAmunition);
		this.display.setHpDivider(this.faerie.maxHp);
	}

	public void checkInput(Input input, GameContainer container, Deque<Stone> interactableObjs,
			Faerie faerie, Portal portal, GameStage stage, StateBasedGame game, Deque<Shape> enemies) {
		if (input.isKeyPressed(Input.KEY_ESCAPE)) {
			container.exit();
		} else if (input.isKeyDown(Input.KEY_UP)) {
			if(faerie.yPosition > 0) {
				faerie.moveY(-1);				
			}
		} else if (input.isKeyDown(Input.KEY_DOWN)) {
			if(faerie.yPosition < container.getHeight()) {				
				faerie.moveY(1);
			}
		} else if (input.isKeyDown(Input.KEY_RIGHT)) {
			if(faerie.xPosition < container.getWidth()) {
				faerie.moveX(+1);
			}
		} else if (input.isKeyDown(Input.KEY_LEFT)) {
			if(faerie.xPosition > 0) {
				faerie.moveX(-1);				
			}
		}
		if(input.isKeyPressed(Input.KEY_Q)) {
			qButton.pressQ(true);
			faerie.autoattack();
			this.display.setMana(faerie.amunition);
		}
		if(input.isKeyPressed(Input.KEY_SPACE)) {
			this.checkIntersection(faerie, interactableObjs);
			this.intersectionPortal(portal, stage, game);
		}

	}
	
	private void intersectionPortal(Portal portal, GameStage stage, StateBasedGame  game) {
		if(portal.shape.intersects(faerie.ellipse) || portal.shape.contains(faerie.ellipse)) {
			stage.goToNextLevel(game); 
		}
		
	}

	public void checkCollision( Deque<Shape> enemies, Faerie faerie){
		for(Shape enemy : enemies) {
			if(faerie.ellipse.intersects(enemy)) {
				faerie.takeDamage(5); 
				display.setHp(faerie.currentHp);
			}
		}
	}
	
	public void checkIfEnemyGotHit( Deque<Shape> enemies, Deque<AttackSparkle> aas){
		System.out.println("test");
		for(Shape enemy : enemies) {
			System.out.println("enemy");
			for(AttackSparkle aa : aas) {
				System.out.println("aa");
				if(aa.shape.intersects(enemy) || enemy.contains(aa.shape)){
					System.out.println("test");
					aa.hasHitEnemy = true; 
					enemies.remove();
				}
			}
		}
	}
	
	public void checkIntersection(Faerie faerie, Deque<Stone> interactableObjs){
		for(Stone shape : interactableObjs) {
			if(faerie.ellipse.intersects(shape.shape)){
				if( shape instanceof  Stone) {
					Stone stone = (Stone)shape; 
					faerie.collectSphere(stone.type);
					this.updateDisplayWithFaerie();
				}
			}
		}
	}
	
	
	public void update( int delta) {
		this.display.update(delta);
		this.qButton.update(delta);
	}
	
	public void render(Graphics g) {
		this.display.render(g);
		this.qButton.render(g);
	}
}