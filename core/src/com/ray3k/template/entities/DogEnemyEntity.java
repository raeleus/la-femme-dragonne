package com.ray3k.template.entities;

import com.ray3k.template.*;

import static com.ray3k.template.entities.PlayerEntity.*;

public class DogEnemyEntity extends EnemyEntity {
    public static float CHASE_RANGE = 500;
    public static float TRIGGER_RANGE = 300;
    
    public DogEnemyEntity(int ordinal) {
        super(ordinal);
    }
    
    @Override
    public void act(float delta) {
        float distance = Utils.pointDistance(getCollisionBoxCenterX(), getCollisionBoxCenterY(), player.getCollisionBoxCenterX(), player.getCollisionBoxCenterY());
        if (distance < TRIGGER_RANGE) {
        
        } else if (distance < CHASE_RANGE) {
        
        }
    }
}
