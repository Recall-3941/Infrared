package net.ccbluex.liquidbounce.utils;

public final class Translate2 {
    private float x;
    private float y;
    private boolean first = false;

    public Translate2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void interpolate(float targetX, float targetY, double smoothing) {
        if(first) {
            this.x = (float) AnimationUtils2.animate(targetX, this.x, smoothing);
            this.y = (float) AnimationUtils2.animate(targetY, this.y, smoothing);
        } else {
            this.x = targetX;
            this.y = targetY;
            first = true;
        }
    }
    public void translate(float targetX, float targetY) {
        x = AnimationUtils2.lstransition(x, targetX, 0.0);
        y = AnimationUtils2.lstransition(y, targetY, 0.0);
    }
    public void translate(float targetX, float targetY, double speed) {
        x = AnimationUtils2.lstransition(x, targetX, speed);
        y = AnimationUtils2.lstransition(y, targetY, speed);
    }
    public void interpolate2(float targetX, float targetY, double smoothing) {
        this.x = targetX;
        this.y = (float) AnimationUtils2.animate(targetY, this.y, smoothing);
    }

    public float getX() {
        return this.x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
    }
}

