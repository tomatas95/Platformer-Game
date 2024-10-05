package GameUtilities;

import MainComponents.Game;

public class Constants {

    public static final float GRAVITY = 0.04f * Game.SCALE;
    public static final int ANIMATION_SPEED = 25;

    public static class Projectiles{
        public static int CANNON_BALL_DEFAULT_WIDTH = 15;
        public static int CANNON_BALL_DEFAULT_HEIGHT = 15;

        public static int CANNON_BALL_WIDTH = (int) (Game.SCALE * CANNON_BALL_DEFAULT_WIDTH);
        public static int CANNON_BALL_HEIGHT = (int) (Game.SCALE * CANNON_BALL_DEFAULT_HEIGHT);
        public static float SPEED = 0.5f * Game.SCALE;

    }

    public static class ObjectConstants{
        public static final int RED_POTION = 0;
        public static final int BLUE_POTION = 1;
        public static final int BARREL = 2;
        public static final int BOX = 3;
        public static final int SPIKE = 4;
        public static final int CANNON_AIM_LEFT = 5;
        public static final int CANNON_AIM_RIGHT = 6;


        public static final int RED_POTION_VALUE = 15; // health
        public static final int BLUE_POTION_VALUE = 10; // powers

        public static final int CONTAINER_WIDTH_DEFAULT = 40;
        public static final int CONTAINER_HEIGHT_DEFAULT = 30;
        public static final int CONTAINER_WIDTH = (int)(Game.SCALE * CONTAINER_WIDTH_DEFAULT);
        public static final int CONTAINER_HEIGHT = (int)(Game.SCALE * CONTAINER_HEIGHT_DEFAULT);

        public static final int POTION_WIDTH_DEFAULT = 12;
        public static final int POTION_HEIGHT_DEFAULT = 16;
        public static final int POTION_WIDTH = (int)(Game.SCALE * POTION_WIDTH_DEFAULT);
        public static final int POTION_HEIGHT = (int)(Game.SCALE * POTION_HEIGHT_DEFAULT);

        public static final int SPIKE_WIDTH_DEFAULT = 32;
        public static final int SPIKE_HEIGHT_DEFAULT = 32;
        public static final int SPIKE_WIDTH = (int)(Game.SCALE * SPIKE_WIDTH_DEFAULT);
        public static final int SPIKE_HEIGHT = (int)(Game.SCALE * SPIKE_HEIGHT_DEFAULT);

        public static final int CANNON_WIDTH_DEFAULT = 40;
        public static final int CANNON_HEIGHT_DEFAULT = 26;
        public static final int CANNON_WIDTH = (int)(CANNON_WIDTH_DEFAULT * Game.SCALE);
        public static final int CANNON_HEIGHT = (int)(CANNON_HEIGHT_DEFAULT * Game.SCALE);

        public static int GetSpriteAmount(int object_type){
            return switch (object_type) {
                case RED_POTION, BLUE_POTION, CANNON_AIM_LEFT, CANNON_AIM_RIGHT -> 7;
                case BARREL, BOX -> 8;
                default -> 1;
            };
        }
    }
    public static class EnemyState {
        public static final int HOODLER = 0;

        public static final int IDLE = 0;
        public static final int RUNNING = 3;
        public static final int ATTACK = 8;
        public static final int HIT = 6;
        public static final int DEATH = 7;

        public static final int HOODLER_WIDTH_DEFAULT = 32; //  was 72
        public static final int HOODLER_HEIGHT_DEFAULT = 32;
        public static final int HOODLER_WIDTH = (int) (HOODLER_WIDTH_DEFAULT * Game.SCALE);
        public static final int HOODLER_HEIGHT = (int) (HOODLER_HEIGHT_DEFAULT * Game.SCALE);

        public static final int HOODLER_DRAW_OFFSET_X = (int) (13 * Game.SCALE); // from the start of the sprite to the start of the  hitbox
        public static final int HOODLER_DRAW_OFFSET_Y = (int) (9 * Game.SCALE); // https://gyazo.com/26d3202705c246e23befc24655c96f8b


        public static int GetSpriteAmount(int enemyType, int enemyState) {
            switch (enemyType) {
                case HOODLER:
                    switch (enemyState) {
                        case IDLE:
                            return 2;
                        case RUNNING:
                            return 4;
                        case ATTACK:
                            return 8;
                        case HIT:
                            return 3;
                        case DEATH:
                            return 8;
                    }
            }
            return 0;
        }

        public static int GetMaxHealth(int enemyType) {
            switch (enemyType) {
                case HOODLER:
                    return 10;
                default:
                    return 1;
            }
        }

        public static int GetEnemyDamage(int enemyType) {
            switch (enemyType) {
                case HOODLER:
                    return 15;
                default:
                    return 0;
            }
        }
    }

    public static class MenuButtons {

        public static class PauseButton {
            public static final int SOUND_SIZE_DEFAULT = 42; // because 42 in width and height in pixels.
            public static final int SOUND_SIZE_IN_GAME = (int) (SOUND_SIZE_DEFAULT * Game.SCALE);
        }

        public static class Buttons {
            public static final int BUTTON_WIDTH_DEFAULT = 140;
            public static final int BUTTON_HEIGHT_DEFAULT = 56;
            public static final int BUTTON_WIDTH = (int) (BUTTON_WIDTH_DEFAULT * Game.SCALE);
            public static final int BUTTON_HEIGHT = (int) (BUTTON_HEIGHT_DEFAULT * Game.SCALE);
        }

    }


    public static class Volume_Buttons {
        public static final int VOLUME_DEFAULT_WIDTH = 28;
        public static final int VOLUME_DEFAULT_HEIGHT = 44;
        public static final int SLIDER_DEFAULT_WIDTH = 215;

        public static final int VOLUME_WIDTH = (int) (VOLUME_DEFAULT_WIDTH * Game.SCALE);
        public static final int VOLUME_HEIGHT = (int) (VOLUME_DEFAULT_HEIGHT * Game.SCALE);
        public static final int SLIDER_WIDTH = (int) (SLIDER_DEFAULT_WIDTH * Game.SCALE);

    }

    public static class URM_Buttons {
        public static final int URM_BUTTON_DEFAULT_SIZE = 56;
        public static final int URM_SIZE = (int) (URM_BUTTON_DEFAULT_SIZE * Game.SCALE);
    }

    public static class Directions {
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;

    }

    public static class PlayerConstants {
        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int JUMP = 2;
        public static final int FALLING = 5;
        public static final int ATTACK = 6;
        public static final int HIT = 7;
        public static final int DEATH = 8;

        public static int GetSpriteAmount(int player_action) {
            switch (player_action) {
                case RUNNING:
                    return 7;
                case DEATH:
                    return 7;
                case IDLE:
                    return 7;
                case HIT:
                    return 7;
                case JUMP:
                    return 7;
                case ATTACK:
                    return 7;
                case FALLING:
                    return 7;
                default:
                    return 1;
            }
        }
    }
}
