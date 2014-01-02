package defrac.sample.snowflake;

import defrac.display.Image;

import javax.annotation.Nonnull;
import java.util.Random;

public final class Snowflake extends Image {
  // By choosing a seeded random, everyone will get the same animation
  private static final Random RANDOM = new Random(0xdeadbeef);

  private static final float Y_OFFSET = 16.0f;
  private static final float MAX_WIDTH = 1024.0f;
  private static final float MAX_HEIGHT = 590.0f;

  private float originX;
  private float displacementX;
  private float velocityY;
  private float rotationSpeed;
  private float phaseInc;
  private float phase;

  public Snowflake(@Nonnull final TextureAtlas textureAtlas) {
    // Choose a random snowflake texture from the atlas
    super(
        textureAtlas.snowflakes[
            RANDOM.nextInt(textureAtlas.snowflakes.length)
        ]
    );

    // Make rotations look more natural by centering the registration point
    centerRegistrationPoint();
  }

  public void reset() {
    // Change the size of our flake
    final float scale = 0.0625f + 0.25f * random();
    scaleTo(scale, scale);

    // Change the transparency of the flake
    alpha(0.5f + 0.5f * random());

    // Choose random parameters for the life-time of the flake
    originX       = random() * MAX_WIDTH;
    displacementX = random() * 25.0f;
    velocityY     = random() * 0.5f + 0.125f;
    phaseInc      = random() * 0.001953125f;
    phase         = random();
    rotationSpeed = (random() - random()) * 0.015625f;

    // Random rotation
    rotation(random() * (float)Math.PI * 2.0f);

    // Move to initial position
    moveTo(originX, Y_OFFSET);
  }

  public void update() {
    // Calculate new y-position
    final float newY = y() + velocityY;

    if(newY > MAX_HEIGHT) {
      // If we died, we reset
      reset();
    } else {
      // Otherwise increment the phase and move to new position
      phase += phaseInc;

      if(phase > 1.0f) {
        --phase;
      }

      rotateBy(rotationSpeed);

      moveTo(
          originX + displacementX * (float)Math.sin(phase * Math.PI * 2.0),
          newY
      );
    }
  }

  @Nonnull
  public Snowflake moveToRandomPosition() {
    moveTo(random() * MAX_WIDTH, random() * MAX_HEIGHT + Y_OFFSET);
    return this;
  }

  private static float random() {
    return RANDOM.nextFloat();
  }
}
