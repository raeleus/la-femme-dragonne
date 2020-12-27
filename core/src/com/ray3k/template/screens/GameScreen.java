package com.ray3k.template.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.crashinvaders.vfx.effects.ChainVfxEffect;
import com.ray3k.template.*;
import com.ray3k.template.OgmoReader.*;
import com.ray3k.template.entities.*;
import com.ray3k.template.screens.DialogPause.*;
import com.ray3k.template.vfx.*;
import space.earlygrey.shapedrawer.ShapeDrawer;

import static com.ray3k.template.Core.*;
import static com.ray3k.template.Resources.*;

public class GameScreen extends JamScreen {
    public static GameScreen gameScreen;
    public static final Color BG_COLOR = new Color();
    public Stage stage;
    public static ShapeDrawer shapeDrawer;
    public boolean paused;
    private ChainVfxEffect vfxEffect;
    private Label fpsLabel;
    public static final int WORLD_WIDTH = 1024;
    public static final int WORLD_HEIGHT = 576;
    public static final Array<EnemyEntity> enemies = new Array<>();
    public static final Array<String> skinNames = new Array<>(new String[] {"adrien", "ali", "bear", "bobishere",
            "cackling", "cococore", "cosmicmenace", "doge", "dragun-queen", "groxar", "icefill", "james",
            "john", "login94", "lyze", "mgsx", "mr00anderson", "mrstahlfelge", "myke", "nitten", "payne",
            "peanut-panda", "pilzhere", "raeleus", "raseya", "red-sponge", "s64kbstudio", "santorno",
            "tecksup", "tettinger", "zaida", "zeroed"});
    public static Array<String> fluidList;
    public static String target;
    public static boolean firstRun;
    public static LaptopEntity laptop;
    public static boolean trans;
    public static int numberOfEnemies;
    public static int neededWins;
    public static PumpkinEntity pumpkinEntity;
    
    @Override
    public void show() {
        super.show();
    
        bgm_game.setVolume(bgm);
        bgm_game.play();
        
        trans = false;
        gameScreen = this;
        vfxEffect = new GlitchEffect();
        BG_COLOR.set(Color.valueOf("756e86"));
    
        paused = false;
    
        stage = new Stage(new ScreenViewport(), batch);
        
        var root = new Table();
        root.setFillParent(true);
        root.align(Align.bottomLeft);
        root.pad(10);
        stage.addActor(root);
        
        fpsLabel = new Label("test", skin);
        root.add(fpsLabel);
        
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (!paused && keycode == Keys.ESCAPE) {
                    paused = true;
                
                    DialogPause dialogPause = new DialogPause(GameScreen.this);
                    dialogPause.show(stage);
                    dialogPause.addListener(new PauseListener() {
                        @Override
                        public void resume() {
                            paused = false;
                        }
                    
                        @Override
                        public void quit() {
                            core.transition(new MenuScreen());
                        }
                    });
                }
                return super.keyDown(event, keycode);
            }
        });
    
        shapeDrawer = new ShapeDrawer(batch, skin.getRegion("white"));
        shapeDrawer.setPixelSize(.5f);
    
        InputMultiplexer inputMultiplexer = new InputMultiplexer(stage, this);
        Gdx.input.setInputProcessor(inputMultiplexer);
    
        camera = new OrthographicCamera();
        camera.zoom = 1f;
        camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        
        entityController.clear();
        enemies.clear();
        
        entityController.add(new CameraEntity());
        entityController.add(new ScopeEntity());
        
        var skins = new Array<>(fluidList);
        skins.setSize(Math.min(skins.size, numberOfEnemies));
        var spawns = new Array<Vector2>();
        skins.shuffle();
        target = skins.peek();
        var reader = new OgmoReader();
        reader.addListener(new OgmoAdapter() {
            String layer;
    
            @Override
            public void layer(String name, int gridCellWidth, int gridCellHeight, int offsetX, int offsetY) {
                layer = name;
            }
    
            @Override
            public void entity(String name, int id, int x, int y, int width, int height, boolean flippedX,
                               boolean flippedY, int originX, int originY, int rotation, Array<EntityNode> nodes,
                               ObjectMap<String, OgmoValue> valuesMap) {
                switch (name) {
                    case "spawn":
                        spawns.add(new Vector2(x, y));
                        break;
                    case "pumpkin":
                        pumpkinEntity = new PumpkinEntity();
                        pumpkinEntity.setPosition(x, y);
                        pumpkinEntity.width = width;
                        pumpkinEntity.height = height;
                        entityController.add(pumpkinEntity);
                        break;
                }
            }
    
            @Override
            public void decal(int centerX, int centerY, float scaleX, float scaleY, int rotation, String texture,
                              String folder) {

                var name = Utils.filePathNoExtension(texture);
                var decalEntity = new DecalEntity(centerX, centerY, scaleX, scaleY, name);
                if (name.equals("game/laptop")) {
                    laptop = new LaptopEntity();
                    laptop.setPosition(centerX, centerY);
                    laptop.depth = FOREGROUND_DEPTH;
                    entityController.add(laptop);
                } else switch (layer) {
                    case "background":
                        decalEntity.depth = BACKGROUND_DEPTH;
                        break;
                    case "midground":
                        decalEntity.depth = MIDGROUND_DEPTH;
                        break;
                    case "foreground":
                        decalEntity.depth = FOREGROUND_DEPTH;
                        break;
                }
                entityController.add(decalEntity);
            }
        });
        reader.readFile(Gdx.files.internal("levels/level.json"));
        
        spawns.shuffle();
        for (var spawn : spawns) {
            if (skins.size > 0) {
                var enemy = new EnemyEntity();
                enemy.setPosition(spawn.x, spawn.y - 25);
                enemy.depth = ENEMY_DEPTH;
                entityController.add(enemy);
                enemy.skeleton.setSkin(skins.pop());
                enemies.add(enemy);
            } else break;
        }
    }
    
    @Override
    public void act(float delta) {
        if (!paused) {
            entityController.act(delta);
            vfxManager.update(delta);
        }
        stage.act(delta);
        
        fpsLabel.setText(Gdx.graphics.getFramesPerSecond());
        if (trans) {
            core.transition(new IntroductionScreen());
        }
    }
    
    @Override
    public void draw(float delta) {
        batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.setColor(Color.WHITE);
        vfxManager.cleanUpBuffers();
        vfxManager.beginInputCapture();
        Gdx.gl.glClearColor(BG_COLOR.r, BG_COLOR.g, BG_COLOR.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        entityController.draw(paused ? 0 : delta);
        batch.end();
        vfxManager.endInputCapture();
        vfxManager.applyEffects();
        vfxManager.renderToScreen();
        
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        stage.getViewport().apply();
        stage.draw();
    }
    
    @Override
    public void resize(int width, int height) {
        if (width + height != 0) {
            vfxManager.resize(width, height);
            viewport.update(width, height);
            stage.getViewport().update(width, height, true);
        }
    }
    
    @Override
    public void dispose() {
        vfxEffect.dispose();
    }
    
    @Override
    public void hide() {
        super.hide();
        vfxManager.removeAllEffects();
        vfxEffect.dispose();
        entityController.dispose();
        bgm_game.stop();
    }
}
