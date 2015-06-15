package defrac.sample.snowflake;

import defrac.ui.FrameBuilder;

/**
 *
 */
public final class Main {
  public static void main(String[] args) {
    FrameBuilder.
        forScreen(new SnowflakeSample()).
        backgroundColor(0xffffffff).
        width(1024).
        height(590).
        title("Snowflake Sample").
        show();
  }
}
