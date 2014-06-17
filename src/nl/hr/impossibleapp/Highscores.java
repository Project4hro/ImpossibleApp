package nl.hr.impossibleapp;

public class Highscores{
	private int id;
	private String name;
	private int score;
	
	public Highscores(){}
	
	public Highscores(String name, int score){
		super();
		this.name = name;
		this.score = score;
	}
	
	public int getId(){
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public int getScore(){
		return score;
	}
	
	public void setScore(int score){
		this.score = score;
	}
}