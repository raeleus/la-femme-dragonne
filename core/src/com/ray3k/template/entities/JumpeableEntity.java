package com.ray3k.template.entities;

import com.badlogic.gdx.graphics.Color;
import com.dongbat.jbump.Collisions;
import com.dongbat.jbump.Response.Result;

import static com.ray3k.template.Core.*;
import static com.ray3k.template.screens.GameScreen.*;

public class JumpeableEntity extends Entity {
    public float width;
    public float height;
    public static Color color = Color.valueOf("47aded");
    public enum JumpDirection {
        ALL, NORTH, SOUTH, EAST, WEST
    }
    public JumpDirection jumpDirection;
    
    public JumpeableEntity(float width, float height, JumpDirection jumpDirection) {
        this.width = width;
        this.height = height;
        setCollisionBox(0, 0, width, height, nullFilter);
        this.jumpDirection = jumpDirection;
    }
    
    @Override
    public void create() {
        depth = PROP_DEPTH;
    }
    
    @Override
    public void actBefore(float delta) {
    
    }
    
    @Override
    public void act(float delta) {
    
    }
    
    @Override
    public void draw(float delta) {
        if (debugging) {
            shapeDrawer.setColor(color);
            shapeDrawer.filledRectangle(x, y, width, height);
        }
    }
    
    @Override
    public void destroy() {
    
    }
    
    @Override
    public void projectedCollision(Result result) {
    
    }
    
    @Override
    public void collision(Collisions collisions) {
    
    }
}
