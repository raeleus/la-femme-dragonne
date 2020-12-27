package com.ray3k.template.entities;

import com.badlogic.gdx.math.MathUtils;
import com.dongbat.jbump.Collisions;
import com.dongbat.jbump.Response.Result;
import com.esotericsoftware.spine.AnimationState.AnimationStateAdapter;
import com.esotericsoftware.spine.AnimationState.TrackEntry;
import com.ray3k.template.screens.*;

import static com.ray3k.template.Resources.*;
import static com.ray3k.template.screens.GameScreen.*;

public class EnemyEntity extends Entity {
    private static final float WALK_RANGE = 80f;
    private static final float WALK_SPEED_MIN = 20f;
    private static final float WALK_SPEED_MAX = 80f;
    float startX;
    float startY;
    
    @Override
    public void create() {
        setSkeletonData(spine_person, spine_personAnimationData);
        skeleton.setScale(.07f, .07f);
        animationState.setAnimation(0, PersonAnimation.walk, true);
        startX = x;
        startY = y;
        x += MathUtils.random(WALK_RANGE);
        deltaX = MathUtils.randomSign() * MathUtils.random(WALK_SPEED_MIN, WALK_SPEED_MAX);
        skeleton.setScale(Math.signum(deltaX) * skeleton.getScaleX(), skeleton.getScaleY());
        animationState.addListener(new AnimationStateAdapter() {
            @Override
            public void complete(TrackEntry entry) {
                if (entry.getAnimation() == PersonAnimation.die) {
                    destroy = true;
                    if (skeleton.getSkin().getName().equals(GameScreen.target)) {
                        fluidList.removeValue(skeleton.getSkin().getName(), false);
                        numberOfEnemies++;
                        neededWins--;
                        GameScreen.laptop.animationState.setAnimation(0, LaptopAnimation.finish, false);
                    } else {
                        GameScreen.laptop.animationState.setAnimation(0, LaptopAnimation.wrong, false);
                    }
                }
            }
        });
    }
    
    @Override
    public void actBefore(float delta) {
    
    }
    
    @Override
    public void act(float delta) {
        if (x > startX + WALK_RANGE) {
            deltaX *= -1;
            skeleton.setScale(-1 * skeleton.getScaleX(), skeleton.getScaleY());
            x = startX + WALK_RANGE;
        } else if (x < startX) {
            deltaX *= -1;
            skeleton.setScale(-1 * skeleton.getScaleX(), skeleton.getScaleY());
            x = startX;
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
