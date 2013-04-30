package me.evilpeanut;

/**
 * @author Reece Aaron Lecrivain
 */
public enum Rank {
	Visitor((byte)0, "§0[§7Visitor§0]", "7", 0),
	Builder((byte)1, "§0[§EBuilder§0]", "E", 0),
	AdvancedBuilder((byte)2, "§0[§5Adv.Builder§0]", "5", 0),
	Architect((byte)3, "§0[§DArchitect§0]", "D", 0),
	Moderator((byte)4, "§0[§9Moderator§0]", "9", 0),
	Police((byte)5, "§0[§3Police§0]", "3", 0),
	Admin((byte)6, "§0[§4Admin§0]", "4", 5000),
	SpecialAdmin((byte)7, "§0[§4SpecAdmin§0]", "4", 10000),
	Councillor((byte)8, "§0[§ACouncillor§0]", "A", 15000),
	Elite((byte)9, "§0[§CElite§0]", "C", 20000),
	CoOwner((byte)10, "§0[§6Co-Owner§0]", "6", 22000),
	Owner((byte)11, "§0[§BOwner§0]", "B", 25000),
	RainbowOwner((byte)12, "§0[§AO§Bw§Cn§De§Er§0]", "A", 50000),
	Custom((byte)13, "§0[§6Custom§0]", "6", 75000),
	ServerOwner((byte)14, "§0[§BServerHost§0]", "B", 1000000);
	
	private Byte ID;
	private String prefix;
	private String color;
	private Integer evilEditAreaLimit;
	
	/**
	 * @return The ID of the rank
	 */
	Byte getID(){ return ID; }
	
	/**
	 * @param ID The ID of the rank
	 */
	void setID(Byte ID){ this.ID = ID; }
	
	/**
	 * @return The prefix of the rank
	 */
	String getPrefix(){ return prefix; }
	
	/**
	 * @param prefix The prefix of the rank
	 */
	void setPrefix(String prefix){ this.prefix = prefix; }
	
	/**
	 * @return The color of the rank
	 */
	String getColor(){ return color; }
	
	/**
	 * @param color The color of the rank
	 */
	void setColor(String color){ this.color = color; }
	
	/**
	 * @return The evil edit area limit of the rank
	 */
	Integer getEvilEditAreaLimit(){ return evilEditAreaLimit; }
	
	/**
	 * @param evilEditAreaLimit The evil edit area limit of the rank
	 */
	void setEvilEditAreaLimit(Integer evilEditAreaLimit){ this.evilEditAreaLimit = evilEditAreaLimit; }
	
	/**
	 * Define a new rank
	 * @param ID The ID of the rank
	 * @param prefix The prefix of the rank
	 * @param color The color of the rank
	 * @param evilEditAreaLimit The evil edit area limit of the rank
	 */
	Rank (Byte ID, String prefix, String color, Integer evilEditAreaLimit) {
		this.ID = ID;
		this.prefix = prefix;
		this.color = color;
		this.evilEditAreaLimit = evilEditAreaLimit;
	}
	
	/**
	 * Return the next rank on the rank ladder
	 * @param rank The rank
	 * @return The next rank on the rank ladder
	 */
	public static Rank getNextRank(Rank rank) {
		switch (rank) {
		case Visitor:
			return Rank.Builder;
		case Builder:
			return Rank.AdvancedBuilder;
		case AdvancedBuilder:
			return Rank.Architect;
		case Architect:
			return Rank.Moderator;
		case Moderator:
			return Rank.Police;
		case Police:
			return Rank.Admin;
		case Admin:
			return Rank.SpecialAdmin;
		case SpecialAdmin:
			return Rank.Councillor;
		case Councillor:
			return Rank.Elite;
		case Elite:
			return Rank.CoOwner;
		case CoOwner:
			return Rank.Owner;
		case Owner:
			return Rank.RainbowOwner;
		case RainbowOwner:
			return Rank.Custom;
		case Custom:
			return Rank.ServerOwner;
		case ServerOwner:
			return Rank.ServerOwner;
		}
		return rank;
	}
	
	/**
	 * Return the previous rank on the rank ladder
	 * @param rank The rank
	 * @return The previous rank on the rank ladder
	 */
	public static Rank getPreviousRank(Rank rank) {
		switch (rank) {
		case Visitor:
			return Rank.Visitor;
		case Builder:
			return Rank.Visitor;
		case AdvancedBuilder:
			return Rank.Builder;
		case Architect:
			return Rank.AdvancedBuilder;
		case Moderator:
			return Rank.Architect;
		case Police:
			return Rank.Moderator;
		case Admin:
			return Rank.Police;
		case SpecialAdmin:
			return Rank.Admin;
		case Councillor:
			return Rank.SpecialAdmin;
		case Elite:
			return Rank.Councillor;
		case CoOwner:
			return Rank.Elite;
		case Owner:
			return Rank.CoOwner;
		case RainbowOwner:
			return Rank.Owner;
		case Custom:
			return Rank.RainbowOwner;
		case ServerOwner:
			return Rank.Custom;
		}
		return rank;
	}
}
