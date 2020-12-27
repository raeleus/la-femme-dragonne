package com.ray3k.template.entities;

import com.badlogic.gdx.Gdx;
import com.dongbat.jbump.Collisions;
import com.dongbat.jbump.Response.Result;

import static com.ray3k.template.Core.Binding.*;
import static com.ray3k.template.screens.GameScreen.*;

public class CameraEntity extends Entity {
    @Override
    public void create() {
    
    }
    
    @Override
    public void actBefore(float delta) {
    
    }
    
    @Override
    public void act(float delta) {
        if (!gameScreen.isBindingPressed(ZOOM)) {
            gameScreen.camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
            gameScreen.camera.zoom = 1f;
        } else {
            var x = (float) Gdx.input.getX();
            var y = (float) Gdx.graphics.getHeight() - Gdx.input.getY();
            gameScreen.camera.position.set(x * WORLD_WIDTH / Gdx.graphics.getWidth(), y * WORLD_HEIGHT / Gdx.graphics.getHeight(), 0);
            gameScreen.camera.zoom = .15f;
        }
    }
    
    @Override
    public void draw(float delta) {
    
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
