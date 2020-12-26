package com.ray3k.template.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.RandomXS128;
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
import com.badlogic.gdx.utils.viewport.Viewport;
import com.crashinvaders.vfx.effects.ChainVfxEffect;
import com.crashinvaders.vfx.framebuffer.VfxFrameBuffer;
import com.ray3k.template.*;
import com.ray3k.template.OgmoReader.*;
import com.ray3k.template.entities.*;
import com.ray3k.template.entities.JumpeableEntity.*;
import com.ray3k.template.screens.DialogPause.*;
import com.ray3k.template.vfx.*;
import space.earlygrey.shapedrawer.ShapeDrawer;

import static com.ray3k.template.Core.*;

public class GameScreen extends JamScreen {
    public static GameScreen gameScreen;
    public static final Color BG_COLOR = new Color();
    public Stage stage;
    public static ShapeDrawer shapeDrawer;
    public boolean paused;
    private ChainVfxEffect vfxEffect;
    private Label fpsLabel;
    private VfxFrameBuffer vfxFrameBuffer;
    public static Viewport innerViewport;
    public static final int WORLD_WIDTH = 1024;
    public static final int WORLD_HEIGHT = 576;
    public static RandomXS128 random = new RandomXS128();
    public static ObjectMap<String, Triggerable> triggerables;
    
    @Override
    public void show() {
        super.show();
    
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
    
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT);
        camera = new OrthographicCamera();
        camera.zoom = .8f;
        innerViewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        innerViewport.update(WORLD_WIDTH, WORLD_HEIGHT);
    
        vfxFrameBuffer = new VfxFrameBuffer(Format.RGB888);
        vfxFrameBuffer.initialize(WORLD_WIDTH, WORLD_HEIGHT);
        vfxManager.resize(WORLD_WIDTH, WORLD_HEIGHT);
        
        entityController.clear();
        
        triggerables = new ObjectMap<>();
        
        var reader = new OgmoReader();
        reader.addListener(new OgmoAdapter() {
            int ordinal;
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
                    case "player":
                        var player = new PlayerEntity();
                        player.setPosition(x, y);
                        entityController.add(player);
                        player.updateCollisionBox();
                        break;
                    case "wall":
                        var wall = new WallEntity(width, height);
                        wall.setPosition(x, y);
                        entityController.add(wall);
                        wall.updateCollisionBox();
                        break;
                    case "pit":
                        var pit = new PitEntity(width, height);
                        pit.setPosition(x, y);
                        entityController.add(pit);
                        pit.updateCollisionBox();
                        break;
                    case "random-enemy":
                        var enemy = new RandomEnemyEntity(ordinal);
                        ordinal++;
                        enemy.setPosition(x, y);
                        entityController.add(enemy);
                        enemy.updateCollisionBox();
                        break;
                    case "dog-enemy":
                        var dog = new DogEnemyEntity(ordinal);
                        ordinal++;
                        dog.setPosition(x, y);
                        entityController.add(dog);
                        dog.updateCollisionBox();
                        break;
                    case "slideable-obstacle":
                        var slideable = new SlideableEntity(width, height);
                        slideable.setPosition(x, y);
                        entityController.add(slideable);
                        slideable.updateCollisionBox();
                        break;
                    case "jumpeable-obstacle":
                        JumpDirection jumpDirection;
                        var value = valuesMap.get("jump-direction").asString();
                        switch (value) {
                            case "north":
                                jumpDirection = JumpDirection.NORTH;
                                break;
                            case "south":
                                jumpDirection = JumpDirection.SOUTH;
                                break;
                            case "east":
                                jumpDirection = JumpDirection.EAST;
                                break;
                            case "west":
                                jumpDirection = JumpDirection.WEST;
                                break;
                            default:
                                jumpDirection = JumpDirection.ALL;
                                break;
                        }
                        var jumpeable = new JumpeableEntity(width, height, jumpDirection);
                        jumpeable.setPosition(x, y);
                        entityController.add(jumpeable);
                        jumpeable.updateCollisionBox();
                        break;
                    case "disappearing-obstacle":
                        var disappearing = new DisappearingWallEntity(width, height, valuesMap.get("triggerName").asString(), valuesMap.get("range").asInt());
                        disappearing.setPosition(x, y);
                        entityController.add(disappearing);
                        disappearing.updateCollisionBox();
                        triggerables.put(disappearing.triggerName, disappearing);
                        break;
                    case "button":
                        var button = new ButtonEntity(width, height, valuesMap.get("triggerName").asString());
                        button.setPosition(x, y);
                        entityController.add(button);
                        button.updateCollisionBox();
                        break;
                }
            }
    
            @Override
            public void decal(int centerX, int centerY, float scaleX, float scaleY, int rotation, String texture,
                              String folder) {
                if (!layer.equals("template")) {
                    var name = Utils.filePathNoExtension(texture);
                    var decalEntity = new DecalEntity(centerX, centerY, name);
                    entityController.add(decalEntity);
                }
            }
        });
        reader.readFile(Gdx.files.internal("levels/level-1.json"));
    }
    
    @Override
    public void act(float delta) {
        if (!paused) {
            entityController.act(delta);
            vfxManager.update(delta);
        }
        stage.act(delta);
        
        fpsLabel.setText(Gdx.graphics.getFramesPerSecond());
    }
    
    @Override
    public void draw(float delta) {
        batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.setColor(Color.WHITE);
        batch.begin();
        vfxManager.cleanUpBuffers();
        vfxManager.beginInputCapture();
        Gdx.gl.glClearColor(BG_COLOR.r, BG_COLOR.g, BG_COLOR.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        innerViewport.apply();
        batch.setProjectionMatrix(camera.combined);
        entityController.draw(paused ? 0 : delta);
        batch.end();
        vfxManager.endInputCapture();
        vfxManager.applyEffects();
        vfxManager.renderToFbo(vfxFrameBuffer);

        batch.begin();
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        Gdx.gl.glClearColor(Color.PURPLE.r, Color.PURPLE.g, Color.PURPLE.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        var region = new TextureRegion(vfxFrameBuffer.getTexture());
        region.flip(false, true);
        batch.draw(region, 0, 0);
        batch.end();
        batch.disableBlending();
        
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        stage.getViewport().apply();
        stage.draw();
    }
    
    @Override
    public void resize(int width, int height) {
        if (width + height != 0) {
            viewport.update(width, height, true);
            stage.getViewport().update(width, height, true);
        }
    }
    
    @Override
    public void dispose() {
        vfxEffect.dispose();
        vfxFrameBuffer.dispose();
    }
    
    @Override
    public void hide() {
        super.hide();
        vfxManager.removeAllEffects();
        vfxEffect.dispose();
        entityController.dispose();
        vfxFrameBuffer.dispose();
    }
}
