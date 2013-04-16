package me.evilpeanut;

import java.lang.ref.WeakReference;
import java.util.Arrays;

import org.bukkit.entity.Player;


/**
 * @author desht
 *
 * Adapted from ExperienceUtils code originally in ScrollingMenuSign.
 * 
 * Credit to nisovin (http://forums.bukkit.org/threads/experienceutils-make-giving-taking-exp-a-bit-more-intuitive.54450/#post-1067480)
 * for an implementation that avoids the problems of getTotalExperience(), which doesn't work properly after a player has enchanted something.
 * 
 * Credit to comphenix for further contributions:
 * See http://forums.bukkit.org/threads/experiencemanager-was-experienceutils-make-giving-taking-exp-a-bit-more-intuitive.54450/page-3#post-1273622
 * 
 */
public class ExperienceManager {
	// this is to stop the lookup tables growing without control
	private static int hardMaxLevel = 100000;

	private static int xpRequiredForNextLevel[];
	private static int xpTotalToReachLevel[];

	private final WeakReference<Player> player;
	private final String playerName;

	static {
		// 25 is an arbitrary value for the initial table size - the actual
		// value isn't critically
		// important since the tables are resized as needed.
		initLookupTables(25);
	}

	/**
	 * Create a new ExperienceManager for the given player.
	 * 
	 * @param player The player for this ExperienceManager object
	 */
	public ExperienceManager(Player player) {
		if (player == null)
			throw new IllegalArgumentException("Player cannot be null.");

		this.player = new WeakReference<Player>(player);
		this.playerName = player.getName();
	}
	
	/**
	 * Initialize the XP lookup tables. Basing this on observations noted in
	 * https://bukkit.atlassian.net/browse/BUKKIT-47
	 * 
	 * 7 XP to get to level 1, 17 to level 2, 31 to level 3... At each level,
	 * the increment to get to the next level increases alternately by 3 and 4
	 * 
	 * @param maxLevel The highest level handled by the lookup tables
	 */
	private static void initLookupTables(int maxLevel) {
		xpRequiredForNextLevel = new int[maxLevel];
		xpTotalToReachLevel = new int[maxLevel];

		xpTotalToReachLevel[0] = 0;

		// Valid for MC 1.3 and later
		int incr = 17;
		for (int i = 1; i < xpTotalToReachLevel.length; i++) {
			xpRequiredForNextLevel[i - 1] = incr;
			xpTotalToReachLevel[i] = xpTotalToReachLevel[i - 1] + incr;
			if (i >= 30) {
				incr += 7;
			} else if (i >= 16) {
				incr += 3;
			}
		}
		xpRequiredForNextLevel[xpRequiredForNextLevel.length - 1] = incr;
	}

	/**
	 * Calculate the level that the given XP quantity corresponds to, without
	 * using the lookup tables. This is needed if getLevelForExp() is called
	 * with an XP quantity beyond the range of the existing lookup tables.
	 * 
	 * @param exp
	 * @return
	 */
	private static int calculateLevelForExp(int exp) {
		int level = 0;
		int curExp = 7; // level 1
		int incr = 10;

		while (curExp <= exp) {
			curExp += incr;
			level++;
			incr += (level % 2 == 0) ? 3 : 4;
		}
		return level;
	}

	/**
	 * Get the Player associated with this ExperienceManager.
	 * 
	 * @return the Player object
	 * @throws IllegalStateException if the player is no longer online
	 */
	public Player getPlayer() {
		Player p = player.get();

		if (p == null) {
			throw new IllegalStateException("Player " + playerName + " is not online");
		}
		return p;
	}

	/**
	 * Set the player's fractional experience.
	 * 
	 * @param amt Amount of XP, should not be negative
	 */
	public void setExp(double amt) {
		setExp(0, amt);
	}

	private void setExp(double base, double amt) {
		int xp = (int) (base + amt);

		if (xp < 0)
			xp = 0;

		Player player = getPlayer();
		int curLvl = player.getLevel();
		int newLvl = getLevelForExp(xp);

		// Increment level
		if (curLvl != newLvl) {
			player.setLevel(newLvl);
		}
		// Increment total experience - this should force the server to send an update packet
		if (xp > base) {
			player.setTotalExperience(player.getTotalExperience() + xp - (int)base);
		}

		double pct = (base - getXpForLevel(newLvl) + amt) / (double) (xpRequiredForNextLevel[newLvl]);
		player.setExp((float) pct);
	}

	/**
	 * Get the player's current XP total.
	 * 
	 * @return the player's total XP
	 */
	public int getCurrentExp() {
		Player player = getPlayer();

		int lvl = player.getLevel();
		int cur = getXpForLevel(lvl) + (int) Math.round(xpRequiredForNextLevel[lvl] * player.getExp());
		return cur;
	}

	/**
	 * Get the level that the given amount of XP falls within.
	 * 
	 * @param exp The amount to check for.
	 * @return The level that a player with this amount total XP would be.
	 */
	public int getLevelForExp(int exp) {
		if (exp <= 0)
			return 0;
		if (exp > xpTotalToReachLevel[xpTotalToReachLevel.length - 1]) {
			// need to extend the lookup tables
			int newMax = calculateLevelForExp(exp) * 2;
			if (newMax > hardMaxLevel) {
				throw new IllegalArgumentException("Level for exp " + exp + " > hard max level "
						+ hardMaxLevel);
			}
			initLookupTables(newMax);
		}
		int pos = Arrays.binarySearch(xpTotalToReachLevel, exp);
		return pos < 0 ? -pos - 2 : pos;
	}

	/**
	 * Return the total XP needed to be the given level.
	 * 
	 * @param level The level to check for.
	 * @return The amount of XP needed for the level.
	 */
	public int getXpForLevel(int level) {
		if (level > hardMaxLevel) {
			throw new IllegalArgumentException("Level " + level + " > hard max level "
					+ hardMaxLevel);
		}

		if (level >= xpTotalToReachLevel.length) {
			initLookupTables(level * 2);
		}
		return xpTotalToReachLevel[level];
	}
}