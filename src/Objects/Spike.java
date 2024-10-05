package Objects;

import MainComponents.Game;

public class Spike extends GameObject{
    public Spike(int x, int y, int objectType) {
        super(x, y, objectType);
        initHitbox(32,16); // https://gyazo.com/c41edea6571d34acb212b45056e96289
        xDrawOffset = 0;
        yDrawOffset = (int)(Game.SCALE * 16); // to make it 32
        hitBox.y += yDrawOffset;
    }
}
