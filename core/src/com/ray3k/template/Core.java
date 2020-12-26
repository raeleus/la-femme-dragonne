package com.ray3k.template;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.dongbat.jbump.CollisionFilter;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Response;
import com.dongbat.jbump.World;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.ray3k.template.AnimationStateDataLoader.*;
import com.ray3k.template.entities.*;
import com.ray3k.template.screens.*;
import com.ray3k.template.transitions.*;

import static com.ray3k.template.Resources.*;

public class Core extends JamGame {
    public static final String PROJECT_NAME = "libgdx15";
    public static Core core;
    public static Skin skin;
    public static SkeletonRenderer skeletonRenderer;
    public static ChangeListener sndChangeListener;
    public static EntityController entityController;
    public static World<Entity> world;
    public static CollisionFilter defaultCollisionFilter;
    public static CrossPlatformWorker crossPlatformWorker;
    public enum Binding {
        LEFT, RIGHT, UP, DOWN, SPRINT, JUMP, SLIDE;
    }
    public static float bgm;
    public static float sfx;
    public static Preferences preferences;
    public static int PLAYER_DEPTH = -100;
    public static int ENEMY_DEPTH = 50;
    public static int PROJECTILE_DEPTH = 10;
    public static int BACKGROUND_DEPTH = 200;
    public static int PROP_DEPTH = 190;
    public static boolean debugging;
    public static CollisionFilter nullFilter = (item, other) -> null;
    
    @Override
    public void create() {
        super.create();
        core = this;
        
        preferences = Gdx.app.getPreferences(PROJECT_NAME);
        
        bgm = preferences.getFloat("bgm", 1.0f);
        sfx = preferences.getFloat("sfx", 1.0f);
        
        setDefaultBindings();
        JamScreen.loadBindings();
        
        skeletonRenderer = new SkeletonRenderer();
        skeletonRenderer.setPremultipliedAlpha(true);
        
        entityController = new EntityController();
        
        world = new World<>(100);
        defaultCollisionFilter = (item, other) -> Response.bounce;
        
        sndChangeListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sfx_click.play();
            }
        };
        
        setScreen(new LoadScreen(() -> {
            loadResources(assetManager);
            skin = skin_skin;
        }));
        defaultTransition = Transitions.colorFade(Color.BLACK);
        defaultTransitionDuration = .5f;
    }
    
    @Override
    public void render() {
        super.render();
    }
    
    @Override
    public void loadAssets() {
        assetManager.setLoader(Skin.class, new SkinFreeTypeLoader(assetManager.getFileHandleResolver()));
        assetManager.setLoader(SkeletonData.class, new SkeletonDataLoader(assetManager.getFileHandleResolver()));
        assetManager.setLoader(AnimationStateData.class, new AnimationStateDataLoader(assetManager.getFileHandleResolver()));
        
        String textureAtlasPath = null;
        var fileHandle = Gdx.files.internal("textures.txt");
        if (fileHandle.exists()) for (String path : fileHandle.readString().split("\\n")) {
            assetManager.load(path, TextureAtlas.class);
            textureAtlasPath = path;
        }
        
        fileHandle = Gdx.files.internal("skin.txt");
        if (fileHandle.exists()) for (String path : fileHandle.readString().split("\\n")) {
            assetManager.load(path, Skin.class, new SkinParameter(textureAtlasPath));
        }
    
        fileHandle = Gdx.files.internal("bgm.txt");
        if (fileHandle.exists()) for (String path : fileHandle.readString().split("\\n")) {
            assetManager.load(path, Music.class);
        }
    
        fileHandle = Gdx.files.internal("sfx.txt");
        if (fileHandle.exists()) for (String path : fileHandle.readString().split("\\n")) {
            assetManager.load(path, Sound.class);
        }
    
        
        fileHandle = Gdx.files.internal("spine.txt");
        if (fileHandle.exists()) for (String path2 : fileHandle.readString().split("\\n")) {
            assetManager.load(path2 + "-animation", AnimationStateData.class, new AnimationStateDataParameter(path2, textureAtlasPath));
        }
    }
    
    public void setDefaultBindings() {
        JamScreen.addKeyBinding(Binding.LEFT, Input.Keys.A);
        JamScreen.addKeyBinding(Binding.RIGHT, Input.Keys.D);
        JamScreen.addKeyBinding(Binding.UP, Input.Keys.W);
        JamScreen.addKeyBinding(Binding.DOWN, Input.Keys.S);
        JamScreen.addKeyBinding(Binding.SPRINT, Keys.SHIFT_LEFT);
        JamScreen.addButtonBinding(Binding.SLIDE, Buttons.RIGHT);
        JamScreen.addKeyBinding(Binding.JUMP, Keys.SPACE);
    }
}
