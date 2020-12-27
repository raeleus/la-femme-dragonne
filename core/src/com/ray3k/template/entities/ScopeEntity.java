package com.ray3k.template.entities;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.dongbat.jbump.Collisions;
import com.dongbat.jbump.Response.Result;
import com.esotericsoftware.spine.AnimationState.AnimationStateAdapter;
import com.esotericsoftware.spine.AnimationState.TrackEntry;
import com.esotericsoftware.spine.Event;
import com.ray3k.template.*;
import com.ray3k.template.Core.*;
import com.ray3k.template.screens.*;

import static com.ray3k.template.JamGame.*;
import static com.ray3k.template.Resources.ScopeAnimation.*;
import static com.ray3k.template.Resources.*;
import static com.ray3k.template.screens.GameScreen.*;

public class ScopeEntity extends Entity {
    private enum Mode {
        IDLE, RELOADING
    }
    private Mode mode;
    
    @Override
    public void create() {
        setSkeletonData(spine_scope, spine_scopeAnimationData);
        skeleton.setScale(.5f, .5f);
        animationState.setAnimation(0, stand, false);
        mode = Mode.IDLE;
        animationState.addListener(new AnimationStateAdapter() {
            @Override
            public void complete(TrackEntry entry) {
                mode = Mode.IDLE;
            }
    
            @Override
            public void event(TrackEntry entry, Event event) {
                if (event.getData().getAudioPath() != null && !event.getData().getAudioPath().equals("")) {
                    Sound sound = assetManager.get("sfx/" + event.getData().getAudioPath());
                    sound.play();
                }
            }
        });
    }
    
    @Override
    public void actBefore(float delta) {
    
    }
    
    @Override
    public void act(float delta) {
        setPosition(gameScreen.camera.position.x, gameScreen.camera.position.y);
        skeleton.setColor(gameScreen.isBindingPressed(Binding.ZOOM) ? Color.WHITE : Color.CLEAR);
        
        if (mode == Mode.IDLE && gameScreen.isBindingJustPressed(Binding.SHOOT)) {
            mode = Mode.RELOADING;
            boolean found = false;
            for (var enemy : GameScreen.enemies) {
                if (enemy.skeletonBounds.aabbContainsPoint(x, y)) {
                    enemy.animationState.setAnimation(0, PersonAnimation.die, false);
                    enemy.setSpeed(0);
                    found = true;
                    break;
                }
            }
            animationState.setAnimation(0, found? shootHit : shootMiss, false);
        }
        
        if (gameScreen.isBindingPressed(Binding.ZOOM) && !pumpkinEntity.destroy && x > pumpkinEntity.x && y > pumpkinEntity.y && x < pumpkinEntity.x + pumpkinEntity.width && y < pumpkinEntity.y + pumpkinEntity.height) {
            pumpkinEntity.destroy = true;
            sfx_gameDragonHeySexy.play(Core.sfx);
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
