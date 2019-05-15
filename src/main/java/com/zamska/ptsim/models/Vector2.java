package com.zamska.ptsim.models;

public class Vector2<T extends Number> {
    private T x;
    private T y;

    public Vector2(T x, T y) {
        this.x = x;
        this.y = y;
    }

    public T getX() {
        return x;
    }

    public void setX(T x) {
        this.x = x;
    }

    public T getY() {
        return y;
    }

    public void setY(T y) {
        this.y = y;
    }

    public Vector2<Float> diffFloat(Vector2<Float> other) {
        return new Vector2<>(other.getX() - getX().floatValue(), other.getY() - getY().floatValue());
    }

    public Vector2<Float> getUnitVector() {
        float m = magnitude();
        return new Vector2<>(getX().floatValue() / m, getY().floatValue() / m);
    }

    public Vector2<Float> multiply(float k) {
        return new Vector2<>(getX().floatValue() * k, getY().floatValue() * k);
    }

    public Vector2<Float> add(Vector2<T> other) {
        return new Vector2<>(getX().floatValue() + other.getX().floatValue(), getY().floatValue() + other.getY().floatValue());
    }

    public Vector2<Integer> round() {
        return new Vector2<>(Math.round(getX().floatValue()), Math.round(getY().intValue()));
    }

    public float magnitude() {
        return (float) Math.sqrt(getX().floatValue() * getX().floatValue() + getY().floatValue() * getY().floatValue());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector2)) {
            return false;
        }

        Vector2 otherVector = (Vector2) obj;

        return otherVector.x.equals(x) && otherVector.y.equals(y);
    }

    @Override
    public String toString() {
        return String.format(
                "<%f, %f>",
                x.floatValue(),
                y.floatValue()
        );
    }
}
