package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class MyGdxGame extends ApplicationAdapter {
	/**
     * Distance between items (in pixels)
     */
    private static final float PAD = 20f;
    private static final String TEXTURE_PATH = "data/badlogic.jpg";

    private AssetManager assetManager;
    private SpriteBatch batch;
    private Stage stage;

    /**
     * All items available
     */
    private ArrayMap<Integer, Item> items;

    /**
     * Just items to be displayed on the screen
     */
    private ArrayMap<Integer, Item> itemsDisplayed;

    private Skin uiSkin;
    private Table scrollTable;
    private TextField textSearch;

    private String searchLastCriteria;

    private final Vector2 GAME_DEFAULT_RESOLUTION = new Vector2(1280f, 768f);

    @Override
    public void create() {
        batch = new SpriteBatch();
        stage = new Stage(new FitViewport(GAME_DEFAULT_RESOLUTION.x, GAME_DEFAULT_RESOLUTION.y));
        items = new ArrayMap<Integer, Item>();
        itemsDisplayed = new ArrayMap<Integer, Item>();

        // Defines the inputprocessor to be used to catch input actions (scroll
        // and text on the search TextField)
        Gdx.input.setInputProcessor(this.stage);

        assetManager = new AssetManager();
        assetManager.load("data/badlogic.jpg", Texture.class);
        assetManager.load("uiskin.atlas", TextureAtlas.class);
        assetManager.finishLoading();

        // Loads the ui's skin to be used on this example:
        uiSkin = new Skin(Gdx.files.internal("uiskin.json"), assetManager.get("uiskin.atlas", TextureAtlas.class));

        // Table used to position all the items:
        scrollTable = new Table();

        // Creates all item list:
        criaItens(333);

        // Creates the ui window:
        Window window = new Window("ScrollPane Tutorial", uiSkin);
        // The window shall fill the whole window:
        window.setFillParent(true);

        // This table groups the Search label and the TextField used to gather
        // the search criteria:
        Table table = new Table();
        table.add(new Label("Search", uiSkin)).spaceRight(10f);

        textSearch = new TextField("", uiSkin);

        // This event waits untilk the RETURN key is pressed to reorganize the
        // intens inside the grid:
        textSearch.addListener(new InputListener() {
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Keys.ENTER)
                    rearrangeTable();

                // Gdx.app.log("KEY", String.valueOf(keycode));

                return super.keyDown(event, keycode);
            }
        });

        table.add(textSearch).minSize(400f, 15f);

        // The search field will be aligned at the right of the window:
        window.add(table).right();
        window.row();

        rearrangeTable();

        // Prepares the scroll manager:
        ScrollPane scroll = new ScrollPane(scrollTable, uiSkin);

        // Only scroll vertically:
        scroll.setScrollingDisabled(true, false);

        window.add(scroll).fill().expand();

        InputListener stopTouchDown = new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                event.stop();
                return false;
            }
        };

        window.pack();
        stage.addActor(window);
    }

    @Override
    public void render() {

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f));

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Rendering start:
        batch.begin();
        stage.draw();
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        batch.getProjectionMatrix().setToOrtho2D(0f, 0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        stage.getViewport().update(width, height);

        rearrangeTable();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        // Don't forget to free unmanaged objects:

        batch.dispose();
        uiSkin.dispose();
        stage.dispose();
    }

    private final void criaItens(int total) {
        items.clear();
        itemsDisplayed.clear();

        for (int i = 0; i < total; i++) {
            Color novaCor = new Color(1f, 1f, 1f, 1f);
            items.put(i, new Item(i, "Item " + i, assetManager.get(TEXTURE_PATH, Texture.class), novaCor, uiSkin));
        }
    }

    /**
     * Recalculates the display items list (to fit the search criteria
     * specified)
     */
    private final void rearrangeTable() {
        scrollTable.clear();

        computeDisplayedItems(textSearch.getText());

        /**
         * The horizontal size of each image
         */
        int textureSizeX = assetManager.get(TEXTURE_PATH, Texture.class).getWidth();

        /**
         * Maximum items to be displayed on a single line
         */
        int itemsMaxPerLine = (int) (Gdx.graphics.getWidth() / textureSizeX);
        itemsMaxPerLine -= (int) (PAD * itemsMaxPerLine / textureSizeX) + 1;

        /**
         * How many lines are needed to render all items
         */
        int linesTotal = itemsDisplayed.size / itemsMaxPerLine;

        /**
         * Items to be rendered on the last line
         */
        int furtherRecords = itemsDisplayed.size % itemsMaxPerLine;

        int itemsCount = 0;
        for (int i = 0; i < linesTotal; i++) {

			/*
             * IMPORTANT: Here we use two's whiles, this happens because it is
			 * necessary to position the labels in a row while the images are
			 * positioned on the next line. This effect "gives the felling" to
			 * the user that labels in fact image's title, although technically
			 * there is no relationship between them.
			 */

            itemsCount = 0;

            // Posiciona os Label's:
            while (itemsCount < itemsMaxPerLine) {

                // Adds the item's Label:
                addText((i * itemsMaxPerLine) + itemsCount);

                itemsCount++;
            }

            // Finishes the line, preparing the table to insert new items to the
            // next line:
            scrollTable.row();

            itemsCount = 0;

            // Places TextField's:
            while (itemsCount < itemsMaxPerLine) {

                addImage((i * itemsMaxPerLine) + itemsCount);

                itemsCount++;
            }

            // Finishes the line, preparing the table to insert new items to the
            // next line:
            scrollTable.row();
        }

        // Place last line's labels:
        for (int i = 0; i < furtherRecords; i++) {
            addText((linesTotal * itemsMaxPerLine) + i);
        }
        scrollTable.row();

        // Places last line's TextFields:
        for (int i = 0; i < furtherRecords; i++) {
            addImage((linesTotal * itemsMaxPerLine) + i);
        }
    }

    private final void addText(int indice) {
        scrollTable.add(itemsDisplayed.getValueAt(indice).getDescription()).center();
    }

    public final void addImage(int indice) {
        float imagemTamanhoX = assetManager.get(TEXTURE_PATH, Texture.class).getWidth();
        float imagemTamanhoY = assetManager.get(TEXTURE_PATH, Texture.class).getHeight();

        //@formatter:off
        scrollTable.add(itemsDisplayed.getValueAt(indice).getImage())
                .minHeight(imagemTamanhoY)    //Image's minimal vertical size (prevents the image to be distort)
                .minWidth(imagemTamanhoX)    //Image's horizontal size (prevents the image to be distort)
                .spaceBottom(PAD)            //Padding between images below this one
                .spaceLeft(PAD)                //Padding between images to the left of this one
                .spaceRight(PAD)            //Padding between images to the right if this one
                .center();                    //Centers the image on the cell
        //@formatter:on
    }

    private final void computeDisplayedItems(String searchCriteria) {
        if ((searchCriteria == null && searchLastCriteria == null) || searchCriteria.equals(searchLastCriteria)) {
            if (items.size != itemsDisplayed.size) {
                itemsDisplayed.clear();
                itemsDisplayed.putAll(items);
            }
            return;
        }

        itemsDisplayed.clear();

        if (searchCriteria == null || searchCriteria.isEmpty()) {
            itemsDisplayed.putAll(items);
            return;
        }

        for (int i = 0; i < items.size; i++) {

            if (items.getValueAt(i).getDescription().getText().toString().contains(searchCriteria))
                itemsDisplayed.put(items.getKeyAt(i), items.getValueAt(i));
        }

        searchLastCriteria = searchCriteria;
    }
}