package com.ray3k.template.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.dongbat.jbump.Collisions;
import com.dongbat.jbump.Rect;
import com.dongbat.jbump.Response;
import com.esotericsoftware.spine.AnimationState.AnimationStateAdapter;
import com.esotericsoftware.spine.AnimationState.TrackEntry;
import com.ray3k.template.*;
import com.ray3k.template.entities.JumpeableEntity.*;

import static com.ray3k.template.Core.*;
import static com.ray3k.template.Core.Binding.*;
import static com.ray3k.template.Resources.*;
import static com.ray3k.template.Resources.PlayerAnimation.*;
import static com.ray3k.template.screens.GameScreen.*;

public class PlayerEntity extends Entity {
    public static PlayerEntity player;
    public static float SPRINT_SPEED = 300;
    public static float MOVE_SPEED = 200;
    public static float SLIDE_SPEED = 600;
    public static float SLIDE_SPRINT_SPEED = 700;
    public static float SLIDE_FRICTION = 1400;
    public static float JUMP_SPEED = 550;
    public static float JUMP_SPRINT_SPEED = 650;
    public static float JUMP_FRICTION = 1100;
    public static float HURT_SPEED = 600;
    public static float HURT_FRICTION = 1400;
    private enum Mode {
        SLIDING, JUMPING, HURTING, PIT_FALLING, NORMAL
    }
    private Mode mode = Mode.NORMAL;
    private float friction;
    private float pitRespawnX;
    private float pitRespawnY;
    public static final Vector2 temp = new Vector2();
    private Array<Binding> inputQueue = new Array<>();
    private boolean overObstacle;
    
    @Override
    public void create() {
        player = this;
        setSkeletonData(spine_player, spine_playerAnimationData);
        animationState.setAnimation(0, Resources.PlayerAnimation.stand, true);
        skeletonBounds.update(skeleton, true);
        setCollisionBox(skeletonBounds, (item, other) -> {
            if (other.userData instanceof WallEntity) {
                return Response.slide;
            } else if (other.userData instanceof SlideableEntity) {
                if (mode == Mode.SLIDING) return Response.cross;
                else return Response.slide;
            } else if (other.userData instanceof JumpeableEntity) {
                if (mode == Mode.JUMPING) return Response.cross;
                else return Response.slide;
            } else if (other.userData instanceof PitEntity || other.userData instanceof EnemyEntity || other.userData instanceof ButtonEntity) {
                return Response.cross;
            }
            return null;
        });
        animationState.addListener(new AnimationStateAdapter() {
            @Override
            public void complete(TrackEntry entry) {
                if (entry.getAnimation() == pitDeath) {
                    setPosition(pitRespawnX, pitRespawnY);
                    animationState.setAnimation(0, stand, true);
                    mode = Mode.NORMAL;
                }
            }
        });
        depth = PLAYER_DEPTH;
    }
    
    @Override
    public void actBefore(float delta) {
        if (mode != Mode.JUMPING && mode != Mode.PIT_FALLING) {
            pitRespawnX = x;
            pitRespawnY = y;
        }
    }
    
