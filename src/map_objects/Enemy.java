package map_objects;

import java.util.List;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Ellipse;

import de.faerix.base.GameObject;
import de.faerix.base.enums.ContainerSize;

public class Enemy extends GameObject {
	public float xPos;
	public float yPos;
	public Image img;
	public Image damageImg;
	public Image normalImg;
	public Ellipse shape;
	public int size;
	public int maxHp;
	public float velocity;

	public boolean isDead = false;
	public boolean hasStone = false;
	public int hp;
	private Sound dmg;

	public Enemy() {
		Random random = new Random();
		this.xPos = (float) random.nextInt(ContainerSize.width.getNumVal());
		this.yPos = (float) random.nextInt(ContainerSize.height.getNumVal());
		this.size = 100 + random.nextInt(100);
		this.shape = new Ellipse(this.xPos, this.yPos, size / 4, size / 4);
		this.maxHp = 50;
		this.velocity = (float) (random.nextFloat() * 0.09);
		try {
			this.dmg = new Sound("assets/sound/uk.ogg");
		} catch (SlickException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			this.damageImg = new Image("assets/map_objects/enemy_dmg.png").getScaledCopy(size, size);
			this.normalImg = new Image("assets/map_objects/enemy.png").getScaledCopy(size, size);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		if (random.nextInt(100) < 80)
			this.hasStone = true;
	}

	public void move(float faerieX, float faerieY) {
		if (this.xPos - faerieX < 0)
			this.xPos += 1 * 0.02;
		else
			this.xPos -= 1 * this.velocity;
		if (this.yPos - faerieY < 0)
			this.yPos += 1 * 0.02;
		else
			this.yPos -= 1 * this.velocity;
	}

	private boolean invincible = false;
	private int timer = 1001;

	public void takeDamage(int hp) {
		System.out.println(hp);
		if (!this.invincible) {
			this.maxHp -= hp;
			this.img = this.damageImg;
			this.dmg.play();
			this.invincible = true;
			this.timer = 0;
			if (this.maxHp <= 0) {
				this.isDead = true;
			}
		}
	}

	public void render(Graphics g) {
		g.setColor(Color.black);
		g.fill(shape);
		if (this.timer > 500) {
			this.img = this.normalImg;
		} else {
			this.img = this.damageImg;
		}
		g.drawImage(this.img, this.xPos - size / 2, this.yPos - size / 2);
	}

	public void update(int delta) {
		this.shape.setCenterX(this.xPos);
		this.shape.setCenterY(this.yPos);
		this.timer++;
		if (1000 < this.timer) {
			this.invincible = false;
		}
	}

}
