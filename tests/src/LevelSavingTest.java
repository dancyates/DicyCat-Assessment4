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

	@Test
	public void fireTruckShouldSaveToTheCorrectPosition(){
		//Emulates the saving of a game in the case that a medium difficulty game were saved into slot 0 at the point the game is initialised.
		testGameSave.saveGame(0,1, new Vector2(234 * 16, 3900),60*15,6,0, new Vector2(0,0));
		testGameSave.addPlayer(new FireTruck(new Vector2(234 * 16, 3900), new Float[]{0f,0f,0f}, 2));

		assertEquals(new Vector2(234 * 16, 3900),testGameSave.getPlayers().get(0).getPosition());
	}

	@Test
	public void 

}