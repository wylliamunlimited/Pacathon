package com.buaisociety.pacman;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.cjcrafter.neat.Client;
import com.cjcrafter.neat.Neat;
import com.cjcrafter.neat.genome.ConnectionGene;
import com.cjcrafter.neat.genome.NodeGene;
import com.buaisociety.pacman.util.Disposable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class PacmanNeatClient implements Disposable {

    private static final @NotNull Texture NODE_TEXTURE = createNodeTexture();
    private static final @NotNull TextureRegion CONNECTION_TEXTURE = createConnectionTexture();

    private final @NotNull Neat neat;
    private final @NotNull Client client;
    private @Nullable GameManager gameManager;
    private @Nullable CompletableFuture<Void> gameCompleteFuture;
    private boolean renderNetwork;

    public PacmanNeatClient(@NotNull Neat neat, @NotNull Client client) {
        this.neat = neat;
        this.client = client;
    }

    public @NotNull Neat getNeat() {
        return neat;
    }

    public @NotNull Client getClient() {
        return client;
    }

    public @NotNull GameManager getGameManager() {
        if (gameManager == null)
            throw new IllegalStateException("Game manager not set");
        return gameManager;
    }

    public void setGameManager(@NotNull GameManager gameManager) {
        this.gameManager = gameManager;
        this.gameCompleteFuture = new CompletableFuture<>();
    }

    public @NotNull CompletableFuture<Void> getGameCompleteFuture() {
        if (gameCompleteFuture == null)
            throw new IllegalStateException("Game manager not set");
        return gameCompleteFuture;
    }

    public boolean isRenderNetwork() {
        return renderNetwork;
    }

    public void setRenderNetwork(boolean renderNetwork) {
        this.renderNetwork = renderNetwork;
    }

    public void render(@NotNull SpriteBatch batch) {
        if (renderNetwork)
            renderNeuralNetwork(batch);
        else if (gameManager != null)
            gameManager.render(batch);
    }

    /**
     * Renders the neural network of the client.
     *
     * @param batch the sprite batch to render to
     */
    private void renderNeuralNetwork(@NotNull SpriteBatch batch) {
        Map<Integer, NodeGene> cache = new HashMap<>();
        for (NodeGene node : client.getGenome().getNodes()) {
            cache.put(node.getId(), node);
        }

        for (ConnectionGene connection : client.getGenome().getConnections()) {
            NodeGene from = cache.get(connection.getFromId());
            NodeGene to = cache.get(connection.getToId());
            renderConnection(batch, connection, from, to);
        }

        for (NodeGene node : client.getGenome().getNodes()) {
            renderNode(batch, node);
        }
    }

    private void renderNode(@NotNull SpriteBatch batch, @NotNull NodeGene node) {
        float nodeSize = 6;
        float x = node.getPosition().x() * 8 * 28 - nodeSize / 2;
        float y = node.getPosition().y() * 8 * 36 - nodeSize / 2;

        float activation = getClient().getCalculator().getActivation(node.getId());
        batch.setColor(activation, activation, activation, 1f);
        batch.draw(NODE_TEXTURE, x, y, nodeSize, nodeSize);
        batch.setColor(Color.WHITE);  // reset tint
    }

    private void renderConnection(@NotNull SpriteBatch batch, @NotNull ConnectionGene connection, @NotNull NodeGene from, @NotNull NodeGene to) {
        float x1 = from.getPosition().x() * 8 * 28;
        float y1 = from.getPosition().y() * 8 * 36;
        float x2 = to.getPosition().x() * 8 * 28;
        float y2 = to.getPosition().y() * 8 * 36;

        float angle = (float) Math.atan2(y2 - y1, x2 - x1) * 180f / (float) Math.PI;
        float distance = (float) Math.hypot(x2 - x1, y2 - y1);

        Color tint = connection.getWeight() > 0 ? Color.GREEN : Color.RED;
        if (!connection.getEnabled()) {
            tint = Color.GRAY;
        }

        float scale = Math.min(Math.abs(connection.getWeight()), 1f);
        batch.setColor(tint);
        batch.draw(CONNECTION_TEXTURE, x1, y1, 0, 0.5f, distance, 1f, 1f, scale, angle);
        batch.setColor(Color.WHITE);  // reset tint
    }

    public static @NotNull Texture createNodeTexture() {
        int diameter = 10;
        Pixmap pixmap = new Pixmap(diameter, diameter, Pixmap.Format.RGBA8888);
        pixmap.setColor(1f, 1f, 1f, 1f);
        pixmap.fillCircle(diameter / 2, diameter / 2, diameter / 2);
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    public static @NotNull TextureRegion createConnectionTexture() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(1f, 1f, 1f, 1f);
        pixmap.drawLine(0, 0, 1, 1);
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return new TextureRegion(texture);
    }

    /**
     * Disposes of the resources. Should be called when the object is deleted.
     */
    @Override
    public void dispose() {
        if (gameManager != null)
            gameManager.dispose();
    }
}
