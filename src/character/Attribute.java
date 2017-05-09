package character;

public class Attribute extends Stat {
	private static final long serialVersionUID = 1L;
	public static final String HP = "hp";
	public static final String AC = "ac";
	public static final String PB = "pb";
	public static final String NAC = "nac";

	public Attribute(){
		super();
	}
	public Attribute(String name, double baseVal){
		super(name, baseVal);
	}
	
	@Override 
	public double getTotal(){
		return (this.getBaseVal() + this.getBonusVal());
	}
}
