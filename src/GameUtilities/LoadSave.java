package GameUtilities;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;


public class LoadSave {

    public static final String PLAYER_ATLAS = "character_sprite.png";
    public static final String LEVEL_ATLAS = "outside_sprites.png";
    public static final String MENU_BUTTONS = "button_atlas.png";

    public static final String MENU_BACKGROUND = "menu_background.png";
    public static final String PAUSE_BACKGROUND = "pause_menu.png";
    public static final String PAUSE_SOUND_BUTTONS = "sound_button.png";
    public static final String PAUSE_URM_BUTTONS = "urm_buttons.png";
    public static final String VOLUME_BUTTONS = "volume_buttons.png";
    public static final String MENU_BACKGROUND_IMAGE_FILLER = "background_menu.png";
    public static final String PLAYING_BACKGROUND_IMAGE_FILLER = "playing_background_asset.png";
    public static final String ENEMY_HOODLER = "hoodler_sprite.png";

    public static final String STATUS_BAR = "health_power_bar.png";
    public static final String LEVEL_COMPLETE = "completed_sprite.png";

    public static final String POTION_ATLAS = "potions_sprites.png";
    public static final String CONTAINER_ATLAS = "objects_sprites.png";
    public static final String TRAP_ATLAS = "spike_asset.png";
    public static final String CANNON_ATLAS = "cannon_atlas.png";
    public static final String CANNON_BALL = "ball.png";
    public static final String DEATH_SCREEN = "death_screen.png";
    public static final String OPTIONS_MENU = "options_background.png";




    public static BufferedImage GetSpriteAtlas(String fileName) throws IOException {
        BufferedImage image = null;

        InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
        // takes image as an input stream, and we can load this image as an input
        assert is != null;
        image = ImageIO.read(is);
        is.close();
        return image;
    }

    public static BufferedImage[] GetAllLevels() throws URISyntaxException, IOException {
        URL url = LoadSave.class.getResource("/gameLevels");
        File file = null;

        file = new File(url.toURI());

        File[] levelFiles = file.listFiles();
        File[] sortedLevelFiles = new File[levelFiles.length];

        for (int i = 0; i < sortedLevelFiles.length; i++) {
            for (int j = 0; j < levelFiles.length; j++) {
                if (levelFiles[j].getName().equals((i + 1) + ".png")) {
                    sortedLevelFiles[i] = levelFiles[j];
                }
            }
        }
        BufferedImage[] levelImages = new BufferedImage[sortedLevelFiles.length];
        for (int i = 0; i < levelImages.length; i++) {
            levelImages[i] = ImageIO.read(sortedLevelFiles[i]);
        }

        return levelImages;

    }
}
