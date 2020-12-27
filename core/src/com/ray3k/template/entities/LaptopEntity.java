package com.ray3k.template.entities;

import com.badlogic.gdx.audio.Sound;
import com.dongbat.jbump.Collisions;
import com.dongbat.jbump.Response.Result;
import com.esotericsoftware.spine.AnimationState.AnimationStateAdapter;
import com.esotericsoftware.spine.AnimationState.TrackEntry;
import com.esotericsoftware.spine.Event;
import com.ray3k.template.screens.*;

import static com.ray3k.template.Core.*;
import static com.ray3k.template.Resources.*;

public class LaptopEntity extends Entity {
    @Override
    public void create() {
        setSkeletonData(spine_laptop,spine_laptopAnimationData);
        if (GameScreen.firstRun) {
            animationState.setAnimation(0, LaptopAnimation.start, false);
            GameScreen.firstRun = false;
        }
        animationState.addAnimation(0, GameScreen.target, false, 0);
        animationState.addListener(new AnimationStateAdapter() {
            @Override
            public void complete(TrackEntry entry) {
                if (entry.getAnimation() == LaptopAnimation.finish || entry.getAnimation() == LaptopAnimation.wrong) {
                    GameScreen.trans = true;
                }
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
