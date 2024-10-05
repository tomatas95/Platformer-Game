package GameStates;

public enum GameState {
    // We use enum here because we'll define fixed set constants depending on whether we are playing or in menu etc.
PLAYING, MENU, OPTIONS, QUIT;

    public static GameState state = MENU;
}
