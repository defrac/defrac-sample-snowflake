package defrac.sample.snowflake;

import defrac.app.Bootstrap;
import defrac.app.GenericApp;
import defrac.concurrent.Future;
import defrac.display.*;
import defrac.event.EnterFrameEvent;
import defrac.event.EventListener;
import defrac.event.Events;
import defrac.lang.Procedure;
import defrac.resource.TextureDataResource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class SnowflakeSample extends GenericApp {
  // Number of snowflakes on screen
  private static final int NUM_SNOWFLAKES = 1250;

  public static void main(final String[] args) {
    Bootstrap.run(new SnowflakeSample());
  }

  @Nullable
  private DisplayObjectContainer container;

  @Override
  protected void onCreate() {
    // Change the background color to white
    backgroundColor(0xffffffff);

    // Access a resource from the "resources" directory.
    //
    // We want to load it as a TextureData, so we choose the TextureDataResource.
    // Other resource types can be loaded with InputStreamResource or BinaryResource.
    //
    // Note: If we enable WebP conversion (on by default) a runtime choice will be made
    //       to load the WebP version of this image
    final TextureDataResource textureDataResource =
        TextureDataResource.from("textures.png");

    // Start loading the resource, this will create a Future of TextureData
    // We could also attach listeners to textureDataResource (like textureDataResource.onComplete)
    // but for the sake of this example, we take a shortcut and act on the Future
    final Future<TextureData> dataFuture = textureDataResource.load();

    // Map the Future of TextureData to a Future of TextureAtlas
    // This computation will take place only if the computation succeeds
    final Future<TextureAtlas> textureAtlasFuture =
      dataFuture.map(TextureAtlas.DATA_TO_ATLAS);

    // The TextureAtlas has been loaded, continue with it.
    //
    // Note: Futures are computations that are either complete, or incomplete
    //       If a computation already completed, the listener will be called
    //       immediately.
    textureAtlasFuture.onSuccess(new Procedure<TextureAtlas>() {
      @Override
      public void apply(final TextureAtlas textureAtlas) {
        continueWithAtlas(textureAtlas);
      }
    });
  }

  private void continueWithAtlas(@Nonnull final TextureAtlas textureAtlas) {
    // Create a layer that will hold all the snowflakes.
    // We can give a hint about how many display objects
    // will be contained in this layer to save some memory
    final Layer snowflakeLayer = new Layer(NUM_SNOWFLAKES);
    snowflakeLayer.blendMode(BlendMode.SCREEN);

    // The setup for the stage is quite simple,
    // create a container (so we can easily reposition everything)
    // put the "vista" in the background, then the snowflakes in the
    // middle and the window layer on top
    container = new Layer(3);
    container.addChild(new Image(textureAtlas.vista).moveBy(0, -3));
    container.addChild(snowflakeLayer);
    container.addChild(new Image(textureAtlas.window));

    // Center the registration point of the container
    container.centerRegistrationPoint();

    // Add the container to the stage
    addChild(container);

    // Now create the actual snowflakes...
    final Snowflake[] snowflakes = new Snowflake[NUM_SNOWFLAKES];

    for(int i = 0; i < NUM_SNOWFLAKES; ++i) {
      final Snowflake snowflake = new Snowflake(textureAtlas);

      snowflake.reset();
      snowflakeLayer.addChild(
          snowflake
              .moveToRandomPosition());
      snowflakes[i] = snowflake;
    }

    // Center everything
    centerEverything();

    // Update all the snowflakes on every frame
    Events.onEnterFrame.add(new EventListener<EnterFrameEvent>() {
      @Override
      public void onEvent(final EnterFrameEvent event) {
        for (final Snowflake snowflake : snowflakes) {
          snowflake.update();
        }
      }
    });

    // On resize, center everything
    stage().addListener(new Stage.SimpleListener() {
      @Override
      public void onStageResize(@Nonnull Stage stage, float width, float height) {
        centerEverything();
      }
    });
  }

  private void centerEverything() {
    if(null == container) {
      return;
    }

    // The registration point of the container is already at its center
    // so we can safely move it to the middle of the stage
    container.moveTo(
        stage().width() * 0.5f,
        stage().height() * 0.5f);
  }
}
