package com.ray3k.template.entities;

import com.badlogic.gdx.graphics.Color;
import com.dongbat.jbump.CollisionFilter;
import com.dongbat.jbump.Collisions;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Response;
import com.dongbat.jbump.Response.Result;

import static com.ray3k.template.Core.*;
import static com.ray3k.template.screens.GameScreen.*;

public class PitEntity extends Entity {
    public float width;
    public float height;
    
    public PitEntity(float width, float height) {
        this.width = width;
        this.height = height;
        setCollisionBox(0, 0, width, height, nullFilter);
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
            shapeDrawer.setColor(Color.BLACK);
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
