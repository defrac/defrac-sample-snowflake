package defrac.sample.snowflake;

import defrac.display.Texture;
import defrac.display.TextureData;
import defrac.lang.Function;

import javax.annotation.Nonnull;

public final class TextureAtlas {
  // Function which returns a TextureAtlas for a given TextureData
  public static final Function<TextureData, TextureAtlas> DATA_TO_ATLAS = new Function<TextureData, TextureAtlas>()  {
    @Override
    @Nonnull
    public TextureAtlas apply(@Nonnull final TextureData textureData) {
      return new TextureAtlas(textureData);
    }
  };

  @Nonnull
  public final Texture[] snowflakes;

  @Nonnull
  public final Texture window;

  @Nonnull
  public final Texture vista;

  private TextureAtlas(@Nonnull final TextureData textureData) {
    // Create Texture instances from a given TextureData
    snowflakes = new Texture[8];
    window = new Texture(textureData, 0.0f, 128.0f, 1024.0f, 590.0f);
    vista = new Texture(textureData, 0.0f, 704.0f, 1024.0f, 586.0f);

    // We have 8 snowflakes in the first row of the texture, each is a 128x128 sprite
    for(int i = 0; i < 8; ++i) {
      snowflakes[i] = new Texture(textureData, i * 128.0f, 0.0f, 128.0f, 128.0f);
    }
  }
}
