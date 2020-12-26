package com.ray3k.template.entities;

import com.badlogic.gdx.graphics.Color;
import com.ray3k.template.*;

import static com.ray3k.template.Core.*;
import static com.ray3k.template.screens.GameScreen.*;

public class DisappearingWallEntity extends WallEntity implements Triggerable {
    private static final Color color = Color.valueOf("79eda5");
    public String triggerName;
    public int range;
    
    public DisappearingWallEntity(float width, float height, String triggerName, int range) {
        super(width, height);
        this.triggerName = triggerName;
        this.range = range;
    }
    
    @Override
    public void act(float delta) {
        super.act(delta);
        if (range > -1) {
            var distance = Utils.pointDistance(getCollisionBoxCenterX(), getCollisionBoxCenterY(), PlayerEntity.player.x,  PlayerEntity.player.y);
            if (distance < range) destroy = true;
        }
    }
    
    @Override
    public String getTriggerName() {
        return triggerName;
    }
    
    @Override
    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }
    
    @Override
    public void trigger() {
        destroy = true;
    }
    
    @Override
    public void draw(float delta) {
        if (debugging) {
            shapeDrawer.setColor(color);
            shapeDrawer.filledRectangle(x, y, width, height);
        }
    }
}
