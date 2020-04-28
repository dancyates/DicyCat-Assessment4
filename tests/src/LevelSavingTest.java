import com.badlogic.gdx.math.Vector2;
import com.dicycat.kroy.entities.FireTruck;
import com.dicycat.kroy.saving.GameSave;
import de.tomgrill.gdxtesting.GdxTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)

public class LevelSavingTest {
	private GameSave testGameSave;

	@Before
	public void init() {
		testGameSave = new GameSave();
	}

	/**
	 * Test ID; LevelSaving_1.1
	 *
	 * Input: N/A
	 * Expected Output: Correct firetruck position is saved
	 */
	@Test
	public void fireTruckShouldSaveToTheCorrectPosition(){
		//Emulates the saving of a game in the case that a medium difficulty game were saved into slot 0 at the point the game is initialised.
		testGameSave.saveGame(0,1, new Vector2(234 * 16, 3900),60*15,6,0, new Vector2(0,0),0);

		assertEquals(new Vector2(234 * 16, 3900),testGameSave.getSpawnPos());
	}

	/**
	 * Test ID; LevelSaving_1.2
	 *
	 * Input: N/A
	 * Expected Output: Correct score is saved
	 */
	@Test
	public void scoreShouldSaveAndLoadCorrectly(){
		//Emulates the saving of a game in the case that a medium difficulty game were saved into slot 0 at the point the game is initialised, but with a score of 1000
		testGameSave.saveGame(0,1, new Vector2(234 * 16, 3900),60*15,6,0, new Vector2(0,0),1000);

		assertEquals(1000, testGameSave.getScore());
	}

	/**
	 * Test ID; LevelSaving_1.3
	 *
	 * Input: N/A
	 * Expected Output: Correct timer is saved
	 */
	@Test
	public void timerShouldSaveAndLoadCorrectly(){
		//Emulates the saving of a game in the case that a medium difficulty game were saved into slot 0 at the point the game is initialised
		testGameSave.saveGame(0,1, new Vector2(234 * 16, 3900),60*15,6,0, new Vector2(0,0),1000);

		assertEquals(900, testGameSave.getGameTimer(), 1); //Checks whether the loaded timer is the same as expected, within a tolerance of 1.
	}

}