    @Override
    public void act(float delta) {
        if (mode == Mode.NORMAL) {
            setSpeed(gameScreen.isBindingPressed(SPRINT) ? SPRINT_SPEED : MOVE_SPEED);
            if (gameScreen.areAllBindingsPressed(UP, RIGHT)) {
                animationState.setAnimation(0, PlayerAnimation.northEast, true);
                setDirection(45);
            } else if (gameScreen.areAllBindingsPressed(DOWN, RIGHT)) {
                animationState.setAnimation(0, PlayerAnimation.southEast, true);
                setDirection(325);
            } else if (gameScreen.isBindingPressed(RIGHT)) {
                animationState.setAnimation(0, PlayerAnimation.east, true);
                setDirection(0);
            } else if (gameScreen.areAllBindingsPressed(UP, LEFT)) {
                animationState.setAnimation(0, PlayerAnimation.northWest, true);
                setDirection(135);
            } else if (gameScreen.areAllBindingsPressed(DOWN, LEFT)) {
                animationState.setAnimation(0, PlayerAnimation.southWest, true);
                setDirection(215);
            } else if (gameScreen.isBindingPressed(LEFT)) {
                animationState.setAnimation(0, PlayerAnimation.west, true);
                setDirection(180);
            } else if (gameScreen.isBindingPressed(UP)) {
                animationState.setAnimation(0, PlayerAnimation.north, true);
                setDirection(90);
            } else if (gameScreen.isBindingPressed(DOWN)) {
                animationState.setAnimation(0, PlayerAnimation.south, true);
                setDirection(270);
            } else {
                animationState.setAnimation(0, PlayerAnimation.stand, true);
                setMotion(0, 0);
            }
            
            if (gameScreen.isBindingJustPressed(SLIDE)) {
                inputQueue.add(SLIDE);
            }
            
            if (gameScreen.isBindingJustPressed(JUMP)) {
                inputQueue.add(JUMP);
            }
            
            if (inputQueue.size > 0 && inputQueue.first() == SLIDE && gameScreen.isAnyBindingPressed(UP, DOWN, LEFT, RIGHT)) {
                inputQueue.removeIndex(0);
                setSpeed(gameScreen.isBindingPressed(SPRINT) ? SLIDE_SPRINT_SPEED : SLIDE_SPEED);
                friction = SLIDE_FRICTION;
                mode = Mode.SLIDING;
                animationState.setAnimation(0, slide, true);
            }
    
            if (inputQueue.size > 0 && inputQueue.peek() == JUMP && gameScreen.isAnyBindingPressed(UP, DOWN, LEFT, RIGHT)) {
                inputQueue.removeIndex(0);
                setSpeed(gameScreen.isBindingPressed(SPRINT) ? JUMP_SPRINT_SPEED : JUMP_SPEED);
                friction = JUMP_FRICTION;
                mode = Mode.JUMPING;
                animationState.setAnimation(0, jump, false);
            }
        }
        
        if (mode == Mode.SLIDING) {
            setSpeed(Utils.approach(getSpeed(), overObstacle ? SPRINT_SPEED : 0, friction * delta));
            if (getSpeed() < SPRINT_SPEED) {
                mode = Mode.NORMAL;
                animationState.setAnimation(0, stand, true);
            }
            if (!overObstacle && gameScreen.isBindingJustPressed(JUMP)) {
                inputQueue.add(JUMP);
                mode = Mode.NORMAL;
                animationState.setAnimation(0, stand, true);
            }
        }
    
        if (mode == Mode.JUMPING) {
            setSpeed(Utils.approach(getSpeed(), overObstacle ? SPRINT_SPEED : 0, friction * delta));
            animationState.getCurrent(0).setTrackTime(MathUtils.clamp((JUMP_SPEED - getSpeed()) / (JUMP_SPEED - SPRINT_SPEED), 0, 1));
            if (getSpeed() < SPRINT_SPEED) {
                mode = Mode.NORMAL;
                animationState.addAnimation(0, stand, true, 0);
            }
            
            if (!overObstacle) {
                if (gameScreen.isBindingJustPressed(SLIDE)) {
                    inputQueue.add(SLIDE);
                }
    
                if (gameScreen.areAllBindingsPressed(UP, RIGHT)) {
                    setDirection(45);
                } else if (gameScreen.areAllBindingsPressed(DOWN, RIGHT)) {
                    setDirection(325);
                } else if (gameScreen.isBindingPressed(RIGHT)) {
                    setDirection(0);
                } else if (gameScreen.areAllBindingsPressed(UP, LEFT)) {
                    setDirection(135);
                } else if (gameScreen.areAllBindingsPressed(DOWN, LEFT)) {
                    setDirection(215);
                } else if (gameScreen.isBindingPressed(LEFT)) {
                    setDirection(180);
                } else if (gameScreen.isBindingPressed(UP)) {
                    setDirection(90);
                } else if (gameScreen.isBindingPressed(DOWN)) {
                    setDirection(270);
                }
            }
        }
    
        if (mode == Mode.HURTING) {
            setSpeed(Utils.approach(getSpeed(), 0, friction * delta));
            animationState.getCurrent(0).setTrackTime((JUMP_SPEED - getSpeed()) / JUMP_SPEED);
            if (MathUtils.isZero(getSpeed())) {
                mode = Mode.NORMAL;
                animationState.addAnimation(0, stand, true, 0);
            }
        }
        
        gameScreen.camera.position.set(x, y, 0);
        overObstacle = false;
    }
    
