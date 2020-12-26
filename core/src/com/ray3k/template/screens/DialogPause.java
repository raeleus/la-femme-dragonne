package com.ray3k.template.screens;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.ray3k.template.Core;
import com.ray3k.template.JamScreen;
import com.ray3k.template.entities.*;

import static com.ray3k.template.Core.*;
import static com.ray3k.template.entities.RandomEnemyEntity.*;
import static com.ray3k.template.entities.PlayerEntity.*;

public class DialogPause extends Dialog {
    private JamScreen jamScreen;
    private boolean detectInput;
    
    public DialogPause(JamScreen jamScreen) {
        super("", skin);
        this.jamScreen = jamScreen;
        
        detectInput = true;
    
        Table root = getContentTable();
        
        root.pad(10);
        root.defaults().space(10);
        Label label = new Label("Paused", skin);
        root.add(label);
        
        root.row();
        label = new Label("Press Escape or any control\nto resume game", skin);
        label.setAlignment(Align.center);
        root.add(label);
        
        root.row();
        Table table = new Table();
        root.add(table);
    
        table.defaults().space(10);
        TextButton textButton = new TextButton("Resume", skin);
        table.add(textButton);
        textButton.addListener(sndChangeListener);
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                fire(new ResumeEvent());
                hide();
                detectInput = false;
            }
        });
        
        textButton = new TextButton("Quit", skin);
        table.add(textButton);
        textButton.addListener(sndChangeListener);
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                fire(new QuitEvent());
                hide();
                detectInput = false;
            }
        });
        
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Keys.ESCAPE) {
                    fire(new ResumeEvent());
                    hide();
                    detectInput = false;
                    event.cancel();
                    return true;
                } else {
                    return super.keyDown(event, keycode);
                }
            }
        });
    
        root.row();
        table = new Table();
        root.add(table);
        
        textButton = new TextButton("", skin);
        textButton.setChecked(debugging);
        textButton.setText(textButton.isChecked()? "Debug: On" : "Debug: Off");
        table.add(textButton);
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                var textButton = (TextButton) actor;
                debugging = textButton.isChecked();
                textButton.setText(textButton.isChecked()? "Debug: On" : "Debug: Off");
            }
        });
        
        root.row();
        table = new Table();
        root.add(table);
        
        table.defaults().space(10);
        label = new Label("Player Move Speed", skin);
        table.add(label);
        
        var slider = new Slider(0, 2000, 1, false, skin);
        slider.setValue(MOVE_SPEED);
        table.add(slider).minWidth(500);
        
        var moveSpeedLabel = new Label(Float.toString(MOVE_SPEED), skin);
        table.add(moveSpeedLabel).minWidth(200);
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                var slider = (Slider) actor;
                MOVE_SPEED = slider.getValue();
                moveSpeedLabel.setText(Float.toString(slider.getValue()));
            }
        });
    
        root.row();
        table = new Table();
        root.add(table);
    
        table.defaults().space(10);
        label = new Label("Player Sprint Speed", skin);
        table.add(label);
    
        slider = new Slider(0, 2000, 1, false, skin);
        slider.setValue(SPRINT_SPEED);
        table.add(slider).minWidth(500);
    
        var sprintSpeedLabel = new Label(Float.toString(SPRINT_SPEED), skin);
        table.add(sprintSpeedLabel).minWidth(200);
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                var slider = (Slider) actor;
                SPRINT_SPEED = slider.getValue();
                sprintSpeedLabel.setText(Float.toString(slider.getValue()));
            }
        });
    
        root.row();
        table = new Table();
        root.add(table);
    
        table.defaults().space(10);
        label = new Label("Player Slide Speed", skin);
        table.add(label);
    
        slider = new Slider(0, 2000, 1, false, skin);
        slider.setValue(SLIDE_SPEED);
        table.add(slider).minWidth(500);
    
        var slideSpeedLabel = new Label(Float.toString(SLIDE_SPEED), skin);
        table.add(slideSpeedLabel).minWidth(200);
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                var slider = (Slider) actor;
                SLIDE_SPEED = slider.getValue();
                slideSpeedLabel.setText(Float.toString(slider.getValue()));
            }
        });
    
        root.row();
        table = new Table();
        root.add(table);
    
        table.defaults().space(10);
        label = new Label("Player Sprint Slide Speed", skin);
        table.add(label);
    
        slider = new Slider(0, 2000, 1, false, skin);
        slider.setValue(SLIDE_SPRINT_SPEED);
        table.add(slider).minWidth(500);
    
        var slideSprintSpeedLabel = new Label(Float.toString(SLIDE_SPRINT_SPEED), skin);
        table.add(slideSprintSpeedLabel).minWidth(200);
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                var slider = (Slider) actor;
                SLIDE_SPRINT_SPEED = slider.getValue();
                slideSprintSpeedLabel.setText(Float.toString(slider.getValue()));
            }
        });
    
        root.row();
        table = new Table();
        root.add(table);
    
        table.defaults().space(10);
        label = new Label("Player Slide Friction", skin);
        table.add(label);
    
        slider = new Slider(0, 2000, 1, false, skin);
        slider.setValue(SLIDE_FRICTION);
        table.add(slider).minWidth(500);
    
        var slideFrictionLabel = new Label(Float.toString(SLIDE_FRICTION), skin);
        table.add(slideFrictionLabel).minWidth(200);
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                var slider = (Slider) actor;
                SLIDE_FRICTION = slider.getValue();
                slideFrictionLabel.setText(Float.toString(slider.getValue()));
            }
        });
    
        root.row();
        table = new Table();
        root.add(table);
    
        table.defaults().space(10);
        label = new Label("Player Jump Speed", skin);
        table.add(label);
    
        slider = new Slider(0, 2000, 1, false, skin);
        slider.setValue(JUMP_SPEED);
        table.add(slider).minWidth(500);
    
        var jumpSpeedLabel = new Label(Float.toString(JUMP_SPEED), skin);
        table.add(jumpSpeedLabel).minWidth(200);
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                var slider = (Slider) actor;
                JUMP_SPEED = slider.getValue();
                jumpSpeedLabel.setText(Float.toString(slider.getValue()));
            }
        });
    
        root.row();
        table = new Table();
        root.add(table);
    
        table.defaults().space(10);
        label = new Label("Player Sprint Jump Speed", skin);
        table.add(label);
    
        slider = new Slider(0, 2000, 1, false, skin);
        slider.setValue(JUMP_SPRINT_SPEED);
        table.add(slider).minWidth(500);
    
        var jumpSprintSpeedLabel = new Label(Float.toString(JUMP_SPRINT_SPEED), skin);
        table.add(jumpSprintSpeedLabel).minWidth(200);
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                var slider = (Slider) actor;
                JUMP_SPEED = slider.getValue();
                jumpSprintSpeedLabel.setText(Float.toString(slider.getValue()));
            }
        });
    
        root.row();
        table = new Table();
        root.add(table);
    
        table.defaults().space(10);
        label = new Label("Player Jump Friction", skin);
        table.add(label);
    
        slider = new Slider(0, 2000, 1, false, skin);
        slider.setValue(JUMP_FRICTION);
        table.add(slider).minWidth(500);
    
        var jumpFrictionLabel = new Label(Float.toString(JUMP_FRICTION), skin);
        table.add(jumpFrictionLabel).minWidth(200);
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                var slider = (Slider) actor;
                JUMP_FRICTION = slider.getValue();
                jumpFrictionLabel.setText(Float.toString(slider.getValue()));
            }
        });
    
        root.row();
        table = new Table();
        root.add(table);
    
        table.defaults().space(10);
        label = new Label("Enemy Speed", skin);
        table.add(label);
    
        slider = new Slider(0, 2000, 1, false, skin);
        slider.setValue(ENEMY_SPEED);
        table.add(slider).minWidth(500);
    
        var enemySpeedLabel = new Label(Float.toString(ENEMY_SPEED), skin);
        table.add(enemySpeedLabel).minWidth(200);
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                var slider = (Slider) actor;
                ENEMY_SPEED = slider.getValue();
                enemySpeedLabel.setText(Float.toString(slider.getValue()));
            }
        });
    
        root.row();
        table = new Table();
        root.add(table);
    
        table.defaults().space(10);
        label = new Label("Player Hurt Speed", skin);
        table.add(label);
    
        slider = new Slider(0, 2000, 1, false, skin);
        slider.setValue(HURT_SPEED);
        table.add(slider).minWidth(500);
    
        var playerHurtSpeedLabel = new Label(Float.toString(HURT_SPEED), skin);
        table.add(playerHurtSpeedLabel).minWidth(200);
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                var slider = (Slider) actor;
                HURT_SPEED = slider.getValue();
                playerHurtSpeedLabel.setText(Float.toString(slider.getValue()));
            }
        });
    
        root.row();
        table = new Table();
        root.add(table);
    
        table.defaults().space(10);
        label = new Label("Player Hurt Friction", skin);
        table.add(label);
    
        slider = new Slider(0, 2000, 1, false, skin);
        slider.setValue(HURT_FRICTION);
        table.add(slider).minWidth(500);
    
        var playerHurtFrictionLabel = new Label(Float.toString(HURT_FRICTION), skin);
        table.add(playerHurtFrictionLabel).minWidth(200);
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                var slider = (Slider) actor;
                HURT_FRICTION = slider.getValue();
                playerHurtFrictionLabel.setText(Float.toString(slider.getValue()));
            }
        });
    }
    
    @Override
    public void act(float delta) {
        super.act(delta);
        
        if (detectInput) {
            if (jamScreen.isAnyBindingPressed()) {
                fire(new ResumeEvent());
                hide();
                detectInput = false;
            }
        }
    }
    
    public static class ResumeEvent extends Event {
    
    }
    
    public static class QuitEvent extends Event {
    
    }
    
    public static abstract class PauseListener implements EventListener {
        @Override
        public boolean handle(Event event) {
            if (event instanceof ResumeEvent) {
                resume();
                return true;
            } else if (event instanceof QuitEvent) {
                quit();
                return true;
            } else {
                return false;
            }
        }
        
        public abstract void resume();
        public abstract void quit();
    }
}
