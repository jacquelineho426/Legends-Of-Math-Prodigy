package Battle;

import java.util.Comparator;

// sorts players by level
public class sortByLevel  implements Comparator <Hero>{

	@Override
	public int compare(Hero o1, Hero o2) {
		return o1.getLevel() - o2.getLevel();
	}

	
}