    @Override
    public void draw(float delta) {
        if (debugging) {
            shapeDrawer.setColor(Color.BLUE);
            shapeDrawer.setDefaultLineWidth(1);
            shapeDrawer.rectangle(x + bboxX, y + bboxY, bboxWidth, bboxHeight);
        }
    }
    
    @Override
    public void destroy() {
    
    }
    
    @Override
    public void projectedCollision(Response.Result result) {
        for (int i = 0; i < result.projectedCollisions.size(); i++) {
            var collision = result.projectedCollisions.get(i);
            if (mode == Mode.JUMPING && collision.other.userData instanceof JumpeableEntity) {
                var jumpeable = (JumpeableEntity) collision.other.userData;
                if (jumpeable.jumpDirection != JumpDirection.ALL &&
                        (jumpeable.jumpDirection == JumpDirection.NORTH && collision.normal.y != 1 ||
                                jumpeable.jumpDirection == JumpDirection.SOUTH && collision.normal.y != -1 ||
                                jumpeable.jumpDirection == JumpDirection.WEST && collision.normal.x != -1 ||
                                jumpeable.jumpDirection == JumpDirection.EAST && collision.normal.x != 1)) {
                    result.goalX = collision.itemRect.x;
                    result.goalY = collision.itemRect.y;
                    result.projectedCollisions.remove(i);
                    i--;
                }
            }
        }
    }
    
    @Override
    public void collision(Collisions collisions) {
        for (int i = 0; i < collisions.size(); i++) {
            var collision = collisions.get(i);
            if (collision.other.userData instanceof ButtonEntity) {
                var button = (ButtonEntity) collision.other.userData;
                button.destroy = true;
                var trigger = triggerables.get(button.triggerName);
                if (trigger != null) trigger.trigger();
            }
            
            if (mode != Mode.JUMPING && mode != Mode.PIT_FALLING && collision.other.userData instanceof PitEntity) {
                if (isInside(getCollisionBoxCenterX(), getCollisionBoxCenterY(), collision.otherRect) &&
                        isInside(getCollisionBoxLeft(), getCollisionBoxCenterY(), collision.otherRect) &&
                        isInside(getCollisionBoxRight(), getCollisionBoxCenterY(), collision.otherRect) &&
                        isInside(getCollisionBoxCenterX(), getCollisionBoxTop(), collision.otherRect) &&
                        isInside(getCollisionBoxCenterX(), getCollisionBoxBottom(), collision.otherRect)) {
                    mode = Mode.PIT_FALLING;
                    animationState.setAnimation(0, pitDeath, false);
                    setSpeed(0);
                }
            }
            
            if (mode == Mode.NORMAL && collision.other.userData instanceof EnemyEntity) {
                temp.set(collision.normal.x, collision.normal.y);
                setMotion(HURT_SPEED, temp.angleDeg());
                mode = Mode.HURTING;
                friction = HURT_FRICTION;
                animationState.setAnimation(0, hurt, false);
            }
    
            if (mode == Mode.JUMPING && collision.other.userData instanceof JumpeableEntity) {
                overObstacle = true;
            } else if (mode == Mode.SLIDING && collision.other.userData instanceof SlideableEntity) {
                overObstacle = true;
            }
        }
    }
    
    private boolean isInside(float x, float y, Rect rect) {
        return x > rect.x && x < rect.x + rect.w && y > rect.y && y < rect.y + rect.h;
    }
}