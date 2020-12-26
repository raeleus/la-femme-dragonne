package com.ray3k.template.entities;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasSprite;
import com.dongbat.jbump.Collisions;
import com.dongbat.jbump.Response.Result;
import com.ray3k.template.*;

import static com.ray3k.template.Core.*;

public class DecalEntity extends Entity {
    private AtlasSprite sprite;
    
    public DecalEntity(float centerX, float centerY, String regionName) {
        sprite = new AtlasSprite(Resources.textures_textures.findRegion(regionName));
        sprite.setOriginCenter();
        sprite.setPosition(centerX - sprite.getWidth() / 2, centerY - sprite.getHeight() / 2);
        depth = BACKGROUND_DEPTH;
    }
    
    @Override
    public void create() {
    
    }
    
    @Override
    public void actBefore(float delta) {
    
    }
    
    @Override
    public void act(float delta) {
    
    }
    
    @Override
    public void draw(float delta) {
        sprite.draw(batch);
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
