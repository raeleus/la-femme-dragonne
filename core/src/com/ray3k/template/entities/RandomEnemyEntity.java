package com.ray3k.template.entities;

import com.dongbat.jbump.CollisionFilter;
import com.dongbat.jbump.Collisions;
import com.dongbat.jbump.Response;
import com.dongbat.jbump.Response.Result;

import static com.ray3k.template.Core.*;
import static com.ray3k.template.Resources.EnemyAnimation.*;
import static com.ray3k.template.Resources.*;
import static com.ray3k.template.screens.GameScreen.*;

public class RandomEnemyEntity extends Entity {
    public int ordinal;
    public static float ENEMY_SPEED = 200f;
    private static final CollisionFilter bounceFilter = (item, other) -> {
        if (other.userData instanceof WallEntity || other.userData instanceof  PitEntity) {
            return Response.bounce;
        }
        return null;
    };
    
    public RandomEnemyEntity(int ordinal) {
        this.ordinal = ordinal;
    }
    
    @Override
    public void create() {
        setSkeletonData(spine_enemy, spine_enemyAnimationData);
        animationState.setAnimation(0, animation, true);
        skeletonBounds.update(skeleton, true);
        setCollisionBox(skeletonBounds, bounceFilter);

        random.setSeed(ordinal);
        setMotion(ENEMY_SPEED, random.nextFloat() * 360);
    
        depth = ENEMY_DEPTH;
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
        for (int i = 0; i < collisions.size(); i++) {
            var collision = collisions.get(i);
            if (collision.other.userData instanceof WallEntity || collision.other.userData instanceof PitEntity) {
                if (collision.normal.x != 0) deltaX *= -1;
                if (collision.normal.y != 0) deltaY *= -1;
            }
        }
    }
}
