package io.turing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class StatTrackerTest {

	@Test
	void testForStatTrackerObject() {
		StatTracker statTracker = new StatTracker("File1", "File2", "File3");
		
		assertNotNull(statTracker);
	}

}
