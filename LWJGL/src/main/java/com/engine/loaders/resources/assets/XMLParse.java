package com.engine.loaders.resources.assets;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import com.engine.loaders.resources.ContainerObject;
import com.engine.loaders.resources.EnemyObject;
import com.engine.loaders.resources.PlayerObject;
import com.engine.loaders.resources.ProjectileObject;
import com.engine.loaders.resources.RotMJGameObject;
import com.engine.loaders.resources.WallObject;
import com.engine.loaders.resources.WeaponObject;
import com.owlike.genson.Genson;

public class XMLParse {
	public static Hashtable<String,WeaponObject> weapons = new Hashtable<String,WeaponObject>();
	public static Hashtable<String,ProjectileObject> projectiles = new Hashtable<String,ProjectileObject>();
	public static Hashtable<String,ContainerObject> containers = new Hashtable<String,ContainerObject>();
	public static Hashtable<String,RotMJGameObject> gameObjects = new Hashtable<String,RotMJGameObject>();
	public static Hashtable<String,WallObject> walls = new Hashtable<String,WallObject>();
	public static Hashtable<String,PlayerObject> players = new Hashtable<String,PlayerObject>();
	public static Hashtable<String,EnemyObject> enemies = new Hashtable<String,EnemyObject>();
	public XMLParse(String path){
		load(path);
	}
	public static void load(String path) {
		
		SAXBuilder builder = new SAXBuilder();
		File xmlFile = new File(path);

		try {

			Document document = (Document) builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			List<Element> list = rootNode.getChildren("Object");

			for (int i = 0; i < list.size(); i++) {

				Element node = (Element) list.get(i);
				String clas = node.getChildText("Class");
				if (clas.equals("Equipment")) {
					parseItem(node);
				}else if(clas.equals("Projectile")){
					parseProjectile(node);
				}else if(clas.equals("Container")){
					parseContainer(node);
				}else if(clas.equals("Wall")){
					parseWall(node);
				}else if(clas.equals("GameObject")){
					parseGameObject(node);
				}else if(clas.equals("Player")){
					parsePlayer(node);
				}
				else if(clas.equals("Enemy")){
					parseEnemy(node);
				}

			}
			System.out.println("done");

		} catch (IOException io) {
			System.out.println(io.getMessage());
		} catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		}
	}

	public static void parseItem(Element node) {
		Genson gen = new Genson();
		Element textureMap = node.getChild("Texture");
		String name = node.getAttributeValue("id");
		int id = Integer.parseInt(node.getAttributeValue("type"));
		String file = textureMap.getChildText("File");
		int x = Integer.parseInt(textureMap.getChildText("X"));
		int y = Integer.parseInt(textureMap.getChildText("Y"));

		int numProjectiles = Integer.parseInt(node
				.getChildText("NumProjectiles"));

		Element projectile = node.getChild("Projectile");
		String namePro = projectile.getChildText("ObjectId");
		int speed = Integer.parseInt(projectile.getChildText("Speed"));
		int min = Integer.parseInt(projectile.getChildText("MinDamage"));
		int max = Integer.parseInt(projectile.getChildText("MaxDamage"));
		double range = Double.parseDouble(projectile.getChildText("Range"));
		float angleBetween = Float.parseFloat(projectile.getChildText("AngleBetweenShots"));
		boolean multiHit = (projectile.getChildText("MultiHit")) != null;
		double rof = Double.parseDouble(node.getChildText("RateOfFire"));

		com.engine.graph.Texture tex = null;
		try {
			//tex = new com.engine.graph.Texture(file, x, y);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WeaponObject w = new WeaponObject(name, id, tex, true,true,rof, namePro, speed,
				min, max, numProjectiles, range, multiHit,angleBetween);
		//System.out.println(gen.serialize(w));
		weapons.put(name,w);

	}
	public static void parseProjectile(Element node) {
		Genson gen = new Genson();
		Element textureMap = node.getChild("Texture");
		String name = node.getAttributeValue("id");
		int id = Integer.parseInt(node.getAttributeValue("type"));
		String file = textureMap.getChildText("File");
		int x = Integer.parseInt(textureMap.getChildText("X"));
		int y = Integer.parseInt(textureMap.getChildText("Y"));
		int corrAngle = Integer.parseInt(node.getChildText("AngleCorrection"));
		int size = Integer.parseInt(node.getChildText("Size"));
		
		
		com.engine.graph.Texture tex = null;
		try {
			//tex = new com.engine.graph.Texture(file, x, y);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ProjectileObject pr = new ProjectileObject(name,id,tex,corrAngle, size);
		//System.out.println(gen.serialize(pr));
		projectiles.put(name, pr);

	}
	public static void parseContainer(Element node){
		Element textureMap = node.getChild("Texture");
		String name = node.getAttributeValue("id");
		int id = Integer.parseInt(node.getAttributeValue("type"));
		String file = textureMap.getChildText("File");
		int x = Integer.parseInt(textureMap.getChildText("X"));
		int y = Integer.parseInt(textureMap.getChildText("Y"));
		boolean normalItems = (node.getChildText("CanPutNormalObjects")) != null;
		boolean loot = (node.getChildText("Loot")) != null;
		com.engine.graph.Texture tex = null;
		try {
			//tex = new com.engine.graph.Texture(file, x, y);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ContainerObject c = new ContainerObject(name, id, tex, normalItems, loot);
		containers.put(name, c);
	}
	public static void parseGameObject(Element node){
		Element textureMap = node.getChild("Texture");
		String name = node.getAttributeValue("id");
		int id = Integer.parseInt(node.getAttributeValue("type"));
		String file = textureMap.getChildText("File");
		int x = Integer.parseInt(textureMap.getChildText("X"));
		int y = Integer.parseInt(textureMap.getChildText("Y"));
		boolean drawOnGround = (node.getChildText("DrawOnGround")) != null;
		boolean moveable = (node.getChildText("Static")) != null;
		com.engine.graph.Texture tex = null;
		try {
			//tex = new com.engine.graph.Texture(file, x, y);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RotMJGameObject g = new RotMJGameObject(name,id,tex,drawOnGround,moveable);
		gameObjects.put(name, g);
		
	}
	public static void parseWall(Element node){
		Element textureMap = node.getChild("Texture");
		String name = node.getAttributeValue("id");
		int id = Integer.parseInt(node.getAttributeValue("type"));
		String file = textureMap.getChildText("File");
		int x = Integer.parseInt(textureMap.getChildText("X"));
		int y = Integer.parseInt(textureMap.getChildText("Y"));
			Element topWallElement = node.getChild("Top");
				Element texTop = topWallElement.getChild("Texture");
				String fileTop = texTop.getChildText("File");
				int xTop = Integer.parseInt(texTop.getChildText("X"));
				int yTop = Integer.parseInt(texTop.getChildText("Y"));
				com.engine.graph.Texture top = null;
				try {
					//top = new com.engine.graph.Texture(fileTop,xTop,yTop);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		com.engine.graph.Texture tex = null;
		try {
			//tex = new com.engine.graph.Texture(file, x, y);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WallObject w = new WallObject(name,id,tex, true,false,top);
		walls.put(name, w);
	}
	public static void parsePlayer(Element node){
		Element textureMap = node.getChild("Texture");
		String name = node.getAttributeValue("id");
		int id = Integer.parseInt(node.getAttributeValue("type"));
		String file = textureMap.getChildText("File");
		int x = Integer.parseInt(textureMap.getChildText("X"));
		int y = Integer.parseInt(textureMap.getChildText("Y"));
		com.engine.graph.Texture tex = null;
		try {
			//tex = new com.engine.graph.Texture(file, x, y);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int maxHp, hp, maxAtt,att,
		maxDef, def, maxSpeed, spd, maxDex, dex,
		maxVit, vit;
		int[] slots = new int[12];
		int[] equip = new int[12];
		String slotString = node.getChildText("SlotTypes");
		
		String[] splits = slotString.split(",");
		for(int i = 0 ; i<splits.length;i++){
			String s = splits[i];
			slots[i]=Integer.parseInt(s);
		}
		String equipString = node.getChildText("Equipment");
		
		splits = equipString.split(",");
		for(int i = 0 ; i<splits.length;i++){
			String s = splits[i];
			equip[i]=Integer.parseInt(s);
		}
		Element childNode = node.getChild("Hp");
		maxHp=Integer.parseInt(childNode.getAttributeValue("max"));
		hp=Integer.parseInt(childNode.getText());
		childNode = node.getChild("Attack");
		maxAtt=Integer.parseInt(childNode.getAttributeValue("max"));
		att=Integer.parseInt(childNode.getText());
		childNode = node.getChild("Defense");
		maxDef=Integer.parseInt(childNode.getAttributeValue("max"));
		def=Integer.parseInt(childNode.getText());
		childNode = node.getChild("Speed");
		maxSpeed=Integer.parseInt(childNode.getAttributeValue("max"));
		spd=Integer.parseInt(childNode.getText());
		childNode = node.getChild("Dexterity");
		maxDex=Integer.parseInt(childNode.getAttributeValue("max"));
		dex=Integer.parseInt(childNode.getText());
		childNode = node.getChild("Vitality");
		maxVit=Integer.parseInt(childNode.getAttributeValue("max"));
		vit=Integer.parseInt(childNode.getText());
		PlayerObject p = new PlayerObject(name,id,tex,true, true, slots,equip,maxHp, hp, maxAtt,att,
				maxDef, def, maxSpeed, spd, maxDex, dex,
				maxVit, vit);
		Genson gen = new Genson();
		//System.out.println(gen.serialize(p));
		players.put(name, p);
	}
	public static void parseEnemy(Element node){
		Element textureMap = node.getChild("Texture");
		String name = node.getAttributeValue("id");
		int id = Integer.parseInt(node.getAttributeValue("type"));
		String file = textureMap.getChildText("File");
		int x = Integer.parseInt(textureMap.getChildText("X"));
		int y = Integer.parseInt(textureMap.getChildText("Y"));
		com.engine.graph.Texture tex = null;
		try {
			//tex = new com.engine.graph.Texture(file, x, y);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int maxHp,def, size;
		maxHp=Integer.parseInt(node.getChildText("MaxHitPoints"));
		def=Integer.parseInt(node.getChildText("Defense"));
		size=Integer.parseInt(node.getChildText("Size"));
		
		ArrayList<AttackPattern> patterns = new ArrayList<AttackPattern>();
		
		List<Element> projectileNodes = node.getChildren("Projectile");
		for(Element projectileNode : projectileNodes){
			
			String projId = projectileNode.getChildText("ObjectId");
			float angleBetween = Float.parseFloat(projectileNode.getChildText("AngleBetweenShots"));
			float range = Float.parseFloat(projectileNode.getChildText("Range"));
			int speed = Integer.parseInt(projectileNode.getChildText("Speed"));
			int sizeProjectile = Integer.parseInt(projectileNode.getChildText("Size"));
			int damage = Integer.parseInt(projectileNode.getChildText("Damage"));
			int delay = Integer.parseInt(projectileNode.getChildText("ShotDelay"));
			boolean targetPlayer = Boolean.parseBoolean(projectileNode.getChildText("TargetPlayer"));
			int numShots = Integer.parseInt(projectileNode.getChildText("ShotsPerFrame"));
			AttackPattern p = new AttackPattern(delay,targetPlayer,numShots,angleBetween,damage,sizeProjectile,speed,range,projId);
			patterns.add(p);
			
		}
		
		
			
		
		
		EnemyObject e = new EnemyObject(name,id,tex,true,true,maxHp,def,size,patterns);
		Genson gen = new Genson();
		//System.out.println(gen.serialize(e));
		enemies.put(name, e);
	}
	
	public ProjectileObject getProjectileObject(String name){
		return projectiles.get(name);
	}
	public WeaponObject getWeapon(String name){
		return weapons.get(name);
	}
	public ContainerObject getContainer(String name){
		return containers.get(name);
	}
	public RotMJGameObject getGameObject(String name){
		return gameObjects.get(name);
	}
	public WallObject getWallObject(String name){
		return walls.get(name);
	}
	public PlayerObject getPlayerObject(String name){
		return players.get(name);
	}
	public EnemyObject getEnemyObject(String name){
		return enemies.get(name);
	}
}
