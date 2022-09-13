package Battle;

import java.util.Comparator;

// sorts players by name
public class sortByName implements Comparator <Hero>{

	@Override
	public int compare(Hero o1, Hero o2) {
		// TODO Auto-generated method stub
		return o1.getName().compareToIgnoreCase(o2.getName());
	}

}
